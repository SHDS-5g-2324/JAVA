package bookstore;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import bookstore.book.Read;
import bookstore.book.Search;
import bookstore.member.DeleteId;
import bookstore.member.GetBalance;
import bookstore.member.IdCheck;
import bookstore.member.Login;
import bookstore.member.MoneyChange;
import bookstore.member.PwdChange;
import bookstore.member.RegisterMember;
import bookstore.purchase.Cart;
import bookstore.purchase.Purchase;
import bookstore.util.DbConnect;

public class Main {
	static final DbConnect db = new DbConnect();
	static boolean loggedIn = false;
	public static String loggedInUserId;
	static String bookName;

	public static void main(String[] args) {
		try (Connection conn = db.getConnection()) {
			Scanner scanner = new Scanner(System.in);
			while (true) {
				System.out.println("--------------------------");
				System.out.println("1.로그인 2.회원가입 3.종료");
				System.out.print("메뉴를 선택하세요: ");
			
				String menu = scanner.nextLine();

				switch (menu) {
				case "1":
					loggedInUserId = Login.login(db);
					if (!"".equals(loggedInUserId)) {
						System.out.println("로그인 성공!");
						loggedIn = true;
						processMenu(db);
					} else {
						System.out.println("아이디 또는 비밀번호가 일치하지 않습니다.");
					}
					break;
				case "2":
					RegisterMember.createData(db);
					break;
				case "3":
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

	static void processMenu(DbConnect db) throws SQLException {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("--------------------------");
			System.out.println("1.마이페이지 2.책목록 3.회원탈퇴 4.로그아웃");
			System.out.print("원하는 작업을 선택하세요: ");
			String select = scanner.next(); // 개행 문자까지만 읽음
			scanner.nextLine(); // 개행 문자 소비

			switch (select) {
			case "1":
				while (true) {
					System.out.println("--------------------------");
					System.out.println("1.개인정보확인 2.비밀번호변경 3.잔액조회 4.잔액충전 5.구매내역조회 6.관심목록조회 7.장바구니목록 8.뒤로가기");
					System.out.print("원하는 작업을 선택하세요: ");
					String choice = scanner.nextLine();
					switch (choice) {
					case "1":
						IdCheck.readData(db, loggedInUserId);
						break;
					case "2":
						PwdChange.updateData(db, loggedInUserId);
						break;
					case "3":
						GetBalance.readBalance(db, loggedInUserId);
						break;
					case "4":
						MoneyChange.updateMoney(db, loggedInUserId);
						break;
					case "5":
						Read.executeQuery(db, loggedInUserId);
						break;
					case "6":
						Read.displayFavoriteBooks(db, loggedInUserId);
						break;
					case "7":
						Cart.cartDisplay(db, loggedInUserId);
						break;
					case "8":
						break;
					default:
						System.out.println("올바른 메뉴를 선택하세요.");
					}
					if ("8".equals(choice))
						break;
				}
				break;
			case "2":
				while (true) {
					System.out.println("--------------------------");
					System.out.println("1.책목록 2.TOP10도서 3.책구매하기 4.책검색 5.뒤로가기");
					System.out.print("원하는 작업을 선택하세요: ");
					String choice = scanner.nextLine();

					switch (choice) {
					case "1":
						Read.bookList(db);
						break;
					case "2":
						Read.top10List(db);
						break;
					case "3":
						Read.bookList(db);
						Purchase.purchaseBook(db);
						break;
					case "4":
						Search.searchBook(db); // 검색이 되었으면 관심목록 추가 , 구매하기 , 뒤로가기 버튼을 만들고
						// 검색이 안되었으면 게속 검색하기 겠습니까를 추가헤서 뒤로가기랑 냅둔다
						break;
					case "5":
						break;
					default:
						System.out.println("올바른 메뉴를 선택하세요.");
						break;
					}
					if ("5".equals(choice)) {
						break;
					}
				}
				break;
			case "3":
				DeleteId.deleteData(db, loggedInUserId);
				break;
			case "4":
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
