package com.tybest.seckill.controller;

import com.tybest.seckill.model.Result;
import com.tybest.seckill.service.SeckillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tb
 * @date 2018/12/5 10:02
 */
@Api(tags ="分布式秒杀")
@RestController
@RequestMapping("/seckillDistributed")
public class SeckillDistributedController extends AbstractBaseController {


    @ApiOperation(value="秒杀一(Rediss分布式锁)",nickname="科帮网")
    @PostMapping("/startRedisLock")
    public Result startRedisLock(long seckillId){
        return seckill(seckillId, new SeckillProcesosr() {
            @Override
            public void processor(SeckillService seckillService, long seckillId, long userId) {

            }
        });
    }
}
