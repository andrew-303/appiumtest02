package xueqiu.page;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * App初始化页面
 */
public class App extends BasePage {

    /**
     * App启动
     */
    public static void start() throws MalformedURLException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("platformName", "android");
        desiredCapabilities.setCapability("deviceName", "127.0.0.1:7555");
        desiredCapabilities.setCapability("appActivity", ".view.WelcomeActivityAlias");
        desiredCapabilities.setCapability("appPackage", "com.xueqiu.android");
        desiredCapabilities.setCapability("noReset", true);

        URL remoteUrl = new URL("http://localhost:4723/wd/hub");

        driver = new AndroidDriver(remoteUrl, desiredCapabilities);
        //隐式等待
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
    }

    /**
     * 进入搜索页
     */
    public static SearchPage toSearch() {
        //首页搜索入口
        click(By.id("com.xueqiu.android:id/home_search"));
        //返回搜索页
        return new SearchPage();
    }

    /**
     * 进入自选股页
     */
    public static StockPage toStocks() {
        //通过xpath找到自选股页面
        click(By.xpath("//*[contains(@resource-id,'tab_name') and @text='自选']"));
        return new StockPage();
    }
}
