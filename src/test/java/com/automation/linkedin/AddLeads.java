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

import java.util.Map;
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
    String token = zohoCrmHelper.renewAccessToken();

    @SneakyThrows
    @Test(description = "add leads from CRM", dataProvider = "dataProviderPeopleSearch", alwaysRun = true )
    public void addLeads(String name, String email, String password,  String msg, String linkedinperson){
        int leadsRequestCount = 1;
        Thread.sleep(randomResult);
        String data = zohoCrmHelper.getLeadList( token, 1,  "Waiting",  linkedinperson);
        if (data.contains("INVALID_TOKEN")){
            String token = zohoCrmHelper.renewAccessToken();
            data = zohoCrmHelper.getLeadList( token, 1,  "Waiting",  linkedinperson);
        }
        int leadsAddedCount = 0;

        if (data.isEmpty()) {
            System.out.println("Skip" + linkedinperson);
            return;
        };


        setupBrowser(true, "name");
        Thread.sleep(randomResult*3);
        openLinkedInLoginPage();
        System.out.println("-------------------------------------------------------\n" +
                "START: "+name+"\n" +
                "-------------------------------------------------------");
        signInPage.signIn(randomResult, email, password);
        Thread.sleep(1000*20);
        Thread.sleep(randomResult);
        WebDriverRunner.getWebDriver().manage().window().maximize();

        // System.out.println(new JSONObject( data ).getJSONArray("data").length());
        //System.out.println(new JSONObject( data ).getJSONArray("data").getJSONObject(50).getString("Website"));
        System.out.println("data: " + data);
        for (int i = 0; i < new JSONObject( data ).getJSONArray("data").length(); i++)
        {
            Thread.sleep(randomResult);

            Thread.sleep(200);
            String id = new JSONObject( data ).getJSONArray("data").getJSONObject(i).getString("id");
            if (String.valueOf(new JSONObject( data ).getJSONArray("data").getJSONObject(i).get("Website")).contains("null")) continue;
            String personRef = new JSONObject( data ).getJSONArray("data").getJSONObject(i).getString("Website");
            Selenide.open(personRef);
            Thread.sleep(randomResult);
            if (WebDriverRunner.getWebDriver().getCurrentUrl().contains("404")) {
                {
                    String changeLeadStatusResponse = zohoCrmHelper.changeLeadStatus(id, token, "421659000001302365");
                    JSONObject changeLeadStatusResponseJson = new JSONObject(changeLeadStatusResponse);;
                    System.out.println("code: " + changeLeadStatusResponseJson.getString("code") );
                    System.out.println("\n" );
                    if (changeLeadStatusResponseJson.getString("code").equals("RECORD_NOT_IN_PROCESS")) {
                        System.out.println("Try direct change:\n" + zohoCrmHelper.directChangeLeadStatus(id, token,"Attempted to Contact") );
                    };
                }
                continue;
            };
            personPage.addToFriends(msg,false);
            Thread.sleep(randomResult);
            if (personPage.limitAlertHeader.exists()){

                System.out.println("\n========================================================================\n" +"Out of requests"+ "\n========================================================================\n");
                WebDriverRunner.getWebDriver().quit();
                break;
            }
            leadsAddedCount = leadsRequestCount++;
            System.out.println("Leads added from " + name + "account = " + leadsAddedCount);
            if (leadsAddedCount==30) break;
            {
                String changeLeadStatusResponse = zohoCrmHelper.changeLeadStatus(id, token, "421659000001302365");
                JSONObject changeLeadStatusResponseJson = new JSONObject(changeLeadStatusResponse);;
                System.out.println("code: " + changeLeadStatusResponseJson.getString("code") );
                System.out.println("\n" );
                if (changeLeadStatusResponse.contains("INVALID_TOKEN")) {
                    token = zohoCrmHelper.renewAccessToken();
                    zohoCrmHelper.changeLeadStatus(id, token, "421659000001302365");
                }
                if (changeLeadStatusResponseJson.getString("code").equals("RECORD_NOT_IN_PROCESS")) {
                    System.out.println("Try direct change:\n" + zohoCrmHelper.directChangeLeadStatus(id, token,"Attempted to Contact") );
                };
            }
        }
    }

    @DataProvider(name = "dataProviderPeopleSearch", parallel=true)
    public static Object[][] dataProviderPeopleSearch() {
        Map<String, String> env = System.getenv();

        return new Object[][]{
                //1
                {       "name",
                        env.get("LOGIN"),
                        env.get("PASSWORD"),
                        "Hello there. I stumbled across your account accidentally and was impressed with your expertise. Would you mind accepting this invite so we could talk some more?",
                        "Aleksandra Sternenko"
                },
        };
    }
}
