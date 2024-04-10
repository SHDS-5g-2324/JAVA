import java.sql.*;
import java.util.Scanner;

public class MemberCRUD {
    static final String JDBC_URL = "jdbc:oracle:thin:@211.178.201.98:1521:xe";
    static final String USER = "shds2";
    static final String PASSWORD = "shds1234";
    static final String INSERT_MEMBER_QUERY = "INSERT INTO member (member_no, name, id, pwd, ages, sex, email, money) " + // 수정
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    static boolean loggedIn = false;
    static String loggedInUserId;

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println("1.로그인 2.회원가입 3.종료");
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
            System.out.println("1.마이페이지 2.책 구매 3.회원탈퇴 4.로그아웃");
            System.out.print("원하는 작업을 선택하세요: ");
            int select = scanner.nextInt();

            switch (select) {
                case 1:
                    while (true) {
                        System.out.println("1.개인정보확인 2.개인정보수정 3.잔액조회 4.잔액충전 5.구매내역조회 6.관심목록조회 7.마이페이지 나가기");
                        System.out.print("원하는 작업을 선택하세요: ");

                        int choice = scanner.nextInt();
                        switch (choice) {
                            case 1:
                                readData(conn, loggedInUserId);
                                break;
                            case 2:
                                updateData(conn, scanner, loggedInUserId);
                                break;
                            case 3: // 잔액조회
                                readBalance(conn, loggedInUserId);
                                break;
                            case 4: // 잔액 충전
                                updateMoney(conn, scanner, loggedInUserId);
                                break;
                            case 5: //구매내역 조회
                                executeQuery(conn, loggedInUserId);
                                break;
                            case 6: // 관심내역 조회
                                displayFavoriteBooks(conn , loggedInUserId);
                                break;
                            case 7:
                                break;
                            default:
                                System.out.println("올바른 메뉴를 선택하세요.");
                        }
                        if (choice == 7)
                            break;
                    }
                    break;
                case 2:
                    // 책 구매 기능 추가
                    System.out.println("책을 구매합니다.");
                    break;
                case 3:
                    deleteData(conn, scanner, loggedInUserId);
                    break;
                case 4:
                    loggedIn = false;
                    System.out.println("로그아웃 되었습니다.");
                    return;
                default:
                    System.out.println("올바른 메뉴를 선택하세요.");
                    break; // default case에서도 반드시 break를 추가해야 합니다.
            }
        }
    }



    public static void createData(Connection conn, Scanner scanner) throws SQLException {
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

    static void readData(Connection conn, String userId) throws SQLException {
        String sql = "SELECT * FROM member WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("이름: " + rs.getString("name") +
                            ", 아이디: " + rs.getString("id") +
                            ", 나이: " + rs.getInt("ages") + // 수정
                            ", 성별: " + rs.getString("sex") +
                            ", 이메일: " + rs.getString("email") +
                            ", 보유 잔액: " + rs.getInt("money"));  // 총 구매내역 또는 총액 을 출력할 예정
                } else {
                    System.out.println("해당하는 사용자의 정보가 없습니다.");
                }
            }
        }
    }

    static void updateData(Connection conn, Scanner scanner, String userId) throws SQLException {
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

    static void readBalance(Connection conn, String userId) throws SQLException {
        String balanceQuery = "SELECT money FROM member WHERE id = ?";
        int currentBalance = 0;

        try (PreparedStatement balanceStmt = conn.prepareStatement(balanceQuery)) {
            balanceStmt.setString(1, userId);
            try (ResultSet rs = balanceStmt.executeQuery()) {
                if (rs.next()) {
                    currentBalance = rs.getInt("money");
                    System.out.println("현재 잔액: " + currentBalance + "원");
                } else {
                    System.out.println("잔액 조회에 실패했습니다.");
                }
            }
        }
    }
    static void updateMoney(Connection conn, Scanner scanner, String userId) throws SQLException {
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

    static void executeQuery(Connection conn, String memberId) throws SQLException {  // 구매내역 조회
        String sql = "SELECT M.*, P.*, B.* " +
                "FROM MEMBER M " +
                "LEFT JOIN PURCHASE P ON M.ID = P.ID " +
                "LEFT JOIN BOOK B ON B.BOOK_ID = P.BOOK_ID " +
                "WHERE M.ID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, memberId);
            ResultSet rs = pstmt.executeQuery();

            boolean hasPurchaseHistory = false; // 구매 내역 유무를 확인하기 위한 플래그

            while (rs.next()) {
                hasPurchaseHistory = true; // 구매 내역이 하나라도 있으면 플래그를 true로 설정

                int purchaseNo = rs.getInt("purchase_no");
                if (rs.wasNull()) {
                    System.out.println("구매 내역이 없습니다.");
                    break;
                }
                System.out.print("Member No: " + rs.getInt("member_no") + "  ");
                System.out.print("ID: " + rs.getString("id") + "  ");
                System.out.print("Name: " + rs.getString("name")+ "  ");
                System.out.print("Money: " + rs.getInt("money") + "  ");


                System.out.print("Purchase No: " + purchaseNo + "  ");
                System.out.print("Book ID: " + rs.getString("book_id") + "  ");
                System.out.print("State: " + rs.getString("state") + "  ");

                System.out.print("Title: " + rs.getString("subject") + "  ");
                System.out.print("Price: " + rs.getInt("price") + "  ");
                System.out.println("Author: " + rs.getString("author") + "  ");
            }

            if (!hasPurchaseHistory) { // 구매 내역이 없는 경우
                System.out.println("구매 내역이 없습니다.");
            }
        }
    }

    static void displayFavoriteBooks(Connection conn, String loggedInUserId) throws SQLException {
        String sql = "SELECT * " +
                "FROM Like_book L " +
                "LEFT JOIN MEMBER M ON M.ID = L.ID " +
                "JOIN book B ON B.book_id = L.book_id " +
                "WHERE L.id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, loggedInUserId);
            ResultSet rs = pstmt.executeQuery();

            boolean hasFavorites = false;

            while (rs.next()) {
                hasFavorites = true;
                System.out.print("Book ID: " + rs.getString("book_id") + "  ");
                System.out.print("Title: " + rs.getString("subject") + "  ");
                System.out.print("Author: " + rs.getString("author") + "  ");
                System.out.print("Price: " + rs.getInt("price") + "  ");
                System.out.println();
            }

            if (!hasFavorites) {
                System.out.println("관심목록이 없습니다.");
            }
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
