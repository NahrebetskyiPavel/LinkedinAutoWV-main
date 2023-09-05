package com.automation.linkedin.pages.search;

import com.automation.linkedin.pages.search.popup.AddPeoplePopupPage;
import com.automation.linkedin.pages.search.popup.MsgPopUpPage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.SneakyThrows;
import org.openqa.selenium.By;

import java.util.Random;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class SearchPeoplePage {
    AddPeoplePopupPage addPeoplePopupPage = new AddPeoplePopupPage();
    MsgPopUpPage msgPopUpPage = new MsgPopUpPage();

    public SelenideElement addBtn = Selenide.$(By.xpath("//*[text()='Connect']"));

    public ElementsCollection PersonPages = $$x("//ul[contains(@class,'reusable-search__entity-result-list list-style-none')]//li//span[contains(@class,'entity-result__title-text')]/a[contains(@class,'app-aware-link ')]");
    public ElementsCollection addButtons = Selenide.$$(By.xpath("//*[text()='Connect']"));

    public SelenideElement nextPageBtn = Selenide.$("li-icon[type='chevron-right-icon']");
    public SelenideElement previousPageBtn = Selenide.$("li-icon[type='chevron-left-icon']");
    public SelenideElement cannotAddLeadPopUp = Selenide.$("#artdeco-toasts [role='alert']");
    public SelenideElement getCannotAddLeadPopUpClose = Selenide.$("#artdeco-toasts li-icon[type='cancel-icon']");
    Random random = new Random();
    int low = 1;
    int high = 10;
    int randomResult = random.nextInt(high-low) + low;

    @SneakyThrows
    public void addLead(String message){


        Thread.sleep(3000);
        if (addBtn.exists())
        {
             addBtn.shouldBe(visible).click();
              if (addPeoplePopupPage.otherBtn.isDisplayed()){
                   addPeoplePopupPage.otherBtn.click();
                    Selenide.$(By.xpath("//*[text()='Connect']")).click();
                }
               addPeoplePopupPage.addNote.shouldBe(visible).click();
               addPeoplePopupPage.addNoteTextField.shouldBe(visible).setValue(message);
                if (addPeoplePopupPage.requireEmailField.isDisplayed()) {
                    addPeoplePopupPage.requireEmailField.setValue("info@wisevisionllc.com");
                    addPeoplePopupPage.sendRequestBtn.shouldBe(visible).click();
                    Selenide.executeJavaScript("window.scrollTo(2000, document.body.scrollHeight)");
                    previousPageBtn.shouldBe(visible).click();
                }else {
                    addPeoplePopupPage.sendRequestBtn.shouldBe(visible).click();
                }
                if (cannotAddLeadPopUp.isDisplayed()){ getCannotAddLeadPopUpClose.click(); Selenide.refresh(); }
        }
        else {
            Selenide.executeJavaScript("window.scrollTo(2000, document.body.scrollHeight)");
            if (addBtn.exists()) return;
            if (msgPopUpPage.closeMsgPopUp.exists()) {msgPopUpPage.closeMsgPopUp.click(); return;}
            Thread.sleep(1000);
            previousPageBtn.shouldBe(visible).click();
            Thread.sleep( 2000);
        }
    }
}
