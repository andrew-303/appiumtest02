package com.hgwz.appium.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TestYamlRead {

    public static void main(String[] args) throws IOException {
        TestYamlRead testYamlRead = new TestYamlRead();
        testYamlRead.readYaml01();
        System.out.println("-----分隔线-----");

        testYamlRead.readYaml02();

        System.out.println(TestYamlRead.class.getCanonicalName());
        System.out.println(TestYamlRead.class.getName());
    }

    public void readYaml01() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Object[][] demo = mapper.readValue(this.getClass().getResourceAsStream("/xueqiu/testcase/TestSearch.yaml"),
                Object[][].class);

        System.out.println("demo:"+demo);
        System.out.println("长度："+demo.length);
        System.out.println(demo[0].length);
        System.out.println(demo[0]);
        System.out.println(demo[0][0]);
        System.out.println(demo[0][1]);

    }

    public void readYaml02() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Object[][] demo = mapper.readValue(this.getClass().getResourceAsStream("/xueqiu/testcase/TestSearch.yaml"),
                Object[][].class);
        List<Object> list = Arrays.asList(demo);
        for (Object o : list) {
            System.out.println(o);
            System.out.println(o.toString());
        }
    }
}
