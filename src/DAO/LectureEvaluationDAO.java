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
	// �����ͺ��̽� ������ ����
	private static PreparedStatement pstmt;
	private static Connection conn;
		
	public LectureEvaluationDAO() {
		super();
	}
	
	/** �����򰡿��θ���Ʈ ��ȸ
	 * @param p_list ������ȣ����Ʈ -> List<Integer>
	 * @return �����򰡿��θ���Ʈ(boolean) -> HashMap<Integer, Boolean>
	 * ! DB���� ������ attendanceNum�� ���ڷ� ���� p_list�� attendanceNum�� ������ �� ?
	 * ! // ������� �� ������ ??
	 * ! DAO HashMap ������� ���� �ؾ���*/
	public HashMap<Integer, Boolean> getLectureEvaluationExistList(List<Integer> p_list) {
		List<LectureEvaluation> temp = new ArrayList<LectureEvaluation>();
		
		try {
			String SQL = "SELECT * FROM LectureEvaluation"
					+ " WHERE attendanceNum = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			int i = 0;
			
			// �����򰡸���Ʈ ��ȸ�ϱ�
			while(i < p_list.size()) {
				pstmt.setInt(1, p_list.get(i));
				ResultSet rs = pstmt.executeQuery();
				int rsAttendanceNum = rs.getInt("attendanceNum"); // ������ȣ ��������
				String rsText = rs.getString("LectureEvaluationText"); // �����򰡳��� ��������
				
				LectureEvaluation lectureEvaluation = new LectureEvaluation(
						rsText,
						rsAttendanceNum
						);
						
				temp.add(lectureEvaluation);
				pstmt.clearParameters(); // set �� ���� �ʱ�ȭ
				i++;
			}	
		}catch(Exception e){
	        e.printStackTrace();
	    }finally{
	    	 if(pstmt != null) try{pstmt.close();}catch(SQLException sqle){}
		      if(conn != null) try{conn.close();}catch(SQLException sqle){}
	    }

		HashMap<Integer, Boolean> hashMap = new HashMap<>(); // ����
		// �Ȱ��� ���̸�ŭ false�� �ʱ�ȭ
		for(int i = 0; i < p_list.size(); i++) {
			hashMap.put(p_list.get(i), false);
		}
		
		// �򰡸���Ʈ ��
		for(int i = 0; i < temp.size(); i++) {
			// DB���� ������ attendanceNum�� ���ڷ� ���� p_list�� attendanceNum�� ������
			if(temp.get(i).getAttendanceNum() == p_list.get(i)) {
				if(hashMap.replace(p_list.get(i), true) == null) { // �������
					
				}
			}
		}
		temp.clear(); // ����
		return hashMap;
	}
	
	/** �����򰡿��� ��ȸ
	 * @param p_attendancenum ������ȣ
	 * @return �����򰡿��ΰ��(boolean)
	 * ! DAO �˰��� ��� ��ȯ ���� �ʿ�*/
	public boolean isLectureEvaluationExist(int p_attendancenum) {
		
		try {
			String SQL = "SELECT * FROM LectureEvaluation"
					+ " WHERE attendanceNum = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_attendancenum);
			ResultSet rs = pstmt.executeQuery();
			
			// ��ȸ��� ������ 
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
	
	/** �������ۼ� 
	 * @param p_attendancenum ������ȣ
	 * @param p_text �����򰡳���
	 * @return �ۼ��������(boolean)
	 * ! DAO ��쿡 ���� ��� �߰� �ʿ�*/
	public boolean writeLectureEvaluation(int p_attendancenum, String p_text) {
		
		try {
			String SQL = "INSERT INTO LectureEvaluation (LectureEvaluationText, attendanceNum)"
					+ " VALUES (?, ?)";
			conn = getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, p_attendancenum);
			pstmt.setString(2, p_text);
			int result = pstmt.executeUpdate(); 
			
			// SQL ����
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
