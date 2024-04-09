import java.sql.*;
import java.util.Scanner;

public class CRUDExample {
    static final String JDBC_URL = "jdbc:oracle:thin:@211.178.201.98:1521:xe";
    static final String USER = "shds2";
    static final String PASSWORD = "shds1234";
    static final String INSERT_MEMBER_QUERY = "INSERT INTO member (memberno, name, id, pwd, age, sex, email, point) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    static boolean loggedIn = false;

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {
            System.out.println("DB 연결 성공!");

            while (true) {
                System.out.println("1. 로그인 2. 회원가입 3. 종료");
                System.out.print("메뉴를 선택하세요: ");
                int menu = scanner.nextInt();

                switch (menu) {
                    case 1:
                        if (login(conn, scanner)) {
                            System.out.println("로그인 성공!");
                            loggedIn = true;
                            processMenu(conn, scanner);
                        } else {
                            System.out.println("아이디 또는 비밀번호가 일치하지 않습니다.");
                        }
                        break;
                    case 2:
                        createData(conn, scanner);
                        break;
                    case 3:
                        System.out.println("프로그램을 종료합니다.");
                        return;
                    default:
                        System.out.println("올바른 메뉴를 선택하세요.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static boolean login(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("아이디: ");
        String id = scanner.next();
        System.out.print("비밀번호: ");
        String password = scanner.next();

        String sql = "SELECT COUNT(*) AS count FROM member WHERE id = ? AND pwd = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    return count == 1;
                }
            }
        }
        return false;
    }

    static void processMenu(Connection conn, Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("1. Create 2. Read 3. Update 4. Delete 5. 로그아웃");
            System.out.print("원하는 작업을 선택하세요: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    createData(conn, scanner);
                    break;
                case 2:
                    readData(conn);
                    break;
                case 3:
                    updateData(conn, scanner);
                    break;
                case 4:
                    deleteData(conn, scanner);
                    break;
                case 5:
                    loggedIn = false;
                    System.out.println("로그아웃 되었습니다.");
                    return;
                default:
                    System.out.println("올바른 메뉴를 선택하세요.");
            }
        }
    }

    static void createData(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("이름: ");
        String name = scanner.next();
        System.out.print("나이: ");
        int age = scanner.nextInt();
        System.out.print("성별: ");
        String sex = scanner.next();
        System.out.print("이메일: ");
        String email = scanner.next();
        System.out.print("포인트: ");
        int point = scanner.nextInt();

        // 마지막 memberno 가져오기
        int lastMemberNo = getLastMemberNo(conn);

        // 새로운 번호 생성
        int num = lastMemberNo + 1;

        // 중복되지 않은 아이디를 사용하여 회원 추가
        try (PreparedStatement pstmt = conn.prepareStatement(INSERT_MEMBER_QUERY)) {
            pstmt.setInt(1, num);
            pstmt.setString(2, name);
            pstmt.setString(3, name.toLowerCase() + age); // 임시 아이디 생성
            pstmt.setString(4, name.toLowerCase() + age); // 임시 비밀번호 생성
            pstmt.setInt(5, age);
            pstmt.setString(6, sex);
            pstmt.setString(7, email);
            pstmt.setInt(8, point);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0)
                System.out.println("데이터가 성공적으로 추가되었습니다.");
            else
                System.out.println("데이터 추가에 실패했습니다.");
        }
    }

    // 마지막 memberno 가져오는 메서드
    static int getLastMemberNo(Connection conn) throws SQLException {
        String sql = "SELECT MAX(memberno) AS lastMemberNo FROM member";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("lastMemberNo");
            }
        }
        return 0;
    }

    static void readData(Connection conn) throws SQLException {
        String sql = "SELECT * FROM member";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("번호: " + rs.getInt("memberno") +
                        ", 이름: " + rs.getString("name") +
                        ", 아이디: " + rs.getString("id") +
                        ", 나이: " + rs.getInt("age") +
                        ", 성별: " + rs.getString("sex") +
                        ", 이메일: " + rs.getString("email") +
                        ", 포인트: " + rs.getInt("point"));
            }
        }
    }

    static void updateData(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("수정할 회원 번호: ");
        int memberNo = scanner.nextInt();
        System.out.print("변경할 포인트: ");
        int point = scanner.nextInt();

        String sql = "UPDATE member SET point = ? WHERE memberno = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, point);
            pstmt.setInt(2, memberNo);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0)
                System.out.println("데이터가 성공적으로 수정되었습니다.");
            else
                System.out.println("데이터 수정에 실패했습니다.");
        }
    }

    static void deleteData(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("삭제할 회원 번호: ");
        int memberNo = scanner.nextInt();

        String sql = "DELETE FROM member WHERE memberno = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, memberNo);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0)
                System.out.println("데이터가 성공적으로 삭제되었습니다.");
            else
                System.out.println("데이터 삭제에 실패했습니다.");
        }
    }
}
