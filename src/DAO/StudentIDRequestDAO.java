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
	
	// ������ ������ ���ÿ� jbdc ����.
	public StudentIDRequestDAO() {
		super();
	}
	
	/** (2018-12-08 Ȯ�οϷ�)
	 * �й���û�߰� 
	 * @param p_date ��û����
	 * @param p_accountID �������̵�
	 * @return ��û�߰���������(boolean)
	 * @throws SQLException DB����
	 * ! DAO SQL ���� ��� �߰�
	 * */
	public boolean addReqSID(LocalDate p_date, String p_accountID) throws SQLException { // ���̵� �̹Ƿ� accountID�� �̸� ����
		StudentIDRequest studentIDRequest = new StudentIDRequest();
		studentIDRequest.setReqSIDdate(p_date);
		studentIDRequest.setAccountID(p_accountID);

		try {
			String SQL = "INSERT INTO StudentIDRequest (reqSIDdate, accountID) "
					+ " VALUES (?, ?)";
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
	
	/** �й���û��⿭�����ȸ 
	 * @return �й���û��⿭���(��û��ȣ, ��û����, ���̵�)
	 * @throws SQLException DB����
	 * ! rs.beforeFirst() �׽�Ʈ�غ���
	 * */
	public ArrayList<StudentIDRequest> getReqSIDList() throws SQLException { // DAO ������ ArrayList�� ǥ���ϴ��� ?
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
				LocalDate rsDate = OurTimes.sqlDateToLocalDate(rs.getDate("reqSIDdate"));
				String rsAccountID = rs.getString("accountID");
				
				StudentIDRequest studentIDRequest = new StudentIDRequest();
				studentIDRequest.setReqSIDnum(rsReqSIDnum);
				studentIDRequest.setReqSIDdate(rsDate);
				studentIDRequest.setAccountID(rsAccountID);
				
				resultArrayList.add(studentIDRequest); // ArrayList�� ���
			}
		   	
			return resultArrayList;
			
		}catch(SQLException e) {
		      throw e;
		      
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	}
	
	/** �й��ο��㰡
	 * @param p_reqnum ��û��ȣ
	 * @param p_dcode �а��ڵ�
	 * @return �ο��㰡���(boolean)
	 * @throws SQLException DB����
	 * + ��û��¥ ����ϴ� �� ���� �ʿ�
	 * ! DAO - ��ȯ�� ���� �ʿ� 
	 * ! DAO - �ش� �⵵�� ���� ���� �й� ���ؿ���
	 * */
	public boolean permitReqSID(int p_reqnum, int p_dcode) throws SQLException{
		
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
			
			String rsAccountID = rs.getString("accountID");
			
			
			// �л����̺��� �ش� �⵵�� ���� ���� �й��� ���ؿ´�. �׸��� �ο��й��� �װ��� ���ϱ� 1�� �ȴ�. 
			int year_now = OurTimes.dateNow().getYear();
			SQL = "SELECT studentID\r\n" + 
					"FROM Student\r\n" + 
					"WHERE FLOOR(studentID/100000) = ?\r\n" + 
					"ORDER BY studentID DESC\r\n" + 
					"LIMIT 1;";
			pstmt.clearParameters(); // �ʱ�ȭ
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, year_now);
			rs = pstmt.executeQuery(); // ResultSet
			
			int newStuOrder;
			// newStuOrder = (�ش� �⵵�� ���� ���� �й�)�� �ش�⵵���� + 1
			// e.g. �ش� �⵵�� ���� ���� �й� = 201300006
			// e.g. �װ��� �ش�⵵���� = 00006 = 6
			// e.g. ���� newStuOrder = 6+1 = 7
			if(rs.next())
				newStuOrder = (rs.getInt("studentID") % 100000) + 1;
			// �ش� �⵵�� ó�� �л��̸� newStuOrder = 1
			else
				newStuOrder = 1;
			
			// �й� ��û
			AccountDAO accountDAO = new AccountDAO();
			if(accountDAO.requestSID(rsAccountID, year_now, newStuOrder, p_dcode) == -1) {
				return false; // -1�̸� ����
			}
			deleteReqSID(p_reqnum);
			
			return true;
			
		}catch(SQLException e) {
		      throw e;
		      
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	}
	
	/** �й��ο�����
	 * @param p_reqnum ��û��ȣ
	 * @return �������(boolean)
	 * ! ������ �ߺ��Ǵµ� ó�����
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
	
	/** 
	 * �й���û����
	 * @param p_reqnum ��û��ȣ
	 * @return �������(boolean)
	 * @throw SQLException DB����
	 * ! DAO SQL ���� ��쵵 �߰��ؾ��� */
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
		      
		}finally { // ������ �ݴ� ����
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	}
}
