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
	
	// µ¥ÀÌÅÍº£ÀÌ½º Á¢±ÙÀ» À§ÇØ
	private PreparedStatement pstmt;
	private Connection conn;
	
	// Çù¾÷ÇÏ±â À§ÇÑ DAO Å¬·¡½º º¯¼ö ¼±¾ð
	StudentDAO studentDAO;
	StudentIDRequestDAO studentIDRequestDAO;
	
	public enum signUpResult { // È¸¿ø°¡ÀÔ °á°ú enum
		SUCCESS,
		INVALID_ID,
		INVALID_PWD,
		INVALID_NAME,
		INVALID_BIRTH
	}

	public enum loginResult { // ·Î±×ÀÎ °á°ú enum
		SUCCESS_STUDENT, //ÇÐ»ý ·Î±×ÀÎ
		FAIL_STUDENT, //ÇÐ¹øºÎ¿©¸¦ ¹ÞÁö ¸øÇÔ
		SUCCESS_ADMIN, //°ü¸®ÀÚ ·Î±×ÀÎ
		NOT_FOUND_ID,
		INCORRECT_PWD
}
	
	// »ý¼ºÀÚ »ý¼º°ú µ¿½Ã¿¡ jbdc ¼³Á¤.
	public AccountDAO() {
		super();
		
		studentDAO = new StudentDAO();
		studentIDRequestDAO = new StudentIDRequestDAO();
	}

	/* ÇØ´ç String ¹®ÀÚ¿­ ³»ºÎ¿¡ ¼ýÀÚ°¡ Á¸ÀçÇÏ´ÂÁö Ã¼Å© */
	public static boolean isIncludeNumber(String string) {
		for(int i = 0 ; i < string.length(); i ++)
	    {    
	        // 48 ~ 57Àº ¾Æ½ºÅ° ÄÚµå·Î 0~9ÀÌ´Ù.
	        if(48 <= string.charAt(i) && string.charAt(i) <= 57)
	            return true;
	    }
		return false;
	}
	
	/**
	 * È¸¿ø°¡ÀÔ
	 * @param p_id ¾ÆÀÌµð, 5±ÛÀÚ ÀÌ»ó 12±ÛÀÚ ÀÌÇÏ ¿µ¹®¿Í ¼ýÀÚ·Î ÀÌ·ç¾îÁ®¾ß ÇÑ´Ù.
	 * @param p_pwd ºñ¹Ð¹øÈ£, 5±ÛÀÚ ÀÌ»ó 20±ÛÀÚ ÀÌÇÏ¿©¾ß ÇÑ´Ù.
	 * @param p_name ÀÌ¸§, 2±ÛÀÚ ÀÌ»ó 5±ÛÀÚ ÀÌÇÏ ÇÑ±ÛÀÌ¾î¾ß ÇÑ´Ù.
	 * @param p_birth »ý³â¿ùÀÏ, ÇöÀç ³¯Â¥º¸´Ù ÀüÀÌ¾î¾ß ÇÑ´Ù.
	 * @return °á°ú(signUpResult).
	 * @throws SQLException DB¿À·ù
	 * °¢°¢ÀÇ °æ¿ì¿¡ µû¸¥ enum ¸®ÅÏ.
	 * ! DAO ¼öÁ¤ ¿Ï·á
	 */
	public signUpResult signUp(String p_id, String p_pwd, String p_name, LocalDate p_birth) throws SQLException {
		Account account = new Account();
		account.setAccountID(p_id);
		account.setPwd(p_pwd);
		account.setAccountName(p_name);
		account.setBirth(p_birth);
		
		// °èÁ¤Àº 5±ÛÀÚ ÀÌ»ó 12±ÛÀÚ ÀÌÇÏ, ¿µ¹®¿Í ¼ýÀÚ·Î ÀÌ·ç¾îÁ®¾ß ÇÑ´Ù.
		if(!isValidAccountId(account.getAccountID()))
			return signUpResult.INVALID_ID;
		// ºñ¹Ð¹øÈ£´Â 5±ÛÀÚ ÀÌ»ó 20±ÛÀÚ ÀÌÇÏ¿©¾ß ÇÑ´Ù.
		if(!isValidPwd(account.getPwd()))
			return signUpResult.INVALID_PWD;
		// ÀÌ¸§Àº 2±ÛÀÚ ÀÌ»ó 5±ÛÀÚ ÀÌÇÏ, ÇÑ±ÛÀÌ¾î¾ß ÇÑ´Ù.
		if(!isValidAccountName(account.getAccountName()))
			return signUpResult.INVALID_NAME;
		// »ýÀÏÀÌ ÇöÀç ³¯Â¥º¸´Ù ÀüÀÌ¾î¾ß ÇÑ´Ù.
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
		    int result = pstmt.executeUpdate(); // -> SQL ½ÇÆÐÇÑ °æ¿ìµµ ³Ö¾î¾ß ÇÏ³ª ?
		    
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
		return name.length() >= 2 && name.length() <= 5 && name.matches("^[°¡-ÆR]+$");
	}
	private boolean isValidBirth(LocalDate birth)
	{
		return birth.compareTo(OurTimes.dateNow()) < 0;
	}

	/** 
	 * (2018-12-08 È®ÀÎ)
	 * ·Î±×ÀÎ - ÇØ´ç ·Î±×ÀÎÁ¤º¸°¡ ÇÐ»ýÀ¸·Î ¹Þ¾Æµé¿©Áö´ÂÁö °ü¸®ÀÚ·Î ¹Þ¾Æµé¿©Áö´ÂÁö¸¦ ¾Ë·ÁÁÖ°í ¼¼¼Ç È°¼ºÈ­¸¦ ÇÑ´Ù.
	 * @param p_id ¾ÆÀÌµð
	 * @param p_pwd ºñ¹Ð¹øÈ£
	 * @param p_session HTTP ¼¼¼Ç µ¥ÀÌÅÍ (¼¼¼Ç È°¼ºÈ­¸¦ À§ÇÔ)
	 * @return °á°ú(LoginResult)
	 * @throws SQLException DB ¿À·ù
	 * °¢°¢ÀÇ °æ¿ì¿¡ µû¸¥ enum ¸®ÅÏ
	 * + ÇöÀç ¼¼¼ÇÀ» ÇØ´ç °èÁ¤À¸·Î È°¼ºÈ­ Ãß°¡ ÇÊ¿ä
	 * !DAO ¼öÁ¤ - ¸®ÅÏÀÌ ´Þ¶óÁü, HttpSession p_sessionÃß°¡(¼¼¼ÇÈ°¼ºÈ­¸¦ À§ÇÔ)
	 * !ClassDiagram ¼öÁ¤ - °ü¸®ÀÚ Å¬·¡½º ÇÊ¿ä¾ø¾îÁü.
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
			
			// Á¶È¸°á°ú ¾ÆÀÌµð ¾øÀ½
			if(!rs.next()) {
				return loginResult.NOT_FOUND_ID;	
			}
			
			String rsID = rs.getString("accountID");
			String rsPWD = rs.getString("pwd");
			
			// ºñ¹Ð¹øÈ£°¡ ¸ÂÁö ¾ÊÀ½
			if(!p_pwd.equals(rsPWD)) {
				return loginResult.INCORRECT_PWD;
			}
			
			// DB¿¡¼­ ¹ÞÀº Á¤º¸·Î Àç¼³Á¤
			account.setAccountID(rsID);
			account.setPwd(rsPWD);
			
			// °ü¸®ÀÚÀÎÁö °Ë»ç
			if(isAdmin(account.getAccountID()))
			{
				// °ü¸®ÀÚ·Î È°¼ºÈ­
				p_session.setAttribute("accountID", p_id);
				p_session.setAttribute("isAdmin", "yes");
				return loginResult.SUCCESS_ADMIN;
			}
			
			// ÇÐ»ýÀÎÁö °Ë»ç (ÇÐ¹øºÎ¿©¸¦ ¹ÞÁö ¸øÇØ ÇÐ»ý Å×ÀÌºí¿¡ ¿Ã¶ó¿ÀÁö ¾ÊÀº »óÅÂÀÎÁö °Ë»çÇÏ°Ô µÈ´Ù.)
			// ÀÌ ¶§ ÇÐ¹øµµ ¾Ë ¼ö ÀÖ´Ù.
			int sid = isStudent(account.getAccountID());
			if(sid == 0)
				return loginResult.FAIL_STUDENT;
			else {
				// ÇÐ»ýÀ¸·Î È°¼ºÈ­
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
	private int isStudent(String id) throws SQLException //ÇÐ¹øÀ» ¸®ÅÏ (0=ÇÐ»ý¾Æ´Ô, È¤Àº ÇÐ¹øÀÌ ¾øÀ½)
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
	 * ½Å±ÔÇÐ¹ø¿äÃ»
	 * @param p_id ¾ÆÀÌµð
	 * @param p_newStuYear µî·Ï³âµµ, 
	 * @param p_newStuOrder µî·Ï¼ø¼­ 
	 * @param p_dcode ÇÐ°ú¹øÈ£
	 * @return ÇÐ¹ø
	 * @throws SQLException DB¿À·ù
	 * + StudentDAO.createNewStudent ÇÔ¼ö ½ÇÇà Ãß°¡
	 * ! DAO ¼öÁ¤ ÇÊ¿ä */
	public int requestSID(String p_id, int p_newStuYear, int p_newStuOrder, int p_dcode) throws SQLException
	{
		
	// ÁÖ¾îÁø ¾ÆÀÌµðÀÇ °èÁ¤Á¤º¸ °¡Á®¿À±â -> acc -> ÇÐ»ý ÀÌ¸§¸¸ °¡Á®¿À¸é µÇ³ª¿ä ??
		try {
			String SQL = "SELECT * FROM Account A WHERE A.accountID = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, p_id);
			ResultSet rs = pstmt.executeQuery();
			
			// Á¶È¸°á°ú °èÁ¤Á¤º¸ ¾øÀ½
			if(!rs.next()) {
				return -1;	
			}
			
			String rsName = rs.getString("accountName"); // ÇÐ»ý ÀÌ¸§ ¹Þ±â
			
			// ÇÐ¹ø »ý¼º ÇÔ¼ö ½ÇÇà
			return studentDAO.createNewStudent(p_newStuYear, p_newStuOrder, rsName, p_id, p_dcode);
		}catch(SQLException e){
	        throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
    	}
	}
	
}
