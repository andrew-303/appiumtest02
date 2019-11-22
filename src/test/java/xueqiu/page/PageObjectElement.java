package xueqiu.page;

import org.openqa.selenium.By;

import java.util.HashMap;
import java.util.List;

/**
 * 元素对象
 */
public class PageObjectElement {
    public List<HashMap<String,String>> element;

    public By getLocator() {
        String osOrigin = BasePage.driver.getCapabilities().getPlatform().toString().toLowerCase();
        return By.id("xxx");
    }

    //元素定位器
    public By getLocator(String os, String version) {
        for (HashMap<String, String> map : element) {
            //判断配置文件中os和version与传进来的参数是否一致
            if (map.get("os") == os && map.get("version") == version) {
                if (map.get("id") != null) {
                    //返回id的元素
                    return By.id(map.get("id"));
                } else if (map.get("xpath") != null) {
                    //返回xpath的元素
                    return By.xpath(map.get("xpath"));
                }
                break;
            }
        }
        return null;
    }
}
