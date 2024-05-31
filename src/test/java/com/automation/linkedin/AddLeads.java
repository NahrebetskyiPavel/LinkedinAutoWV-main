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
    @Test(description = "add leads from CRM", dataProvider = "dataProviderPeopleSearch", alwaysRun = true )
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
                //wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","TOTAL = " + totalLeadsAddedCount + "\n");
                //wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","Finish \n"  + "account = " + linkedinperson + " "+ leadsAddedCount + " leadsAdded = " + leadsAddedCount + "\n");
            {
              String response =  wiseVisionApiHelper.impastoAddToFriends(profileId, email, password, cookie, personRef);
                Thread.sleep(1000*60);
              int taskId = (int) new JSONObject( response ).get("taskId");
              String taskInfo = wiseVisionApiHelper.impastoGetTaskinfo(profileId, taskId);
              String taskStatus = new JSONObject( taskInfo ).getString("status");
              String taskResult = String.valueOf(new JSONObject( taskInfo ));
                if (taskResult.contains("error") && taskResult.contains("Invitation already sent")) {
                    changeLeadStatusAttemptToContacted(id);
                    continue;
                };
                if (taskResult.contains("error") && taskResult.contains("Invalid url")) {
                    changeLeadStatus(id, broken, "Broken");
                    continue;
                };
                if (taskResult.contains("error") && taskResult.contains("Profile link invalid")) {
                    changeLeadStatus(id, broken, "Broken");
                    continue;
                };
                if (taskResult.contains("error") && taskResult.contains("Task expired")) {
                    changeLeadStatus(id, broken, "Broken");
                    continue;
                };
                String taskResults;
                if (new JSONObject( taskInfo ).get("results") instanceof JSONArray) {
                     taskResults = String.valueOf(new JSONObject( taskInfo ).getJSONArray("results").getJSONObject(0));
                } else {
                    Thread.sleep(60000);
                     taskResult = String.valueOf(new JSONObject( taskInfo ).get("results"));
                     if (taskResult.contains("null"))                     Thread.sleep(60000);
                     if (taskStatus.contains("processing"))                     Thread.sleep(60000);
                     if (taskResult.contains("Proxy connection ended before receiving CONNECT response")) continue;
                     if (taskResult.contains("Cookie is not valid")) {
                         System.out.println("Cookie is not valid");
                         break;
                     };
                    if (taskStatus.contains("processing"))                     Thread.sleep(60000);
                    taskResults = String.valueOf(new JSONObject( taskInfo ).getJSONArray("results").getJSONObject(0));
                }

                try {
                    statusChecker.waitForStatus("finished", taskStatus);

                    if (taskStatus.contains("processing")) statusChecker.waitForStatus("finished", taskStatus);

                    if (taskResults.contains("error")) {
                        System.out.println("ERROR: " + new JSONObject( taskInfo ).getJSONArray("results").getJSONObject(0).getString("error"));
                        System.out.println("Status is now 'error'.");

                        continue;
                    };
                    if (taskStatus.contains("expired")) {
                        System.out.println("ERROR: " + new JSONObject( taskInfo ).getJSONArray("results").getJSONObject(0).getString("error"));
                        System.out.println("Status is now 'expired'.");

                        continue;
                    };
                    System.out.println("Status is now 'finished'.");


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
                {       "margit-matthes",
                        "margit.Matthes@outlook.de",
                        "33222200Shin",
                        "AQEDAUuampkDA9uSAAABjzNCPogAAAGPV07CiFYAUTlaawCUQBq15anEMNwZmKlCqaTK2oqUSr-P05fihZvoGHPgFk7KZroX9ZKpVEbvEjOLth1xcqkJDG3F_a2_o_3WYwet4mooFR5SjuTy2Z_eezlj",
                        "Margit Matthes"
                },
                {       "andrei-gorbunkov-a34b4a2aa",
                        "andreiGorbunkov@outlook.de",
                        "33222200Shin",
                        "AQEDAUqQcUgEIxsAAAABjvWFnKYAAAGPlUYADFYAombm43_GJ7Tg5JgPeG6gMA7igoCuX850p-7SUHJnbnQrAqxiiJv4ADi_L76d5_T8_1z0Ea_ZO7h2I-35JHOu43bflHEbj-G5kzFRsyhBhkJZwHc8",
                        "Andrei Gorbunkov"
                },

                {       "paul-bereza",
                        "paul.bereza02@outlook.de",
                        "33222200Shin",
                        "AQEFAREBAAAAAA9y_ngAAAGPTWyIZQAAAY9xerrsTQAAtHVybjpsaTplbnRlcnByaXNlQXV0aFRva2VuOmVKeGpaQUFDbnRYRkMwRzBrTkJ0UnhBdGNPak1TMFlRSS9uZXNWNHdJN0krWUM4REl3RE1hUW9RXnVybjpsaTplbnRlcnByaXNlUHJvZmlsZToodXJuOmxpOmVudGVycHJpc2VBY2NvdW50OjIxMjU2Mjg0OSwzMDMyMjU2NjUpXnVybjpsaTptZW1iZXI6MTI2NjM4OTU1Mqtahj54xFZSxnrvy1Fhjo_T5CKqWfcLzzEpATi94tKyJSkHYCbgDxcQJJBeH2a8gJu17Nee6uDtPtfo_xlPThNbQRG8NrY1MvzU75pvL6Rvd7fsGhGb9M58udN77XZ1qFysniogLshJfVj7ldODM6ZXSGdf68Gml-blsKo3mCQX9GbJCdGFEc0IjGUj57r7S4gaOng",
                        "Paul Bereza"
                },
                {       "alessio-vacenko-b506612b3",
                        "alessio.Vacenko@outlook.it",
                        "33222200Shin",
                        "AQEDAUthqywCOthiAAABj00h33QAAAGPvkp4OE4ASeyim2hF4oGJkCfAbCkjDWmIdJfwVO3XyI1HeaM_0-kmXSiiMnwHo7xnp6urbl5O3VczT6kuWjuBlXHiAKh7-TSsVSn6Vrc2y521ONn_sVF2klqU",
                        "Alessio Vacenko"
                },
                {       "margit-matthes",
                        "margit.Matthes@outlook.de",
                        "33222200Shin",
                        "AQEDAUuampkDA9uSAAABjzNCPogAAAGPV07CiFYAUTlaawCUQBq15anEMNwZmKlCqaTK2oqUSr-P05fihZvoGHPgFk7KZroX9ZKpVEbvEjOLth1xcqkJDG3F_a2_o_3WYwet4mooFR5SjuTy2Z_eezlj",
                        "Margit Matthes"
                },
                {       "dmitriy-timashov",
                        "timashov.dmitriy@outlook.de",
                        "33222200Shin",
                        "AQEDAUsszhwD-UTVAAABjzNJL1kAAAGPV1WzWU0Am_0hKUQRi5bvt9ZVU15-Yig0zqKpUaul-TQl69vjsUu4AX_e5zQ9o0HR6yR3IQZpg56KgFMtjg6JC54K00TwLxRDc-_GMW5K4ECEUrrX8BVS6nZ7",
                        "Dmitriy Timashov"
                },

                {       "eliza-kolner",
                        "eliza.kolner0103@outlook.de",
                        "ek03303KK",
                        "AQEFAREBAAAAAA9zBMEAAAGPTWfEpAAAAY_lKiqTTQAAtHVybjpsaTplbnRlcnByaXNlQXV0aFRva2VuOmVKeGpaQUFDbnRYRkUwRzBrTkNMbXlCYTRIRGhTa1lRSS9uZVhsMHdJN0krNVFZREl3RExpQW5SXnVybjpsaTplbnRlcnByaXNlUHJvZmlsZToodXJuOmxpOmVudGVycHJpc2VBY2NvdW50OjIxMjU2MjgzMywzMDMyMjkxNDUpXnVybjpsaTptZW1iZXI6MTI4MDM3NTY4NWp8bEsKJ75ogALhXQXQOlD3_brt0oR5mr_80U-ICsdrFqy-1_NJDjBInsNdgTVTTTiucKXT3yLe7uO0ozSA-OpT2CoPk-eUXF8diJIz_3Z_ciTBjWjlYNDM16fYlOI0mnZ_P-RdJDWNMvtQwywWon_TNUL5nyhQROKH4yOtj5VTz4BbSV5zMWjs405mZxzJEzUYwds",
                        "Eliza Kolner"
                },

                {       "Matthew Martinez",
                        "mMartiz11@outlook.it",
                        "metmar11mmjy",
                        "AQEDAUvkzscEuod7AAABjcxZHVEAAAGPM0jgWU0AxNq9k7vcTnaST2Y01G-gU50hTFao_KHUxDpMO4OkxBjTDSjb2Mt1lbEXESZCcCoYA9Pd0qUZmLLUFDt-Eeh2HHGvi5izLQvjtJbOcQh7J1J7ZZOI",
                        "Matthew Martinez"
                },

                {       "Sergio Cheban",
                        "sergio.Cheban@outlook.it",
                        "33222200Shin",
                        "AQEDAUtjbVAFBgsLAAABjYNqZdIAAAGPMzVut00ASQCqvztRswdvlx8bV-veaBhTipBEPJIpAdvgAkQHWkxZ1BmRwBk74yqXj_Milu2_xWCAnFN6nfHXW2FD91dKRuryVwMu87BBSdDcO1sXtWVxoefe",
                        "Sergio Cheban"
                },

                {       "Sherida Sluijk",
                        "s.Sluijk@outlook.de",
                        "33222200Shin",
                        "AQEDAUtyJqUALIwdAAABjxTmPLIAAAGPOPLAsk0ApibiizcM0XQOpyUn2klpIGBn5rXDSFfvwHtCMcayud_Iu5IP3PbCe_SypwV4VqRrQInooXtqyzKhPvwbBXFZbFPh9-gDbqvWhBe8zwnbeD7RsPCy",
                        "Sherida Sluijk"
                },

                {       "petr-2",
                        "petr.degtyarev@outlook.de",
                        "33222200Shin",
                        "AQEDAUs5cmoFe9mZAAABjlyBf2oAAAGPMzVXok0Aase-74YwTB2el__HXiApMk1VKjDZtiYnIDeGI_Ez0PCLecWzCQhotwuxGmOsGFdTDsfLoyvCiOiFkdEKpoiqtjPBEqoZR_aSsiFM2OhDXtMNs2cp",
                        "petr-2"
                },

                {       "elias-danilov",
                        "elias.danilov@outlook.it",
                        "33222200Shin",
                        "AQEDAUs6XDsCa_PgAAABjXEAR6wAAAGPvnUX8VYAR6FAgkuaV0y_vHtOvXsZpgRzeT26KlNYH93IyJIvw7UoOHeBkpUI_sp-S3bWqGBbPA_2Dxr_E6qhJL-v3awX4tp5K2b74cZe6QP1xVtzzrFYmS8L",
                        "Elias Danilov"
                },

                {       "stefania-mykhaylenko",
                        "mykhaylenko.stefania@outlook.fr",
                        "cTsH3KhU",
                        "AQEFAREBAAAAAA9y9r0AAAGPTV_V0AAAAY_lIlocTgAAtHVybjpsaTplbnRlcnByaXNlQXV0aFRva2VuOmVKeGpaQUFDbnRWdGhpQmFTT2g2Q1lnV09LekZ5QWhpSk4vci9ncG1STmJIYVRFd0FnQ2ozUWdCXnVybjpsaTplbnRlcnByaXNlUHJvZmlsZToodXJuOmxpOmVudGVycHJpc2VBY2NvdW50OjIxMjU2NzYwMSwzMDMyMjQ2OTIpXnVybjpsaTptZW1iZXI6MTI4MDM3MjUxNh1vv87hOCPXubfRNXRCFvEdBEeBTizDkYRNnTNWEhcaDcM0E_8th2IPZIiva19huKk8Mv64w5PAKLxrksA8bXi3Z2ohbE-aFQLQW9rp5YuEwvoHWELtI6RMALKMN3tLEWtwU-2FndA63Z_9laag79-jDLyNtln4wLlo1dYCv8ET9lSb3usRHMlNnjKXOS2ujwUwnxQ",
                        "Mykhaylenko Stefania"
                },

                {       "eliza-kolner",
                        "eliza.kolner0103@outlook.de",
                        "ek03303KK",
                        "AQEFAREBAAAAAA9zBMEAAAGPTWfEpAAAAY_lKiqTTQAAtHVybjpsaTplbnRlcnByaXNlQXV0aFRva2VuOmVKeGpaQUFDbnRYRkUwRzBrTkNMbXlCYTRIRGhTa1lRSS9uZVhsMHdJN0krNVFZREl3RExpQW5SXnVybjpsaTplbnRlcnByaXNlUHJvZmlsZToodXJuOmxpOmVudGVycHJpc2VBY2NvdW50OjIxMjU2MjgzMywzMDMyMjkxNDUpXnVybjpsaTptZW1iZXI6MTI4MDM3NTY4NWp8bEsKJ75ogALhXQXQOlD3_brt0oR5mr_80U-ICsdrFqy-1_NJDjBInsNdgTVTTTiucKXT3yLe7uO0ozSA-OpT2CoPk-eUXF8diJIz_3Z_ciTBjWjlYNDM16fYlOI0mnZ_P-RdJDWNMvtQwywWon_TNUL5nyhQROKH4yOtj5VTz4BbSV5zMWjs405mZxzJEzUYwds",
                        "Eliza Kolner"
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
