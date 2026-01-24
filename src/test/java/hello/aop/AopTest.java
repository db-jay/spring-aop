package hello.aop;

import hello.aop.order.OrderRepository;
import hello.aop.order.OrderService;
import hello.aop.order.aop.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest
//@Import(AspectV1.class) // @Import는 주로 설정 파일을 추가할 때 사용하지만, 스프링 빈도 간단하게 등록할 수 있다.
//@Import(AspectV2.class) // pointcut signature 사용
//@Import(AspectV3.class) // 트랜잭션 추가
//@Import(AspectV4Pointcut.class) // 외부 포인트컷 호출
//@Import({AspectV5Order.LogAspect.class, AspectV5Order.TransactionAspect.class}) // @Order 어노테이션 활용하여 순서 지정
@Import(AspectV6advice.class) // advice(Before, AfterReturning, AfterThrowing, After) 사용

public class AopTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void  aopInfo() {
        log.info("isAopProxy, orderService={}", AopUtils.isAopProxy(orderService));
        log.info("isAopProxy, orderRepository={}", AopUtils.isAopProxy(orderRepository));
    }

    @Test
    void success() {
        orderService.orderItem("itemA");
    }

    @Test
    void exception() {
//        orderService.orderItem("ex");
        Assertions.assertThatThrownBy(() -> orderService.orderItem("ex"))
                .isInstanceOf(IllegalStateException.class) ;
    }
}
