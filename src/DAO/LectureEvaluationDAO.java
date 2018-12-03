package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class LectureEvaluationDAO extends DAOBase {
	// 데이터베이스 접근을 위해
	private static PreparedStatement pstmt;
	private static Connection conn;
		
	public LectureEvaluationDAO() {
		super();
	}
}
