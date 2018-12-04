package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SyllabusDAO extends DAOBase {

	// �����ͺ��̽� ������ ����
	private static PreparedStatement pstmt;
	private static Connection conn;
	
	public SyllabusDAO() {
		super();
	}

	/** ���ǰ�ȹ�� ��ȸ
	 * @param p_sylcode ���ǰ�ȹ����ȣ
	 * @return ���ǰ�ȹ������(String)
	 * @throws SQLException DB����
	 * ! DAO �˰��� syllabusText�� ����*/
	public String getTextBySYLCode(int p_sylcode) throws SQLException {
		
		try {
			String SQL = "SELECT syllabusText FROM Syllabus WHERE syllabusCode = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_sylcode);
			ResultSet rs = pstmt.executeQuery();
			
			// ��ȸ��� ���� 
			if(!rs.next()) 
				return null;	
			
			return rs.getString("syllabusText");
		}catch(SQLException e){
	        throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	}
	
	/** ���ǰ�ȹ�� ���
	 * @param p_text ����
	 * @return ��������(boolean)
	 * @throws SQLException DB����
	 * */
	public boolean addSyllabus(String p_text) throws SQLException {
		try {
			String SQL = "INSERT INTO Syllabus (text) VALUES (?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, p_text);
			int result = pstmt.executeUpdate(); 
			
			// SQL ����
			if(result != 1)
				return false;
			
			return true;
			
		}catch(SQLException e){
	        throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	}
}
