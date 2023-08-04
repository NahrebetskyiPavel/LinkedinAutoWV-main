package com.automation.linkedin.pages;

import com.automation.linkedin.pages.search.popup.AddPeoplePopupPage;
import com.automation.linkedin.pages.search.popup.MsgPopUpPage;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.SneakyThrows;
import org.openqa.selenium.By;

import java.util.Random;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class PersonPage {
    public SelenideElement moreBtn = Selenide.$(By.xpath("//main[contains(@class,'scaffold-layout__main')]//span[contains(text(),'More')]"));
    public SelenideElement addBtn = $x("//main[contains(@class,'scaffold-layout__main')]//span[text()='Connect']");
    public SelenideElement getCannotAddLeadPopUpClose = Selenide.$("#artdeco-toasts li-icon[type='cancel-icon']");
    public SelenideElement cannotAddLeadPopUp = Selenide.$("#artdeco-toasts [role='alert']");
    public SelenideElement limitAlertHeader = $x("//h2[@id='ip-fuse-limit-alert__header']");

    AddPeoplePopupPage addPeoplePopupPage = new AddPeoplePopupPage();
    Random random = new Random();
    int low = 1000;
    int high = 4000;
    int randomResult = random.nextInt(high-low) + low;
    @SneakyThrows
    public void addLead(String message){


        Thread.sleep(randomResult);
        if (addBtn.exists())
        {
            Thread.sleep(randomResult);
            addBtn.shouldBe(visible).click();
            if (addPeoplePopupPage.otherBtn.isDisplayed()){
                Thread.sleep(randomResult);
                addPeoplePopupPage.otherBtn.click();
                Thread.sleep(randomResult);
                Selenide.$(By.xpath("//*[text()='Connect']")).click();
            }
            Thread.sleep(randomResult);
            addPeoplePopupPage.addNote.shouldBe(visible).click();
            Thread.sleep(randomResult);
            addPeoplePopupPage.addNoteTextField.shouldBe(visible).setValue(message);
            if (addPeoplePopupPage.requireEmailField.isDisplayed()) {
                Thread.sleep(randomResult);
                addPeoplePopupPage.requireEmailField.setValue("info@wisevisionllc.com");
                Thread.sleep(randomResult);
                addPeoplePopupPage.sendRequestBtn.shouldBe(visible).click();
                Thread.sleep(randomResult);
                Selenide.executeJavaScript("window.scrollTo(2000, document.body.scrollHeight)");
            }else {
                Thread.sleep(randomResult);
                addPeoplePopupPage.sendRequestBtn.shouldBe(visible).click();
            }
            if (cannotAddLeadPopUp.isDisplayed()){ getCannotAddLeadPopUpClose.click(); Selenide.refresh(); }
        }

    }
}
