package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ClassObject.ScholarShip;

public class ScholarshipDAO extends DAOBase {
	// �����ͺ��̽� ������ ����
	private static PreparedStatement pstmt;
	private static Connection conn;
	
	public ScholarshipDAO() {
		super();
	}
	
	public enum addScholarshipResult {
		SUCCESS,
		INVALID_SCNAME,
		ANOMAL
	}
	/** 
	 * �������翩����ȸ
	 * @param p_scnum ���й�ȣ
	 * @return ���翩��(boolean)
	 * @throws SQLException DB����*/
	public boolean isScholarshipExist(int p_scnum) throws SQLException {
		try {
			String SQL = "SELECT * FROM Scholarship WHERE scholarshipNum = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_scnum);
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
	
	/** �����߰�
	 * @param p_scname �����̸�, 2�� �̻� 20�� ���Ͽ��� �Ѵ�.
	 * @return �������ΰ��(addScholarshipResult)
	 * @throws SQLException DB����
	 * ! String ���ڿ��� ���ڷ� ������ �¿� ����ó���� ��� ? 
	 * ! SQL �����ϴ� ����� enum DAO�� �߰� �ʿ� */
	public addScholarshipResult addScholarship(String p_scname) throws SQLException{
		
		p_scname = p_scname.trim(); // �¿� ��������
		if( !(p_scname.length() >= 2 && p_scname.length() <= 20) )
			return addScholarshipResult.INVALID_SCNAME;
		
		try {
			String SQL = "INSERT INTO Scholarship (scholarshipName) VALUES (?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, p_scname);
			int result = pstmt.executeUpdate(); 
			
			// SQL ����
			if(result != 1)
				return addScholarshipResult.ANOMAL;
			
			return addScholarshipResult.SUCCESS;
		}catch(SQLException e){
	        throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	 
	}
	
	/** ���������ȸ 
	 * @return ������������Ʈ(���й�ȣ, �����̸�)
	 * @throws SQLException DB���� 
	 * ! DAO ��ȸ��� ���� X ���� �ʿ� */
	public List<ScholarShip> getScholarshipList() throws SQLException{
		List<ScholarShip> scholarShips = new ArrayList<>();
		try {
			String SQL = "SELECT scholarshipNum, scholarshipName FROM Scholarship";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int rsScholarshipNum = rs.getInt("scholarshipNum");
				String rsScholarshipName = rs.getString("scholarshipName");
				
				ScholarShip scholarShip = new ScholarShip(
						rsScholarshipNum,
						rsScholarshipName
						);
				scholarShips.add(scholarShip);
			}
			
			return scholarShips;
		}catch(SQLException e){
	        throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	}
}
