package com.automation.crm;

import api.helpers.ZohoCrmHelper;
import com.automation.linkedin.pages.PersonPage;
import com.automation.linkedin.pages.login.SignInPage;
import com.automation.linkedin.pages.messaging.MessagingPage;
import com.automation.linkedin.pages.search.SearchPeoplePage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import lombok.SneakyThrows;
import okhttp3.Response;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Random;

import static com.automation.linkedin.Base.openLinkedInLoginPage;
import static com.automation.linkedin.Base.setupBrowser;
import static com.codeborne.selenide.Condition.interactable;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class AddLeads {
    ZohoCrmHelper zohoCrmHelper = new ZohoCrmHelper();
    SignInPage signInPage = new SignInPage();
    SearchPeoplePage searchPeoplePage = new SearchPeoplePage();
    Random random = new Random();
    int low = 2000;
    int high = 5000;
    int randomResult = random.nextInt(high-low) + low;
    private static final String Contacted  = "421659000001302293";

    @SneakyThrows
    @Test(description = "add leads from search page", dataProvider = "dataProviderPeopleAddToCRM")
    public void addLeadsToCRM(String name, String email, String password){
        setupBrowser(true, name);
        Thread.sleep(randomResult*3);
        openLinkedInLoginPage();
        signInPage.signIn(randomResult, email, password);
        Selenide.open("https://www.linkedin.com/mynetwork/invite-connect/connections/");
        WebDriverRunner.getWebDriver().manage().window().maximize();
        String token = zohoCrmHelper.renewAccessToken();
        for (int i = 0; i < 5; i++) {
            Thread.sleep(randomResult);
            Selenide.executeJavaScript("window.scrollTo(2000, document.body.scrollHeight)");
            if ($x("//span[normalize-space()='Show more results']").exists()) {
                //if (!$x("//span[normalize-space()='Show more results']").exists())continue;
                $x("//span[normalize-space()='Show more results']").shouldBe(interactable).click();
                Thread.sleep(randomResult);
                Selenide.executeJavaScript("window.scrollTo(2000, document.body.scrollHeight)");
            }        }
        for (int i = 0; i < 200; i++) {
            Thread.sleep(randomResult);
            ElementsCollection leads = $$x("//div[@class='mn-connection-card__details']/a");
            SelenideElement person = leads.get(i);
                Thread.sleep(200);
                if (person.find(By.cssSelector(".mn-connection-card__name")).text().contains("LinkedIn Member")) continue;
                String[] personNamearr = person.find(By.cssSelector(".mn-connection-card__name")).text().split("\\s");
                String personName = personNamearr[0] + " " + personNamearr[1];
                Thread.sleep(randomResult);

                String leadInfoResponseBody = zohoCrmHelper.getLeadInfoByFullName(token, personName);
                if (leadInfoResponseBody.contains("INVALID_TOKEN")) {
                    token = zohoCrmHelper.renewAccessToken();
                    leadInfoResponseBody = zohoCrmHelper.getLeadInfoByFullName(token, personName);
                }
                if (leadInfoResponseBody.length() > 0 && leadInfoResponseBody.contains("data")) {
                    JSONObject responseBodyJsonObjectLeadInfo = new JSONObject(leadInfoResponseBody);
                    String leadId = responseBodyJsonObjectLeadInfo.getJSONArray("data").getJSONObject(0).getString("id");
                    System.out.println(leadId);
                    System.out.println(personName);
                    zohoCrmHelper.changeLeadStatus(leadId, token, Contacted);
                }
                else continue;
            Thread.sleep(randomResult);
        }
    }



    @DataProvider(name = "dataProviderPeopleAddToCRM", parallel=false)
    public static Object[][] dataProviderPeopleAddToCRM() {
        return new Object[][]{
                {       "Маша ",
                        "deynekamariawv@gmail.com",
                        "3N2wbnsw",
                },
                {       "Денис",
                        "basdenisphytontm@gmail.com",
                        "asd321qq",
                },
                {       "Анастасия ",
                        "vozniakanastasia52@gmail.com",
                        "zdHXF5bf",
                },

                {       "Максим",
                        "kotokmaksym@gmail.com",
                        "r4E3w2q1",
                },
                {       "Наталья",
                        "natalia.marcoon@gmail.com ",
                        "asd321qq",
                },
                {       "Александра",
                        "alexandra.sternenko@gmail.com",
                        "asd321qq",
                },
                {       "Роксолана ",
                        "roksolanatrofim@gmail.com ",
                        "89fcmTT88V",
                },
                {       "Марьян",
                        "reshetunmaryanwv@gmail.com",
                        "rSbnGaRS",
                },
                {       "Настя ",
                        "anastasiiakuntii@gmail.com",
                        "nastya4141",
                },
                {       "Nikita ",
                        "kni2012@ukr.net",
                        "33222200s",
                },
        };
    }
}

