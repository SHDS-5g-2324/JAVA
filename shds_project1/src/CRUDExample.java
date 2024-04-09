import java.sql.*;
import java.util.Scanner;


public class CRUDExample {
    static final String JDBC_URL = "jdbc:oracle:thin:@211.178.201.98:1521:xe";
    static final String USER = "shds";
    static final String PASSWORD = "shds1234";
    static final String INSERT_MEMBER_QUERY = "INSERT INTO member (memberno, name, id, pwd, age, sex, email, money) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    static boolean loggedIn = false;
    static String loggedInUserId;

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
                    if (count == 1) {
                        loggedInUserId = id;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    static void processMenu(Connection conn, Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("1. 개인정보확인 2. 개인정보수정 3. 회원탈퇴 4. 로그아웃");
            System.out.print("원하는 작업을 선택하세요: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    if (loggedIn) {
                        readData(conn, loggedInUserId);
                    } else {
                        System.out.println("로그인이 필요합니다.");
                    }
                    break;
                case 2:
                    updateData(conn, scanner, loggedInUserId);
                    break;
                case 3:
                    deleteData(conn, scanner, loggedInUserId);
                case 4:
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
        String id;
        boolean idExists;

        do {
            System.out.print("아이디: ");
            id = scanner.next();
            idExists = checkIfIdExists(conn, id);
            if (idExists) {
                System.out.println("이미 존재하는 아이디입니다. 다른 아이디를 입력하세요.");
            }
        } while (idExists);

        System.out.print("비밀번호: ");
        String password = scanner.next();
        System.out.print("나이: ");
        int age = scanner.nextInt();
        System.out.print("성별: ");
        String sex = scanner.next();
        System.out.print("이메일: ");
        String email = scanner.next();
        int money = 0;

        try (PreparedStatement pstmt = conn.prepareStatement(INSERT_MEMBER_QUERY)) {
            int lastMemberNo = getLastMemberNo(conn);
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
                System.out.println("데이터가 성공적으로 추가되었습니다.");
            else
                System.out.println("데이터 추가에 실패했습니다.");
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
        String sql = "SELECT MAX(memberno) AS lastMemberNo FROM member";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("lastMemberNo");
            }
        }
        return 0;
    }

    static void readData(Connection conn, String userId) throws SQLException {
        String sql = "SELECT * FROM member WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("이름: " + rs.getString("name") +
                            ", 아이디: " + rs.getString("id") +
                            ", 나이: " + rs.getInt("age") +
                            ", 성별: " + rs.getString("sex") +
                            ", 이메일: " + rs.getString("email") +
                            ", 보유 잔액: " + rs.getInt("money"));
                } else {
                    System.out.println("해당하는 사용자의 정보가 없습니다.");
                }
            }
        }
    }

    static void updateData(Connection conn, Scanner scanner, String userId) throws SQLException {
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

    static void updateMoney(Connection conn, Scanner scanner, String userId) throws SQLException {
        System.out.print("추가할 금액 입력:");
        int plusMoney = scanner.nextInt();

        // 현재 잔액을 조회하는 쿼리
        String balanceQuery = "SELECT money FROM member WHERE id = ?";
        int currentBalance = 0;

        try (PreparedStatement balanceStmt = conn.prepareStatement(balanceQuery)) {
            balanceStmt.setString(1, userId);
            try (ResultSet rs = balanceStmt.executeQuery()) {
                if (rs.next()) {
                    currentBalance = rs.getInt("money");
                }
            }
        }

        // 잔액에 plusMoney를 추가하여 새로운 잔액을 계산
        int newBalance = currentBalance + plusMoney;

        // 새로운 잔액을 업데이트하는 쿼리
        String updateQuery = "UPDATE member SET money = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setInt(1, newBalance);
            pstmt.setString(2, userId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0)
                System.out.println("잔액이 성공적으로 수정되었습니다.");
            else
                System.out.println("잔액 수정에 실패했습니다.");
        }
    }

    static void deleteData(Connection conn, Scanner scanner, String userId) throws SQLException {
        // 현재 로그인한 사용자의 아이디를 사용하여 삭제
        String sql = "DELETE FROM member WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("데이터가 성공적으로 삭제되었습니다.");
                // 삭제 후 메인 메뉴로 돌아가기
                loggedIn = false;
                loggedInUserId = null; // 로그인 정보 초기화
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
