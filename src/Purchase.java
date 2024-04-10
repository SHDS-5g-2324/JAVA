import java.sql.*;

public class Purchase {

    static final String JDBC_URL = "jdbc:oracle:thin:@211.178.201.98:1521:xe";
    static final String USER = "shds2";
    static final String PASSWORD = "shds1234";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            System.out.println("DB 연결 성공!");

            purchaseData(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void purchaseData(Connection conn) throws SQLException {
        String sql = "SELECT * FROM purchase";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("구매 번호: " + rs.getInt("purchase_no") +
                        ", 책 번호: " + rs.getString("book_id") +
                        ", 사용자 ID: " + rs.getString("id") +
                        ", 상태: " + rs.getString("state"));
            }
        }
    }
}
