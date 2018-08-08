package com.springframework.log.task;

import com.springframework.log.service.InterfaceLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Configuration
@Service
public class DeleteInterfaceLogTask implements ApplicationListener<ContextRefreshedEvent> {

    private AtomicInteger atomicInteger = new AtomicInteger(0);
    @Autowired
    private InterfaceLogService interfaceLogService;
    @Value("${MAX_COUNT:1000000}")
    private Long MAX_COUNT;
    @Value("${SAVE_DAY:90}")
    private int SAVE_DAY;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        int i = atomicInteger.incrementAndGet();
        if (i > 1) {
            return;
        }
        process();
    }

    private void process() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (checkTime()) {
                        deleteErrorLog();
                    } else {
                        try {
                            Thread.sleep(3600000);//1小时
                        } catch (InterruptedException e) {
                            log.error("sleep error");
                        }
                    }
                }
            }
        }, "deleteErrorLog").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (checkTime()) {
                        deleteSuccessLog();
                    } else {
                        try {
                            Thread.sleep(3600000);//1小时
                        } catch (InterruptedException e) {
                            log.error("sleep error");
                        }
                    }
                }
            }
        }, "deleteSuccessLog").start();

    }

    private boolean checkTime() {
        Date currentTime = new Date();
        Calendar start = Calendar.getInstance();
        start.setTime(currentTime);
        start.set(Calendar.HOUR_OF_DAY, 2);
        start.set(Calendar.MINUTE, 0);
        Date startTime = start.getTime();
        Calendar end = Calendar.getInstance();
        end.setTime(currentTime);
        end.set(Calendar.HOUR_OF_DAY, 6);
        end.set(Calendar.MINUTE, 0);
        Date endTime = end.getTime();
        return currentTime.after(startTime) && currentTime.before(endTime);
    }

    private void deleteSuccessLog() {
        try {
            Date date = getTime();
            int num = 0;
            int times = 0;
            do {
                num = interfaceLogService.deleteSuccessLogByTime(date);
                times++;
                sleep5Minute(num, times);
            } while (num == 100);

            do {
                times = 0;
                Long count = interfaceLogService.getSuccessLogCount();
                if (count < MAX_COUNT) {
                    break;
                }
                num = interfaceLogService.deleteSuccessLog();
                times++;
                sleep5Minute(num, times);
            } while (num == 100);
        } catch (Exception e) {
            log.error("delete successLog error,",e);
        }
    }

    private void deleteErrorLog() {
        try {
            Date date = getTime();
            int num = 0;
            int times = 0;
            do {
                num = interfaceLogService.deleteErrorLogByTime(date);
                times++;
                sleep5Minute(num, times);
            } while (num == 100);

            do {
                times = 0;
                Long count = interfaceLogService.getErrorLogCount();
                if (count < MAX_COUNT) {
                    break;
                }
                num = interfaceLogService.deleteErrorLog();
                times++;
                sleep5Minute(num, times);
            } while (num == 100);
        } catch (Exception e) {
            log.error("delete errorLog error,",e);
        }
    }

    private void sleep5Minute(int num, int times) {
        if (num == 100 && times == 100) {
            try {
                Thread.sleep(300000l);
            } catch (InterruptedException e) {
                log.error("sleep error");
            }
        }
    }


    private Date getTime() {
        Calendar calc = Calendar.getInstance();
        calc.setTime(new Date());
        calc.add(calc.DATE, -SAVE_DAY);
        return calc.getTime();
    }

}
