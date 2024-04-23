package bookstore.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bookstore.util.DbConnect;

public class IdCheck {
	public static void readData(DbConnect db, String userId) throws SQLException {
		String sql = "SELECT * FROM member WHERE id = ?";
		try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
			pstmt.setString(1, userId);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					System.out.println("이름: " + rs.getString("name") + ", 아이디: " + rs.getString("id") + ", 나이: "
							+ rs.getInt("ages") + // 수정
							", 성별: " + rs.getString("sex") + ", 이메일: " + rs.getString("email") + ", 보유 잔액: "
							+ rs.getInt("money")); // 총 구매내역 또는 총액 을 출력할 예정
				} else {
					System.out.println("해당하는 사용자의 정보가 없습니다.");
				}
			}
		}
	}

	static boolean checkIfIdExists(DbConnect db, String id) throws SQLException {
		String sql = "SELECT COUNT(*) AS count FROM member WHERE id = ?";
		try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
			pstmt.setString(1, id);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					int count = rs.getInt("count");
					return count > 0;
				}
			}
		}
		return false;
	}

	static int getLastMemberNo(Connection conn) throws SQLException {
		String sql = "SELECT MAX(member_no) AS lastMemberNo FROM member"; // 수정
		try (PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
			if (rs.next()) {
				return rs.getInt("lastMemberNo");
			}
		}
		return 0;
	}

}
