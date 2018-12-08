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
	
	// �����ͺ��̽� ������ ����
	private static PreparedStatement pstmt;
	private static Connection conn;
		
	public SubjectDAO() {
		super();
	}
	
	// �������߰���� enum
	public enum AddSubjectResult {
		SUCCESS,
		INVALID_SUBJNAME,
		INVALID_SCORE,
		COLLISION_SUBJNAME
	}
	
	/** �����̸���ȸ
	 * @param p_scode �����ڵ�
	 * @return �����(String)
	 * @throws SQLException DB����
	 * ! DAO�� ��ȸ ��� ���� ��쵵 ����ϴ��� ? */
	public String getSubjectNameBySCode(int p_scode) throws SQLException{
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
		}catch(SQLException e){
	        throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	}
	
	/** ���񼼺�������ȸ 
	 * @param p_scode �����ڵ�
	 * @return ���񼼺�����(�����,����) Subject
	 * @throws SQLException DB����
	 * ! DAO ��ȸ������� �����ؾ� �ϴ���?*/
	public Subject getSubjectInfoBySCode(int p_scode) throws SQLException {
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
			
		}catch(SQLException e){
	        throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	}
	
	/** ������ȸ 
	 * @param p_scode �����ڵ�
	 * @return ����(double)
	 * @throws SQLException DB����
	 * ! DAO ��ȸ������� �����ؾ� �ϴ���?*/
	public double getScoreBySCode(int p_scode) throws SQLException {
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
	        throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	}
	
	/** �������翩����ȸ 
	 * @param p_scode �����ڵ�
	 * @return ���翩��(boolean)
	 * @throws SQLException DB ����
	 * ! DAO �˰��� return ���� ���� �ʿ� */
	public boolean isSubjectExistBySCode(int p_scode) throws SQLException{
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
		}catch(SQLException e){
			throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	}
	
	/**
	 * (2018-12-08 Ȯ��)
	 * ������ �߰�
	 * @param p_subjectname �����, 1���� �̻� 20���� ���Ͽ��� �ϸ� ���� ������ ���� ���� ������ �ȵȴ�.
	 * @param p_score ����, 1.0�� �̻� 10.0�� ���Ͽ��� �Ѵ�.
	 * @return �������߰����(enum)
	 * @throws SQLException DB����
	 * ! DAO sql ���� ���п� ���� ��� enum �߰� �� �˰��� ���� �ʿ� */
	public AddSubjectResult addSubject(String p_subjectname, double p_score) throws SQLException {
		
		// �˻�
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
			
			// �˻� - ��ȸ��� �ߺ� ����
			if(rs.next())
				return AddSubjectResult.COLLISION_SUBJNAME;
			
			// ���� - �߰�
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
	
	/** ���б��������ȸ 
	 * @param p_dcode �а��ڵ�
	 * @return ������������Ʈ(�����ڵ�, �����) null : ��ȸ���ɱⰣ �ƴѰ��.
	 * @throws SQLException DB����
	 * ! DAO ���� �ð��� ���� ��ȸ ���� ���ο� ���� �˰��� �߰� �ʿ�*/
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
			if(OurTimes.isNowOnTerm()) { // �б����� ���
				currentTerm = OurTimes.currentTerm();
			}
			// �������� ���
			else {
				// ������û �Ⱓ(2.1~2.7, 8.1~8.7) ���� ���� ���� 2���� 8�� �� �� ��ȸ ����.
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
