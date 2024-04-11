package bookstore.member;

import java.sql.ResultSet;
import java.sql.SQLException;

import bookstore.util.DbConnect;

public class GetBalance {

	public static void readBalance(DbConnect db, String userId) throws SQLException {
		String balanceQuery = "SELECT money FROM member WHERE id = ?";
		int currentBalance = 0;

			try (ResultSet rs = db.getSqlResult(balanceQuery, 1, userId)) {
				if (rs.next()) {
					currentBalance = rs.getInt("money");
					System.out.println("현재 잔액: " + currentBalance + "원");
				} else {
					System.out.println("잔액 조회에 실패했습니다.");
				}
			
		}
	}
}
