package com.automation.linkedin;

import api.helpers.ZohoCrmHelper;
import com.automation.linkedin.pages.PersonPage;
import com.automation.linkedin.pages.login.SignInPage;
import com.automation.linkedin.pages.messaging.MessagingPage;
import com.automation.linkedin.pages.search.SearchPeoplePage;
import com.codeborne.selenide.*;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;

import org.testng.annotations.Test;

import java.util.Random;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class AddLeads extends Base {
    SignInPage signInPage = new SignInPage();
    SearchPeoplePage searchPeoplePage = new SearchPeoplePage();
    PersonPage personPage = new PersonPage();
    MessagingPage messagingPage = new MessagingPage();
    ZohoCrmHelper zohoCrmHelper = new ZohoCrmHelper();
    Random random = new Random();
    int low = 2000;
    int high = 5000;
    int randomResult = random.nextInt(high-low) + low;

    @SneakyThrows
    @Test(description = "add leads from CRM", dataProvider = "dataProviderPeopleSearch", alwaysRun = true )
    public void addLeads(String name, String email, String password,  String msg, String linkedinperson){


        setupBrowser(true, name);
        Thread.sleep(randomResult*3);
        openLinkedInLoginPage();
        signInPage.signIn(randomResult, email, password);
        System.out.println("-------------------------------------------------------\n" +
                "START: "+name+"\n" +
                "-------------------------------------------------------");
        Thread.sleep(1000*20);
        Thread.sleep(randomResult);
        WebDriverRunner.getWebDriver().manage().window().maximize();
        Thread.sleep(10000*10000);

    }

    @DataProvider(name = "dataProviderPeopleSearch", parallel=true)
    public static Object[][] dataProviderPeopleSearch() {

        return new Object[][]{
                {       "Dymitr Tolmach",
                        "dymitr.tolmach1012@outlook.com",
                        "33222200Shin",
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        "Dymitr Tolmach"
                },
        };
    }
}
