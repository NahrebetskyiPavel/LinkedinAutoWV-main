package com.automation.linkedin;

import com.automation.linkedin.pages.login.SignInPage;
import com.automation.linkedin.pages.messaging.MessagingPage;
import com.automation.linkedin.pages.mynetwork.Connections;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import lombok.SneakyThrows;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.interactable;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class Messaging extends Base{
    SignInPage signInPage = new SignInPage();
    MessagingPage messagingPage = new MessagingPage();
    Connections connections = new Connections();
    @SneakyThrows
    @Test(description = "send FollowUp Msg", dataProvider = "dataProviderPeopleSearch")
    public void sendFolowUpMsg(String name, String email, String password, String msg){
        setupBrowser(true, name);
        openLinkedInLoginPage();
        signInPage.signIn(randomResult, email, password);
        WebDriverRunner.getWebDriver().manage().window().maximize();
        Selenide.open("https://www.linkedin.com/mynetwork/invite-connect/connections/");
        Thread.sleep(10000);
        for (int i = 0; i < 10; i++) { Selenide.executeJavaScript("window.scrollTo(2000, document.body.scrollHeight)"); }
        Selenide.executeJavaScript("window.scrollTo(-20000, document.body.scrollHeight)");
        Selenide.executeJavaScript("window.scrollTo(0, -2000);\n");
        Thread.sleep(10000);
        for (SelenideElement messageBtn: connections.messageBtns) {
            if ($x("//div[contains(@aria-label,'Messaging')]//div[contains(@class,'msg-overlay-bubble-header__controls')]/button[3]").exists()){
                for (SelenideElement close: $$x("//div[contains(@aria-label,'Messaging')]//div[contains(@class,'msg-overlay-bubble-header__controls')]/button[3]")) {
                    close.click();
                }
            }
            messageBtn.click();
            Thread.sleep(5000);
            if (   $x("//ul[contains(@class,'msg-s-message-list-content')]").exists() &&    $x("//ul[contains(@class,'msg-s-message-list-content')]").text().length()>0){

                $x("//div[contains(@aria-label,'Messaging')]//div[contains(@class,'msg-overlay-bubble-header__controls')]/button[3]").click();
            }
            else {
                $x("//div[contains(@aria-label,'Write a message…')]").click();
                $x("//div[contains(@aria-label,'Write a message…')]").sendKeys("Hello");
                $x("//button[normalize-space()='Send']").click();
                $x("//div[contains(@aria-label,'Messaging')]//div[contains(@class,'msg-overlay-bubble-header__controls')]/button[3]").click();
            }
        }
    }

    @DataProvider(name = "dataProviderPeopleSearch", parallel=true)
    public static Object[][] dataProviderPeopleSearch() {
        return new Object[][]{
                {       "roksolanatrofim",
                        "roksolanatrofim@gmail.com ",
                        "89fcmTT88V",
                        "Thanks for connecting, .\n" +
                                "\n" +
                                "How are you on this nice day? :)\n" +
                                "\n" +
                                "I was hoping that you would enlighten me if there is a demand for web development services in the Saudi Arabian market. The thing is: I represent a team of creative web developers of a Ukrainian IT company WiseVision. If you have some demand for custom soft dev we’ll be happy to coop with you, as we have experts in many different fields of IT.\n" +
                                "\n" +
                                "Just let me know if you are interested.\n",

                },
        };
    }
}
