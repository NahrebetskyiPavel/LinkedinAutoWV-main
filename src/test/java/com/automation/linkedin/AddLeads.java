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
    int leadLow = 5;
    int leadsHigh = 20;
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
      //      System.out.println("data:\n" + data);
        if (data.contains("INVALID_TOKEN")){
            String token = zohoCrmHelper.renewAccessToken();
            data = zohoCrmHelper.getLeadList( token, j,  "Waiting",  linkedinperson);
        }
        if (data.length()<=1){
            throw new Exception("NO LEADS FOUND");
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

            String personRef = originalUrl.replaceAll("http://.*?linkedin", "http://www.linkedin")
                                            .replaceAll("//", "")
                                            .replaceAll("//", "")
                                            .replaceAll("https:", "https://")
                                            .replaceAll("http:", "http://");

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
                    if (taskStatus.contains("expired")) {
                        System.out.println("Status is now 'expired'.");

                        throw new Exception("Status is now 'expired");
                    };
                    if (taskStatus.contains("finished")) break;
                    if (taskStatus.contains("failed")) break;
                }
                if (taskInfo.contains("To verify this member knows you, please enter their email to connect. You can also include a personal note")) {
                    //System.out.println("ERROR: " + new JSONObject( taskInfo ).getJSONArray("results").getJSONObject(0).getString("error"));
                    System.out.println("Status is now 'error'.");
                    System.out.println("email required.");
                    changeLeadStatus(id,broken, "broken");
                    continue;
                };
                if (taskInfo.contains("Invalid url")) {
                    //System.out.println("ERROR: " + new JSONObject( taskInfo ).getJSONArray("results").getJSONObject(0).getString("error"));
                    System.out.println("Status is now 'error'.");
                    System.out.println("Invalid url.");
                    changeLeadStatus(id,broken, "broken");
                    continue;
                };
                if (taskInfo.contains("ERR_TUNNEL_CONNECTION_FAILED")) {

                    System.out.println("Status is now 'error'.");
                    System.out.println("ERR_TUNNEL_CONNECTION_FAILED");
                   throw new Exception("ERR_TUNNEL_CONNECTION_FAILED");
                };
                if (taskInfo.contains("ERR_SSL_PROTOCOL_ERROR")) {

                    System.out.println("Status is now 'error'.");
                    System.out.println("ERR_SSL_PROTOCOL_ERROR");
                   throw new Exception("ERR_SSL_PROTOCOL_ERROR");
                };
                if (taskInfo.contains("status code 594")) {

                    System.out.println("status code 594");
                    System.out.println("status code 594");
                   throw new Exception("status code 594");
                };
                if (taskInfo.contains("Profile link invalid")) {
                    System.out.println("ERROR: " + new JSONObject( taskInfo ).getJSONArray("results").getJSONObject(0).getString("error"));
                    System.out.println("Status is now 'error'.");
                    System.out.println("Profile link invalid.");
                    changeLeadStatus(id,broken, "broken");
                    continue;
                };
                if (taskInfo.contains("Cookie is not valid")) {
                    System.out.println("Cookie is not valid");
                    throw new Exception("Cookie is not valid!");
                };
                if (taskInfo.contains("Navigation timeout of 30000 ms exceeded")) {
                    System.out.println("Navigation timeout of 30000 ms exceeded");
                    throw new Exception("Navigation timeout of 30000 ms exceeded");
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

                    if (taskInfo.contains("Invitation already sent")) {
                        System.out.println("ERROR: " + new JSONObject( taskInfo ).getJSONArray("results").getJSONObject(0).getString("error"));
                        System.out.println("Invitation already sent'.");
                        changeLeadStatusAttemptToContacted(id);

                        continue;
                    };
                    if (taskInfo.contains("write EPROTO")) {
                        System.out.println("write EPROTO proxy error");
                        throw new Exception("write EPROTO proxy error");
                    };
                    if (taskInfo.contains("Profile already in connections")) {
                        System.out.println("ERROR: " + new JSONObject( taskInfo ).getJSONArray("results").getJSONObject(0).getString("error"));
                        System.out.println("Profile already in connections");
                        changeLeadStatusAttemptToContacted(id);

                        continue;
                    };
                    if (taskStatus.contains("processing")) statusChecker.waitForStatus("finished", taskStatus);
                    if (taskStatus.contains("failed")) {
                        System.out.println("ERROR: " + new JSONObject( taskInfo ).getJSONObject("results").getString("error"));
                        System.out.println("Status is now 'failed'.");
                        continue;
                    };
                    if (taskInfo.contains("error")) {
                        System.out.println("ERROR: " + new JSONObject( taskInfo ).getJSONArray("results").getJSONObject(0).getString("error"));
                        System.out.println("Status is now 'error'.");
                        continue;
                    };
                    if (taskInfo.contains("Invalid url")) {
                        System.out.println("ERROR: " + new JSONObject( taskInfo ).getJSONArray("results").getJSONObject(0).getString("error"));
                        System.out.println("Status is now 'error'.");
                        System.out.println("Invalid url.");
                        changeLeadStatus(id,broken, "broken");
                        continue;
                    };
                    if (taskInfo.contains("Invalid url")) {
                        System.out.println("ERROR: " + new JSONObject( taskInfo ).getJSONArray("results").getJSONObject(0).getString("error"));
                        System.out.println("Status is now 'error'.");
                        System.out.println("Invalid url.");
                        changeLeadStatus(id,broken, "broken");
                        continue;
                    };

                    if (taskStatus.contains("write EPROTO")) {
                        System.out.println("ERROR: " + new JSONObject( taskInfo ).getJSONArray("results").getJSONObject(0).getString("error"));
                        System.out.println("write EPROTO proxy err'.");

                        break;
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

                {       "Evgeny-Gazitov",
                        "evgeny.gazitov8753@outlook.it",
                        "33222200Shin",
                        "AQEDAUsJgSoACPECAAABlX91PhYAAAGVo4HCFlYARxppAFEu2z2PuxjuXYBO7OBLatNyhy84r3VkAanlhTBQyIvD4vbvLbIG1mhiiw7EpTXvuy0eNGNU9nGxGP-_rQqEi5lbfoQQper6afSjzZeQEfIQ|eyJsYW5ndWFnZXMiOlsiaXQtSVQiLCJpdCIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJpdC1JVCIsInRpbWVab25lIjoiRXVyb3BlL1JvbWUiLCJpcCI6Ijk1LjI0MC4yMDYuMTk0In0=",
                        "Evgeny Gazitov"
                },

                {       "Johan-Heinlein",
                        "johan.heinlein@outlook.de",
                        "eGdFPRgS",
                        "AQEDAUxEX5oApf6pAAABjglKvk4AAAGU37pv3E0Aj86fZrt57nf9dDQ9L2ycAOFb1RU57UHtsDbugqCEgQI9RWHVyVFlgLNiv9OrCA8Ljw3_6SwfcVDdvLa4sBTiEznok-P7bnKgFpD9l5u9N5ilTHF_|eyJsYW5ndWFnZXMiOlsiZGUtREUiLCJkZSIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJkZS1ERSIsInRpbWVab25lIjoiRXVyb3BlL0JlcmxpbiIsImlwIjoiMzcuMjAxLjE5OS4yMSJ9",
                        "Johan Heinlein"
                },
                {       "Artemio-Chumakov",
                        "artemio.chumakov1981@outlook.it",
                        "33222200Shin",
                        "AQEDAUtCHbcD5C-bAAABkcaWbF4AAAGU37dZ7E0AvbdmKlhCZSBkmNh-AjzXAruOXXZvwJkbpduIywU_XK8rQ9OA7HvxFIWRKos9zQdvCNqVWfhMTSR_HsNJoGlt2gECiowvI-wx55iz52P1Qvy4Z0Tz|eyJsYW5ndWFnZXMiOlsiaXQtSVQiLCJpdCIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJpdC1JVCIsInRpbWVab25lIjoiRXVyb3BlL1JvbWUiLCJpcCI6bnVsbH0=",
                        "Artemio Chumakov"
                },

                {       "patrick-yushko-b2080b2b8",
                        "yushko.patrick@outlook.it",
                        "206GLMC2",
                        "AQEFAHUBAAAAABBl9t0AAAGPfE7YuwAAAZMaRBSVVgAAGHVybjpsaTptZW1iZXI6MTI4MDAxMjQyNMyB0U3N4EXZoT7jRSgrh2ZsRQhw3dIPIjxxs7HK2bI8jIXKrSaNXE-7GhbppvBOQSO2mokFi0nLkNu11TQ3PPReQB8-2boUF0iWaZ7L3W1dFU9X07glDGQPpLsaMRPWQju-eZbs2y2zeE1w8P2PvROcYJDwbJXaXTTxwbFor9oT7iUhsfAU30z5UF7qX0VUHdnorZw",
                        "Yushko Patrick"
                },


                {       "michael-krusciov",
                        "michael.krusciov@outlook.de",
                        "cTsH3KhU",
                        "AQEDAUwy4cUDY3T1AAABlX9lJx8AAAGVo3GrH00AKYznEfusBysjPG529_HO2FaM1rf2Ua3DKqRXYTGS-ToJDCCXB2uqjFMIjTqjO_iHygqxb3sjwA0RSj6E8wO7GDTSGmEBWVrxyvbUPry7PJKavlV0|eyJsYW5ndWFnZXMiOlsiZGUtREUiLCJkZSIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJkZS1ERSIsInRpbWVab25lIjoiRXVyb3BlL0JlcmxpbiIsImlwIjoiMTg1LjE4My4xNzkuNiJ9",
                        "Michael Krusciov"
                },


                {       "Anastasiia-Vozniak",
                        "vozniakanastasia52@gmail.com",
                        "asd2424qq",
                        "AQEDATp4HzoBa6O7AAABlGlxPOgAAAGUjX3A6E0AwNpK6n3p3Ulp5DO-BwvtTrsYEBYy9LxAD6xohwUCfLQO52TrKY4CuGewUPx04ho-yh9X6s0CZ_L_uBtMC0MvER8xlAxfO5ENZX--bzIUQuvtufWI|eyJsYW5ndWFnZXMiOlsidWstVUEiLCJ1ayIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJ1ay1VQSIsInRpbWVab25lIjoiRXVyb3BlL0tpZXYiLCJpcCI6IjE4OC4xNjMuMjYuNjkifQ==",
                        "Anastasiia Vozniak"
                },
                {       "Art-Stenko",
                        "artstenko@gmail.com",
                        "GOgoCyclone_11",
                        "AQEDAR1-TWgCdaXbAAABlGmXZtsAAAGU71o8iU0AX2vCEj5S0ms2kzRxYBRPWlb-BlkleFw59RilRgTGUZKEKeA3LHcXC5v2PEGQ4htj0m20Zgs8vTv6FxAjj6ZwLwCao7rehPhPpbiaXZbxwkBFGHGs|eyJsYW5ndWFnZXMiOlsiZGUtREUiLCJkZSIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJkZS1ERSIsInRpbWVab25lIjoiRXVyb3BlL0JlcmxpbiIsImlwIjoiMTg4LjI0NS4xOTkuMjA1In0=",
                        "Art Stenko"
                },
                {       "lina-Kompanets",
                        "ekompanets02@gmail.com",
                        "35ulurev",
                        "AQEDASj3SfwEGLIvAAABlGmC3Y4AAAGU70nIQk0Ai4A6U01EkeKnuSgP56RlxVoG-olHOnD8HPJtTuBozbjz6UZCSih9CDJB8pukeO7YCWMq24saiShfxJkZyoo_ZtfaW5TZm_l29SXGTBWvz5JSGdgZ|eyJsYW5ndWFnZXMiOlsiZGUtREUiLCJkZSIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJkZS1ERSIsInRpbWVab25lIjoiRXVyb3BlL0JlcmxpbiIsImlwIjoiMTg4LjI0NS4xOTkuMjA1In0=",
                        "lina Kompanets"
                },
                {       "Nikita-K",
                        "kni2012@ukr.net",
                        "33222200s",
                        "AQEDASE8mKgD8e3SAAABlGmER_UAAAGUjZDL9VYAMzZSaOOCF1F8PcfZQznfhQYTQutnyrKzLmQzv-g0CmX1Nu-ZlsnpsXHketfREZe-Bl_GFzC5dVXFk1iw8Gw5iD5EDxhi5DmPxWTxZCI58QmHNpm8|eyJsYW5ndWFnZXMiOlsidWstVUEiLCJ1ayIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJ1ay1VQSIsInRpbWVab25lIjoiRXVyb3BlL0tpZXYiLCJpcCI6IjE4OC4xNjMuNjkuMzMifQ==",
                        "Nikita K"
                },
                {       "Maria-Deyneka",
                        "deynekamariawv@gmail.com",
                        "3N2wbnsw",
                        "AQEDATpgt8MFzfWtAAABlGmMmFwAAAGUjZkcXE0AvlevlyzCIVWISv_-5GXi67C29LB7IMZOc9D-uWJDSDvBtN_p16tzzEquHGNEL-C4ct8uXrUwfsbpeY4tDJ2IqPq83BQfohaEMNtZrSAr8KWxPqlw|eyJsYW5ndWFnZXMiOlsidWstVUEiLCJ1ayIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJ1ay1VQSIsInRpbWVab25lIjoiRXVyb3BlL0tpZXYiLCJpcCI6IjE3OC4xMzYuMzcuMjE5In0=",
                        "Maria Deyneka"
                },
                {       "Marian-Reshetun",
                        "reshetunmaryanwv@gmail.com",
                        "33222200Shin",
                        "AQEDATpm9GsDKEL5AAABlGmOfpAAAAGU_02FZE0ALTKPrP2OgK7G6YxUJw3HzHzxtFy65g0LZreuBTXCZ_7dufkRTICalYOnUuAkHioX3kupbIyjpHnuLlB2ML8h7MX7BLxvoRhl7K9IOQ5bmWjI2z6H|eyJsYW5ndWFnZXMiOlsiZGUtREUiLCJkZSIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJkZS1ERSIsInRpbWVab25lIjoiRXVyb3BlL0JlcmxpbiIsImlwIjoiMTg4LjI0NS4xOTkuMjA1In0=",
                        "Marian Reshetun"
                },
                {       "Anastasiia-Kuntii",
                        "anastasiiakuntii@gmail.com",
                        "33222200Shin",
                        "AQEDASosSSoEwgrNAAABlGmRGiUAAAGUjZ2eJU0AhC51KbwrLYzi4wa2ytQxdxJv4VpDm3awy0TQZEatEjsuQgmEZI5rzK1ANRo6I-kuNg72s-33zhWTr5kS4quQwdpXozxkrjzKyNkESDnlF0vDuas4|eyJsYW5ndWFnZXMiOlsidWstVUEiLCJ1ayIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJ1ay1VQSIsInRpbWVab25lIjoiRXVyb3BlL0tpZXYiLCJpcCI6IjE4OC4xNjMuMjguNTEifQ==",
                        "Anastasiia Kuntii"
                },
                {       "Natalia-Marcun",
                        "natalia.marcoon@gmail.com",
                        "asd321qq",
                        "AQEDATxzPCcDSaa2AAABlGmTliAAAAGUjaAaIE4AWy01oPscxLvE1AGPoHL1b-BM9xTko4B66dc5mgq9BXLfVA3_PgVtwp5_zNEEtAKpvant2d58dlQWprhSe1W83oGvH82--WTubb20UvdZPxeONqCd|eyJsYW5ndWFnZXMiOlsidWstVUEiLCJ1ayIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJ1ay1VQSIsInRpbWVab25lIjoiRXVyb3BlL0tpZXYiLCJpcCI6IjE4OC4xNjMuNjUuMTk0In0=",
                        "Natalia Marcun"
                },
                {       "Aleksandra-Sternenko",
                        "alexandra.sternenko@gmail.com",
                        "asd321qq",
                        "AQEDATxvso0AsoGrAAABlGmcpMYAAAGUjakoxk0At3rdIcYwg58nfPB100bIp55gaun_CLHbmRhw-J9lsRGXjBUy8peYKS15_zVsmeIbGpBPQzmETdRZntEpa4d7CQxIDcAy5Cn7m0nPv-__V08N5W8L|eyJsYW5ndWFnZXMiOlsidWstVUEiLCJ1ayIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJ1ay1VQSIsInRpbWVab25lIjoiRXVyb3BlL0tpZXYiLCJpcCI6IjE4OC4xNjMuODIuMTgxIn0=",
                        "Aleksandra Sternenko"
                },

                {       "Matthew-Martinez",
                        "mMartiz11@outlook.it",
                        "metmar11mmjy",
                        "AQEDAUvkzscEuod7AAABjcxZHVEAAAGVZaJUXk0AZtEe_NDTtXzWjNSIhGHwaK6uxLrfIID9n94Trrah5UBV8qZbnsjj0uRerARQuFR5AHFRd3uAGQFE5uMGzGZIzZNmYbG90TRwHOjFjCHsFnQDGI4f|eyJsYW5ndWFnZXMiOlsiaXQtSVQiLCJpdCIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJpdC1JVCIsInRpbWVab25lIjoiRXVyb3BlL1JvbWUiLCJpcCI6Ijg0LjMzLjI1MS4xNzgifQ==",
                        "Matthew Martinez"
                }
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
