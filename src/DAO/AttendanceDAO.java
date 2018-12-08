package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import ClassObject.Attendance;
import ClassObject.AttendanceListByLCode;
import ClassObject.AttendanceListBySID;
import ClassObject.AttendanceTimeList;
import ClassObject.GradeInfo;
import ClassObject.GradeInfoOfTerm;
import ClassObject.LectureDetail;
import ClassObject.StudentIDRequest;
import Util.OurTimes;

import java.sql.Date;

public class AttendanceDAO extends DAOBase {
	
	static Connection conn;
	static PreparedStatement pstmt;
	
	// ���� �����ϴ� DAO ����.
	LectureEvaluationDAO lectureEvaluationDAO;
	LectureDAO lectureDAO;
	AttendanceDAO attendanceDAO;
	GradeInfoDAO gradeInfoDAO;
	
	 // ȸ������ ��� enum
	public enum attendanceResult {
		SUCCESS,
		NOT_ENOUGH_SCORE,
		COLLISION_TIMETABLE 
	}
	
	// ������ ������ ���ÿ� jbdc ����.
	public AttendanceDAO() {
		super();
		lectureEvaluationDAO = new LectureEvaluationDAO();
		lectureDAO = new LectureDAO();
		attendanceDAO = new AttendanceDAO();
		gradeInfoDAO = new GradeInfoDAO();
	}
	
	/** �л��Ǽ��������ȸ 
	 * @param p_sid �й�
	 * @return �л��Ǽ������(������ȣ, �й��ڵ�, �����, ���������, ����б�, ���ǿ���, ���ǽ��۽ð�, �����������, ����)
	 * @throws SQLException DB����
	 * + ���� �б�(LegisterTerm) ���ϴ� �� �����ʿ�
	 * !DAO ��쿡 ���� ���� �߰� �ʿ�*/
	public List<AttendanceListBySID> getAttendanceListBySID(int p_sid) throws SQLException {
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
	 * ! ���� �ʿ�*/
	public attendanceResult addAttendance(int p_lcode, int p_sid) throws SQLException {
		LectureDetail lectureInfoList = lectureDAO.getLectureInfoByLCode(p_lcode);
		List<AttendanceListBySID> attendanceList = attendanceDAO.getAttendanceListBySID(p_sid);
		
		// ���ݱ��� ������ ����� �� ���� ���ϱ�
		int i = 0;
		double attendanceScore = 0;
		while(i < attendanceList.size()) {
			attendanceScore += attendanceList.get(i).getScore();
		}
		// �й��� ���� ���ϱ�
		double lectureScore = lectureInfoList.getScore();
		// �������غ��� �ʰ��ϸ�
		if(attendanceScore + lectureScore > 18) {
			return attendanceResult.NOT_ENOUGH_SCORE;
		}
		
		
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
}
