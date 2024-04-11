package bookstore.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Login {
	public static boolean login(Connection conn, Scanner scanner) throws SQLException {
		System.out.print("아이디: ");
		String id = scanner.next();
		System.out.print("비밀번호: ");
		String password = scanner.next();

		String sql = "SELECT COUNT(*) AS count FROM member WHERE id = ? AND pwd = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, id);
			pstmt.setString(2, password);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					int count = rs.getInt("count");
					if (count == 1) {
						return true;
					}
				}
			}
		}
		return false;
	}

	
}
