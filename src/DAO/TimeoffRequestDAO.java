package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ClassObject.ChangeType;
import ClassObject.Subject;
import ClassObject.TimeoffRequest;
import DAO.ProfessorDAO.AddProfessorResult;
import Util.OurTimes;

public class TimeoffRequestDAO extends DAOBase {
	// 데이터베이스 접근을 위해
	private static PreparedStatement pstmt;
	private static Connection conn;
	
	// 협업 DAO
	ChangeRecordDAO changeRecordDAO;
	
	public TimeoffRequestDAO() {
		super();
		changeRecordDAO = new ChangeRecordDAO();
	}
	
	/** 
	 * 휴·복학신청대기열목록조회
	 * @param void
	 * @return 휴·복학요청 대기열목록(요청번호, 요청일자, 휴·복학구분, 시작학기, 종료학기, 신청사유, 학번)
	 * @throws SQLException*/
	public List<TimeoffRequest> getTimeoffRequestList() throws SQLException {
		List<TimeoffRequest> arrayList = new ArrayList<>();
		try {
			String SQL = "SELECT reqNum, reqDate, changeType, startSemester, endSemester, reason, studentID" + 
					" FROM TimeoffRequest";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				// 담기
				TimeoffRequest timeoffRequest = new TimeoffRequest(
						rs.getInt("reqNum"),
						OurTimes.sqlDateToLocalDate(rs.getDate("reqDate")),
						ChangeType.gotChangeType(rs.getInt("changeType")),
						rs.getString("reason"),
						rs.getInt("startSemester"),
						rs.getInt("endSemester"),
						rs.getInt("studentID")
						);
				
				arrayList.add(timeoffRequest);
			}
			
		return arrayList;
		
		}catch(SQLException e){
	        throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	}
	
	/** 
	 * 휴복학요청 허락
	 * @param p_reqnum 요청번호
	 * @return 성공결과(boolean)
	 * @throws SQLException
	 * */
	public boolean permitTimeoffRequest(int p_reqnum) throws SQLException {
		try {
			String SQL = "SELECT reqDate, changeType, startSemester, endSemester, studentID" + 
					" FROM TimeoffRequest"
					+ " WHERE reqNum = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			ResultSet rs = pstmt.executeQuery();
			TimeoffRequest temp = new TimeoffRequest();
			
			// 담기 (하나만 리턴하므로 객체 하나에 담기)
			if(rs.next()) {
				temp.setReqDate(OurTimes.sqlDateToLocalDate(rs.getDate("reqDate")));
				temp.setChangeType(ChangeType.gotChangeType(rs.getInt("changeType")));
				temp.setStartSemester(rs.getInt("startSemester"));
				temp.setEndSemester(rs.getInt("endSemester"));
				temp.setStudentID(rs.getInt("studentID"));
			}
			
			if(changeRecordDAO.addChangeRecord(temp.getReqDate(), temp.getChangeType(),
					temp.getStartSemester(), temp.getEndSemester(), temp.getStudentID())) {
				if(deleteTimeoffReq(p_reqnum))
					return true;
			}
			return false;
		}catch(SQLException e){
	        throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	}
	
	/** 
	 * 휴·복학요청 거절
	 * @param p_reqnum 요청번호
	 * @return 성공여부결과(boolean)
	 * @throws SQLException */
	public boolean rejectTimeoffReq(int p_reqnum) throws SQLException {
		try {
			if(deleteTimeoffReq(p_reqnum))
				return true;
			
			return false;	
		}
		catch(SQLException e) {
			throw e;
		}
	}
	
	/** 
	 * 요청사항삭제
	 * @param p_reqnum 요청번호
	 * @return 성공여부(boolean)
	 * @throw SQLException */
	public boolean deleteTimeoffReq(int p_reqnum) throws SQLException {
		try {
			String SQL = "DELETE FROM TimeOffRequest" + 
					" WHERE TimeOffRequest.reqNum = ?";
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
	
	/** 
	 * 휴복학신청추가
	 * @param p_reqdate 요청날짜
	 * @param p_changetype 요청구분
	 * @param p_start 시작학기
	 * @param p_end 종료학기
	 * @param p_reason 신청사유
	 * @param p_sid 학번
	 * @return 신청성공여부(boolean)
	 * @throws SQLException */
	public boolean addTimeOnOff(LocalDate p_reqdate, ChangeType p_changetype, int p_start, int p_end, String p_reason, int p_sid)
	throws SQLException {
		try {
			String SQL = "INSERT INTO TimeoffRequest (reqDate, changeType, reason, startSemester, endSemester, studentID)"
					+ " VALUES (?, ?, ?, ?, ?, ?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setDate(1, OurTimes.LocalDateTosqlDate(p_reqdate));
			pstmt.setInt(2, ChangeType.gotTinyInt(p_changetype));
			pstmt.setString(3, p_reason);
			pstmt.setInt(4, p_start);
			pstmt.setInt(5, p_end);
			pstmt.setInt(6, p_sid);
			
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
