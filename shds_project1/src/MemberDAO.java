import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
    private final Connection connection;

    public MemberDAO(Connection connection) {
        this.connection = connection;
    }

    public void create(Member member) throws SQLException {
        String sql = "INSERT INTO member (memberno, name, id, pwd, age, sex, email, point) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, member.getMemberno());
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getId());
            pstmt.setString(4, member.getPassword());
            pstmt.setInt(5, member.getAge());
            pstmt.setString(6, member.getSex());
            pstmt.setString(7, member.getEmail());
            pstmt.setInt(8, member.getPoint());
            pstmt.executeUpdate();
        }
    }

    public Member read(int memberNo) throws SQLException {
        String sql = "SELECT * FROM member WHERE memberno = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, memberNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Member(
                            rs.getInt("memberno"),
                            rs.getString("name"),
                            rs.getString("id"),
                            rs.getString("pwd"),
                            rs.getInt("age"),
                            rs.getString("sex"),
                            rs.getString("email"),
                            rs.getInt("point")
                    );
                }
            }
        }
        return null;
    }

    public List<Member> readAll() throws SQLException {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM member";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                members.add(new Member(
                        rs.getInt("memberno"),
                        rs.getString("name"),
                        rs.getString("id"),
                        rs.getString("pwd"),
                        rs.getInt("age"),
                        rs.getString("sex"),
                        rs.getString("email"),
                        rs.getInt("point")
                ));
            }
        }
        return members;
    }

    public void update(Member member) throws SQLException {
        String sql = "UPDATE member SET name = ?, id = ?, pwd = ?, age = ?, sex = ?, email = ?, point = ? WHERE memberno = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getId());
            pstmt.setString(3, member.getPassword());
            pstmt.setInt(4, member.getAge());
            pstmt.setString(5, member.getSex());
            pstmt.setString(6, member.getEmail());
            pstmt.setInt(7, member.getPoint());
            pstmt.setInt(8, member.getMemberno());
            pstmt.executeUpdate();
        }
    }

    public void delete(int memberNo) throws SQLException {
        String sql = "DELETE FROM member WHERE memberno = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, memberNo);
            pstmt.executeUpdate();
        }
    }
}
