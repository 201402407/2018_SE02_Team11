package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ChangeRecordDAO extends DAOBase {

	// �����ͺ��̽� ������ ����
	private static PreparedStatement pstmt;
	private static Connection conn;
	
	public ChangeRecordDAO() {
		super();
	}
}
