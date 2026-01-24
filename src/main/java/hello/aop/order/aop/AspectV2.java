package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class AspectV2 {

    // hello.aop.order 패키지와 하위 패키지 범위를 포인트컷으로 지정
    @Pointcut("execution(* hello.aop.order..*(..))")
    private void allOrder() {} // pointcut signature


    // @Around 애노테이션에 pointcut signature을 지정
    @Around("allOrder")


    /*
     * @Around 애노테이션의 메서드인 doLog 는 어드바이스( Advice )가 된다.
     * OrderService , OrderRepository 의 모든 메서드는 AOP 적용의 대상이 된다.
     */
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature()); // join point signature

        return joinPoint.proceed();
    }
}
