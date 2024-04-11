package src.bookstore.book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Read {
	/*책 전체 리스트 읽기*/
	static void bookList(Connection conn) throws SQLException {
		String sql = "SELECT * FROM Book ";

		try (PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				System.out.println("제목: " + rs.getString("SUBJECT") + ", 가격: " + rs.getInt("PRICE") + ", 저자: "
						+ rs.getString("AUTHOR") + ", 출판사: " + rs.getString("PUBLISHER") + ", 좋아요 수: "
						+ rs.getInt("LIKE_COUNT") + ", 판매 수: " + rs.getInt("SELL_COUNT") + ", 수량: "
						+ rs.getInt("AMOUNT"));
			}
		}
	}
	
	/*판매량 기준 상위 10개 리스트 출력*/
	static void top10List(Connection conn) throws SQLException {
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
}
