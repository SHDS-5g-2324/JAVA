package bookstore;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Delete {
    public static void deleteData(Connection conn, Scanner scanner, String userId) throws SQLException {
        // 현재 로그인한 사용자의 아이디를 사용하여 삭제
        String sql = "DELETE FROM member WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("데이터가 성공적으로 삭제되었습니다.");
                // 삭제 후 메인 메뉴로 돌아가기
                return; // 메인 메뉴로 돌아가기 위해 메서드를 종료
            } else {
                System.out.println("데이터 삭제에 실패했습니다.");
            }
        }

        // 데이터 삭제에 실패한 경우에만 아래 코드가 실행됩니다.
        // 메인 메뉴로 돌아가기
        System.out.println("메인 메뉴로 돌아갑니다.");
    }
}