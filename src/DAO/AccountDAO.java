package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ClassObject.Account;

public class AccountDAO extends DAOBase {
	
	// �����ͺ��̽� ������ ����
	private static PreparedStatement pstmt;
	private static Connection conn;
	
	public enum signUpResult { // ȸ������ ��� enum
		SUCCESS,
		INVALID_FORM,
		MISSING_FIELD
	}

	public enum loginResult { // �α��� ��� enum
		SUCCESS,
		MISSING_FIELD,
		NOT_FOUND_ID,
		INCORRECT_PWD
}
	
	// ������ ������ ���ÿ� jbdc ����.
	public AccountDAO() {
		super();
	}

	/* �ش� String ���ڿ� ���ο� ���ڰ� �����ϴ��� üũ */
	public static boolean isIncludeNumber(String string) {
		for(int i = 0 ; i < string.length(); i ++)
	    {    
	        // 48 ~ 57�� �ƽ�Ű �ڵ�� 0~9�̴�.
	        if(48 <= string.charAt(i) && string.charAt(i) <= 57)
	            return true;
	    }
		return false;
	}
	
	/* ȸ������ */
	public signUpResult signUp(String p_id, String p_pwd, String p_name, int p_birth) {
		Account account = new Account();
		account.setAccountID(p_id);
		account.setPwd(p_pwd);
		account.setAccountName(p_name);
		account.setBirth(p_birth);
		
		// null üũ
		if(account.getAccountID().isEmpty() || account.getPwd().isEmpty() 
				|| account.getAccountName().isEmpty() || account.getBirth() == 0) {
			return signUpResult.MISSING_FIELD;
		}

		// ���� ��¥�� �����ͼ� int�� ��ȯ. -> Date Ÿ�� �Ⱦ��� int�� �Ͻǲ���..
		String inDate = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
		int isDate = Integer.parseInt(inDate);

		// �̸� �� ���ڰ� �ִ� ���, ������ ���� ��¥���� ū ���
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

	/* �α��� */
	public loginResult login(String p_id, String p_pwd) {
		Account account = new Account();
		account.setAccountID(p_id);
		account.setPwd(p_pwd);
		
		// null üũ
		if(account.getAccountID().isEmpty() || account.getPwd().isEmpty()) {
			return loginResult.MISSING_FIELD;
		}		
		
		try {
			String SQL = "SELECT A.accountID FROM Account A WHERE A.accountID = ?";
			conn = DriverManager.getConnection(getJdbcUrl(), getDbId(), getDbPwd());
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, account.getAccountID());
			ResultSet rs = pstmt.executeQuery();
			
			// ��ȸ��� ���̵� ����
			if(!rs.next()) {
				return loginResult.NOT_FOUND_ID;	
			}
			
			String rsID = rs.getString("accountID");
			String rsPWD = rs.getString("pwd");
			
			// ��й�ȣ�� ���� ����
			if(!rsPWD.equals(rsPWD)) {
				return loginResult.INCORRECT_PWD;
			}
			
			// DB���� ���� ������ �缳��
			account.setAccountID(rsID);
			account.setPwd(rsPWD);
			
			 // + ���� ������ �ش� �������� Ȱ��ȭ.
		}catch(Exception e){
	        e.printStackTrace();
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
		return loginResult.SUCCESS;
}
	
	/* �ű��й���û */
	public  int requestSID(String p_id, int p_newStuYear, int p_newStuOrder, int p_dcode) {
		Account account = new Account();
		
	// �־��� ���̵��� �������� �������� -> acc -> �л� �̸��� �������� �ǳ��� ??
	try {
		String SQL = "SELECT * FROM Account A WHERE A.accountID = ?";
		conn = DriverManager.getConnection(getJdbcUrl(), getDbId(), getDbPwd());
		pstmt = conn.prepareStatement(SQL);
		pstmt.setString(1, p_id);
		ResultSet rs = pstmt.executeQuery();
		
		// ��ȸ��� �������� ����
		if(!rs.next()) {
			return 0;	
		}
		
		String rsName = rs.getString("accountName"); // �л� �̸� �ޱ�
		
		//account.setAccountID(p_id);
		//account.setPwd(p_pwd);
		account.setAccountName(rsName);
		//account.setBirth(p_birth);
		
		// �й� ���� �Լ� ����
		/*
		if(StudentDAO.createNewStudent(p_newStuYear, p_newStuOrder, rsName, p_accountID, p_dcode)) { // �̰͸� �ѱ�� �Ǵ���??
			return StudentDAO.studentID; // �й�
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
