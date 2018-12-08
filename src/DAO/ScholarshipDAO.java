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
		INVALID_SCNAME,
		ANOMAL
	}
	/** 
	 * 장학존재여부조회
	 * @param p_scnum 장학번호
	 * @return 존재여부(boolean)
	 * @throws SQLException DB오류*/
	public boolean isScholarshipExist(int p_scnum) throws SQLException {
		try {
			String SQL = "SELECT * FROM Scholarship WHERE scholarshipNum = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_scnum);
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
	
	/** 장학추가
	 * @param p_scname 장학이름, 2자 이상 20자 이하여야 한다.
	 * @return 성공여부결과(addScholarshipResult)
	 * @throws SQLException DB오류
	 * ! String 문자열이 인자로 들어오면 좌우 공백처리는 어떻게 ? 
	 * ! SQL 실패하는 경우의 enum DAO에 추가 필요 */
	public addScholarshipResult addScholarship(String p_scname) throws SQLException{
		
		p_scname = p_scname.trim(); // 좌우 공백제거
		if( !(p_scname.length() >= 2 && p_scname.length() <= 20) )
			return addScholarshipResult.INVALID_SCNAME;
		
		try {
			String SQL = "INSERT INTO Scholarship (scholarshipName) VALUES (?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, p_scname);
			int result = pstmt.executeUpdate(); 
			
			// SQL 실패
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
	
	/** 모든장학조회 
	 * @return 장학정보리스트(장학번호, 장학이름)
	 * @throws SQLException DB오류 
	 * ! DAO 조회결과 존재 X 수정 필요 */
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
