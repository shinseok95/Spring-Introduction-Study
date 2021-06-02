# Spring Boot Instruction
> 인프런 김영한(우아한 형제들)님의 **스프링 입문 - 코드로 배우는 스프링 부트, 웹 MVC, DB 접근 기술** 강의 내용 정리 Repository입니다.

# 학습 내용

## 환경설정

Maven, Gradle : 필요한 라이브러리를 설치하고 빌드 라이프 사이클까지 관리해주는 툴

## Dependencies

Spring Web
Thymeleaf : 템플릿 엔진

logging -> logback / slf4j

배포시 /build/libs에서 jar 파일만 배포하면 됨

## Spring 기초

정적 컨텐츠 : 단순히 html 파일을 보여주는 것
MVC와 템플릿 엔진 : JSP, PHP처럼 서버에서 가공해서 보여주는 것
API : JSON을 통해 클라이언트에게 데이터를 전달하는 방식 (react, vue.js)


### 정적 컨텐츠

톰캣에서 요청을 받고 스프링에게 넘김
스프링은 매핑된 컨트롤러가 있는지 찾고, 컨트롤러가 없으면 내부 static에서 찾아서 반환

### MVC와 템플릿 엔진

**View : 화면과 관련된 작업 처리**
**Controller : 비지니스 로직 & 서버 뒷단 내용 처리**

톰캣에서 요청을 받고 스프링에게 넘김
스프링은 매핑된 컨트롤러를 찾고, 로직을 처리 후 모델을  어트리뷰트로 넘겨주고, 리턴값으로는 view 이름을 넘겨줌
viewResolver는 view를 찾아주고 템플릿 엔진을 연결시켜줌
템플릿엔진은 렌더링해서 브라우저에 넘겨줌

### API

**ResponseBody**

ResponseBody 어노테이션이 붙어있으면 컨트롤러에서 처리 후, viewResolver가 동작되는 것이 아니라 HttpMessageConverter를 동작 
HttpMessageConverter는 리턴된 값이 String이면 StringHttpMessageConverter를, Object면 MappingJackson2HttpMessageConverter를 실행
그리고 그 값을 http 바디에 넣어서 브라우저에 넘김
 
Java bean 규약 => getter / setter (property 접근방식)
