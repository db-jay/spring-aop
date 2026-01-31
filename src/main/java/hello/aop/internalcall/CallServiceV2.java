package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CallServiceV2 {

//    private final ApplicationContext applicationContext; // ApplicationContext 는 너무 많은 기능을 제공
    private final ObjectProvider<CallServiceV2> callServiceV2ObjectProvider;

//    callServiceProvider.getObject() 를 호출하는 시점에 스프링 컨테이너에서 빈을 조회한다.
//    여기서는 자기 자신을 주입 받는 것이 아니기 때문에 순환 사이클이 발생하지 않는다.
    public CallServiceV2(ObjectProvider<CallServiceV2> callServiceV2ObjectProvider) {
        this.callServiceV2ObjectProvider = callServiceV2ObjectProvider;
    }

    //    public CallServiceV2(ApplicationContext applicationContext) {
//        this.applicationContext = applicationContext;
//    }


    public void external() {
        log.info("call external");
//        CallServiceV2 callServicve2 = applicationContext.getBean(CallServiceV2.class);
        CallServiceV2 callServicve2 = callServiceV2ObjectProvider.getObject();
        callServicve2.internal(); // = this.internal
    }

    public void internal() {
        log.info("call internal");
    }
}
