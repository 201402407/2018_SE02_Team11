package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ClassObject.LectureDetail;
import Util.OurTimes;

public class LectureDAO extends DAOBase {
	
	// 학생의 학과 조사를 위해
	private StudentDAO studentDAO;
	
	// 데이터베이스 접근을 위해
	private static PreparedStatement pstmt;
	private static Connection conn;
	
	// 단순히 상속을 해주면 끝.
	public LectureDAO ()
	{
		super();
		
		studentDAO = new StudentDAO();
	}
	
	/**
	 * 수강신청가능리스트조회
	 * @param p_sid 학번
	 * @return 분반상세정보리스트(분반코드, 과목명, 교수이름, 등록학기, 신청인원, 전체인원, 강의요일, 강의시작시각, 강의종료시각, 학점)
	 * @return null 선조건이 맞지 않는 경우.
	 */
	public List<LectureDetail> getApplyLectureLits(int p_sid)
	{
		boolean isTimeOff = studentDAO.getTimeOffBySID(p_sid);
		
		if( isTimeOff && OurTimes.isNowAbleToAddAttendance() )
		{
			int dcode = DepartmentDAO.getDCodeBySID(p_sid);  //해당학과
			int currentTerm = OurTimes.currentTerm();  //현재학기
			
			try
			{
				conn = getConnection();
				String sql = "SELECT L.lectureCode, S.subjectName, P.name, L.registerTerm, L.applyNum, L.allNum, L.dayOfWeek, L.startTime, L.endTime, S.score" + 
						"FROM Lecture L WHERE L.registerTerm = ?" + 
						"LEFT JOIN Subject S ON L.subjectCode = S.subjectCode" + 
						"LEFT JOIN Professor P ON L.profCode = P.professorCode";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, currentTerm); //현재학기
				ResultSet rs = pstmt.executeQuery();
				
				// 조회
				List<LectureDetail> lectureDetailList = new ArrayList<LectureDetail>();
				while(rs.next())
				{
					LectureDetail ld = new LectureDetail(
							rs.getInt("lectureCode"),  //분반코드
							rs.getString("subjectName"),  //과목명
							rs.getString("profName"),  //교수이름
							rs.getInt("registerTerm"),  //등록학기
							rs.getInt("applyNum"),  //신청인원
							rs.getInt("allNum"),  //전체인원
							OurTimes.intToDayOfWeek(rs.getInt("dayOfWeek")),  //강의요일
							OurTimes.sqltimeToLocalTime(rs.getTime("startTime")),  //강의시작시각
							OurTimes.sqltimeToLocalTime(rs.getTime("endTime")),  //강의종료시각
							rs.getDouble("score")
					);
					lectureDetailList.add(ld);
				}
			}
			catch (SQLException sqle)
			{
				sqle.printStackTrace();
				return null;
			}
			finally
			{
			      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
			      if(conn != null) try{conn.close();}catch(SQLException sqle){}
			}
			
		}
		else
			return null;
	}
}
