package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ClassObject.Account;

public class AccountDAO extends DAOBase {
	
	// 데이터베이스 접근을 위해
	private static PreparedStatement pstmt;
	private static Connection conn;
	
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
	public AccountDAO() {
		super();
	}

	/* 해당 String 문자열 내부에 숫자가 존재하는지 체크 */
	public static boolean isIncludeNumber(String string) {
		for(int i = 0 ; i < string.length(); i ++)
	    {    
	        // 48 ~ 57은 아스키 코드로 0~9이다.
	        if(48 <= string.charAt(i) && string.charAt(i) <= 57)
	            return true;
	    }
		return false;
	}
	
	/* 회원가입 */
	public signUpResult signUp(String p_id, String p_pwd, String p_name, int p_birth) {
		Account account = new Account();
		account.setAccountID(p_id);
		account.setPwd(p_pwd);
		account.setAccountName(p_name);
		account.setBirth(p_birth);
		
		// null 체크
		if(account.getAccountID().isEmpty() || account.getPwd().isEmpty() 
				|| account.getAccountName().isEmpty() || account.getBirth() == 0) {
			return signUpResult.MISSING_FIELD;
		}

		// 현재 날짜를 가져와서 int로 변환. -> Date 타입 안쓰고 int로 하실껀지..
		String inDate = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
		int isDate = Integer.parseInt(inDate);

		// 이름 중 숫자가 있는 경우, 생일이 현재 날짜보다 큰 경우
		if(isIncludeNumber(account.getAccountName()) || (account.getBirth() >= isDate)) {
			return signUpResult.INVALID_FORM;
		}
		
		try {
			String SQL = "INSERT INTO Account (accountID, pwd, accountName, birth) VALUES (?, ?, ?, ?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
		    pstmt.setString(1,account.getAccountID());
		    pstmt.setString(2,account.getPwd());
		    pstmt.setString(3,account.getAccountName());
		    pstmt.setInt(4, account.getBirth());
		    pstmt.executeUpdate();
		    
		    //StudentIDRequestDAO.addReqSID(isDate, accountID);
		}catch(Exception e) {
		      e.printStackTrace();
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		return signUpResult.SUCCESS;
}

	/* 로그인 */
	public loginResult login(String p_id, String p_pwd) {
		Account account = new Account();
		account.setAccountID(p_id);
		account.setPwd(p_pwd);
		
		// null 체크
		if(account.getAccountID().isEmpty() || account.getPwd().isEmpty()) {
			return loginResult.MISSING_FIELD;
		}		
		
		try {
			String SQL = "SELECT A.accountID FROM Account A WHERE A.accountID = ?";
			conn = DriverManager.getConnection(getJdbcUrl(), getDbId(), getDbPwd());
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, account.getAccountID());
			ResultSet rs = pstmt.executeQuery();
			
			// 조회결과 아이디 없음
			if(!rs.next()) {
				return loginResult.NOT_FOUND_ID;	
			}
			
			String rsID = rs.getString("accountID");
			String rsPWD = rs.getString("pwd");
			
			// 비밀번호가 맞지 않음
			if(!rsPWD.equals(rsPWD)) {
				return loginResult.INCORRECT_PWD;
			}
			
			// DB에서 받은 정보로 재설정
			account.setAccountID(rsID);
			account.setPwd(rsPWD);
			
			 // + 현재 세션을 해당 계정으로 활성화.
		}catch(Exception e){
	        e.printStackTrace();
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
		return loginResult.SUCCESS;
}
	
	/* 신규학번요청 */
	public  int requestSID(String p_id, int p_newStuYear, int p_newStuOrder, int p_dcode) {
		Account account = new Account();
		
	// 주어진 아이디의 계정정보 가져오기 -> acc -> 학생 이름만 가져오면 되나요 ??
	try {
		String SQL = "SELECT * FROM Account A WHERE A.accountID = ?";
		conn = DriverManager.getConnection(getJdbcUrl(), getDbId(), getDbPwd());
		pstmt = conn.prepareStatement(SQL);
		pstmt.setString(1, p_id);
		ResultSet rs = pstmt.executeQuery();
		
		// 조회결과 계정정보 없음
		if(!rs.next()) {
			return 0;	
		}
		
		String rsName = rs.getString("accountName"); // 학생 이름 받기
		
		//account.setAccountID(p_id);
		//account.setPwd(p_pwd);
		account.setAccountName(rsName);
		//account.setBirth(p_birth);
		
		// 학번 생성 함수 실행
		/*
		if(StudentDAO.createNewStudent(p_newStuYear, p_newStuOrder, rsName, p_accountID, p_dcode)) { // 이것만 넘기면 되는지??
			return StudentDAO.studentID; // 학번
		}
		*/
	}catch(Exception e){
        e.printStackTrace();
    }finally{
    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
	      if(conn != null) try{conn.close();}catch(SQLException sqle){}
    }
	
	return 0;
	}
}
