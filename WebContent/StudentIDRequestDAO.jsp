<%@page import="java.util.Stack"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Date" %>
<% request.setCharacterEncoding("euc-kr"); %>
<%!	int reqSIDnum; %>
<%!	Date reqSIDdate; %>
<%!	String accountID; %>

<%!
   Connection conn = null;
   PreparedStatement pstmt = null;
   String str = "";
   ResultSet rs = null;
%>

<%!
	private enum signUpResult {
		SUCCESS,
		INVALID_FORM,
		MISSING_FIELD
	}
%>

<%!
	private enum loginResult {
		SUCCESS,
		MISSING_FIELD,
		NOT_FOUND_ID,
		INCORRECT_PWD
}
%>
<%!
	public boolean isIncludeNumber(String string) {
	for(int i = 0 ; i < string.length(); i ++)
    {    
        // 48 ~ 57은 아스키 코드로 0~9이다.
        if(48 <= string.charAt(i) && string.charAt(i) <= 57)
            return true;
    }
	return false;
}
%>
<%!
	public boolean addReqSID(Date p_date, String p_accountID) { // 아이디 이므로 accountID로 이름 변경
		reqSIDdate = p_date;
		accountID = p_accountID;
		
		try {
			String SQL = "INSERT INTO StudentIDRequest VALUES (?, ?)";
			pstmt = conn.prepareStatement(SQL);
		    pstmt.setDate(1,reqSIDdate);
		    pstmt.setString(2,accountID);
		    pstmt.executeUpdate();
		    return true;
		}catch(Exception e) {
		      e.printStackTrace();
		      str = "member 테이블에 새로운 레코드를 추가에 실패했습니다.";
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		return false;
		
}

%>
<%!
	public ArrayList<ArrayList> getReqSIDList() {
		ArrayList<ArrayList> resultArrayList = new ArrayList<ArrayList>();
		
		try {
			String SQL = "SELECT * FROM StudentIDRequest;";
			pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery(); // ResultSet
			
			// 조회결과 아무것도 없음
			if(!rs.next()) {
				return null;	
			}
			
			rs.beforeFirst(); // 첫 행으로 이동  -> 이게 맞나 ?
			int i = 0;
			while(rs.next()) {
				int rsReqSIDnum = rs.getInt("reqSIDnum");
				Date rsDate = rs.getDate("reqSIDdate");
				String rsAccountID = rs.getString("accountID");
				ArrayList temp = new ArrayList();
				temp[0].add(rsReqSIDnum);
				temp[1].add(rsDate);
				temp[2].add(rsAccountID);
				
				
				i++;
			}
			 %>
		   	
		}catch(Exception e) {
		      e.printStackTrace();
		      str = "member 테이블에 새로운 레코드를 추가에 실패했습니다.";
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
}
%>
<%!
	public signUpResult signUp(String p_id, String p_pwd, String p_name, int p_birth) {
		accountID = p_id;
		pwd = p_pwd;
		name = p_name;
		birth = p_birth;
		
		// null 체크
		if(accountID.isEmpty() || pwd.isEmpty() || name.isEmpty() || birth == 0) {
			return signUpResult.MISSING_FIELD;
		}

		// 현재 날짜를 가져와서 int로 변환.
		String inDate = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
		int isDate = Integer.parseInt(inDate);

		// 이름 중 숫자가 있는 경우, 생일이 현재 날짜보다 큰 경우
		if(isIncludeNumber(name) || (birth >= isDate)) {
			return signUpResult.INVALID_FORM;
		}
		
		try {
			String SQL = "INSERT INTO Account (accountID, pwd, accountName, birth) VALUES (?, ?, ?, ?)";
			pstmt = conn.prepareStatement(SQL);
		    pstmt.setString(1,accountID);
		    pstmt.setString(2,pwd);
		    pstmt.setString(3,name);
		    pstmt.setInt(4, birth);
		    pstmt.executeUpdate();
		    
		    StudentIDRequestDAO.addReqSID(isDate, accountID);
		}catch(Exception e) {
		      e.printStackTrace();
		      str = "member 테이블에 새로운 레코드를 추가에 실패했습니다.";
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		return signUpResult.SUCCESS;
}
%>

<%!
	public loginResult login(String p_id, String p_pwd) {
		accountID = p_id;
		pwd = p_pwd;
		
		// null 체크
		if(accountID.isEmpty() || pwd.isEmpty()) {
			return loginResult.MISSING_FIELD;
		}		
		
		try {
			String SQL = "SELECT A.accountID, A.pwd FROM Account A WHERE A.accountID = " + accountID;
			pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			
			// 조회결과 아이디 없음
			if(!rs.next()) {
				return loginResult.NOT_FOUND_ID;	
			}
			
			String rsID = rs.getString("accountID");
			String rsPWD = rs.getString("pwd");
			
			// 비밀번호가 맞지 않음
			if(!pwd.equals(rsPWD)) {
				return loginResult.INCORRECT_PWD;
			}
			
			 // + 현재 세션을 해당 계정으로 활성화.
		}catch(Exception e){
	        e.printStackTrace();
	    }finally{
	    	
	    }
		return loginResult.SUCCESS;
}
%>

<%!
	public int requestSID(String p_accountID, int p_newStuYear, int p_newStuOrder, int p_dcode) {
	
	// 주어진 아이디의 계정정보 가져오기 -> acc
	try {
		String SQL = "SELECT * FROM Account A WHERE A.accountID = " + p_accountID;
		pstmt = conn.prepareStatement(SQL);
		rs = pstmt.executeQuery();
		
		// 조회결과 계정정보 없음
		if(!rs.next()) {
			return 0;	
		}
		
		String rsName = rs.getString("accountName"); // 학생 이름 받기
		
		// 학번 생성 함수 실행
		if(StudentDAO.createNewStudent(p_newStuYear, p_newStuOrder, rsName, p_accountID, p_dcode)) {
			return StudentDAO.studentID; // 학번
		}
	}catch(Exception e){
        e.printStackTrace();
    }finally{
    	
    }
	
	return 0;
}
%>

<% 
   try{
      String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/SE02?autoReconnect=true"; // DB 저장주소
      String dbId = "SE02_11"; // 아이디
      String dbPass = "2018"; // 비밀번호
      
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(jdbcUrl,dbId,dbPass); // DB연결
      
      signUp("testID", "password", "Leehaewon", 19960130); // 회원가입
      login("Leehaewon", "1234"); // 로그인
      requestSID("testID", 2018, 2, 20181); // 학번요청
      
   }catch(Exception e) {
      e.printStackTrace();
      str = "member 테이블에 새로운 레코드를 추가에 실패했습니다.";
   }finally {
      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
      if(conn != null) try{conn.close();}catch(SQLException sqle){}
   }
%>
