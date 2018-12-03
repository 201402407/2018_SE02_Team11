package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ClassObject.ScholarShip;

public class ScholarshipDAO extends DAOBase {
	// 데이터베이스 접근을 위해
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
	/** 장학존재여부조회
	 * @param p_scnum 장학번호
	 * @return 존재여부(boolean)*/
	public boolean isScholarshipExist(int p_scnum) {
		try {
			String SQL = "SELECT * FROM Scholarship WHERE scholarshipNum = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_scnum);
			ResultSet rs = pstmt.executeQuery();
			
			// 조회결과 존재 
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
	
	/** 장학추가
	 * @param p_scname 장학이름
	 * @return 성공여부결과(addScholarshipResult)
	 * ! String 문자열이 인자로 들어오면 좌우 공백처리는 어떻게 ? 
	 * ! SQL 실패하는 경우의 enum DAO에 추가 필요 */
	public addScholarshipResult addScholarship(String p_scname) {
		
		// null이거나 ""인 경우
		if(p_scname.equals(null) || p_scname.trim().equals(""))
			return addScholarshipResult.MISSING_FIELD;
		p_scname = p_scname.trim(); // 좌우 공백제거
		try {
			String SQL = "INSERT INTO Scholar (scholarshipName) VALUES (?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, p_scname);
			int result = pstmt.executeUpdate(); 
			
			// SQL 실패
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
	
	/** 모든장학조회 
	 * @return 장학정보리스트(장학번호, 장학이름) 
	 * ! DAO 조회결과 존재 X 수정 필요 */
	public List<ScholarShip> getScholarshipList() {
		List<ScholarShip> scholarShips = new ArrayList<>();
		try {
			String SQL = "SELECT scholarshipNum, scholarshipName FROM Scholarship";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			ResultSet rs = pstmt.executeQuery();
			
			// 조회결과 존재 X 
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
