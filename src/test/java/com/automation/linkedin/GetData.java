package com.automation.linkedin;

import api.helpers.ZohoCrmHelper;
import com.automation.linkedin.pages.PersonPage;
import com.automation.linkedin.pages.login.SignInPage;
import com.automation.linkedin.pages.search.SearchPeoplePage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Random;

import static com.codeborne.selenide.Condition.interactable;
import static com.codeborne.selenide.Condition.visible;
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
    ElementsCollection woks = $$("#experience +div +div ul li");

    @SneakyThrows
    @Test(description = "Get data from person page", dataProvider = "dataProviderPeopleSearch")
    public void getData (String name, String clientName, String email, String password, String searchLink, String msg, String pickList, String leadCompany, String leadCompanyId){
        setupBrowser(true, name);
        openLinkedInLoginPage();
        signInPage.signIn(randomResult, email, password);
        Selenide.open(searchLink);
        WebDriverRunner.getWebDriver().manage().window().maximize();
        for (int i = 0; i < 5; i++) {
            Thread.sleep(randomResult);
            for (SelenideElement person:searchPeoplePage.PersonPages
            ) {
                Thread.sleep(200);
                String personRef = person.getAttribute("href");
                if (person.text().contains("LinkedIn Member")) continue;

                String[] personNamearr = person.find(By.cssSelector("span")).text().split("\\s");
                String personName = personNamearr[0] + " " + personNamearr[1];
                Thread.sleep(randomResult);
                Selenide.executeJavaScript("window.scrollTo(2000, document.body.scrollHeight)");
                //TODO replace with api request  get link to person page
                Selenide.executeJavaScript("window.open(\'" + personRef + "\')");
                Thread.sleep(randomResult*3);
                Selenide.switchTo().window(1);
                Thread.sleep(randomResult);
                String link = WebDriverRunner.getWebDriver().getCurrentUrl();
                System.out.println( "\n Peron name: " + personName + "\n Peron link: " + link );
                personPage.moreBtn.shouldBe(interactable, Duration.ofSeconds(15));
                Selenide.executeJavaScript("window.scrollTo(2000, document.body.scrollHeight)");
                if (aboutHeader.exists()){
                    Selenide.executeJavaScript("document.getElementById(\"about\").scrollIntoView();");
                if (seeMoreBtn.exists()) { seeMoreBtn.shouldBe(interactable,Duration.ofSeconds(10)).click(); }
                String about = aboutBody.text();
                    System.out.println(about);
                    woks.stream().map(SelenideElement::text).forEach(System.out::println);
                    System.out.println(location.text());
                    //TODO add POST rqst post person data
                    Selenide.closeWindow();
                    switchTo().window(0);
                }
                else {
                    Selenide.closeWindow();
                    switchTo().window(0);
                    continue;
                };
            }
            Thread.sleep(randomResult);
            searchPeoplePage.previousPageBtn.shouldBe(visible).click();
            Thread.sleep(randomResult);
        }
    }



    @DataProvider(name = "dataProviderPeopleSearch", parallel=true)
    public static Object[][] dataProviderPeopleSearch() {
        String clientName = "";
        String leadCompanyGamblingId ="421659000005125089";
        String leadCompanyAmsterdamId ="421659000005261283";
        String leadCompanyAustraliaId ="421659000005261273";
        String leadCompanyName ="Gambling LinkedIn";
        return new Object[][]{
                {       "Анастасия - Amsterdam Head of",
                        clientName,
                        "vozniakanastasia52@gmail.com",
                        "zdHXF5bf",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22102011674%22%5D&network=%5B%22O%22%5D&origin=FACETED_SEARCH&page=85&sid=_xY&titleFreeText=Head%20of",
                        "Hello! \n I've reached out to invite you to explore our services for your gambling company. My team is skilled in web and app development and can provide tailored solutions to suit your needs. Let's chat and see how we can help!",
                        "Yurij",
                        leadCompanyName,
                        leadCompanyGamblingId
                },
        };
    }
}
