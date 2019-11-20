package xueqiu.testcase;

import xueqiu.page.App;
import xueqiu.page.StockPage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.net.MalformedURLException;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

/**
 * 自选股测试case
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestStock {
    private static StockPage stockPage;

    @BeforeAll
    public static void beforeAll() throws MalformedURLException {
        App.start();
        stockPage = App.toStocks();
    }

    @BeforeEach
    public void beforeEach() {

    }

    /**
     * 增加自选股
     */
    @Order(100)
    @Test
    public void addDefaultSelectedStocks() {
        if (stockPage.getAllStocks().size() >= 1) {
            stockPage.deleteAll();
        }
        assertThat(stockPage.addDefaultSelectedStocks().getAllStocks().size(),greaterThanOrEqualTo(6));
    }

    /**
     * 添加股票
     * 使用Junit5完成参数化
     */
    @Order(200)
    @ParameterizedTest
    @MethodSource("data")
    public void addStock(String code, String name) {
        stockPage.toSearch().search(code).select().cancel();
        assertThat(stockPage.getAllStocks(),hasItem(name));
    }

    /**
     * 参数化的方法
     */
    public static Stream<Arguments> data() {
        return Stream.of(
                Arguments.arguments("pdd","拼多多"),
                Arguments.arguments("jd","京东")
        );
    }
}
