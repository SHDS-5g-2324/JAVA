package bookstore.book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import bookstore.like.FavoriteAdd;
import bookstore.purchase.Purchase;
import bookstore.util.DbConnect;

public class Search {
	/*책 검색*/
	public static void searchBook(DbConnect db) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection conn = db.getConnection();
		do {
			System.out.println("--------------------------");
			System.out.print("검색할 책의 제목을 입력하세요: ");
			Scanner scanner = new Scanner(System.in);
			String bookName = scanner.nextLine(); // 사용자로부터 입력 받은 책의 제목을 bookName 변수에 저장
			String bookId = "";
			String sql = "SELECT * FROM BOOK WHERE SUBJECT LIKE ?";
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%" + bookName + "%");
				rs = pstmt.executeQuery();

				boolean found = false; // 검색 결과를 표시하기 위한 플래그
				System.out.println("['" + bookName + "'이 포함된 검색 결과 ]");

				while (rs.next()) {
					found = true; // 검색 결과가 있음을 표시
					bookId = rs.getString("BOOK_ID");
					System.out.println("[" + bookId + "]" + "제목: " + rs.getString("SUBJECT")
							+ ", 가격: " + rs.getInt("PRICE") + ", 저자: " + rs.getString("AUTHOR") + ", 출판사: "
							+ rs.getString("PUBLISHER") + ", 좋아요 수: " + rs.getInt("LIKE_COUNT") + ", 수량: "
							+ rs.getInt("AMOUNT"));
				}

				if (!found) {
					System.out.println("해당하는 책이 없습니다.");
					continue; // 검색 결과가 없으면 다시 반복문으로 돌아감
				}

				// 책 구매 또는 관심책 추가 메뉴 제공
				System.out.println("--------------------------");
				System.out.println("1.책 구매하기 2.관심책 추가하기 3.이전 메뉴로 돌아가기");
				System.out.print("원하는 작업을 선택하세요: ");
				int num = scanner.nextInt();
				while (num != 3) {
					switch (num) {
						case 1:
							Purchase.purchaseBook(db, bookId);
							return;
						case 2:
							FavoriteAdd.addFavoriteBook(db, bookId);
							return;
						default:
							System.out.println("잘못된 선택입니다.");
							System.out.println("--------------------------");
							System.out.println("1.책 구매하기 2.관심책 추가하기 3.이전 메뉴로 돌아가기");
							num = scanner.nextInt();
					}
				}
			} finally {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			}

			// 사용자가 다시 검색을 할지 물어봄
			String again;
			do {
				System.out.println("--------------------------");
				System.out.print("책을 검색하시겠습니까? (1.예  2.아니요): ");
				again = scanner.next();
				if (!again.equals("1") && !again.equals("2")) {
					System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
				}
			} while (!again.equals("1") && !again.equals("2"));

			if (again.equals("2")) { // 아니라고 입력하면 메서드 종료
				return;
			}
		} while (true);
	}

}
