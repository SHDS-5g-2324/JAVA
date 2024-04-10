package bookstore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Update {
    public static void updateData(Connection conn, Scanner scanner, String userId) throws SQLException {
        System.out.print("새로운 비밀번호 입력:");  // 개인정보 수정은 비밀번호만 하는게 맞을까요 ? 조언좀
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

    public static void updateMoney(Connection conn, Scanner scanner, String userId) throws SQLException {
        System.out.print("추가할 금액 입력: ");
        int plusMoney = scanner.nextInt();

        String updateQuery = "UPDATE member SET money = money + ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setInt(1, plusMoney);
            pstmt.setString(2, userId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0)
                System.out.println("잔액이 " + plusMoney + "원 충전되었습니다.");
            else
                System.out.println("잔액 수정에 실패했습니다.");
        }
    }
}