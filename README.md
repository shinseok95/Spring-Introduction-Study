# Spring Boot Instruction
> 인프런 김영한(우아한 형제들)님의 **스프링 입문 - 코드로 배우는 스프링 부트, 웹 MVC, DB 접근 기술** 강의 내용 정리 Repository입니다.

# 학습 내용

## 환경설정

**Maven, Gradle** : 필요한 라이브러리를 설치하고 빌드 라이프 사이클까지 관리해주는 툴

## Dependencies
<pre>

Spring Web
Thymeleaf : 템플릿 엔진

logging -> logback / slf4j

서버 배포시 /build/libs에서 jar 파일만 배포하면 됨

</pre>

## 화면을 브라우저에 보여주는 방식

<pre>
정적 컨텐츠 : 단순히 html 파일을 보여주는 것
MVC와 템플릿 엔진 : JSP, PHP처럼 서버에서 가공해서 보여주는 것
API : JSON을 통해 클라이언트에게 데이터를 전달하는 방식 (react, vue.js)

</pre>

### 정적 컨텐츠

<pre>

톰캣에서 요청을 받고 스프링에게 넘김
스프링은 매핑된 컨트롤러가 있는지 찾고, 컨트롤러가 없으면 내부 static에서 찾아서 반환

</pre>

### MVC와 템플릿 엔진

<pre>
View : 화면과 관련된 작업 처리
Controller : 비지니스 로직 & 서버 뒷단 내용 처리

톰캣에서 요청을 받고 스프링에게 넘김
스프링은 매핑된 컨트롤러를 찾고, 로직을 처리 후 모델을  어트리뷰트로 넘겨주고, 리턴값으로는 view 이름을 넘겨줌
viewResolver는 view를 찾아주고 템플릿 엔진을 연결시켜줌
템플릿엔진은 렌더링해서 브라우저에 넘겨줌
</pre>

### API

<pre>
ResponseBody

ResponseBody 어노테이션이 붙어있으면 컨트롤러에서 처리 후, viewResolver가 동작되는 것이 아니라 HttpMessageConverter를 동작 
HttpMessageConverter는 리턴된 값이 String이면 StringHttpMessageConverter를, Object면 MappingJackson2HttpMessageConverter를 실행
그리고 그 값을 http 바디에 넣어서 브라우저에 넘김
 
Java bean 규약 => getter / setter (property 접근방식)
</pre>


## 비즈니스 요구사항

- Cotroller 
  - 웹 MVC의 컨트롤러 역할 (외부 요청을 받음)
- Service
  - 핵심 비즈니스 로직 구현
  - Service 클래스는 비지니스에 의존적인 용어를 사용해야함 (ex: join)
- Repository 
  - 데이터베이스에 접근, 도메인 객체를 DB에 저장하고 관리
  - Repository 클래스는 기계적으로 개발스러운 용어를 사용(ex : insert, select 등)
- Damain
  - 비즈니스 도메인 객체 (ex : 회원, 주문, 쿠폰 등등 주로 데이터베이스에 저장하고 관리됨)

### Optional 클래스

Optional 클래스 -> 어떤 객체를 찾을 때, 없는 경우 null로 반환하기보다 Optional 객체로 감싸서 반환

```java

//ofNullable는 객체를 Optional 객체로 감싸는 함수
@Override
public Optional<Member> findById(Long id) {
 return Optional.ofNullable(store.get(id));
}

// ifPresent는 만약 존재한다면 실행되는 함수
repository.findByName(member.getName())
                .ifPresent(m ->{
                    throw new IllegalStateException("이미 존재하는 회원입니다.")
                });

// get은 감싸진 내부 클래스를 받아오는 함수 
Member result = repository.findById(member.getId()).get()

```

## Test

테스트는 테스트 클래스(jUnit)를 통해 시행<br>
테스트 주도 개발(TDD) : 테스트 케이스를 먼저 제작하고, 구현 클래스를 개발한 후 잘 동작되는지 확인하는 개발 방법

### 테스트 클래스

<pre>

