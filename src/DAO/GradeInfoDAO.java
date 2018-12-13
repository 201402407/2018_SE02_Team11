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

	// 데이터베이스 접근을 위해
	private static PreparedStatement pstmt;
	private static Connection conn;
	
	// getGradeListByAttCodeList에서의 협업을 위해
	private LectureEvaluationDAO lectureEvaluationDAO;
	
	public GradeInfoDAO() {
		super();
		lectureEvaluationDAO = new LectureEvaluationDAO();
	}
	
	/** 성적부여
	 * @param p_attendancenum 수강번호
	 * @param p_grade 평점
	 * @throws SQLException DB오류
	 * @return 성공결과(boolean) */
	public boolean addGrade(int p_attendancenum, double p_grade) throws SQLException {
		
		try {
			String SQL = "INSERT INTO GradeInfo (grade, attendanceNum)" + 
					" VALUES (?, ?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_attendancenum);
			pstmt.setDouble(2, p_grade);
			int result = pstmt.executeUpdate(); 
			
			// SQL 실패
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
	
	/** 성적정보리스트 조회 getGradeListByAttCodeList, 반환될 성적정보의 순서가 주어진 수강번호의 순서와 일치한다. 
	 * @param p_attnumlist 수강번호리스트
	 * @return 성적정보리스트(평점보여짐여부, 평점)
	 * @throws SQLException DB오류
	 * ! DAO명세서 수정필요 - "반환될 성적정보의 순서가 주어진 수강번호의 순서와 일치한다."
	 * ! DAO명세서 수정필요 - 평점보여짐여부를 조사하기 위해 LectureEvaluationDAO와 협력한다
	 */
	public List<GradeInfo> getGradeListByAttCodeList(List<Integer> p_attnumlist) throws SQLException
	{
		// Tip: PreparedStatement로 IN 구문 쓰기
		// https://stackoverflow.com/questions/178479/preparedstatement-in-clause-alternatives/10240302#10240302
		// https://stackoverflow.com/a/2861510
		
		// Example
		// <Input>  ->  <Output>
		// 10024    ->  3.5, false
		// 10035    ->  (null)
		// 10115    ->  (null)
		// 10097    ->  4.0, true
		// 10013    ->  2.5, fals
		
		// 입력물과 반환물의 리스트 길이는 똑같아야 한다.
		List<GradeInfo> gradeInfoList = new ArrayList<GradeInfo>( Collections.nCopies(p_attnumlist.size(), null) );
		// 수강리스트가 비었다면 SQL문을 실행할 수고 조차 들여선 안된다.
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
			
			// 각 수강번호에 해당하는 평점을 채워넣는다.
			while(rs.next())
			{
				int attNum = rs.getInt("attendanceNum");
				double grade = rs.getDouble("grade");
				gradeInfoList.set(
						p_attnumlist.indexOf(attNum),
						new GradeInfo(grade, false)
						);
			}
			
			// 각 수강번호에 해당하는 "평점보여짐여부"를 채워넣는다. 이 때 각 수강번호에 해당하는 강의평가여부가 필요하다.
			HashMap<Integer, Boolean> el = lectureEvaluationDAO.getLectureEvaluationExistList(p_attnumlist);
			for( int attNum : p_attnumlist )
			{
				Boolean evaluated = el.get(attNum); //해당 수강의 강의평가여부
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
