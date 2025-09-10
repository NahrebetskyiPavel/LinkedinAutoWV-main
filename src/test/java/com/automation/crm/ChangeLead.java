package com.automation.crm;

import api.helpers.ZohoCrmHelper;
import com.automation.linkedin.pages.login.SignInPage;
import com.automation.linkedin.pages.search.SearchPeoplePage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.StatusChecker;

import java.util.Random;

import static com.automation.linkedin.Base.openLinkedInLoginPage;
import static com.automation.linkedin.Base.setupBrowser;
import static com.codeborne.selenide.Condition.interactable;
import static com.codeborne.selenide.Selenide.*;

public class ChangeLead {
    ZohoCrmHelper zohoCrmHelper = new ZohoCrmHelper();
    SignInPage signInPage = new SignInPage();
    SearchPeoplePage searchPeoplePage = new SearchPeoplePage();
    Random random = new Random();
    int low = 2000;
    int high = 5000;
    int randomResult = random.nextInt(high-low) + low;
    private static final String Contacted  = "421659000001302293";

    @SneakyThrows
    @Test(description = "add leads from search page", dataProvider = "dataProviderPeopleAddToCRM")
    public void addLeadsToCRM(String profileId, String email, String password, String cookie, String linkedinperson){
        int numberOfProfiles = 200;
        String token = zohoCrmHelper.renewAccessToken();
        System.out.println("Start");
        System.out.println("profileId " + profileId);
        System.out.println("email " + email);
        System.out.println("password " + password);
        System.out.println("cookie " + cookie);
        System.out.println("linkedinperson " + linkedinperson);
        String connectionsListTask = zohoCrmHelper.getConnectionsList(profileId, email, password, cookie, "Recently added", numberOfProfiles);
        Thread.sleep(1000*60);
        System.out.println("connectionsListTask: " + connectionsListTask);
//        System.out.println(connectionsListTask);
        String connectionsListTaskId = String.valueOf(new JSONObject( connectionsListTask ).get("taskId"));
        System.out.println(connectionsListTaskId);
        Thread.sleep(1000*60);
        String connectionsList = zohoCrmHelper.getTaskInfo(connectionsListTaskId, profileId);
        System.out.println(connectionsList);
while (true){
    if (String.valueOf(new JSONObject( connectionsList ).get("status")).contains("finished")) break;
    if (String.valueOf(new JSONObject( connectionsList ).get("status")).contains("failed")) break;
    //connectionsList = zohoCrmHelper.getTaskInfo(connectionsListTaskId, profileId);
    Thread.sleep(20*1000);
}

        new StatusChecker().waitForStatus("finished", String.valueOf(new JSONObject( connectionsList ).get("status")), 30000);

        for (int i = 0; i < numberOfProfiles; i++)
        {
            String data;
            try {
                data = String.valueOf(new JSONObject( connectionsList ).getJSONArray("results").get(i));
            } catch (Exception e){
                if ((e +"").contains("JSONArray") &&  (e +"").contains("not found") );
                System.out.println("JSONArray" + i +"not found");
                break;
            }
            String personName = String.valueOf(new JSONObject( data ).getString("fullName"));
            //String personRef = String.valueOf(new JSONObject( data ).getString("personRef"));
            System.out.println(personName);
            Thread.sleep(randomResult);

                String leadInfoResponseBody = zohoCrmHelper.getLeadInfoByFullName(token, personName);
                System.out.println(leadInfoResponseBody);
                if (leadInfoResponseBody.contains("INVALID_TOKEN")) {
                    token = zohoCrmHelper.renewAccessToken();
                    leadInfoResponseBody = zohoCrmHelper.getLeadInfoByFullName(token, personName);
                }
                if (leadInfoResponseBody.length() > 0 && leadInfoResponseBody.contains("data")) {
                    JSONObject responseBodyJsonObjectLeadInfo = new JSONObject(leadInfoResponseBody);
                    String leadId = responseBodyJsonObjectLeadInfo.getJSONArray("data").getJSONObject(0).getString("id");
                    System.out.println(leadId);
                    System.out.println(personName);
                    if (responseBodyJsonObjectLeadInfo.getJSONArray("data").getJSONObject(0).getString("Lead_Status").equals("Attempted to Contact"))
                    {
                        String changeLeadStatusResponse = zohoCrmHelper.changeLeadStatus(leadId, token, Contacted);
                    JSONObject changeLeadStatusResponseJson = new JSONObject(changeLeadStatusResponse);;
                    System.out.println("code: " + changeLeadStatusResponseJson.getString("code") );
                    System.out.println("\n" );
                    if (changeLeadStatusResponseJson.getString("code").equals("RECORD_NOT_IN_PROCESS")) {
                        System.out.println("Try direct change:\n" + zohoCrmHelper.changeLeadStatus(leadId, token) );
                    };
                    }
//else {
//                       // String Last_Name, String token, String pickList, String LinkedInLink, String leadStatus, String leadCompany, String leadCompanyId, String accountname
//                        String response = zohoCrmHelper.AddLeadToCRM(personName, token, pickList, personRef, "Attempted to Contact", leadCompany, leadCompanyId, linkedinperson);
//                        if (response.contains("INVALID_TOKEN")) {
//                            token = zohoCrmHelper.renewAccessToken();
//                        }
//                    }
                }
            Thread.sleep(randomResult);
        }
    }


//String profileId, String email, String password, String cookie
    @DataProvider(name = "dataProviderPeopleAddToCRM", parallel=false)
    public static Object[][] dataProviderPeopleAddToCRM() {
        return new Object[][]{

                {       "Anastasiia-Vozniak",
                        "vozniakanastasia52@gmail.com",
                        "asd2424qq",
                        "",
                        "Anastasiia Vozniak"
                },
                {       "Art-Stenko",
                        "artstenko@gmail.com",
                        "GOgoCyclone_11",
                        "",
                        "Art Stenko"
                },
                {       "lina-Kompanets",
                        "ekompanets02@gmail.com",
                        "35ulurev",
                        "",
                        "lina Kompanets"
                },
                {       "Maria-Deyneka",
                        "deynekamariawv@gmail.com",
                        "3N2wbnsw",
                        "",
                        "Maria Deyneka"
                },
                {       "Marian-Reshetun",
                        "reshetunmaryanwv@gmail.com",
                        "33222200Shin",
                        "",
                        "Marian Reshetun"
                },
                {       "Anastasiia-Kuntii",
                        "anastasiiakuntii@gmail.com",
                        "33222200Shin",
                        "",
                        "Anastasiia Kuntii"
                },
                {       "Natalia-Marcun",
                        "natalia.marcoon@gmail.com",
                        "asd321qq",
                        "",
                        "Natalia Marcun"
                },
                {       "Aleksandra-Sternenko",
                        "alexandra.sternenko@gmail.com",
                        "asd321qq",
                        "",
                        "Aleksandra Sternenko"
                },

                {       "Matthew-Martinez",
                        "mMartiz11@outlook.it",
                        "metmar11mmjy",
                        "",
                        "Matthew Martinez"
                },
                {       "Oksana-Dovhan",
                        "wisevision.office@gmail.com",
                        "33222200Shin",
                        "",
                        "Oksana Dovhan"
                },
                {       "Danylo-Lytvyn",
                        "wisevision.beast@gmail.com",
                        "1171534Oli35Wisew",
                        "",
                        "Danylo Lytvyn"
                }/*,
                {       "Nikita-K",
                        "kni2012@ukr.net",
                        "33222200s",
                        ""
                }*/

        };
    }
}

