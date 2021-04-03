#  Class Memo

<br/>

## IoC (Inversion of Control)

> 제어의 역전

<br/>

#### 일반적인 의존성에 대한 제어권

```java
class OwnerController {
	private OwnerRepository repository = new OwnerRepository();
}
```

* 자기가 사용할 객체는 new 생성자로 알아서 만들어 쓴다!

<br/>

#### IoC

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
* 이 개념이 바로 의존성 주입 (Dependency Injection) 이다.

<br/>

### IoC 컨테이너

> Application Context (BeanFactory) 라고도 한다.

* 얘가 Bean들을 만들고, Bean들의 의존성을 엮어주며, 제공한다.
* 스프링이 ~을 해준다. 라고 말할 때 그 '스프링'  주체 역할을 해주는게 이 녀석인 듯 하다.
* 얘는 우리가 직접 볼 일도, 직접 쓸 일도 없을것이다...
* 굳이 쓰려면 @Autowired를 이용해 Bean에 주입을 받아서 쓸 수는 있다.

<br/>

### 빈(Bean)

> 스프링 IoC 컨테이너가 관리하는 객체

* 콩...ㅋㅋ
* 스프링 안에서 만들어지는 모든 객체들을 빈이라고 표현한다.
* 다른 말로, Application Context가 관리하는 모든 객체가 Bean이다.

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
    
    	@Bean
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

> @LogExecution Time 으로 메소드 처리 시간 로깅하기

<br/>

<br/>

<br/>

<br/>

<br/>

