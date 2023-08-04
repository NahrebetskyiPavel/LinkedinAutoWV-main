package com.automation.linkedin.pages.login;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.SneakyThrows;

import static com.codeborne.selenide.Condition.visible;

public class SignInPage {
    public SelenideElement loginField = Selenide.$("#username");
    public SelenideElement passwordField = Selenide.$("#password");

    @SneakyThrows
    public void signIn(int wait, String email, String password) {
        Thread.sleep(10*1000);
        this.loginField.shouldBe(visible).setValue(email);
        this.passwordField.shouldBe(visible).setValue(password).pressEnter();
        Thread.sleep(wait);
    }
}
