public class Member {
    private int memberno;
    private String name;
    private String id;
    private String password;
    private int age;
    private String sex;
    private String email;
    private int point;


    public Member(int memberno, String name, String id, String password, int age, String sex, String email, int point) {
        this.memberno = memberno;
        this.name = name;
        this.id = id;
        this.password = password;
        this.age = age;
        this.sex = sex;
        this.email = email;
        this.point = point;
    }
    public int getMemberno() {
        return memberno;
    }

    public void setMemberno(int memberno) {
        this.memberno = memberno;
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
