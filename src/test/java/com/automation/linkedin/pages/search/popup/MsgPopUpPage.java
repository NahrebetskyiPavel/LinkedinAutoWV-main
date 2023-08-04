package com.automation.linkedin.pages.search.popup;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

public class MsgPopUpPage {
    public SelenideElement closeMsgPopUp = Selenide.$(By.xpath("//header[contains(@class, 'msg-overlay-bubble-header msg-overlay-conversation-bubble-header ')]//button[contains(@class, 'msg-overlay-bubble-header__control artdeco-button artdeco-button--circle artdeco-button--muted artdeco-button--1 artdeco-button--tertiary')]"));

}
