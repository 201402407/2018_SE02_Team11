package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DepartmentDAO extends DAOBase {
	// 데이터베이스 접근을 위해
	private PreparedStatement pstmt;
	private Connection conn;
	
	public DepartmentDAO() {
		super();
	}
	
	/**
	 * 학생학과코드조회: 해당하는 학생의 학과코드를 구한다.
	 * @param p_sid 학번
	 * @return 학과코드. 해당하는 학생이 없으면 (주어진 학번의 데이터가 없으면) -1
	 * @throws SQLException DB오류
	 * ! DAO 수정 필요 (-1을 반환할 수 있다는 말이다.)
	 */
	public int getDCodeBySID(int p_sid) throws SQLException
	{
		//결과: 학과코드
		int dcode;
		
		try {
			// DB 연결
			conn = getConnection();
			// 쿼리문 설정
			String sql = "SELECT departmentCode FROM Student\r\n" + 
					"WHERE studentID = ?;";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, p_sid);
			// 쿼리 실행
			ResultSet rs = pstmt.executeQuery();
			// 기대하는 rs: Row 하나에 Column은 departmentCode 오직 하나.
			// 이후 rs 처리...
			
			// 하나도 없으면
			if(!rs.next())
				dcode = -1;
			// 있으면
			else
				dcode = rs.getInt("departmentCode");
			
			// 결과 리턴
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
	 * 학과존재여부조회
	 * @param p_dcode 학과코드
	 * @return 존재여부
	 * @throws SQLException DB오류
	 */
	public boolean isDepartmentExistByDCode(int p_dcode) throws SQLException
	{
		//결과: 존재여부
		boolean isExist;
		
		try {
			// DB 연결
			conn = getConnection();
			// 쿼리문 설정
			String sql = "SELECT * FROM Department WHERE departmentCode = ?;";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, p_dcode);
			// 쿼리 실행
			ResultSet rs = pstmt.executeQuery();
			// 기대하는 rs: Row 하나, Column은 Department에 해당하는 것 모두.
			// 이후 rs 처리...
			
			isExist = rs.next();
			
			// 결과 리턴
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
