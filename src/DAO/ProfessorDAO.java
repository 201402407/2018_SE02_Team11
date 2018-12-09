package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfessorDAO extends DAOBase {

	// µ¥ÀÌÅÍº£ÀÌ½º Á¢±ÙÀ» À§ÇØ
	private static PreparedStatement pstmt;
	private static Connection conn;
	
	public ProfessorDAO() {
		super();
	}
	
	public enum AddProfessorResult {
		SUCCESS,
		INVALID_NAME
	}
	/* ÇØ´ç String ¹®ÀÚ¿­ ³»ºÎ¿¡ ¼ıÀÚ°¡ Á¸ÀçÇÏ´ÂÁö Ã¼Å© */
	public static boolean isIncludeNumber(String string) {
		for(int i = 0 ; i < string.length(); i ++)
	    {    
	        // 48 ~ 57Àº ¾Æ½ºÅ° ÄÚµå·Î 0~9ÀÌ´Ù.
	        if(48 <= string.charAt(i) && string.charAt(i) <= 57)
	            return true;
	    }
		return false;
	}
	
	/** ±³¼öÀÌ¸§Á¶È¸ 
	 * @param p_profcode ±³¼öµî·Ï¹øÈ£
	 * @return ±³¼öÀÌ¸§(String)
	 * @throws SQLException DB¿À·ù
	 * ! DAO Á¶È¸°á°ú ¾øÀ½ Ç¥½Ã ÇÊ¿ä
	 * */
	public String getProfNameByPCode(int p_profcode) throws SQLException {
		try {
			String SQL = "SELECT profName FROM Professor WHERE professorCode = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_profcode);
			ResultSet rs = pstmt.executeQuery();
			
			// Á¶È¸°á°ú ¾øÀ½ 
			if(!rs.next()) {
				return null;	
			}
			
			return rs.getString("profName");
			
		}catch(SQLException e){
	        throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	}
	
	/** ±³¼ö Ãß°¡ 
	 * @param p_name ±³¼öÀÌ¸§, 2±ÛÀÚ ÀÌ»ó 5±ÛÀÚ ÀÌÇÏ ÇÑ±ÛÀÌ¾î¾ß ÇÑ´Ù.
	 * @return ±³¼öÃß°¡°á°ú(boolean)
	 * @throws SQLException DB¿À·ù
	 * ! DAO °æ¿ì¿¡µû¸¥°á°ú Ãß°¡ ÇÊ¿ä
	 * ! DAO enum ¸®ÅÏ°ª º¯°æ ÇÊ¿ä
	 * */
	public AddProfessorResult addProfessor(String p_profname) throws SQLException {
		// ¼ıÀÚ°¡ Æ÷ÇÔµÇ¾î ÀÖ´ÂÁö ¿©ºÎ
		if(!isValidProfName(p_profname)) {
			return AddProfessorResult.INVALID_NAME;
		}	
		try {
			String SQL = "INSERT INTO Professor (profName) VALUES (?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, p_profname);
			int result = pstmt.executeUpdate(); 
			
			// SQL ½ÇÆĞ
			if(result != 1)
				throw new SQLException("Affected Rows: " + result);
			
			return AddProfessorResult.SUCCESS;
			
		}catch(SQLException e){
	        throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	}
	private boolean isValidProfName(String name)
	{
		return name.length() >= 2 && name.length() <= 5 && name.matches("^[°¡-ÆR]+$");
	}
	
	/** ±³¼öÁ¸Àç¿©ºÎÁ¶È¸ 
	 * @param p_profcode ±³¼öµî·Ï¹øÈ£
	 * @return Á¶È¸°á°ú(boolean)
	 * @throws SQLException DB¿À·ù
	 * ! DAO À§¿À ¤¿¸¶Âù°¡Áö*/
	public boolean IsProfessorByPCode(int p_profcode) throws SQLException{
		try {
			String SQL = "SELECT * FROM Professor WHERE professorCode = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_profcode);
			ResultSet rs = pstmt.executeQuery();
			
			// Á¶È¸°á°ú Á¸Àç 
			if(rs.next()) 
				return true;	
		return false;	
		}catch(SQLException e){
	        throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	}
}
