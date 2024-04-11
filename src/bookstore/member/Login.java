package bookstore.member;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import bookstore.Main;
import bookstore.util.DbConnect;

public class Login {
	/*로그인 성공 시 아이디 리턴, 실패 시 "" 리턴*/
	public static String login(DbConnect db) throws SQLException {
		Scanner scanner = new Scanner(System.in);
		System.out.print("아이디: ");
		String id = scanner.next();
		System.out.print("비밀번호: ");
		String password = scanner.next();

		String sql = "SELECT COUNT(*) AS count FROM member WHERE id = ? AND pwd = ?";
		try {
			Map<Integer, String> map = new HashMap<>();
			map.put(1, id);
			map.put(2, password);
			try (ResultSet rs = db.getSqlResult(sql, map);) {
				if (rs.next()) {
					int count = rs.getInt("count");
					if (count == 1) {
						Main.loggedInUserId = id;
						return id;
					}
				}
			}
		} finally {
		}
		return "";
	}
}
