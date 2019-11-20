package xueqiu.page;

import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;

/**
 * 封装搜索页
 */
public class SearchPage extends BasePage{

    //搜索页面中搜索输入框id
    private By inputBox = By.id("com.xueqiu.android:id/search_input_text");

    /**
     * 搜索操作
     * @param keyword
     */
    public SearchPage search(String keyword) {
        //在搜索输入框输入要搜索的内容
        App.driver.findElement(inputBox).sendKeys(keyword);
        //点击搜索结果，如果有多条记录，默认点击第一条
        click(By.id("com.xueqiu.android:id/name"));
        return this;
    }

    /**
     * 获取当前股价
     */
    public Float getCurrentPrice() {
        MobileElement el4 = (MobileElement)findElement(By.id("com.xueqiu.android:id/current_price"));
        return Float.valueOf(el4.getText());
    }

    /**
     * 点击搜索结果页的取消按钮
     */
    public App cancel() {
        click(By.id("com.xueqiu.android:id/action_close"));
        return new App();
    }

    /**
     * 将搜索的股票加入自选
     */
    public SearchPage select() {
        click(By.id("com.xueqiu.android:id/follow_btn"));
        return this;
    }
}
