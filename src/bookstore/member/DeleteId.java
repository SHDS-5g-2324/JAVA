package bookstore.member;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import bookstore.util.DbConnect;

public class DeleteId {
	public static void deleteData(DbConnect db, String userId) throws SQLException {
        // 현재 로그인한 사용자의 아이디의 비밀번호를 임의의 문자열로 변경
		// 이후 재가입 불가능하게 하기 위해서
        String sql = "UPDATE member SET pwd = CONCAT(pwd, '_deleted@@@@###@@') WHERE id = ?";
        
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
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
