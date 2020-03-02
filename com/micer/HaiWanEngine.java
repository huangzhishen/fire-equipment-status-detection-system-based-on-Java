package com.micer;

import com.micer.engine.Engine;
import com.micer.engine.EngineException;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class HaiWanEngine {
    public HaiWanEngine()
    {

    }


    /**
     * 主函数，整个程序的入口
     * 功能：
     * 1、读取配置文件，然后实例化hubOrientedTCPConnEngine类
     * 2、调用hubOrientedTCPConnEngine的父类AbastractEngine的start()方法，hubOrientedTCPConnEngine类只有初始化init()方法
     */
    public static void main(String[] args) {

        Logger logger = Logger.getLogger(HaiWanEngine.class);
        try
        {
            ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
            Engine engine = (Engine) context.getBean("hubOrientedTCPConnEngine");
            engine.start();
        }
        catch (EngineException e)
        {
            e.printStackTrace();
            logger.debug(e.getMessage());
            System.exit(-1);
        }
    }


}
