import java.sql.*;

public class DbConnect {
    public static void main(String[] args) {
        // Oracle 데이터베이스 연결 정보
        String jdbcURL = "jdbc:oracle:thin:@211.178.201.98:1521:xe"; // 호스트:포트/서비스이름
        String username = "shds2";
        String password = "shds1234";

        // JDBC 연결 객체
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // JDBC 드라이버 로드
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // 데이터베이스 연결
            connection = DriverManager.getConnection(jdbcURL, username, password);

            // SQL 쿼리 작성

            String sqlQuery = "SELECT * FROM member";

            // SQL 쿼리 실행
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlQuery);

            // 결과 처리
            while (resultSet.next()) {
                // 각 행의 데이터 가져오기
                int memberno = resultSet.getInt("memberno");
                String name = resultSet.getString("name");
                String id = resultSet.getString("id");
                String pwd = resultSet.getString("pwd");
                int age = resultSet.getInt("age");
                String sex = resultSet.getString("sex");
                String email = resultSet.getString("email");
                int point = resultSet.getInt("point");

                // 결과 출력
                System.out.println("Member No: " + memberno);
                System.out.println("Name: " + name);
                System.out.println("ID: " + id);
                System.out.println("Password: " + pwd);
                System.out.println("Age: " + age);
                System.out.println("Sex: " + sex);
                System.out.println("Email: " + email);
                System.out.println("Point: " + point);
                System.out.println();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 리소스 해제
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
