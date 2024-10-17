package com.automation.linkedin;

import api.helpers.WiseVisionApiHelper;
import api.helpers.ZohoCrmHelper;
import com.automation.linkedin.pages.PersonPage;
import com.automation.linkedin.pages.login.SignInPage;
import com.automation.linkedin.pages.messaging.MessagingPage;
import com.automation.linkedin.pages.search.SearchPeoplePage;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;

import org.testng.annotations.Test;
import utils.StatusChecker;

import java.util.Random;

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
    String broken = "421659000017740001";
    Random random = new Random();
    int low = 2000;
    int high = 5000;
    int randomResult = random.nextInt(high-low) + low;
    int leadLow = 20;
    int leadsHigh = 30;
    int leadsRandomResult = random.nextInt(leadsHigh-leadLow) + leadLow;

    String token = zohoCrmHelper.renewAccessToken();
    int totalLeadsAddedCount = 0;

    @SneakyThrows
    @Test(description = "add leads from CRM", dataProvider = "dataProviderPeopleSearch")
    public void addLeads(String profileId, String email, String password,  String cookie, String linkedinperson){
        int leadsRequestCount = 1;
        for (int j = 0; j < 10; j++) {
        Thread.sleep(randomResult);
        String data = zohoCrmHelper.getLeadList( token, j,  "Waiting",  linkedinperson);
        if (data.contains("INVALID_TOKEN")){
            String token = zohoCrmHelper.renewAccessToken();
            data = zohoCrmHelper.getLeadList( token, j,  "Waiting",  linkedinperson);
        }
        int leadsAddedCount = 0;

        if (data.isEmpty()) {
            //System.out.println("Skip" + linkedinperson);
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
        //System.out.println("data: " + data);
        for (int i = 0; i < new JSONObject( data ).getJSONArray("data").length(); i++)
        {
            Thread.sleep(randomResult);

            Thread.sleep(200);
            String id = new JSONObject( data ).getJSONArray("data").getJSONObject(i).getString("id");
            if (String.valueOf(new JSONObject( data ).getJSONArray("data").getJSONObject(i).get("Website")).contains("null")) continue;
            String originalUrl = new JSONObject( data ).getJSONArray("data").getJSONObject(i).getString("Website");

            String personRef = originalUrl.replaceAll("http://.*?linkedin", "http://www.linkedin");

            //System.out.println("personRef: " + personRef);
            //System.out.println("id: " + id);
            Thread.sleep(randomResult);
                //wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","TOTAL = " + totalLeadsAddedCount + "\n");
                //wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","Finish \n"  + "account = " + linkedinperson + " "+ leadsAddedCount + " leadsAdded = " + leadsAddedCount + "\n");
            {
              String response =  wiseVisionApiHelper.impastoAddToFriends(profileId, email, password, cookie, personRef);
              int taskId = (int) new JSONObject( response ).get("taskId");
              String taskInfo = wiseVisionApiHelper.impastoGetTaskinfo(profileId, taskId);
              String taskStatus = new JSONObject( taskInfo ).getString("status");
              String taskResult = String.valueOf(new JSONObject( taskInfo ));
                while (true){
                    Thread.sleep( 60 * 1000);
                    taskInfo = wiseVisionApiHelper.impastoGetTaskinfo(profileId, taskId);
                    taskStatus = new JSONObject( taskInfo ).getString("status");
                    if (taskStatus.contains("finished")) break;
                    if (taskStatus.contains("failed")) break;
                }
                if (taskInfo.contains("Cookie is not valid")) {
                    System.out.println("Cookie is not valid");
                    throw new Exception("Cookie is not valid!");
                };
                String taskResults;
                if (new JSONObject( taskInfo ).get("results") instanceof JSONArray) {
                    Thread.sleep(60000);
                     taskResult = String.valueOf(new JSONObject( taskInfo ).get("results"));
                     //if (taskResult.contains("null"))                     Thread.sleep(60000);
                     //if (taskStatus.contains("processing"))                     Thread.sleep(60000);
                     //if (taskResult.contains("Proxy connection ended before receiving CONNECT response")) continue;
                    if (taskResult.contains("Cookie is not valid")) {
                        System.out.println("Cookie is not valid");
                        throw new Exception("Cookie is not valid!");
                    };
                    taskInfo = String.valueOf(new JSONObject( taskInfo ));
                }

                try {
                  //  Thread.sleep(50*1000);
                    //statusChecker.waitForStatus("finished", taskStatus);
                    Thread.sleep(10*1000);
                    if (taskStatus.contains("processing")) statusChecker.waitForStatus("finished", taskStatus);
                    if (taskStatus.contains("failed")) {
                        System.out.println("ERROR: " + new JSONObject( taskInfo ).getJSONObject("results").getString("error"));
                        System.out.println("Status is now 'failed'.");
                        changeLeadStatus(id,broken, "broken");

                        continue;
                    };
                    if (taskInfo.contains("error")) {
                        System.out.println("ERROR: " + new JSONObject( taskInfo ).getJSONArray("results").getJSONObject(0).getString("error"));
                        System.out.println("Status is now 'error'.");
                        changeLeadStatus(id,broken, "broken");
                        continue;
                    };
                    if (taskStatus.contains("expired")) {
                        System.out.println("ERROR: " + new JSONObject( taskInfo ).getJSONArray("results").getJSONObject(0).getString("error"));
                        System.out.println("Status is now 'expired'.");

                        continue;
                    };
                    System.out.println("Status is now 'finished'.");
                    changeLeadStatusAttemptToContacted(id);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                changeLeadStatusAttemptToContacted(id);
            }

            leadsAddedCount = leadsRequestCount++;
            System.out.println("Leads added from " + linkedinperson + " account = " + leadsAddedCount);
            //wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","Leads added from " + name + "account = " + leadsAddedCount);
            if (leadsAddedCount==leadsRandomResult) {
                totalLeadsAddedCount = totalLeadsAddedCount + leadsAddedCount;

                wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","Finish \n"  + "account = " + linkedinperson  + leadsAddedCount + " leadsAdded = " + leadsAddedCount + "\n");
                wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","TOTAL = " + totalLeadsAddedCount + "\n");

                break;
            };

        }
            if (leadsAddedCount==leadsRandomResult) {
                wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","\nTOTAL = " + totalLeadsAddedCount + "\n");
                break;
            };
        }
    }

    @DataProvider(name = "dataProviderPeopleSearch", parallel=true)
    public static Object[][] dataProviderPeopleSearch() {

        return new Object[][]{



                {       "paul-bereza",
                        "paul.bereza02@outlook.de",
                        "33222200Shin",
                        "AQEFAHUBAAAAAA9y_ngAAAGQEc-OggAAAZJrHPFmTgAAGHVybjpsaTptZW1iZXI6MTI2NjM4OTU1MmPdIWQ_rYxSbUskA00ccuc4z22iLxS-DaOZi1BasZ1xpanENw1kxjqll7jjjMvxF4OkBMpOKn4UqQjD-TvaFp-4e0OWq0X6l0gfFic2AOw56efsfRQuGFI95O0ZWtrpPi6W-jI21rbodkCxIiNAp1LBj4tOnfQmpv5Uflxk0p_aDoESUjIEY8P8v17cP6ERz1sM1wI",
                        "Paul Bereza"
                },



                {       "elias-danilov",
                        "elias.danilov@outlook.it",
                        "33222200Shin",
                        "AQEDAUs6XDsAQUISAAABkpCWoT4AAAGStKMlPk4Az-OpQheSV0gMA6CS_y0IvVZwPtgmkD8epCwXkxLOxDjykiod6RneE8jL3xKAtVvb8PQS6LclUcdZFzUishn3LJxM5Wnhd2IKwRDpXMGd5dvRLy40",
                        "Elias Danilov"
                },

                {       "stefania-mykhaylenko",
                        "mykhaylenko.stefania@outlook.fr",
                        "cTsH3KhU",
                        "AQEDAUxQ7yQBSRMtAAABko_uwsUAAAGSs_tGxU0AiD6PdPd_2p-El7Y2tbFN-7LkugEiVUbwM8nda-pWMvm-yC_gfelAY_5X7M1cMNGVWUvOO5j5Zg0jYgoITAC4gZaY1qszXrl-NUfijJhRHFni6ZL6",
                        "Mykhaylenko Stefania"
                },

                {       "den-vaviron",
                        "denVavir00@outlook.de",
                        "33222200Shin",
                        "AQEDAUpubFMFCUCvAAABjP1fQdMAAAGSfA_8DE0AnZeA7m5pUCCSc00VyGY3dqXBirHDa5HXFDIUjdig0sdqyEqRtKZsQtnfcjmbDB58wMHS3uHVaEHP0T6hxFlzV-3868RnKT9KqKYvnnYya_zaRDmm",
                        "Den Vaviron"
                },


                {       "max-mikhaylov",
                        "max.Mikhaylov@outlook.de",
                        "33222200Shin",
                        "AQEDAUtQFBQCjJC7AAABjp8_IKQAAAGSfA_4Jk0ALptb50ypM8w9vm4kI3vNGuU_jQzT7RhBU0kvZSlj8c3pRzlwqVGD70umAHxExqKQlr_PkyiT8SU564g-0BjvZd9AWbnZHbRY2LPs8YoTSfKSulJk",
                        "Max Mikhaylov"
                },

                {       "patrick-yushko-b2080b2b8",
                        "yushko.patrick@outlook.it",
                        "206GLMC2",
                        "AQEFAHUBAAAAABBl9t0AAAGPfE7YuwAAAZKKB79JTgAAGHVybjpsaTptZW1iZXI6MTI4MDAxMjQyNCkImZePKBQ_wsgIfQWUSS0i6-dmUGCWTHTk9knhrciGs3m02ZJRFeH-yFBI0TCyHMX5Kd8EPiHgBntFKAhretAjvkccDcc8ygEzCHuxRi0ZPGGzGcZOSLlZN-3pq6L99DGZNdps7hvn5kW1HqfbKbIlI-rEU5bi2o8fboIgbmCz1bPljvmACEwrIX3kU2cNmKWDRt0",
                        "Yushko Patrick"
                },

                {       "daniele-tsvetkov",
                        "daniele.tsvetkov@outlook.it",
                        "33222200Shin",
                        "AQEDAUtiZkQEzpptAAABjYItJMIAAAGSigdqoE0AXvavHfjpr35J7Ncy9oEmlKpEz-K2MwJfaNqhtNxtQCdk9YWFkTIPRA_fwnwXbgrd6xM2FYW5iFsyKfTA5O_T7Z9M-JuJadMsZ8a_lEd_3k-HxK5P",
                        "Daniele Tsvetkov"
                },

                {       "michael-krusciov",
                        "michael.krusciov@outlook.de",
                        "cTsH3KhU",
                        "AQEDAUwy4cUFlrcZAAABkooGJoQAAAGSrhKqhE0As94EPe1YU5RRsVgWIEQkpNmziej7Jk9xqajz10nrIXUyanph0wCbHQG7QJrxLobL_AQ3drjDyrb1L2VhUANH_lyinIv2mx4ZolwYfUKBWj7t6lyA",
                        "Michael Krusciov"
                },

                {       "michael-zhmorshchuk-3161302b2",
                        "zhmorshchuk.michael@outlook.de",
                        "33222200Shin",
                        "AQEDAUsgIMIDmVD3AAABkTQUZv8AAAGSjt6EoU0AV2dCQldBkYHaNYTBVXuhnEhimF-2mdUXitJuX7y4KSTuleT7KK62teqN0ntBlDO6Mf2juliqKmJ4Wy6GgTWe10Jaa9eEUp3AVXHHP1zf_6MOIZqj",
                        "Zhmorshchuk Michael"
                },


                {       "kenan-strelbytsky-364ba22b8",
                        "strelbytsky.kenan@outlook.de",
                        "ygm9ijzZ",
                        "AQEDAUxZvXwBvIgNAAABj3xBliYAAAGSigrIwU0AcgYVfHaTonyE1HaLyc5y0kx5MKcWw2YbYqyCdFQbs5Di068bge4ojQloWC5XLUBq3a1ymp0Nbbad12hE1JhivoPhwxuBXboJYb-S9K2DxVQ4iApa",
                        "Strelbytsky Kenan"
                },





        };
    }


    public void changeLeadStatusAttemptToContacted(String id){
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
    public void changeLeadStatus(String id, String transitionsId, String transitionsStatus){
        String changeLeadStatusResponse;
        JSONObject changeLeadStatusResponseJson;
        changeLeadStatusResponse = zohoCrmHelper.changeLeadStatus(id, token, transitionsId);
        changeLeadStatusResponseJson = new JSONObject(changeLeadStatusResponse);;
        System.out.println("code: " + changeLeadStatusResponseJson.getString("code") );
        System.out.println(changeLeadStatusResponseJson);
        System.out.println("\n" );
        if (changeLeadStatusResponse.contains("INVALID_TOKEN")) {
            token = zohoCrmHelper.renewAccessToken();
            zohoCrmHelper.changeLeadStatus(id, token, transitionsId);
        }
        if (changeLeadStatusResponseJson.getString("code").equals("RECORD_NOT_IN_PROCESS")) {
            System.out.println("Try direct change:\n" + zohoCrmHelper.directChangeLeadStatus(id, token,transitionsStatus) );
        };
    }
}
