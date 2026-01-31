package hello.aop.pointcut;

import hello.aop.member.MemberService;
import hello.aop.member.annotation.ClassAop;
import hello.aop.member.annotation.MethodAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(ParameterTest.ParameterAspect.class)
@SpringBootTest
public class ParameterTest {

    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberService={}", memberService.getClass());
        memberService.hello("helloA");
    }

    @Slf4j
    @Aspect
    static class ParameterAspect {

        @Pointcut("execution(* hello.aop.member..*.*(..))")
        private void allMember() {
        }

        // joinPoint.getArgs()[0] 와 같이 매개변수를 전달 받는다. => 배열에서 직접 꺼내기
        @Around("allMember()")
        public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
            Object arg1 = joinPoint.getArgs()[0];
            log.info("[logArgs1]{}, arg={}", joinPoint.getSignature(), arg1);
            return joinPoint.proceed();
        }

        // args(arg,..) 와 같이 매개변수를 전달 받는다.
        @Around("allMember() && args(arg,..)")
        public Object logArgs2(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {
            log.info("[logArgs2]{}, arg={}", joinPoint.getSignature(), arg);
            return joinPoint.proceed();
        }

        // @Before 를 사용한 축약 버전이다. 추가로 타입을 String 으로 제한했다
        @Before("allMember() && args(arg,..)")
        public void logArgs3(String arg) {
            log.info("[logArgs3] arg={}", arg);
        }

        // this => proxy 객체를 전달받는다.
        @Before("allMember() && this(obj)")
        public void thisArgs(JoinPoint joinpoint, MemberService obj) {
            log.info("[thisArgs] joinpoint={}, obj={}", joinpoint.getSignature(), obj.getClass());
        }

        // target => proxy가 호출하는 실제 대상 객체를 전달 받는다.
        @Before("allMember() && target(obj)")
        public void targetArgs(JoinPoint joinpoint, MemberService obj) {
            log.info("[targetArgs] joinpoint={}, obj={}", joinpoint.getSignature(), obj.getClass());
        }

        // 타입의 애노테이션을 전달 받는다.
        @Before("allMember() && @target(annotation)")
        public void atTarget(JoinPoint joinpoint, ClassAop annotation) {
            log.info("[@target]{}, obj={}", joinpoint.getSignature(), annotation);
        }

        @Before("allMember() && @within(annotation)")
        public void atWhthin(JoinPoint joinpoint, ClassAop annotation) {
            log.info("[@within]{}, obj={}", joinpoint.getSignature(), annotation);
        }

        // 메서드의 애노테이션을 전달 받는다.
        // annotation.value() 로 해당 애노테이션의 값을 출력하는 모습을 확인할 수 있다.
        @Before("allMember() && @annotation(annotation)")
        public void atAnnotation(JoinPoint joinpoint, MethodAop annotation) {
            log.info("[@annotation]{}, annotationValue={}", joinpoint.getSignature(), annotation.value());
        }
    }
}
