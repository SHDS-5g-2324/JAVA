import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetBalance {

	static void readBalance(Connection conn, String userId) throws SQLException {
		String balanceQuery = "SELECT money FROM member WHERE id = ?";
		int currentBalance = 0;

		try (PreparedStatement balanceStmt = conn.prepareStatement(balanceQuery)) {
			balanceStmt.setString(1, userId);
			try (ResultSet rs = balanceStmt.executeQuery()) {
				if (rs.next()) {
					currentBalance = rs.getInt("money");
					System.out.println("현재 잔액: " + currentBalance + "원");
				} else {
					System.out.println("잔액 조회에 실패했습니다.");
				}
			}
		}
	}
}
