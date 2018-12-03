package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ClassObject.ScholarshipAward;
import ClassObject.Subject;
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
	
	public enum awardToStudentResult {
		SUCCESS,
		NOT_FOUND_SCHOLAR,
		NOT_FOUND_STUDENT,
		SQL_FAILED
	}
	/** ���м���
	 * @param p_scnum ���й�ȣ
	 * @param p_money ���б�
	 * @param p_sid �й�
	 * @return �������ΰ��(awardToStudentResult)
	 * */
	public awardToStudentResult awardToStudent(int p_scnum, int p_money, int p_sid) {
		if(!scholarshipDAO.isScholarshipExist(p_scnum))
			return awardToStudentResult.NOT_FOUND_SCHOLAR;
		if(!studentDAO.isStudentExist(p_sid))
			return awardToStudentResult.NOT_FOUND_STUDENT;
		
		try {
			String SQL = "INSERT INTO ScholarshipAward "
					+ "(awardMoney, awardDate, scholarshipNum, studentID) VALUES (?, ?, ?, ?)";
			Date currentTime = Date.valueOf(OurTimes.dateNow()); // ���糯¥ Date Ÿ������ ��ȯ
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_money);
			pstmt.setDate(2, currentTime);
			pstmt.setInt(3, p_scnum);
			pstmt.setInt(4, p_sid);
			int result = pstmt.executeUpdate(); 
			
			// SQL ����
			if(result != 1)
				return awardToStudentResult.SQL_FAILED;
			
			return awardToStudentResult.SUCCESS;
		}catch(Exception e){
	        e.printStackTrace();
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
		return awardToStudentResult.SQL_FAILED;
	}
	
	/** ���г�����ȸ
	 * @param p_awardname �����̸�
	 * @param p_money ���б�
	 * @param p_sid �й�
	 * @return ���г���(�����̸�, ���б�, ��������)
	 * ! DAO �˰��� ����(SA.schoalrshipName) ���� �ʿ�
	 * ! Date Ÿ������ LocalDate Ÿ������ ��Ȯ�� �ؾ� �� �ʿ�
	 * ! DAO ��ȸ��� X ���� �߰� �ʿ�*/
	public List<AwardInfoBySID> getAwardInfoBySID(String p_awardname, int p_money, int p_sid) {
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
			
			// ��ȸ��� X
			if(!rs.next())
				return null;
			
			rs.beforeFirst();
			
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
		
		}catch(Exception e){
	        e.printStackTrace();
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	return null;
	}
}
