# JAVA
자바 부분

## 기능구현
### 기본 기능
- 로그인
- 회원가입
- (DB저장 여부 확인 후 존재하면 로그인 / 없으면 회원가입)
- 책 리스트 출력

### 구매 기능
- 책 리스트 출력
- 번호/이름 입력
- 구매대기 상태로 PURCHASE 테이블에 저장
- 내 구매내역에서 확인
- 구매확정 --> 구매완료 상태로 변경
- (책 수량(-), 포인트(-), 보유 책 리스트(+))
- 구매 리스트 출력
-
- 상태 업데이트(구매대기 / 구매완료)
- 

### 좋아요 기능 
- 책 번호/이름 입력
- LIKE_BOOK 테이블에 계정 아이디 + 책 아이디 저장

### 

### 구분

### 마이페이지  - 개인정보확인
		   개인정보 수정
		   잔액 조회
		   잔액추가 
		   구매내역 
		   관심목록 

### 책 구매하기 - 구매 가능한 책 리스트 전체출력
		    나이별 인기 top 3 
		    관심 목록 추가 
		    책 구매하기 
                   
### 회원탈퇴
### 로그아웃


## Read.java

### `boolean login(Connection conn, Scanner scanner)`
    - 사용자의 아이디와 비밀번호를 입력받아 로그인을 수행하는 메서드입니다.

### `void readData(Connection conn, String userId)`
    - 특정 사용자의 정보를 데이터베이스에서 조회하여 출력하는 메서드입니다.

### `boolean checkIfIdExists(Connection conn, String id)`
    - 특정 아이디가 데이터베이스에 존재하는지 확인하는 메서드입니다.

### `int getLastMemberNo(Connection conn)`
    - 가장 최근에 추가된 회원의 회원 번호를 조회하는 메서드입니다.

### `void readBalance(Connection conn, String userId)`
    - 특정 사용자의 보유 잔액을 조회하는 메서드입니다.

### `void executeQuery(Connection conn, String memberId)`
    - 특정 회원의 구매 내역을 조회하는 메서드입니다.

### `void displayFavoriteBooks(Connection conn, String loggedInUserId)`
    - 특정 사용자의 관심 책 목록을 조회하여 출력하는 메서드입니다.

### `void bookList(Connection conn)`
    - 전체 책 목록을 조회하여 출력하는 메서드입니다.

### `void top10List(Connection conn)`
    - 판매량이 높은 상위 10개 책 목록을 조회하여 출력하는 메서드입니다.

### `void searchBook(Connection conn, Scanner scanner)`
    - 사용자가 입력한 책 제목으로 책을 검색하여 결과를 출력하고, 구매 또는 관심 책 추가를 선택할 수 있도록 하는 메서드입니다.

### `void purchaseBook(Connection conn, Scanner scanner)`
    - 사용자가 책을 구매하기 위해 책의 ID를 입력할 수 있도록 하는 메서드입니다.

### `void purchaseBook(Connection conn, Scanner scanner, String bookId)`
    - 특정 책의 ID를 전달받아 해당 책을 구매하기 위한 메서드입니다.

### `void processImmediatePayment(Connection conn, String userId, String bookId, int bookPrice)`
    - 사용자가 선택한 책을 즉시 결제하는 메서드입니다.

### `void processDelayedPayment(Connection conn, String userId, String bookId, int bookPrice)`
    - 사용자가 선택한 책을 나중에 결제하기 위해 대기 상태로 변경하는 메서드입니다.

### `int getCurrentBalance(Connection conn, String userId)`
    - 특정 사용자의 현재 잔액을 조회하는 메서드입니다.

### `void addPurchase(Connection conn, String userId, String bookId, String state)`
    - 사용자의 결제 내역을 데이터베이스에 추가하는 메서드입니다.
