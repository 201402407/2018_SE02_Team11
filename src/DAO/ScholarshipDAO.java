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
		MISSING_FIELD,
		SQL_FAILED
	}
	/** �������翩����ȸ
	 * @param p_scnum ���й�ȣ
	 * @return ���翩��(boolean)*/
	public boolean isScholarshipExist(int p_scnum) {
		try {
			String SQL = "SELECT * FROM Scholarship WHERE scholarshipNum = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_scnum);
			ResultSet rs = pstmt.executeQuery();
			
			// ��ȸ��� ���� 
			if(rs.next()) 
				return true;	
			
		}catch(Exception e){
	        e.printStackTrace();
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
		return false;
	}
	
	/** �����߰�
	 * @param p_scname �����̸�
	 * @return �������ΰ��(addScholarshipResult)
	 * ! String ���ڿ��� ���ڷ� ������ �¿� ����ó���� ��� ? 
	 * ! SQL �����ϴ� ����� enum DAO�� �߰� �ʿ� */
	public addScholarshipResult addScholarship(String p_scname) {
		
		// null�̰ų� ""�� ���
		if(p_scname.equals(null) || p_scname.trim().equals(""))
			return addScholarshipResult.MISSING_FIELD;
		p_scname = p_scname.trim(); // �¿� ��������
		try {
			String SQL = "INSERT INTO Scholar (scholarshipName) VALUES (?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, p_scname);
			int result = pstmt.executeUpdate(); 
			
			// SQL ����
			if(result != 1)
				return addScholarshipResult.SQL_FAILED;
			
			return addScholarshipResult.SUCCESS;
		}catch(Exception e){
	        e.printStackTrace();
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
		return addScholarshipResult.MISSING_FIELD; 
	}
	
	/** ���������ȸ 
	 * @return ������������Ʈ(���й�ȣ, �����̸�) 
	 * ! DAO ��ȸ��� ���� X ���� �ʿ� */
	public List<ScholarShip> getScholarshipList() {
		List<ScholarShip> scholarShips = new ArrayList<>();
		try {
			String SQL = "SELECT scholarshipNum, scholarshipName FROM Scholarship";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			ResultSet rs = pstmt.executeQuery();
			
			// ��ȸ��� ���� X 
			if(!rs.next()) 
				return null;	
			
			rs.beforeFirst();
			
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
		}catch(Exception e){
	        e.printStackTrace();
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
		return null;
	}
}
