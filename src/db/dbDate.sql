CREATE TABLE "member" (
    "id" VARCHAR2(20) NOT NULL,
    "name" VARCHAR2(10) NOT NULL,
    "pwd" VARCHAR2(20) NOT NULL,
    "ages" VARCHAR2(5) NOT NULL,
    "sex" VARCHAR2(5) NOT NULL,
    "email" VARCHAR2(20) NOT NULL,
    "money" NUMBER(10) NOT NULL,
    "member_no" NUMBER(10) NOT NULL
);

CREATE TABLE "book" (
    "book_id" VARCHAR2(10) NOT NULL,
    "subject" VARCHAR2(100) NOT NULL,
    "price" NUMBER(10) NOT NULL,
    "link" VARCHAR2(100) NOT NULL,
    "author" VARCHAR2(10) NOT NULL,
    "publisher" VARCHAR2(20) NOT NULL,
    "like_count" NUMBER(10) NOT NULL,
    "sell_count" NUMBER(10) NOT NULL,
    "amount" NUMBER(10) NOT NULL
);

CREATE TABLE "like_book" (
    "book_id" VARCHAR2(10) NOT NULL,
    "id" VARCHAR2(20) NOT NULL
);

CREATE TABLE "purchase" (
    "purchase_no" NUMBER(10) NOT NULL,
    "book_id" VARCHAR2(10) NOT NULL,
    "id" VARCHAR2(20) NOT NULL,
    "state" VARCHAR2(10) NOT NULL
);


ALTER TABLE "member" ADD CONSTRAINT "PK_MEMBER" PRIMARY KEY ("id");

ALTER TABLE "book" ADD CONSTRAINT "PK_BOOK" PRIMARY KEY ("book_id");

ALTER TABLE "like_book" ADD CONSTRAINT "PK_LIKE_BOOK" PRIMARY KEY ("book_id", "id");

ALTER TABLE "purchase" ADD CONSTRAINT "PK_PURCHASE" PRIMARY KEY ("purchase_no", "book_id", "id");

ALTER TABLE "like_book" ADD CONSTRAINT "FK_book_TO_like_book_1" FOREIGN KEY ("book_id")
REFERENCES "book" ("book_id");

ALTER TABLE "like_book" ADD CONSTRAINT "FK_member_TO_like_book_1" FOREIGN KEY ("id")
REFERENCES "member" ("id");

ALTER TABLE "purchase" ADD CONSTRAINT "FK_book_TO_purchase_1" FOREIGN KEY ("book_id")
REFERENCES "book" ("book_id");

ALTER TABLE "purchase" ADD CONSTRAINT "FK_member_TO_purchase_1" FOREIGN KEY ("id")
REFERENCES "member" ("id");


ALTER TABLE "like_book" DROP CONSTRAINT "PK_LIKE_BOOK";
ALTER TABLE "purchase" DROP CONSTRAINT "PK_PURCHASE";

ALTER TABLE "like_book" DROP CONSTRAINT "FK_member_TO_like_book_1";
ALTER TABLE "purchase" DROP CONSTRAINT "FK_member_TO_purchase_1";

-- member 테이블에 더미 데이터 삽입
INSERT INTO "member" ("id", "name", "pwd", "ages", "sex", "email", "money", "member_no")
VALUES ('john_doe001', '홍길동', 'P@ssw0rd1', '30', '남', 'john@example.com', 65000, 1);

INSERT INTO "member" ("id", "name", "pwd", "ages", "sex", "email", "money", "member_no")
VALUES ('emma_smith002', '이영희', 'Pwd12345', '25', '여', 'emma@example.com', 75000, 2);

INSERT INTO "member" ("id", "name", "pwd", "ages", "sex", "email", "money", "member_no")
VALUES ('william_johnson003', '박민수', 'Secret@789', '35', '남', 'william@example.com', 55000, 3);

INSERT INTO "member" ("id", "name", "pwd", "ages", "sex", "email", "money", "member_no")
VALUES ('sophia_brown004', '정지영', 'SecurePwd12', '28', '여', 'sophia@example.com', 85000, 4);
-- member 테이블에 더미 데이터 삽입
INSERT INTO "member" ("id", "name", "pwd", "ages", "sex", "email", "money", "member_no")
VALUES ('james_miller005', '김민수', 'Passw0rd', '40', '남', 'james@example.com', 70000, 5);

INSERT INTO "member" ("id", "name", "pwd", "ages", "sex", "email", "money", "member_no")
VALUES ('olivia_taylor006', '이지연', 'Pwd5678', '22', '여', 'olivia@example.com', 60000, 6);

