package xueqiu.page;

import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;

/**
 * 自选股页面
 */
public class StockPage extends BasePage{

    /**
     * 删除所有自选股
     */
    public StockPage deleteAll() {
        click(By.id("com.xueqiu.android:id/edit_group"));
        click(By.id("com.xueqiu.android:id/check_all"));
        click(By.id("com.xueqiu.android:id/cancel_follow"));
        click(By.id("com.xueqiu.android:id/md_buttonDefaultPositive"));
        click(By.id("com.xueqiu.android:id/action_close"));
        return this;
    }

    /**
     * 获取所有自选股
     */
    public List<String> getAllStocks() {
        handleAlert();

        List<String> stocks = new ArrayList<>();
        findElements(By.id("com.xueqiu.android:id/portfolio_stockName")).forEach(element -> {
            stocks.add(element.getText());
        });
        System.out.println("stocks元素列表："+ stocks);
        return stocks;
    }

    /**
     * 添加自选股
     */
    public StockPage addDefaultSelectedStocks() {
        click(By.id("com.xueqiu.android:id/add_to_portfolio_stock"));
        return this;
    }

    /**
     * 进入自选股的搜索页
     */
    public SearchPage toSearch() {
        click(By.id("com.xueqiu.android:id/action_search"));
        return new SearchPage();
    }
}
