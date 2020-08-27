package cn.caishen.worklog.task;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

/**
 * just test
 * @author LB
 */
@EnableScheduling
@Component
public class TestJob {

    public void excuteTextJob(){
        System.out.println("=================================测试第二种自动任务");
    }
}
