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
	
	// ���� �����ϴ� DAO ����.
	LectureEvaluationDAO lectureEvaluationDAO;
	LectureDAO lectureDAO;
	AttendanceDAO attendanceDAO;
	GradeInfoDAO gradeInfoDAO;
	
	 // ȸ������ ��� enum
	public enum attendanceResult {
		SUCCESS,
		NOT_ENOUGH_SCORE ,
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
	 * + ���� �б�(LegisterTerm) ���ϴ� �� �����ʿ�
	 * !DAO ��쿡 ���� ���� �߰� �ʿ�*/
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

					// �б� ������ üũ ( ���� �б� ���� �ƴϸ� )
					if(!OurTimes.isNowOnTerm()) {
						return null;
					}
					
					// ���� �ð� ���ϱ�
					Calendar cal = Calendar.getInstance();
					int year = cal.get( Calendar.YEAR );
					int month = cal.get( Calendar.MONTH ) + 1;
					
					int registerTerm = (year * 10) + month;
					pstmt.setInt(1, p_sid);
					pstmt.setInt(2, registerTerm);
					ResultSet rs = pstmt.executeQuery(); // ResultSet
					
					// ��ȸ��� �ƹ��͵� ����
					if(!rs.next()) {
						return null;	
					}
					
					rs.beforeFirst(); // ù ������ �̵�  -> �̰� �³� ?
					
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
					
				}catch(Exception e) {
				      e.printStackTrace();
				      
				}finally {
				      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
				      if(conn != null) try{conn.close();}catch(SQLException sqle){}
				}
			return null;
	}
	
	/** �����ð������ȸ 
	 * @param p_sid �й�
	 * @return �����ð����(�����, ���ǿ���, ���ǽ��۽ð�, ��������ð�)
	 * + �����б�(LegisterTerm) ���ϴ¹� ���� �ʿ�
	 * ! DAO ��쿡 ���� ��� �߰� �ʿ�*/
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


			// �б� ������ üũ ( ���� �б� ���� �ƴϸ� )
			if(!OurTimes.isNowOnTerm()) {
				return null;
			}

			// ���� �ð� ���ϱ�
			Calendar cal = Calendar.getInstance();
			int year = cal.get( Calendar.YEAR );
			int month = cal.get( Calendar.MONTH ) + 1;
			
			int registerTerm = (year * 10) + month;
			pstmt.setInt(1, p_sid);
			pstmt.setInt(2, registerTerm);
			ResultSet rs = pstmt.executeQuery(); // ResultSet
			
			// ��ȸ��� �ƹ��͵� ����
			if(!rs.next()) {
				return null;	
			}
			
			rs.beforeFirst(); // ù ������ �̵�  -> �̰� �³� ?
			
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
		
		}catch(Exception e) {
		      e.printStackTrace();
		      
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	return null;
	}
	
	/** ������û 
	 * @param p_lcode �й��ڵ�
	 * @param p_sid �й�
	 * @return ������û�������(enum)
	 * ! ���� �ʿ�*/
	public attendanceResult addAttendance(int p_lcode, int p_sid) {
		ArrayList<ArrayList> lectureInfoList = new LectureDAO().getLectureInfoByLCode(p_lcode);
		ArrayList<ArrayList> attendanceList = new AttendanceDAO().getAttendanceListBySID(p_sid);
		
	}
	
	/** �����򰡿��� 
	 * @param p_attendancecode ������ȣ
	 * @return �����򰡰��
	 * ! LectureEvaluationDAO ���� �ʿ� */
	public boolean getLectureEvaluationByCode(int p_attendancecode) {
		if(lectureEvaluationDAO.isLectureEvaluationExist(p_attendancecode))
			return true;
		return false;
	}
	
	/** ����������ȸ
	 * @param p_sid �й�
	 * @param p_semester �̼��б�
	 * @return �б⺰��������Ʈ(�����, ��������������, ����, ���������, ���������������)
	 * ! DAO ��쿡 ���� ��� ���� �ʿ�
	 * ! GradeInfoDAO ���� �ʿ�*/
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
			
			// ��ȸ��� �ƹ��͵� ����
			if(!rs.next()) {
				return null;	
			}
			
			rs.beforeFirst(); // ù ������ �̵�  -> �̰� �³� ?
			
			// ��� ��������
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
	
	/** �ش�йݼ��������ȸ 
	 * @param p_lcode �й��ڵ�
	 * @return �ش�йݼ������(����� ,������ȣ, ���������, �й�, �л��̸�)
	 * ! DAO ��쿡 ���� ���, ��ȯ�� ���� �� ����, SQL �ڵ� ���� �ʿ�
	 * ! ���� �ð� üũ �ʿ� ���� ??
	 * ! �л����̸�!*/
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
			
			// ��ȸ��� �ƹ��͵� ����
			if(!rs.next()) {	
				return null;	
			}
			
			rs.beforeFirst(); // ù ������ �̵�  -> �̰� �³� ?
			
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
		
		}catch(Exception e) {
		      e.printStackTrace();
		      
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		return arrayList;
	}
}
