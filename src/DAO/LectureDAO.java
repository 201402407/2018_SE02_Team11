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
	
	/* 2018-12-04 ���� �޸�
	 * DB������ ������ return ��� throw�� �ϵ��� ����.
	 * �׸��Ͽ� ȣ���ڿ��� DB������ ������ �˸���. (����Ʈ�� �׳� �� �Ͱ�, DB������ ���� ����Ʈ�� �� ���� ���̰� �ִ�.)
	 */
	
	public enum AddLectureResult {
		SUCCESS,
		NOT_FOUND_SUBJCODE,
		NOT_FOUND_PROFCODE,
		NOT_FOUND_DEPCODE,
		INVALID_TERM,
		INVALID_ALLNUM, // !DAO�� �߰��Ǿ� �� �� (addLecture ����)
		INVALID_DAY_OF_WEEK,  // !DAO�� �߰��Ǿ� �� �� (addLecture ����)
		INVALID_S_E_TIME,  // !DAO�� �߰��Ǿ� �� �� (addLecture ����)
	}
	
	// �����ϴ� DAO
	private StudentDAO studentDAO;
	private DepartmentDAO departmentDAO;
	private SyllabusDAO syllabusDAO;
	private SubjectDAO subjectDAO;
	private ProfessorDAO professorDAO;
	
	// �����ͺ��̽� ������ ����
	private PreparedStatement pstmt;
	private Connection conn;
	
	// �ܼ��� ����� ���ָ� ��.
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
	 * ������û���ɸ���Ʈ��ȸ
	 * @param p_sid �й�
	 * @return �йݻ���������Ʈ(�й��ڵ�, �����, �����̸�, ����б�, ��û�ο�, ��ü�ο�, ���ǿ���, ���ǽ��۽ð�, ��������ð�, ����).
	 * �л��� �������̰ų� ������û�Ⱓ�� �ƴѰ�� null
	 * @throws SQLException DB����.
	 * ! DAO���� ����
	 */
	public List<LectureDetail> getApplyLectureList(int p_sid) throws SQLException
	{
		try
		{
			// ���� �����
			List<LectureDetail> lectureDetailList;
			
			// ������
			boolean isTimeOff = studentDAO.getTimeOffBySID(p_sid);
			if( !(!isTimeOff && OurTimes.isNowAbleToAddAttendance()) )
				return null;
			// �л��� �ش��а��� ���Ѵ�
			int dcode = departmentDAO.getDCodeBySID(p_sid);
			// �����б�: ������ �б����� �ƴϳ�, ������û�Ⱓ���� "�����б�"�� ������ �ٰ��� �б� �� ���� ����� ���̴�.
			int currentApplyTerm = OurTimes.closestFutureTerm();
			
			// DB ����
			conn = getConnection();
			// ������ ����
			String sql = "SELECT L.lectureCode, S.subjectName, P.profName, L.registerTerm, L.applyNum, L.allNum, L.dayOfWeek, L.startTime, L.endTime, S.score\r\n" + 
					"FROM Lecture L\r\n" + 
					"LEFT JOIN Subject S ON L.subjectCode = S.subjectCode\r\n" + 
					"LEFT JOIN Professor P ON L.profCode = P.professorCode\r\n" + 
					"WHERE L.registerTerm = ? AND L.departmentCode = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, currentApplyTerm);
			pstmt.setInt(2, dcode);
			// ���� ����
			ResultSet rs = pstmt.executeQuery();
			// ���� ��� ó��(rs)...
			
			// ��ȸ�Ͽ� ���� ����Ʈ�� ����
			lectureDetailList = new ArrayList<LectureDetail>();
			while(rs.next())
			{
				LectureDetail ld = new LectureDetail(
						rs.getInt("lectureCode"),  //�й��ڵ�
						rs.getString("subjectName"),  //�����
						rs.getString("profName"),  //�����̸�
						rs.getInt("registerTerm"),  //����б�
						rs.getInt("applyNum"),  //��û�ο�
						rs.getInt("allNum"),  //��ü�ο�
						OurTimes.intToDayOfWeek(rs.getInt("dayOfWeek")),  //���ǿ���
						OurTimes.sqlTimeToLocalTime(rs.getTime("startTime")),  //���ǽ��۽ð�
						OurTimes.sqlTimeToLocalTime(rs.getTime("endTime")),  //��������ð�
						rs.getDouble("score")  //����
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
	 * ����˻�. �������� "������û����Ʈ��ȸ"�� ������.
	 * @param p_sid �й�
	 * @param p_subjectname �����
	 * @return ������û���ɸ���Ʈ �� �־��� ����� �ش��ϴ� ��, �������̰ų� ������û�Ⱓ �ƴϸ� null
	 * @throws SQLException DB����
	 */
	public List<LectureDetail> getLectureByName(int p_sid, String p_subjectname) throws SQLException
	{
		try
		{
			// ���� �����
			List<LectureDetail> lectureDetailList;
			
			// ������
			boolean isTimeOff = studentDAO.getTimeOffBySID(p_sid);
			if( !(!isTimeOff && OurTimes.isNowAbleToAddAttendance()) )
				return null;
			
			// �ش��а�
			int dcode = departmentDAO.getDCodeBySID(p_sid);
			// ������û�Ⱓ���� "�����б�"�� ������ �ٰ��� �б� �� ���� ����� ���̴�.
			int currentApplyTerm = OurTimes.closestFutureTerm();
			// ������� �˻����� �Ѵ�.
			// https://stackoverflow.com/questions/8247970/using-like-wildcard-in-prepared-statement
			String subjectname_wildcarded = "%" + p_subjectname
					.replace("!", "!!")
					.replace("%", "!%")
					.replace("_", "!_")
					.replace("[", "![") + "%";
			
			// DB ����
			conn = getConnection();
			// ������ ����
			String sql = "SELECT L.lectureCode, S.subjectName, P.profName, L.registerTerm, L.applyNum, L.allNum, L.dayOfWeek, L.startTime, L.endTime, S.score\r\n" + 
					"FROM Lecture L\r\n" + 
					"LEFT JOIN Subject S ON L.subjectCode = S.subjectCode\r\n" + 
					"LEFT JOIN Professor P ON L.profCode = P.professorCode\r\n" + 
					"WHERE L.registerTerm = ? AND S.subjectName LIKE ? ESCAPE '!' AND L.departmentCode = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, currentApplyTerm);
			pstmt.setString(2, subjectname_wildcarded);
			pstmt.setInt(3, dcode);
			// ���� ����
			ResultSet rs = pstmt.executeQuery();
			// ���� ��� ó��(rs)...
			
			// ��ȸ�Ͽ� ���� ����Ʈ�� ����
			lectureDetailList = new ArrayList<LectureDetail>();
			while(rs.next())
			{
				LectureDetail ld = new LectureDetail(
						rs.getInt("lectureCode"),  //�й��ڵ�
						rs.getString("subjectName"),  //�����
						rs.getString("profName"),  //�����̸�
						rs.getInt("registerTerm"),  //����б�
						rs.getInt("applyNum"),  //��û�ο�
						rs.getInt("allNum"),  //��ü�ο�
						OurTimes.intToDayOfWeek(rs.getInt("dayOfWeek")),  //���ǿ���
						OurTimes.sqlTimeToLocalTime(rs.getTime("startTime")),  //���ǽ��۽ð�
						OurTimes.sqlTimeToLocalTime(rs.getTime("endTime")),  //��������ð�
						rs.getDouble("score")  //����
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
	 * ���ǰ�ȹ�� ��ȸ. �й��ڵ带 ���� �й��� ���ǰ�ȹ���� ��ȸ�Ѵ�.
	 * @param p_lcode �й��ڵ�
	 * @return ���ڿ��� �� ���ǰ�ȹ�� ���빰. �й��߰� ��� ���ǰ�ȹ�� ���빰�� �Է����� �ʾ����� "".
	 */
	public String getSyllabusByLCode(int p_lcode) throws SQLException
	{
		// �ش� �й��� ���ǰ�ȹ����ȣ�� �����´�. -> sylCode
		// 1���� �й� ��ƼƼ�� �׻� 1���� ���ǰ�ȹ�� ��ƼƼ�� ���� �ִ�.
		int sylCode;
		try {
			//DB ����
			conn = getConnection();
			//������ ����
			String sql = "SELECT syllabusCode FROM Lecture WHERE lectureCode = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, p_lcode);
			//����
			ResultSet rs = pstmt.executeQuery();
			// ���� ��� ó��(rs)...
			
			if(!rs.next())
				return null;
			sylCode = rs.getInt("syllabusCode");
		}
		catch (SQLException sqle)
		{
			// ���ǰ�ȹ����ȣ�� �������� ���� ���࿡�� DB������ �߻�.
			throw sqle;
		}
		finally
		{
		      if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
		}
		
		// ���ǰ�ȹ�� ���빰�� �����´�. -> sylText
		// DB����, ���빰����� -> getTextBySYLCode�� ""�� ��ȯ�Ѵ�
		// DB���� -> getTextBySYLcode�� null�� ��ȯ�Ѵ�.
		String sylText = syllabusDAO.getTextBySYLCode(sylCode);
		if (sylText.isEmpty())
			sylText = null;
		
		return sylText;
	}
	
	/**
	 * �йݻ�������ȸ
	 * @param p_lcode �й��ڵ�
	 * @return �йݻ�����(�й��ڵ�, �����, �����̸�, ����б�, ��û�ο�, ��ü�ο�, ���ǿ���, ���ǽ��۽ð�, ��������ð�, ����).
	 * �й��ڵ忡 �ش��ϴ� �й��� �������� ������ null
	 * @throws SQLException DB����.
	 */
	public LectureDetail getLectureInfoByLCode(int p_lcode) throws SQLException
	{
		// ���� �����
		LectureDetail lectureDetail;
		
		try {
			// DB ����
			conn = getConnection();
			// ������ ����
			String sql = "SELECT L.lectureCode, S.subjectName, P.profName, L.registerTerm, L.applyNum, L.allNum, L.dayOfWeek, L.startTime, L.endTime, S.score\r\n" + 
					"FROM Lecture L\r\n" + 
					"LEFT JOIN Subject S ON L.subjectCode = S.subjectCode\r\n" + 
					"LEFT JOIN Professor P ON L.profCode = P.professorCode\r\n" + 
					"WHERE L.lectureCode = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, p_lcode);
			// ���� ����
			ResultSet rs = pstmt.executeQuery();
			// ���� ��� ó��(rs)...
			
			if(!rs.next())
				return null;
			
			lectureDetail = new LectureDetail(
				rs.getInt("lectureCode"),  //�й��ڵ�
				rs.getString("subjectName"),  //�����
				rs.getString("profName"),  //�����̸�
				rs.getInt("registerTerm"),  //����б�
				rs.getInt("applyNum"),  //��û�ο�
				rs.getInt("allNum"),  //��ü�ο�
				OurTimes.intToDayOfWeek(rs.getInt("dayOfWeek")),  //���ǿ���
				OurTimes.sqlTimeToLocalTime(rs.getTime("startTime")),  //���ǽ��۽ð�
				OurTimes.sqlTimeToLocalTime(rs.getTime("endTime")),  //��������ð�
				rs.getDouble("score")  //����
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
	 * �й��߰�. 
	 * [�Է�����]
	 * �а��ڵ�� ������Ϲ�ȣ��, �а��ڵ�� �����ϴ� ���̾�� ��.
	 * ����б�� "���� ����� �̷��б�" ����(>=)���� �Ѵ�.
	 * ��ü�ο��� ������ ���� �ȿ� �־�� �ϸ�, OurSchoolPolicy Ŭ������ ����ٶ�.
	 * ���ǿ����� �����ϰ� �ݿ��� ���̿��� ��.
	 * ���ǽ��۽ð��� ��������ð����� �ռ��� ��.
	 * @param p_subjcode �����ڵ�
	 * @param p_profcode ������Ϲ�ȣ
	 * @param p_depcode �а��ڵ�
	 * @param p_registerterm ����б�
	 * @param p_allnum ��ü�ο�
	 * @param p_dayofweek ���ǿ���
	 * @param p_starttime ���ǽ��۽ð�
	 * @param p_endtime ��������ð�
	 * @param p_sylText ���ǰ�ȹ������
	 * @return �й��߰����(enum)
	 * @throws SQLException DB����
	 * ! DAO���� �ʿ� (������� ��ȯ������ Ÿ�缺 �˻� �߰�)
	 */
	public AddLectureResult addLecture (
			int p_subjcode, int p_profcode, int p_depcode,
			int p_registerterm,
			int p_allnum,
			DayOfWeek p_dayofweek, LocalTime p_starttime, LocalTime p_endtime,
			String p_sylText
			) throws SQLException
	{
		// �Ķ������ Ÿ�缺 �˻�
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
		// ���ǰ�ȹ�����빰�� ������� �� �ִ�.
		if(p_sylText == null)
			p_sylText = "";
		
		try {
			// ���ǰ�ȹ���� ����ϰ�, �� ��ȣ(Last Inserted ID)�� �����´�.
			int sylCode = syllabusDAO.addSyllabus(p_sylText);
			
			// DB ����
			conn = getConnection();
			// ������ ����
			String sql = "INSERT INTO Lecture (registerTerm, applyNum, allNum, dayofweek, startTime, endTime, subjectCode, departmentCode, profCode, syllabusCode)\r\n" + 
					"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, p_registerterm);
			pstmt.setInt(2, 0);  // applyNum(�����ο�)
			pstmt.setInt(3, p_allnum);
			pstmt.setInt(4, OurTimes.dayOfWeekToInt(p_dayofweek) );
			pstmt.setTime(5, OurTimes.LocalTimeTosqlTime(p_starttime) );
			pstmt.setTime(6, OurTimes.LocalTimeTosqlTime(p_endtime) );
			pstmt.setInt(7, p_subjcode);
			pstmt.setInt(8, p_depcode);
			pstmt.setInt(9, p_profcode);
			pstmt.setInt(10, sylCode);
			
			// ���� ����
			int result = pstmt.executeUpdate();
			// ���� ��� ó��...
			
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
