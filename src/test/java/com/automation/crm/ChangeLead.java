package com.automation.crm;

import api.helpers.ZohoCrmHelper;
import com.automation.linkedin.pages.login.SignInPage;
import com.automation.linkedin.pages.search.SearchPeoplePage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Random;

import static com.automation.linkedin.Base.openLinkedInLoginPage;
import static com.automation.linkedin.Base.setupBrowser;
import static com.codeborne.selenide.Condition.interactable;
import static com.codeborne.selenide.Selenide.*;

public class ChangeLead {
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
            for (SelenideElement lead:leads
                 ) {
                System.out.println("=======leadtext=======");
                //System.out.println(lead.text());

                String[] personNamearr = lead.text().replace("Member’s name","").split("\\s");
                String personName = personNamearr[1].replace(" Member’s","") +" "+ personNamearr[2].replace(" Member’s","");
                System.out.println(personName);
                System.out.println("=======================");

            }
            SelenideElement person = leads.get(i);
                Thread.sleep(200);
                if (person.find(By.cssSelector(".mn-connection-card__name")).text().contains("LinkedIn Member")) continue;
                String[] personNamearr = person.text().replace("Member’s name","").split("\\s");;
                String personName = personNamearr[1].replace(" Member’s","") +" "+ personNamearr[2].replace(" Member’s","");
                Thread.sleep(randomResult);

                String leadInfoResponseBody = zohoCrmHelper.getLeadInfoByFullName(token, personName);
                System.out.println(personName);
                System.out.println(leadInfoResponseBody);
                if (leadInfoResponseBody.contains("INVALID_TOKEN")) {
                    token = zohoCrmHelper.renewAccessToken();
                    leadInfoResponseBody = zohoCrmHelper.getLeadInfoByFullName(token, personName);
                }
                if (leadInfoResponseBody.length() > 0 && leadInfoResponseBody.contains("data")) {
                    JSONObject responseBodyJsonObjectLeadInfo = new JSONObject(leadInfoResponseBody);
                    String leadId = responseBodyJsonObjectLeadInfo.getJSONArray("data").getJSONObject(0).getString("id");
                    System.out.println(leadId);
                    System.out.println(personName);
                    if (responseBodyJsonObjectLeadInfo.getJSONArray("data").getJSONObject(0).getString("Lead_Status").equals("Attempted to Contact"))

                    {
                        String changeLeadStatusResponse = zohoCrmHelper.changeLeadStatus(leadId, token, Contacted);
                    JSONObject changeLeadStatusResponseJson = new JSONObject(changeLeadStatusResponse);;
                    System.out.println("code: " + changeLeadStatusResponseJson.getString("code") );
                    System.out.println("\n" );
                    if (changeLeadStatusResponseJson.getString("code").equals("RECORD_NOT_IN_PROCESS")) {
                        System.out.println("Try direct change:\n" + zohoCrmHelper.changeLeadStatus(leadId, token) );
                    };
                    }
                    else {continue;}
                }
                else continue;
            Thread.sleep(randomResult);
        }
    }



    @DataProvider(name = "dataProviderPeopleAddToCRM", parallel=false)
    public static Object[][] dataProviderPeopleAddToCRM() {
        return new Object[][]{
                {       "Настя - Stuttgart CEO",
                        "anastasiiakuntii@gmail.com",
                        "33222200Shin",
                }
        };
    }
}

