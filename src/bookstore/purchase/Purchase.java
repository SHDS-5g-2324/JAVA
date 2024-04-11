package bookstore.purchase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import bookstore.Main;
import bookstore.util.DbConnect;

public class Purchase {

	// 구매하기 창에서 북 리스트를 보여주고, 선택하는 경우(bookId가 없을 때)
	public static void purchaseBook(DbConnect db) throws SQLException {
		String userId = Main.loggedInUserId; // 현재 로그인한 사용자의 ID 가져오기
		System.out.println("--------------------------");
		System.out.println("(돌아가려면 0을 입력하세요.)원하는 책의 ID를 입력하세요.");
		Scanner scanner = new Scanner(System.in);
		String bookId = scanner.nextLine();
		if ("0".equals(bookId))
			return; // 0 입력 받으면 종료
		String bookInfoSql = "SELECT * FROM Book WHERE BOOK_ID = ?";
		// 책 정보 조회
		try (ResultSet rs = db.getSqlResult(bookInfoSql, 1, bookId)) {
			if (rs.next()) {
				int bookPrice = rs.getInt("PRICE");
				System.out.println("책 제목: " + rs.getString("SUBJECT"));
				System.out.println("책 가격: " + bookPrice);

				// 구매 옵션 선택
				System.out.println("1. 바로 결제하기  2. 장바구니 담기");
				System.out.print("구매 옵션을 선택하세요: ");
				int purchaseOption = scanner.nextInt();

				if (purchaseOption == 1) {
					// 바로 결제하기: 보유 잔액에서 즉시 결제
					processImmediatePayment(db.getConnection(), userId, bookId, bookPrice);
					return;
				} else if (purchaseOption == 2) {
					// 장바구니: 결제 대기 상태로 이동
					processDelayedPayment(db.getConnection(), userId, bookId, bookPrice);
					return;
				} else {
					System.out.println("올바른 옵션을 선택하세요.");
				}

			}
		}
	}

	// 책 조회 후 구매하기 버튼 클릭 시 (bookId를 가지고 오는 경우)
	public static void purchaseBook(DbConnect db, String bookId) throws SQLException {
		String userId = Main.loggedInUserId; // 현재 로그인한 사용자의 ID 가져오기
		String bookInfoSql = "SELECT * FROM Book WHERE BOOK_ID = ?";
		System.out.println("--------------------------");
		Scanner scanner = new Scanner(System.in);
		// 책 정보 조회
		try (ResultSet rs = db.getSqlResult(bookInfoSql, 1, bookId)) {
			// 결과가 없는 경우 처리
			if (!rs.next()) {
				System.out.println("검색된 책이 없습니다.");
				return; // 메소드 종료
			}

			// 결과가 있는 경우 처리
			int bookPrice = rs.getInt("PRICE");
			System.out.println("책 제목: " + rs.getString("SUBJECT"));
			System.out.println("책 가격: " + bookPrice);

			// 구매 옵션 선택
			System.out.println("--------------------------");
			System.out.println("1. 바로 결제하기  2. 장바구니 담기");
			System.out.print("구매 옵션을 선택하세요: ");
			String purchaseOption = scanner.next();

			if (purchaseOption.equals("1")) {
				// 바로 결제하기: 보유 잔액에서 즉시 결제
				processImmediatePayment(db.getConnection(), userId, bookId, bookPrice);
				return;
			} else if (purchaseOption.equals("2")) {
				// 나중에 결제하기: 장바구니 상태로 이동
				processDelayedPayment(db.getConnection(), userId, bookId, bookPrice);
				return;
			} else {
				System.out.println("올바른 옵션을 선택하세요.");
			}

		}
	}

	static void processImmediatePayment(Connection conn, String userId, String bookId, int bookPrice)
			throws SQLException {

		int bookAmount = getBookAmount(conn, bookId);
		if (bookAmount <= 0) {
			System.out.println("책의 재고가 부족하여 결제할 수 없습니다.");
			return;
		}

		// 보유 잔액 확인
		int currentBalance = getCurrentBalance(conn, userId); // readbalance 부분이 있지만 void 형태이기에 int로 하나더만들었음
		if (currentBalance < bookPrice) {
			System.out.println("잔액이 부족하여 결제할 수 없습니다.");
			return;
		} else {
			System.out.println("결제 후 잔액 : " + (currentBalance - bookPrice) + "원 입니다.");
		}

		// 결제 진행
		String updateBalanceSql = "UPDATE Member SET MONEY = MONEY - ? WHERE ID = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(updateBalanceSql)) {
			pstmt.setInt(1, bookPrice);
			pstmt.setString(2, userId);
			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				// 결제 성공 시 구매 내역 추가
				addPurchase(conn, userId, bookId, "결제완료");
				System.out.println("결제가 완료되었습니다.");
				String decreaseAmountSql = "UPDATE Book SET AMOUNT = AMOUNT - 1 WHERE BOOK_ID = ?";
				try (PreparedStatement updateStmt = conn.prepareStatement(decreaseAmountSql)) {
					updateStmt.setString(1, bookId);
					updateStmt.executeUpdate();
				}
				return;
			} else {
				System.out.println("결제에 실패했습니다.");
				return;
			}
		}
	}

	static int getBookAmount(Connection conn, String bookId) throws SQLException {
		String amountQuery = "SELECT AMOUNT FROM Book WHERE BOOK_ID = ?";
		try (PreparedStatement amountStmt = conn.prepareStatement(amountQuery)) {
			amountStmt.setString(1, bookId);
			try (ResultSet rs = amountStmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("AMOUNT");
				} else {
					System.out.println("책의 재고를 조회하는데 실패했습니다.");
				}
			}
		}
		return 0;
	}

	static void processDelayedPayment(Connection conn, String userId, String bookId, int bookPrice)
			throws SQLException {
		// 결제 대기 상태로 이동
		addPurchase(conn, userId, bookId, "장바구니");
		System.out.println("장바구니에 담겼습니다.");
	}

	static int getCurrentBalance(Connection conn, String userId) throws SQLException {
		String balanceQuery = "SELECT MONEY FROM Member WHERE ID = ?";
		int currentBalance = 0;

		try (PreparedStatement balanceStmt = conn.prepareStatement(balanceQuery)) {
			balanceStmt.setString(1, userId);
			try (ResultSet rs = balanceStmt.executeQuery()) {
				if (rs.next()) {
					currentBalance = rs.getInt("MONEY");
					System.out.println("현재 보유 잔액 : " + currentBalance + "원 입니다.");
				} else {
					System.out.println("잔액 조회에 실패했습니다.");
				}
			}
		}
		return currentBalance;
	}

	static void addPurchase(Connection conn, String userId, String bookId, String state) throws SQLException {
		String insertSql = "INSERT INTO Purchase (PURCHASE_NO, BOOK_ID, ID, STATE) VALUES (?, ?, ?, ?)";
		try (PreparedStatement countStmt = conn.prepareStatement("SELECT COUNT(*) AS count FROM PURCHASE");
				ResultSet countRs = countStmt.executeQuery();
				PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
			int count = 0;
			if (countRs.next()) {
				count = countRs.getInt("count") + 1;
			}
			pstmt.setInt(1, count);
			pstmt.setString(2, bookId);
			pstmt.setString(3, userId);
			pstmt.setString(4, state);
			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("상태가 업데이트 되었습니다.");
			} else {
				System.out.println("구매 내역 추가에 실패했습니다.");
			}
		}
	}
}
