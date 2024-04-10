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
}
