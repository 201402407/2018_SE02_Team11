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
		INVALID_TERM
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
	 * �������� ���� �ʴ� ��� null
	 * @throws SQLException DB����.
	 * ! DAO���� ���� �ʿ�
	 */
	public List<LectureDetail> getApplyLectureList(int p_sid) throws SQLException
	{
		// �������̸� �����ǿ� ���� �ʴ´�.
		boolean isTimeOff = studentDAO.getTimeOffBySID(p_sid);
		if( !(isTimeOff && OurTimes.isNowAbleToAddAttendance()) )
			return null;
		
		// ���� �����
		List<LectureDetail> lectureDetailList;

		// �л��� �ش��а��� ���Ѵ�
		int dcode = departmentDAO.getDCodeBySID(p_sid);
		// �����б�: ������ �б����� �ƴϳ�, ������û�Ⱓ���� "�����б�"�� ������ �ٰ��� �б� �� ���� ����� ���̴�.
		int currentApplyTerm = OurTimes.closestFutureTerm();
		
		try
		{
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
	 * ����˻�. �������� "������û����Ʈ��ȸ"�� ������.
	 * @param p_sid �й�
	 * @param p_subjectname �����
	 * @return ������û���ɸ���Ʈ �� �־��� ����� �ش��ϴ� ��
	 */
	public List<LectureDetail> getLectureByName(int p_sid, String p_subjectname)
	{
		// �������̸� �����ǿ� ���� �ʴ´�.
		boolean isTimeOff = studentDAO.getTimeOffBySID(p_sid);
		if( !(isTimeOff && OurTimes.isNowAbleToAddAttendance()) )
			return null;
		
		// ���� �����
		List<LectureDetail> lectureDetailList;
		
		//�ش��а�
		int dcode = departmentDAO.getDCodeBySID(p_sid);
		//������û�Ⱓ���� "�����б�"�� ������ �ٰ��� �б� �� ���� ����� ���̴�.
		int currentApplyTerm = OurTimes.closestFutureTerm();
		// https://stackoverflow.com/questions/8247970/using-like-wildcard-in-prepared-statement
		String subjectname_wildcarded = "%" + p_subjectname
				.replace("!", "!!")
				.replace("%", "!%")
				.replace("_", "!_")
				.replace("[", "![") + "%";
		
		try
		{
			// DB ����
			conn = getConnection();
			// ������ ����
			String sql = "SELECT L.lectureCode, S.subjectName, P.profName, L.registerTerm, L.applyNum, L.allNum, L.dayOfWeek, L.startTime, L.endTime, S.score\r\n" + 
					"FROM Lecture L\r\n" + 
					"LEFT JOIN Subject S ON L.subjectCode = S.subjectCode\r\n" + 
					"LEFT JOIN Professor P ON L.profCode = P.professorCode\r\n" + 
					"WHERE L.registerTerm = ? AND S.subjectName LIKE ? ESCAPE '!' AND L.departmentCode = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dcode);
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
	 * @return ���ڿ��� �� ���ǰ�ȹ�� ���빰. �й��߰� ��� ���ǰ�ȹ�� ���빰�� �Է����� �ʾ����� null.
	 * @throws SQLException DB����.
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
		String sylText;
		try {
			sylText = syllabusDAO.getTextBySYLCode(sylCode);
			if (sylText.isEmpty())
				sylText = null;
		}
		catch (SQLException sqle_sylText)
		{
			// SyllabusDAO.getTextBySYLCode���� DB������ �߻�.
			throw sqle_sylText;
		}
		
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
		if( isInvalidRegisterTerm(p_registerterm) )
			return AddLectureResult.INVALID_TERM;
		if( ! subjectDAO.isSubjectExistBySCode(p_subjcode) )
			return AddLectureResult.NOT_FOUND_SUBJCODE;
		if( ! professorDAO.IsProfessorByPCode(p_profcode) )
			return AddLectureResult.NOT_FOUND_PROFCODE;
		if( ! departmentDAO.isDepartmentExistByDCode(p_depcode) )
			return AddLectureResult.NOT_FOUND_DEPCODE;
		
		
		// ���ǰ�ȹ�����빰�� ������� �� �ִ�.
		if(p_sylText == null)
			p_sylText = "";
		
		
		
	}

	private boolean isInvalidRegisterTerm(int p_registerterm) {
		return false;
	}
}
