package com.hgwz.appium.demo;

import java.util.HashMap;
import java.util.Map;

public class TestMapEntrySet {
    public static void main(String[] args) {
        HashMap<String,String> map = new HashMap<>();
        map.put("aaa","beijing" );
        map.put("bbb","shanghai" );
        map.put("ccc","hangzhou" );

        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "-->" + entry.getValue());
        }
    }
}
