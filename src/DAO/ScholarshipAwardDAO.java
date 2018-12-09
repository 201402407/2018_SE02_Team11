package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ClassObject.AwardInfoBySID;
import Util.OurTimes;

public class ScholarshipAwardDAO extends DAOBase {
	// �����ͺ��̽� ������ ����
	private static PreparedStatement pstmt;
	private static Connection conn;
	
	// �����ϴ� DAO Ŭ���� ���� ����
	ScholarshipDAO scholarshipDAO;
	StudentDAO studentDAO;
	
	public ScholarshipAwardDAO() {
		super();
		
		// �����ϴ� DAO Ŭ���� ��ü ����
		scholarshipDAO = new ScholarshipDAO();
		studentDAO = new StudentDAO();
	}
	
	public enum AwardToStudentResult {
		SUCCESS,
		NOT_FOUND_SCHOLAR,
		NOT_FOUND_STUDENT
	}
	/** ���м���
	 * @param p_scnum ���й�ȣ
	 * @param p_money ���б�
	 * @param p_sid �й�
	 * @return �������ΰ��(AwardToStudentResult)
	 * @throws SQLException DB����
	 * */
	public AwardToStudentResult awardToStudent(int p_scnum, int p_money, int p_sid) throws SQLException {
		if(!scholarshipDAO.isScholarshipExist(p_scnum))
			return AwardToStudentResult.NOT_FOUND_SCHOLAR;
		if(!studentDAO.isStudentExist(p_sid))
			return AwardToStudentResult.NOT_FOUND_STUDENT;
		
		try {
			String SQL = "INSERT INTO ScholarshipAward "
					+ "(awardMoney, awardDate, scholarshipNum, studentID) VALUES (?, ?, ?, ?)";
			Date currentTime = OurTimes.LocalDateTosqlDate(OurTimes.dateNow()); // ���糯¥ sqlDate Ÿ������ ��ȯ
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_money);
			pstmt.setDate(2, currentTime);
			pstmt.setInt(3, p_scnum);
			pstmt.setInt(4, p_sid);
			int result = pstmt.executeUpdate(); 
			
			// SQL ����
			if(result != 1)
				throw new SQLException("Affected rows: " + result);
			
			return AwardToStudentResult.SUCCESS;
		}catch(SQLException e){
	        throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	}
	
	/** ���г�����ȸ
	 * @param p_awardname �����̸�
	 * @param p_money ���б�
	 * @param p_sid �й�
	 * @return ���г���(�����̸�, ���б�, ��������)
	 * @throws SQLException DB����
	 * ! DAO �˰��� ����(SA.schoalrshipName) ���� �ʿ�
	 * ! Date Ÿ������ LocalDate Ÿ������ ��Ȯ�� �ؾ� �� �ʿ�
	 * ! DAO ��ȸ��� X ���� �߰� �ʿ�*/
	public List<AwardInfoBySID> getAwardInfoBySID(String p_awardname, int p_money, int p_sid) throws SQLException{
		List<AwardInfoBySID> scholarshipAwards = new ArrayList<>();
		
		try {
			String SQL = "SELECT SH.scholarshipName, SA.awardMoney, SA.awardDate"
					+ " FROM ScholarshipAward SA"
					+ " LEFT JOIN Student STD"
					+ " ON SA.studentID = STD.studentID"
					+ " LEFT JOIN Scholarship SH"
					+ " ON SA.scholarshipNum = SH.scholarshipNum"
					+ " WHERE STD.studentID = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_sid);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String rsScholarshipName = rs.getString("scholarshipName");
				int rsAwardMoney = rs.getInt("awardMoney");
				Date temp = rs.getDate("awardDate");
				LocalDate rsAwardDate = OurTimes.sqlDateToLocalDate(temp);
				
				AwardInfoBySID awardInfoBySID = new AwardInfoBySID(
						rsScholarshipName,
						rsAwardMoney,
						rsAwardDate
						);
				
				scholarshipAwards.add(awardInfoBySID);
			}
			
		return scholarshipAwards;
		
		}catch(SQLException e){
	        throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	}
}
