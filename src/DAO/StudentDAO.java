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

	// 생성자 생성과 동시에 jbdc 설정.
	public StudentDAO() {
		setJdbcUrl("jdbc:mysql://127.0.0.1:3306/SE02?autoReconnect=true"); // DB 저장주소
		setDbId("SE02_11");
		setDbPwd("2018");
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//conn = DriverManager.getConnection(this.jdbcUrl, this.dbId, this.dbPass);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* 학적상태조회 */
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
			
			// 조회결과 아무것도 없음
			if(!rs.next()) {
				return null;	
			}
			
			// 꺼내오기
			String rsSName = rs.getString("studentName");
			String rsDName = rs.getString("departmentName");
			int rsYear = rs.getInt("studentYear");
			int rsSemester = rs.getInt("semester");
			boolean rsTimeOff = rs.getBoolean("isTimeOff");
			boolean rsGraduate = rs.getBoolean("isGraduate");
			int rsBankAccountNum = rs.getInt("bankAccountNum");
			
			// ArrayList에 넣기
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
	
	/* 휴복학설정 */
	public boolean setTimeOff(int p_sid, boolean p_changetype) {
		
		try {
			String SQL = "UPDATE Student" + 
					" SET Student.isTimeOff = ?"
					+ " WHERE Student.studentID = ?";
			conn = DriverManager.getConnection(getJdbcUrl(), getDbId(), getDbPwd());
			pstmt = conn.prepareStatement(SQL);
			pstmt.setBoolean(1, p_changetype);
			pstmt.setInt(2, p_sid);
			int result = pstmt.executeUpdate(); // 변경된 row수 만큼 리턴
			if(result == 1) // 1줄만 변경하므로 성공
				return true;
		}catch(Exception e) {
		      e.printStackTrace();
		      
		}finally { // 삭제는 반대 순서
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		
		return false;
	}
	
	/* 휴복학요청거절알림 */
	/*
	public boolean rejectTimeOff(int p_sid, String p_reason) {
		
		try {
			INSERT INTO 학생알림함 (학번, 메시지텍스트)
			VALUES ( p_sid, "[휴복학요청이 거절되었습니다.]" + p_reason )
			String SQL = "INSERT INTO 학생알림함 (학번, 메시지텍스트)" + 
					" SET Student.isTimeOff = ?"
					+ " WHERE Student.studentID = ?";
			conn = DriverManager.getConnection(getJdbcUrl(), getDbId(), getDbPwd());
			pstmt = conn.prepareStatement(SQL);
			pstmt.setBoolean(1, p_changetype);
			pstmt.setInt(2, p_sid);
			int result = pstmt.executeUpdate(); // 변경된 row수 만큼 리턴
			if(result == 1) // 1줄만 변경하므로 성공
				return true;
		}catch(Exception e) {
		      e.printStackTrace();
		      
		}finally { // 삭제는 반대 순서
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	}
	*/
	
	/* 휴학여부조회 */
	public boolean getTimeOffBySID(int p_sid) {
		
		try {
			String SQL = "SELECT isTimeOff FROM Student" + 
					" WHERE studentID = ?";
			conn = DriverManager.getConnection(getJdbcUrl(), getDbId(), getDbPwd());
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_sid);
			ResultSet rs = pstmt.executeQuery(); // ResultSet

			// 존재하면
			if(rs.next()) {
				return rs.getBoolean("isTimeOff");
			}
			
		}catch(Exception e) {
		      e.printStackTrace();
		      
		}finally { // 삭제는 반대 순서
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		// 존재하지 않을 시. -> 오류 메세지도 같이 ??
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
		    int result = pstmt.executeUpdate(); // 성공 결과를 모르겠음
		    
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

			// 존재하면
			if(rs.next()) {
				return true;
			}
			
		}catch(Exception e) {
		      e.printStackTrace();
		      
		}finally { // 삭제는 반대 순서
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		return false;
	}
}
