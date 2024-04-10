import lombok.Data;

@Data
public class Member {
    private int memberno;
    private String name;
    private String id;
    private String pwd;
    private int age;
    private String sex;
    private String email;
    private int money;


    public Member(int memberno, String name, String id, String pwd, int age, String sex, String email, int money) {
        this.memberno = memberno;
        this.name = name;
        this.id = id;
        this.pwd = pwd;
        this.age = age;
        this.sex = sex;
        this.email = email;
        this.money = money;
    }
}