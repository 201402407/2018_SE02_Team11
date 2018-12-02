package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import ClassObject.Attendance;
import ClassObject.StudentIDRequest;

import java.sql.Date;

public class AttendanceDAO {
	static String jdbcUrl; 
	static String dbId;
	static String dbPwd;
	
	static Connection conn;
	static PreparedStatement pstmt;
	
	private static String getJdbcUrl() {
		return jdbcUrl;
	}

	private void setJdbcUrl(String jdbcUrl) {
		AccountDAO.jdbcUrl = jdbcUrl;
	}

	private static String getDbId() {
		return dbId;
	}

	private void setDbId(String dbId) {
		AccountDAO.dbId = dbId;
	}

	private static String getDbPwd() {
		return dbPwd;
	}

	private void setDbPwd(String dbPwd) {
		AccountDAO.dbPwd = dbPwd;
	}

	public enum attendanceResult { // ȸ������ ��� enum
		SUCCESS,
		NOT_ENOUGH_SCORE ,
		COLLISION_TIMETABLE 
	}
	
	// ������ ������ ���ÿ� jbdc ����.
	public AttendanceDAO() {
		setJdbcUrl("jdbc:mysql://127.0.0.1:3306/SE02?autoReconnect=true"); // DB �����ּ�
		setDbId("SE02_11");
		setDbPwd("2018");
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//conn = DriverManager.getConnection(this.jdbcUrl, this.dbId, this.dbPass);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* �л��Ǽ��������ȸ */
	public ArrayList<ArrayList> getAttendanceListBySID(int p_sid) {
			ArrayList<ArrayList> arrayList = new ArrayList<ArrayList>();
			
				try {
					String SQL = "SELECT A.attendanceNum, A.lectureCode, S.subjectName,"
							+ " A.isRetake, L.registerTerm, L.dayOfWeek, L.startTime, L.endTime, S.score"
							+ " FROM Attendance A"
							+ " WHERE A.studentID = ? AND L.registerTerm = ?"
							+ " LEFT JOIN Lecture L ON A.lectureCode = L.lectureCode"
							+ " LEFT JOIN Subject S ON L.subjectCode = S.subjectCode";
					conn = DriverManager.getConnection(getJdbcUrl(), getDbId(), getDbPwd());
					pstmt = conn.prepareStatement(SQL);

					// ���� �ð� ���ϱ�
					Calendar cal = Calendar.getInstance();
					int year = cal.get( Calendar.YEAR );
					int month = cal.get( Calendar.MONTH ) + 1;
					// ���� �ش� ���� �����̸� null ����.
					if(month == 1 || month == 2 || month == 7 || month == 8) {
						return null;
					}
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
						ArrayList temp = new ArrayList<>();
						temp.add(rsReqSIDnum);
						temp.add(rsLcode);
						temp.add(rsSubjectName);
						temp.add(isRetake);
						temp.add(rsRegisterTerm);
						temp.add(rsDayOfWeek);
						temp.add(rsStartTime);
						temp.add(rsEndTime);
						temp.add(rsScore);
						
						arrayList.add(temp);
					}
					
				}catch(Exception e) {
				      e.printStackTrace();
				      
				}finally {
				      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
				      if(conn != null) try{conn.close();}catch(SQLException sqle){}
				}
			return arrayList;
	}
	
	/* �����ð������ȸ */
	public ArrayList<ArrayList> getAttendanceTimeListBySID(int p_sid) {
		ArrayList<ArrayList> arrayList = new ArrayList<ArrayList>();
		try {
			String SQL = "SELECT S.subjectName, L.dayOfWeek, L.startTime, L.endTime"
					+ " FROM Attendance A"
					+ " WHERE A.studentID = ? AND L.registerTerm = ?"
					+ " LEFT JOIN Lecture L ON A.lectureCode = L.lectureCode"
					+ " LEFT JOIN Subject S ON L.subjectCode = S.subjectCode";
			conn = DriverManager.getConnection(getJdbcUrl(), getDbId(), getDbPwd());
			pstmt = conn.prepareStatement(SQL);

			// ���� �ð� ���ϱ�
			Calendar cal = Calendar.getInstance();
			int year = cal.get( Calendar.YEAR );
			int month = cal.get( Calendar.MONTH ) + 1;
			// ���� �ش� ���� �����̸� null ����.
			if(month == 1 || month == 2 || month == 7 || month == 8) {
				return null;
			}
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
				
				ArrayList temp = new ArrayList<>();
			
				temp.add(rsSubjectName);
				temp.add(rsDayOfWeek);
				temp.add(rsStartTime);
				temp.add(rsEndTime);
				
				arrayList.add(temp);
			}
			
		}catch(Exception e) {
		      e.printStackTrace();
		      
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	return arrayList;
	}
	
	/* ������û */
	public attendanceResult addAttendance(int p_lcode, int p_sid) {
		ArrayList<ArrayList> lectureInfoList = new LectureDAO().getLectureInfoByLCode(p_lcode);
		ArrayList<ArrayList> attendanceList = new AttendanceDAO().getAttendanceListBySID(p_sid);
		
		
	}
	
	/* �����򰡿��� */
	public boolean getLectureEvaluationByCode(int p_attendancecode) {
		if(new LectureEvaluationDAO().isLectureEvaluationExist(p_attendancecode))
			return true;
		return false;
	}
	
	public ArrayList<ArrayList> getGradeInfo(int p_sid, int p_semester) {
		ArrayList<Integer> attlist = new ArrayList<Integer>();
		try {
			String SQL = "SELECT A.attendanceNum"
					+ " FROM Attendance A"
					+ " WHERE A.studentID = ? AND L.registerTerm = ?"
					+ " LEFT JOIN Lecture L"
					+ " ON L.lectureCode = A.lectureCode";
			conn = DriverManager.getConnection(getJdbcUrl(), getDbId(), getDbPwd());
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
		
		new GradeInfoDAO().getGradeListByAttCodeList(attlist);
	}
	
	public ArrayList<>
}
