package bookstore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Read {
    public static boolean login(Connection conn, Scanner scanner) throws SQLException {
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
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void readData(Connection conn, String userId) throws SQLException {
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
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("lastMemberNo");
            }
        }
        return 0;
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
                System.out.print("Name: " + rs.getString("name") + "  ");
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

    static void bookList(Connection conn) throws SQLException {
        String sql = "SELECT * FROM Book ";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                System.out.println("제목: " + rs.getString("SUBJECT") +
                        ", 가격: " + rs.getInt("PRICE") +
                        ", 저자: " + rs.getString("AUTHOR") +
                        ", 출판사: " + rs.getString("PUBLISHER") +
                        ", 좋아요 수: " + rs.getInt("LIKE_COUNT") +
                        ", 판매 수: " + rs.getInt("SELL_COUNT") +
                        ", 수량: " + rs.getInt("AMOUNT"));
            }
        }
    }

    static void top10List(Connection conn) throws SQLException {
        String sql = "SELECT * FROM (SELECT * FROM Book ORDER BY SELL_COUNT DESC) WHERE ROWNUM <= 10";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            int rank = 1;
            while (rs.next()) {
                System.out.println("순위: " + rank +
                        ", 제목: " + rs.getString("SUBJECT") +
                        ", 가격: " + rs.getInt("PRICE") +
                        ", 저자: " + rs.getString("AUTHOR") +
                        ", 출판사: " + rs.getString("PUBLISHER")
                );
                rank++;
            }
        }
    }

    static void searchBook(Connection conn, Scanner scanner) throws SQLException {
        do {
            System.out.print("검색할 책의 제목을 입력하세요: ");
            scanner.nextLine(); // 입력 버퍼 비우기

            String bookName = scanner.nextLine(); // 사용자로부터 입력 받은 책의 제목을 bookName 변수에 저장

            String sql = "SELECT * FROM BOOK WHERE SUBJECT = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, bookName);
                try (ResultSet rs = pstmt.executeQuery()) {
                    boolean found = false; // 검색 결과를 표시하기 위한 플래그
                    while (rs.next()) {
                        found = true; // 검색 결과가 있음을 표시
                        System.out.println("제목: " + rs.getString("SUBJECT") +
                                ", 가격: " + rs.getInt("PRICE") +
                                ", 저자: " + rs.getString("AUTHOR") +
                                ", 출판사: " + rs.getString("PUBLISHER") +
                                ", 좋아요 수: " + rs.getInt("LIKE_COUNT") +
                                ", 수량: " + rs.getInt("AMOUNT"));

                        // 책 구매 또는 관심책 추가 메뉴 제공
                        System.out.println("1.책 구매하기  2.관심책 추가하기 3.이전 메뉴로 돌아가기");
                        System.out.print("원하는 작업을 선택하세요: ");
                        int num = scanner.nextInt();
                        switch (num) {
                            case 1:
                                System.out.println();
                                /* 책 구매하기 기능 호출구매를 하면
                                바로 결제 하기와 나중에 결제하기 를 선택하고
                                    결제하기를 하면 보유 잔액에서 차감하고 결제대기를 하면 넘어간다
                                그다음  추가 된다

                                */

                                break;
                            case 2:
                                Create.addFavoriteBook(conn, scanner, rs.getString("BOOK_ID"));
                                // 관심책 추가하기 기능 호출
                                break;
                            case 3:
                                break;
                            default:
                                System.out.println("잘못된 선택입니다.");
                        }
                        if (num == 3) {
                            break;
                        }
                    }

                    if (!found) {
                        System.out.println("해당하는 책이 없습니다.");
                    }
                }
            }
            // 사용자가 다시 검색을 할지 물어봄
            String again;
            do {
                System.out.print("책을 검색하시겠습니까? (1.예  2.아니요): ");
                again = scanner.next();
                if (!again.equals("1") && !again.equals("2")) {
                    System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
                }
            } while (!again.equals("1") && !again.equals("2"));

            if (again.equals("2")) {  // 아니라고 입력하면 메서드 종료
                return;
            }
        } while (true);
    }
}