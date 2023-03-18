package com.itguigu;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;


/**
 * 新搭的环境提交试试
 * Unit test for simple App.
 */
public class AppTest {

    private Logger logger = LoggerFactory.getLogger(AppTest.class);

    /**
     * 日志打印测试
     * TRACE 不会打印是因为这个日志级别高于 logback.xml 中定义的 DEBUG
     */
    @Test
    public void logOutputTest(){
        logger.error("ERROR");
        logger.warn("WARN");
        logger.info("INFO");
        logger.debug("DEBUG");
        logger.trace("TRACE");
    }

    @Test
    public void testConcurrentSkipListMap(){
        ConcurrentSkipListMap concurrentSkipListMap = new ConcurrentSkipListMap<String,String>();
        for (int i=0;i< 10; i++) {
            concurrentSkipListMap.put(i+"",i+"");
        }
        System.out.println(concurrentSkipListMap.toString());

        ConcurrentNavigableMap concurrentNavigableMap = concurrentSkipListMap.headMap(5+"", true);

        System.out.println(concurrentNavigableMap.toString());
    }
}
