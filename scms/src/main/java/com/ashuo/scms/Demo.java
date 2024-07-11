package com.ashuo.scms;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;

public class Demo {
    public static void main(String[] args) {
        //引入谷歌驱动
        ChromeOptions options = new ChromeOptions();
        //允许所有请求
        options.addArguments("--remote-allow-origins=*");
        WebDriver webDriver = new ChromeDriver(options);
        //启动需要打开的网页
        webDriver.get("http://localhost:8080/");
        // 利用id定位元素:百度输入框,并在输入框中输入:软件测试
        webDriver.findElement(By.id("name")).sendKeys("ashuo");
        webDriver.findElement(By.id("password")).sendKeys("123456");
        webDriver.findElement(By.id("login")).click();
        Set<String> handles = webDriver.getWindowHandles();
        String target_handle = "";
        for (String handle : handles) {
            target_handle = handle;
        }
        webDriver.switchTo().window(target_handle);
        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("100"))).click();
        webDriver.get("http://localhost:8080/season/seasonlist");
        webDriver.findElement(By.id("add")).click();
        webDriver.findElement(By.id("seasonName")).sendKeys("2021年秋季运动会");
        webDriver.findElement(By.id("seasonTopicDesc")).sendKeys("2021年秋季运动会");
        webDriver.findElement(By.id("seasonBeginTime")).sendKeys("2021-09-10 00:00:00");
        webDriver.findElement(By.id("seasonEndTime")).sendKeys("2021-09-20 00:00:00");
        webDriver.findElement(By.className("el-button")).click();
        Select select = new Select(webDriver.findElement(By.id("seasonStatus")));
        select.selectByIndex(0); //下标从0开始

        webDriver.findElement(By.id("submit")).click();
        webDriver.quit();
    }
}
