package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ClassObject.Attendance;
import ClassObject.AttendanceListByLCode;
import ClassObject.AttendanceListBySID;
import ClassObject.AttendanceTimeList;
import ClassObject.StudentIDRequest;
import Util.OurTimes;

import java.sql.Date;

public class AttendanceDAO extends DAOBase {
	
	static Connection conn;
	static PreparedStatement pstmt;
	
	// 같이 협업하는 DAO 선언.
	LectureEvaluationDAO lectureEvaluationDAO;
	LectureDAO lectureDAO;
	AttendanceDAO attendanceDAO;
	GradeInfoDAO gradeInfoDAO;
	
	 // 회원가입 결과 enum
	public enum attendanceResult {
		SUCCESS,
		NOT_ENOUGH_SCORE ,
		COLLISION_TIMETABLE 
	}
	
	// 생성자 생성과 동시에 jbdc 설정.
	public AttendanceDAO() {
		super();
		lectureEvaluationDAO = new LectureEvaluationDAO();
		lectureDAO = new LectureDAO();
		attendanceDAO = new AttendanceDAO();
		gradeInfoDAO = new GradeInfoDAO();
	}
	
	/** 학생의수강목록조회 
	 * @param p_sid 학번
	 * @return 학생의수강목록(수강번호, 분반코드, 과목명, 재수강여부, 등록학기, 강의요일, 강의시작시각, 강의종료시작, 학점)
	 * + 현재 학기(LegisterTerm) 구하는 법 수정필요
	 * !DAO 경우에 따른 조건 추가 필요*/
	public List<AttendanceListBySID> getAttendanceListBySID(int p_sid) {
			List<AttendanceListBySID> arrayList = new ArrayList<AttendanceListBySID>();
			
				try {
					String SQL = "SELECT A.attendanceNum, A.lectureCode, S.subjectName,"
							+ " A.isRetake, L.registerTerm, L.dayOfWeek, L.startTime, L.endTime, S.score"
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
					
					// 현재 시간 구하기
					Calendar cal = Calendar.getInstance();
					int year = cal.get( Calendar.YEAR );
					int month = cal.get( Calendar.MONTH ) + 1;
					
					int registerTerm = (year * 10) + month;
					pstmt.setInt(1, p_sid);
					pstmt.setInt(2, registerTerm);
					ResultSet rs = pstmt.executeQuery(); // ResultSet
					
					// 조회결과 아무것도 없음
					if(!rs.next()) {
						return null;	
					}
					
					rs.beforeFirst(); // 첫 행으로 이동  -> 이게 맞나 ?
					
					// 목록 꺼내오기
					while(rs.next()) {
						int rsReqSIDnum = rs.getInt("attendanceNum");
						int rsLcode = rs.getInt("lectureCode");
						String rsSubjectName = rs.getString("subjectName");
						boolean isRetake = rs.getBoolean("isRetake");
						int rsRegisterTerm = rs.getInt("registerTerm");
						String rsDayOfWeek = rs.getString("dayOfWeek");
						String rsStartTime = rs.getString("startTime");
						String rsEndTime = rs.getString("endTime");
						double rsScore = rs.getDouble("score");
						
						AttendanceListBySID attendanceListBySID = new AttendanceListBySID(
								rsReqSIDnum,
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
					
				}catch(Exception e) {
				      e.printStackTrace();
				      
				}finally {
				      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
				      if(conn != null) try{conn.close();}catch(SQLException sqle){}
				}
			return null;
	}
	
	/** 수강시간목록조회 
	 * @param p_sid 학번
	 * @return 수강시간목록(과목명, 강의요일, 강의시작시각, 강의종료시각)
	 * + 현재학기(LegisterTerm) 구하는법 수정 필요
	 * ! DAO 경우에 따른 결과 추가 필요*/
	public List<AttendanceTimeList> getAttendanceTimeListBySID(int p_sid) {
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

			// 현재 시간 구하기
			Calendar cal = Calendar.getInstance();
			int year = cal.get( Calendar.YEAR );
			int month = cal.get( Calendar.MONTH ) + 1;
			
			int registerTerm = (year * 10) + month;
			pstmt.setInt(1, p_sid);
			pstmt.setInt(2, registerTerm);
			ResultSet rs = pstmt.executeQuery(); // ResultSet
			
			// 조회결과 아무것도 없음
			if(!rs.next()) {
				return null;	
			}
			
			rs.beforeFirst(); // 첫 행으로 이동  -> 이게 맞나 ?
			
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
		
		}catch(Exception e) {
		      e.printStackTrace();
		      
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	return null;
	}
	
	/** 수강신청 
	 * @param p_lcode 분반코드
	 * @param p_sid 학번
	 * @return 수강신청성공결과(enum)
	 * ! 구현 필요*/
	public attendanceResult addAttendance(int p_lcode, int p_sid) {
		ArrayList<ArrayList> lectureInfoList = new LectureDAO().getLectureInfoByLCode(p_lcode);
		ArrayList<ArrayList> attendanceList = new AttendanceDAO().getAttendanceListBySID(p_sid);
		
	}
	
	/** 강의평가여부 
	 * @param p_attendancecode 수강번호
	 * @return 강의평가결과
	 * ! LectureEvaluationDAO 구현 필요 */
	public boolean getLectureEvaluationByCode(int p_attendancecode) {
		if(lectureEvaluationDAO.isLectureEvaluationExist(p_attendancecode))
			return true;
		return false;
	}
	
	/** 성적정보조회
	 * @param p_sid 학번
	 * @param p_semester 이수학기
	 * @return 학기별성적리스트(과목명, 평점보여짐여부, 평점, 재수강여부, 이전수강당시평점)
	 * ! DAO 경우에 따른 결과 수정 필요
	 * ! GradeInfoDAO 구현 필요*/
	public ArrayList<Integer> getGradeInfo(int p_sid, int p_semester) {
		ArrayList<Integer> attlist = new ArrayList<Integer>();
		try {
			String SQL = "SELECT A.attendanceNum"
					+ " FROM Attendance A"
					+ " WHERE A.studentID = ? AND L.registerTerm = ?"
					+ " LEFT JOIN Lecture L"
					+ " ON L.lectureCode = A.lectureCode";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_sid);
			pstmt.setInt(2, p_semester);
			ResultSet rs = pstmt.executeQuery(); // ResultSet
			
			// 조회결과 아무것도 없음
			if(!rs.next()) {
				return null;	
			}
			
			rs.beforeFirst(); // 첫 행으로 이동  -> 이게 맞나 ?
			
			// 목록 꺼내오기
			while(rs.next()) {
				int rsAttendanceNum = rs.getInt("attendanceNum");
				attlist.add(rsAttendanceNum);
			}
			
		}catch(Exception e) {
		      e.printStackTrace();
		      
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		
		GradeInfoDAO.getGradeListByAttCodeList(attlist);
	}
	
	/** 해당분반수강목록조회 
	 * @param p_lcode 분반코드
	 * @return 해당분반수강목록(과목명 ,수강번호, 재수강여부, 학번, 학생이름)
	 * ! DAO 경우에 따른 결과, 반환값 수정 및 순서, SQL 코드 변경 필요
	 * ! 현재 시간 체크 필요 여부 ??
	 * ! 학생의이름!*/
	public List<AttendanceListByLCode> getAttendanceListbyLCode(int p_lcode) {
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
			
			// 조회결과 아무것도 없음
			if(!rs.next()) {	
				return null;	
			}
			
			rs.beforeFirst(); // 첫 행으로 이동  -> 이게 맞나 ?
			
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
		
		}catch(Exception e) {
		      e.printStackTrace();
		      
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		return arrayList;
	}
}
