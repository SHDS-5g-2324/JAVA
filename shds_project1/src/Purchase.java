import java.sql.*;

public class Purchase{

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
        String sql = "SELECT * FROM PURCHASE";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("책 번호: " + rs.getString("PURCHASE_ID") +
                        ", 제목: " + rs.getString("MEMBER_NO") +
                        ", 가격: " + rs.getDouble("BOOK_ID") +
                        ", 링크: " + rs.getString("STATE"))
                      ;
            }
        }
    }
}
