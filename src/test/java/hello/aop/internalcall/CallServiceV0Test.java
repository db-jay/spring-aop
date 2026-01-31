package hello.aop.internalcall;

import hello.aop.internalcall.aop.CallLogAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j

// 수동으로 Bean 설정 정보에 CallLogAspect 포함 (테스트를 위함, 일반적으로는 객체에 직접 @Component 어노테이션 사용)
@Import(CallLogAspect.class)
@SpringBootTest
class CallServiceV0Test {
    /*
    * 1. AOP 작동 원리
        * CallServiceV0 객체는 @Component annotation을 통해 Spring Bean으로 등록됨
        * Spring AOP가 @Aspect annotation을 통해 CallLogAspect 객체가 AOP로 적용대상이라는 것을 발겸함.
    * 2. SPRING 라이프사이클
        * Spring이 Run되며 CallServiceV0 타입의 Bean을 생성할 때, 이 Bean이 AOP 적용 대상이라는 것을 인지함.
        * 원본 CallServiceV0 객체 대신 AOP 로직이 포함된 프록시(Proxy) 객체를 생성하여 스프링 컨테이너에 Bean 등록.
        * CallServiceV0Test에서 @Autowired는 바로 이 프록시 객체를 주입해주는 것.
    */
    @Autowired
    CallServiceV0 callServiceV0;

    @Test
    void external() {
        // 문제는 callServiceV0 프록시 객체를 external(); 메서드로 호출했을 때 external() 내부의 this.internal() 메서드가 존재한다는 점이다.
        // internal() 메서드는 proxy를 호출해야 하지만 의도와 다르게 원본객체를 호출하게 된다.
        callServiceV0.external();
    }

    @Test
    void internal() {
        callServiceV0.internal();
    }
}