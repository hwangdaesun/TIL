# Spring HTTP Clients

## 왜 공부하는가

- Spring에서 외부 API를 호출하는 대표적인 방법인 `OpenFeign`, `WebClient`, `RestClient`의 차이를 정리하기 위해
- 단순 조회, 선언형 호출, 스트리밍/SSE 같은 상황별로 어떤 도구를 선택해야 하는지 판단하기 위해
- Spring MVC 기반 서비스에서 AI 스트리밍 연동처럼 긴 연결을 다뤄야 할 때 선택 기준을 명확히 하기 위해

## 핵심 개념

- `OpenFeign`
  - 인터페이스 기반의 선언형 HTTP 클라이언트
  - Spring Cloud 생태계에서 많이 사용
  - 코드가 읽기 쉽고, 서비스 간 호출을 추상화하기 좋음
- `WebClient`
  - 비동기, 논블로킹 기반 HTTP 클라이언트
  - reactive 스트림 처리에 적합
  - 토큰 스트리밍, SSE, 고동시성 처리에 유리
- `RestClient`
  - 동기, 블로킹 기반 HTTP 클라이언트
  - Spring Framework의 표준 REST 호출 방식 중 하나
  - 단순 요청-응답 흐름에 적합

## 특징 비교

- 선언형 인터페이스 중심
  - `OpenFeign`
- fluent API 중심
  - `WebClient`, `RestClient`
- 비동기 / 논블로킹
  - `WebClient`
- 동기 / 블로킹
  - `OpenFeign`, `RestClient`

## 선택 기준

- 외부 API를 단순히 한 번 호출하고 결과만 받는다면 `RestClient`
- 서비스 호출을 인터페이스로 추상화하고 싶다면 `OpenFeign`
- 토큰 스트리밍, SSE, 장시간 연결, 논블로킹이 중요하다면 `WebClient`
- Spring MVC에서 AI 응답을 SSE로 중계해야 한다면 `WebClient + SseEmitter` 조합을 우선 검토

## 관련 키워드

- declarative client
- blocking
- non-blocking
- reactive
- SSE
- streaming
- Spring MVC

## 참고할 것

- Spring Framework REST Clients 공식 문서
- Spring WebClient 공식 문서
- Spring Cloud OpenFeign 공식 문서

## 상세 정리

### 1. OpenFeign

- 선언형 인터페이스 기반 HTTP 클라이언트
- `@FeignClient`로 원격 호출 코드를 인터페이스처럼 작성 가능
- 장점
  - 코드가 짧고 읽기 쉬움
  - 호출 대상이 인터페이스로 분리되어 유지보수에 유리
- 단점
  - 기본적으로 블로킹 성격
  - 토큰 스트리밍이나 논블로킹 처리에는 적합하지 않음

### 2. WebClient

- 비동기, 논블로킹 기반 HTTP 클라이언트
- reactive 흐름과 잘 맞고, 응답을 스트림으로 다루기 좋음
- 장점
  - SSE, 스트리밍 응답 처리에 적합
  - 동시 연결이 많을 때 스레드 점유를 줄이기 쉬움
- 단점
  - 사용 방식이 익숙하지 않으면 복잡하게 느껴질 수 있음
  - 중간에 `block()`을 쓰면 장점이 크게 줄어듦

### 3. RestClient

- Spring Framework의 동기, 블로킹 REST 클라이언트
- 단순한 요청-응답 처리에 적합
- 장점
  - API가 단순하고 직관적임
  - 일반적인 REST 호출에 쓰기 좋음
- 단점
  - 응답이 길어지는 스트리밍 시나리오에서는 스레드를 오래 점유함

### 4. 한눈에 비교

| 항목 | OpenFeign | WebClient | RestClient |
|---|---|---|---|
| 스타일 | 선언형 | fluent | fluent |
| 호출 모델 | 동기/블로킹 | 비동기/논블로킹 | 동기/블로킹 |
| 주 사용처 | 서비스 간 호출 | 스트리밍, SSE, reactive | 일반 REST 호출 |
| 장점 | 가독성, 추상화 | 확장성, 스트리밍 적합 | 단순함, 직관성 |
| 주의점 | 스트리밍에 부적합 | `block()` 사용 주의 | 긴 연결에 스레드 점유 |

### 5. 실무에서 볼 포인트

- Spring MVC 기반 AI 챗봇에서 토큰을 SSE로 흘려보내려면 `WebClient`가 유리함
- `RestClient`는 단순 조회나 짧은 요청에 적합함
- `OpenFeign`은 선언형 호출이 필요할 때 좋지만, 스트리밍 중심 설계에는 우선순위가 낮음
- 실제 선택 기준은 “동기냐 비동기냐”보다 “응답이 길어지는가, 스트리밍이 필요한가”가 더 중요함

### 6. Spring MVC + WebClient + SseEmitter 스레드 흐름

- 최초 HTTP 요청은 `Tomcat`의 request thread가 받는다.
- 컨트롤러가 `SseEmitter`를 반환하면 Spring MVC는 async 처리로 전환한다.
- 이 시점에 `Tomcat` request thread는 반환되고, 응답은 열린 상태로 유지된다.
- `WebClient`는 비동기, 논블로킹 방식으로 업스트림 요청을 처리한다.
- 업스트림 응답의 토큰은 `WebClient`의 비동기 처리 흐름에서 수신된다.
- 수신된 토큰은 그 시점의 처리 흐름에서 `emitter.send(...)`로 전달된다.
- `emitter.send(...)`를 호출하는 스레드는 Tomcat request thread로 고정되지 않는다.
- Spring MVC async 응답에서는 응답 write가 별도 스레드에서 수행될 수 있다.

실제로 이러한 흐름인지 테스트 필요!

#### 타임라인

```text
Client -> Tomcat request thread -> Controller
Controller -> SseEmitter 반환 -> Spring MVC async 전환
Tomcat request thread 반환
WebClient -> AI Server 업스트림 요청
WebClient 비동기 흐름에서 토큰 수신
토큰을 emitter.send(...)로 전달
SSE 응답이 Client로 전송
```

#### 단정할 수 있는 범위

- `WebClient`는 blocking 방식이 아니다.
- `SseEmitter`는 MVC async 응답으로 동작한다.
- `Tomcat` request thread는 최초 요청 처리 이후 계속 점유되지 않는다.
- `emitter.send(...)`의 실행 스레드는 구현 방식에 따라 달라질 수 있다.
- 따라서 `emitter.send(...)`가 항상 Tomcat thread에서 실행된다고 볼 수는 없다.


## 남은 질문

- `WebClient`의 connection pool과 timeout은 어떻게 잡는 게 좋은가?
- `OpenFeign`을 여전히 써야 하는 기준은 무엇인가?