테스트 메소드들은 각자에 의존적이지 않게 개별적으로 실행됨(순서 보장 X)
-> 하나의 메소드가 실행될 때마다 AfterEach를 통해 특정 작업을 수행시켜줘야함

BeforeEach : 메소드별 테스트가 시작되기 전마다 실행되는 callback 메소드 
AfterEach : 메소드별 테스트가 끝날 때마다 실행되는 callback 메소드 

given : 주어진 상황
when : 실행 
then : 결과 체크

</pre>

### Assertions

```java

//JUnit
Assertions.assertEquals(member,result);
// assertj
Assertions.assertThat(member).isEqualTo(result);
```

### 예외상황 테스트


```java

// ① assertThorows 메소드 사용(기대되는 결과, 실제 수행 결과)
assertThrows(IllegalStateException.class, ()->memberService.join(member2));

// ② try~catch 문으로 테스트
try{
 memberService.join(member2);
 fail();

}catch (IllegalStateException e){
 Assertions.assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
}
```

### 스프링 통합 테스트

- @SpringBootTest 
  - 스프링 컨테이너와 테스트를 함께 실행한다
- @Transactional 
  - AfterEach로 지워줄 필요 없이, 테스트 내용이 DB에 반영이 되지 않음 -> 다음 테스트에 영향 X


## Bean과 Dependancy

### 의존성 주입 (DI)

- 필드 주입
  - 잘 사용하지 않음 (해당 Bean을 중간에 바꿀 수 있는 방법이 없음)
- Setter 주입
  - 단점 -> 한번 setting되면 중간에 Bean을 바꿀 일이 거의 없는데, Setter 메서드는 항상 Public으로 열려있어야함
- 생성자 주입
  - 처음 application이 조립될 때, 한번 설정하고 다시 변경 못하도록 막을 수 있음 (의존관계가 실행중에 동적으로 변하는 경우는 거의 없음)

정형화된 Cotroller, Service, Repository(일반적으로 사용하는 코드들) : **컴포넌트 스캔 활용**<br>
정형화되지 않거나, 상황에 따라 구현 클래스를 변경해야하는 클래스 : **Configuration 활용**<br><br>

Autowired를 통한 DI는 Bean으로 등록된 객체에서만 동작<br>

### Component Scan

- ① Component Scan
  - @Component 어노테이션이 있으면 Bean으로 자동 등록
  - @Contoller, @Service, @Repository등은 내부적으로 @Component 어노테이션이 포함되어 있음
  - @Autowired를 사용하면, 객체 생성 시점에 컨테이너에서 Bean을 찾아서 주입시켜줌

```java

@Controller
public class MemberContoroller {

    private final MemberService memberService;

    @Autowired
    public MemberContoroller(MemberService memberService) {
        this.memberService = memberService;
    }
}

@Service
public class MemberService {

    private MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
}

@Repository
public class MemoryMemberRepository implements MemberRepository{~}

```

### Configuration

- ② Configuration
  - @Configuration 클래스를 통해 Bean을 직접 등록

```java
@Configuration
public class SpringConfig {

    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }
}
```


## Mapping

- GetMappind
  - URL을 통한 조회때 주로 사용
  - ex) @GetMapping("/Members/new")

- PostMapping
  - 보통 데이터를 Form를 통해 전달할 때 주로 사용
  - ex) @PostMapping("/Members/new")

## JDBC

- Connection 
  - DB와 연결하는 역할
- PreparedStatement
  - SQL 구문 실행 역할
- ResultSet
  - 결과값을 받아오는 역할
<br><br>

- DataSourceUtils : DB 트랜잭션 관련 기능을 알아서 해줌
  - DataSourceUtils.getConnection(dataSource) : DB 연결
  - DataSourceUtils.releaseConnections(conn,dataSource) : DB 연결 해제 (DB를 사용할 때는, 사용 후 반드시 연결을 끊어줘야함)<br>

<pre>

객체지향의 다형성을 극대화해서 사용 가능 => Repository가 변경되더라도 Service에서 코드 수정없이 변경 가능
즉, 스프링의 DI를 사용하면, 기존 코드를 전혀 손대지 않고, 설정만으로 구현 클래스를 변경할 수 있다.
OCP(Open-Close Principle) : 확장에는 열려있고, 수정에는 닫혀있다
</pre>

