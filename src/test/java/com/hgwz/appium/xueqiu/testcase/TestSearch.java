package com.hgwz.appium.xueqiu.testcase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.hgwz.appium.xueqiu.page.App;
import com.hgwz.appium.xueqiu.page.SearchPage;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

/**
 * 首页搜索测试case
 */
@RunWith(Parameterized.class)
public class TestSearch {
    private static Logger logger = LoggerFactory.getLogger(TestSearch.class);

    public static SearchPage searchPage;

    /**
     * 所有case跑之前，先启动APP
     */
    @BeforeClass
    public static void beforeAll() throws MalformedURLException {
        logger.info("启动App");
        App.start();
    }

    /**
     * 用Junit4完成参数化,参数为：股票名称、股价
     */
    @Parameterized.Parameters
    public static Collection<Object[]> data() throws IOException {
        /* 用读取yaml文件替换代码中写死参数,即实现了测试数据的数据驱动
        return Arrays.asList(new Object[][]{
                {"alibaba",100f},
                {"xiaomi",8f},
                {"jd",33f}
        });*/
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        String path = "/" + TestSearch.class.getCanonicalName().replace(".", "/")+".yaml";
        Object[][] demo = mapper.readValue(TestSearch.class.getResourceAsStream(path),
                Object[][].class);
        return Arrays.asList(demo);
    }
    //第一个参数股票名称
    @Parameterized.Parameter(0)
    public String stock;

    //第二个参数股价
    @Parameterized.Parameter(1)
    public Float price;

    /**
     * 测试前置条件
     */
    @Before
    public void before() {
        logger.info("进入搜索页");
        //进入搜索页
        searchPage = App.toSearch();
    }

    /**
     * 搜索内容case,断言搜索结果
     */
    @Test
    public void search() {
        logger.info("开始搜索页测试");
        assertThat(searchPage.search(stock).getCurrentPrice(), greaterThanOrEqualTo(price));
    }

    /**
     * case执行完毕，点击搜索页取消按钮
     */
    @After
    public void after() {
        searchPage.cancel();
    }
}
