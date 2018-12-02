package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAOBase {
	
	// �̷��� ����Ŭ���� DAOBase�� �����θ�, ���� DAO���Լ� ��Ÿ���� ������ ������ �� �ϳ��� ���� �� �ִ�. 
	
	private static String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/SE02?autoReconnect=true"; 
	private static String dbId = "SE02_11";
	private static String dbPwd = "2018";
	
	public DAOBase ()
	{
		// ���� DB������ ���� �ʴ´�. DB������ �� ���۷��̼ǿ��� �Ѵ�.
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
	}
	
	/** 
	 * ������ DB������ ��� ������ �ܼ��� �̰��� ȣ������.
	 * try������ ���δ� �͵� ���� ����
	 */
	protected Connection getConnection() throws SQLException
	{
		return DriverManager.getConnection(jdbcUrl, dbId,  dbPwd);
	}
}
