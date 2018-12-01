<%@page import="java.util.Stack"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="java.sql.*" %>
<% request.setCharacterEncoding("euc-kr"); %>
<%!	String accountID; %>
<%!	String pwd; %>
<%!	String name; %>
<%!	int birth; %>
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
        // 48 ~ 57�� �ƽ�Ű �ڵ�� 0~9�̴�.
        if(48 <= string.charAt(i) && string.charAt(i) <= 57)
            return true;
    }
	return false;
}
%>
<%!
	public signUpResult signUp(String p_id, String p_pwd, String p_name, int p_birth) {
		accountID = p_id;
		pwd = p_pwd;
		name = p_name;
		birth = p_birth;
		
		// null üũ
		if(accountID.isEmpty() || pwd.isEmpty() || name.isEmpty() || birth == 0) {
			return signUpResult.MISSING_FIELD;
		}

		// ���� ��¥�� �����ͼ� int�� ��ȯ.
		String inDate = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
		int isDate = Integer.parseInt(inDate);

		// �̸� �� ���ڰ� �ִ� ���, ������ ���� ��¥���� ū ���
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
		      str = "member ���̺� ���ο� ���ڵ带 �߰��� �����߽��ϴ�.";
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
		
		// null üũ
		if(accountID.isEmpty() || pwd.isEmpty()) {
			return loginResult.MISSING_FIELD;
		}		
		
		try {
			String SQL = "SELECT A.accountID FROM Account A WHERE A.accountID = " + accountID;
			pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			
			// ��ȸ��� ���̵� ����
			if(!rs.next()) {
				return loginResult.NOT_FOUND_ID;	
			}
			
			String rsID = rs.getString("accountID");
			String rsPWD = rs.getString("pwd");
			
			// ��й�ȣ�� ���� ����
			if(!pwd.equals(rsPWD)) {
				return loginResult.INCORRECT_PWD;
			}
			
			 // + ���� ������ �ش� �������� Ȱ��ȭ.
		}catch(Exception e){
	        e.printStackTrace();
	    }finally{
	    	
	    }
		return loginResult.SUCCESS;
}
%>

<%!
	public int requestSID(String p_accountID, int p_newStuYear, int p_newStuOrder, int p_dcode) {
	
	// �־��� ���̵��� �������� �������� -> acc
	try {
		String SQL = "SELECT * FROM Account A WHERE A.accountID = " + p_accountID;
		pstmt = conn.prepareStatement(SQL);
		rs = pstmt.executeQuery();
		
		// ��ȸ��� �������� ����
		if(!rs.next()) {
			return 0;	
		}
		
		String rsName = rs.getString("accountName"); // �л� �̸� �ޱ�
		
		// �й� ���� �Լ� ����
		if(StudentDAO.createNewStudent(p_newStuYear, p_newStuOrder, rsName, p_accountID, p_dcode)) {
			return StudentDAO.studentID; // �й�
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
      String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/SE02?autoReconnect=true"; // DB �����ּ�
      String dbId = "SE02_11"; // ���̵�
      String dbPass = "2018"; // ��й�ȣ
      
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(jdbcUrl,dbId,dbPass); // DB����
      
      signUp("testID", "password", "Leehaewon", 19960130); // ȸ������
      login("Leehaewon", "1234"); // �α���
      requestSID("testID", 2018, 2, 20181); // �й���û
      
   }catch(Exception e) {
      e.printStackTrace();
      str = "member ���̺� ���ο� ���ڵ带 �߰��� �����߽��ϴ�.";
   }finally {
      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
      if(conn != null) try{conn.close();}catch(SQLException sqle){}
   }
%>
