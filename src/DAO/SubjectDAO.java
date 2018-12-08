package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ClassObject.Subject;
import DAO.AccountDAO.signUpResult;
import Util.OurTimes;

public class SubjectDAO extends DAOBase {
	
	// 데이터베이스 접근을 위해
	private static PreparedStatement pstmt;
	private static Connection conn;
		
	public SubjectDAO() {
		super();
	}
	
	// 교과목추가결과 enum
	public enum AddSubjectResult {
		SUCCESS,
		INVALID_SUBJNAME,
		INVALID_SCORE,
		COLLISION_SUBJNAME
	}
	
	/** 과목이름조회
	 * @param p_scode 과목코드
	 * @return 과목명(String)
	 * @throws SQLException DB오류
	 * ! DAO에 조회 결과 없는 경우도 써야하는지 ? */
	public String getSubjectNameBySCode(int p_scode) throws SQLException{
		try {
			String SQL = "SELECT subjectName FROM Subject WHERE subjectCode = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_scode);
			ResultSet rs = pstmt.executeQuery();
			
			// 조회결과 없음
			if(!rs.next()) {
				return null;	
			}
			
			return rs.getString("subjectName");
		}catch(SQLException e){
	        throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	}
	
	/** 과목세부정보조회 
	 * @param p_scode 과목코드
	 * @return 과목세부정보(과목명,학점) Subject
	 * @throws SQLException DB오류
	 * ! DAO 조회결과없음 수정해야 하는지?*/
	public Subject getSubjectInfoBySCode(int p_scode) throws SQLException {
		try {
			String SQL = "SELECT subjectName, score FROM Subject WHERE subjectCode = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_scode);
			ResultSet rs = pstmt.executeQuery();
			
			// 조회결과 없음
			if(!rs.next()) {
				return null;	
			}
			
			String rsSubjectName = rs.getString("subjectName");
			double rsScore = rs.getDouble("score");
			Subject subject = new Subject( // 과목명, 학점 만 리턴인데 과목코드도 넣어서 해도 되는지?
					p_scode,
					rsSubjectName,
					rsScore
					);
			return subject;
			
		}catch(SQLException e){
	        throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	}
	
	/** 학점조회 
	 * @param p_scode 과목코드
	 * @return 학점(double)
	 * @throws SQLException DB오류
	 * ! DAO 조회결과없음 수정해야 하는지?*/
	public double getScoreBySCode(int p_scode) throws SQLException {
		try {
			String SQL = "SELECT score FROM Subject WHERE subjectCode = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_scode);
			ResultSet rs = pstmt.executeQuery();
			
			// 조회결과 없음 -> -1리턴?
			if(!rs.next()) {
				return -1;	
			}
			
			return rs.getDouble("score");
			
		}catch(Exception e){
	        throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	}
	
	/** 과목존재여부조회 
	 * @param p_scode 과목코드
	 * @return 존재여부(boolean)
	 * @throws SQLException DB 오류
	 * ! DAO 알고리즘 return 조건 수정 필요 */
	public boolean isSubjectExistBySCode(int p_scode) throws SQLException{
		try {
			String SQL = "SELECT * FROM Subject WHERE subjectCode = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_scode);
			ResultSet rs = pstmt.executeQuery();
			
			// 조회결과 없음
			if(!rs.next())
				return false;
			return true;
		}catch(SQLException e){
			throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	}
	
	/**
	 * (2018-12-08 확인)
	 * 교과목 추가
	 * @param p_subjectname 과목명, 1글자 이상 20글자 이하여야 하며 기존 과목명과 같은 것이 있으면 안된다.
	 * @param p_score 학점, 1.0점 이상 10.0점 이하여야 한다.
	 * @return 교과목추가결과(enum)
	 * @throws SQLException DB오류
	 * ! DAO sql 실행 실패에 따른 결과 enum 추가 및 알고리즘 수정 필요 */
	public AddSubjectResult addSubject(String p_subjectname, double p_score) throws SQLException {
		
		// 검사
		if(! (p_subjectname.length() >= 1 && p_subjectname.length() <= 20))
			return AddSubjectResult.INVALID_SUBJNAME;
		if(! (p_score >= 1.0 && p_score <= 10.0))
			return AddSubjectResult.INVALID_SCORE;
		
		try {
			String SQL = "SELECT * FROM Subject WHERE subjectName = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, p_subjectname);
			ResultSet rs = pstmt.executeQuery();
			
			// 검사 - 조회결과 중복 존재
			if(rs.next())
				return AddSubjectResult.COLLISION_SUBJNAME;
			
			// 본격 - 추가
			SQL = "INSERT INTO Subject (subjectName, score)" 
			+ " VALUES (?, ?)";
			pstmt.clearParameters();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, p_subjectname);
			pstmt.setDouble(2, p_score);
			int result = pstmt.executeUpdate();
			
			if(result != 1)
		    	throw new SQLException("Affected rows: " + result);
			return AddSubjectResult.SUCCESS;
		}catch(SQLException e){
	        throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	}
	
	/** 당학기운용과목조회 
	 * @param p_dcode 학과코드
	 * @return 과목정보리스트(과목코드, 과목명) null : 조회가능기간 아닌경우.
	 * @throws SQLException DB오류
	 * ! DAO 현재 시간에 따른 조회 가능 여부에 대한 알고리즘 추가 필요*/
	public List<Subject> getThisSemesterSubjectByDCode(int p_dcode) throws SQLException {
		List<Subject> arrayList = new ArrayList<Subject>();
		
		try {
			String SQL = "SELECT Subject.subjectCode, Subject.subjectName"
					+ " FROM Lecture"
					+ " WHERE Lecture.registerTerm = ?"
					+ " AND Lecture.departmentCode = ?"
					+ " INNER JOIN Subject"
					+ " ON Lecture.departmentCode = Subject.subjectCode"
					+ " GROUP BY Subject.subjectCode";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);

			int currentTerm;
			if(OurTimes.isNowOnTerm()) { // 학기중인 경우
				currentTerm = OurTimes.currentTerm();
			}
			// 방학중인 경우
			else {
				// 수강신청 기간(2.1~2.7, 8.1~8.7) 포함 개강 전인 2월과 8월 둘 다 조회 가능.
				if(OurTimes.dateNow().getMonthValue() == 2 || 
						OurTimes.dateNow().getMonthValue() == 8) { 
					currentTerm = OurTimes.closestFutureTerm();
				}
				else {
					return null;
				}
			}
			pstmt.setInt(1, currentTerm);
			pstmt.setInt(2, p_dcode);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int rsSubjectCode = rs.getInt("subjectCode");
				String rsSubjectName = rs.getString("subjectName");
				
				Subject subject = new Subject();
				subject.setSubjectCode(rsSubjectCode);
				subject.setSubjectName(rsSubjectName);
				
				arrayList.add(subject);	
			}
		return arrayList;
		
		}catch(SQLException e){
	        throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	}
}
