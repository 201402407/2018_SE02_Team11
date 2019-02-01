package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import ClassObject.AttendanceListByLCode;
import ClassObject.AttendanceListBySID;
import ClassObject.AttendanceTimeList;
import ClassObject.GradeInfo;
import ClassObject.GradeInfoOfTerm;
import ClassObject.LectureDetail;
import Util.OurSchoolPolicy;
import Util.OurTimes;

public class AttendanceDAO extends DAOBase {
	
	static Connection conn;
	static PreparedStatement pstmt;
	
	// 같이 협업하는 DAO 선언.
	LectureEvaluationDAO lectureEvaluationDAO;
	LectureDAO lectureDAO;
	GradeInfoDAO gradeInfoDAO;
	
	 // 수강 결과 enum
	public enum attendanceResult {
		SUCCESS,
		NO_SUCH_STUDENT, //없는 학생
		UNABLE_ADD_ATTENDANCE, //선조건 - 수강신청 불가
		NOT_ENOUGH_NUM, //여석부족
		NOT_ENOUGH_SCORE, //잔여신청학점부족
		COLLISION_TIMETABLE  //시간충돌
	}
	
	// 생성자 생성과 동시에 jbdc 설정.
	public AttendanceDAO() {
		super();
		lectureEvaluationDAO = new LectureEvaluationDAO();
		lectureDAO = new LectureDAO();
		gradeInfoDAO = new GradeInfoDAO();
	}
	
	/** 학생의수강목록조회 
	 * @param p_sid 학번
	 * @return 학생의수강목록(수강번호, 분반코드, 과목명, 재수강여부, 등록학기, 강의요일, 강의시작시각, 강의종료시작, 학점).
	 * @throws SQLException DB오류
	 * + 현재 학기(LegisterTerm) 구하는 법 수정필요
	 * !DAO 경우에 따른 조건 추가 필요
	 * ! 학기중이 아니라도 조회는 가능함 (다만 수강신청을 안했다면 공백)
	 * */
	public List<AttendanceListBySID> getAttendanceListBySID(int p_sid) throws SQLException {
			List<AttendanceListBySID> arrayList = new ArrayList<AttendanceListBySID>();
			
			// 현재 학기를 구한다
			// 방학중이라면 근미래학기가 된다.
			int registerTerm;
			if(OurTimes.isNowOnTerm())
				registerTerm = OurTimes.currentTerm();
			else
				registerTerm = OurTimes.closestFutureTerm();
			
				try {
					String SQL = "SELECT A.attendanceNum, A.lectureCode, S.subjectName,"
							+ " A.isRetake, L.registerTerm, L.dayOfWeek, L.startTime, L.endTime, S.score"
							+ " FROM Attendance A"
							+ " LEFT JOIN Lecture L ON A.lectureCode = L.lectureCode"
							+ " LEFT JOIN Subject S ON L.subjectCode = S.subjectCode"
							+ " WHERE A.studentID = ? AND L.registerTerm = ?";
					conn = getConnection();
					pstmt = conn.prepareStatement(SQL);

					/*
					// 학기 중인지 체크 ( 만약 학기 중이 아니면 )
					if(!OurTimes.isNowOnTerm()) {
						return null;
					}
					*/
					
					pstmt.setInt(1, p_sid);
					pstmt.setInt(2, registerTerm);
					ResultSet rs = pstmt.executeQuery(); // ResultSet
					
					// 목록 꺼내오기
					while(rs.next()) {
						int rsAttNum = rs.getInt("attendanceNum");
						int rsLcode = rs.getInt("lectureCode");
						String rsSubjectName = rs.getString("subjectName");
						boolean isRetake = rs.getBoolean("isRetake");
						int rsRegisterTerm = rs.getInt("registerTerm");
						DayOfWeek rsDayOfWeek = OurTimes.intToDayOfWeek(rs.getInt("dayOfWeek"));
						LocalTime rsStartTime = OurTimes.sqlTimeToLocalTime(rs.getTime("startTime"));
						LocalTime rsEndTime = OurTimes.sqlTimeToLocalTime(rs.getTime("endTime"));
						double rsScore = rs.getDouble("score");
						
						AttendanceListBySID attendanceListBySID = new AttendanceListBySID(
								rsAttNum,
								rsLcode,
								rsSubjectName,
								isRetake,
								rsRegisterTerm,
								rsDayOfWeek,
								rsStartTime,
								rsEndTime,
								rsScore
								);
						arrayList.add(attendanceListBySID);
					}
					
					return arrayList;
					
				}catch(SQLException e) {
				      throw e;
				      
				}finally {
				      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
				      if(conn != null) try{conn.close();}catch(SQLException sqle){}
				}
	}
	
