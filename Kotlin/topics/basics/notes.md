# Notes

## 한 줄 요약

- Kotlin은 JVM 위에서 동작하면서 null-safety, 간결한 데이터 표현, 함수형 스타일의 컬렉션 처리를 언어 차원에서 지원한다.

## 상세 정리

### 1. Null-safety

- nullable 타입은 `String?`처럼 `?`를 붙여 표현한다.
- null 가능성이 있는 값은 safe call `?.`, Elvis operator `?:`, 명시적 검사로 다룬다.

### 2. Data class

- `data class`는 값 객체를 만들 때 자주 쓰며 `equals`, `hashCode`, `toString`, `copy`를 자동 생성한다.
- DTO, command, query result처럼 상태를 담는 타입에 적합하다.

### 3. Sealed class/interface

- 제한된 하위 타입 집합을 표현할 때 사용한다.
- `when`과 함께 쓰면 케이스 누락을 줄일 수 있다.

### 4. Extension function

- 기존 타입을 수정하지 않고 함수 호출 형태의 API를 추가할 수 있다.
- 공통 변환, 검증, 포맷팅 로직을 표현할 때 유용하다.

### 5. Collection API

- `map`, `filter`, `groupBy`, `associateBy` 같은 함수로 컬렉션 변환을 간결하게 표현한다.
- 기본 컬렉션 인터페이스는 읽기 전용과 변경 가능 타입을 구분한다.

## 예시

```kotlin
data class User(val id: Long, val name: String?)

fun User.displayName(): String = name ?: "Unknown"

fun main() {
    val users = listOf(User(1, "Dustin"), User(2, null))
    val names = users.map { it.displayName() }

    println(names)
}
```

## 남은 질문

- Kotlin에서 Java nullable API를 다룰 때 platform type을 어떻게 관리할까?
- Spring에서 Kotlin data class를 Entity로 써도 괜찮을까?

