package bookstore.like;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import bookstore.Main;
import bookstore.util.DbConnect;

public class FavoriteAdd {
	/* 전달받은 bookid 가 있는 경우 */
	public static void addFavoriteBook(DbConnect db, String bookId) throws SQLException {
		String userId = Main.loggedInUserId; // 현재 로그인한 사용자의 ID 가져오기
		Connection conn = db.getConnection();
		// 이미 관심 책 목록에 있는지 확인
		String checkSql = "SELECT COUNT(*) AS count FROM Like_book WHERE id = ? AND book_id = ?";
		
		try (PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
			pstmt.setString(1, userId);
			pstmt.setString(2, bookId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					int count = rs.getInt("count");
					if (count > 0) {
						System.out.println("이미 관심 책으로 추가되어 있습니다.");
						return;
					}
				}
			}
		}

		// 관심 책 추가
		String insertSql = "INSERT INTO Like_book (id, book_id) VALUES (?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
			pstmt.setString(1, userId);
			pstmt.setString(2, bookId);
			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("관심 책으로 추가되었습니다.");
				// 관심 책 추가에 성공한 경우 해당 책의 LIKE_COUNT를 1 증가시키는 쿼리 실행
				String updateLikeCountSql = "UPDATE Book SET LIKE_COUNT = LIKE_COUNT + 1 WHERE BOOK_ID = ?";
				try (PreparedStatement updateStmt = conn.prepareStatement(updateLikeCountSql)) {
					updateStmt.setString(1, bookId);
					updateStmt.executeUpdate();
				}
			} else {
				System.out.println("관심 책 추가에 실패했습니다.");
			}
		}
	}

	/*book id 없이 가져온 경우*/
	public static void addFavoriteBook(DbConnect db) throws SQLException {
		String userId = Main.loggedInUserId; // 현재 로그인한 사용자의 ID 가져오기
		System.out.println("관심 등록할 책의 ID를 입력해주세요.");
		Scanner scanner = new Scanner(System.in);
		Connection conn = db.getConnection();
		String bookId = scanner.nextLine();
		// book 테이블에 존재하는 book_id인지 확인
		String checkSql = "SELECT COUNT(*) AS count FROM book WHERE book_id = ?";
		
		try (PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
			pstmt.setString(1, bookId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					int count = rs.getInt("count");
					if (count == 0) {
						System.out.println("존재하지 않는 BOOK_ID입니다.");
						return;
					}
				}
			}
		}

		
		// 이미 관심 책 목록에 있는지 확인
		checkSql = "SELECT COUNT(*) AS count FROM like_book WHERE id = ? AND book_id = ?";
		PreparedStatement pstmtCheck = null;
		ResultSet rs = null;
		try {
			pstmtCheck = conn.prepareStatement(checkSql);
			pstmtCheck.setString(1, userId);
			pstmtCheck.setString(2, bookId);
			rs = pstmtCheck.executeQuery();
			if (rs.next()) {
				int count = rs.getInt("count");
				if (count > 0) {
					System.out.println("이미 관심 책으로 추가되어 있습니다.");
					return; // 이미 추가된 경우 메소드 종료
				}
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmtCheck != null) {
				pstmtCheck.close();
			}
		}

		// 관심 책 추가
		String insertSql = "INSERT INTO Like_book (id, book_id) VALUES (?, ?)";
		PreparedStatement pstmtInsert = null;
		try {
			pstmtInsert = conn.prepareStatement(insertSql);
			pstmtInsert.setString(1, userId);
			pstmtInsert.setString(2, bookId);
			int affectedRows = pstmtInsert.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("관심 책으로 추가되었습니다.");
				// 관심 책 추가에 성공한 경우 해당 책의 LIKE_COUNT를 1 증가시키는 쿼리 실행
				String updateLikeCountSql = "UPDATE Book SET LIKE_COUNT = LIKE_COUNT + 1 WHERE BOOK_ID = ?";
				PreparedStatement pstmtUpdate = null;
				try {
					pstmtUpdate = conn.prepareStatement(updateLikeCountSql);
					pstmtUpdate.setString(1, bookId);
					pstmtUpdate.executeUpdate();
				} finally {
					if (pstmtUpdate != null) {
						pstmtUpdate.close();
					}
				}
				return;

			} else {
				System.out.println("관심 책 추가에 실패했습니다.");
				return;
			}
		} finally {
			if (pstmtInsert != null) {
				pstmtInsert.close();
			}
		}
	}

}
