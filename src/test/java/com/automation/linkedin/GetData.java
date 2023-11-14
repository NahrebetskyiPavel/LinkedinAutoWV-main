package com.automation.linkedin;

import api.helpers.WiseVisionApiHelper;
import api.helpers.ZohoCrmHelper;
import com.automation.linkedin.pages.PersonPage;
import com.automation.linkedin.pages.login.SignInPage;
import com.automation.linkedin.pages.search.SearchPeoplePage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import lombok.SneakyThrows;
import okhttp3.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.interactable;
import static com.codeborne.selenide.Selenide.*;

public class GetData extends Base{
    SignInPage signInPage = new SignInPage();
    SearchPeoplePage searchPeoplePage = new SearchPeoplePage();
    PersonPage personPage = new PersonPage();
    ZohoCrmHelper zohoCrmHelper = new ZohoCrmHelper();


    SelenideElement aboutHeader = $("div[id='about'] +div");
    SelenideElement aboutBody = $("div[id='about'] +div +div");
    SelenideElement seeMoreBtn = $("div[id='about'] +div +div button");
    SelenideElement location = $("div.pv-text-details__left-panel.mt2 span");
    ElementsCollection works = $$("#experience +div +div ul li");
    WiseVisionApiHelper wiseVisionApiHelper = new WiseVisionApiHelper();
    String personName = "No data";
    String about = " ";
    String workHistory = " ";
    String locationData = " ";

    @SneakyThrows
    @Test(description = "Get data from person page", dataProvider = "dataProviderPeopleSearch")
    public void getData (String name, String email, String password){
        setupBrowser(true, name);
        openLinkedInLoginPage();
        signInPage.signIn(randomResult, email, password);
        WebDriverRunner.getWebDriver().manage().window().maximize();
        Thread.sleep(randomResult);
        Selenide.open("https://www.linkedin.com/mynetwork/invite-connect/connections/");
        Thread.sleep(randomResult);

    }



    @DataProvider(name = "dataProviderPeopleSearch", parallel=true)
    public static Object[][] dataProviderPeopleSearch() {
        return new Object[][]{
                {       "Анастасия",
                        "reshetunmaryanwv@gmail.com",
                        "rSbnGaRS",
                },
        };
    }
}
