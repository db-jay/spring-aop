# Spring AOP 학습 프로젝트

Spring AOP의 핵심 개념과 다양한 포인트컷 표현식, 실무 활용 패턴을 단계별로 학습하는 프로젝트입니다.

## 학습 내용

### 1. Spring AOP (@Aspect)
- `@Aspect`, `@Around` 애노테이션 기반 AOP 구현 (AspectV1 ~ AspectV6)
- AspectJ 표현식을 사용한 Pointcut 정의
- `ProceedingJoinPoint`로 대상 메서드 호출
- Aspect 순서 지정 (`@Order`)
- Advice 종류별 활용 (`@Around`, `@Before`, `@After`, `@AfterReturning`, `@AfterThrowing`)

### 2. 포인트컷 표현식
- `execution` - 메서드 실행 매칭
- `within` - 특정 타입 내 메서드 매칭
- `args` - 메서드 인자 타입 매칭
- `@annotation` - 특정 애노테이션이 붙은 메서드 매칭
- `@target`, `@within` - 클래스 레벨 애노테이션 매칭
- `bean` - 스프링 빈 이름 매칭
- `this`, `target` - 프록시/대상 객체 매칭

### 3. 커스텀 애노테이션 기반 AOP
- `@ClassAop`, `@MethodAop` 커스텀 애노테이션 정의
- 애노테이션 기반 포인트컷으로 AOP 적용

### 4. 프록시 내부 호출 문제
- 같은 클래스 내부 메서드 호출 시 AOP 미적용 문제 (CallServiceV0)
- 자기 자신 주입 방식 (CallServiceV1)
- `ObjectProvider` 지연 조회 방식 (CallServiceV2)
- 구조 변경을 통한 해결 (CallServiceV3)

### 5. 실무 활용 - Retry & Trace
- `@Retry` 커스텀 애노테이션으로 재시도 AOP 구현
- `@Trace` 커스텀 애노테이션으로 로그 추적 AOP 구현

### 6. 프록시 캐스팅과 DI
- JDK 동적 프록시 vs CGLIB 프록시 캐스팅 차이
- 프록시 기술에 따른 의존성 주입 동작 차이

## 프로젝트 구조

```
src/
├── main/java/hello/aop/
│   ├── order/
│   │   ├── OrderService.java        # 주문 서비스
│   │   ├── OrderRepository.java     # 주문 리포지토리
│   │   └── aop/
│   │       ├── AspectV1 ~ V6        # 단계별 Aspect 구현
│   │       └── Pointcuts.java       # 재사용 포인트컷 정의
│   ├── member/
│   │   ├── MemberService.java       # 회원 서비스 인터페이스
│   │   ├── MemberServiceImpl.java   # 회원 서비스 구현
│   │   └── annotation/              # 커스텀 애노테이션
│   ├── internalcall/
│   │   ├── CallServiceV0 ~ V3       # 내부 호출 문제 해결 버전별 예시
│   │   └── aop/CallLogAspect.java   # 로깅 Aspect
│   └── exam/
│       ├── ExamService.java         # 시험 서비스
│       ├── ExamReapository.java     # 시험 리포지토리
│       ├── annotation/              # @Retry, @Trace 애노테이션
│       └── aop/                     # RetryAspect, TraceAspect
│
└── test/java/hello/aop/
    ├── AopTest.java                 # 기본 AOP 테스트
    ├── pointcut/
    │   ├── ExecutionTest.java       # execution 포인트컷 테스트
    │   ├── WithinTest.java          # within 포인트컷 테스트
    │   ├── ArgsTest.java            # args 포인트컷 테스트
    │   ├── AtAnnotationTest.java    # @annotation 포인트컷 테스트
    │   ├── AtTargetAtWithinTest.java # @target, @within 테스트
    │   ├── BeanTest.java            # bean 포인트컷 테스트
    │   ├── ThisTargetTest.java      # this, target 테스트
    │   └── ParameterTest.java       # 파라미터 바인딩 테스트
    ├── internalcall/                # 내부 호출 테스트
    ├── exam/                        # Retry, Trace 테스트
    └── proxyvs/                     # 프록시 캐스팅/DI 테스트
```

## 핵심 개념 정리

| 개념 | 설명 |
|------|------|
| Advice | 프록시가 호출하는 부가 기능 (로깅, 트랜잭션 등) |
| Pointcut | 어디에 부가 기능을 적용할지 판단하는 필터링 로직 |
| Advisor | Advice + Pointcut을 하나로 묶은 것 |
| @Aspect | 여러 Advisor를 편리하게 생성하는 애노테이션 |
| JoinPoint | Advice가 적용될 수 있는 위치 (메서드 실행 지점) |

## 기술 스택
- Java 17+
- Spring Boot
- Gradle
