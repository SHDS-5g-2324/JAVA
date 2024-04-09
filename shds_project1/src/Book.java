import java.sql.*;

public class Book{

    static final String JDBC_URL = "jdbc:oracle:thin:@211.178.201.98:1521:xe";
    static final String USER = "shds2";
    static final String PASSWORD = "shds1234";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            System.out.println("DB 연결 성공!");

            readData(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void readData(Connection conn) throws SQLException {
        String sql = "SELECT * FROM BOOK WHERE AMOUNT > 0";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("책 번호: " + rs.getString("BOOK_ID") +
                        ", 제목: " + rs.getString("SUBJECT") +
                        ", 가격: " + rs.getDouble("PRICE") +
                        ", 링크: " + rs.getString("LINK") +
                        ", 저자: " + rs.getString("AUTHOR") +
                        ", 출판사: " + rs.getString("PUBLISHER") +
                        ", 좋아요 수: " + rs.getInt("LIKE_COUNT") +
                        ", 판매 수: " + rs.getInt("SELL_COUNT") +
                        ", 재고 수량: " + rs.getInt("AMOUNT"));
            }
        }
    }
}
