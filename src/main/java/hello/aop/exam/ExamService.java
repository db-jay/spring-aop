package hello.aop.exam;

import hello.aop.exam.annotation.Trace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamReapository examReapository;

    @Trace
    public void request(String itemId) {
        examReapository.save(itemId);
    }
}
