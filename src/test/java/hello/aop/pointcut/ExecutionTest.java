package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.junit.jupiter.api.BeforeEach;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
public class ExecutionTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    @Test
    void printMethod() {
        log.info("helloMethod=P{}", helloMethod);
    }

    @Test
    void exactMatch() {
        //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        // AspectJExpressionPointcut 에 pointcut.setExpression 을 통해서 포인트컷 표현식을 적용
        pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(String))");

        // pointcut.matches(메서드, 대상 클래스)
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void allMatch() {
        pointcut.setExpression("execution(* *(..))"); // 접근제어 생략, 반환타입 *, 선언타입 생략, 메서드이름 *, 예외 생략
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatch()  {
        pointcut.setExpression("execution(* hello(..))"); // 접근제어 생략, 반환타입 *, 선언타입 생략, 메서드이름 hello(..)
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchStar1()  {
        pointcut.setExpression("execution(* hel*(..))"); // 접근제어 생략, 반환타입 *, 선언타입 생략, 메서드이름 hel*
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchStar2()  {
        pointcut.setExpression("execution(* *el*(..))"); // 접근제어 생략, 반환타입 *, 선언타입 생략, 메서드이름 *el*
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchFalse()  {
        pointcut.setExpression("execution(* no(..))"); // 접근제어 생략, 반환타입 *, 선언타입 생략, 메서드이름 no(..) => throw error
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void packageExactMatch1()  {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.hello(..))"); // 접근제어 생략, 반환타입 *, hello.aop.member.MemberServiceImpl 패키지와 파일의 hello메서드
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageExactMatch2()  {
        pointcut.setExpression("execution(* hello.aop.member.*.*(..))"); // 접근제어 생략, 반환타입 *, hello.aop.member.*.* 파일, 메서드
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageExactFalse()  {
        pointcut.setExpression("execution(* hello.aop.*.*(..))"); // 접근제어 생략, 반환타입 *, hello.aop.*.* 하위 파일, 메서드 => throw error (패키지 위치 상이)
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    void packageMatchSubPackage1()  {
        pointcut.setExpression("execution(* hello.aop.member..*.*(..))"); // member package 하위 파일 모두 허용
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    void packageMatchSubPackage2()  {
        pointcut.setExpression("execution(* hello.aop...*.*(..))"); // aop package 하위 파일 모두 허용
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    // 패키지 위치 정확하게 매치
    @Test
    void typeExactMatch() {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    // 부모 타입(interface)은 자식 타입이 선언한 메서드도 매치 가능
    @Test
    void typeMatchSuperType() {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }


    //  부모 타입을 표현식에 선언한 경우 부모 타입에서 선언한 메서드가 자식 타입에 있어야 매칭에 성공한다.
    //  부모 타입에 있는 hello(String) 메서드는 매칭에 성공하지만, 부모 타입에 없는 internal(String) 는 매칭에 실패한다.
    @Test
    void typeMatchInternal() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))"); // 자식 타입은 매치 가능
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchNoSuperTypeMethodFalse() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))"); // 자식 타입이 선언한 메서드는 부모 타입에 매치 불가능 => throw error
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isFalse();
    }

    //String 타입의 파라미터 허용
    @Test
    void argsMatch() {
        pointcut.setExpression("execution(* *(String))"); // string 타입의 파라미터 허용
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /*
     * execution 파라미터 매칭 규칙
     * (String) : 정확하게 String 타입 파라미터
     * () : 파라미터가 없어야 한다.
     * (*) : 정확히 하나의 파라미터, 단 모든 타입을 허용한다.
     * (*, *) : 정확히 두 개의 파라미터, 단 모든 타입을 허용한다.
     * (..) : 숫자와 무관하게 모든 파라미터, 모든 타입을 허용한다. 참고로 파라미터가 없어도 된다. 0..* 로 이해하
     * 면 된다.
     * (String, ..) : String 타입으로 시작해야 한다. 숫자와 무관하게 모든 파라미터, 모든 타입을 허용한다.
     * 예) (String) , (String, Xxx) , (String, Xxx, Xxx) 허용
     */

    // 파라미터가 없어야 함
    @Test
    void argsMatchNoArgs() {
        pointcut.setExpression("execution(* *())"); // 파라미터가 없는 경우 => throw errer
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    //정확히 하나의 파라미터 허용, 모든 타입 허용 (e.g. Xxx)
    @Test
    void argsMatchStar() {
        pointcut.setExpression("execution(* *(*))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    //숫자와 무관하게 모든 파라미터, 모든 타입 허용 || 파라미터가 없어도 됨 (e.g.(), (Xxx), (Xxx, Xxx))
    @Test
    void argsMatchAll() {
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    //String 타입으로 시작, 숫자와 무관하게 모든 파라미터, 모든 타입 허용 (e.g. (String), (String, Xxx), (String, Xxx, Xxx))
    @Test
    void argsMatchComplex() {
        pointcut.setExpression("execution(* *(String, ..))"); // (String + xxx) 2개로 제한 => (String, *)
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
}
