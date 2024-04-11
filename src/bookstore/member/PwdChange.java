package bookstore.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class PwdChange {
	public static void updateData(Connection conn, Scanner scanner, String userId) throws SQLException {
        System.out.print("새로운 비밀번호 입력:");
        String newPassword = scanner.next();

        String sql = "UPDATE member SET pwd = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, userId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0)
                System.out.println("비밀번호가 성공적으로 수정되었습니다.");
            else
                System.out.println("비밀번호 수정에 실패했습니다.");
        }
    }
}
