package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfessorDAO extends DAOBase {

	// �����ͺ��̽� ������ ����
	private static PreparedStatement pstmt;
	private static Connection conn;
	
	public ProfessorDAO() {
		super();
	}
	
	public enum AddProfessorResult {
		SUCCESS,
		INVALID_NAME
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
	
	/** �����̸���ȸ 
	 * @param p_profcode ������Ϲ�ȣ
	 * @return �����̸�(String)
	 * @throws SQLException DB����
	 * ! DAO ��ȸ��� ���� ǥ�� �ʿ�
	 * */
	public String getProfNameByPCode(int p_profcode) throws SQLException {
		try {
			String SQL = "SELECT profName FROM Professor WHERE professorCode = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_profcode);
			ResultSet rs = pstmt.executeQuery();
			
			// ��ȸ��� ���� 
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
	
	/** ���� �߰� 
	 * @param p_name �����̸�, 2���� �̻� 5���� ���� �ѱ��̾�� �Ѵ�.
	 * @return �����߰����(boolean)
	 * @throws SQLException DB����
	 * ! DAO ��쿡������� �߰� �ʿ�
	 * ! DAO enum ���ϰ� ���� �ʿ�
	 * */
	public AddProfessorResult addProfessor(String p_profname) throws SQLException {
		// ���ڰ� ���ԵǾ� �ִ��� ����
		if(!isValidProfName(p_profname)) {
			return AddProfessorResult.INVALID_NAME;
		}	
		try {
			String SQL = "INSERT INTO Professor (profName) VALUES (?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, p_profname);
			int result = pstmt.executeUpdate(); 
			
			// SQL ����
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
		return name.length() >= 2 && name.length() <= 5 && name.matches("^[��-�R]+$");
	}
	
	/** �������翩����ȸ 
	 * @param p_profcode ������Ϲ�ȣ
	 * @return ��ȸ���(boolean)
	 * @throws SQLException DB����
	 * ! DAO ���� ����������*/
	public boolean IsProfessorByPCode(int p_profcode) throws SQLException{
		try {
			String SQL = "SELECT * FROM Professor WHERE professorCode = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_profcode);
			ResultSet rs = pstmt.executeQuery();
			
			// ��ȸ��� ���� 
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
