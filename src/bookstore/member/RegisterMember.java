package bookstore.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import bookstore.util.DbConnect;

public class RegisterMember {
	static DbConnect db;

	public static void createData(DbConnect data) throws SQLException {
		db = data;
		Scanner scanner = new Scanner(System.in);
		System.out.print("이름: ");
		String name = scanner.next();
		String id;
		boolean idExists;

		do {
			System.out.print("아이디: ");
			id = scanner.next();
			idExists = checkIfIdExists(db, id); // Create 클래스 내에 메소드이므로 클래스명 없이 직접 호출
			if (idExists) {
				System.out.println("이미 존재하는 아이디입니다. 다른 아이디를 입력하세요.");
			}
		} while (idExists);

		System.out.print("비밀번호: ");
		String password = scanner.next();
		int age;
		while (true) {
			try {
				System.out.print("나이: ");
				age = Integer.parseInt(scanner.next());
				break; // 입력이 올바른 경우 루프 종료
			} catch (NumberFormatException e) {
				System.out.println("올바른 형식이 아닙니다. 나이는 숫자로 입력하세요.");
			}
		}

		System.out.print("성별: ");
		String sex = scanner.next();
		System.out.print("이메일: ");
		String email = scanner.next();
		int money = 0;
		
		String sql = "INSERT INTO member (member_no, name, id, pwd, ages, sex, email, money) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
			int lastMemberNo = getLastMemberNo(db); // Create 클래스 내에 메소드이므로 클래스명 없이 직접 호출
			int num = lastMemberNo + 1;

			pstmt.setInt(1, num);
			pstmt.setString(2, name);
			pstmt.setString(3, id);
			pstmt.setString(4, password);
			pstmt.setInt(5, age);
			pstmt.setString(6, sex);
			pstmt.setString(7, email);
			pstmt.setInt(8, money);

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0)
				System.out.println("회원가입이 되었습니다.");
			else
				System.out.println("회원가입에 실패했습니다.");
		}
	}

	static boolean checkIfIdExists(DbConnect db, String id) throws SQLException {
		String sql = "SELECT COUNT(*) AS count FROM member WHERE id = ?";
		Map<Integer, String> map = new HashMap<>();
		map.put(1, id);
		try (ResultSet rs = db.getSqlResult(sql, map)) {
			if (rs.next()) {
				int count = rs.getInt("count");
				return count > 0;
			}
		}
		return false;
	}

	static int getLastMemberNo(DbConnect db) throws SQLException {
		String sql = "SELECT MAX(member_no) AS lastMemberNo FROM member"; // 수정
		try (ResultSet rs = db.getSqlResult(sql)) {
			if (rs.next()) {
				return rs.getInt("lastMemberNo");
			}
		}
		return 0;
	}
}
