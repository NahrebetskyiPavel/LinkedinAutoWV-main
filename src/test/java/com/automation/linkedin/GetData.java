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
    @SneakyThrows
    @Test(description = "Get data from person page", dataProvider = "dataProviderPeopleSearch")
    public void getData (String name, String clientName, String email, String password, String searchLink, String msg, String pickList, String leadCompany, String leadCompanyId){
        setupBrowser(true, name);
        openLinkedInLoginPage();
        signInPage.signIn(randomResult, email, password);
        Selenide.open(searchLink);
        WebDriverRunner.getWebDriver().manage().window().maximize();
            Thread.sleep(randomResult);
                Thread.sleep(200);
                while (true){
                String personRef = wiseVisionApiHelper.getUnprocessedLinks();
                    System.out.println(personRef);
                Thread.sleep(randomResult);
                Selenide.executeJavaScript("window.scrollTo(2000, document.body.scrollHeight)");
                //TODO replace with api request  get link to person page
                Selenide.executeJavaScript("window.open(\'" + personRef + "\')");
                Thread.sleep(randomResult*3);
                Selenide.switchTo().window(1);
                Thread.sleep(randomResult);
                String link = WebDriverRunner.getWebDriver().getCurrentUrl();
                String personName = $("div.pv-text-details__left-panel H1").text();
                System.out.println( "\n Peron name: " + personName + "\n Peron link: " + link );
                personPage.moreBtn.shouldBe(interactable, Duration.ofSeconds(15));
                Selenide.executeJavaScript("window.scrollTo(2000, document.body.scrollHeight)");
                if (aboutHeader.exists()){
                    Selenide.executeJavaScript("document.getElementById(\"about\").scrollIntoView();");
                if (seeMoreBtn.exists()) { seeMoreBtn.shouldBe(interactable,Duration.ofSeconds(10)).click(); }
                String about = aboutBody.text();
                    System.out.println(about);
                    String workHistory = "";
                    for (SelenideElement wok : works) {
                        workHistory = wok.text() + "\n";
                        System.out.println(workHistory);
                    }
                    String locationData = location.text();
                    System.out.println(locationData);
                    wiseVisionApiHelper.postLinkedinPersonData(personRef, personName, about, workHistory, locationData);
                    Selenide.closeWindow();
                    switchTo().window(0);
                }
                else {
                    Selenide.closeWindow();
                    switchTo().window(0);
                };
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
