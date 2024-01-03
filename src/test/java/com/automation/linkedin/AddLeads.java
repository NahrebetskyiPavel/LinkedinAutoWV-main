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
    @Test(description = "add leads from search page", dataProvider = "dataProviderPeopleSearch", alwaysRun = true )
    public void addLeads(String name, String email, String password,  String msg, String linkedinperson){
        int leadsRequestCount = 0;
        Thread.sleep(randomResult);
        String token = zohoCrmHelper.renewAccessToken();
        String data = zohoCrmHelper.getLeadList( token, 1,  "Waiting",  linkedinperson);


        if (data.isEmpty()) {
            System.out.println("Skip" + linkedinperson);
            return;
        };
        System.out.println("-------------------------------------------------------\n" +
                "START: "+name+"\n" +
                "-------------------------------------------------------");
        setupBrowser(true, name);
        Thread.sleep(randomResult*3);
        openLinkedInLoginPage();
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
                String personRef = new JSONObject( data ).getJSONArray("data").getJSONObject(i).getString("Website");
                Selenide.open(personRef);
                Thread.sleep(randomResult);
                String id = new JSONObject( data ).getJSONArray("data").getJSONObject(i).getString("id");
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
                if (i==30) break;
            {
                String changeLeadStatusResponse = zohoCrmHelper.changeLeadStatus(id, token, "421659000001302365");
                JSONObject changeLeadStatusResponseJson = new JSONObject(changeLeadStatusResponse);;
                System.out.println("code: " + changeLeadStatusResponseJson.getString("code") );
                System.out.println("\n" );
                if (changeLeadStatusResponseJson.getString("code").equals("RECORD_NOT_IN_PROCESS")) {
                    System.out.println("Try direct change:\n" + zohoCrmHelper.directChangeLeadStatus(id, token,"Attempted to Contact") );
                };
            }
        }
    }

    @DataProvider(name = "dataProviderPeopleSearch", parallel=true)
    public static Object[][] dataProviderPeopleSearch() {

        return new Object[][]{
                {       "Aleksandra",
                        "alexandra.sternenko@gmail.com",
                        "asd321qq",
                        "Hello there. I stumbled across your account accidentally and was impressed with your expertise. Would you mind accepting this invite so we could talk some more?",
                        "Aleksandra"
                }
        };
    }
}
