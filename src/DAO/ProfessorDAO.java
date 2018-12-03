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
	
	/* 교수이름조회 */
	public String getProfNameByPCode(int p_profcode) {
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
			
		}catch(Exception e){
	        e.printStackTrace();
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
		return null;
	}
	
	/* 교수 추가 */
	public boolean addProfessor(String p_profname) {
		// 숫자가 포함되어 있는지 여부
		if(isIncludeNumber(p_profname)) {
			return false;
		}	
		try {
			String SQL = "INSERT INTO Professor (profName) VALUES (?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, p_profname);
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
	
	/* 교수존재여부조회 */
	public boolean IsProfessorByPCode(int p_profcode) {
		try {
			String SQL = "SELECT * FROM Professor WHERE professorCode = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_profcode);
			ResultSet rs = pstmt.executeQuery();
			
			// 조회결과 없음 
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
}
