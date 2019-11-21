package xueqiu.page;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * App初始化页面
 */
public class App extends BasePage {
    private static App app;

    //单例实现获取App
    public static App getInstance() {
        if (app == null) {
            app = new App();
        }
        return app;
    }

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
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        //从开始到等待元素出现的时长
        long start = System.currentTimeMillis();
        new WebDriverWait(driver,40).until(x ->{
            String xml = driver.getPageSource();
            boolean exist = xml.contains("home_search") || xml.contains("image_cancel");
            System.out.println("元素出现时长："+(System.currentTimeMillis() - start)/1000 + "s");
            System.out.println("是否存在home_search或者image_cancel: "+exist);
            return exist;
        });
    }

    /**
     * 进入搜索页
     */

    public SearchPage toSearch() {
        //首页搜索入口
        click(By.id("com.xueqiu.android:id/home_search"));
        //parseSteps("/xueqiu/page/app.yaml","toSearch");
        //返回搜索页
        return new SearchPage();
    }

    /**
     * 进入自选股页
     */
    public StockPage toStocks() {
        //通过xpath找到自选股页面
        click(By.xpath("//*[contains(@resource-id,'tab_name') and @text='自选']"));
        //parseSteps("/xueqiu/page/app.yaml","toStocks");
        return new StockPage();
    }
}
