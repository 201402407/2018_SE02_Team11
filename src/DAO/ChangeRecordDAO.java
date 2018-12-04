package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ClassObject.ChangeRecord;
import ClassObject.ChangeType;
import Util.OurTimes;
public class ChangeRecordDAO extends DAOBase {

	// 데이터베이스 접근을 위해
	private static PreparedStatement pstmt;
	private static Connection conn;
	
	// 협업 DAO
	StudentDAO studentDAO;
	TimeoffRequestDAO timeoffRequestDAO;
	
	public ChangeRecordDAO() {
		super();
		
		studentDAO = new StudentDAO();
		timeoffRequestDAO = new TimeoffRequestDAO();
	}
	
	public enum RequestTimeOnOffsResult {
		SUCCESS,
		NULL_IN_DB,
		CURRENTLY_UNAVAILABLE
	}
	
	/** 
	 * 학적변동사항내역조회
	 * @param p_sid 학번
	 * @return 학적변동내역(변동일자, 변동구분, 시작학기, 종료학기) List<ChangeRecord>
	 * @throws SQLException DB오류
	 * */
	public List<ChangeRecord> getChangeRecordListBySID(int p_sid) throws SQLException{
		List<ChangeRecord> arrayList = new ArrayList<ChangeRecord>();
		
		try {
			String SQL = "SELECT CR.changeDate, CR.changeType, CR.startSemester, CR.endSemester"
					+ " FROM ChangeRecord CR"
					+ " WHERE CR.studentID = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_sid);
			ResultSet rs = pstmt.executeQuery(); // ResultSet
			
			// 목록 꺼내오기
			while(rs.next()) {
				ChangeRecord changeRecord = new ChangeRecord();
				
				changeRecord.setChangeDate(OurTimes.sqlDateToLocalDate(rs.getDate("changeDate")));
				changeRecord.setChangeType(ChangeType.gotChangeType(rs.getInt("changeType")));
				changeRecord.setStartSemester(rs.getInt("startSemester"));
				changeRecord.setEndSemester(rs.getInt("endSemester"));
				
				arrayList.add(changeRecord);
			}
			
			return arrayList;
		}catch(SQLException e) {
		      throw e;
		      
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	}
	
	/** 
	 * 학적변동추가
	 * @param p_date 변동일자
	 * @param p_change 변동구분
	 * @param p_start 시작학기
	 * @param p_end 종료학기
	 * @param p_sid 학번
	 * @return 성공여부결과(boolean)
	 * @throws SQLException
	 * */
	public boolean addChangeRecord(LocalDate p_date, ChangeType p_change, int p_start, int p_end, int p_sid) 
	throws SQLException {
		try {
			String SQL = "INSERT INTO ChangeRecord (changeDate, changeType, startSemester, endSemester, studentID)"
					+ " VALUES (?, ?, ?, ?, ?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
		    pstmt.setDate(1,OurTimes.LocalDateTosqlDate(p_date));
		    pstmt.setInt(2, ChangeType.gotTinyInt(p_change));
		    pstmt.setInt(3, p_start);
		    pstmt.setInt(4, p_end);
		    pstmt.setInt(5, p_sid);
		    
		    int result = pstmt.executeUpdate();
		    if(result == 1) {
		    	if(studentDAO.setTimeOff(p_sid, p_change)) {
			    	return true;
			    }
		    }
		    	
		    return false;
		}catch(SQLException e) {
		      throw e;
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	}
	
	/** 
	 * 휴복학신청
	 * @param p_change 변동구분
	 * @param p_start 시작학기
	 * @param p_end 종료학기
	 * @param p_reason 신청사유
	 * @param p_sid 학번
	 * @return 신청완료여부(enum)
	 * @throws SQLException
	 * */
	public RequestTimeOnOffsResult requestTimeOnOffs(ChangeType p_change, int p_start, 
			int p_end, String p_reason, int p_sid) throws SQLException {
		try {
			if(OurTimes.isNowOnTerm())
				return RequestTimeOnOffsResult.CURRENTLY_UNAVAILABLE;
			
			if(timeoffRequestDAO.addTimeOnOff(OurTimes.dateNow(), p_change, p_start, p_end, p_reason, p_sid))
				return RequestTimeOnOffsResult.SUCCESS;
			else 
				return RequestTimeOnOffsResult.NULL_IN_DB;
		}
		catch(SQLException e) {
			throw e;
		}
	}
}