	/** 수강시간목록조회 
	 * @param p_sid 학번
	 * @return 수강시간목록(과목명, 강의요일, 강의시작시각, 강의종료시각)
	 * @throws SQLException DB오류
	 * + 현재학기(LegisterTerm) 구하는법 수정 필요
	 * ! DAO 경우에 따른 결과 추가 필요*/
	public List<AttendanceTimeList> getAttendanceTimeListBySID(int p_sid) throws SQLException {
		List<AttendanceTimeList> arrayList = new ArrayList<AttendanceTimeList>();
		try {
			String SQL = "SELECT S.subjectName, L.dayOfWeek, L.startTime, L.endTime"
					+ " FROM Attendance A"
					+ " WHERE A.studentID = ? AND L.registerTerm = ?"
					+ " LEFT JOIN Lecture L ON A.lectureCode = L.lectureCode"
					+ " LEFT JOIN Subject S ON L.subjectCode = S.subjectCode";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);


			// 학기 중인지 체크 ( 만약 학기 중이 아니면 )
			if(!OurTimes.isNowOnTerm()) {
				return null;
			}

			// 현재 학기 int로 구하기
			int registerTerm = OurTimes.currentTerm();
			pstmt.setInt(1, p_sid);
			pstmt.setInt(2, registerTerm);
			ResultSet rs = pstmt.executeQuery(); // ResultSet
			
			// 목록 꺼내오기
			while(rs.next()) {
				String rsSubjectName = rs.getString("subjectName");
				String rsDayOfWeek = rs.getString("dayOfWeek");
				String rsStartTime = rs.getString("startTime");
				String rsEndTime = rs.getString("endTime");
				
				AttendanceTimeList attendanceTimeList = new AttendanceTimeList(
						rsSubjectName,
						rsDayOfWeek,
						rsStartTime,
						rsEndTime);
				
				arrayList.add(attendanceTimeList);
			}
			
		return arrayList;
		
		}catch(SQLException e) {
		      throw e;
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	}
	
