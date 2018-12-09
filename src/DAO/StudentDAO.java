package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ClassObject.ChangeType;
import ClassObject.StudentInfo;

public class StudentDAO extends DAOBase {
	static Connection conn;
	static PreparedStatement pstmt;
	
	// 협업 DAO
	
	// 생성자 생성과 동시에 jbdc 설정.
	public StudentDAO() {
		super();
		
	}
	
	/** 학적상태조회 
	 * @param p_sid 학번
	 * @return 학적상태(이름, 학과명, 학년, 이수중인학기, 휴학여부, 졸업여부, 은행계좌번호)
	 * @throws SQLException DB오류
	 * ! DAO수정 - 은행정보를 제거
	 * */
	public List<StudentInfo> getStudentInfoBySID(int p_sid) throws SQLException {
		
		List<StudentInfo> arrayList = new ArrayList<StudentInfo>();
		
		try {
			String SQL = "SELECT S.studentName, D.departmentName, S.studentYear, S.semester,"
					+ " S.isTimeOff, S.isGraduate"
					+ " FROM Student S"
					+ " LEFT JOIN Department D" 
					+ " ON S.departmentCode = D.departmentCode"
					+ " WHERE S.studentID = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_sid);
			ResultSet rs = pstmt.executeQuery(); // ResultSet
			
			// 꺼내오기
			if (rs.next())
			{
				String rsSName = rs.getString("studentName");
				String rsDName = rs.getString("departmentName");
				int rsYear = rs.getInt("studentYear");
				int rsSemester = rs.getInt("semester");
				boolean rsTimeOff = rs.getBoolean("isTimeOff");
				boolean rsGraduate = rs.getBoolean("isGraduate");
			
				StudentInfo studentInfo = new StudentInfo(rsSName,
						rsDName,
						rsYear,
						rsSemester,
						rsTimeOff,
						rsGraduate
				);
				arrayList.add(studentInfo);
			}
		   	
			return arrayList;
			
		}catch(SQLException e) {
		      throw e;
		      
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		
	}
	
	/** 휴복학설정
	 * @param p_sid 학번
	 * @param p_changetype 휴복학여부
	 * @return 성공결과(boolean)
	 * @throws SQLException DB오류 */
	public boolean setTimeOff(int p_sid, ChangeType p_changetype) throws SQLException {
		
		try {
			String SQL = "UPDATE Student" + 
					" SET Student.isTimeOff = ?"
					+ " WHERE Student.studentID = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			boolean isTimeoff = (p_changetype == ChangeType.TAKEOFF) ? true : false;
			pstmt.setBoolean(1, isTimeoff);
			pstmt.setInt(2, p_sid);
			int result = pstmt.executeUpdate(); // 변경된 row수 만큼 리턴
			if(result == 1) // 1줄만 변경하므로 성공
				return true;
			return false;
		}catch(SQLException e) {
		      throw e;
		      
		}finally { // 삭제는 반대 순서
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		
	}
	/* 휴복학설정 */
	
	
	/* 휴복학요청거절알림 */
	/*
	public boolean rejectTimeOff(int p_sid, String p_reason) {
		
		try {
			INSERT INTO 학생알림함 (학번, 메시지텍스트)
			VALUES ( p_sid, "[휴복학요청이 거절되었습니다.]" + p_reason )
			String SQL = "INSERT INTO 학생알림함 (학번, 메시지텍스트)" + 
					" SET Student.isTimeOff = ?"
					+ " WHERE Student.studentID = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setBoolean(1, p_changetype);
			pstmt.setInt(2, p_sid);
			int result = pstmt.executeUpdate(); // 변경된 row수 만큼 리턴
			if(result == 1) // 1줄만 변경하므로 성공
				return true;
		}catch(Exception e) {
		      e.printStackTrace();
		      
		}finally { // 삭제는 반대 순서
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	}
	*/
	
	/** 휴학여부조회
	 * @param p_sid 학번
	 * @return 휴학여부(boolean) -> true : 휴학, false : 재학
	 * @throws SQLException DB오류, p_sid 존재안함
	 * ! DAO 완료 */
	public boolean getTimeOffBySID(int p_sid) throws SQLException {
		
		try {
			String SQL = "SELECT isTimeOff FROM Student" + 
					" WHERE studentID = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_sid);
			ResultSet rs = pstmt.executeQuery(); // ResultSet

			// 존재하면
			if(rs.next()) {
				return rs.getBoolean("isTimeOff");
			}
			else {
				throw new SQLException("No such student ID.");
			}
			
		}catch(SQLException e) {
		      throw e;
		      
		}finally { // 삭제는 반대 순서
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	}
	
	/** 신규학번설정 
	 * @param p_newStuYear 등록년도
	 * @param p_newStuOrder 등록순서
	 * @param p_name 학생이름
	 * @param p_accountID 계정아이디
	 * @param p_dcode 학과코드
	 * @return 학번 (실패시 -1)
	 * @throws SQLException DB오류
	 * ! DAO 리턴값 + 알고리즘 수정해야 함, year->studentYear
	 * */
	public int createNewStudent(int p_newStuYear, int p_newStuOrder, String p_name, String p_accountID, int p_dcode) 
	throws SQLException {
		
		int newStuID = p_newStuYear * 100000 + p_newStuOrder;
		
		try {
			String SQL = "INSERT INTO Student (studentID, studentName, studentYear,"
					+ " semester, isTimeOff, isGraduate, accountID, departmentCode)"
					+ " VALUES (?, ?, 1, 1, FALSE, FALSE, ?, ?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, newStuID);
			pstmt.setString(2, p_name);
			pstmt.setString(3, p_accountID);
			pstmt.setInt(4, p_dcode);
		    int result = pstmt.executeUpdate();
		    if(result != 1)
		    	return -1;
		    return newStuID;
		}catch(SQLException e) {
		      throw e;
		}finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	}
	
	/** 학생존재여부
	 * @param p_sid 학번
	 * @return 존재여부(boolean) -> 존재 : true, 미존재 : false
	 * @throws SQLException DB오류
	 * */
	public boolean isStudentExist(int p_sid) throws SQLException {
		try {
			String SQL = "SELECT * FROM Student" + 
					" WHERE studentID = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_sid);
			ResultSet rs = pstmt.executeQuery(); // ResultSet

			// 존재하면
			if(rs.next()) {
				return true;
			}
			return false;	
		}catch(SQLException e) {
		      throw e;
		      
		}finally { // 삭제는 반대 순서
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	}
}
