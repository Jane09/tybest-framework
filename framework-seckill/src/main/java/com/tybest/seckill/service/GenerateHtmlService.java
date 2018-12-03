package com.tybest.seckill.service;

import com.tybest.seckill.entity.Seckill;
import com.tybest.seckill.model.Result;
import com.tybest.seckill.repository.SeckillRepository;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import static org.apache.commons.lang3.concurrent.BasicThreadFactory.Builder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author tb
 * @date 2018/12/3 13:32
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GenerateHtmlService {

    private static int corePoolSize = Runtime.getRuntime().availableProcessors();
    private static ThreadPoolExecutor executor  = new ThreadPoolExecutor(corePoolSize, corePoolSize+1, 10L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            new Builder().namingPattern("seckill-generator-html-task-%d").daemon(true).build(),
            new ThreadPoolExecutor.AbortPolicy());

    private final Configuration configuration;
    private final SeckillRepository seckillRepository;

    @Value("${spring.freemarker.html.path}")
    private String path;


    public Result generator() {
        List<Seckill> list = seckillRepository.findAll();
        final List<Future<String>> resultList = new ArrayList<Future<String>>();
        for(Seckill seckill:list){
            resultList.add(executor.submit(new GeneratorTask(seckill)));
        }
        for (Future<String> fs : resultList) {
            try {
                log.info(fs.get());
            } catch (InterruptedException | ExecutionException e) {
                log.error("Generator Html failed",e);
            }
        }
        return Result.ok();
    }


    @RequiredArgsConstructor
    class GeneratorTask implements Callable<String> {

        private final Seckill seckill;

        @Override
        public String call() throws Exception {
            Template template = configuration.getTemplate("goods.flt");
            File file= new File(path+seckill.getSeckillId()+".html");
            Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            template.process(seckill, writer);
            return "success";
        }
    }
}
