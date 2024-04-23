package bookstore.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bookstore.member.Member;

public class DbConnect {
	// Oracle 데이터베이스 연결 정보
	private static final String JDBC_URL = "jdbc:oracle:thin:@211.178.201.98:1521:xe"; // 호스트:포트/서비스이름
	private static final String USERNAME = "shds2";
	private static final String PASSWORD = "shds1234";
	// JDBC 연결 객체
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	public DbConnect() {
		try {
			// JDBC 드라이버 로드
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 데이터베이스 연결
			conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 데이터베이스 연결 반환 메소드
	public Connection getConnection() {
		return conn;
	}

	// member 전체 get 메소드
	public List<Member> getMember() {
		List<Member> list = new ArrayList<>();
		try {
			// SQL 쿼리 작성
			String sql = "SELECT * FROM member";
			// SQL 쿼리 실행
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			// 결과 처리
			while (rs.next()) {
				// 각 행의 데이터 가져와서 리스트에 저장
				list.add(
						new Member(rs.getInt("memberno"), rs.getString("name"), rs.getString("id"), rs.getString("pwd"),
								rs.getInt("age"), rs.getString("sex"), rs.getString("email"), rs.getInt("money")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(); // 데이터베이스 연결 닫기
		}
		return list;
	}

	// 회원 추가 메소드
	public void addMember(String name, String id, String password, int age, String sex, String email, int money) {
		try {
			// SQL 쿼리 작성
			String sql = "INSERT INTO member (name, id, pwd, age, sex, email, money) VALUES (?, ?, ?, ?, ?, ?, ?)";
			// SQL 쿼리 실행
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, id);
			pstmt.setString(3, password);
			pstmt.setInt(4, age);
			pstmt.setString(5, sex);
			pstmt.setString(6, email);
			pstmt.setInt(7, money);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(); // 데이터베이스 연결 닫기
		}
	}

	// 데이터베이스 연결 닫기
	private void close() {
		try {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/* SELECT String 메소드 */
	public ResultSet getSqlResult(String sql, Map<Integer, String> map) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);

			for (Integer key : map.keySet()) {
				pstmt.setString(key, map.get(key));
			}

			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			// 예외 발생 시 ResultSet을 반환하지 않고 예외를 다시 던집니다.
			throw e;
		}

		return rs;
	}

	/* 입력 값이 하나인 경우 */
	public ResultSet getSqlResult(String sql, int idx, String to) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(idx, to);
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			// 예외 발생 시 ResultSet을 반환하지 않고 예외를 다시 던집니다.
			throw e;
		}

		return rs;
	}

	/* 그룹함수 등 sql 결과 하나만 리턴 메소드 */
	public ResultSet getSqlResult(String sql) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			// 예외 발생 시 ResultSet을 반환하지 않고 예외를 다시 던집니다.
			throw e;
		}

		return rs;
	}

}
