package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

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
	 * @return �ű� ���ǰ�ȹ�� ��ȣ
	 * @throws SQLException DB����
	 * ! DAO���� �����ٶ� (��������(Boolean)���� ���ǰ�ȹ����ȣ(int)�� ���� ������ �ٲ����. text->syllabusText)
	 * */
	public int addSyllabus(String p_text) throws SQLException {
		try {
			String SQL = "INSERT INTO Syllabus (syllabusText) VALUES (?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, p_text);
			int result = pstmt.executeUpdate();
			
			// INSERT�� ����� �������� ���ϸ�
			if(result != 1)
				throw new SQLException("Inserting syllabus, but no rows affected.");
			
			// ���ǰ�ȹ����ȣ
			ResultSet generatedKeys = pstmt.getGeneratedKeys();
			if(!generatedKeys.next())
				throw new SQLException("Inserting syllabus was succes, but no syllabusCode obtained.");
			return generatedKeys.getInt(1);
		}
		catch(SQLException e){
	        throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	}
}
