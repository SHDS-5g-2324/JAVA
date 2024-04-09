public class Member {
    private int memberNo;
    private String name;
    private String id;
    private String password;
    private int age;
    private String sex;
    private String email;
    private int point;

    // 생성자
    public Member(int memberNo, String name, String id, String password, int age, String sex, String email, int point) {
        this.memberNo = memberNo;
        this.name = name;
        this.id = id;
        this.password = password;
        this.age = age;
        this.sex = sex;
        this.email = email;
        this.point = point;
    }

    // Getter 및 Setter 메서드들
    public int getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(int memberNo) {
        this.memberNo = memberNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
