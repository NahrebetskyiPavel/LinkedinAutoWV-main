package com.automation.linkedin;

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
    Random random = new Random();
    int low = 2000;
    int high = 5000;
    int randomResult = random.nextInt(high-low) + low;

    @SneakyThrows
    @Test(description = "add leads from search page", dataProvider = "dataProviderPeopleSearch", alwaysRun = true )
    public void addLeads(String name, String email, String password,  String msg, String linkedinperson){
        int leadsRequestCount = 0;
        Thread.sleep(randomResult);
        System.out.println("-------------------------------------------------------\n" +
                "START: "+name+"\n" +
                "-------------------------------------------------------");
        setupBrowser(true, name);
        Thread.sleep(randomResult*3);
        openLinkedInLoginPage();
        signInPage.signIn(randomResult, email, password);
        Thread.sleep(1000*20);
        Selenide.open("https://www.linkedin.com/");
        Thread.sleep(randomResult);
        WebDriverRunner.getWebDriver().manage().window().maximize();
        String token = zohoCrmHelper.renewAccessToken();
        String data = zohoCrmHelper.getLeadList( token, 1,  "Waiting",  linkedinperson,  "Anastasia");
       // System.out.println(new JSONObject( data ).getJSONArray("data").length());
        //System.out.println(new JSONObject( data ).getJSONArray("data").getJSONObject(50).getString("Website"));

        for (int i = 0; i < new JSONObject( data ).getJSONArray("data").length(); i++)
        {
            Thread.sleep(randomResult);

                Thread.sleep(200);
                String personRef = new JSONObject( data ).getJSONArray("data").getJSONObject(i).getString("Website");
                Selenide.open(personRef);
                Thread.sleep(randomResult);
                String id = new JSONObject( data ).getJSONArray("data").getJSONObject(50).getString("id");
                personPage.addToFriends(msg,false);
                zohoCrmHelper.changeLeadStatus(id, token,"421659000001302365");
        }
    }

    @DataProvider(name = "dataProviderPeopleSearch", parallel=true)
    public static Object[][] dataProviderPeopleSearch() {

        return new Object[][]{
                {       "Настя ",
                        "anastasiiakuntii@gmail.com",
                        "33222200Shin",
                        "Hi. I came across your account and found that we have some common interests. Would you like to chat a little about the Australian market and some new tendencies and opportunities within it? ;)",
                        "Anastasiia K."
                },
                {       "Pasha ",
                        "pavelnagrebetski@gmail.com",
                        "Asd321qq",
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        "Pavlo"
                },
                {       "Александра - Gothenburg ",
                        "alexandra.sternenko@gmail.com",
                        "asd321qq",
                        "Hello there. I stumbled across your account accidentally and was impressed with your expertise. Would you mind accepting this invite so we could talk some more?",
                        "Yurij"
                },
                {       "Маша - Gothenburg Founder ",
                        "deynekamariawv@gmail.com",
                        "qwertqaz1234",
                        "Hello there. I stumbled across your account accidentally and was impressed with your expertise. Would you mind accepting this invite so we could talk some more?",
                        "Yurij"
                },
                {       "Михайло - Gothenburg  CFO",
                        "michael.salo1995@gmail.com",
                        "newman1996",
                        "Hello there. I happened upon your account accidentally and was impressed with your expertise. How about accepting this invite so that we can talk some more?",
                        "Yurij"
                },
/*                {       "Nikita - Stockholm board of directors",
                        clientName,
                        "kni2012@ukr.net",
                        "33222200s",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22100907646%22%5D&network=%5B%22O%22%5D&origin=FACETED_SEARCH&page=50&sid=E7r&titleFreeText=board%20of%20directors",
                        "Hello there. I stumbled across your account by chance and was impressed with your expertise. Would you mind accepting this invite to have an opportunity to talk in the future?",
                        "Valeriia",
                        "Stockholm November",
                        "421659000009767013",
                        true
                },*/
                {       "Наталья- Gothenburg CEO",
                        "natalia.marcoon@gmail.com ",
                        "33222200Shin",
                        "Hello there. I stumbled across your account accidentally and was impressed with your expertise. Would you mind accepting this invite so we could talk some more?",
                        "Valeriia"
                },
                {       "Денис - Stockholm CEO",
                        "basdenisphytontm@gmail.com",
                        "33222200Shin_",
                        "Hello there. I stumbled across your account by chance and was impressed with your expertise. Would you mind accepting this invite to have an opportunity to talk in the future?",
                        "Valeriia"
                },

                {       "Роксолана - Stockholm CFO",
                        "roksolanatrofim@gmail.com ",
                        "89fcmTT88V",
                        "Hello there. I stumbled across your account accidentally and was impressed with your expertise. Would you mind accepting this invite so we could talk some more?",
                        "Alex"
                },
                {       "Марьян -  Stockholm CTO",
                        "reshetunmaryanwv@gmail.com",
                        "rSbnGaRS",
                        "Hello there. I stumbled across your account accidentally and was impressed with your expertise. Would you mind accepting this invite so we could talk some more?",
                        "Alex"
                },
/*                {       "Максим - Stockholm co-founder",
                        clientName,
                        "kotokmaksym@gmail.com",
                        "r4E3w2q1",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22100907646%22%5D&network=%5B%22O%22%5D&origin=FACETED_SEARCH&page=80&sid=SEz&titleFreeText=co-founder",
                        "Hello there. I stumbled across your account accidentally and was impressed with your expertise. Would you mind accepting this invite so we could talk some more?",
                        "Yurij",
                        "Stockholm November",
                        "421659000009767013",
                        false
                },*/
                {       "Анастасия - Stockholm owner",
                        "vozniakanastasia52@gmail.com",
                        "33222200Shin",
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        "Pavlo"
                }

/* ==================================================================================================================================================================== */
/*                {       "Софія",
                        clientName,
                        "sofi.podlesna@gmail.com",
                        "7riuwotu949",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22100459316%22%5D&origin=FACETED_SEARCH&page=85&sid=B%2Cg&titleFreeText=president",
                        "Hi, NAME. I came across your account and was impressed with your expertise. Would you mind having a quick chat about the Saudi Arabian market and the opportunities professional web development offers to businesses overall and your company in particular? \n",
                        "Alex",
                        "Saudi Arabia",
                        "421659000006238011"
                },
                {       "Test",
                        clientName,
                        "wisevision1985@gmail.com",
                        "33222200Shin",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22103644278%22%5D&network=%5B%22O%22%5D&origin=FACETED_SEARCH&page=100&sid=Ng9",
                        "Hi, I want expand my contact, thx for accepting",
                        "Pavlo",
                        "Test",
                        "421659000007355929"
                },
                {       "Test",
                        clientName,
                        "wisevision1986@gmail.com",
                        "33222200Shin",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22103644278%22%5D&network=%5B%22O%22%5D&origin=FACETED_SEARCH&page=90&sid=Ng9",
                        "Hi, I want expand my contact, thx for accepting",
                        "Pavlo",
                        "Test",
                        "421659000007355929"
                },
<==============================================================================================================================================================>



               */
        };
    }
}
