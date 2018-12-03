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
	
	// �л��� �а� ���縦 ����
	private StudentDAO studentDAO;
	
	// �����ͺ��̽� ������ ����
	private PreparedStatement pstmt;
	private Connection conn;
	
	// �ܼ��� ����� ���ָ� ��.
	public LectureDAO ()
	{
		super();
		
		studentDAO = new StudentDAO();
	}
	
	/**
	 * ������û���ɸ���Ʈ��ȸ
	 * @param p_sid �й�
	 * @return �йݻ���������Ʈ(�й��ڵ�, �����, �����̸�, ����б�, ��û�ο�, ��ü�ο�, ���ǿ���, ���ǽ��۽ð�, ��������ð�, ����).
	 * �������� ���� �ʴ� ��� null
	 * ! DAO���� ���� �ʿ�
	 */
	public List<LectureDetail> getApplyLectureList(int p_sid)
	{
		boolean isTimeOff = studentDAO.getTimeOffBySID(p_sid);
		
		if( isTimeOff && OurTimes.isNowAbleToAddAttendance() )
		{
			int dcode = DepartmentDAO.getDCodeBySID(p_sid);  //�ش��а�
			int currentApplyTerm = OurTimes.closestFutureTerm();  //��û���� �ϴ� �й��� ����б�� ������ �ٰ��� �б��.
			
			try
			{
				conn = getConnection();
				// �ش��б�, �ش�й�
				String sql = "SELECT L.lectureCode, S.subjectName, P.name, L.registerTerm, L.applyNum, L.allNum, L.dayOfWeek, L.startTime, L.endTime, S.score" + 
						"FROM Lecture L WHERE L.registerTerm = ? AND L.departmentCode = ?" + 
						"LEFT JOIN Subject S ON L.subjectCode = S.subjectCode" + 
						"LEFT JOIN Professor P ON L.profCode = P.professorCode";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, currentApplyTerm);
				pstmt.setInt(2, dcode);
				ResultSet rs = pstmt.executeQuery();
				
				// ��ȸ�Ͽ� ���� ����Ʈ�� ����
				List<LectureDetail> lectureDetailList = new ArrayList<LectureDetail>();
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
							rs.getDouble("score")
					);
					lectureDetailList.add(ld);
				}
				
				return lectureDetailList;
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
	
	/**
	 * ����˻�
	 * @param p_id
	 * @param p_subjectname
	 * @return ������û���ɸ���Ʈ �� �־��� ���� �ش��ϴ� ��
	 * ! DAO���� ���� �ʿ�, 
	 */
	public List<LectureDetail> getLectureByName(int id, String p_subjectname)
	{
		
	}
}
