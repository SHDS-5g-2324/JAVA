package bookstore.book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Read {
	/*책 전체 리스트 읽기*/
	public static void bookList(Connection conn) throws SQLException {
		String sql = "SELECT * FROM Book ";
		String bookId = "";
		try (PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				bookId = rs.getString("book_id");
				System.out.println("[" + bookId + "] " + "제목: " + rs.getString("SUBJECT") + ", 가격: " + rs.getInt("PRICE") + ", 저자: "
						+ rs.getString("AUTHOR") + ", 출판사: " + rs.getString("PUBLISHER") + ", 좋아요 수: "
						+ rs.getInt("LIKE_COUNT") + ", 판매 수: " + rs.getInt("SELL_COUNT") + ", 수량: "
						+ rs.getInt("AMOUNT"));
			}
		}
	}
	
	/*판매량 기준 상위 10개 리스트 출력*/
	public static void top10List(Connection conn) throws SQLException {
		String sql = "SELECT * FROM (SELECT * FROM Book ORDER BY SELL_COUNT DESC) WHERE ROWNUM <= 10";

		try (PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
			int rank = 1;
			while (rs.next()) {
				System.out.println("순위: " + rank + ", 제목: " + rs.getString("SUBJECT") + ", 가격: " + rs.getInt("PRICE")
						+ ", 저자: " + rs.getString("AUTHOR") + ", 출판사: " + rs.getString("PUBLISHER"));
				rank++;
			}
		}
	}
	/*관심있는 책 목록 출력*/
	public static void displayFavoriteBooks(Connection conn, String loggedInUserId) throws SQLException {
		String sql = "SELECT * " + "FROM Like_book L " + "LEFT JOIN MEMBER M ON M.ID = L.ID "
				+ "JOIN book B ON B.book_id = L.book_id " + "WHERE L.id = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, loggedInUserId);
			ResultSet rs = pstmt.executeQuery();

			boolean hasFavorites = false;

			while (rs.next()) {
				hasFavorites = true;
				System.out.print("Book ID: " + rs.getString("book_id") + "  ");
				System.out.print("Title: " + rs.getString("subject") + "  ");
				System.out.print("Author: " + rs.getString("author") + "  ");
				System.out.print("Price: " + rs.getInt("price") + "  ");
				System.out.println();
			}

			if (!hasFavorites) {
				System.out.println("관심목록이 없습니다.");
			}
		}
	}
	
	/*구매한 책 목록 출력*/
	public static void executeQuery(Connection conn, String memberId) throws SQLException { // 구매내역 조회
		String sql = "SELECT * " + "FROM MEMBER M " + "LEFT JOIN PURCHASE P ON M.ID = P.ID "
				+ "LEFT JOIN BOOK B ON B.BOOK_ID = P.BOOK_ID " + "WHERE M.ID = ? AND P.STATE = '결제완료'";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, memberId);
			ResultSet rs = pstmt.executeQuery();

			boolean hasPurchaseHistory = false; // 구매 내역 유무를 확인하기 위한 플래그

			while (rs.next()) {
				hasPurchaseHistory = true; // 구매 내역이 하나라도 있으면 플래그를 true로 설정

				int purchaseNo = rs.getInt("purchase_no");
				if (rs.wasNull()) {
					System.out.println("구매 내역이 없습니다.");
					break;
				}
				System.out.print("Member No: " + rs.getInt("member_no") + "  ");
				System.out.print("ID: " + rs.getString("id") + "  ");
				System.out.print("Name: " + rs.getString("name") + "  ");
				System.out.print("Money: " + rs.getInt("money") + "  ");

				System.out.print("Purchase No: " + purchaseNo + "  ");
				System.out.print("Book ID: " + rs.getString("book_id") + "  ");
				System.out.print("State: " + rs.getString("state") + "  ");

				System.out.print("Title: " + rs.getString("subject") + "  ");
				System.out.print("Price: " + rs.getInt("price") + "  ");
				System.out.println("Author: " + rs.getString("author") + "  ");
			}

			if (!hasPurchaseHistory) { // 구매 내역이 없는 경우
				System.out.println("구매 내역이 없습니다.");
			}
		}
	}

	public static void cartDisplay(Connection conn, String memberId) throws SQLException { // 구매내역 조회
		String sql = "SELECT * " + "FROM MEMBER M " + "LEFT JOIN PURCHASE P ON M.ID = P.ID "
				+ "LEFT JOIN BOOK B ON B.BOOK_ID = P.BOOK_ID " + "WHERE M.ID = ? AND P.STATE = '장바구니'";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, memberId);
			ResultSet rs = pstmt.executeQuery();

			boolean hasPurchaseHistory = false; // 구매 내역 유무를 확인하기 위한 플래그

			while (rs.next()) {
				hasPurchaseHistory = true; // 구매 내역이 하나라도 있으면 플래그를 true로 설정

				int purchaseNo = rs.getInt("purchase_no");
				if (rs.wasNull()) {
					System.out.println("장바구니가 없습니다.");
					break;
				}
				System.out.print("Member No: " + rs.getInt("member_no") + "  ");
				System.out.print("ID: " + rs.getString("id") + "  ");
				System.out.print("Name: " + rs.getString("name") + "  ");
				System.out.print("Money: " + rs.getInt("money") + "  ");

				System.out.print("Purchase No: " + purchaseNo + "  ");
				System.out.print("Book ID: " + rs.getString("book_id") + "  ");
				System.out.print("State: " + rs.getString("state") + "  ");

				System.out.print("Title: " + rs.getString("subject") + "  ");
				System.out.print("Price: " + rs.getInt("price") + "  ");
				System.out.println("Author: " + rs.getString("author") + "  ");
			}

			if (!hasPurchaseHistory) { // 구매 내역이 없는 경우
				System.out.println("장바구니가 없습니다.");
			}
		}
	}
}
