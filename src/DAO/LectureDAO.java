package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import ClassObject.LectureDetail;
import Util.OurSchoolPolicy;
import Util.OurTimes;

public class LectureDAO extends DAOBase {
	
	/* 2018-12-04 구현 메모
	 * DB오류가 났으면 return 대신 throw를 하도록 하자.
	 * 그리하여 호출자에게 DB오류가 났음을 알리자. (리스트가 그냥 빈 것과, DB오류로 인해 리스트가 빈 것은 차이가 있다.)
	 */
	
	public enum AddLectureResult {
		SUCCESS,
		NOT_FOUND_SUBJCODE,
		NOT_FOUND_PROFCODE,
		NOT_FOUND_DEPCODE,
		INVALID_TERM,
		INVALID_ALLNUM, // !DAO에 추가되야 할 것 (addLecture 참고)
		INVALID_DAY_OF_WEEK,  // !DAO에 추가되야 할 것 (addLecture 참고)
		INVALID_S_E_TIME,  // !DAO에 추가되야 할 것 (addLecture 참고)
	}
	
	// 협업하는 DAO
	private StudentDAO studentDAO;
	private DepartmentDAO departmentDAO;
	private SyllabusDAO syllabusDAO;
	private SubjectDAO subjectDAO;
	private ProfessorDAO professorDAO;
	
	// 데이터베이스 접근을 위해
	private PreparedStatement pstmt;
	private Connection conn;
	
	// 단순히 상속을 해주면 끝.
	public LectureDAO ()
	{
		super();
		
		studentDAO = new StudentDAO();
		departmentDAO = new DepartmentDAO();
		syllabusDAO = new SyllabusDAO();
		subjectDAO = new SubjectDAO();
		professorDAO = new ProfessorDAO();
	}
	
	/**
	 * 수강신청가능리스트조회
	 * @param p_sid 학번
	 * @return 분반상세정보리스트(분반코드, 과목명, 교수이름, 등록학기, 신청인원, 전체인원, 강의요일, 강의시작시각, 강의종료시각, 학점).
	 * 학생이 휴학중이거나 수강신청기간이 아닌경우 null
	 * @throws SQLException DB오류.
	 * ! DAO명세서 수정
	 */
	public List<LectureDetail> getApplyLectureList(int p_sid) throws SQLException
	{
		try
		{
			// 리턴 결과물
			List<LectureDetail> lectureDetailList;
			
			// 선조건
			boolean isTimeOff = studentDAO.getTimeOffBySID(p_sid);
			if( !(!isTimeOff && OurTimes.isNowAbleToAddAttendance()) )
				return null;
			// 학생의 해당학과를 구한다
			int dcode = departmentDAO.getDCodeBySID(p_sid);
			// 현재학기: 아직은 학기중이 아니나, 수강신청기간동안 "현재학기"는 앞으로 다가올 학기 중 가장 가까운 것이다.
			int currentApplyTerm = OurTimes.closestFutureTerm();
			
			// DB 접속
			conn = getConnection();
			// 쿼리문 설정
			String sql = "SELECT L.lectureCode, S.subjectName, P.profName, L.registerTerm, L.applyNum, L.allNum, L.dayOfWeek, L.startTime, L.endTime, S.score\r\n" + 
					"FROM Lecture L\r\n" + 
					"LEFT JOIN Subject S ON L.subjectCode = S.subjectCode\r\n" + 
					"LEFT JOIN Professor P ON L.profCode = P.professorCode\r\n" + 
					"WHERE L.registerTerm = ? AND L.departmentCode = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, currentApplyTerm);
			pstmt.setInt(2, dcode);
			// 쿼리 실행
			ResultSet rs = pstmt.executeQuery();
			// 이후 결과 처리(rs)...
			
			// 조회하여 나온 리스트를 추출
			lectureDetailList = new ArrayList<LectureDetail>();
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
						OurTimes.sqlTimeToLocalTime(rs.getTime("startTime")),  //강의시작시각
						OurTimes.sqlTimeToLocalTime(rs.getTime("endTime")),  //강의종료시각
						rs.getDouble("score")  //학점
				);
				lectureDetailList.add(ld);
			}
			
