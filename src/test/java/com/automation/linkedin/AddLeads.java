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
import utils.GoogleSheets;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Array;
import java.util.Arrays;
import java.util.List;
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
    Random random = new Random();
    int low = 2000;
    int high = 5000;
    int randomResult = random.nextInt(high-low) + low;
    int leadLow = 21;
    int leadsHigh = 27;
    int leadsRandomResult = random.nextInt(leadsHigh-leadLow) + leadLow;
    String token = zohoCrmHelper.renewAccessToken();
    static int totalLeadsAddedCount = 0;

    @SneakyThrows
    @Test(description = "add leads from CRM", dataProvider = "dataProviderPeopleSearch", alwaysRun = true )
    public void addLeads(String name, String email, String password,  String msg, String linkedinperson){
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
        setupBrowser(true, name);
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
            if (!String.valueOf(new JSONObject( data ).getJSONArray("data").getJSONObject(i).get("Website")).contains("http")) continue;
            String personRef = new JSONObject( data ).getJSONArray("data").getJSONObject(i).getString("Website");
            System.out.println("personRef: " + personRef);
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
            try {

            }
            catch (Exception e){
                wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0",name +" SMTH WRONG");
                wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","TOTAL = " + totalLeadsAddedCount + "\n");
                wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","Finish \n"  + "account = " + name + " "+ leadsAddedCount + " leadsAdded = " + leadsAddedCount + "\n");

            }
            personPage.addToFriends(msg,false);
            Thread.sleep(randomResult);
            if (personPage.limitAlertHeader.exists()){

                System.out.println("\n========================================================================\n" +"Out of requests"+ "\n========================================================================\n");
                wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","Skip " + linkedinperson + " Out of requests \n");

                WebDriverRunner.getWebDriver().quit();
                break;
            }
            leadsAddedCount = leadsRequestCount++;
            System.out.println("Leads added from " + name + " account = " + leadsAddedCount);
            //wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","Leads added from " + name + "account = " + leadsAddedCount);
            if (leadsAddedCount==leadsRandomResult) {
                totalLeadsAddedCount = totalLeadsAddedCount + leadsAddedCount;

                wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","Finish \n"  + "account = " + name  + leadsAddedCount + " leadsAdded = " + leadsAddedCount + "\n");
                wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","TOTAL = " + totalLeadsAddedCount + "\n");

                break;
            };
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
            if (leadsAddedCount==leadsRandomResult) {
                wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","\nTOTAL = " + totalLeadsAddedCount + "\n");

                break;
            };
        }
    }

    @DataProvider(name = "dataProviderPeopleSearch", parallel=true)
    public static Object[][] dataProviderPeopleSearch() throws GeneralSecurityException, IOException {
        List<List<Object>> values = new GoogleSheets().getAccountsInfo();
        List<List<Object>> accountInfos = new GoogleSheets().getAccountsInfo();
        String[]  accountInfo = {"name", "login", "pass"};
        for (List row : values) {
            accountInfos.add(Arrays.asList(accountInfo));
            accountInfo[0] = (String) row.get(0);
            accountInfo[1] = (String) row.get(1);
            accountInfo[2] = (String) row.get(2);
            accountInfos.add(Arrays.asList(accountInfo));
            // Print columns A and E, which correspond to indices 0 and 4.
            System.out.println(accountInfos.get(0).get(0));
            System.out.printf("%s, %s, %s\n", row.get(0), row.get(1), row.get(2));
        }
        return new Object[][]{
                //1
                {
                        accountInfos.get(0).get(0),
                        accountInfos.get(0).get(1),
                        accountInfos.get(0).get(2),
                        "Hello there. I stumbled across your account accidentally and was impressed with your expertise. Would you mind accepting this invite so we could talk some more?",
                        accountInfos.get(0).get(0),
                },
                //2
                {
                        accountInfos.get(1).get(0),
                        accountInfos.get(1).get(1),
                        accountInfos.get(1).get(2),
                        "Hello there. I stumbled across your account accidentally and was impressed with your expertise. Would you mind accepting this invite so we could talk some more?",
                        accountInfos.get(1).get(0),
                },
                //3
                {
                        accountInfos.get(2).get(0),
                        accountInfos.get(2).get(1),
                        accountInfos.get(2).get(2),
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        accountInfos.get(2).get(0),
                },
                //4
                {
                        accountInfos.get(3).get(0),
                        accountInfos.get(3).get(1),
                        accountInfos.get(3).get(2),
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        accountInfos.get(3).get(0),
                },
                //5
                {
                        accountInfos.get(4).get(0),
                        accountInfos.get(4).get(1),
                        accountInfos.get(4).get(2),
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        accountInfos.get(4).get(0),
                },
                //6
                {
                        accountInfos.get(5).get(0),
                        accountInfos.get(5).get(1),
                        accountInfos.get(5).get(2),
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        accountInfos.get(5).get(0),
                },
                //7
                {
                        accountInfos.get(6).get(0),
                        accountInfos.get(6).get(1),
                        accountInfos.get(6).get(2),
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        accountInfos.get(6).get(0),
                },
                //8
                {
                        accountInfos.get(7).get(0),
                        accountInfos.get(7).get(1),
                        accountInfos.get(7).get(2),
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        accountInfos.get(7).get(0),
                },
                //9
                {
                        accountInfos.get(8).get(0),
                        accountInfos.get(8).get(1),
                        accountInfos.get(8).get(2),
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        accountInfos.get(8).get(0),
                },
                //10
                {
                        accountInfos.get(9).get(0),
                        accountInfos.get(9).get(1),
                        accountInfos.get(9).get(2),
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        accountInfos.get(9).get(0),
                },
                //11
                {
                        accountInfos.get(10).get(0),
                        accountInfos.get(10).get(1),
                        accountInfos.get(10).get(2),
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        accountInfos.get(10).get(0),
                },
                //12
                {
                        accountInfos.get(11).get(0),
                        accountInfos.get(11).get(1),
                        accountInfos.get(11).get(2),
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        accountInfos.get(11).get(0),
                },
                //13
                {
                        accountInfos.get(12).get(0),
                        accountInfos.get(12).get(1),
                        accountInfos.get(12).get(2),
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        accountInfos.get(12).get(0),
                },
                {
                        "Art Stenko",
                        "artstenko@gmail.com",
                        "GOgoCyclone_11",
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        "Art Stenko"
                },
                {
                        accountInfos.get(13).get(0),
                        accountInfos.get(13).get(1),
                        accountInfos.get(13).get(2),
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        accountInfos.get(13).get(0),
                },
                {
                        accountInfos.get(14).get(0),
                        accountInfos.get(14).get(1),
                        accountInfos.get(14).get(2),
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        accountInfos.get(14).get(0),
                },
                {
                        accountInfos.get(15).get(0),
                        accountInfos.get(15).get(1),
                        accountInfos.get(15).get(2),
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        accountInfos.get(15).get(0),
                },

             /*   //14
                {
                        accountInfos.get(13).get(0),
                        accountInfos.get(13).get(1),
                        accountInfos.get(13).get(2),
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        "Demetrios Mikhaylov"
                },

                {
                        accountInfos.get(14).get(0),
                        accountInfos.get(14).get(1),
                        accountInfos.get(14).get(2),
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        "Nikita K."
                },    {
                        accountInfos.get(15).get(0),
                        accountInfos.get(15).get(1),
                        accountInfos.get(15).get(2),
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        "Nikita K."
                },*/




        };
    }
}
