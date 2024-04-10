package bookstore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Create {
    static final String INSERT_MEMBER_QUERY = "INSERT INTO member (member_no, name, id, pwd, ages, sex, email, money) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    public static void createData(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("이름: ");
        String name = scanner.next();
        String id;
        boolean idExists;

        do {
            System.out.print("아이디: ");
            id = scanner.next();
            idExists = checkIfIdExists(conn, id); // Create 클래스 내에 메소드이므로 클래스명 없이 직접 호출
            if (idExists) {
                System.out.println("이미 존재하는 아이디입니다. 다른 아이디를 입력하세요.");
            }
        } while (idExists);

        System.out.print("비밀번호: ");
        String password = scanner.next();
        int age;
        while (true) {
            try {
                System.out.print("나이: ");
                age = Integer.parseInt(scanner.next());
                break; // 입력이 올바른 경우 루프 종료
            } catch (NumberFormatException e) {
                System.out.println("올바른 형식이 아닙니다. 나이는 숫자로 입력하세요.");
            }
        }

        System.out.print("성별: ");
        String sex = scanner.next();
        System.out.print("이메일: ");
        String email = scanner.next();
        int money = 0;

        try (PreparedStatement pstmt = conn.prepareStatement(INSERT_MEMBER_QUERY)) {
            int lastMemberNo = getLastMemberNo(conn); // Create 클래스 내에 메소드이므로 클래스명 없이 직접 호출
            int num = lastMemberNo + 1;

            pstmt.setInt(1, num);
            pstmt.setString(2, name);
            pstmt.setString(3, id);
            pstmt.setString(4, password);
            pstmt.setInt(5, age);
            pstmt.setString(6, sex);
            pstmt.setString(7, email);
            pstmt.setInt(8, money);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0)
                System.out.println("회원가입이 되었습니다.");
            else
                System.out.println("회원가입에 실패했습니다.");
        }
    }

    static boolean checkIfIdExists(Connection conn, String id) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM member WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    return count > 0;
                }
            }
        }
        return false;
    }

    static int getLastMemberNo(Connection conn) throws SQLException {
        String sql = "SELECT MAX(member_no) AS lastMemberNo FROM member"; // 수정
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("lastMemberNo");
            }
        }
        return 0;
    }

    static void addFavoriteBook(Connection conn, Scanner scanner, String bookId) throws SQLException {
        String userId = Main.loggedInUserId; // 현재 로그인한 사용자의 ID 가져오기

        // 이미 관심 책 목록에 있는지 확인
        String checkSql = "SELECT COUNT(*) AS count FROM Like_book WHERE id = ? AND book_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    if (count > 0) {
                        System.out.println("이미 관심 책으로 추가되어 있습니다.");
                        return;
                    }
                }
            }
        }

        // 관심 책 추가
        String insertSql = "INSERT INTO Like_book (id, book_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, bookId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("관심 책으로 추가되었습니다.");
                // 관심 책 추가에 성공한 경우 해당 책의 LIKE_COUNT를 1 증가시키는 쿼리 실행
                String updateLikeCountSql = "UPDATE Book SET LIKE_COUNT = LIKE_COUNT + 1 WHERE BOOK_ID = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateLikeCountSql)) {
                    updateStmt.setString(1, bookId);
                    updateStmt.executeUpdate();
                }
            } else {
                System.out.println("관심 책 추가에 실패했습니다.");
            }
        }
    }
}
