package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;

import ClassObject.StudentIDRequest;
import Util.OurTimes;

public class StudentIDRequestDAO extends DAOBase {
	
	static Connection conn;
	static PreparedStatement pstmt;
	
	// 생성자 생성과 동시에 jbdc 설정.
	public StudentIDRequestDAO() {
		super();
	}
	
	public boolean isIncludeNumber(String string) {
		for(int i = 0 ; i < string.length(); i ++)
	    {    
	        // 48 ~ 57은 아스키 코드로 0~9이다.
	        if(48 <= string.charAt(i) && string.charAt(i) <= 57)
	            return true;
	    }
		return false;
	}
	
	/** 학번요청추가 
	 * @param p_date 요청일자
	 * @param p_accountID 계정아이디
	 * @return 요청추가성공여부(boolean)
	 * @throws SQLException DB오류
	 * ! DAO SQL 실패 경우 추가
	 * */
	public boolean addReqSID(LocalDate p_date, String p_accountID) throws SQLException { // 아이디 이므로 accountID로 이름 변경
		StudentIDRequest studentIDRequest = new StudentIDRequest();
		studentIDRequest.setReqSIDdate(p_date);
		studentIDRequest.setAccountID(p_accountID);

		try {
			String SQL = "INSERT INTO StudentIDRequest VALUES (?, ?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
		    pstmt.setDate(1,OurTimes.LocalDateTosqlDate(studentIDRequest.getReqSIDdate()));
		    pstmt.setString(2,studentIDRequest.getAccountID());
		    int result = pstmt.executeUpdate();
		    if(result != 1)
		    	return false;
		    
		    return true;
		}catch(SQLException e) {
		      throw e;
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		
	}
	
	/** 학번요청대기열목록조회 
	 * @return 학번요청대기열목록(요청번호, 요청일자, 아이디)
	 * @throws SQLException DB오류
	 * ! rs.beforeFirst() 테스트해보기
	 * */
	public ArrayList<StudentIDRequest> getReqSIDList() throws SQLException { // DAO 명세서에 ArrayList로 표기하는지 ?
		ArrayList<StudentIDRequest> resultArrayList = new ArrayList<StudentIDRequest>();
		
		try {
			String SQL = "SELECT * FROM StudentIDRequest";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			ResultSet rs = pstmt.executeQuery(); // ResultSet
			
			// 조회결과 아무것도 없음
			if(!rs.next()) {
				return null;	
			}
			
			rs.beforeFirst(); // 첫 행으로 이동  -> 이게 맞나 ?
			
			// 목록 꺼내오기
			while(rs.next()) {
				int rsReqSIDnum = rs.getInt("reqSIDnum");
				LocalDate rsDate = OurTimes.sqlDateToLocalDate(rs.getDate("reqSIDdate"));
				String rsAccountID = rs.getString("accountID");
				
				StudentIDRequest studentIDRequest = new StudentIDRequest();
				studentIDRequest.setReqSIDnum(rsReqSIDnum);
				studentIDRequest.setReqSIDdate(rsDate);
				studentIDRequest.setAccountID(rsAccountID);
				
				resultArrayList.add(studentIDRequest); // ArrayList로 담기
			}
		   	
			return resultArrayList;
			
		}catch(SQLException e) {
		      throw e;
		      
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	}
	
	/** 학번부여허가
	 * @param p_reqnum 요청번호
	 * @param p_dcode 학과코드
	 * @return 부여허가결과(boolean)
	 * @throws SQLException DB오류
	 * + 요청날짜 사용하는 법 수정 필요
	 * ! DAO 반환값 수정 필요 */
	public boolean permitReqSID(int p_reqnum, int p_dcode) throws SQLException{
		
		try {
			// 해당 요청번호의 학번을 불러온다.
			String SQL = "SELECT * FROM StudentIDRequest WHERE reqSIDnum = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_reqnum);
			ResultSet rs = pstmt.executeQuery(); // ResultSet
			
			// 조회결과 아무것도 없음
			if(!rs.next()) {
				return false;	
			}

			// 학번요청 정보 가져오기 (일단은 java.util.Date로 했고 안되면 getDate로 변경)
			
			String rsAccountID = rs.getString("accountID");
						
			// 년도만 뽑아오는 과정
			int reqnum_earliest_from_that_year = OurTimes.dateNow().getYear();
			
			// 요청년도가 년도(r.reqSIDdate)에 속하는 요청정보 중 가장 이른 것의 요청번호를 가져온다.
			SQL = "SELECT reqSIDnum FROM StudentIDRequest"
					+ " WHERE YEAR(reqSIDdate) = ?"
					+ " ORDER BY reqSIDdate ASC LIMIT 1";
			pstmt.clearParameters(); // 초기화
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, reqnum_earliest_from_that_year);
			rs = pstmt.executeQuery(); // ResultSet
			
			int rsSID;
			// 올 해 하나라도 존재하는 경우
			if(rs.next()) {
				rsSID = rs.getInt("reqSIDnum");
					
			}
			// 존재하지 않으면 최초가 되므로 첫 번째가 됨.
			else {
				rsSID = p_reqnum - 1;
			}
			int year_reqnum = p_reqnum - rsSID;
			// 학번 요청
			AccountDAO accountDAO = new AccountDAO();
			if(accountDAO.requestSID(rsAccountID, reqnum_earliest_from_that_year, year_reqnum, p_dcode) == -1) {
				return false; // -1이면 실패
			}
			
			// 삭제
			if(deleteReqSID(p_reqnum))
				return true;
			return false;	
		}catch(SQLException e) {
		      throw e;
		      
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	}
	
	/** 학번부여거절
	 * @param p_reqnum 요청번호
	 * @return 거절결과(boolean)
	 * ! 삭제랑 중복되는데 처리방안
	 * @throws SQLException 
	 *  */
	public boolean rejectReqSID(int p_reqnum) throws SQLException{
		try {
			if(deleteReqSID(p_reqnum))
				return true;
			return false;
		}
		catch (SQLException e) {
			throw e;
		}
	}
	
	/** 학번요청삭제
	 * @param p_reqnum 요청번호
	 * @return 삭제결과(boolean)
	 * @throw SQLException DB오류
	 * ! DAO SQL 실패 경우도 추가해야함 */
	public boolean deleteReqSID(int p_reqnum) throws SQLException{
		try {
			String SQL = "DELETE FROM StudentIDRequest" + 
					" WHERE StudentIDRequest.reqSIDNum = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_reqnum);
			int result = pstmt.executeUpdate();
			if(result != 1)
				return false;
			
			return true;
		}catch(SQLException e) {
		      throw e;
		      
		}finally { // 삭제는 반대 순서
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	}
}