			return lectureDetailList;
		}
		catch (SQLException sqle) {
			throw sqle;
		} finally {
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	}
	
	/**
	 * 과목검색. 선조건은 "수강신청리스트조회"를 따른다.
	 * @param p_sid 학번
	 * @param p_subjectname 과목명
	 * @return 수강신청가능리스트 중 주어진 과목명에 해당하는 것, 휴학중이거나 수강신청기간 아니면 null
	 * @throws SQLException DB오류
	 */
	public List<LectureDetail> getLectureByName(int p_sid, String p_subjectname) throws SQLException
	{
		try
		{
			// 리턴 결과물
			List<LectureDetail> lectureDetailList;
			
			// 선조건
			boolean isTimeOff = studentDAO.getTimeOffBySID(p_sid);
			if( !(!isTimeOff && OurTimes.isNowAbleToAddAttendance()) )
				return null;
			
			// 해당학과
			int dcode = departmentDAO.getDCodeBySID(p_sid);
			// 수강신청기간동안 "현재학기"는 앞으로 다가올 학기 중 가장 가까운 것이다.
			int currentApplyTerm = OurTimes.closestFutureTerm();
			// 과목명을 검색코자 한다.
			// https://stackoverflow.com/questions/8247970/using-like-wildcard-in-prepared-statement
			String subjectname_wildcarded = "%" + p_subjectname
					.replace("!", "!!")
					.replace("%", "!%")
					.replace("_", "!_")
					.replace("[", "![") + "%";
			
			// DB 접속
			conn = getConnection();
			// 쿼리문 설정
			String sql = "SELECT L.lectureCode, S.subjectName, P.profName, L.registerTerm, L.applyNum, L.allNum, L.dayOfWeek, L.startTime, L.endTime, S.score\r\n" + 
					"FROM Lecture L\r\n" + 
					"LEFT JOIN Subject S ON L.subjectCode = S.subjectCode\r\n" + 
					"LEFT JOIN Professor P ON L.profCode = P.professorCode\r\n" + 
					"WHERE L.registerTerm = ? AND S.subjectName LIKE ? ESCAPE '!' AND L.departmentCode = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, currentApplyTerm);
			pstmt.setString(2, subjectname_wildcarded);
			pstmt.setInt(3, dcode);
			// 쿼리 실행
			ResultSet rs = pstmt.executeQuery();
			// 이후 결과 처리(rs)...
			
			// 조회하여 나온 리스트를 추출
			lectureDetailList = new ArrayList<LectureDetail>();
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
						OurTimes.sqlTimeToLocalTime(rs.getTime("startTime")),  //강의시작시각
						OurTimes.sqlTimeToLocalTime(rs.getTime("endTime")),  //강의종료시각
						rs.getDouble("score")  //학점
				);
				lectureDetailList.add(ld);
			}
			
			return lectureDetailList;
		}
		catch (SQLException sqle)
		{
			throw sqle;
		}
		finally
		{
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	}
	
	/**
	 * 강의계획서 조회. 분반코드를 가진 분반의 강의계획서를 조회한다.
	 * @param p_lcode 분반코드
	 * @return 문자열로 된 강의계획서 내용물. 분반추가 당시 강의계획서 내용물을 입력하지 않았으면 "".
	 */
	public String getSyllabusByLCode(int p_lcode) throws SQLException
	{
		// 해당 분반의 강의계획서번호를 가져온다. -> sylCode
		// 1개의 분반 엔티티는 항상 1개의 강의계획서 엔티티를 갖고 있다.
		int sylCode;
		try {
			//DB 접속
			conn = getConnection();
			//쿼리문 설정
			String sql = "SELECT syllabusCode FROM Lecture WHERE lectureCode = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, p_lcode);
			//쿼리
			ResultSet rs = pstmt.executeQuery();
			// 이후 결과 처리(rs)...
			
			if(!rs.next())
				return null;
			sylCode = rs.getInt("syllabusCode");
		}
		catch (SQLException sqle)
		{
			// 강의계획서번호를 가져오는 쿼리 실행에서 DB오류가 발생.
			throw sqle;
		}
		finally
		{
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		
		// 강의계획서 내용물을 가져온다. -> sylText
		// DB성공, 내용물비었음 -> getTextBySYLCode는 ""을 반환한다
		// DB실패 -> getTextBySYLcode는 null을 반환한다.
		String sylText = syllabusDAO.getTextBySYLCode(sylCode);
		if (sylText.isEmpty())
			sylText = null;
		
		return sylText;
	}
	
	/**
	 * 분반상세정보조회
	 * @param p_lcode 분반코드
	 * @return 분반상세정보(분반코드, 과목명, 교수이름, 등록학기, 신청인원, 전체인원, 강의요일, 강의시작시각, 강의종료시각, 학점).
	 * 분반코드에 해당하는 분반이 존재하지 않으면 null
	 * @throws SQLException DB오류.
	 */
	public LectureDetail getLectureInfoByLCode(int p_lcode) throws SQLException
	{
		// 리턴 결과물
		LectureDetail lectureDetail;
		
		try {
			// DB 연결
			conn = getConnection();
			// 쿼리문 설정
			String sql = "SELECT L.lectureCode, S.subjectName, P.profName, L.registerTerm, L.applyNum, L.allNum, L.dayOfWeek, L.startTime, L.endTime, S.score\r\n" + 
					"FROM Lecture L\r\n" + 
					"LEFT JOIN Subject S ON L.subjectCode = S.subjectCode\r\n" + 
					"LEFT JOIN Professor P ON L.profCode = P.professorCode\r\n" + 
					"WHERE L.lectureCode = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, p_lcode);
			// 쿼리 실행
			ResultSet rs = pstmt.executeQuery();
			// 이후 결과 처리(rs)...
			
			if(!rs.next())
				return null;
			
			lectureDetail = new LectureDetail(
				rs.getInt("lectureCode"),  //분반코드
				rs.getString("subjectName"),  //과목명
				rs.getString("profName"),  //교수이름
				rs.getInt("registerTerm"),  //등록학기
				rs.getInt("applyNum"),  //신청인원
				rs.getInt("allNum"),  //전체인원
				OurTimes.intToDayOfWeek(rs.getInt("dayOfWeek")),  //강의요일
				OurTimes.sqlTimeToLocalTime(rs.getTime("startTime")),  //강의시작시각
				OurTimes.sqlTimeToLocalTime(rs.getTime("endTime")),  //강의종료시각
				rs.getDouble("score")  //학점
			);
			return lectureDetail;
		}
		catch(SQLException sqle)
		{
			throw sqle;
		}
		finally
		{
			if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		    if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	}
	
	/**
	 * 분반추가. 
	 * [입력조건]
	 * 학과코드와 교수등록번호와, 학과코드는 존재하는 것이어야 함.
	 * 등록학기는 "가장 가까운 미래학기" 이후(>=)여야 한다.
	 * 전체인원은 정해진 범위 안에 있어야 하며, OurSchoolPolicy 클래스를 참고바람.
	 * 강의요일은 월요일과 금요일 사이여야 함.
	 * 강의시작시각은 강의종료시각보다 앞서야 함.
	 * @param p_subjcode 과목코드
	 * @param p_profcode 교수등록번호
	 * @param p_depcode 학과코드
	 * @param p_registerterm 등록학기
	 * @param p_allnum 전체인원
	 * @param p_dayofweek 강의요일
	 * @param p_starttime 강의시작시각
	 * @param p_endtime 강의종료시각
	 * @param p_sylText 강의계획서내용
	 * @return 분반추가결과(enum)
	 * @throws SQLException DB오류
	 * ! DAO수정 필요 (설명란과 반환값란에 타당성 검사 추가)
	 */
	public AddLectureResult addLecture (
			int p_subjcode, int p_profcode, int p_depcode,
			int p_registerterm,
			int p_allnum,
			DayOfWeek p_dayofweek, LocalTime p_starttime, LocalTime p_endtime,
			String p_sylText
			) throws SQLException
	{
		// 파라메터의 타당성 검사
		if( ! subjectDAO.isSubjectExistBySCode(p_subjcode) )
			return AddLectureResult.NOT_FOUND_SUBJCODE;
		if( ! professorDAO.IsProfessorByPCode(p_profcode) )
			return AddLectureResult.NOT_FOUND_PROFCODE;
		if( ! departmentDAO.isDepartmentExistByDCode(p_depcode) )
			return AddLectureResult.NOT_FOUND_DEPCODE;
		if( ! isValidRegisterTerm(p_registerterm) )
			return AddLectureResult.INVALID_TERM;
		if( ! isValidAllNum(p_allnum) )
			return AddLectureResult.INVALID_ALLNUM;
		if( ! isValidDayOfWeek(p_dayofweek) )
			return AddLectureResult.INVALID_DAY_OF_WEEK;
		if( ! isValidStartEndTime(p_starttime, p_endtime) )
			return AddLectureResult.INVALID_S_E_TIME;
		// 강의계획서내용물은 비어있을 수 있다.
		if(p_sylText == null)
			p_sylText = "";
		
		try {
			// 강의계획서를 등록하고, 그 번호(Last Inserted ID)를 가져온다.
			int sylCode = syllabusDAO.addSyllabus(p_sylText);
			
			// DB 접속
			conn = getConnection();
			// 쿼리문 설정
			String sql = "INSERT INTO Lecture (registerTerm, applyNum, allNum, dayofweek, startTime, endTime, subjectCode, departmentCode, profCode, syllabusCode)\r\n" + 
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, p_registerterm);
			pstmt.setInt(2, 0);  // applyNum(수강인원)
			pstmt.setInt(3, p_allnum);
			pstmt.setInt(4, OurTimes.dayOfWeekToInt(p_dayofweek) );
			pstmt.setTime(5, OurTimes.LocalTimeTosqlTime(p_starttime) );
			pstmt.setTime(6, OurTimes.LocalTimeTosqlTime(p_endtime) );
			pstmt.setInt(7, p_subjcode);
			pstmt.setInt(8, p_depcode);
			pstmt.setInt(9, p_profcode);
			pstmt.setInt(10, sylCode);
			
			// 쿼리 실행
			int result = pstmt.executeUpdate();
			// 이후 결과 처리...
			
			if (result != 1)
				throw new SQLException("Affected Rows: " + result);
			else
				return AddLectureResult.SUCCESS;
		}
		catch(SQLException sqle){
			throw sqle;
		} finally {
			if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		    if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
	}
	private boolean isValidRegisterTerm(int p_registerterm) {
		return p_registerterm >= OurTimes.closestFutureTerm();
	}
	private boolean isValidAllNum(int p_allnum) {
		int min = OurSchoolPolicy.MIN_ALLNUM;
		int max = OurSchoolPolicy.MAX_ALLNUM;
		return p_allnum >= min && p_allnum <= max;
	}
	private boolean isValidDayOfWeek(DayOfWeek dow) {
		return dow == DayOfWeek.MONDAY || dow == DayOfWeek.TUESDAY || dow == DayOfWeek.WEDNESDAY || dow == DayOfWeek.THURSDAY || dow == DayOfWeek.FRIDAY;
	}
	private boolean isValidStartEndTime(LocalTime p_starttime, LocalTime p_endtime) {
		return p_starttime.compareTo(p_endtime) < 0;
	}
}
