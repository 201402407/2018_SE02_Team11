package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SyllabusDAO extends DAOBase {

	// 데이터베이스 접근을 위해
	private static PreparedStatement pstmt;
	private static Connection conn;
	
	public SyllabusDAO() {
		super();
	}

	/** 강의계획서 조회
	 * @param p_sylcode 강의계획서번호
	 * @return 강의계획서내용(String)
	 * @throws SQLException DB오류
	 * ! DAO 알고리즘 syllabusText로 변경*/
	public String getTextBySYLCode(int p_sylcode) throws SQLException {
		
		try {
			String SQL = "SELECT syllabusText FROM Syllabus WHERE syllabusCode = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_sylcode);
			ResultSet rs = pstmt.executeQuery();
			
			// 조회결과 없음 
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
	
	/** 강의계획서 등록
	 * @param p_text 내용
	 * @return 성공여부(boolean)
	 * @throws SQLException DB오류
	 * */
	public boolean addSyllabus(String p_text) throws SQLException {
		try {
			String SQL = "INSERT INTO Syllabus (text) VALUES (?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, p_text);
			int result = pstmt.executeUpdate(); 
			
			// SQL 실패
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
