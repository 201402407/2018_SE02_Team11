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
	private static PreparedStatement pstmt;
	private static Connection conn;
	
	// �ܼ��� ����� ���ָ� ��.
	public LectureDAO ()
	{
		super();
		
		studentDAO = new StudentDAO();
	}
	
	/**
	 * ������û���ɸ���Ʈ��ȸ
	 * @param p_sid �й�
	 * @return �йݻ���������Ʈ(�й��ڵ�, �����, �����̸�, ����б�, ��û�ο�, ��ü�ο�, ���ǿ���, ���ǽ��۽ð�, ��������ð�, ����)
	 * @return null �������� ���� �ʴ� ���.
	 */
	public List<LectureDetail> getApplyLectureLits(int p_sid)
	{
		boolean isTimeOff = studentDAO.getTimeOffBySID(p_sid);
		
		if( isTimeOff && OurTimes.isNowAbleToAddAttendance() )
		{
			int dcode = DepartmentDAO.getDCodeBySID(p_sid);  //�ش��а�
			int currentTerm = OurTimes.currentTerm();  //�����б�
			
			try
			{
				conn = getConnection();
				String sql = "SELECT L.lectureCode, S.subjectName, P.name, L.registerTerm, L.applyNum, L.allNum, L.dayOfWeek, L.startTime, L.endTime, S.score" + 
						"FROM Lecture L WHERE L.registerTerm = ?" + 
						"LEFT JOIN Subject S ON L.subjectCode = S.subjectCode" + 
						"LEFT JOIN Professor P ON L.profCode = P.professorCode";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, currentTerm); //�����б�
				ResultSet rs = pstmt.executeQuery();
				
				// ��ȸ
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
							OurTimes.sqltimeToLocalTime(rs.getTime("startTime")),  //���ǽ��۽ð�
							OurTimes.sqltimeToLocalTime(rs.getTime("endTime")),  //��������ð�
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
