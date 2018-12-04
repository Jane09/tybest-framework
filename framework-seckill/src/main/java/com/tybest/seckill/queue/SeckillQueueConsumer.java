package com.tybest.seckill.queue;

import com.tybest.seckill.entity.SuccessKilled;
import com.tybest.seckill.model.Result;
import com.tybest.seckill.service.SeckillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author tb
 * @date 2018/12/4 12:17
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SeckillQueueConsumer implements ApplicationRunner {

    private final SeckillService seckillService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        while (true) {
            SuccessKilled killed =SeckillQueue.getInstance().consume();
            Result result = seckillService.seckillSeq(killed.getSeckillId(),killed.getUserId());
            if(result!=null){
                log.info("用户:{}{}",killed.getUserId(),result.get("msg"));
            }else{
                log.info("用户:{}{}",killed.getUserId(),"哎呦喂，人也太多了，请稍后！");
            }
        }
    }
}
