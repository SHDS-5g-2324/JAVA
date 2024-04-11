# 신한DS 금융SW 아카데미 :: 인터넷 서점 JAVA - ORACLE 구현
## 1. 개발 환경
### 프로그래밍 언어 : Java 17
### 사용 라이브러리 : Lombok, Ojdbc
### 사용 DB : Oracle DB 11g
### 협업 툴 : Github, Slack
## 2. 기술 목표
### 객체지향 프로그래밍
- 단일 책임 원칙
- DTO, Controller, Service 등 하나의 객체는 하나의 역할만 수행할수 있도록 분리함
### 디자인 패턴
#### MVC 패턴
- 각 도메인에 대해서 Model, View, Controller 로 나누어서 기능 구현
#### Builder 패턴
- 각 엔티티는 롬복과 빌터패턴을 이용하여서 엔티티를 생성합니다.
#### 싱글톤 패턴
- 직접 DB에 접근하는 유일한 객체인 DataConnectionManager 객체를 싱글톤으로 사용하여, DB연결 인스턴스의 다중 생성을 방지하고, 재사용하여 리소스 사용을 최적화
#### 예외 처리
- 사용자 입력을 받는 다양한 상황에서, 의도하지 않은 입력으로 시스템에 문제가 생기는 것을 방지하기 위해, 문자열 가공 및 정제과정을 구현하고 자바가 제공하는 Exception을 활용
#### 단위 테스트
- 테스트코드를 이용하여 DB연결 상황을 가정하고 테스트를 진행

# 3. 프로젝트 설계과정
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

---------

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


## Create.java
### `void bookList(Connection conn)`
- 이 메서드는 서점의 모든 책 목록을 검색하여 출력합니다.

### `void top10List(Connection conn)`
- 이 메서드는 판매량이 높은 상위 10권의 책 목록을 검색하여 출력합니다.

### `void searchBook(Connection conn, Scanner scanner)`
- 이 메서드는 사용자가 책 제목을 입력하여 책을 검색하고, 검색 결과를 출력하며, 구매 또는 관심 책으로 추가할 수 있도록 합니다.

### `void purchaseBook(Connection conn, Scanner scanner)`
- 이 메서드는 사용자가 책의 ID를 입력하여 구매할 수 있도록 합니다.

### `void purchaseBook(Connection conn, Scanner scanner, String bookId)`
- 이 메서드는 특정 책의 ID를 받아 해당 책을 구매하는 데 사용됩니다.

### `void processImmediatePayment(Connection conn, String userId, String bookId, int bookPrice)`
- 이 메서드는 사용자가 선택한 책을 즉시 결제할 수 있도록 합니다.

## Main.java
### `void main(String[] args)`
- 이 메서드는 사용자가 프로그램을 실행할 때 호출되며, 로그인, 회원가입, 종료 등의 메뉴를 제공합니다.

### `static boolean login(Connection conn, Scanner scanner)`
- 이 메서드는 사용자의 아이디와 비밀번호를 입력받아 로그인하는 기능을 담당합니다.

### `static void processMenu(Connection conn, Scanner scanner)`
- 이 메서드는 로그인 후 메뉴를 선택하여 마이페이지, 책 목록 등의 기능을 수행합니다.

## Update.java
### `public static void updateData(Connection conn, Scanner scanner, String userId) throws SQLException`
- 이 메서드는 사용자의 비밀번호를 업데이트합니다.
  - `Connection conn`: 데이터베이스 연결을 위한 Connection 객체
  - `Scanner scanner`: 사용자 입력을 위한 Scanner 객체
  - `String userId`: 사용자의 ID
  - 예외 처리: SQLException 발생 가능

### `public static void updateMoney(Connection conn, Scanner scanner, String userId) throws SQLException`
- 이 메서드는 사용자의 잔액을 업데이트합니다.
  - `Connection conn`: 데이터베이스 연결을 위한 Connection 객체
  - `Scanner scanner`: 사용자 입력을 위한 Scanner 객체
  - `String userId`: 사용자의 ID
  - 예외 처리: SQLException 발생 가능

