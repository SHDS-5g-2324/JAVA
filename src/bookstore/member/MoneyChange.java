package bookstore.member;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import bookstore.util.DbConnect;

public class MoneyChange {
	public static void updateMoney(DbConnect db, String userId) throws SQLException {
        System.out.print("추가할 금액 입력: ");
        Scanner scanner = new Scanner(System.in);
        int plusMoney = scanner.nextInt();

        String updateQuery = "UPDATE member SET money = money + ? WHERE id = ?";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(updateQuery)) {
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
