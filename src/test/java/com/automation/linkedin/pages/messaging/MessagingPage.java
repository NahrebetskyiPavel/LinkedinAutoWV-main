package com.automation.linkedin.pages.messaging;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MessagingPage {

    public SelenideElement msgConversationCardContent = $(By.xpath("//div[contains(@class, 'msg-conversation-card__content--selectable')]//p"));
    public SelenideElement writeAMessageField = $(By.xpath("//div[contains(@aria-label,'Write a message…')]"));
    public ElementsCollection msgCards = $$("p.msg-conversation-card__message-snippet");

}
