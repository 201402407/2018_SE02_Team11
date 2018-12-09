package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

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
	 * @return 신규 강의계획서 번호
	 * @throws SQLException DB오류
	 * ! DAO명세서 수정바람 (성공여부(Boolean)에서 강의계획서번호(int)로 리턴 형식이 바뀌었음. text->syllabusText)
	 * */
	public int addSyllabus(String p_text) throws SQLException {
		try {
			String SQL = "INSERT INTO Syllabus (syllabusText) VALUES (?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, p_text);
			int result = pstmt.executeUpdate();
			
			// INSERT가 제대로 행해지지 못하면
			if(result != 1)
				throw new SQLException("Inserting syllabus, but no rows affected.");
			
			// 강의계획서번호
			ResultSet generatedKeys = pstmt.getGeneratedKeys();
			if(!generatedKeys.next())
				throw new SQLException("Inserting syllabus was succes, but no syllabusCode obtained.");
			return generatedKeys.getInt(1);
		}
		catch(SQLException e){
	        throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	}
}
