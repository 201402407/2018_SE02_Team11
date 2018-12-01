package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import ClassObject.StudentIDRequest;

public class StudentIDRequestDAO {
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

	public enum signUpResult { // 회원가입 결과 enum
		SUCCESS,
		INVALID_FORM,
		MISSING_FIELD
	}

	public enum loginResult { // 로그인 결과 enum
		SUCCESS,
		MISSING_FIELD,
		NOT_FOUND_ID,
		INCORRECT_PWD
}
	
	// 생성자 생성과 동시에 jbdc 설정.
	public StudentIDRequestDAO() {
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
	
	public boolean isIncludeNumber(String string) {
		for(int i = 0 ; i < string.length(); i ++)
	    {    
	        // 48 ~ 57은 아스키 코드로 0~9이다.
	        if(48 <= string.charAt(i) && string.charAt(i) <= 57)
	            return true;
	    }
		return false;
	}
	
	/* 학번요청추가  */
	public boolean addReqSID(Date p_date, String p_accountID) { // 아이디 이므로 accountID로 이름 변경
		StudentIDRequest studentIDRequest = new StudentIDRequest();
		studentIDRequest.setReqSIDdate(p_date);
		studentIDRequest.setAccountID(p_accountID);
		
		try {
			String SQL = "INSERT INTO StudentIDRequest VALUES (?, ?)";
			conn = DriverManager.getConnection(getJdbcUrl(), getDbId(), getDbPwd());
			pstmt = conn.prepareStatement(SQL);
		    pstmt.setDate(1,studentIDRequest.getReqSIDdate());
		    pstmt.setString(2,studentIDRequest.getAccountID());
		    pstmt.executeUpdate();
		    
		    return true;
		}catch(Exception e) {
		      e.printStackTrace();
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		
		return false;
		
	}
	
	/* 학번요청대기열목록조회 */
	public ArrayList<StudentIDRequest> getReqSIDList() { // DAO 명세서에 ArrayList로 표기하는지 ?
		ArrayList<StudentIDRequest> resultArrayList = new ArrayList<StudentIDRequest>();
		
		try {
			String SQL = "SELECT * FROM StudentIDRequest";
			conn = DriverManager.getConnection(getJdbcUrl(), getDbId(), getDbPwd());
			pstmt = conn.prepareStatement(SQL);
			ResultSet rs = pstmt.executeQuery(); // ResultSet
			
			// 조회결과 아무것도 없음
			if(!rs.next()) {
				return null;	
			}
			
			rs.beforeFirst(); // 첫 행으로 이동  -> 이게 맞나 ?
			
			// 목록 꺼내오기
			while(rs.next()) {
				int rsReqSIDnum = rs.getInt("reqSIDnum");
				Date rsDate = rs.getDate("reqSIDdate");
				String rsAccountID = rs.getString("accountID");
				
				StudentIDRequest studentIDRequest = new StudentIDRequest();
				studentIDRequest.setReqSIDnum(rsReqSIDnum);
				studentIDRequest.setReqSIDdate(rsDate);
				studentIDRequest.setAccountID(rsAccountID);
				
				resultArrayList.add(studentIDRequest); // ArrayList로 담기
			}
		   	
			return resultArrayList;
			
		}catch(Exception e) {
		      e.printStackTrace();
		      
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		return null;
	}
	
	/* 학번부여허가 */
	public boolean permitReqSID(int p_reqnum, int p_dcode) {
		
		try {
			// 해당 요청번호의 학번을 불러온다.
			String SQL = "SELECT * FROM StudentIDRequest WHERE reqSIDnum = " + p_reqnum;
			conn = DriverManager.getConnection(getJdbcUrl(), getDbId(), getDbPwd());
			pstmt = conn.prepareStatement(SQL);
			ResultSet rs = pstmt.executeQuery(); // ResultSet
			
			// 조회결과 아무것도 없음
			if(!rs.next()) {
				return false;	
			}

			// 학번요청 정보 가져오기
			Date rsReqSIDdate = rs.getDate("reqSIDdate");
			String rsAccountID = rs.getString("accountID");
						
			// 년도만 뽑아오는 과정
			SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
			String temp = transFormat.format(rsReqSIDdate);
			temp = temp.substring(0, 4);
			int reqnum_earliest_from_that_year = Integer.valueOf(temp);
			
			// 요청년도가 년도(r.reqSIDdate)에 속하는 요청정보 중 가장 이른 것의 요청번호를 가져온다.
			SQL = "SELECT reqSIDnum FROM StudentIDRequest"
					+ " WHERE YEAR(reqSIDdate) = " + reqnum_earliest_from_that_year
					+ " ORDER BY reqSIDdate ASC LIMIT 1";
			pstmt = null; // 초기화
			pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery(); // ResultSet
		   	
			// 조회결과 아무것도 없음
			if(!rs.next()) {
				return false;
			}
			
			int rsSID = rs.getInt("reqSIDnum");
			// 년도당_요청번호 <- r.reqnum - reqnum_earliest_from_that_year??
			int year_reqnum = p_reqnum - rsSID;
			
			// 학번 요청
			AccountDAO accountDAO = new AccountDAO();
			if(accountDAO.requestSID(rsAccountID, reqnum_earliest_from_that_year, year_reqnum, p_dcode) == 0) {
				return false; // 0이면 실패
			}
			
			// 삭제
			if(deleteReqSID(p_reqnum))
				return true;
			
		}catch(Exception e) {
		      e.printStackTrace();
		      
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		return false;
	}
	
	/* 학번부여거절 */
	public boolean rejectReqSID(int p_reqnum) {
		if(deleteReqSID(p_reqnum))
			return true;
		return false;
	}
	
	/* 학번요청삭제 */
	public boolean deleteReqSID(int p_reqnum) {
		try {
			String SQL = "DELETE FROM StudentIDRequest" + 
					" WHERE StudentIDRequest.reqSIDNum = " + p_reqnum;
			conn = DriverManager.getConnection(getJdbcUrl(), getDbId(), getDbPwd());
			pstmt = conn.prepareStatement(SQL);
			pstmt.executeUpdate();
			
			return true;
		}catch(Exception e) {
		      e.printStackTrace();
		      
		}finally { // 삭제는 반대 순서
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		return false;
	}
}