	/** 수강신청 
	 * @param p_lcode 분반코드
	 * @param p_sid 학번
	 * @return 수강신청성공결과(enum)
	 * @throws SQLException DB오류
	 * ! DAO 수정*/
	public attendanceResult addAttendance(int p_lcode, int p_sid) throws SQLException {
		
		StudentDAO studentDAO = new StudentDAO();
		// -해당하는 학번 존재 안함
		if ( ! studentDAO.isStudentExist(p_sid) )
			return attendanceResult.NO_SUCH_STUDENT;
		
		// -선조건 : 휴학중이거나 수강신청기간이 아니면 신청불가
		boolean isTimeOff = studentDAO.getTimeOffBySID(p_sid);
		if(!(!isTimeOff && OurTimes.isNowAbleToAddAttendance()))
			return attendanceResult.UNABLE_ADD_ATTENDANCE;
		
		LectureDetail targetLectureInfo = lectureDAO.getLectureInfoByLCode(p_lcode);
		List<AttendanceListBySID> attendanceList = getAttendanceListBySID(p_sid);
		int currentApplyTerm = OurTimes.closestFutureTerm();
		
		// - 여석존재 검사하기
		if(targetLectureInfo.getApplyNum() >= targetLectureInfo.getAllNum())
			return attendanceResult.NOT_ENOUGH_NUM;
		
		// - 잔여학점 검사하기
		// 지금까지 수강한 목록의 총 학점 구하기
		double attendanceScore = 0;
		for(AttendanceListBySID att : attendanceList) {
			attendanceScore += att.getScore();
		}
		// 분반의 학점 구하기
		double targetLectureScore = targetLectureInfo.getScore();
		// 일정기준보다 초과하면
		if(attendanceScore + targetLectureScore > OurSchoolPolicy.MAX_ATTENDANCE_SUM_SCORE) {
			return attendanceResult.NOT_ENOUGH_SCORE;
		}
		
		// -시간표 겹침 검사하기
		for(AttendanceListBySID att : attendanceList)
		{
			if( checkTimeCollision(att, targetLectureInfo) )
				return attendanceResult.COLLISION_TIMETABLE;
		}
		
		// 재수강여부를 검사한다.
		boolean isRetake = false;
		try
		{
			String sql = "SELECT * FROM Attendance A\r\n" + 
					"LEFT JOIN Lecture L\r\n" + 
					"ON A.lectureCode = L.lectureCode\r\n" + 
					"LEFT JOIN Subject S\r\n" + 
					"ON L.subjectCode = S.subjectCode\r\n" + 
					"WHERE A.studentID = ? AND S.SubjectName = ? AND L.registerTerm < ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, p_sid);
			pstmt.setString(2, targetLectureInfo.getSubjectName());
			pstmt.setInt(3, currentApplyTerm);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next())
				isRetake = true;
					
		} catch(SQLException sqle){
			throw sqle;
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		
		// 모든 조건 충족, 이제 추가를 한다
		try
		{
			String sql = "INSERT INTO Attendance (isRetake, studentID, lectureCode) "
					+ "VALUES (?, ?, ?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setBoolean(1, isRetake);
			pstmt.setInt(2, p_sid);
			pstmt.setInt(3, p_lcode);
			int result = pstmt.executeUpdate();
			if(result != 1)
				throw new SQLException("Affected Row is " + result);
					
		} catch(SQLException sqle){
			throw sqle;
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		// applyNum 증가
		try
		{
			String sql = "UPDATE Lecture\r\n" + 
					"SET applyNum = applyNum +1\r\n" + 
					"WHERE lectureCode = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, targetLectureInfo.getLectureCode());
			pstmt.executeUpdate();
			
			return attendanceResult.SUCCESS;
			
		} catch(SQLException sqle){
			throw sqle;
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		
	}
	private boolean checkTimeCollision(AttendanceListBySID att, LectureDetail target)
	{
		return
				(att.getDayOfWeek() == target.getDayOfWeek()) &&
				(att.getStartTime().compareTo(target.getEndTime()) < 0) &&
				(att.getEndTime().compareTo(target.getStartTime()) > 0);
	}
	
	/** 강의평가여부 
	 * @param p_attendancecode 수강번호
	 * @return 강의평가결과
	 * @throws SQLException DB오류
	 * ! LectureEvaluationDAO 구현 필요 */
	public boolean getLectureEvaluationByCode(int p_attendancecode) throws SQLException {
		try {
			if(lectureEvaluationDAO.isLectureEvaluationExist(p_attendancecode))
				return true;
		} catch (SQLException e) {
			throw e;
		}
		return false;
	}
	
	/** 성적정보조회
	 * @param p_sid 학번
	 * @param p_semester 이수학기
	 * @return 학기별성적리스트(과목명, 평점보여짐여부, 평점, 재수강여부, 이전수강당시평점);
	 * 성적부여되지 않은 수강은 제외된다.
	 * @throws SQLException DB오류
	 * ! DAO - 경우에 따른 결과 수정 필요
	 * ! DAO - SELECT문 가져오는 칼럼이 더 많아짐.
	 * ! DAO - 재수강 당시 성적을 가져오는 알고리즘
	 * ! DAO 한번 봐주세요!
	 */
	public ArrayList<GradeInfoOfTerm> getGradeInfo(int p_sid, int p_semester) throws SQLException {
		try {
			String SQL = "SELECT A.attendanceNum, A.isRetake, S.subjectCode, S.subjectName"
					+ " FROM Attendance A"
					+ " LEFT JOIN Lecture L"
					+ " ON A.lectureCode = L.lectureCode"
					+ " LEFT JOIN Subject S"
					+ " ON L.subjectCode = S.subjectCode"
					+ " WHERE A.studentID = ? AND L.registerTerm = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_sid);
			pstmt.setInt(2, p_semester);
			ResultSet rs = pstmt.executeQuery(); // ResultSet
			
			// 목록 꺼내오기 - 해당학기,해당학생의 수강코드 리스트에서.
			List<MyAttendance> myAttendanceList = new ArrayList<MyAttendance>(); //col: A.attendanceNum, A.isRetake, S.subjectName
			List<Integer> attNumList = new ArrayList<Integer>();  //col: A.attendanceNum
			while(rs.next()) {
				int rsAttendanceNum = rs.getInt("attendanceNum");
				boolean rsIsRetake = rs.getBoolean("isRetake");
				int rsSubjectCode = rs.getInt("subjectCode");
				String rsSubjectName = rs.getString("subjectName");
				myAttendanceList.add(
						new MyAttendance(rsAttendanceNum, rsIsRetake, rsSubjectCode, rsSubjectName)
						);
				attNumList.add(rsAttendanceNum);
			}
			
			List<GradeInfo> gradeInfoList = gradeInfoDAO.getGradeListByAttCodeList(attNumList);
			
			ArrayList<GradeInfoOfTerm> myGITList = new ArrayList<GradeInfoOfTerm>(); //학기별성적리스트 - 결과물
			// 각 수강에 해당하는 모든 성적정보에 대해...
			// gradeInfoList의 길이 = attNumList의 길이 = myAttendanceList의 길이
			for(int i = 0; i < myAttendanceList.size(); i++)
			{
				GradeInfo gi = gradeInfoList.get(i);
				// 성적부여되지 않은 것은 제한다.
				if(gi == null)
					continue;
				// 재수강인 경우, 전 수강 당시의 성적.평점을 가져온다.
				double gradeBefore = myGradeLastTerm(p_sid, p_semester, myAttendanceList.get(i).subjectCode);
				// 레코드 추가
				GradeInfoOfTerm myGIT = new GradeInfoOfTerm(
						gi.isVisibleGrade(), 
						gi.getGrade(), 
						myAttendanceList.get(i).isRetake, 
						gradeBefore, 
						myAttendanceList.get(i).subjectName
						);
				myGITList.add(myGIT);
			}
			
			return myGITList;
			
		}catch(SQLException sqle) {
		      throw sqle;
		      
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		
	}
	private class MyAttendance {
		@SuppressWarnings("unused")
		int attNum;
		boolean isRetake;
		int subjectCode;
		String subjectName;
		MyAttendance(int attNum, boolean isRetake, int subjectCode, String subjectName) {
			super();
			this.attNum = attNum;
			this.isRetake = isRetake;
			this.subjectCode = subjectCode;
			this.subjectName = subjectName;
		}
	}
	private double myGradeLastTerm(int p_sid, int p_semesterBefore, int p_subjcode) throws SQLException
	{
		PreparedStatement pstmt2 = null;
		try {
			String sql = "SELECT G.grade\r\n" + 
					"FROM GradeInfo G\r\n" + 
					"LEFT JOIN Attendance A\r\n" + 
					"ON G.attendanceNum = A.attendanceNum\r\n" + 
					"LEFT JOIN Lecture L\r\n" + 
					"ON A.lectureCode = L.lectureCode\r\n" + 
					"WHERE A.studentID = ? AND L.registerTerm < ? AND L.subjectCode = ?\r\n" + 
					"ORDER BY L.registerTerm DESC\r\n" + 
					"LIMIT 1;";
			pstmt2 = conn.prepareStatement(sql);
			pstmt2.setInt(1, p_sid);
			pstmt2.setInt(2, p_semesterBefore);
			pstmt2.setInt(3, p_subjcode);
			ResultSet rs = pstmt2.executeQuery();
			
			if(!rs.next())
				return -1;
			else
				return rs.getDouble("grade");
		}
		catch(SQLException sqle) {
		      throw sqle;
		      
		}finally {
		      if(pstmt2 != null) try{pstmt2.close();}catch(SQLException sqle){}
		}
	}
	
	
	
	/** 해당분반수강목록조회 
	 * @param p_lcode 분반코드
	 * @return 해당분반수강목록(과목명 ,수강번호, 재수강여부, 학번, 학생이름)
	 * @throws SQLException DB오류
	 * ! DAO 경우에 따른 결과, 반환값 수정 및 순서, SQL 코드 변경 필요
	 * ! 현재 시간 체크 필요 여부 ??
	 * ! 학생의이름!*/
	public List<AttendanceListByLCode> getAttendanceListbyLCode(int p_lcode) throws SQLException{
		List<AttendanceListByLCode> arrayList = new ArrayList<AttendanceListByLCode>();
		
		try {
			String SQL = "SELECT S.subjectName, A.attendanceNum, A.isRetake, A.studentID, Stu.studentName"
					+ " FROM Attendance A"
					+ " LEFT JOIN Lecture L ON A.lectureCode = L.lectureCode"
					+ " LEFT JOIN Student Stu ON A.studentID = Stu.studentID"
					+ " LEFT JOIN Subject S ON L.subjectCode = S.subjectCode"
					+ " WHERE L.lectureCode = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_lcode);
			ResultSet rs = pstmt.executeQuery(); // ResultSet
			
			// 목록 꺼내오기
			while(rs.next()) {
				String rsSubjectName = rs.getString("subjectName");
				int rsAttendanceNum = rs.getInt("attendanceNum");
				boolean rsRetake = rs.getBoolean("isRetake");
				int rsStudentID = rs.getInt("studentID");
				String rsStudentName = rs.getString("studentName");
				
				AttendanceListByLCode attendanceListByLCode = new AttendanceListByLCode(
						rsSubjectName,
						rsAttendanceNum,
						rsRetake,
						rsStudentID,
						rsStudentName
				);
				
				arrayList.add(attendanceListByLCode);
			}
			return arrayList;
		}catch(SQLException e) {
		      throw e;
		      
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	}
	
	/**
	 * 강의평가
	 * @param p_attnum 수강번호
	 * @param p_evaltext 강의평가 텍스트, 1자 이상 255자 이하여야 한다.
	 * @throws SQLException DB오류
	 * @return 성공시 true, p_evaltext의 선조건 실패 혹은 추가실패시 fail
	 * ! DAO명세서에 없는 물건이므로 추가요망
	 */
	public boolean doLectureEvaluation(int p_attnum, String p_evaltext) throws SQLException
	{
		if(p_evaltext.length() < 1 || p_evaltext.length() > 255)
			return false;
		return lectureEvaluationDAO.writeLectureEvaluation(p_attnum, p_evaltext);
	}
}
