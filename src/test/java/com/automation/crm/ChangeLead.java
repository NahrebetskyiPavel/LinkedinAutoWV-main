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
    public void addLeadsToCRM(String profileId, String email, String password, String cookie){
        int numberOfProfiles = 50;
        String token = zohoCrmHelper.renewAccessToken();
        String connectionsListTask = zohoCrmHelper.getConnectionsList("andrei-gorbunkov-a34b4a2aa", "andreiGorbunkov@outlook.de", "33222200Shin","AQEDAUqQcUgAJO_LAAABjRGv3SIAAAGOmMFYqE0ArnVnmtRxkfVOu6vUysML6PHk2oENpaWG43H6H_RZGisvCqLeBj7azZTBPn0_vjE7zPme8YjHw6GyXwEOBkQvUkqNijYnP9HnwG2A5y5wR9E-hY_q", "Recently added", numberOfProfiles);
        String connectionsListTaskId = String.valueOf(new JSONObject( connectionsListTask ).get("taskId"));
        Thread.sleep(1000*60);
        String connectionsList = zohoCrmHelper.getTaskInfo(connectionsListTaskId, "andrei-gorbunkov-a34b4a2aa");
        new StatusChecker().waitForStatus("finished", String.valueOf(new JSONObject( connectionsList ).get("status")) );

        for (int i = 0; i < numberOfProfiles; i++)
        {
            String data = String.valueOf(new JSONObject( connectionsList ).getJSONArray("results").get(i));
            String personName = String.valueOf(new JSONObject( data ).getString("fullName"));

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

                }
            Thread.sleep(randomResult);
        }
    }


//String profileId, String email, String password, String cookie
    @DataProvider(name = "dataProviderPeopleAddToCRM", parallel=false)
    public static Object[][] dataProviderPeopleAddToCRM() {
        return new Object[][]{
                {       "Александра ",
                        "alexandra.sternenko@gmail.com",
                        "asd321qq",
                },
                {       "Маша  ",
                        "deynekamariawv@gmail.com",
                        "3N2wbnsw",
                },
                {       "Михайло ",
                        "michael.salo1995@gmail.com",
                        "newman1996",
                },
                {       "Наталья",
                        "natalia.marcoon@gmail.com ",
                        "33222200Shin",
                },
                {       "Денис ",
                        "basdenisphytontm@gmail.com",
                        "33222200Shin",
                },
                {       "Настя",
                        "anastasiiakuntii@gmail.com",
                        "33222200Shin",
                },
                {       "Роксолана ",
                        "roksolanatrofim@gmail.com ",
                        "89fcmTT88V",
                },
                {       "Марьян ",
                        "reshetunmaryanwv@gmail.com",
                        "33222200Shin",
                },
                {       "Анастасия ",
                        "vozniakanastasia52@gmail.com",
                        "33222200Shin",
                },
                {       "Artem Pevchenko",
                        "artemter223@outlook.com",
                        "33222200Shin",
                },
//11
                {       "Roman Gulyaev",
                        "gulyaev.roman@outlook.com",
                        "33222200Shin",
                },
//12
                {       "Dmytro Andreev",
                        "andreev.dima@outlook.de",
                        "33222200Shin",
                },
//13
                {       "Oleg Konorov",
                        "oleg.konorov@outlook.com",
                        "33222200Shin",
                },


//18
                {       "Dymitr Tolmach",
                        "dymitr.tolmach1012@outlook.com",
                        "33222200Shin",
                },
//19
                {       "Oleg Valter",
                        "ovalter@outlook.co.nz",
                        "Shmee2023",
                },

//20
                {       "Dmitriy Semiletov",
                        "semi.dima@outlook.com",
                        "33222200Shin",
                },
                {       "Oleg Konorov",
                        "oleg.konorov@outlook.com",
                        "33222200Shin",
                },
                {       "Pavel  Nagrebetski",
                        "pavelnagrebetski@gmail.com",
                        "Asd321qq",
                },
                {       "Maksim Bakh",
                        "Bekhmaksim@outlook.com",
                        "33222200Shin",
                },

        };
    }
}

