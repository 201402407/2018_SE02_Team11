package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ScholarshipDAO extends DAOBase {
	// �����ͺ��̽� ������ ����
	private static PreparedStatement pstmt;
	private static Connection conn;
	
	public ScholarshipDAO() {
		super();
	}
}
