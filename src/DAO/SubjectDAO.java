package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ClassObject.Subject;
import DAO.AccountDAO.signUpResult;

public class SubjectDAO extends DAOBase {
	
	// 데이터베이스 접근을 위해
	private static PreparedStatement pstmt;
	private static Connection conn;
		
	public SubjectDAO() {
		super();
	}
	
	// 교과목추가결과 enum
	enum AddSubjectResult {
		SUCCESS,
		MISSING_FIELD,
		COLLISION_SUBJNAME,
		SQL_FAILED
	}
	
	/* 과목이름조회 */
	public String getSubjectNameBySCode(int p_scode) {
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
		}catch(Exception e){
	        e.printStackTrace();
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
		return null;
	}
	
	/* 과목세부정보조회 */
	public Subject getSubjectInfoBySCode(int p_scode) {
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
			
		}catch(Exception e){
	        e.printStackTrace();
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
		return null;
	}
	/* 학점조회 */
	public double getScoreBySCode(int p_scode) {
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
	        e.printStackTrace();
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
		return -1;
	}
	
	/* 과목존재여부조회 */
	public boolean isSubjectExistBySCode(int p_scode) {
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
		}catch(Exception e){
	        e.printStackTrace();
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
		return false;
	}
	
	/* 교과목 추가 */
	public AddSubjectResult addSubject(String p_subjectname, double p_score) {
		if(p_subjectname.isEmpty() || p_score < 0) {
			return AddSubjectResult.MISSING_FIELD;
		}
		
		try {
			String SQL = "SELECT * FROM Subject WHERE subjectName = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, p_subjectname);
			ResultSet rs = pstmt.executeQuery();
			
			// 조회결과 중복 존재
			if(rs.next())
				return AddSubjectResult.COLLISION_SUBJNAME;
			
			SQL = "INSERT INTO Subject (subjectName, score)" 
			+ " VALUES (?, ?)";
			pstmt = null;
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, p_subjectname);
			pstmt.setDouble(2, p_score);
			int result = pstmt.executeUpdate(); // -> SQL 실패한 경우도 넣어야 하나 ?
			
			if(result != 1) // 1개의 행만 추가하므로 1이 아닌가?
		    	return AddSubjectResult.SQL_FAILED;
			return AddSubjectResult.SUCCESS;
		}catch(Exception e){
	        e.printStackTrace();
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
		return AddSubjectResult.SQL_FAILED;
	}
	
	/* 당학기운용과목조회 */
	public List<Subject> getThisSemesterSubjectByDCode(int p_dcode) {
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
			pstmt.setInt(1, 현재학기);
			pstmt.setInt(2, p_dcode);
			ResultSet rs = pstmt.executeQuery();
			
			// 조회결과 중복 존재
			if(!rs.next())
				return null;
			
			rs.beforeFirst();
			
			while(rs.next()) {
				int rsSubjectCode = rs.getInt("subjectCode");
				String rsSubjectName = rs.getString("subjectName");
				
				Subject subject = new Subject();
				subject.setSubjectCode(rsSubjectCode);
				subject.setSubjectName(rsSubjectName);
				
				arrayList.add(subject);	
			}
		return arrayList;
		
		}catch(Exception e){
	        e.printStackTrace();
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
		return null;
	}
}
