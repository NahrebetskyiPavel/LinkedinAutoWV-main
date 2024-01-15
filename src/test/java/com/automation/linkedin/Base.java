package com.automation.linkedin;

import com.codeborne.selenide.*;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static com.codeborne.selenide.Selenide.$$x;

public class Base {

    Random random = new Random();
    int low = 2000;
    int high = 5000;
    int randomResult = random.nextInt(high-low) + low;
    public static void setupBrowser(boolean isRemote, String name){
        if (isRemote){
            Configuration.driverManagerEnabled = false;
            Configuration.remote = "http://34.116.212.22:4444/wd/hub";
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
            Configuration.screenshots = false;
        }
    }
    public static void openLinkedInLoginPage(){
        Selenide.open("https://www.linkedin.com/login");
        WebDriverRunner.getWebDriver().quit();
        Selenide.open("https://www.linkedin.com/login");
    }

    public static void closeMsgPopups(){
        ElementsCollection msgPopUpClose = $$x("//div[@aria-label='Messaging']//button[3]");
        if (msgPopUpClose.first().exists()){
            for (SelenideElement closeMsgPopUp:msgPopUpClose
            ) {
                closeMsgPopUp.click();
            }
        }
    }
}
