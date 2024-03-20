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
    String token = zohoCrmHelper.renewAccessToken();

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
        int totalLeadsAddedCount = 0;

        if (data.isEmpty()) {
            System.out.println("Skip" + linkedinperson);
            wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","Skip" + linkedinperson + "because data isEmpty");

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
            System.out.println("personRe: " + personRef);
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
                wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","Skip" + linkedinperson + "Out of requests");

                WebDriverRunner.getWebDriver().quit();
                break;
            }
            leadsAddedCount = leadsRequestCount++;
            totalLeadsAddedCount = totalLeadsAddedCount + leadsAddedCount;
            System.out.println("Leads added from " + name + "account = " + leadsAddedCount);
            wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","Leads added from " + name + "account = " + leadsAddedCount);
            if (leadsAddedCount==25) {

                wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","Finish \n"  + "account = " + leadsAddedCount + "leadsAdded = " + leadsAddedCount);
                wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","TOTAL = " + totalLeadsAddedCount );

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
            if (leadsAddedCount==25) {
                wiseVisionApiHelper.SendMsgToTelegram("5990565707", "6895594171:AAGlEWr1ogP5Kkd4q5BumdKG6_nCRVSbMg0","TOTAL = " + totalLeadsAddedCount );

                break;
            };
        }
    }

    @DataProvider(name = "dataProviderPeopleSearch", parallel=true)
    public static Object[][] dataProviderPeopleSearch() {

        return new Object[][]{
                //1
                {       "Aleksandra Sternenko",
                        "alexandra.sternenko@gmail.com",
                        "asd321qq",
                        "Hello there. I stumbled across your account accidentally and was impressed with your expertise. Would you mind accepting this invite so we could talk some more?",
                        "Aleksandra Sternenko"
                },
//6
                {       "Natalia Marcun",
                        "natalia.marcoon@gmail.com ",
                        "33222200Shin",
                        "Hello there. I stumbled across your account accidentally and was impressed with your expertise. Would you mind accepting this invite so we could talk some more?",
                        "Natalia Marcun"
                },
//9
                {       "Artem Pevchenko",
                        "artemter223@outlook.com",
                        "33222200Shin",
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        "Artem Pevchenko"
                },
                {       "Dmytro Andreev",
                        "andreev.dima@outlook.de",
                        "33222200Shin",
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        "Dmytro Andreev"
                },
                {       "Dymitr Tolmach",
                        "dymitr.tolmach1012@outlook.com",
                        "33222200Shin",
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        "Dymitr Tolmach"
                },
                {       "Fedotov Alexey",
                        "fedotov.alexey@outlook.de",
                        "33222200Shin",
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        "Fedotov Alexey"
                },
                {       "Demetrios Mikhaylov",
                        "demetrios.Mikhaylov@outlook.de",
                        "33222200Shin",
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        "Demetrios Mikhaylov"
                },
                {       "Anastasiia Vozniak",
                        "vozniakanastasia52@gmail.com",
                        "33222200Shin",
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        "Anastasiia Vozniak"
                },
                {       "Roksolana Trofimchuk",
                        "roksolanatrofim@gmail.com ",
                        "89fcmTT88V",
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        "Roksolana Trofimchuk"
                },
                {       "Roman Gulyaev",
                        "gulyaev.roman@outlook.com",
                        "33222200Shin",
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        "Roman Gulyaev"
                },
                {       "Anastasiia Kuntii",
                        "anastasiiakuntii@gmail.com",
                        "33222200Shin",
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        "Anastasiia Kuntii"
                },
                {       "Marian Reshetun",
                        "reshetunmaryanwv@gmail.com",
                        "33222200Shin",
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        "Marian Reshetun"
                },
                {       "Alex Borzov",
                        "borzov.alex@outlook.de",
                        "33222200Shin",
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        "Alex Borzov"
                },
                {       "Barakhoyev Musa",
                        "barakhoyev.musa@outlook.it",
                        "33222200Shin",
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        "Barakhoyev Musa"
                },



        };
    }
}
