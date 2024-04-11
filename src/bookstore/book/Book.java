import lombok.Data;

@Data
public class Book {
    private String book_id;
    private String subject;
    private int price;
    private String link;
    private String author;
    private String publisher;
    private int like_count;
    private int sell_count;
    private int amount;

    public Book(String book_id, String subject, int price, String link, String author, String publisher, int like_count, int sell_count, int amount) {
        this.book_id = book_id;
        this.subject = subject;
        this.price = price;
        this.link = link;
        this.author = author;
        this.publisher = publisher;
        this.like_count = like_count;
        this.sell_count = sell_count;
        this.amount = amount;
    }
}