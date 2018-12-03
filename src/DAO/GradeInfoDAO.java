package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GradeInfoDAO extends DAOBase {

	// �����ͺ��̽� ������ ����
	private static PreparedStatement pstmt;
	private static Connection conn;
	
	public GradeInfoDAO() {
		super();
	}
	
	/** �����ο�
	 * @param p_attendancenum ������ȣ
	 * @param p_grade ����
	 * @return �������(boolean) */
	public boolean addGrade(int p_attendancenum, double p_grade) {
		
		try {
			String SQL = "INSERT INTO GradeInfo (grade, attendanceNum)" + 
					" VALUES (?, ?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_attendancenum);
			pstmt.setDouble(2, p_grade);
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
	
	/** ������������Ʈ ��ȸ 
	 * @param p_attnumlist ������ȣ����Ʈ
	 * @return ������������Ʈ(��������������, ����)*/


}
