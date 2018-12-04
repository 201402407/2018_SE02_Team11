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
	// �����ͺ��̽� ������ ����
	private static PreparedStatement pstmt;
	private static Connection conn;
	
	// ���� DAO
	ChangeRecordDAO changeRecordDAO;
	
	public TimeoffRequestDAO() {
		super();
		changeRecordDAO = new ChangeRecordDAO();
	}
	
	/** 
	 * �ޡ����н�û��⿭�����ȸ
	 * @param void
	 * @return �ޡ����п�û ��⿭���(��û��ȣ, ��û����, �ޡ����б���, �����б�, �����б�, ��û����, �й�)
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
				
				// ���
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
	 * �޺��п�û ���
	 * @param p_reqnum ��û��ȣ
	 * @return �������(boolean)
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
			
			// ��� (�ϳ��� �����ϹǷ� ��ü �ϳ��� ���)
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
	 * �ޡ����п�û ����
	 * @param p_reqnum ��û��ȣ
	 * @return �������ΰ��(boolean)
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
	 * ��û���׻���
	 * @param p_reqnum ��û��ȣ
	 * @return ��������(boolean)
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
		      
		}finally { // ������ �ݴ� ����
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	}
	
	/** 
	 * �޺��н�û�߰�
	 * @param p_reqdate ��û��¥
	 * @param p_changetype ��û����
	 * @param p_start �����б�
	 * @param p_end �����б�
	 * @param p_reason ��û����
	 * @param p_sid �й�
	 * @return ��û��������(boolean)
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
			
			// SQL ����
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
