# Class Memo

<br/>

#### 요약

* 스프링의 핵심은 IoC, AOP, PSA 이다.
* IoC에서 의존성 주입은, 필요한 객체를 외부에서 스프링이 넣어주는 방식이다. 
  * 테스트도 쉽고, 코드 작성이 용이하다.
* AOP는 사방으로 흩어져서 작성된 코드를 한곳으로 모으는 코딩 기법이자 기술이다.
  * logging이나, 트랜잭션, 성능측정, 인증, 권한 확인 등...
  * 그 중 spring은 proxy 라는 패턴을 이용해서 구현한다.
  * 또는 byte code를 변경해서 구현하기도...
* 대부분의 라이브러리는 전부 PSA이고, Spring이 제공하는 API 대부분이 PSA이다.
  * 조금 더 유연하게 대처할 수 있는 추상화 된 좋은 인터페이스를 제공하는 것이다.

<br/>

<br/>

## 프로젝트 준비

* JDK 버전: 11 이상

* 소스 코드: https://github.com/spring-projects/spring-petclinic 

* 실행 방법

  > ./mvnw package

  * package 라는 빌드를 수행하면, 프로젝트를 빌드 해서 패키지 파일을 만든다.
  * 패키징이라는 옵션으로 타입을 지정해주지 않으면 default 프로젝트 타입은 jar 파일

  > java -jar target/*jar

  * 현재 디렉토리 하위에서 jar 파일을 찾아 실행하는 명령어

  > IDE에서 메인 애플리케이션 실행

  * Spring boot 기반 프로젝트이기 때문에, Application java 파일을 run 하는 방법으로 가능

<br/>

<br/>

### 프로젝트 살펴보기

<br/>

#### 프로젝트 Flow가 어떻게 될까?

* 예시로, home에서 새로 고침을 한다.
* 그러면 owners/find 라는 요청이 오면, spring의 디스패처 서블릿으로 가게 된다.
* Dispacher Servlet은 ownerController에 있는 매핑된 메소드를 호출하게 된다.

<br/>

#### 기존 코드를 조금 고쳐보기

* LastName이 아닌 FirstName으로 검색하도록
  * View 변경
  * 코드 조금 변경
* 앞에서부터 일치하는게 아닌 해당 키워드가 들어있는 걸 검색하도록
  * 쿼리 일부 변경
* Owner에 age 항목 추가
  * Model 변경
  * DB Schema 및 Data 변경
  * View 변경

<br/>

#### Issue

* SQL column 문제
  * DB 스키마 및 데이터 파일을 수정해주어 해결했다.
* No validator could be found for constraint 'java.lang.Integer'
  * @NotEmpty 어노테이션을 문자열이 아닌 Integer 형에 써서 그런 것 아니었을까 싶다.
* View 에서 추가한 age의 default value="0" 으로 계속 나오는 문제
  * Owner의 age 필드를 int형으로 선언해줘서 그랬던 것 같다. Integer로 수정하니 해결

<br/>

<br/>

## 스프링 IoC

### IoC (Inversion of Control)

> 제어권이 역전된 것

<br/>

#### 일반적인 의존성에 대한 제어권

```java
class OwnerController {
	private OwnerRepository repository = new OwnerRepository();
}
```

* 자기가 사용할 객체는 new 생성자로 알아서 만들어 쓴다!
* OwnerReopsitory를 OwnerController 본인이 사용하기 위해 직접 만드는 것.
* 의존성에 대한 제어권을 스스로가 가지고 있다.

<br/>

#### IoC, 제어권의 역전

```java
class OwnerController {
	private OwnerRepository repo;
	
    public OwnerController(OwnerRepository repo) {
		this.repo = repo;
	}
	// repo를 사용합니다.
}

class OwnerControllerTest {
	@Test
	public void create() {
		OwnerRepository repo = new OwnerRepository();
		OwnerController controller = new OwnerController(repo);
	}
}
```

* 자기가 사용할 객체는 타입만 맞으면 외부에서 알아서 넣어준다.
* 위의 경우는 OwnerController에서 사용할 OwnerRepository를 외부에서 넣어준다.
* 이로써 의존성을 관리해주는 것이 자신이 아니라 다른 누군가가 해주는 것으로 바뀌었다.
* 이 개념이 바로 의존성 주입 (Dependency Injection) 이다.

<br/>

* 이렇게 스프링은 Bean이라는 객체를 만들어 객체로 등록을 해준다.
* 등록된 객체들은 Spring Container 안에 있으니까 Bean 이라고 부르는 것.
* 이 Bean들의 의존성을 관리해준다. 즉, 필요한 의존성을 서로 주입 해주는 역할을 수행하는 것이다.
  * 의존성 주입은 Bean 끼리만 가능하다.

<br/>

<br/>

### 스프링 IoC 컨테이너

> = Application Context (BeanFactory) 라고도 한다.

* Bean을 만들고, Bean 사이의 의존성을 엮어주며, 컨테이너가 가지고 있는 Bean을 제공한다.
* 스프링이 ~을 해준다. 라고 말할 때 그 '스프링'  주체 역할을 해주는게 이 녀석.

<br/>

#### ApplicationContext, BeanFactory

* 해당 코드는 가장 핵심적인 클래스이긴 하지만, 우리가 직접 참고해서 쓸 일이 거의 없다.
* 굳이 쓰려면 @Autowired를 이용해 Bean에 주입을 받아서 쓸 수는 있다.
* BeanFactory가 사실상 IoC 컨테이너이고, ApplicationContext는 BeanFactory를 상속받고 있다.
* 같은 역할을 하지만, ApplicationContext가 훨씬 더 다양한 일을 하긴 한다.

<br/>

#### 직접 사용하기

```java
private ApplicationContext applicationContext;

public OwnerController(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
}

@GetMapping("/bean")
@ResponseBody
public String bean() {
	return "bean: " + applicationContext.getBean(OwnerController.class);
}
```

* 위의 경우와 같이, 스프링에서 제공하는 ApplicationContext를 직접 선언 및 주입받아 사용할 수도 있긴하다.
* 하지만 이렇게 getBean의 형식으로 직접 가져오는 식의 코딩을 할 일은 거의 전혀 수준으로 없다고...
* 직접 쓸 필요 없이 어차피 스프링이 알아서 주입 해주기 때문.

<br/>

#### Bean은 싱글톤 객체

```java
private final OwnerRepository owners;
private ApplicationContext applicationContext;

public OwnerController(OwnerRepository clinicService, ApplicationContext applicationContext) {
	this.owners = clinicService;
	this.applicationContext = applicationContext;
}

@GetMapping("/bean")
@ResponseBody
public String bean() {
	return "bean: " + applicationContext.getBean(OwnerRepository.class) + "\n"
		+ "owners: " + this.owners;
}
```

* 직접 생성한 ApplicationContext에서 getBean()을 이용해 가져온 OwnerRepository 클래스를 출력한다.
* 스프링에 의해 주입 받은 owners 객체 변수를 출력한다.
* 출력된 각 클래스의 Hash 값은 당연히 같다.
* 스프링 IoC 컨테이너에 등록된 Bean 객체는 싱글톤으로 하나만 존재하고, 재사용되기 때문.

<br/>

#### 참고

* [application context에 대한 설명 - github](https://github.com/spring-guides/understanding/tree/master/application-context)
* [ApplicationContext에 대한 설명 - 공식 Reference](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/ApplicationContext.html)
* [BeanFactory에 대한 설명 - 공식 Reference](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/beans/factory/BeanFactory.html)

<br/>

<br/>

### 빈(Bean)

> 스프링 IoC 컨테이너가 관리하는 객체

* 콩...ㅋㅋ
* 스프링 안에서 만들어지는 모든 객체들을 빈이라고 표현한다.
* 다른 말로, Application Context가 관리하는 모든 객체가 Bean이다.
* 스프링 IoC 컨테이너 안에서 관리되는 모든 객체가 Bean이다.
  * IntelliJ IDE에서는, 좌측에 녹색 콩 모양이 떠있으면 Bean으로 등록되어 관리되는 클래스들이다.

<br/>

#### Bean으로 등록하는 방법

* Component Scanning
  * 컴포넌트 스캔은 Annotation을 처리하는 역할을 한다.
  * Application이 존재하는 패키지 하위 모든 클래스들을 돌면서 @Component 어노테이션이 달린 것들을 찾아 Bean으로 등록해준다.
  * @Component에 해당하는 어노테이션들 예시
    * @Repository
    * @Service
    * @Controller

<br/>

* 직접 일일이 XML이나 자바 설정 파일에 등록

  * @Bean 어노테이션을 이용해 직접 정의할 수 있다.

  * 단, @Configuration 어노테이션이 있는 파일에 정의해야 한다

  * ```java
    @SpringBootApplication(proxyBeanMethods = false)
    public class PetClinicApplication {
    
    	@Bean // 어노테이션
    	public String sanggoe() {
    		return "sanggoe";
    	}
    
    	public static void main(String[] args) {
    		SpringApplication.run(PetClinicApplication.class, args);
    	}
    
    }
    ```

<br/>

#### Bean을 꺼내 쓰는 방법

* @Autowired 또는 @Injection
* ApplicationContext에서 getBean()으로 직접 꺼내는 방법
  * 이건 별로 권장하지 않는 방법

<br/>

#### 특징

* 오로지 "빈" 만 의존성을 관리 한다. 빈이 아닌 것들은 의존성을 관리하지 않는다.
* 클래스는 Bean 이 되어야 해요~

<br/>

### 의존성 주입 (Dependency Inection)

> 필요한 의존성을 어떻게 받아올 것인가?

<br/>

#### @Autowired / @Inject를 어디에 붙이는가?

* Constructor
  * 어떠한 클래스에 반드시 필요한 의존성이면 생성자에 붙이는 것을 추천!
  * Bean이 되는 클래스에 **생성자가 하나만 있고**, 생성자에 **매개변수 Type이 빈으로 등록되어 있다면** @Autowired가 없더라도 그 **Bean을 주입해준다.**
    * 다른 말로, 위의 경우라면 Autowired나 Inject를 생략할 수 있다.
* Setter
  * 여기에다가도 붙일 수 있다. 우선 순위는 두 번째.
  * 하지만 Setter를 이용해 의존성을 주입하는 방법은 다소 위험한 코드가 될 수 있다.
  * 생성자로 의존성을 주입하면 한 번 생성한 이후로 바뀔 염려가 없지만, Setter를 이용하면 외부에서 주입이 다시 일어나는 등의 변동이 생길 우려가 있기 때문.
* Field
  * Setter도 없다면, 필드 위에다가도 @Autowired를 붙일 수 있다.

<br/>

<br/>

## AOP (Aspect Oriented Programming)

> 흩어진 코드를 한곳으로 모으는 코딩 기법

<br/>

* 흩어진 AAAA와 BBBB

```java
class A {
	method a () {
		AAAA
		오늘은 7월 4일 미국 독립 기념일이래요.
		BBBB
	}
	method b () {
		AAAA
		저는 아침에 운동을 다녀와서 밥먹고 빨래를 했습니다.
		BBBB
	}
}
class B {
	method c() {
		AAAA
		점심은 이거 찍느라 못먹었는데 저녁엔 제육볶음을 먹고 싶네요.
		BBBB
	}
}
```

<br/>

* 모아 놓은 AAAA와 BBBB

```java
class A {
	method a () {
		오늘은 7월 4일 미국 독립 기념일이래요.
	}
	method b () {
		저는 아침에 운동을 다녀와서 밥먹고 빨래를 했습니다.
	}
}
class B {
	method c() {
		점심은 이거 찍느라 못먹었는데 저녁엔 제육볶음을 먹고 싶네요.
	}
}
class AAAABBBB {
	method aaaabbb(JoinPoint point) {
		AAAA
		point.execute()
		BBBB
    }
}
```

<br/>

* 흩어져있던 코드의 경우, 같은 역할을 하는게 난잡하게 흩어져있다.
* 이걸 한 곳으로 모아서 흩어진 코드를 호출 하고, 각 메소드를 호출하도록 기능을 모아서 정리해 놓는 것이다. 게다가 객체지향 기법에 맞게 메소드는 자신이 해야할 일만 수행하도록 할 수 있다.
* 바이트 코드를 조작하는 방법, 프록시 패턴을 사용하는 방법이 있다.

<br/>

### AOP 적용 예제

* 예제 코드에서는 @Transactional 어노테이션이 AOP에 해당하는 코드들이다.
* 해당 메소드의 트랜잭션 모두 try catch 처럼 묶여있다고 한다.
* 문제가 발생할 경우 수행되는 catch 에서의 트랜잭션이 위에서 살펴본 AAAA, BBBB 형식처럼 동일하게 둘러쌓여 있고, 따라서 기본적으로 제공되는 메소드들은 트랜잭션이 다 적용되어 있다고...
  * 솔직히 아직은 무슨말인지 잘 모르겠다 (?)

<br/>

#### 어떤 메소드가 실행이 되었을 때, 그 시간을 로그로 남기는 예제

> @LogExecutionTime 으로 메소드 처리 시간 로깅하기

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {
}
```

* 어디에 적용할지 표시해두는 용도. 메소드를 타겟으로 한다.
* 적용 시기에 대한 정책은 RUNTIME 까지 하도록 해야 스프링이 수행되는 동안 로그가 남는다고 한다.

<br/>

<br/>

## PSA(Portable Service Abstraction) 소개

> 잘 만든 인터페이스
>
> 이식 가능한, 바꿔끼기 좋은 서비스 추상화

<br/>

* 내가 작성한 코드가 확장성이 좋지 못한 코드이거나, 기술에 특화되어 있는 코드일 경우
  * 테스트 만들기도 어렵다.
  * 어떤 기술이 바꿀 때마다 내 코드가 바뀐다.
* 나의 코드가 잘 만든 인터페이스일 경우라면
  * 테스트하기도 좋다.
  * 다른걸로 바꿔 끼기도 좋다.
  * 해당 인터페이스 아래에 있는 기술 자체를 바꾸더라도, 내 코드가 바뀌지 않는다.
    * JDBC - Hibernate - JPA 기술이 바뀌어도!!

<br/>

#### Spring은 어떤 PSA를 제공하는가?

* **스프링이 제공하는 대부분의 API가 전부 다 PSA이다.**
* 다 추상화 된 엄청나게 호환성 좋은... 다른 기술들이 들어와도 기존 코드가 바뀌지 않는 90%가 추상화이다.
* **추상화된 특정 인터페이스만 잘 알면, 뒷단에 무슨 기술이 있든지 상관 없이 잘 쓸 수 있는 것!**
* 세상에 날고긴다는 짱짱 개발자분들께서 스프링을 만드셨는데 당연하지...

<br/>

### 스프링 트랜잭션

> PlatformTransactionManager

<br/>

* @Transactional
* 트랜잭션을 처리하는 Aspect는, PlatformTransactionManager 이라는 인터페이스를 사용해서 코딩한다. 
* 그렇기 때문에 Bean이 바뀌더라도 Transaction Annotation을 처리하는 코드는 바뀌지 않는다.
  * JpaTransactionManager | DatasourceTransactionManager | Hibernate TransactionManager
* 이것이 추상화!

<br/>

### 스프링 캐시

> CacheManager

<br/>

* @Cacheable | @ChacheEvict | ...
* CacheManager 라는 인터페이스를 사용해야 한다.
* 그럼 마찬가지로 Bean이 바뀌어도 처리하는 코드가 바뀔 일은 없다.
* JCacheManager | ConcurrentMapCacheManager | EhCacheCacheManager | ...

<br/>

### 스프링 웹 MVC

> @Controller와 @RequestMapping

<br/>

* 마찬가지로 우리 코드는 추상화 되어있기 때문에 하위 어떤 기술을 사용하더라도 변경되지 않는다.
* @Controller | @ReuqestMapping | ...
* 의존성을 확인해 보아야 어떤 것을 쓰는지 알 수 있을 것이다.
  * Servlet | Reactive
* 톰캣, 제티, 네티, 언더토우

<br/>