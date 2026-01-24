package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class AspectV1 {

    // @Around 애노테이션의 값인 execution(* hello.aop.order..*(..)) 는 포인트컷이 된다.
    @Around("execution(* hello.aop.order..*(..))")

    /*
     * @Around 애노테이션의 메서드인 doLog 는 어드바이스( Advice )가 된다.
     * OrderService , OrderRepository 의 모든 메서드는 AOP 적용의 대상이 된다.
     */
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature()); // join point signature

        return joinPoint.proceed();
    }
}
