package com.automation.linkedin.pages;

import com.automation.linkedin.pages.search.popup.AddPeoplePopupPage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import lombok.SneakyThrows;
import org.openqa.selenium.By;

import java.time.Duration;
import java.util.Random;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class PersonPage {
    public SelenideElement moreBtn = $(By.xpath("//main[contains(@class,'scaffold-layout__main')]//span[contains(text(),'More')]"));
    public SelenideElement addBtn = $x("//main[contains(@class,'scaffold-layout__main')]//span[text()='Connect']");
    public SelenideElement getCannotAddLeadPopUpClose = $("#artdeco-toasts li-icon[type='cancel-icon']");
    public SelenideElement cannotAddLeadPopUp = $("#artdeco-toasts [role='alert']");
    public SelenideElement msgParagraph = $(".msg-form__msg-content-container .msg-form__contenteditable>p");
    public SelenideElement limitAlertHeader = $x("//h2[@id='ip-fuse-limit-alert__header']");
    public SelenideElement inMailMsgBtn = $x("//main//span[@class='artdeco-button__text'][normalize-space()='Message']");
    public SelenideElement premiumUpsellLink = $("div.premium-upsell-link");
    public SelenideElement premiumUpsellLinkCloseBtn = $x("//div[@role='dialog']//li-icon[@type='cancel-icon']");
    public SelenideElement inMailWindow = $x("//div[@aria-label='Messaging']");
    public SelenideElement inMailSubject = $x("//div[@aria-label='Messaging']//input[@placeholder='Subject (optional)']");
    public SelenideElement inMailMessege= $x("//div[@aria-label='Messaging']//div[@aria-label='Write a message…']");
    public SelenideElement inMailMessegeBtnSubmit= $x("//div[@aria-label='Messaging']//button[@type='submit']");
    public SelenideElement errorMsg= $x("//div[@data-test-artdeco-toast-item-type='error']//p//span");
    public SelenideElement msgBtn= $x("//main[contains(@class,'scaffold-layout__main')]//span[text()='Message']");
    public SelenideElement closeBtn = $x("//div[contains(@class,'msg-overlay-bubble-header__controls')]//*[contains(@href,'#close-small')]");
    public ElementsCollection closeBtns = $$x("//div[contains(@aria-label,'Messaging')]//div[contains(@class,'msg-overlay-bubble-header__controls')]/button[3]");
    private int count = 0;
   String JS_ADD_TEXT_TO_INPUT = "var elm = arguments[0], txt = arguments[1];\n" +
           "  elm.value += txt;\n" +
           "  elm.dispatchEvent(new Event('change'));\n";

    AddPeoplePopupPage addPeoplePopupPage = new AddPeoplePopupPage();
    Random random = new Random();
    int low = 1000;
    int high = 4000;
    int randomResult = random.nextInt(high-low) + low;

    @SneakyThrows
    public boolean addLead(String message, boolean isPremiumTruem){

        Thread.sleep(randomResult);
        clickMoreBtn();
/*        if (inMailMsgBtn.exists()) {
            clickCloseMessageWindow();
            clickMessageBtn();
            Thread.sleep(randomResult);
            if (inMailSubject.exists()){
                closeInMailPopUp();
                inMailSubject.shouldBe(visible).shouldBe(interactable).setValue("Cooperation");
                    Thread.sleep(randomResult);
                    inMailMessege.shouldBe(interactable).click();
                    Thread.sleep(randomResult);
                    try {inMailMessege.shouldBe(interactable).sendKeys(message);}catch (WebDriverException ignore){}
                    Thread.sleep(randomResult);
                    inMailMessegeBtnSubmit.shouldBe(interactable).click();
                    Thread.sleep(randomResult);
                    clickCloseMessageWindow();
                count += 1;
                //System.out.println("In maild sent = " + count);
        }
            if (premiumUpsellLink.exists()){
                Thread.sleep(3000);
                closePremiumAd();
                addToFriends(message);
                return;
            }
            if (isPremiumTrue){ sendInMailMsgPremium(message); }
        }
        else {
            addToFriends(message);
        }*/

       return addToFriends(message,false);


    }

    public void closeInMailPopUp(){
        if ( inMailWindow.is(visible) && inMailSubject.is(not(visible)) ) {
            $x("//div[@aria-label='Messaging']//button[2]").click();
        }
    }

    public void sendInMailMsgPremium(String message){
        if ($(".msg-overlay-conversation-bubble").is(visible)) return;
        if ( premiumUpsellLink.is(not(visible)) && inMailSubject.is(not(visible)) ) {
            Selenide.switchTo().window(2);
            WebDriverRunner.getWebDriver().getCurrentUrl();
            if (WebDriverRunner.getWebDriver().getCurrentUrl().contains("https://www.linkedin.com/sales")){
                $x("//input[@placeholder='Subject (required)']").setValue("Cooperation");
                $x("//textarea[@placeholder='Type your message here…']").setValue(message);
                $x("//span[text()='Send']").click();
                Selenide.closeWindow();
                switchTo().window(1);
            }
        }
    }

    public void clickMoreBtn(){
        moreBtn.should(exist, Duration.ofSeconds(20));
        moreBtn.shouldBe(visible, Duration.ofSeconds(20));
        moreBtn.shouldBe(interactable, Duration.ofSeconds(20));
        moreBtn.click();
    }


    public void clickCloseMessageWindow(){
        if ($x("//div[@aria-label='Messaging']//button[3]").exists()) $x("//div[@aria-label='Messaging']//button[3]").shouldBe(visible).click();
        if ($x("//div[@aria-label='Messaging']//button[2]").exists()) $x("//div[@aria-label='Messaging']//button[2]").shouldBe(visible).click();
    }

    public void clickMessageBtn(){
        inMailMsgBtn.should(exist, Duration.ofSeconds(20));
        inMailMsgBtn.shouldBe(visible, Duration.ofSeconds(20));
        inMailMsgBtn.shouldBe(interactable, Duration.ofSeconds(20));
        inMailMsgBtn.click();

    }

    public void closePremiumAd(){ premiumUpsellLinkCloseBtn.shouldBe(interactable, Duration.ofSeconds(30)).click(); }
    @SneakyThrows
    public boolean addToFriends(String message, boolean withMassage){

    if (!addBtn.is(visible)) clickMoreBtn();
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
            if (withMassage){
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
            }
                if (limitAlertHeader.isDisplayed()){
                    System.out.println("\n========================================================================\n" +"Out of requests"+ "\n========================================================================\n");
                    WebDriverRunner.getWebDriver().quit(); }
                Selenide.executeJavaScript("window.scrollTo(2000, document.body.scrollHeight)");
            }else {
                Thread.sleep(randomResult);
                if (addPeoplePopupPage.requireEmailField.isDisplayed()) {
                    Thread.sleep(randomResult);
                    addPeoplePopupPage.requireEmailField.setValue("info@wisevisionllc.com");
                    Thread.sleep(randomResult);
                }
                if (this.errorMsg.exists()) return false;
                if (addPeoplePopupPage.sendRequestBtn.is(visible)){addPeoplePopupPage.sendRequestBtn.shouldBe(interactable).click();}
                else addPeoplePopupPage.sendRequestBtnWithoutNote.shouldBe(visible,Duration.ofSeconds(40)).click();
                if (limitAlertHeader.isDisplayed()){
                    System.out.println("\n========================================================================\n" +"Out of requests"+ "\n========================================================================\n");
                    WebDriverRunner.getWebDriver().quit(); }
            }
            return true;
        }
        return false;
    }
    @SneakyThrows
    public boolean sentMsg(String msg){
        Thread.sleep(10000);
        if(msgParagraph.is(visible)){msgParagraph.clear();}
        if(msgParagraph.is(visible)){msgParagraph.clear();}
        if(msgParagraph.is(visible)){msgParagraph.clear();}
        if(msgParagraph.is(visible)){msgParagraph.clear();}
        if(!$x("//button[normalize-space()='Send']").is(visible)) {
            if (closeBtn.is(interactable)) closeBtn.click();
            return false;
        };
        $$x("//div[contains(@aria-label,'Write a message…')]").last().click();
        $$x("//div[contains(@aria-label,'Write a message…')]").last().sendKeys(msg);
        Thread.sleep(5000);
        $x("//button[normalize-space()='Send']").click();
        Thread.sleep(5000);
        closeBtn.click();

        if (closeBtn.is(interactable)) closeBtn.click();

        return true;
    }
}
