package com.automation.linkedin;

import api.helpers.WiseVisionApiHelper;
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
import utils.StatusChecker;

import java.util.Random;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class AddLeads extends Base {
    SignInPage signInPage = new SignInPage();
    SearchPeoplePage searchPeoplePage = new SearchPeoplePage();
    PersonPage personPage = new PersonPage();
    MessagingPage messagingPage = new MessagingPage();
    ZohoCrmHelper zohoCrmHelper = new ZohoCrmHelper();
    WiseVisionApiHelper wiseVisionApiHelper = new WiseVisionApiHelper();
    StatusChecker statusChecker = new StatusChecker();
    String attemptedToContact = "421659000010541270";
    String attemptedToContact1 = "421659000001302365";
    Random random = new Random();
    int low = 2000;
    int high = 5000;
    int randomResult = random.nextInt(high-low) + low;
    String token = zohoCrmHelper.renewAccessToken();
    int totalLeadsAddedCount = 0;

    @SneakyThrows
    @Test(description = "add leads from CRM", dataProvider = "dataProviderPeopleSearch", alwaysRun = true )
    public void addLeads(String profileId, String email, String password,  String cookie, String linkedinperson){
        int leadsRequestCount = 1;
        for (int j = 0; j < 10; j++) {
        Thread.sleep(randomResult);
        String data = zohoCrmHelper.getLeadList( token, j,  "Waiting",  linkedinperson);
        if (data.contains("INVALID_TOKEN")){
            String token = zohoCrmHelper.renewAccessToken();
            data = zohoCrmHelper.getLeadList( token, 1,  "Waiting",  linkedinperson);
        }
        int leadsAddedCount = 0;

        if (data.isEmpty()) {
            System.out.println("Skip" + linkedinperson);
            wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","Skip " + linkedinperson + "because data isEmpty");

            return;
        };

        System.out.println("-------------------------------------------------------\n" +
                "START: "+linkedinperson+"\n" +
                "-------------------------------------------------------");
        Thread.sleep(1000*20);
        Thread.sleep(randomResult);

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
            System.out.println("personRef: " + personRef);
            System.out.println("id: " + id);
            Thread.sleep(randomResult);
                wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","TOTAL = " + totalLeadsAddedCount + "\n");
                wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","Finish \n"  + "account = " + linkedinperson + " "+ leadsAddedCount + " leadsAdded = " + leadsAddedCount + "\n");
            {
              String response =  wiseVisionApiHelper.impastoAddToFriends(profileId, email, password, cookie, personRef);
                Thread.sleep(1000*60);
              int taskId = (int) new JSONObject( response ).get("taskId");
              String taskStatus = new JSONObject( wiseVisionApiHelper.impastoGetTaskinfo(profileId, taskId) ).getString("status");
                try {
                    statusChecker.waitForStatus("finished", taskStatus);
                    System.out.println("Status is now 'finished'.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String changeLeadStatusResponse;
                JSONObject changeLeadStatusResponseJson;
                changeLeadStatusResponse = zohoCrmHelper.changeLeadStatus(id, token, attemptedToContact1);
                changeLeadStatusResponseJson = new JSONObject(changeLeadStatusResponse);;
                    if (changeLeadStatusResponseJson.getString("code").equals("INVALID_DATA")) {
                    changeLeadStatusResponse = zohoCrmHelper.changeLeadStatus(id, token, attemptedToContact);
                    changeLeadStatusResponseJson = new JSONObject(changeLeadStatusResponse);;
                    }
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

            leadsAddedCount = leadsRequestCount++;
            totalLeadsAddedCount = totalLeadsAddedCount + leadsAddedCount;
            System.out.println("Leads added from " + linkedinperson + "account = " + leadsAddedCount);
            //wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","Leads added from " + name + "account = " + leadsAddedCount);
            if (leadsAddedCount==25) {

                wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","Finish \n"  + "account = " + linkedinperson  + leadsAddedCount + " leadsAdded = " + leadsAddedCount + "\n");
                wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","TOTAL = " + totalLeadsAddedCount + "\n");

                break;
            };

        }
            if (leadsAddedCount==25) {
                wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","\nTOTAL = " + totalLeadsAddedCount + "\n");
                break;
            };
        }
    }

    @DataProvider(name = "dataProviderPeopleSearch", parallel=true)
    public static Object[][] dataProviderPeopleSearch() {

        return new Object[][]{
                //1
                {       "alexey-fedotov-41a4a42b2",
                        "fedotov.alexey@outlook.de",
                        "33222200Shin",
                        "AQEDAUst11YDXkwtAAABjnWOXgkAAAGOmZriCU0AtuKG_eCf1YjB0qq1iJOy2u8pEpMBJD3vUW7eOV7cjVEkEoyTiU3TX_9cs5fUcXjKXxrCxSKbE3MihEPEUG972caFxWvP_tXkX_q5OAjvRcyflMR5",
                        "Fedotov Alexey"
                },
                {       "andrei-gorbunkov-a34b4a2aa",
                        "andreiGorbunkov@outlook.de",
                        "33222200Shin",
                        "AQEDAUqQcUgAJO_LAAABjRGv3SIAAAGOmMFYqE0ArnVnmtRxkfVOu6vUysML6PHk2oENpaWG43H6H_RZGisvCqLeBj7azZTBPn0_vjE7zPme8YjHw6GyXwEOBkQvUkqNijYnP9HnwG2A5y5wR9E-hY_q",
                        "Andrei Gorbunkov"
                },
                {       "maryana-nikolayenko",
                        "nikolayenko.maryana@outlook.de",
                        "33222200Shin",
                        "AQEDAUwXjXUEIGu5AAABjepsA3kAAAGOqQA4JU0AzNcS_1QgrITl8veER7O_l56aNg9ujszf2XbmB2Mw6Kx2fFl8azF0opPeG7dC7DSGuQ4NbYz7X3haGOyA8BqHLXtxeljElBZaD90Fuc1JOS5pWmSx",
                        "Nikolayenko Maryana"
                },
                {       "aline-paul",
                        "aline.paul@outlook.de",
                        "33222200Shin",
                        "AQEDAUst11YDXkwtAAABjnWOXgkAAAGOmZriCU0AtuKG_eCf1YjB0qq1iJOy2u8pEpMBJD3vUW7eOV7cjVEkEoyTiU3TX_9cs5fUcXjKXxrCxSKbE3MihEPEUG972caFxWvP_tXkX_q5OAjvRcyflMR5",
                        "Aline Paul"
                },
                {       "paul-bereza",
                        "paul.bereza02@outlook.de",
                        "33222200Shin",
                        "AQEDAUt7kjAFQ5V1AAABjY17BIQAAAGOrchRxk0ANnqFXzwoOoGvqdSns-mBZgVIAigOKbTZJ0RWUBNK8UB64oOwoeTXFih1uxrDgIW8DA6YEpGKavO-W1MdA2YdEUSLqalw5LgoK7VQMCDwi7SmvsPn",
                        "Paul Bereza"
                },
                {       "alessio-vacenko-b506612b3",
                        "alessio.Vacenko@outlook.it",
                        "33222200Shin",
                        "AQEDAUthqywBUW83AAABjoGzPasAAAGOpb_Bq00AYesxSzqz_svmdxwpgeBI81kGKBz9KOqrYeQvGGre8kwdFnnUm2mcCEmofBpk5hydytWEP2hVdpRs910CvZ5kko7h1JcCY1jsGoAdpRxNvdqaCuac",
                        "Alessio Vacenko"
                },
                {       "margit-matthes",
                        "margit.Matthes@outlook.de",
                        "33222200Shin",
                        "AQEDAUuampkCi39pAAABjoGopG0AAAGOpbUobU0AYzgDBXlGuCNM8mjS3qeymKlCs3GDAAAX2yazREUmzNsgqrkCEBZWhzFf7xDJ05XaRilGTXcvDKyDYGYfmp7b-E2i0L4QTVBbKA7d6GA6MsD6y_Hb",
                        "Margit Matthes"
                },

        };
    }
}
