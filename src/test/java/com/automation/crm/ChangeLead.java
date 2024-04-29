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
    String attemptedToContact = "421659000010541270";
    String attemptedToContact1 = "421659000001302365";


    @SneakyThrows
    @Test(description = "add leads from search page", dataProvider = "dataProviderPeopleAddToCRM")
    public void addLeadsToCRM(String name, String email, String password){
        setupBrowser(true, name);
        Thread.sleep(randomResult*3);
        openLinkedInLoginPage();
        signInPage.signIn(randomResult, email, password);
        Thread.sleep(10000);

        Selenide.open("https://www.linkedin.com/mynetwork/invite-connect/connections/");
        WebDriverRunner.getWebDriver().manage().window().maximize();
        String token = zohoCrmHelper.renewAccessToken();

        for (int i = 0; i < 4; i++) {
            Thread.sleep(randomResult);
            Selenide.executeJavaScript("window.scrollTo(2000, document.body.scrollHeight)");
            if ($x("//span[normalize-space()='Show more results']").exists()) {
                //if (!$x("//span[normalize-space()='Show more results']").exists())continue;
                $x("//span[normalize-space()='Show more results']").shouldBe(interactable).click();
                Thread.sleep(randomResult);
                Selenide.executeJavaScript("window.scrollTo(2000, document.body.scrollHeight)");
            }
        }
        ElementsCollection leads = $$x("//div[@class='mn-connection-card__details']/a");
/*        for (SelenideElement lead: leads) {
            System.out.println( lead.find(By.cssSelector(".mn-connection-card__name")).text() );
        }*/
        for (int i = 0; i < leads.size(); i++) {
            Thread.sleep(randomResult);
            SelenideElement person = leads.get(i);
                Thread.sleep(200);
                if (person.find(By.cssSelector(".mn-connection-card__name")).text().contains("LinkedIn Member")) continue;
                String[] personNamearr = person.find(By.cssSelector(".mn-connection-card__name")).text().split("\\s");
                String personName;
                if (personNamearr.length>1)  personName = personNamearr[0] + " " + personNamearr[1];
                else personName =  personNamearr[0];
                Thread.sleep(randomResult);

                String leadInfoResponseBody = zohoCrmHelper.getLeadInfoByFullName(token, personName);
                if (leadInfoResponseBody.isEmpty()){
                    //{
                    //  "name": "Unknown",
                    //  "id": "421659000016077007"
                    //}
                    zohoCrmHelper.AddLeadToCRM(personName, token, "Anastasia", person.getAttribute("href"),  "Attempted to Contact", "Unknown", "421659000016077007", name);
                    System.out.println("add to CRM: " + personName);
                }

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
                        changeStatus(leadId, token, Contacted);
                    else {continue;}
                }
                else continue;
            Thread.sleep(randomResult);
        }






    }

    public void changeStatus(String leadId, String token, String status)      {
        String changeLeadStatusResponse = zohoCrmHelper.changeLeadStatus(leadId, token, status);
        JSONObject changeLeadStatusResponseJson = new JSONObject(changeLeadStatusResponse);;
        System.out.println("code: " + changeLeadStatusResponseJson.getString("code") );
        System.out.println("\n" );
        if (changeLeadStatusResponseJson.getString("code").equals("RECORD_NOT_IN_PROCESS")) {
            System.out.println("Try direct change:\n" + zohoCrmHelper.changeLeadStatus(leadId, token) );
        };
    }


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
                {       "Gorichev Andrey",
                        "gorichev.andrey12311@outlook.it",
                        "33222200Shin",
                },
                {       "Barakhoyev Musa",
                        "barakhoyev.musa@outlook.it",
                        "33222200Shin",
                },
                {       "Demetrios Mikhaylov",
                        "demetrios.Mikhaylov@outlook.de",
                        "33222200Shin",
                },
                {
                        "Nikita K.",
                        "kni2012@ukr.net",
                        "33222200s",
                },

        };
    }
}

