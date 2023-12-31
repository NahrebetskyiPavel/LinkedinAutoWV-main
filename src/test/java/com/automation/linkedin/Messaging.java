package com.automation.linkedin;

import com.automation.linkedin.pages.login.SignInPage;
import com.automation.linkedin.pages.messaging.MessagingPage;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import lombok.SneakyThrows;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.interactable;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class Messaging extends Base{
    SignInPage signInPage = new SignInPage();
    MessagingPage messagingPage = new MessagingPage();
    @SneakyThrows
    @Test(description = "send FollowUp Msg", dataProvider = "dataProviderPeopleSearch")
    public void sendFolowUpMsg(String name, String clientName, String email, String password, String msgLink, String msg, String pickList, String leadCompany, String leadCompanyId){
        setupBrowser(true, name);
        openLinkedInLoginPage();
        signInPage.signIn(randomResult, email, password);
        WebDriverRunner.getWebDriver().manage().window().maximize();
        Selenide.open(msgLink);
        messagingPage.writeAMessageField.shouldBe(visible,Duration.ofSeconds(10));
        for (int i = 0; i < 100; i++) {
            for (SelenideElement msgCard:messagingPage.msgCards)
            {
                msgCard.shouldBe(interactable, Duration.ofSeconds(20));
                Thread.sleep(1000*2);
                if (msgCard.text().contains("Ahoj!"))
                {
                    msgCard.click();
                    Thread.sleep(randomResult*3);
                    messagingPage.writeAMessageField.shouldBe(visible,Duration.ofSeconds(10)).shouldBe(interactable);
                    messagingPage.writeAMessageField.click();
                    messagingPage.writeAMessageField.setValue(msg);
                    messagingPage.writeAMessageField.click();
                    Thread.sleep(randomResult*3);
                    $x("//button[normalize-space()='Send']").shouldBe(interactable).click();
                }
                if ($x("//*[text()='Load more conversations']").is(visible)){
                    $x("//*[text()='Load more conversations']").shouldBe(interactable).click();
                    Thread.sleep(5000);
                }
            }
            messagingPage.msgCards.get(6+i).scrollIntoView(true);
        }

    }

    @DataProvider(name = "dataProviderPeopleSearch", parallel=true)
    public static Object[][] dataProviderPeopleSearch() {
        String clientName = "";
        String leadCompanyGamblingId ="421659000005125089";
        String leadCompanyAmsterdamId ="421659000005261283";
        String leadCompanyAustraliaId ="421659000005261273";
        String leadCompanyName ="Gambling LinkedIn";
        return new Object[][]{
                {       "Денис",
                        clientName,
                        "basdenisphytontm@gmail.com",
                        "asd321qq",
                        "https://www.linkedin.com/messaging",
                        "Thanks for connecting, .\n" +
                                "\n" +
                                "How are you on this nice day? :)\n" +
                                "\n" +
                                "I was hoping that you would enlighten me if there is a demand for web development services in the Saudi Arabian market. The thing is: I represent a team of creative web developers of a Ukrainian IT company WiseVision. If you have some demand for custom soft dev we’ll be happy to coop with you, as we have experts in many different fields of IT.\n" +
                                "\n" +
                                "Just let me know if you are interested.\n",
                        "Pavlo",
                        "Australia Linkedin",
                        leadCompanyAustraliaId
                },
        };
    }
}
