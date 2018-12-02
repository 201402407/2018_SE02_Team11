package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ClassObject.Student;
import ClassObject.StudentIDRequest;

public class StudentDAO {
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

	// ������ ������ ���ÿ� jbdc ����.
	public StudentDAO() {
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
	
	/* ����������ȸ */
	public ArrayList getStudentInfoBySID(int p_sid) {
		
		try {
			String SQL = "SELECT S.name, D.departmentName, S.year, S.semester,"
					+ " S.isTimeOff, S.isGraduate, S.bankAccountNum"
					+ " FROM Student S"
					+ " LEFT JOIN Department D" 
					+ " ON S.departmentCode = D.departmentCode"
					+ " WHERE S.studentID = ?";
			conn = DriverManager.getConnection(getJdbcUrl(), getDbId(), getDbPwd());
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_sid);
			ResultSet rs = pstmt.executeQuery(); // ResultSet
			
			// ��ȸ��� �ƹ��͵� ����
			if(!rs.next()) {
				return null;	
			}
			
			// ��������
			String rsSName = rs.getString("studentName");
			String rsDName = rs.getString("departmentName");
			int rsYear = rs.getInt("studentYear");
			int rsSemester = rs.getInt("semester");
			boolean rsTimeOff = rs.getBoolean("isTimeOff");
			boolean rsGraduate = rs.getBoolean("isGraduate");
			int rsBankAccountNum = rs.getInt("bankAccountNum");
			
			// ArrayList�� �ֱ�
			ArrayList arrayList = new ArrayList();
			arrayList.add(rsSName);
		   	arrayList.add(rsDName);
		   	arrayList.add(rsYear);
		   	arrayList.add(rsSemester);
		   	arrayList.add(rsTimeOff);
		   	arrayList.add(rsGraduate);
		   	arrayList.add(rsBankAccountNum);
		   	
			return arrayList;
			
		}catch(Exception e) {
		      e.printStackTrace();
		      
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		
		return null;
	}
	
	/* �޺��м��� */
	public boolean setTimeOff(int p_sid, boolean p_changetype) {
		
		try {
			String SQL = "UPDATE Student" + 
					" SET Student.isTimeOff = ?"
					+ " WHERE Student.studentID = ?";
			conn = DriverManager.getConnection(getJdbcUrl(), getDbId(), getDbPwd());
			pstmt = conn.prepareStatement(SQL);
			pstmt.setBoolean(1, p_changetype);
			pstmt.setInt(2, p_sid);
			int result = pstmt.executeUpdate(); // ����� row�� ��ŭ ����
			if(result == 1) // 1�ٸ� �����ϹǷ� ����
				return true;
		}catch(Exception e) {
		      e.printStackTrace();
		      
		}finally { // ������ �ݴ� ����
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		
		return false;
	}
	
	/* �޺��п�û�����˸� */
	/*
	public boolean rejectTimeOff(int p_sid, String p_reason) {
		
		try {
			INSERT INTO �л��˸��� (�й�, �޽����ؽ�Ʈ)
			VALUES ( p_sid, "[�޺��п�û�� �����Ǿ����ϴ�.]" + p_reason )
			String SQL = "INSERT INTO �л��˸��� (�й�, �޽����ؽ�Ʈ)" + 
					" SET Student.isTimeOff = ?"
					+ " WHERE Student.studentID = ?";
			conn = DriverManager.getConnection(getJdbcUrl(), getDbId(), getDbPwd());
			pstmt = conn.prepareStatement(SQL);
			pstmt.setBoolean(1, p_changetype);
			pstmt.setInt(2, p_sid);
			int result = pstmt.executeUpdate(); // ����� row�� ��ŭ ����
			if(result == 1) // 1�ٸ� �����ϹǷ� ����
				return true;
		}catch(Exception e) {
		      e.printStackTrace();
		      
		}finally { // ������ �ݴ� ����
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	}
	*/
	
	/* ���п�����ȸ */
	public boolean getTimeOffBySID(int p_sid) {
		
		try {
			String SQL = "SELECT isTimeOff FROM Student" + 
					" WHERE studentID = ?";
			conn = DriverManager.getConnection(getJdbcUrl(), getDbId(), getDbPwd());
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_sid);
			ResultSet rs = pstmt.executeQuery(); // ResultSet

			// �����ϸ�
			if(rs.next()) {
				return rs.getBoolean("isTimeOff");
			}
			
		}catch(Exception e) {
		      e.printStackTrace();
		      
		}finally { // ������ �ݴ� ����
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		// �������� ���� ��. -> ���� �޼����� ���� ??
		return false;
	}
	
	public boolean createNewStudent(int p_newStuYear, int p_newStuOrder, String p_name, String p_accountID, int p_dcode) {
		
		int newStuID = p_newStuYear * 100000 + p_newStuOrder;
		
		try {
			String SQL = "INSERT INTO Student (studentID, name, year, semester, isTimeOff, isGraduate, accountID, departmentCode)"
					+ " VALUES (?, ?, 1, 1, FALSE, FALSE, ?, ?)";
			conn = DriverManager.getConnection(getJdbcUrl(), getDbId(), getDbPwd());
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, newStuID);
			pstmt.setString(2, p_name);
			pstmt.setString(3, p_accountID);
			pstmt.setInt(4, p_dcode);
		    int result = pstmt.executeUpdate(); // ���� ����� �𸣰���
		    
		    return true;
		}catch(Exception e) {
		      e.printStackTrace();
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		
		return false;
	}
	
	public boolean isStudentExist(int p_sid) {
		try {
			String SQL = "SELECT * FROM Student" + 
					" WHERE studentID = ?";
			conn = DriverManager.getConnection(getJdbcUrl(), getDbId(), getDbPwd());
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_sid);
			ResultSet rs = pstmt.executeQuery(); // ResultSet

			// �����ϸ�
			if(rs.next()) {
				return true;
			}
			
		}catch(Exception e) {
		      e.printStackTrace();
		      
		}finally { // ������ �ݴ� ����
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		return false;
	}
}
