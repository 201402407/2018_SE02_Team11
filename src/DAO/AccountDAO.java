package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.servlet.http.HttpSession;

import ClassObject.Account;
import Util.OurTimes;

public class AccountDAO extends DAOBase {
	
	// �����ͺ��̽� ������ ����
	private PreparedStatement pstmt;
	private Connection conn;
	
	// �����ϱ� ���� DAO Ŭ���� ���� ����
	StudentDAO studentDAO;
	StudentIDRequestDAO studentIDRequestDAO;
	
	public enum signUpResult { // ȸ������ ��� enum
		SUCCESS,
		INVALID_ID,
		INVALID_PWD,
		INVALID_NAME,
		INVALID_BIRTH
	}

	public enum loginResult { // �α��� ��� enum
		SUCCESS_STUDENT, //�л� �α���
		FAIL_STUDENT, //�й��ο��� ���� ����
		SUCCESS_ADMIN, //������ �α���
		NOT_FOUND_ID,
		INCORRECT_PWD
}
	
	// ������ ������ ���ÿ� jbdc ����.
	public AccountDAO() {
		super();
		
		studentDAO = new StudentDAO();
		studentIDRequestDAO = new StudentIDRequestDAO();
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
	
	/**
	 * ȸ������
	 * @param p_id ���̵�, 5���� �̻� 12���� ���� ������ ���ڷ� �̷������ �Ѵ�.
	 * @param p_pwd ��й�ȣ, 5���� �̻� 20���� ���Ͽ��� �Ѵ�.
	 * @param p_name �̸�, 2���� �̻� 5���� ���� �ѱ��̾�� �Ѵ�.
	 * @param p_birth �������, ���� ��¥���� ���̾�� �Ѵ�.
	 * @return ���(signUpResult).
	 * @throws SQLException DB����
	 * ������ ��쿡 ���� enum ����.
	 * ! DAO ���� �Ϸ�
	 */
	public signUpResult signUp(String p_id, String p_pwd, String p_name, LocalDate p_birth) throws SQLException {
		Account account = new Account();
		account.setAccountID(p_id);
		account.setPwd(p_pwd);
		account.setAccountName(p_name);
		account.setBirth(p_birth);
		
		// ������ 5���� �̻� 12���� ����, ������ ���ڷ� �̷������ �Ѵ�.
		if(!isValidAccountId(account.getAccountID()))
			return signUpResult.INVALID_ID;
		// ��й�ȣ�� 5���� �̻� 20���� ���Ͽ��� �Ѵ�.
		if(!isValidPwd(account.getPwd()))
			return signUpResult.INVALID_PWD;
		// �̸��� 2���� �̻� 5���� ����, �ѱ��̾�� �Ѵ�.
		if(!isValidAccountName(account.getAccountName()))
			return signUpResult.INVALID_NAME;
		// ������ ���� ��¥���� ���̾�� �Ѵ�.
		if(!isValidBirth(account.getBirth()))
			return signUpResult.INVALID_BIRTH;
		
		try {
			String SQL = "INSERT INTO Account (accountID, pwd, accountName, birth) VALUES (?, ?, ?, ?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
		    pstmt.setString(1,account.getAccountID());
		    pstmt.setString(2,account.getPwd());
		    pstmt.setString(3,account.getAccountName());
		    pstmt.setDate(4, OurTimes.LocalDateTosqlDate(account.getBirth()));
		    int result = pstmt.executeUpdate(); // -> SQL ������ ��쵵 �־�� �ϳ� ?
		    
		    if(result != 1)
		    	throw new SQLException("Affected row is " + result);
		    
		    if(studentIDRequestDAO.addReqSID(OurTimes.dateNow(), account.getAccountID())) {
		    	return signUpResult.SUCCESS;
		    }
		    else
		    	throw new SQLException("studentIDRequestDAO.addReqSID Failed.");
		}catch(SQLException e) {
		      throw e;
		}
		finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	}
	private boolean isValidAccountId(String id)
	{
		return id.length() >= 5 && id.length() <= 12 && id.matches("^[A-Za-z0-9]+$");

	}
	private boolean isValidPwd(String pwd)
	{
		return pwd.length() >= 5 && pwd.length() <= 20;
	}
	private boolean isValidAccountName(String name)
	{
		return name.length() >= 2 && name.length() <= 5 && name.matches("^[��-�R]+$");
	}
	private boolean isValidBirth(LocalDate birth)
	{
		return birth.compareTo(OurTimes.dateNow()) < 0;
	}

	/** 
	 * (2018-12-08 Ȯ��)
	 * �α��� - �ش� �α��������� �л����� �޾Ƶ鿩������ �����ڷ� �޾Ƶ鿩�������� �˷��ְ� ���� Ȱ��ȭ�� �Ѵ�.
	 * @param p_id ���̵�
	 * @param p_pwd ��й�ȣ
	 * @param p_session HTTP ���� ������ (���� Ȱ��ȭ�� ����)
	 * @return ���(LoginResult)
	 * @throws SQLException DB ����
	 * ������ ��쿡 ���� enum ����
	 * + ���� ������ �ش� �������� Ȱ��ȭ �߰� �ʿ�
	 * !DAO ���� - ������ �޶���, HttpSession p_session�߰�(����Ȱ��ȭ�� ����)
	 * !ClassDiagram ���� - ������ Ŭ���� �ʿ������.
	 */
	public loginResult login(String p_id, String p_pwd, HttpSession p_session) throws SQLException {
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
			
			// ��ȸ��� ���̵� ����
			if(!rs.next()) {
				return loginResult.NOT_FOUND_ID;	
			}
			
			String rsID = rs.getString("accountID");
			String rsPWD = rs.getString("pwd");
			
			// ��й�ȣ�� ���� ����
			if(!p_pwd.equals(rsPWD)) {
				return loginResult.INCORRECT_PWD;
			}
			
			// DB���� ���� ������ �缳��
			account.setAccountID(rsID);
			account.setPwd(rsPWD);
			
			// ���������� �˻�
			if(isAdmin(account.getAccountID()))
			{
				// �����ڷ� Ȱ��ȭ
				p_session.setAttribute("accountID", p_id);
				p_session.setAttribute("isAdmin", "yes");
				return loginResult.SUCCESS_ADMIN;
			}
			
			// �л����� �˻� (�й��ο��� ���� ���� �л� ���̺� �ö���� ���� �������� �˻��ϰ� �ȴ�.)
			// �� �� �й��� �� �� �ִ�.
			int sid = isStudent(account.getAccountID());
			if(sid == 0)
				return loginResult.FAIL_STUDENT;
			else {
				// �л����� Ȱ��ȭ
				p_session.setAttribute("accountID", p_id);
				p_session.setAttribute("sid", Integer.toString(sid));
				return loginResult.SUCCESS_STUDENT;
			}
				
		
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
	private int isStudent(String id) throws SQLException //�й��� ���� (0=�л��ƴ�, Ȥ�� �й��� ����)
	{
		String SQL = "SELECT *\r\n" + 
				"FROM Student\r\n" + 
				"WHERE accountID = ?;";
		pstmt = conn.prepareStatement(SQL);
		pstmt.setString(1, id);
		ResultSet rs = pstmt.executeQuery();
		
		if(rs.next())
		{
			return rs.getInt("studentID");
		}
		else
		{
			return 0;
		}
	}
	
	/** 
	 * �ű��й���û
	 * @param p_id ���̵�
	 * @param p_newStuYear ��ϳ⵵, 
	 * @param p_newStuOrder ��ϼ��� 
	 * @param p_dcode �а���ȣ
	 * @return �й�
	 * @throws SQLException DB����
	 * + StudentDAO.createNewStudent �Լ� ���� �߰�
	 * ! DAO ���� �ʿ� */
	public int requestSID(String p_id, int p_newStuYear, int p_newStuOrder, int p_dcode) throws SQLException
	{
		
	// �־��� ���̵��� �������� �������� -> acc -> �л� �̸��� �������� �ǳ��� ??
		try {
			String SQL = "SELECT * FROM Account A WHERE A.accountID = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, p_id);
			ResultSet rs = pstmt.executeQuery();
			
			// ��ȸ��� �������� ����
			if(!rs.next()) {
				return -1;	
			}
			
			String rsName = rs.getString("accountName"); // �л� �̸� �ޱ�
			
			// �й� ���� �Լ� ����
			return studentDAO.createNewStudent(p_newStuYear, p_newStuOrder, rsName, p_id, p_dcode);
		}catch(SQLException e){
	        throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
    	}
	}
	
}