INSERT INTO "member" ("id", "name", "pwd", "ages", "sex", "email", "money", "member_no")
VALUES ('liam_wilson007', '김현수', 'Secret123', '33', '남', 'liam@example.com', 80000, 7);

INSERT INTO "member" ("id", "name", "pwd", "ages", "sex", "email", "money", "member_no")
VALUES ('ava_moore008', '정미숙', 'SecurePwd56', '29', '여', 'ava@example.com', 70000, 8);

INSERT INTO "member" ("id", "name", "pwd", "ages", "sex", "email", "money", "member_no")
VALUES ('mason_king009', '박승희', 'P@ssw0rd789', '38', '남', 'mason@example.com', 60000, 9);

INSERT INTO "member" ("id", "name", "pwd", "ages", "sex", "email", "money", "member_no")
VALUES ('mia_turner010', '김서연', 'Pwd98765', '26', '여', 'mia@example.com', 90000, 10);

INSERT INTO "member" ("id", "name", "pwd", "ages", "sex", "email", "money", "member_no")
VALUES ('ethan_adams011', '이민호', 'SecretPwd78', '32', '남', 'ethan@example.com', 55000, 11);

INSERT INTO "member" ("id", "name", "pwd", "ages", "sex", "email", "money", "member_no")
VALUES ('isabella_stewart012', '김지현', 'Secure123Pwd', '27', '여', 'isabella@example.com', 65000, 12);

INSERT INTO "member" ("id", "name", "pwd", "ages", "sex", "email", "money", "member_no")
VALUES ('noah_robinson013', '이승호', 'P@ss123word', '36', '남', 'noah@example.com', 75000, 13);

INSERT INTO "member" ("id", "name", "pwd", "ages", "sex", "email", "money", "member_no")
VALUES ('sophia_thompson014', '김수진', '123SecurePwd', '23', '여', 'sophiath@example.com', 85000, 14);

INSERT INTO "member" ("id", "name", "pwd", "ages", "sex", "email", "money", "member_no")
VALUES ('liam_scott015', '박영수', 'Pwd@secure12', '41', '남', 'liams@example.com', 60000, 15);

INSERT INTO "member" ("id", "name", "pwd", "ages", "sex", "email", "money", "member_no")
VALUES ('olivia_wright016', '이수정', 'SecureP@ss', '28', '여', 'olivia@example.com', 90000, 16);

INSERT INTO "member" ("id", "name", "pwd", "ages", "sex", "email", "money", "member_no")
VALUES ('william_jackson017', '최영희', 'P@ss12345', '35', '남', 'william@example.com', 75000, 17);

INSERT INTO "member" ("id", "name", "pwd", "ages", "sex", "email", "money", "member_no")
VALUES ('emma_rodriguez018', '김은경', 'Secure12@Pwd', '25', '여', 'emma@example.com', 55000, 18);

INSERT INTO "member" ("id", "name", "pwd", "ages", "sex", "email", "money", "member_no")
VALUES ('james_lee019', '신동현', '123Pwd@secure', '40', '남', 'james@example.com', 85000, 19);

INSERT INTO "member" ("id", "name", "pwd", "ages", "sex", "email", "money", "member_no")
VALUES ('ava_gonzalez020', '김수빈', 'P@ssword12', '29', '여', 'ava@example.com', 65000, 20);

ALTER TABLE "book" MODIFY ("author" VARCHAR2(30 BYTE));

-- book 테이블에 더미 데이터 삽입 (1부터 20까지)
INSERT INTO "book" ("book_id", "subject", "price", "link", "author", "publisher", "like_count", "sell_count", "amount")
VALUES ('bk001', '미움받을 용기', 18000, 'https://www.books.com/bk001', '기시미 이치로', '출판사A', 300, 500, 100);

INSERT INTO "book" ("book_id", "subject", "price", "link", "author", "publisher", "like_count", "sell_count", "amount")
VALUES ('bk002', '해리포터와 마법사의 돌', 25000, 'https://www.books.com/bk002', 'J.K. 롤링', '출판사B', 700, 800, 150);

INSERT INTO "book" ("book_id", "subject", "price", "link", "author", "publisher", "like_count", "sell_count", "amount")
VALUES ('bk003', '노인과 바다', 21000, 'https://www.books.com/bk003', '어니스트 헤밍웨이', '출판사C', 450, 600, 120);

INSERT INTO "book" ("book_id", "subject", "price", "link", "author", "publisher", "like_count", "sell_count", "amount")
VALUES ('bk004', '1984', 22000, 'https://www.books.com/bk004', '조지 오웰', '출판사D', 400, 550, 130);

