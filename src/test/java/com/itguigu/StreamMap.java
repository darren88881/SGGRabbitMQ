package com.itguigu;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @Author darren
 * @Date 2023/3/18 16:32
 */
public class StreamMap {

    @Test
    public void testStreamMap(){

        HashMap<String, String> bindingKeyMap = new HashMap<>();
        bindingKeyMap.put("quick.orange.rabbit","被队列 Q1Q2 接收到");
        bindingKeyMap.put("lazy.orange.elephant","被队列 Q1Q2 接收到");
        bindingKeyMap.put("quick.orange.fox","被队列 Q1 接收到");
        bindingKeyMap.put("lazy.brown.fox","被队列 Q2 接收到");
        bindingKeyMap.put("lazy.pink.rabbit","虽然满足两个绑定但只被队列 Q2 接收一次");
        bindingKeyMap.put("quick.brown.fox","不匹配任何绑定不会被任何队列接收到会被丢弃");
        bindingKeyMap.put("quick.orange.male.rabbit","是四个单词不匹配任何绑定会被丢弃");
        bindingKeyMap.put("lazy.orange.male.rabbit","是四个单词但匹配 Q2");

        bindingKeyMap.entrySet().stream().forEach((Map.Entry entry) -> {
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key+" : "+value);
        } );

        System.out.println("------------------------------------");

        bindingKeyMap.forEach((key, value) ->{
            System.out.println(key+"---"+value);
        });

    }
}
