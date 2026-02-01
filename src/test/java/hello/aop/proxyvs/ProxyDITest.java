package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import hello.aop.proxyvs.code.ProxyDiAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
// 1. spring.aop.proxy-target-class=false : 스프링이 AOP 프록시를 생성할 때 JDK 동적 프록시를 우선 생성하도록 설정.
//@SpringBootTest(properties = {"spring.aop.proxy-target-class=false"})

// 2. spring.aop.proxy-target-class=true : CGLIB로 프록시를 생성하도록 설정
//@SpringBootTest(properties = {"spring.aop.proxy-target-class=true"})

// 3. 스프링은 최종적으로 스프링 부트 2.0에서 CGLIB를 기본으로 사용하도록 결정했다.(default = true)
@SpringBootTest

@Import(ProxyDiAspect.class)
public class ProxyDITest {

    @Autowired
    MemberService memberService; // interface

    @Autowired
    MemberServiceImpl memberServiceImpl; // class

    @Test
    void go() {
        log.info("memberService={}", memberService.getClass());
        log.info("memberServiceImpl={}", memberServiceImpl.getClass());
        memberServiceImpl.hello("hello");
    }
}
