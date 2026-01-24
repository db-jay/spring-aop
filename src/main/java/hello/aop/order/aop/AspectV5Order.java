package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

@Slf4j
@Aspect
public class AspectV5Order {
    @Aspect
    @Order(2)
    public static class LogAspect{
        @Around("hello.aop.order.aop.Pointcuts.allOrder()")
        public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[log] {}", joinPoint.getSignature()); // join point signature

            return joinPoint.proceed();
        }
    }

    @Aspect
    @Order(1)
    public static class TransactionAspect{
        @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
        public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
            try{
                // @Before: 조인 포인트 실행 이전에 실행
                log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
                Object result = joinPoint.proceed();

                // @AfterReturning: 조인 포인트가 정상 완료후 실행
                log.info("[트랜잭션 커밋] {}", joinPoint.getSignature());

                return result;
            } catch (Exception e) {
                // @AfterThrowing : 메서드가 예외를 던지는 경우 실행
                log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
                throw e;
            } finally {

                // @After : 조인 포인트가 정상 또는 예외에 관계없이 실행(finally)
                log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
            }
        }
    }
}
