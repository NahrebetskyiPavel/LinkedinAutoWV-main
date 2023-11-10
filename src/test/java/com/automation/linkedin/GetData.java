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
                Thread.sleep(200);
                while (true){
                    Thread.sleep(randomResult);
                Response getUnprocessedLinksResponse = wiseVisionApiHelper.getUnprocessedLinks();
                String personRef = getUnprocessedLinksResponse.body().string();
                    Thread.sleep(randomResult);
                if (personRef.contains("No unprocessed linkedin URLs found.")) {
                    System.out.println("-------------------------------------------------------\n" +
                            " No unprocessed linkedin URLs found\n" +
                            "-------------------------------------------------------");
                    break;
                }
                Thread.sleep(randomResult);
                Selenide.executeJavaScript("window.scrollTo(2000, document.body.scrollHeight)");
                Selenide.executeJavaScript("window.open(\'" + personRef + "\')");
                Thread.sleep(randomResult*3);
                Selenide.switchTo().window(1);
                Thread.sleep(randomResult);
                Thread.sleep(randomResult);
                    if ($x("//*[text()='Please check your URL or return to LinkedIn home.']").exists())
                    {
                        System.out.println(personRef);
                        wiseVisionApiHelper.postLinkedinPersonData(personRef, "404", "404", "404", "404");
                        Selenide.closeWindow();
                        switchTo().window(0);
                        continue;
                    }
                    personName = $("div.pv-text-details__left-panel--full-width H1").text();
                personPage.moreBtn.shouldBe(interactable, Duration.ofSeconds(15));
                Selenide.executeJavaScript("window.scrollTo(2000, document.body.scrollHeight)");
                if (aboutHeader.exists()){
                    Selenide.executeJavaScript("document.getElementById(\"about\").scrollIntoView();");
                if (seeMoreBtn.exists()) { seeMoreBtn.shouldBe(interactable,Duration.ofSeconds(10)).click(); }
                if (aboutBody.isDisplayed()) about = aboutBody.text();
                }
                if ($("#experience +div +div ul li").exists()){
                    Selenide.executeJavaScript("document.getElementById(\"experience\").scrollIntoView();");
                    for (SelenideElement wok : works) {
                        workHistory = wok.text() + "\n";
                    }
                }
                    if (location.isDisplayed()) locationData = location.text();
                    wiseVisionApiHelper.postLinkedinPersonData(personRef, personName, about, workHistory, locationData);
                    Selenide.closeWindow();
                    switchTo().window(0);


            Thread.sleep(randomResult);
                }
    }



    @DataProvider(name = "dataProviderPeopleSearch", parallel=true)
    public static Object[][] dataProviderPeopleSearch() {
        return new Object[][]{
                {       "Анастасия",
                        "vozniakanastasia52@gmail.com",
                        "zdHXF5bf",
                },
        };
    }
}
