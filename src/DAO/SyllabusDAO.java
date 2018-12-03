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
	 * ! DAO �˰��� syllabusText�� ����*/
	public String getTextBySYLCode(int p_sylcode) {
		
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
		}catch(Exception e){
	        e.printStackTrace();
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	return null;
	}
	
	/** ���ǰ�ȹ�� ���
	 * @param p_text ����
	 * @return ��������(boolean)
	 * */
	public boolean addSyllabus(String p_text) {
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
			
		}catch(Exception e){
	        e.printStackTrace();
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
		return false;
	}
}
