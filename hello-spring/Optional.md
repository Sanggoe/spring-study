### Optional\<T\>

* Java 8에서 추가된 기능
* 메소드를 호출해 반환하는 값이 NULL인 경우가 생길 수 있다.
* NULL 값인 경우에 대해 예외적으로 처리하는 것 역시 개발에서 중요한 이슈 중 하나이다.
* 요즘에는 그 NULL 값을 그대로 반환하는 대신에 이 Optional로 감싸서 반환하는 방법을 선호한다.

<br/>

<br/>

### Method

#### boolean empty(Object obj)

* 인자로 넘겨준 객체가 같은지 여부를 반환한다.

<br/>

#### T get()

* Optional 객체에 저장된 값을 반환한다.

<br/>

#### void ifPresent(Consumer\<? super T\> consumer)

* 값이 있으면 람다함수를 호출한 결과값을 호출하고, 그렇지 않으면 아무 작업도 수행하지 않는다.

<br/>

#### static\<T\> Optional\<T\> ofNullable(T value)

* 명시된 값이 null이 아닌 경우 Optional 객체를 return 하고, null이면 빈 Optional 객체를 반환한다.
* (그럼 클라이언트 측에서 이를 감지하여 NULL 값에 대한 처리를 해줄 수 있다)

<br/>

#### public T orElseGet(Supplier\<? extends T> other)

* 저장된 값이 존재하면 값을 반환하고, 존재하지 않으면 인수로 전달한 람다식의 결과값을 반환한다.

<br/>

### Ref

* [Optional : null을 대하는 새로운 방법](https://www.daleseo.com/java8-optional-before/)
* [Java8 공식 레퍼런스](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html#orElseGet-java.util.function.Supplier-)

