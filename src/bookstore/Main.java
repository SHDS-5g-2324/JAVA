package bookstore;

import java.sql.*;
import java.util.Scanner;

public class Main {
    static final String JDBC_URL = "jdbc:oracle:thin:@211.178.201.98:1521:xe";
    static final String USER = "shds2";
    static final String PASSWORD = "shds1234";
    static final String INSERT_MEMBER_QUERY = "INSERT INTO member (member_no, name, id, pwd, ages, sex, email, money) " + // 수정
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    static boolean loggedIn = false;
    static String loggedInUserId;
    static String bookName;


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
                        Create.createData(conn, scanner);
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
            System.out.println("1.마이페이지 2.책목록 3.회원탈퇴 4.로그아웃");
            System.out.print("원하는 작업을 선택하세요: ");
            int select = scanner.nextInt();

            switch (select) {
                case 1:
                    while (true) {
                        System.out.println("1.개인정보확인 2.비밀번호변경 3.잔액조회 4.잔액충전 5.구매내역조회 6.관심목록조회 7.뒤로가기");
                        System.out.print("원하는 작업을 선택하세요: ");

                        int choice = scanner.nextInt();
                        switch (choice) {
                            case 1:
                                Read.readData(conn, loggedInUserId);
                                break;
                            case 2:
                                Update.updateData(conn, scanner, loggedInUserId);
                                break;
                            case 3:
                                Read.readBalance(conn, loggedInUserId);
                                break;
                            case 4:
                                Update.updateMoney(conn, scanner, loggedInUserId);
                                break;
                            case 5:
                                Read.executeQuery(conn, loggedInUserId);
                                break;
                            case 6:
                                Read.displayFavoriteBooks(conn, loggedInUserId);
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
                    while (true) {
                        System.out.println("1.책목록 2.TOP10도서  3.책검색 4.뒤로가기");
                        System.out.print("원하는 작업을 선택하세요: ");

                        int choice = scanner.nextInt();

                        switch (choice) {
                            case 1:
                                Read.bookList(conn);
                                break;
                            case 2:
                                Read.top10List(conn);
                                break;
                            case 3:
                                Read.searchBook(conn,scanner); // 검색이 되었으면 관심목록 추가 , 구매하기 , 뒤로가기 버튼을 만들고
                                // 검색이 안되었으면 게속 검색하기 겠습니까를 추가헤서 뒤로가기랑 냅둔다
                                break;
                            case 4:
                                break;
                            default:
                                System.out.println("올바른 메뉴를 선택하세요.");
                                break;
                        }
                        if(choice == 4){
                            break;
                        }
                    }
                    break;

                case 3:
                    Delete.deleteData(conn, scanner, loggedInUserId);
                    break;
                case 4:
                    loggedIn = false;
                    System.out.println("로그아웃 되었습니다.");
                    return;
                default:
                    System.out.println("올바른 메뉴를 선택하세요.");
                    break;
            }
        }
    }
}