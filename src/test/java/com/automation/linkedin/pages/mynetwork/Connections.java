package com.automation.linkedin.pages.mynetwork;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class Connections {
ElementsCollection MessageBtn = $$x("//div[@class='entry-point']/button");
SelenideElement messageOverlayConversationBubbleItem = $x("//div[@data-view-name='message-overlay-conversation-bubble-item']");
}
