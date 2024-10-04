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
                        "AQEDAUs6XDsBmGroAAABkh9htIUAAAGScUK0VFYAiXEckJH2_AWGNuougsbwxE6a8_wIV233ZjJ8wSNWVU4UycjmMxDQ6DPJQ3mU3dM2XphYdEniE1TQlgioI6y90U1mnibqPYSUVtajeEC3B857x8jG",
                        "Elias Danilov"
                },

                {       "stefania-mykhaylenko",
                        "mykhaylenko.stefania@outlook.fr",
                        "cTsH3KhU",
                        "AQEDAUxQ7yQEegIPAAABkkHuZ5oAAAGSZfrrmk0AW4hARHjysCU74AttyXwTm3LkU8Ki0U2Hu4pjgfrgHgZrlQrgHuQadpLfK_8ty8ficLDQKenOzW0Ww-ZtJyH6MR9ki5ikksTZdqltwl9c7lpGcXB2",
                        "Mykhaylenko Stefania"
                },

                {       "den-vaviron",
                        "denVavir00@outlook.de",
                        "33222200Shin",
                        "AQEDAUpubFMFCUCvAAABjP1fQdMAAAGSWAXqB00AQjnJ76-7ZRPsHaXwTKEAr9arpU3dbfo75jEv_8uxAMPkA7fzR0PnqAAfWslJWxnzdC-wPWsV9LATEAW0Xn5yYSkq5tt5rta_cTP5SzMxSiEgxA_S",
                        "Den Vaviron"
                },


                {       "max-mikhaylov",
                        "max.Mikhaylov@outlook.de",
                        "33222200Shin",
                        "AQEDAUtQFBQCjJC7AAABjp8_IKQAAAGSWAV9EE0AqZ8aA6NXTjnzopjEk-6RAHYarurRryUBPwgA_1VSTOLTuH10QOR4AyiMEhdKSby_WImQZKuTUPgIVVxPAsBUM-1mI5H1EJPXXLdyQFj7dYO54lRC",
                        "Max Mikhaylov"
                },

                {       "patrick-yushko-b2080b2b8",
                        "yushko.patrick@outlook.it",
                        "206GLMC2",
                        "AQEFAHUBAAAAABBl9t0AAAGPfE7YuwAAAZJl-qvgVgAAGHVybjpsaTptZW1iZXI6MTI4MDAxMjQyNJGhqrGAw0s-VABnEas4z3_d7E7iUoihe4KC5vripjAlG7xljWADvr3hflDc7vYgfidAug0wkMLkBbnQPWneZ2zMEhxGiMz5asYjyKFVbYclxv-15jUg8vRIm55acrrkbjH66U1zPn1iQ-moQxwe45pAyR2xmK1dIYSJXyXYSBy-a5eos-B7BCV-MORD_QUs9mSx72Y",
                        "Yushko Patrick"
                },

                {       "daniele-tsvetkov",
                        "daniele.tsvetkov@outlook.it",
                        "33222200Shin",
                        "AQEDAUtiZkQEzpptAAABjYItJMIAAAGSZfpK6k0Al67Mz7b7jmM9u4FWm6M86kLQXN3qOmjuXHFL5kwL4nmvymFcMgyI_cZCyqk1Eq0Yfyfdrn516hF_A1zQH48tYI2suw5PzolYy7SMU9KYF_jV_OOp",
                        "Daniele Tsvetkov"
                },


                {       "alex-kychyn",
                        "kychyn.alex@outlook.it",
                        "33222200Shin",
                        "AQEFAHUBAAAAABAq9fUAAAGRIjMvzwAAAZJXosDwTgAAGHVybjpsaTptZW1iZXI6MTI2NTU4NjI2OYBDDVuNxs7gIY07RMmtpKkz75MnvGTF3LwM6nvMWX-3_lvXvX-XVwHBrPjJ3a-pvN5qA0aNM2rbKFismFnYhnwKHZ2ZmKtDW8Iklofc711iML-VSn7ciDJjv4pz4JGhGlPyrjlKAz4pBdQQ-394UWbx2G8XAnyav_9PE12dJoHrZN80xwoMV8o1os5QM4H7l-l2158",
                        "Kychyn Alex"
                },


                {       "miyana-emelianenko",
                        "emelianenko.miyana@outlook.de",
                        "CcDZPmv7",
                        "AQEDAUxZNjwAZkXFAAABj3bhQFAAAAGSWAT2gU0AQ4UVw_vtvGV1nkmw7AhF6iJ162bHbdSQDbbZ8URAS5M8LDME7AkvgBKZnIQ0hCExYhobzcmok__EQ7sYkyxVJONtXcI-Lz4RSkp1yBCpFa30uCsx",
                        "Emelianenko Miyana"
                },

                {       "fidel-salyenko-8183a92b3",
                        "fidel.Salyenkooo1962@outlook.it",
                        "33222200Shin",
                        "AQEDAUtXv1QFgKDNAAABjYINrOwAAAGSV6DNp00APOMjqKWazaVEby9F7GuyG7H1cqW1ooZhMDcaURSLLhQp0RzSKGMZKHhA8OnSMIY5YWL3CN9NYR0m7MwmG0f26TKOzxzwMzMLiHJGY2RuFyXFFSy6",
                        "Fidel Salyenko"
                },

                {       "michael-krusciov",
                        "michael.krusciov@outlook.de",
                        "cTsH3KhU",
                        "AQEDAUwy4cUF84KUAAABkleNDrMAAAGSe5mSs1YAHhd65fXcQTNNfl652iE7etF1UlPZ3AKKE3xOhcttytUL87noJsRaIRz_Ec-rVStVen51EaJkZjfqXlxVb6-F2tK0pykFxWcjY8BqXSfDIsYMYx8fЯЯЯ",
                        "Michael Krusciov"
                },


                {       "michael-zhmorshchuk-3161302b2",
                        "zhmorshchuk.michael@outlook.de",
                        "33222200Shin",
                        "AQEDAUsgIMIDmVD3AAABkTQUZv8AAAGSZf0DvE0AGB4hAwWCKA7L6ynUMHK_FQs62Af8F0AVxptP5PZjIr7O2OcsykkgTNcGIAF8j4jgql0ixptIGkjRI3ZVEDznBNrTq5szUfKuDgQICHMZz-h4PpMM",
                        "Zhmorshchuk Michael"
                },


                {       "kenan-strelbytsky-364ba22b8",
                        "strelbytsky.kenan@outlook.de",
                        "ygm9ijzZ",
                        "AQEDAUxZvXwBvIgNAAABj3xBliYAAAGSZf14s00Aj7F88vitr59DxVQ_H3rYmpkh7nF5usbMBb6CM-lo19a7CZXqZjm1LE5A_PSh4J6v9hsHOysVuONSUojiSMSoHNCKWGQXagpJgQzv_4O6BDbj_QfQ",
                        "Strelbytsky Kenan"
                },


                {       "alex-kychyn",
                        "kychyn.alex@outlook.it",
                        "33222200Shin",
                        "AQEFAHUBAAAAABAq9fUAAAGRIjMvzwAAAZJXosDwTgAAGHVybjpsaTptZW1iZXI6MTI2NTU4NjI2OYBDDVuNxs7gIY07RMmtpKkz75MnvGTF3LwM6nvMWX-3_lvXvX-XVwHBrPjJ3a-pvN5qA0aNM2rbKFismFnYhnwKHZ2ZmKtDW8Iklofc711iML-VSn7ciDJjv4pz4JGhGlPyrjlKAz4pBdQQ-394UWbx2G8XAnyav_9PE12dJoHrZN80xwoMV8o1os5QM4H7l-l2158",
                        "Kychyn Alex"
                },

                {       "miyana-emelianenko",
                        "emelianenko.miyana@outlook.de",
                        "CcDZPmv7",
                        "AQEDAUxZNjwAZkXFAAABj3bhQFAAAAGSWAT2gU0AQ4UVw_vtvGV1nkmw7AhF6iJ162bHbdSQDbbZ8URAS5M8LDME7AkvgBKZnIQ0hCExYhobzcmok__EQ7sYkyxVJONtXcI-Lz4RSkp1yBCpFa30uCsx",
                        "Emelianenko Miyana"
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
