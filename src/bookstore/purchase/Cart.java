package bookstore.purchase;

import java.sql.ResultSet;
import java.sql.SQLException;

import bookstore.util.DbConnect;

public class Cart {
	public static void cartDisplay(DbConnect db, String memberId) throws SQLException { // 구매내역 조회
		String sql = "SELECT * " + "FROM MEMBER M " + "LEFT JOIN PURCHASE P ON M.ID = P.ID "
				+ "LEFT JOIN BOOK B ON B.BOOK_ID = P.BOOK_ID " + "WHERE M.ID = ? AND P.STATE = '장바구니'";

		try (ResultSet rs = db.getSqlResult(sql, 1, memberId)) {
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