```java

public Member save(Member member) {

        String sql = "insert into member(name) values(?)";

        Connection conn = null; // DB와 연결하는 역할
        PreparedStatement pstmt = null; // SQL 구문 실행 역할
        ResultSet rs = null; // 결과값을 받아오는 역할

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, member.getName());
            
            pstmt.executeUpdate();
            
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                member.setId(rs.getLong(1));
            } else {
                throw new SQLException("id 조회 실패");
            }
            return member;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs); }
    }
```

## JDBC Template

- JDBC Template 라이브러리 (or MyBatis)
  - JDBC에서 반복되는 코드를 대부분 제거해줌(SQL은 직접 작성)

```java

// Create
// 대신 Query를 작성해줌
SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
jdbcInsert.withTableName("member").usingGeneratedKeyColumns("id");

Map<String,Object> parameters = new HashMap<>();
parameters.put("name",member.getName());

Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

// Select
// connection, preparedStatement, resultSet 에 대한 코드를 모두 자동으로 만들어줌
List<Member> result = jdbcTemplate.query("select * from member where id=?",memberRowMapper(),id);
        return result.stream().findAny();
        
```

## JPA

- ORM(Object-Relational Mapping)
  - 객체는 객체대로 설계하고, 관계형 DB는 관계형 DB대로 설계 -> ORM이 중간에 매핑

- JPA(Java Persistence API) 
  - 자바의 ORM 기술 표준 
  - Hibernate : ORM 프레임워크 (open source)

<pre>
JPA는 기존의 반복 코드(conn, ps, rs)뿐만 아니라 기본적인 SQL도 직접 만들어서 실행

JPA를 통해 SQL & 데이터 중심 설계 -> 객체 중심 설계로 패러다임 전환

jpa는 모든 데이터 변경이 트랜젝션 안에서 실행되어야 함 (Service 클래스에 @Transactional을 붙여줘야함)
</pre>

- 테이블 : 객체에 @Entity 어노테이션을 붙이면 테이블처럼 사용 가능
- 테이블 관리(CRUD) : EntityManager 객체를 통해 모든 관리 가능


```java
// Create
em.persist(member)
// Read
List<Member> result = em.createQuery("select m from Member m where 
m.name = :name", Member.class)
 .setParameter("name", name)
 .getResultList();
// Update
member.setName("Spring")
// Delete
em.remove(member)
       
```

## 스프링 데이터 JPA

JPA 기능을 구현 클래스 없이 인터페이스 기반으로 사용 가능

- 기능
  - 인터페이스를 통한 기본적인 CRUD
  - findByName() , findByEmail() 처럼 메서드 이름 만으로 조회 기능 제공
  - 페이징 기능 자동 제공

```java
public interface SpringDataJpaMemberRepository extends JpaRepository<Member,
Long>, MemberRepository {
 Optional<Member> findByName(String name);
}
       
```

## AOP

- AOP(Aspect Oriented Programming : 관점 지향 프로그래밍)
  - 어떤 로직을 기준으로 공통 관심 사항(cross-cutting-concern)과 핵심 관리 사항(core concern)을 분리하여 모듈화
  - AOP를 적용하면, 실행되기 전까지 실제 객체 대신 Proxy 객체가 등록됨

```java

// AOP 설정
@Aspect
@Component
public class TimeTraceAop {

    // 어디에 적용시킬건지 설정
    @Around("execution(* hello.hellospring..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        System.out.println("START: " + joinPoint.toString());
        try {

            // 핵심 관심 사항이 여기서 실행됨
            return joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("END: " + joinPoint.toString()+ " " + timeMs +
                    "ms");
        }
    }
}
}
       
```


## IntelliJ 단축키

<pre>
클래스 및 패키지 만들기 -> 패키지에 마우스 올리고 Alt+insert
getter and setter 만들기 -> 클래스 내부에서 Alt+insert
Test 만들기 -> 클래스 내부에서 Alt+insert
이름 한꺼번에 만들기 -> ctrl + r  -> r
extract method -> ctrl + r -> m
</pre>