INSERT INTO "book" ("book_id", "subject", "price", "link", "author", "publisher", "like_count", "sell_count", "amount")
VALUES ('bk005', '알고리즘', 30000, 'https://www.books.com/bk005', '클라이만', '출판사E', 800, 900, 200);

INSERT INTO "book" ("book_id", "subject", "price", "link", "author", "publisher", "like_count", "sell_count", "amount")
VALUES ('bk006', '어린 왕자', 15000, 'https://www.books.com/bk006', '생텍쥐페리', '출판사F', 350, 400, 90);



INSERT INTO "book" ("book_id", "subject", "price", "link", "author", "publisher", "like_count", "sell_count", "amount")
VALUES ('bk009', '좋은 인생을 만드는 방법', 28000, 'https://www.books.com/bk009', '로버트 울리', '출판사I', 900, 950, 180);

-- book 테이블에 더미 데이터 삽입 (21부터 30까지)
INSERT INTO "book" ("book_id", "subject", "price", "link", "author", "publisher", "like_count", "sell_count", "amount")
VALUES ('bk011', '죽여 마땅한가', 27000, 'https://www.books.com/bk011', '하태호', '출판사K', 850, 800, 160);

INSERT INTO "book" ("book_id", "subject", "price", "link", "author", "publisher", "like_count", "sell_count", "amount")
VALUES ('bk012', '완벽한 의무', 19000, 'https://www.books.com/bk012', '오카다 히로노부', '출판사L', 600, 700, 130);

INSERT INTO "book" ("book_id", "subject", "price", "link", "author", "publisher", "like_count", "sell_count", "amount")
VALUES ('bk013', '행복해지고 싶은 기분이 드는 책', 25000, 'https://www.books.com/bk013', '이지선', '출판사M', 750, 850, 170);

INSERT INTO "book" ("book_id", "subject", "price", "link", "author", "publisher", "like_count", "sell_count", "amount")
VALUES ('bk014', '실종된 산문', 31000, 'https://www.books.com/bk014', '최정욱', '출판사N', 920, 1000, 190);

INSERT INTO "book" ("book_id", "subject", "price", "link", "author", "publisher", "like_count", "sell_count", "amount")
VALUES ('bk015', '아무것도 아니었던 모든 것', 20000, 'https://www.books.com/bk015', '박송이', '출판사O', 550, 650, 120);

INSERT INTO "book" ("book_id", "subject", "price", "link", "author", "publisher", "like_count", "sell_count", "amount")
VALUES ('bk016', '작은 섬과 바다의 푸른 여행', 28000, 'https://www.books.com/bk016', '김은숙', '출판사P', 800, 900, 180);

INSERT INTO "book" ("book_id", "subject", "price", "link", "author", "publisher", "like_count", "sell_count", "amount")
VALUES ('bk017', '열심히 살고 싶지만 몸이 먼저 불타는 법', 24000, 'https://www.books.com/bk017', '오은영', '출판사Q', 700, 800, 150);

INSERT INTO "book" ("book_id", "subject", "price", "link", "author", "publisher", "like_count", "sell_count", "amount")
VALUES ('bk018', '미안하다, 내가 널 사랑하지 않아', 33000, 'https://www.books.com/bk018', '이미예', '출판사R', 1000, 1100, 220);

INSERT INTO "book" ("book_id", "subject", "price", "link", "author", "publisher", "like_count", "sell_count", "amount")
VALUES ('bk019', '모든 세계의 끝에서 너를 만나', 22000, 'https://www.books.com/bk019', '백난도', '출판사S', 600, 750, 140);

INSERT INTO "book" ("book_id", "subject", "price", "link", "author", "publisher", "like_count", "sell_count", "amount")
VALUES ('bk020', '고래이름의 중간양말', 26000, 'https://www.books.com/bk020', '토미', '출판사T', 750, 850, 170);


-- like_book 테이블에 더미 데이터 삽입
INSERT INTO "like_book" ("book_id", "id")
VALUES ('bk001', 'mia_turner010');

INSERT INTO "like_book" ("book_id", "id")
VALUES ('bk002', 'mia_turner010');

INSERT INTO "like_book" ("book_id", "id")
VALUES ('bk003', 'ethan_adams011');

INSERT INTO "like_book" ("book_id", "id")
VALUES ('bk001', 'isabella_stewart012');

INSERT INTO "like_book" ("book_id", "id")
VALUES ('bk004', 'noah_robinson013');

INSERT INTO "like_book" ("book_id", "id")
VALUES ('bk002', 'sophia_thompson014');

INSERT INTO "like_book" ("book_id", "id")
VALUES ('bk003', 'sophia_thompson014');

INSERT INTO "like_book" ("book_id", "id")
VALUES ('bk005', 'noah_robinson013');

