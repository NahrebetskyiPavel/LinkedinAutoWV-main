package com.automation.linkedin;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Base {

    Random random = new Random();
    int low = 2000;
    int high = 5000;
    int randomResult = random.nextInt(high-low) + low;
    public static void setupBrowser(boolean isRemote, String name){
        if (isRemote){
            Configuration.driverManagerEnabled = false;
            Configuration.remote = "http://34.118.114.182:4444/wd/hub";
            ChromeOptions options = new ChromeOptions();
            options.setCapability("selenoid:options", new HashMap<String, Object>() {{
                // How to add test badge
                put("name", name);

                // How to set session timeout
                put("sessionTimeout", "15m");

                // How to set timezone
                put("env", new ArrayList<String>() {{
                    add("TZ=UTC");
                }});

                // How to add "trash" button
                put("labels", new HashMap<String, Object>() {{
                    put("manual", "true");
                }});

                // How to enable video recording
                put("enableVideo", false);
                put("enableVNC", true);
            }});

            Configuration.browserCapabilities = options;
        }
    }
    public static void openLinkedInLoginPage(){
        Selenide.open("https://www.linkedin.com/login");
        WebDriverRunner.getWebDriver().quit();
        Selenide.open("https://www.linkedin.com/login");
    }
}
