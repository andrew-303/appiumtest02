package xueqiu.page;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 基础页面
 */
public class BasePage {
    private static Logger logger = LoggerFactory.getLogger(BasePage.class);

    private PageObjectModel model = new PageObjectModel();

    /**
     * 定义driver
     */
    public static AndroidDriver<WebElement> driver;

   //测试步骤参数化
    private static HashMap<String,Object> params = new HashMap<>();

    public static HashMap<String, Object> getParams() {
        return params;
    }

    public void setParams(HashMap<String, Object> params) {
        this.params = params;
    }

    //测试步骤结果读取
    private static HashMap<String,Object> results = new HashMap<>();

    public static HashMap<String, Object> getResults() {
        return results;
    }

    /**
     * 通用元素定位与异常处理机制
     */
    public static WebElement findElement(By by) {
        //todo:递归是更好的方法
        //todo:如果定位的元素是动态变化位置
        //System.out.println("待查找的元素是："+ by);
        logger.info("待查找的元素是："+ by);
        try {
            return driver.findElement(by);
        } catch (Exception e) {
            handleAlert();
            return driver.findElement(by);
        }
    }

    /**
     * 点击方法
     */
    public static void click(By by) {
        //todo: 递归是更好的
        System.out.println("点击元素："+by);
        //logger.info("点击元素："+by);
        try {
            driver.findElement(by).click();
        } catch (Exception e) {
            //如果有弹窗导致元素找不到，则处理
            handleAlert();
            //处理完弹窗，继续点击
            driver.findElement(by).click();
        }
    }

    /**
     * 查找元素列表
     */
    public static List<WebElement> findElements(By by) {
        //System.out.println("待查找的元素列表：" + by);
        logger.info("待查找的元素列表：" + by);
        return driver.findElements(by);
    }

    /**
     * 处理弹窗
     */
    static void handleAlert() {
        //现将预知可能出现的弹窗，加入到数组列表中
        List<By> alertBoxs = new ArrayList<By>();
        alertBoxs.add(By.id("com.xueqiu.android:id/image_cancel"));
        alertBoxs.add(By.id("com.xueqiu.android:id/md_buttonDefaultNegative"));
        //需特殊处理的tips
        By tips=By.id("com.xueqiu.android:id/snb_tip_text");
        alertBoxs.add(tips);

        //修改隐式等待时间为3s
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        //循环处理数组列表中的元素
        alertBoxs.forEach(alert -> {
            List<WebElement> ads = driver.findElements(alert);

            if (alert.equals(tips)) {
                System.out.println("snb_tip found!");
                Dimension size = driver.manage().window().getSize();
                try {
                    if (driver.findElements(tips).size() >= 1) {
                        //在窗口的中间位置，进行触摸操作
                        new TouchAction(driver).tap(PointOption.point(size.width / 2, size.height / 2)).perform();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("snb_tip clicked!");
                }
            } else if (ads.size() >= 1) {
                ads.get(0).click();
            }
        });
        //还原隐式等待时间
        driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
    }

    /**
     * 通过PageSource处理弹窗
     */
    private static void handleAlertByPageSource() {
        //todo:xpath匹配，标记 定位
        String xml = driver.getPageSource();
        List<String> alertBoxs = new ArrayList<>();
        alertBoxs.add("");

        alertBoxs.forEach(alert -> {
            if (xml.contains(alert)) {
                driver.findElement(By.id(alert)).click();
            }
        });
    }

    //TODO parseSteps()方法
    public void parseSteps() {
        String method = Thread.currentThread().getStackTrace()[2].getMethodName();
        System.out.println(method);
        parseSteps(method);
    }

    private void parseSteps(String method) {
        //HashMap<String, List<HashMap<String, String>>> 可以取消steps的多余关键字
        //TODO: 参数化，把关键数据参数化到你的yaml中
        //获取配置文件路径
        String path = "/" + this.getClass().getCanonicalName().replace(".","/") + ".yaml";
        parseSteps(path,method);
    }

    private void parseSteps(String path, String method) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            model = mapper.readValue(
                    BasePage.class.getResourceAsStream(path), PageObjectModel.class
            );
            parseSteps(model.methods.get(method));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseSteps(PageObjectMethod steps) {
        steps.getSteps().forEach(step ->{
            WebElement element = null;

            //todo:多个可能定位，多平台支持，多版本的支持
            String id = step.get("id");
            if (id != null) {
                element = driver.findElement(By.id(id));
            }else if(step.get("xpath")!=null){
                element=driver.findElement(By.xpath(step.get("xpath")));
            }else if(step.get("aid")!=null){
                element=driver.findElement(MobileBy.AccessibilityId(step.get("aid")));
            }else if(step.get("element")!=null){
                element=driver.findElement(model.elements.get(step.get("element")).getLocator());
            }

            String send = step.get("send");
            if (send != null) {
                //配置文件中的参数替换
                for (Map.Entry<String, Object> kv : params.entrySet()) {
                    String matcher = "${"+kv.getKey()+"}";
                    if (send.contains(matcher)) {
                        System.out.println("kv: " + kv);
                        send = send.replace(matcher,kv.getValue().toString());
                    }
                }
                element.sendKeys(send);
            } else if (step.get("get") != null) {
                String attribute = element.getAttribute(step.get("get"));
                getResults().put(step.get("dump"),attribute);
            } else {
                element.click();
            }
        });

    }
}
