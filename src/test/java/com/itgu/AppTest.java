package com.itgu;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
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
}
