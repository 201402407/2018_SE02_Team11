package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ClassObject.LectureEvaluation;

public class LectureEvaluationDAO extends DAOBase {
	// 데이터베이스 접근을 위해
	private static PreparedStatement pstmt;
	private static Connection conn;
		
	public LectureEvaluationDAO() {
		super();
	}
	
	/** 강의평가여부리스트 조회
	 * @param p_list 수강번호리스트 -> List<Integer>
	 * @return 강의평가여부리스트(boolean) -> HashMap<Integer, Boolean>
	 * ! DB에서 가져온 attendanceNum과 인자로 받은 p_list의 attendanceNum이 같으면 뜻 ?
	 * ! // 변경실패 시 진행방법 ??
	 * ! DAO HashMap 사용으로 변경 해야함*/
	public HashMap<Integer, Boolean> getLectureEvaluationExistList(List<Integer> p_list) {
		List<LectureEvaluation> temp = new ArrayList<LectureEvaluation>();
		
		try {
			String SQL = "SELECT * FROM LectureEvaluation"
					+ " WHERE attendanceNum = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			int i = 0;
			
			// 강의평가리스트 조회하기
			while(i < p_list.size()) {
				pstmt.setInt(1, p_list.get(i));
				ResultSet rs = pstmt.executeQuery();
				int rsAttendanceNum = rs.getInt("attendanceNum"); // 수강번호 가져오기
				String rsText = rs.getString("LectureEvaluationText"); // 강의평가내용 가져오기
				
				LectureEvaluation lectureEvaluation = new LectureEvaluation(
						rsText,
						rsAttendanceNum
						);
						
				temp.add(lectureEvaluation);
				pstmt.clearParameters(); // set 한 인자 초기화
				i++;
			}	
		}catch(Exception e){
	        e.printStackTrace();
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }

		HashMap<Integer, Boolean> hashMap = new HashMap<>(); // 생성
		// 똑같은 길이만큼 false로 초기화
		for(int i = 0; i < p_list.size(); i++) {
			hashMap.put(p_list.get(i), false);
		}
		
		// 평가리스트 비교
		for(int i = 0; i < temp.size(); i++) {
			// DB에서 가져온 attendanceNum과 인자로 받은 p_list의 attendanceNum이 같으면
			if(temp.get(i).getAttendanceNum() == p_list.get(i)) {
				if(hashMap.replace(p_list.get(i), true) == null) { // 변경실패
					
				}
			}
		}
		temp.clear(); // 비우기
		return hashMap;
	}
	
	/** 강의평가여부 조회
	 * @param p_attendancenum 수강번호
	 * @return 강의평가여부결과(boolean)
	 * ! DAO 알고리즘 결과 반환 수정 필요*/
	public boolean isLectureEvaluationExist(int p_attendancenum) {
		
		try {
			String SQL = "SELECT * FROM LectureEvaluation"
					+ " WHERE attendanceNum = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_attendancenum);
			ResultSet rs = pstmt.executeQuery();
			
			// 조회결과 존재함 
			if(rs.next()) 
				return true;	
			
		}catch(Exception e){
	        e.printStackTrace();
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
		return false;
	}
	
	/** 강의평가작성 
	 * @param p_attendancenum 수강번호
	 * @param p_text 강의평가내용
	 * @return 작성성공결과(boolean)
	 * ! DAO 경우에 따른 결과 추가 필요*/
	public boolean writeLectureEvaluation(int p_attendancenum, String p_text) {
		
		try {
			String SQL = "INSERT INTO LectureEvaluation (LectureEvaluationText, attendanceNum)"
					+ " VALUES (?, ?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_attendancenum);
			pstmt.setString(2, p_text);
			int result = pstmt.executeUpdate(); 
			
			// SQL 실패
			if(result != 1)
				return false;
			
		}catch(Exception e){
	        e.printStackTrace();
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }
		return true;
	}
}
