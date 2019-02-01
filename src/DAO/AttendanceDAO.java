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
	
	// ���� �����ϴ� DAO ����.
	LectureEvaluationDAO lectureEvaluationDAO;
	LectureDAO lectureDAO;
	GradeInfoDAO gradeInfoDAO;
	
	 // ���� ��� enum
	public enum attendanceResult {
		SUCCESS,
		NO_SUCH_STUDENT, //���� �л�
		UNABLE_ADD_ATTENDANCE, //������ - ������û �Ұ�
		NOT_ENOUGH_NUM, //��������
		NOT_ENOUGH_SCORE, //�ܿ���û��������
		COLLISION_TIMETABLE  //�ð��浹
	}
	
	// ������ ������ ���ÿ� jbdc ����.
	public AttendanceDAO() {
		super();
		lectureEvaluationDAO = new LectureEvaluationDAO();
		lectureDAO = new LectureDAO();
		gradeInfoDAO = new GradeInfoDAO();
	}
	
	/** �л��Ǽ��������ȸ 
	 * @param p_sid �й�
	 * @return �л��Ǽ������(������ȣ, �й��ڵ�, �����, ���������, ����б�, ���ǿ���, ���ǽ��۽ð�, �����������, ����).
	 * @throws SQLException DB����
	 * + ���� �б�(LegisterTerm) ���ϴ� �� �����ʿ�
	 * !DAO ��쿡 ���� ���� �߰� �ʿ�
	 * ! �б����� �ƴ϶� ��ȸ�� ������ (�ٸ� ������û�� ���ߴٸ� ����)
	 * */
	public List<AttendanceListBySID> getAttendanceListBySID(int p_sid) throws SQLException {
			List<AttendanceListBySID> arrayList = new ArrayList<AttendanceListBySID>();
			
			// ���� �б⸦ ���Ѵ�
			// �������̶�� �ٹ̷��бⰡ �ȴ�.
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
					// �б� ������ üũ ( ���� �б� ���� �ƴϸ� )
					if(!OurTimes.isNowOnTerm()) {
						return null;
					}
					*/
					
					pstmt.setInt(1, p_sid);
					pstmt.setInt(2, registerTerm);
					ResultSet rs = pstmt.executeQuery(); // ResultSet
					
					// ��� ��������
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
	
	/** �����ð������ȸ 
	 * @param p_sid �й�
	 * @return �����ð����(�����, ���ǿ���, ���ǽ��۽ð�, ��������ð�)
	 * @throws SQLException DB����
	 * + �����б�(LegisterTerm) ���ϴ¹� ���� �ʿ�
	 * ! DAO ��쿡 ���� ��� �߰� �ʿ�*/
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


			// �б� ������ üũ ( ���� �б� ���� �ƴϸ� )
			if(!OurTimes.isNowOnTerm()) {
				return null;
			}

			// ���� �б� int�� ���ϱ�
			int registerTerm = OurTimes.currentTerm();
			pstmt.setInt(1, p_sid);
			pstmt.setInt(2, registerTerm);
			ResultSet rs = pstmt.executeQuery(); // ResultSet
			
			// ��� ��������
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
	
	/** ������û 
	 * @param p_lcode �й��ڵ�
	 * @param p_sid �й�
	 * @return ������û�������(enum)
	 * @throws SQLException DB����
	 * ! DAO ����*/
	public attendanceResult addAttendance(int p_lcode, int p_sid) throws SQLException {
		
		StudentDAO studentDAO = new StudentDAO();
		// -�ش��ϴ� �й� ���� ����
		if ( ! studentDAO.isStudentExist(p_sid) )
			return attendanceResult.NO_SUCH_STUDENT;
		
		// -������ : �������̰ų� ������û�Ⱓ�� �ƴϸ� ��û�Ұ�
		boolean isTimeOff = studentDAO.getTimeOffBySID(p_sid);
		if(!(!isTimeOff && OurTimes.isNowAbleToAddAttendance()))
			return attendanceResult.UNABLE_ADD_ATTENDANCE;
		
		LectureDetail targetLectureInfo = lectureDAO.getLectureInfoByLCode(p_lcode);
		List<AttendanceListBySID> attendanceList = getAttendanceListBySID(p_sid);
		int currentApplyTerm = OurTimes.closestFutureTerm();
		
		// - �������� �˻��ϱ�
		if(targetLectureInfo.getApplyNum() >= targetLectureInfo.getAllNum())
			return attendanceResult.NOT_ENOUGH_NUM;
		
		// - �ܿ����� �˻��ϱ�
		// ���ݱ��� ������ ����� �� ���� ���ϱ�
		double attendanceScore = 0;
		for(AttendanceListBySID att : attendanceList) {
			attendanceScore += att.getScore();
		}
		// �й��� ���� ���ϱ�
		double targetLectureScore = targetLectureInfo.getScore();
		// �������غ��� �ʰ��ϸ�
		if(attendanceScore + targetLectureScore > OurSchoolPolicy.MAX_ATTENDANCE_SUM_SCORE) {
			return attendanceResult.NOT_ENOUGH_SCORE;
		}
		
		// -�ð�ǥ ��ħ �˻��ϱ�
		for(AttendanceListBySID att : attendanceList)
		{
			if( checkTimeCollision(att, targetLectureInfo) )
				return attendanceResult.COLLISION_TIMETABLE;
		}
		
		// ��������θ� �˻��Ѵ�.
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
		
		// ��� ���� ����, ���� �߰��� �Ѵ�
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
		// applyNum ����
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
	
	/** �����򰡿��� 
	 * @param p_attendancecode ������ȣ
	 * @return �����򰡰��
	 * @throws SQLException DB����
	 * ! LectureEvaluationDAO ���� �ʿ� */
	public boolean getLectureEvaluationByCode(int p_attendancecode) throws SQLException {
		try {
			if(lectureEvaluationDAO.isLectureEvaluationExist(p_attendancecode))
				return true;
		} catch (SQLException e) {
			throw e;
		}
		return false;
	}
	
	/** ����������ȸ
	 * @param p_sid �й�
	 * @param p_semester �̼��б�
	 * @return �б⺰��������Ʈ(�����, ��������������, ����, ���������, ���������������);
	 * �����ο����� ���� ������ ���ܵȴ�.
	 * @throws SQLException DB����
	 * ! DAO - ��쿡 ���� ��� ���� �ʿ�
	 * ! DAO - SELECT�� �������� Į���� �� ������.
	 * ! DAO - ����� ��� ������ �������� �˰���
	 * ! DAO �ѹ� ���ּ���!
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
			
			// ��� �������� - �ش��б�,�ش��л��� �����ڵ� ����Ʈ����.
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
			
			ArrayList<GradeInfoOfTerm> myGITList = new ArrayList<GradeInfoOfTerm>(); //�б⺰��������Ʈ - �����
			// �� ������ �ش��ϴ� ��� ���������� ����...
			// gradeInfoList�� ���� = attNumList�� ���� = myAttendanceList�� ����
			for(int i = 0; i < myAttendanceList.size(); i++)
			{
				GradeInfo gi = gradeInfoList.get(i);
				// �����ο����� ���� ���� ���Ѵ�.
				if(gi == null)
					continue;
				// ������� ���, �� ���� ����� ����.������ �����´�.
				double gradeBefore = myGradeLastTerm(p_sid, p_semester, myAttendanceList.get(i).subjectCode);
				// ���ڵ� �߰�
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
	
	
	
	/** �ش�йݼ��������ȸ 
	 * @param p_lcode �й��ڵ�
	 * @return �ش�йݼ������(����� ,������ȣ, ���������, �й�, �л��̸�)
	 * @throws SQLException DB����
	 * ! DAO ��쿡 ���� ���, ��ȯ�� ���� �� ����, SQL �ڵ� ���� �ʿ�
	 * ! ���� �ð� üũ �ʿ� ���� ??
	 * ! �л����̸�!*/
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
			
			// ��� ��������
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
	 * ������
	 * @param p_attnum ������ȣ
	 * @param p_evaltext ������ �ؽ�Ʈ, 1�� �̻� 255�� ���Ͽ��� �Ѵ�.
	 * @throws SQLException DB����
	 * @return ������ true, p_evaltext�� ������ ���� Ȥ�� �߰����н� fail
	 * ! DAO������ ���� �����̹Ƿ� �߰����
	 */
	public boolean doLectureEvaluation(int p_attnum, String p_evaltext) throws SQLException
	{
		if(p_evaltext.length() < 1 || p_evaltext.length() > 255)
			return false;
		return lectureEvaluationDAO.writeLectureEvaluation(p_attnum, p_evaltext);
	}
}
