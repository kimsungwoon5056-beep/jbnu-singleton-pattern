# Singleton 패턴 구현 및 단일 인스턴스 확인

초급프로젝트 일반 분반 11주차 실습 과제 · 202422745 김성운

프로그램 전체에서 하나만 존재해야 하는 설정 객체 `AppSettings`를 Singleton으로 구현하고, 두 변수가 실제로 같은 객체를 가리키는지와 상태가 공유되는지를 실행 결과로 확인했습니다.

## 언어와 실행 환경

| 항목 | 내용 |
|---|---|
| 언어 | Java |
| JDK | OpenJDK 21.0.10 |
| 운영체제 | Windows 11 |

JDK 17 이상이면 같은 명령으로 실행할 수 있습니다. 단일 프로세스·단일 스레드 환경을 기준으로 합니다.

## 파일 구성

| 파일 | 역할 |
|---|---|
| `src/AppSettings.java` | Singleton 구현 코드 |
| `src/Main.java` | 동일 인스턴스와 상태 공유를 확인하는 실행 코드 |

## 실행 방법

저장소 최상위 폴더에서 다음 명령을 실행합니다.

```bash
javac -encoding UTF-8 -d out src/AppSettings.java src/Main.java
java -cp out Main
```

## 구현 내용

```java
public final class AppSettings {
    private static AppSettings instance;   // 유일한 인스턴스를 보관

    private AppSettings() { ... }          // 클래스 밖에서 new 금지

    public static AppSettings getInstance() {
        if (instance == null) {
            instance = new AppSettings();  // 처음 요청할 때만 생성
        }
        return instance;                   // 이후에는 같은 객체 반환
    }
}
```

- **인스턴스 보관:** 유일한 객체를 `private static` 필드 `instance`에 보관합니다.
- **생성 제한:** 생성자를 `private`으로 선언해 클래스 밖에서는 `new AppSettings()`를 쓸 수 없습니다.
- **인스턴스 얻기:** 객체는 `getInstance()`로만 얻을 수 있습니다.
- **공유 상태:** 방문 횟수(`visitCount`)와 테마(`theme`)를 두었습니다.
- **생성 횟수 확인:** 생성자가 몇 번 호출됐는지 세는 `createdCount`로 객체가 한 번만 만들어졌는지 확인합니다.

## 실행 결과

```text
[1] Same instance check
first == second           : true
identityHashCode(first)   : 2061475679
identityHashCode(second)  : 2061475679
constructor call count    : 1

[2] Shared state check
before: second.visitCount = 0, second.theme = light
changed through first: visitCount +3, theme = dark
after : second.visitCount = 3, second.theme = dark

[3] Request again
third == first            : true
third.visitCount          : 3
constructor call count    : 1
```

- **[1] 동일 인스턴스:** `getInstance()`로 받은 `first`와 `second`를 참조 비교(`==`)한 결과가 `true`입니다. 생성자도 한 번만 호출되었습니다.
  - `identityHashCode`는 실행할 때마다 값이 달라질 수 있습니다.
  - 같은 실행 안에서 두 값이 같은 것은 보조 근거일 뿐이고, 같은 객체라는 판단은 `==` 결과로 합니다.
- **[2] 상태 공유:** `first`로 방문 횟수를 3 올리고 테마를 `dark`로 바꾸자, `second`로 읽은 값도 `3`, `dark`로 바뀌었습니다.
- **[3] 다시 요청:** 다시 요청해도 같은 객체가 돌아오고, 이전 상태(`visitCount = 3`)가 그대로 유지됩니다.

클래스 밖에서 생성자를 직접 호출하면 컴파일 단계에서 막힙니다.

```text
Try.java:3: error: AppSettings() has private access in AppSettings
        AppSettings s = new AppSettings();
                        ^
1 error
```

## 동작 설명

인스턴스는 `AppSettings` 클래스의 `private static` 필드인 `instance`에 보관되며, 이 필드는 객체마다가 아니라 클래스에 하나만 존재합니다. `getInstance()`는 `instance`가 비어 있을 때만 생성자를 호출하고, 그 뒤에는 저장된 객체를 그대로 돌려주기 때문에 반복해서 요청해도 새 객체가 만들어지지 않습니다. 생성자는 `private`이므로 다른 클래스에서 `new`로 객체를 따로 만들 수도 없습니다. Java에서 참조 타입에 `==`를 쓰면 두 변수가 같은 객체를 가리킬 때만 `true`가 되므로, `first == second`가 `true`라는 것은 두 변수가 하나의 인스턴스를 공유한다는 직접적인 근거입니다. 또한 `first`로 바꾼 값이 `second`에서 그대로 읽히고 생성자 호출 횟수가 1로 유지되는 것도 같은 결과를 보여 줍니다.

## 참고 자료

- Java Language Specification SE 21, §15.21.3 Reference Equality Operators `==` and `!=` — https://docs.oracle.com/javase/specs/jls/se21/html/jls-15.html#jls-15.21.3
- Java Language Specification SE 21, §8.8.10 Preventing Instantiation of a Class — https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.8.10
- Java SE 21 API, `System.identityHashCode(Object)` — https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/System.html#identityHashCode(java.lang.Object)