INSERT INTO "like_book" ("book_id", "id")
VALUES ('bk006', 'james_smith015');

INSERT INTO "like_book" ("book_id", "id")
VALUES ('bk007', 'olivia_johnson016');
-- 추가로 10개의 더미 데이터 삽입
INSERT INTO "like_book" ("book_id", "id")
VALUES ('bk008', 'emma_anderson017');

INSERT INTO "like_book" ("book_id", "id")
VALUES ('bk009', 'liam_taylor018');

INSERT INTO "like_book" ("book_id", "id")
VALUES ('bk010', 'ava_wilson019');

INSERT INTO "like_book" ("book_id", "id")
VALUES ('bk011', 'william_martin020');

INSERT INTO "like_book" ("book_id", "id")
VALUES ('bk012', 'sophia_thompson014');

INSERT INTO "like_book" ("book_id", "id")
VALUES ('bk013', 'jackson_anderson021');

INSERT INTO "like_book" ("book_id", "id")
VALUES ('bk014', 'olivia_johnson016');

INSERT INTO "like_book" ("book_id", "id")
VALUES ('bk015', 'oliver_miller022');

INSERT INTO "like_book" ("book_id", "id")
VALUES ('bk016', 'emma_anderson017');

INSERT INTO "like_book" ("book_id", "id")
VALUES ('bk017', 'william_martin020');
ALTER TABLE "purchase" MODIFY "state" VARCHAR2(12);

-- purchase 테이블에 더미 데이터 삽입
INSERT INTO "purchase" ("purchase_no", "book_id", "id", "state")
VALUES (1, 'bk001', 'mia_turner010', '결제완료');

INSERT INTO "purchase" ("purchase_no", "book_id", "id", "state")
VALUES (2, 'bk004', 'ethan_adams011', '결제완료');

INSERT INTO "purchase" ("purchase_no", "book_id", "id", "state")
VALUES (3, 'bk002', 'isabella_stewart012', '결제대기');

INSERT INTO "purchase" ("purchase_no", "book_id", "id", "state")
VALUES (4, 'bk003', 'noah_robinson013', '결제대기');

INSERT INTO "purchase" ("purchase_no", "book_id", "id", "state")
VALUES (5, 'bk005', 'sophia_thompson014', '결제완료');

INSERT INTO "purchase" ("purchase_no", "book_id", "id", "state")
VALUES (6, 'bk006', 'james_smith015', '결제완료');

INSERT INTO "purchase" ("purchase_no", "book_id", "id", "state")
VALUES (7, 'bk007', 'olivia_johnson016', '결제대기');

INSERT INTO "purchase" ("purchase_no", "book_id", "id", "state")
VALUES (8, 'bk008', 'emma_anderson017', '결제완료');

INSERT INTO "purchase" ("purchase_no", "book_id", "id", "state")
VALUES (9, 'bk009', 'liam_taylor018', '결제완료');

INSERT INTO "purchase" ("purchase_no", "book_id", "id", "state")
VALUES (10, 'bk010', 'ava_wilson019', '결제완료');

-- purchase 테이블에 추가로 10개의 더미 데이터 삽입
INSERT INTO "purchase" ("purchase_no", "book_id", "id", "state")
VALUES (11, 'bk011', 'william_martin020', '결제완료');

INSERT INTO "purchase" ("purchase_no", "book_id", "id", "state")
VALUES (12, 'bk012', 'sophia_thompson014', '결제대기');

INSERT INTO "purchase" ("purchase_no", "book_id", "id", "state")
VALUES (13, 'bk013', 'jackson_anderson021', '결제완료');

INSERT INTO "purchase" ("purchase_no", "book_id", "id", "state")
VALUES (14, 'bk014', 'olivia_johnson016', '결제대기');

INSERT INTO "purchase" ("purchase_no", "book_id", "id", "state")
VALUES (15, 'bk015', 'oliver_miller022', '결제완료');

INSERT INTO "purchase" ("purchase_no", "book_id", "id", "state")
VALUES (16, 'bk016', 'emma_anderson017', '결제완료');

INSERT INTO "purchase" ("purchase_no", "book_id", "id", "state")
VALUES (17, 'bk017', 'william_martin020', '결제완료');

INSERT INTO "purchase" ("purchase_no", "book_id", "id", "state")
VALUES (18, 'bk018', 'jackson_anderson021', '결제대기');

INSERT INTO "purchase" ("purchase_no", "book_id", "id", "state")
VALUES (19, 'bk019', 'oliver_miller022', '결제완료');

INSERT INTO "purchase" ("purchase_no", "book_id", "id", "state")
VALUES (20, 'bk020', 'sophia_thompson014', '결제대기');
