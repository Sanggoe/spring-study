### 어노테이션

#### @SpringBootApplication

* 스프링이 실행되기 위한 파일이라는 의미의 어노테이션.
* Main 메소드가 들어있는 코드, 어플리케이션을 나타낸다.

#### @Component

* 컴포넌트 스캐닝을 통해 자동으로 스프링 빈에 등록하기 위해서 사용하는 어노테이션이다.
* Controller, Service, Repository 등의 내부에도 들어가있다.

#### @Controller

* spring container에, 해당 객체를 생성해서 스프링에 넣어두고, 관리하기 위한 어노테이션이다.
* Controller를 통해서 외부 요청을 받아 처리한다.

#### @Service

* spring이 올라올 때, 해당 객체를 spring container에 등록해 관리하기 위한 어노테이션이다.
* Service를 통해서 비스니스 로직을 만들어 수행한다.

#### @Repository

* 마찬가지로 해당 객체를 spring container에 등록해 관리하기 위한 어노테이션이다.
* Repository를 통해서 데이터를 저장한다.

#### @Autowired

* 생성자에 붙여 spring container의 객체와 연결시키기 위한 어노테이션이다.
* spring container에, 자동으로 컨트롤러를 등록하면서 필드로 존재하는 객체의 생성자도 호출한다.
* 그러면 앞서 service로 등록해 spring container 안에 존재하는 객체를 가져다가 연결시켜준다.
* 생성자가 하나일 경우, 해당 어노테이션을 생략할 수 있다고 한다!

#### @Bean

* 자바 코드로 스프링 빈을 직접 등록하기 위해 생성자 위에 써주는 어노테이션이다.
* 해당 어노테이션이 있는 클래스에 대해 객체를 만들어 스프링 빈으로 등록해주는 것.
* 그러나 Controller의 경우 자바 코드로 직접 등록하지 않고, 컴포넌트 스캔을 이용한다.

#### @Test

* junit에 속한 것으로, 테스트 하는 메소드라는 의미로 붙여주는 어노테이션이다.

#### @AfterEach

* 각 테스트 메소드가 수행된 후마다 호출되는 메소드라는 의미로 붙여주는 어노테이션이다.

#### @BeforeEach

* 각 테스트 메소드가 수행되기 전에 매번 호출하는 메소드라는 의미로 붙여주는 어노테이션이다.

#### @Entity

* JPA가 관리하는 Entity가 된다는 의미로 붙여주는 어노테이션이다.

#### @Id

* Primary Key로 설정한다는 의미의 어노테이션이다.

#### @GeneratedValue(strategy = GenerationType.IDENTITY)

* Identity : DB가 알아서 ID를 생성해주는 전략 

#### @GetMapping(path)

* 해당 경로로 get 방식으로 요청이 오면 수행할 메소드를 매핑하는 어노테이션이다.

#### @PostMapping(path)

* 해당 경로로 Post 방식으로 요청이 오면 수행할 메소드를 매핑하는 어노테이션이다.

#### @Target

#### @Retention

