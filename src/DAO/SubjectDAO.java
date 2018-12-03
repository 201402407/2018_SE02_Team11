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
	
	// �����ͺ��̽� ������ ����
	private static PreparedStatement pstmt;
	private static Connection conn;
		
	public SubjectDAO() {
		super();
	}
	
	// �������߰���� enum
	enum AddSubjectResult {
		SUCCESS,
		MISSING_FIELD,
		COLLISION_SUBJNAME,
		SQL_FAILED
	}
	
	/* �����̸���ȸ */
	public String getSubjectNameBySCode(int p_scode) {
		try {
			String SQL = "SELECT subjectName FROM Subject WHERE subjectCode = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_scode);
			ResultSet rs = pstmt.executeQuery();
			
			// ��ȸ��� ����
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
	
	/* ���񼼺�������ȸ */
	public Subject getSubjectInfoBySCode(int p_scode) {
		try {
			String SQL = "SELECT subjectName, score FROM Subject WHERE subjectCode = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_scode);
			ResultSet rs = pstmt.executeQuery();
			
			// ��ȸ��� ����
			if(!rs.next()) {
				return null;	
			}
			
			String rsSubjectName = rs.getString("subjectName");
			double rsScore = rs.getDouble("score");
			Subject subject = new Subject( // �����, ���� �� �����ε� �����ڵ嵵 �־ �ص� �Ǵ���?
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
	/* ������ȸ */
	public double getScoreBySCode(int p_scode) {
		try {
			String SQL = "SELECT score FROM Subject WHERE subjectCode = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_scode);
			ResultSet rs = pstmt.executeQuery();
			
			// ��ȸ��� ���� -> -1����?
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
	
	/* �������翩����ȸ */
	public boolean isSubjectExistBySCode(int p_scode) {
		try {
			String SQL = "SELECT * FROM Subject WHERE subjectCode = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_scode);
			ResultSet rs = pstmt.executeQuery();
			
			// ��ȸ��� ����
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
	
	/* ������ �߰� */
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
			
			// ��ȸ��� �ߺ� ����
			if(rs.next())
				return AddSubjectResult.COLLISION_SUBJNAME;
			
			SQL = "INSERT INTO Subject (subjectName, score)" 
			+ " VALUES (?, ?)";
			pstmt = null;
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, p_subjectname);
			pstmt.setDouble(2, p_score);
			int result = pstmt.executeUpdate(); // -> SQL ������ ��쵵 �־�� �ϳ� ?
			
			if(result != 1) // 1���� �ุ �߰��ϹǷ� 1�� �ƴѰ�?
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
	
	/* ���б��������ȸ */
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
			pstmt.setInt(1, �����б�);
			pstmt.setInt(2, p_dcode);
			ResultSet rs = pstmt.executeQuery();
			
			// ��ȸ��� �ߺ� ����
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
