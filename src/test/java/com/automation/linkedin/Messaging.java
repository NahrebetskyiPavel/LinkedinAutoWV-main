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
                {       "Денис Australia CTO",
                        clientName,
                        "basdenisphytontm@gmail.com",
                        "asd321qq",
                        "https://www.linkedin.com/messaging",
                        "Hello there, As a full-stack web, mobile, and software development company based in western Ukraine, we specialize in providing customized solutions to businesses like yours.\n" +
                                " We have a team of highly skilled developers who can help take your business to the next level. Whether you need a website, mobile app, or software solution, we have the expertise to deliver exceptional results.\n" +
                                " If you currently have a specific request or problem that needs solving, I'd be happy to help. \n" +
                                " Best regards,\n",
                        "Pavlo",
                        "Australia Linkedin",
                        leadCompanyAustraliaId
                },
        };
    }
}
