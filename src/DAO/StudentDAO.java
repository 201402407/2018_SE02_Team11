package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ClassObject.ChangeType;
import ClassObject.StudentInfo;

public class StudentDAO extends DAOBase {
	static Connection conn;
	static PreparedStatement pstmt;
	
	// ���� DAO
	
	// ������ ������ ���ÿ� jbdc ����.
	public StudentDAO() {
		super();
		
	}
	
	/** ����������ȸ 
	 * @param p_sid �й�
	 * @return ��������(�̸�, �а���, �г�, �̼������б�, ���п���, ��������, ������¹�ȣ)
	 * @throws SQLException DB����
	 * ! DAO���� - ���������� ����
	 * */
	public List<StudentInfo> getStudentInfoBySID(int p_sid) throws SQLException {
		
		List<StudentInfo> arrayList = new ArrayList<StudentInfo>();
		
		try {
			String SQL = "SELECT S.studentName, D.departmentName, S.studentYear, S.semester,"
					+ " S.isTimeOff, S.isGraduate"
					+ " FROM Student S"
					+ " LEFT JOIN Department D" 
					+ " ON S.departmentCode = D.departmentCode"
					+ " WHERE S.studentID = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_sid);
			ResultSet rs = pstmt.executeQuery(); // ResultSet
			
			// ��������
			if (rs.next())
			{
				String rsSName = rs.getString("studentName");
				String rsDName = rs.getString("departmentName");
				int rsYear = rs.getInt("studentYear");
				int rsSemester = rs.getInt("semester");
				boolean rsTimeOff = rs.getBoolean("isTimeOff");
				boolean rsGraduate = rs.getBoolean("isGraduate");
			
				StudentInfo studentInfo = new StudentInfo(rsSName,
						rsDName,
						rsYear,
						rsSemester,
						rsTimeOff,
						rsGraduate
				);
				arrayList.add(studentInfo);
			}
		   	
			return arrayList;
			
		}catch(SQLException e) {
		      throw e;
		      
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		
	}
	
	/** �޺��м���
	 * @param p_sid �й�
	 * @param p_changetype �޺��п���
	 * @return �������(boolean)
	 * @throws SQLException DB���� */
	public boolean setTimeOff(int p_sid, ChangeType p_changetype) throws SQLException {
		
		try {
			String SQL = "UPDATE Student" + 
					" SET Student.isTimeOff = ?"
					+ " WHERE Student.studentID = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			boolean isTimeoff = (p_changetype == ChangeType.TAKEOFF) ? true : false;
			pstmt.setBoolean(1, isTimeoff);
			pstmt.setInt(2, p_sid);
			int result = pstmt.executeUpdate(); // ����� row�� ��ŭ ����
			if(result == 1) // 1�ٸ� �����ϹǷ� ����
				return true;
			return false;
		}catch(SQLException e) {
		      throw e;
		      
		}finally { // ������ �ݴ� ����
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		
	}
	/* �޺��м��� */
	
	
	/* �޺��п�û�����˸� */
	/*
	public boolean rejectTimeOff(int p_sid, String p_reason) {
		
		try {
			INSERT INTO �л��˸��� (�й�, �޽����ؽ�Ʈ)
			VALUES ( p_sid, "[�޺��п�û�� �����Ǿ����ϴ�.]" + p_reason )
			String SQL = "INSERT INTO �л��˸��� (�й�, �޽����ؽ�Ʈ)" + 
					" SET Student.isTimeOff = ?"
					+ " WHERE Student.studentID = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setBoolean(1, p_changetype);
			pstmt.setInt(2, p_sid);
			int result = pstmt.executeUpdate(); // ����� row�� ��ŭ ����
			if(result == 1) // 1�ٸ� �����ϹǷ� ����
				return true;
		}catch(Exception e) {
		      e.printStackTrace();
		      
		}finally { // ������ �ݴ� ����
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	}
	*/
	
	/** ���п�����ȸ
	 * @param p_sid �й�
	 * @return ���п���(boolean) -> true : ����, false : ����
	 * @throws SQLException DB����, p_sid �������
	 * ! DAO �Ϸ� */
	public boolean getTimeOffBySID(int p_sid) throws SQLException {
		
		try {
			String SQL = "SELECT isTimeOff FROM Student" + 
					" WHERE studentID = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_sid);
			ResultSet rs = pstmt.executeQuery(); // ResultSet

			// �����ϸ�
			if(rs.next()) {
				return rs.getBoolean("isTimeOff");
			}
			else {
				throw new SQLException("No such student ID.");
			}
			
		}catch(SQLException e) {
		      throw e;
		      
		}finally { // ������ �ݴ� ����
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	}
	
	/** �ű��й����� 
	 * @param p_newStuYear ��ϳ⵵
	 * @param p_newStuOrder ��ϼ���
	 * @param p_name �л��̸�
	 * @param p_accountID �������̵�
	 * @param p_dcode �а��ڵ�
	 * @return �й� (���н� -1)
	 * @throws SQLException DB����
	 * ! DAO ���ϰ� + �˰��� �����ؾ� ��, year->studentYear
	 * */
	public int createNewStudent(int p_newStuYear, int p_newStuOrder, String p_name, String p_accountID, int p_dcode) 
	throws SQLException {
		
		int newStuID = p_newStuYear * 100000 + p_newStuOrder;
		
		try {
			String SQL = "INSERT INTO Student (studentID, studentName, studentYear,"
					+ " semester, isTimeOff, isGraduate, accountID, departmentCode)"
					+ " VALUES (?, ?, 1, 1, FALSE, FALSE, ?, ?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, newStuID);
			pstmt.setString(2, p_name);
			pstmt.setString(3, p_accountID);
			pstmt.setInt(4, p_dcode);
		    int result = pstmt.executeUpdate();
		    if(result != 1)
		    	return -1;
		    return newStuID;
		}catch(SQLException e) {
		      throw e;
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	}
	
	/** �л����翩��
	 * @param p_sid �й�
	 * @return ���翩��(boolean) -> ���� : true, ������ : false
	 * @throws SQLException DB����
	 * */
	public boolean isStudentExist(int p_sid) throws SQLException {
		try {
			String SQL = "SELECT * FROM Student" + 
					" WHERE studentID = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_sid);
			ResultSet rs = pstmt.executeQuery(); // ResultSet

			// �����ϸ�
			if(rs.next()) {
				return true;
			}
			return false;	
		}catch(SQLException e) {
		      throw e;
		      
		}finally { // ������ �ݴ� ����
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	}
}
