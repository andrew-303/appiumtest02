package com.hgwz.appium.xueqiu.page;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 基础页面
 */
public class BasePage {
    private static Logger logger = LoggerFactory.getLogger(BasePage.class);
    /**
     * 定义driver
     */
    public static AndroidDriver<WebElement> driver;

    /**
     * 查找元素方法
     * @param by
     * @return WebElement
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
     * @param by
     */
    public static void click(By by) {
        //todo: 递归是更好的
        //System.out.println("点击元素："+by);
        logger.info("点击元素："+by);
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
}
