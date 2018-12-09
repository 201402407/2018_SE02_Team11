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
	// 데이터베이스 접근을 위해
	private static PreparedStatement pstmt;
	private static Connection conn;
	
	// 협업하는 DAO 클래스 변수 선언
	ScholarshipDAO scholarshipDAO;
	StudentDAO studentDAO;
	
	public ScholarshipAwardDAO() {
		super();
		
		// 협업하는 DAO 클래스 객체 생성
		scholarshipDAO = new ScholarshipDAO();
		studentDAO = new StudentDAO();
	}
	
	public enum AwardToStudentResult {
		SUCCESS,
		NOT_FOUND_SCHOLAR,
		NOT_FOUND_STUDENT
	}
	/** 장학수여
	 * @param p_scnum 장학번호
	 * @param p_money 장학금
	 * @param p_sid 학번
	 * @return 성공여부결과(AwardToStudentResult)
	 * @throws SQLException DB오류
	 * */
	public AwardToStudentResult awardToStudent(int p_scnum, int p_money, int p_sid) throws SQLException {
		if(!scholarshipDAO.isScholarshipExist(p_scnum))
			return AwardToStudentResult.NOT_FOUND_SCHOLAR;
		if(!studentDAO.isStudentExist(p_sid))
			return AwardToStudentResult.NOT_FOUND_STUDENT;
		
		try {
			String SQL = "INSERT INTO ScholarshipAward "
					+ "(awardMoney, awardDate, scholarshipNum, studentID) VALUES (?, ?, ?, ?)";
			Date currentTime = OurTimes.LocalDateTosqlDate(OurTimes.dateNow()); // 현재날짜 sqlDate 타입으로 변환
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_money);
			pstmt.setDate(2, currentTime);
			pstmt.setInt(3, p_scnum);
			pstmt.setInt(4, p_sid);
			int result = pstmt.executeUpdate(); 
			
			// SQL 실패
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
	
	/** 장학내역조회
	 * @param p_awardname 장학이름
	 * @param p_money 장학금
	 * @param p_sid 학번
	 * @return 장학내역(장학이름, 장학금, 수혜일자)
	 * @throws SQLException DB오류
	 * ! DAO 알고리즘 오류(SA.schoalrshipName) 수정 필요
	 * ! Date 타입인지 LocalDate 타입인지 명확히 해야 할 필요
	 * ! DAO 조회결과 X 조건 추가 필요*/
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
