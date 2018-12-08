package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import ClassObject.Account;
import Util.OurTimes;

public class AccountDAO extends DAOBase {
	
	// 데이터베이스 접근을 위해
	private PreparedStatement pstmt;
	private Connection conn;
	
	// 협업하기 위한 DAO 클래스 변수 선언
	StudentDAO studentDAO;
	StudentIDRequestDAO studentIDRequestDAO;
	
	public enum signUpResult { // 회원가입 결과 enum
		SUCCESS,
		INVALID_FORM
	}

	public enum loginResult { // 로그인 결과 enum
		SUCCESS_STUDENT, //학생 로그인
		FAIL_STUDENT, //학번부여를 받지 못함
		SUCCESS_ADMIN, //관리자 로그인
		NOT_FOUND_ID,
		INCORRECT_PWD
}
	
	// 생성자 생성과 동시에 jbdc 설정.
	public AccountDAO() {
		super();
		
		studentDAO = new StudentDAO();
		studentIDRequestDAO = new StudentIDRequestDAO();
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
	
	/**
	 * 회원가입
	 * @param p_id 아이디 
	 * @param p_pwd 비밀번호 
	 * @param p_name 이름 
	 * @param p_birth 생년월일
	 * @return 결과(signUpResult).
	 * @throws SQLException DB오류
	 * 각각의 경우에 따른 enum 리턴.
	 * + 생년월일 변수 int -> Date 변경, 입력한 생일과 현재날짜 비교 추가해야 함.
	 * ! DAO명세서 수정 필요 (signUpResult개편, 아이디의 형식제한, 비밀번호의 형식제한, 이름 제한, 생년월일 제한)
	 */
	public signUpResult signUp(String p_id, String p_pwd, String p_name, LocalDate p_birth) throws SQLException {
		Account account = new Account();
		account.setAccountID(p_id);
		account.setPwd(p_pwd);
		account.setAccountName(p_name);
		account.setBirth(p_birth);

		// 계정은 5글자 이상 12글자 이하, 영문와 숫자로 이루어져야 한다.
		
		// 비밀번호는 5글자 이상 20글자 이하여야 한다.
		// 이름은 2글자 이상 5글자 이하, 한글이어야 한다.
		// 생일이 현재 날짜보다 전이어야 한다.
		if(isIncludeNumber(account.getAccountName()) ||
				(OurTimes.compareDateWithoutYear(account.getBirth(),OurTimes.dateNow()) > 0)) {
			return signUpResult.INVALID_FORM;
		}
		
		try {
			String SQL = "INSERT INTO Account (accountID, pwd, accountName, birth) VALUES (?, ?, ?, ?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
		    pstmt.setString(1,account.getAccountID());
		    pstmt.setString(2,account.getPwd());
		    pstmt.setString(3,account.getAccountName());
		    pstmt.setDate(4, OurTimes.LocalDateTosqlDate(account.getBirth()));
		    int result = pstmt.executeUpdate(); // -> SQL 실패한 경우도 넣어야 하나 ?
		    
		    if(result != 1)
		    	throw new SQLException("Affected row is not one.");
		    
		    if(studentIDRequestDAO.addReqSID(account.getBirth(), account.getAccountID())) {
		    	return signUpResult.SUCCESS;
		    }
		    return signUpResult.SUCCESS;
		}catch(SQLException e) {
		      throw e;
		}
		finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
}

	/** 
	 * 로그인 - 해당 로그인정보가 학생으로 받아들여지는지 관리자로 받아들여지는지를 알려준다.
	 * @param p_id 아이디
	 * @param p_pwd 비밀번호
	 * @return 결과(LoginResult)
	 * @throws SQLException DB 오류
	 * 각각의 경우에 따른 enum 리턴
	 * + 현재 세션을 해당 계정으로 활성화 추가 필요
	 * !DAO 수정 - 리턴이 달라짐
	 * !ClassDiagram 수정 - 관리자 클래스 필요없어짐.
	 */
	public loginResult login(String p_id, String p_pwd) throws SQLException {
		Account account = new Account();
		account.setAccountID(p_id);
		account.setPwd(p_pwd);
		
		try {
			conn = getConnection();
			String SQL = "SELECT A.accountID, A.pwd\r\n"
					+ "FROM Account A WHERE A.accountID = ?";
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
			
			// 관리자인지 검사
			if(isAdmin(account.getAccountID()))
				return loginResult.SUCCESS_ADMIN;
			// 학생인지 검사 (학번부여를 받지 못해 학생 테이블에 올라오지 않은 상태인지 검사하게 된다.)
			if(isStudent(account.getAccountID()))
				return loginResult.SUCCESS_STUDENT;
			else
				return loginResult.FAIL_STUDENT;
		
		}catch(SQLException e){
	        throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	}
	private boolean isAdmin(String id)
	{
		return id.equals("admin");
	}
	private boolean isStudent(String id) throws SQLException
	{
		String SQL = "SELECT *\r\n" + 
				"FROM Student\r\n" + 
				"WHERE accountID = ?;";
		pstmt = conn.prepareStatement(SQL);
		pstmt.setString(1, id);
		ResultSet rs = pstmt.executeQuery();
		
		return rs.next();
	}
	
	/** 
	 * 신규학번요청
	 * @param p_id 아이디
	 * @param p_newStuYear 등록년도, 
	 * @param p_newStuOrder 등록순서 
	 * @param p_dcode 학과번호
	 * @return 학번 -> -1은 없음
	 * @throws SQLException DB오류
	 * + StudentDAO.createNewStudent 함수 실행 추가
	 * ! DAO 수정 필요 */
	public int requestSID(String p_id, int p_newStuYear, int p_newStuOrder, int p_dcode) throws SQLException
	{
		Account account = new Account();
		
	// 주어진 아이디의 계정정보 가져오기 -> acc -> 학생 이름만 가져오면 되나요 ??
	try {
		String SQL = "SELECT * FROM Account A WHERE A.accountID = ?";
		conn = getConnection();
		pstmt = conn.prepareStatement(SQL);
		pstmt.setString(1, p_id);
		ResultSet rs = pstmt.executeQuery();
		
		// 조회결과 계정정보 없음
		if(!rs.next()) {
			return -1;	
		}
		
		String rsName = rs.getString("accountName"); // 학생 이름 받기
		
		//account.setAccountID(p_id);
		//account.setPwd(p_pwd);
		account.setAccountName(rsName);
		//account.setBirth(p_birth);
		
		// 학번 생성 함수 실행
		return studentDAO.createNewStudent(p_newStuYear, p_newStuOrder, rsName, p_id, p_dcode);
	}catch(SQLException e){
        return -1;
    }finally{
    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
	      if(conn != null) try{conn.close();}catch(SQLException sqle){}
    	}
	}
}
