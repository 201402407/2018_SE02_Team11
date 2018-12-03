package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import ClassObject.StudentIDRequest;

public class StudentIDRequestDAO extends DAOBase {
	
	static Connection conn;
	static PreparedStatement pstmt;
	
	// ������ ������ ���ÿ� jbdc ����.
	public StudentIDRequestDAO() {
		super();
	}
	
	public boolean isIncludeNumber(String string) {
		for(int i = 0 ; i < string.length(); i ++)
	    {    
	        // 48 ~ 57�� �ƽ�Ű �ڵ�� 0~9�̴�.
	        if(48 <= string.charAt(i) && string.charAt(i) <= 57)
	            return true;
	    }
		return false;
	}
	
	/** �й���û�߰� 
	 * @param p_date ��û����
	 * @param p_accountID �������̵�
	 * @return ��û�߰���������(boolean)
	 * ! DAO SQL ���� ��� �߰�
	 * */
	public boolean addReqSID(Date p_date, String p_accountID) { // ���̵� �̹Ƿ� accountID�� �̸� ����
		StudentIDRequest studentIDRequest = new StudentIDRequest();
		studentIDRequest.setReqSIDdate(p_date);
		studentIDRequest.setAccountID(p_accountID);
		
		try {
			String SQL = "INSERT INTO StudentIDRequest VALUES (?, ?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
		    pstmt.setDate(1,studentIDRequest.getReqSIDdate());
		    pstmt.setString(2,studentIDRequest.getAccountID());
		    int result = pstmt.executeUpdate();
		    if(result != 1)
		    	return false;
		    return true;
		}catch(Exception e) {
		      e.printStackTrace();
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		
		return false;
		
	}
	
	/** �й���û��⿭�����ȸ 
	 * @return �й���û��⿭���(��û��ȣ, ��û����, ���̵�)
	 * ! rs.beforeFirst() �׽�Ʈ�غ���
	 * */
	public ArrayList<StudentIDRequest> getReqSIDList() { // DAO ������ ArrayList�� ǥ���ϴ��� ?
		ArrayList<StudentIDRequest> resultArrayList = new ArrayList<StudentIDRequest>();
		
		try {
			String SQL = "SELECT * FROM StudentIDRequest";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			ResultSet rs = pstmt.executeQuery(); // ResultSet
			
			// ��ȸ��� �ƹ��͵� ����
			if(!rs.next()) {
				return null;	
			}
			
			rs.beforeFirst(); // ù ������ �̵�  -> �̰� �³� ?
			
			// ��� ��������
			while(rs.next()) {
				int rsReqSIDnum = rs.getInt("reqSIDnum");
				Date rsDate = rs.getDate("reqSIDdate");
				String rsAccountID = rs.getString("accountID");
				
				StudentIDRequest studentIDRequest = new StudentIDRequest();
				studentIDRequest.setReqSIDnum(rsReqSIDnum);
				studentIDRequest.setReqSIDdate(rsDate);
				studentIDRequest.setAccountID(rsAccountID);
				
				resultArrayList.add(studentIDRequest); // ArrayList�� ���
			}
		   	
			return resultArrayList;
			
		}catch(Exception e) {
		      e.printStackTrace();
		      
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		return null;
	}
	
	/** �й��ο��㰡
	 * @param p_reqnum ��û��ȣ
	 * @param p_dcode �а��ڵ�
	 * @return �ο��㰡���(boolean)
	 * + ��û��¥ ����ϴ� �� ���� �ʿ�
	 * ! DAO ��ȯ�� ���� �ʿ� */
	public boolean permitReqSID(int p_reqnum, int p_dcode) {
		
		try {
			// �ش� ��û��ȣ�� �й��� �ҷ��´�.
			String SQL = "SELECT * FROM StudentIDRequest WHERE reqSIDnum = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_reqnum);
			ResultSet rs = pstmt.executeQuery(); // ResultSet
			
			// ��ȸ��� �ƹ��͵� ����
			if(!rs.next()) {
				return false;	
			}

			// �й���û ���� �������� (�ϴ��� java.util.Date�� �߰� �ȵǸ� getDate�� ����)
			java.util.Date rsReqSIDdate = rs.getTimestamp("reqSIDdate");
			String rsAccountID = rs.getString("accountID");
						
			// �⵵�� �̾ƿ��� ����
			SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
			String temp = transFormat.format(rsReqSIDdate);
			temp = temp.substring(0, 4);
			int reqnum_earliest_from_that_year = Integer.valueOf(temp);
			
			// ��û�⵵�� �⵵(r.reqSIDdate)�� ���ϴ� ��û���� �� ���� �̸� ���� ��û��ȣ�� �����´�.
			SQL = "SELECT reqSIDnum FROM StudentIDRequest"
					+ " WHERE YEAR(reqSIDdate) = ?"
					+ " ORDER BY reqSIDdate ASC LIMIT 1";
			pstmt.clearParameters(); // �ʱ�ȭ
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, reqnum_earliest_from_that_year);
			rs = pstmt.executeQuery(); // ResultSet
		   	
			// ��ȸ��� �ƹ��͵� ����
			if(!rs.next()) {
				return false;
			}
			
			int rsSID = rs.getInt("reqSIDnum");
			// �⵵��_��û��ȣ <- r.reqnum - reqnum_earliest_from_that_year??
			int year_reqnum = p_reqnum - rsSID;
			
			// �й� ��û
			AccountDAO accountDAO = new AccountDAO();
			if(accountDAO.requestSID(rsAccountID, reqnum_earliest_from_that_year, year_reqnum, p_dcode) == 0) {
				return false; // 0�̸� ����
			}
			
			// ����
			if(deleteReqSID(p_reqnum))
				return true;
			
		}catch(Exception e) {
		      e.printStackTrace();
		      
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		return false;
	}
	
	/** �й��ο�����
	 * @param p_reqnum ��û��ȣ
	 * @return �������(boolean)
	 * ! ������ �ߺ��Ǵµ� ó�����
	 *  */
	public boolean rejectReqSID(int p_reqnum) {
		if(deleteReqSID(p_reqnum))
			return true;
		return false;
	}
	
	/** �й���û����
	 * @param p_reqnum ��û��ȣ
	 * @return �������(boolean)
	 * ! DAO SQL ���� ��쵵 �߰��ؾ��� */
	public boolean deleteReqSID(int p_reqnum) {
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
		}catch(Exception e) {
		      e.printStackTrace();
		      
		}finally { // ������ �ݴ� ����
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		return false;
	}
}
