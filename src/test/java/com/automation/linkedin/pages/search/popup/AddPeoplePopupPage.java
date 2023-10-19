package com.automation.linkedin.pages.search.popup;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

public class AddPeoplePopupPage {
        public SelenideElement addNote = Selenide.$(".artdeco-modal__actionbar.ember-view.text-align-right span");
        public SelenideElement addNoteTextField = Selenide.$("#custom-message");
        public SelenideElement sendRequestBtn = Selenide.$(By.xpath("//*[text()='Send']"));
        public SelenideElement sendRequestBtnWithoutNote = Selenide.$(By.xpath("//*[text()='Send without a note']"));
        public SelenideElement requireEmailField = Selenide.$("input[name='email']");

        public SelenideElement otherBtn = Selenide.$("button[aria-label='Other']");



}
