package com.automation.linkedin.pages;

import com.automation.linkedin.pages.search.popup.AddPeoplePopupPage;
import com.automation.linkedin.pages.search.popup.MsgPopUpPage;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.SetValueOptions;
import com.codeborne.selenide.WebDriverRunner;
import lombok.SneakyThrows;
import org.openqa.selenium.By;

import java.time.Duration;
import java.util.Random;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class PersonPage {
    public SelenideElement moreBtn = $(By.xpath("//main[contains(@class,'scaffold-layout__main')]//span[contains(text(),'More')]"));
    public SelenideElement addBtn = $x("//main[contains(@class,'scaffold-layout__main')]//span[text()='Connect']");
    public SelenideElement getCannotAddLeadPopUpClose = $("#artdeco-toasts li-icon[type='cancel-icon']");
    public SelenideElement cannotAddLeadPopUp = $("#artdeco-toasts [role='alert']");
    public SelenideElement limitAlertHeader = $x("//h2[@id='ip-fuse-limit-alert__header']");
    public SelenideElement inMailMsgBtn = $x("//div[@class='entry-point pvs-profile-actions__action']//span[@class='artdeco-button__text'][normalize-space()='Message']");
    public SelenideElement premiumUpsellLink = $("div.premium-upsell-link");
    public SelenideElement premiumUpsellLinkCloseBtn = $x("//div[@role='dialog']//li-icon[@type='cancel-icon']");
    public SelenideElement inMailWindow = $x("//div[@aria-label='Messaging']");
    public SelenideElement inMailSubject = $x("//div[@aria-label='Messaging']//input[@placeholder='Subject (optional)']");
    public SelenideElement inMailMessege= $x("//div[@aria-label='Messaging']//div[@aria-label='Write a message…']");
    public SelenideElement inMailMessegeBtnSubmit= $x("//div[@aria-label='Messaging']//button[@type='submit']");
   String JS_ADD_TEXT_TO_INPUT = "var elm = arguments[0], txt = arguments[1];\n" +
           "  elm.value += txt;\n" +
           "  elm.dispatchEvent(new Event('change'));\n";

    AddPeoplePopupPage addPeoplePopupPage = new AddPeoplePopupPage();
    Random random = new Random();
    int low = 1000;
    int high = 4000;
    int randomResult = random.nextInt(high-low) + low;
    @SneakyThrows
    public void addLead(String message){

        Thread.sleep(randomResult);
        if (inMailMsgBtn.exists()) {
            if ($x("//div[@aria-label='Messaging']//button[3]").exists())$x("//div[@aria-label='Messaging']//button[3]").shouldBe(visible).click();
//        if ($x("//div[@aria-label='Messaging']//button[2]").exists())$x("//div[@aria-label='Messaging']//button[3]").shouldBe(visible).click();
            if (inMailMsgBtn.exists()) { inMailMsgBtn.shouldBe(visible).shouldBe(interactable).click(); }
            if (premiumUpsellLink.exists() || !inMailSubject.exists()){
                Thread.sleep(3000);
                if (premiumUpsellLink.is(visible)){
                    premiumUpsellLinkCloseBtn.shouldBe(interactable, Duration.ofSeconds(30)).click();
                }
                moreBtn.should(exist, Duration.ofSeconds(20));
                moreBtn.shouldBe(visible, Duration.ofSeconds(20));
                moreBtn.shouldBe(interactable, Duration.ofSeconds(20));
                moreBtn.click();
                if (addBtn.exists())
                {
                    Thread.sleep(randomResult);
                    addBtn.shouldBe(visible).click();
                    if (addPeoplePopupPage.otherBtn.isDisplayed()){
                        Thread.sleep(randomResult);
                        addPeoplePopupPage.otherBtn.click();
                        Thread.sleep(randomResult);
                        $(By.xpath("//*[text()='Connect']")).click();
                    }
                    Thread.sleep(randomResult);
                    addPeoplePopupPage.addNote.shouldBe(visible);
                    addPeoplePopupPage.addNote.shouldBe(interactable).click();
                    Thread.sleep(randomResult);
                    addPeoplePopupPage.addNoteTextField
                            .shouldBe(visible, Duration.ofSeconds(30))
                            .setValue(message);
                    if (addPeoplePopupPage.requireEmailField.isDisplayed()) {
                        Thread.sleep(randomResult);
                        addPeoplePopupPage.requireEmailField.setValue("info@wisevisionllc.com");
                        Thread.sleep(randomResult);
                        addPeoplePopupPage.sendRequestBtn.shouldBe(visible).click();
                        Thread.sleep(randomResult);
                        if (limitAlertHeader.isDisplayed()){
                            System.out.println("\n========================================================================\n" +"Out of requests"+ "\n========================================================================\n");
                            WebDriverRunner.getWebDriver().quit(); }
                        Selenide.executeJavaScript("window.scrollTo(2000, document.body.scrollHeight)");
                    }else {
                        Thread.sleep(randomResult);
                        addPeoplePopupPage.sendRequestBtn.shouldBe(visible,Duration.ofSeconds(40)).click();
                        if (limitAlertHeader.isDisplayed()){
                            System.out.println("\n========================================================================\n" +"Out of requests"+ "\n========================================================================\n");
                            WebDriverRunner.getWebDriver().quit(); }
                    }
                    if (cannotAddLeadPopUp.isDisplayed()){ getCannotAddLeadPopUpClose.click(); Selenide.refresh(); }
                }
                return;
            }

/*            if (!inMailMessegeBtnSubmit.exists()){
                $x("//div[@aria-label='Messaging']//button[2]").click();
                return;
            }*/
            Thread.sleep(randomResult+2000);
            inMailSubject.shouldBe(visible).shouldBe(interactable).setValue("Cooperation");
            Thread.sleep(randomResult);
            inMailMessege.shouldBe(interactable).click();
            Thread.sleep(randomResult);
            System.out.println(message);
            inMailMessege.shouldBe(interactable).sendKeys(message);
            Thread.sleep(randomResult);
            inMailMessegeBtnSubmit.shouldBe(interactable).click();
            Thread.sleep(randomResult);
            $x("//div[@aria-label='Messaging']//button[2]").click();
        }

    }
}
