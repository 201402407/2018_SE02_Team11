package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import ClassObject.StudentIDRequest;

public class StudentIDRequestDAO {
	static String jdbcUrl; 
	static String dbId;
	static String dbPwd;
	
	static Connection conn;
	static PreparedStatement pstmt;
	
	private static String getJdbcUrl() {
		return jdbcUrl;
	}

	private void setJdbcUrl(String jdbcUrl) {
		AccountDAO.jdbcUrl = jdbcUrl;
	}

	private static String getDbId() {
		return dbId;
	}

	private void setDbId(String dbId) {
		AccountDAO.dbId = dbId;
	}

	private static String getDbPwd() {
		return dbPwd;
	}

	private void setDbPwd(String dbPwd) {
		AccountDAO.dbPwd = dbPwd;
	}

	public enum signUpResult { // ȸ������ ��� enum
		SUCCESS,
		INVALID_FORM,
		MISSING_FIELD
	}

	public enum loginResult { // �α��� ��� enum
		SUCCESS,
		MISSING_FIELD,
		NOT_FOUND_ID,
		INCORRECT_PWD
}
	
	// ������ ������ ���ÿ� jbdc ����.
	public StudentIDRequestDAO() {
		setJdbcUrl("jdbc:mysql://127.0.0.1:3306/SE02?autoReconnect=true"); // DB �����ּ�
		setDbId("SE02_11");
		setDbPwd("2018");
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//conn = DriverManager.getConnection(this.jdbcUrl, this.dbId, this.dbPass);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	/* �й���û�߰�  */
	public boolean addReqSID(Date p_date, String p_accountID) { // ���̵� �̹Ƿ� accountID�� �̸� ����
		StudentIDRequest studentIDRequest = new StudentIDRequest();
		studentIDRequest.setReqSIDdate(p_date);
		studentIDRequest.setAccountID(p_accountID);
		
		try {
			String SQL = "INSERT INTO StudentIDRequest VALUES (?, ?)";
			conn = DriverManager.getConnection(getJdbcUrl(), getDbId(), getDbPwd());
			pstmt = conn.prepareStatement(SQL);
		    pstmt.setDate(1,studentIDRequest.getReqSIDdate());
		    pstmt.setString(2,studentIDRequest.getAccountID());
		    pstmt.executeUpdate();
		    
		    return true;
		}catch(Exception e) {
		      e.printStackTrace();
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		
		return false;
		
	}
	
	/* �й���û��⿭�����ȸ */
	public ArrayList<StudentIDRequest> getReqSIDList() { // DAO ������ ArrayList�� ǥ���ϴ��� ?
		ArrayList<StudentIDRequest> resultArrayList = new ArrayList<StudentIDRequest>();
		
		try {
			String SQL = "SELECT * FROM StudentIDRequest";
			conn = DriverManager.getConnection(getJdbcUrl(), getDbId(), getDbPwd());
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
	
	/* �й��ο��㰡 */
	public boolean permitReqSID(int p_reqnum, int p_dcode) {
		
		try {
			// �ش� ��û��ȣ�� �й��� �ҷ��´�.
			String SQL = "SELECT * FROM StudentIDRequest WHERE reqSIDnum = " + p_reqnum;
			conn = DriverManager.getConnection(getJdbcUrl(), getDbId(), getDbPwd());
			pstmt = conn.prepareStatement(SQL);
			ResultSet rs = pstmt.executeQuery(); // ResultSet
			
			// ��ȸ��� �ƹ��͵� ����
			if(!rs.next()) {
				return false;	
			}

			// �й���û ���� ��������
			Date rsReqSIDdate = rs.getDate("reqSIDdate");
			String rsAccountID = rs.getString("accountID");
						
			// �⵵�� �̾ƿ��� ����
			SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
			String temp = transFormat.format(rsReqSIDdate);
			temp = temp.substring(0, 4);
			int reqnum_earliest_from_that_year = Integer.valueOf(temp);
			
			// ��û�⵵�� �⵵(r.reqSIDdate)�� ���ϴ� ��û���� �� ���� �̸� ���� ��û��ȣ�� �����´�.
			SQL = "SELECT reqSIDnum FROM StudentIDRequest"
					+ " WHERE YEAR(reqSIDdate) = " + reqnum_earliest_from_that_year
					+ " ORDER BY reqSIDdate ASC LIMIT 1";
			pstmt = null; // �ʱ�ȭ
			pstmt = conn.prepareStatement(SQL);
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
	
	/* �й��ο����� */
	public boolean rejectReqSID(int p_reqnum) {
		if(deleteReqSID(p_reqnum))
			return true;
		return false;
	}
	
	/* �й���û���� */
	public boolean deleteReqSID(int p_reqnum) {
		try {
			String SQL = "DELETE FROM StudentIDRequest" + 
					" WHERE StudentIDRequest.reqSIDNum = " + p_reqnum;
			conn = DriverManager.getConnection(getJdbcUrl(), getDbId(), getDbPwd());
			pstmt = conn.prepareStatement(SQL);
			pstmt.executeUpdate();
			
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
