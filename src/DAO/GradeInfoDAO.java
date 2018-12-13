package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ClassObject.GradeInfo;

public class GradeInfoDAO extends DAOBase {

	// �����ͺ��̽� ������ ����
	private static PreparedStatement pstmt;
	private static Connection conn;
	
	// getGradeListByAttCodeList������ ������ ����
	private LectureEvaluationDAO lectureEvaluationDAO;
	
	public GradeInfoDAO() {
		super();
		lectureEvaluationDAO = new LectureEvaluationDAO();
	}
	
	/** �����ο�
	 * @param p_attendancenum ������ȣ
	 * @param p_grade ����
	 * @throws SQLException DB����
	 * @return �������(boolean) */
	public boolean addGrade(int p_attendancenum, double p_grade) throws SQLException {
		
		try {
			String SQL = "INSERT INTO GradeInfo (grade, attendanceNum)" + 
					" VALUES (?, ?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_attendancenum);
			pstmt.setDouble(2, p_grade);
			int result = pstmt.executeUpdate(); 
			
			// SQL ����
			if(result != 1)
				return false;
			
			return true;
			
		}catch(SQLException e){
	        throw e;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
		
	}
	
	/** ������������Ʈ ��ȸ getGradeListByAttCodeList, ��ȯ�� ���������� ������ �־��� ������ȣ�� ������ ��ġ�Ѵ�. 
	 * @param p_attnumlist ������ȣ����Ʈ
	 * @return ������������Ʈ(��������������, ����)
	 * @throws SQLException DB����
	 * ! DAO���� �����ʿ� - "��ȯ�� ���������� ������ �־��� ������ȣ�� ������ ��ġ�Ѵ�."
	 * ! DAO���� �����ʿ� - �������������θ� �����ϱ� ���� LectureEvaluationDAO�� �����Ѵ�
	 */
	public List<GradeInfo> getGradeListByAttCodeList(List<Integer> p_attnumlist) throws SQLException
	{
		// Tip: PreparedStatement�� IN ���� ����
		// https://stackoverflow.com/questions/178479/preparedstatement-in-clause-alternatives/10240302#10240302
		// https://stackoverflow.com/a/2861510
		
		// Example
		// <Input>  ->  <Output>
		// 10024    ->  3.5, false
		// 10035    ->  (null)
		// 10115    ->  (null)
		// 10097    ->  4.0, true
		// 10013    ->  2.5, fals
		
		// �Է¹��� ��ȯ���� ����Ʈ ���̴� �Ȱ��ƾ� �Ѵ�.
		List<GradeInfo> gradeInfoList = new ArrayList<GradeInfo>( Collections.nCopies(p_attnumlist.size(), null) );
		// ��������Ʈ�� ����ٸ� SQL���� ������ ���� ���� �鿩�� �ȵȴ�.
			if (p_attnumlist.isEmpty())
				return gradeInfoList;
		
		try {
			String sql_beforePlaceHolders = "SELECT * FROM GradeInfo\r\n" + 
					"WHERE attendanceNum in (%s)";
			String sql = String.format( sql_beforePlaceHolders,
					preparePlaceHolders(p_attnumlist.size()) );
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			setIntegers(pstmt, p_attnumlist);
			System.out.println("------------------");
			System.out.println(sql);
			System.out.println("------------------");
			ResultSet rs = pstmt.executeQuery();
			
			// �� ������ȣ�� �ش��ϴ� ������ ä���ִ´�.
			while(rs.next())
			{
				int attNum = rs.getInt("attendanceNum");
				double grade = rs.getDouble("grade");
				gradeInfoList.set(
						p_attnumlist.indexOf(attNum),
						new GradeInfo(grade, false)
						);
			}
			
			// �� ������ȣ�� �ش��ϴ� "��������������"�� ä���ִ´�. �� �� �� ������ȣ�� �ش��ϴ� �����򰡿��ΰ� �ʿ��ϴ�.
			HashMap<Integer, Boolean> el = lectureEvaluationDAO.getLectureEvaluationExistList(p_attnumlist);
			for( int attNum : p_attnumlist )
			{
				Boolean evaluated = el.get(attNum); //�ش� ������ �����򰡿���
				if(evaluated == null)
					throw new SQLException("getLectureEvaluationExistList() has failed.");
				if( gradeInfoList.get(p_attnumlist.indexOf(attNum)) != null )
					gradeInfoList.get(p_attnumlist.indexOf(attNum)).setVisibleGrade(evaluated);
			}
			
			return gradeInfoList;
		}
		catch(SQLException sqle){
	        throw sqle;
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
	}
	private String preparePlaceHolders(int length) {
	    return String.join(",", Collections.nCopies(length, "?"));
	}

	private void setIntegers(PreparedStatement preparedStatement, Iterable<Integer> values) throws SQLException {
		int i=0;
	    for (int value : values) {
	        preparedStatement.setInt(i + 1, value);
	        i++;
	    }
	}

}
