package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfessorDAO extends DAOBase {

	// 데이터베이스 접근을 위해
	private static PreparedStatement pstmt;
	private static Connection conn;
	
	public ProfessorDAO() {
		super();
	}
	
	public enum AddProfessorResult {
		SUCCESS,
		INVALID_NAME,
		INSERT_NULL
	}
	/* 해당 String 문자열 내부에 숫자가 존재하는지 체크 */
	public static boolean isIncludeNumber(String string) {
		for(int i = 0 ; i < string.length(); i ++)
	    {    
	        // 48 ~ 57은 아스키 코드로 0~9이다.
	        if(48 <= string.charAt(i) && string.charAt(i) <= 57)
	            return true;
	    }
		return false;
	}
	
	/** 교수이름조회 
	 * @param p_profcode 교수등록번호
	 * @return 교수이름(String)
	 * @throws SQLException DB오류
	 * ! DAO 조회결과 없음 표시 필요
	 * */
	public String getProfNameByPCode(int p_profcode) throws SQLException {
		try {
			String SQL = "SELECT profName FROM Professor WHERE professorCode = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_profcode);
			ResultSet rs = pstmt.executeQuery();
			
			// 조회결과 없음 
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
	
	/** 교수 추가 
	 * @param p_name 교수이름
	 * @return 교수추가결과(boolean)
	 * @throws SQLException DB오류
	 * ! DAO 경우에따른결과 추가 필요
	 * ! DAO enum 리턴값 변경 필요
	 * */
	public AddProfessorResult addProfessor(String p_profname) throws SQLException {
		// 숫자가 포함되어 있는지 여부
		if(isIncludeNumber(p_profname)) {
			return AddProfessorResult.INVALID_NAME;
		}	
		try {
			String SQL = "INSERT INTO Professor (profName) VALUES (?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, p_profname);
			int result = pstmt.executeUpdate(); 
			
			// SQL 실패
			if(result != 1)
				return AddProfessorResult.INSERT_NULL;
			
			return AddProfessorResult.SUCCESS;
			
		}catch(SQLException e){
	        throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	}
	
	/** 교수존재여부조회 
	 * @param p_profcode 교수등록번호
	 * @return 조회결과(boolean)
	 * @throws SQLException DB오류
	 * ! DAO 위오 ㅏ마찬가지*/
	public boolean IsProfessorByPCode(int p_profcode) throws SQLException{
		try {
			String SQL = "SELECT * FROM Professor WHERE professorCode = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_profcode);
			ResultSet rs = pstmt.executeQuery();
			
			// 조회결과 존재 
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
