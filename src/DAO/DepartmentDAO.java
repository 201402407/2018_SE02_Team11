package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DepartmentDAO extends DAOBase {
	// �����ͺ��̽� ������ ����
	private PreparedStatement pstmt;
	private Connection conn;
	
	public DepartmentDAO() {
		super();
	}
	
	/**
	 * �л��а��ڵ���ȸ: �ش��ϴ� �л��� �а��ڵ带 ���Ѵ�.
	 * @param p_sid �й�
	 * @return �а��ڵ�. �ش��ϴ� �л��� ������ (�־��� �й��� �����Ͱ� ������) -1
	 * @throws SQLException DB����
	 * ! DAO ���� �ʿ� (-1�� ��ȯ�� �� �ִٴ� ���̴�.)
	 */
	public int getDCodeBySID(int p_sid) throws SQLException
	{
		//���: �а��ڵ�
		int dcode;
		
		try {
			// DB ����
			conn = getConnection();
			// ������ ����
			String sql = "SELECT departmentCode FROM Student\r\n" + 
					"WHERE studentID = ?;";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, p_sid);
			// ���� ����
			ResultSet rs = pstmt.executeQuery();
			// ����ϴ� rs: Row �ϳ��� Column�� departmentCode ���� �ϳ�.
			// ���� rs ó��...
			
			// �ϳ��� ������
			if(!rs.next())
				dcode = -1;
			// ������
			else
				dcode = rs.getInt("departmentCode");
			
			// ��� ����
			return dcode;
		}
		catch(SQLException sqle) {
			throw sqle;
		} finally {
			if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		    if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		
	}
	
	/**
	 * �а����翩����ȸ
	 * @param p_dcode �а��ڵ�
	 * @return ���翩��
	 * @throws SQLException DB����
	 */
	public boolean isDepartmentExistByDCode(int p_dcode) throws SQLException
	{
		//���: ���翩��
		boolean isExist;
		
		try {
			// DB ����
			conn = getConnection();
			// ������ ����
			String sql = "SELECT * FROM Department WHERE departmentCode = ?;";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, p_dcode);
			// ���� ����
			ResultSet rs = pstmt.executeQuery();
			// ����ϴ� rs: Row �ϳ�, Column�� Department�� �ش��ϴ� �� ���.
			// ���� rs ó��...
			
			isExist = rs.next();
			
			// ��� ����
			return isExist;
		}
		catch(SQLException sqle) {
			throw sqle;
		} finally {
			if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		    if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	}
}
