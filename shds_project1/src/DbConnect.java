import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbConnect {
    // Oracle 데이터베이스 연결 정보
    String jdbcURL = "jdbc:oracle:thin:@localhost:1521/xe"; // 호스트:포트/서비스이름
    String username = "shds2";
    String password = "shds1234";
    // JDBC 연결 객체
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    public DbConnect() {
        try {
            // JDBC 드라이버 로드
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // 데이터베이스 연결
            conn = DriverManager.getConnection(jdbcURL, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // 리소스 해제
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //member 전체 get 메소드
    public List<Member> getMember() {
        List<Member> list = new ArrayList<>();
        try {
            Member member = null;
//         SQL 쿼리 작성
            String sql = "SELECT * FROM member";
            // SQL 쿼리 실행
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery(sql);

//         결과 처리
            while (rs.next()) {
                // 각 행의 데이터 가져와서 리스트에 저장
                list.add(new Member(rs.getInt("memberno"),
                        rs.getString("name"), rs.getString("id"), rs.getString("pwd"),
                        rs.getInt("age"), rs.getString("sex"), rs.getString("email"),
                        rs.getInt("money")));
            }
        }catch (Exception e ) {
            e.printStackTrace();
        } finally{
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch(SQLException e){}
        }
        return list;
    }
}