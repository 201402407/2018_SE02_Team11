package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GradeInfoDAO extends DAOBase {

	// 데이터베이스 접근을 위해
	private static PreparedStatement pstmt;
	private static Connection conn;
	
	public GradeInfoDAO() {
		super();
	}
	
	/** 성적부여
	 * @param p_attendancenum 수강번호
	 * @param p_grade 평점
	 * @return 성공결과(boolean) */
	public boolean addGrade(int p_attendancenum, double p_grade) {
		
		try {
			String SQL = "INSERT INTO GradeInfo (grade, attendanceNum)" + 
					" VALUES (?, ?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_attendancenum);
			pstmt.setDouble(2, p_grade);
			int result = pstmt.executeUpdate(); 
			
			// SQL 실패
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
	
	/** 성적정보리스트 조회 
	 * @param p_attnumlist 수강번호리스트
	 * @return 성적정보리스트(평점보여짐여부, 평점)*/


}
