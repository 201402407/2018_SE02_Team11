package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAOBase {
	
	// 이렇게 슈퍼클래스 DAOBase를 만들어두면, 여러 DAO에게서 나타나는 공통의 동작을 딱 하나로 줄일 수 있다. 
	
	private static String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/SE02?autoReconnect=true"; 
	private static String dbId = "SE02_11";
	private static String dbPwd = "2018";
	
	public DAOBase ()
	{
		// 아직 DB접속을 하지 않는다. DB접속은 각 오퍼레이션에서 한다.
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
	}
	
	/** 
	 * 앞으로 DB연결을 얻고 싶으면 단순히 이것을 호출하자.
	 * try문으로 감싸는 것도 잊지 말자
	 */
	protected Connection getConnection() throws SQLException
	{
		return DriverManager.getConnection(jdbcUrl, dbId,  dbPwd);
	}
}
