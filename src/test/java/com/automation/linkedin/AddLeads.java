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
              String taskInfo = wiseVisionApiHelper.impastoGetTaskinfo(profileId, taskId);
              String taskStatus = new JSONObject( taskInfo ).getString("status");
              String taskResult = String.valueOf(new JSONObject( taskInfo ));
                if (taskResult.contains("error") && taskResult.contains("Invalid url")) {

                    continue;
                };
              String taskResults = String.valueOf(new JSONObject( taskInfo ).getJSONArray("results").getJSONObject(0));
                try {
                    statusChecker.waitForStatus("finished", taskStatus);
                    System.out.println("Status is now 'finished'.");

                    if (taskResults.contains("error")) {
                        System.out.println("ERROR: " + new JSONObject( taskInfo ).getJSONArray("results").getJSONObject(0).getString("error"));
                        continue;
                    };
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
            System.out.println("Leads added from " + linkedinperson + " account = " + leadsAddedCount);
            //wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","Leads added from " + name + "account = " + leadsAddedCount);
            if (leadsAddedCount==25) {
                totalLeadsAddedCount = totalLeadsAddedCount + leadsAddedCount;

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

                {       "andrei-gorbunkov-a34b4a2aa",
                        "andreiGorbunkov@outlook.de",
                        "33222200Shin",
                        "AQEDAUqQcUgAJO_LAAABjRGv3SIAAAGOvMqCdU0Ag3zxWG6o12zUqqURQRW9W5YiAsL1JQ-5vdfFpGBelyaVGYHP0iC122HlAv2xNwq1My_zBbSA4uhCMU42DV4iRCVI6UIK2Q98FAhhAkl_Lzklgvzg",
                        "Andrei Gorbunkov"
                },
                {       "aline-paul",
                        "aline.paul@outlook.de",
                        "33222200Shin",
                        "AQEDAUt7kBIAhK1UAAABjr0MjCEAAAGO4RkQIU4AwCpP-a5CDmqHZR87eGGsaDqZD7x-vcizUKMYrOZWE8WVM0BRdPoNC_EvJ6hyBvxggkixZIZEn_NjF3m1ovC5m7MRm9yIZjGXo2FDIeSkofJ5HrH8",
                        "Aline Paul"
                },
                {       "paul-bereza",
                        "paul.bereza02@outlook.de",
                        "33222200Shin",
                        "AQEDAUt7kjAFQ5V1AAABjY17BIQAAAGOrZocyk0ADu01WzC7kYz9BmnkvrCJtPNphBAckembhcTOWFgLSCn2xdhVluaqk3DacYtyQsrIBgd8c7_NtYwfevHyEfAhOtIcLkxdxfUEgj04dDbx_uSrdTPY",
                        "Paul Bereza"
                },
                {       "alessio-vacenko-b506612b3",
                        "alessio.Vacenko@outlook.it",
                        "33222200Shin",
                        "AQEDAUthqywBUW83AAABjoGzPasAAAGO0M-Kik0Ae4BcJ1UTuha87SZJeYtjcP_2EgzmyJOQ2w90rDaR6EKdLlcwxVl_hIYUBgy3IGj22i-BN7hoWrqeZGKPi_iW1-CDfDDZAMgu73Ob4spxYIZu68uP",
                        "Alessio Vacenko"
                },
                {       "margit-matthes",
                        "margit.Matthes@outlook.de",
                        "33222200Shin",
                        "AQEDAUuampkCi39pAAABjoGopG0AAAGO0NAIJE0ANY67cipBXpm84lAAO5KsSsnGqeB1OK-HxQ37rLF4cr5j7qraGxRj9dY1e47pYWWgeevakyzo_wrohxtHSrP6PDsCQOq7LBb5f0FxITaAZLFCP--b",
                        "Margit Matthes"
                },

        };
    }
}
