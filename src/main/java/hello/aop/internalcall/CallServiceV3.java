package hello.aop.internalcall;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 구조를 변경(분리)
    * 가장 나은 대안은 내부 호출이 발생하지 않도록 구조를 변경하는 것이다.
    * 내부 호출 자체가 사라지고, callService internalService 를 호출하는 구조로 변경되었다.
    * 실제 이 방법을 가장 권장한다!!
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CallServiceV3 {

    private final InternalService internalService;

    public void external() {
        log.info("call external");
        internalService.internal();
    }
}
