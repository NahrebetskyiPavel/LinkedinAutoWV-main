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
                $x("//div[contains(@aria-label,'Write a message…')]").sendKeys("Hello.\n" +
                        "\n" +
                        "Thanks for connecting.\n" +
                        "\n" +
                        "I was wondering if you have met any technical challenges as of late. I am a member of the WiseVision team, a software development company that specializes in web development and custom software for businesses. We are really interested in collaboration with your company. If you have a need for any kind of IT solutions, I would be happy to schedule a call with our tech lead.\n");
                $x("//button[normalize-space()='Send']").click();
                $x("//div[contains(@aria-label,'Messaging')]//div[contains(@class,'msg-overlay-bubble-header__controls')]/button[3]").click();
            }
        }
    }

    @DataProvider(name = "dataProviderPeopleSearch", parallel=true)
    public static Object[][] dataProviderPeopleSearch() {
        return new Object[][]{
                {       "Александра - Saudi Arabia Board of directors",
                        "alexandra.sternenko@gmail.com",
                        "asd321qq",
                        "Hello",
                },
                {       "Маша - Stockholm Founder ",
                        "deynekamariawv@gmail.com",
                        "3N2wbnsw",
                        "Hello",
                },
                {       "Михайло - Saudi Arabia CFO",
                        "michael.salo1995@gmail.com",
                        "newman1996",
                        "Hello",
                },
/*                {       "Nikita - Stockholm board of directors",
                        "kni2012@ukr.net",
                        "33222200s",
                        "Hello",

                },*/
                {       "Наталья- Stockholm CEO",
                        "natalia.marcoon@gmail.com ",
                        "asd321qq",
                        "Hello",
                },
                {       "Денис - Saudi Arabia CEO",
                        "basdenisphytontm@gmail.com",
                        "asd321qq",
                        "Hello",
                },
                {       "Настя - Stuttgart CEO",
                        "anastasiiakuntii@gmail.com",
                        "nastya4141",
                        "Hello",
                },
                {       "Роксолана - Stockholm CFO",
                        "roksolanatrofim@gmail.com ",
                        "89fcmTT88V",
                        "Hello",
                },
                {       "Марьян -  Stockholm CTO",
                        "reshetunmaryanwv@gmail.com",
                        "rSbnGaRS",
                        "Hello",
                },
                {       "Максим - Stockholm co-founder",
                        "kotokmaksym@gmail.com",
                        "r4E3w2q1",
                        "Hello",
                },
                {       "Анастасия - Saudi Arabia owner",
                        "vozniakanastasia52@gmail.com",
                        "zdHXF5bf",
                        "Hello",
                }
        };
    }
}
