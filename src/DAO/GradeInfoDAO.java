package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class GradeInfoDAO extends DAOBase {

	// �����ͺ��̽� ������ ����
	private static PreparedStatement pstmt;
	private static Connection conn;
	
	public GradeInfoDAO() {
		super();
	}
}
