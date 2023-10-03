package com.automation.linkedin;

import api.helpers.ZohoCrmHelper;
import com.automation.linkedin.pages.PersonPage;
import com.automation.linkedin.pages.login.SignInPage;
import com.automation.linkedin.pages.messaging.MessagingPage;
import com.automation.linkedin.pages.search.SearchPeoplePage;
import com.codeborne.selenide.*;
import lombok.SneakyThrows;
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
    public void addLeads(String name, String clientName, String email, String password, String searchLink, String msg, String pickList, String leadCompany, String leadCompanyId, boolean premium){
        int leadsRequestCount = 0;
        Thread.sleep(randomResult);
        System.out.println("-------------------------------------------------------\n" +
                "START: "+name+"\n" +
                "-------------------------------------------------------");
        setupBrowser(true, name);
        Thread.sleep(randomResult*3);
        openLinkedInLoginPage();
        signInPage.signIn(randomResult, email, password);
        Selenide.open(searchLink);
        Thread.sleep(randomResult);
        WebDriverRunner.getWebDriver().manage().window().maximize();
        String token = zohoCrmHelper.renewAccessToken();
        while (leadsRequestCount != 50){
            Thread.sleep(randomResult);
            for (SelenideElement person:searchPeoplePage.PersonPages
            ) {
                Thread.sleep(200);
                String personRef = person.getAttribute("href");
                if (person.text().contains("LinkedIn Member")) continue;

                String[] personNamearr = person.find(By.cssSelector("span")).text().split("\\s");
                String personName = personNamearr[0] + " " + personNamearr[1];
                Thread.sleep(randomResult);
                Selenide.executeJavaScript("window.scrollTo(2000, document.body.scrollHeight)");

            Selenide.executeJavaScript("window.open(\'" + personRef + "\')");
                Thread.sleep(randomResult*3);
            Selenide.switchTo().window(1);
                Thread.sleep(randomResult);
                closeMsgPopups();
            if (personPage.addLead(msg.replace("NAME", personNamearr[0]), premium) ){
                if (leadsRequestCount == 30) break;
                leadsRequestCount = leadsRequestCount + 1;
                if (leadsRequestCount == 49) System.out.println(WebDriverRunner.getWebDriver().getCurrentUrl()  );
                System.out.println("leadsRequestCount: " + leadsRequestCount);
                String response = zohoCrmHelper.AddLeadToCRM(personName, token, pickList, personRef, "Attempted to Contact", leadCompany, leadCompanyId, name);
                if (response.contains("INVALID_TOKEN")) {
                    token = zohoCrmHelper.renewAccessToken();
                    zohoCrmHelper.AddLeadToCRM(personName, token, pickList, personRef, "Attempted to Contact", leadCompany, leadCompanyId, name);
                }
                }

                Selenide.closeWindow();
                switchTo().window(0);
                //if (zohoCrmHelper.responseBody.contains("DUPLICATE_DATA")){break;}
            }
            Thread.sleep(randomResult);
            if (!searchPeoplePage.previousPageBtn.is(interactable)){
                System.out.println( WebDriverRunner.getWebDriver().getCurrentUrl() );
                System.out.println("=========== OUT OF SEARCH ===========");
                break;
            }
            searchPeoplePage.previousPageBtn.shouldBe(visible).click();
            Thread.sleep(randomResult);
        }
    }

    @DataProvider(name = "dataProviderPeopleSearch", parallel=true)
    public static Object[][] dataProviderPeopleSearch() {
        String clientName = "";
        String leadCompanyGamblingId ="421659000005125089";
        String leadCompanyAmsterdamId ="421659000005261283";
        String leadCompanyAustraliaId ="421659000005261273";
        String leadCompanyName ="Gambling LinkedIn";
        return new Object[][]{
                {       "Александра - Malmo CTO",
                        clientName,
                        "alexandra.sternenko@gmail.com",
                        "asd321qq",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22101759788%22%5D&origin=FACETED_SEARCH&page=21&sid=uy(&titleFreeText=CTO",
                        "Hi. I came across your account and was impressed with your expertise. Would you mind having a quick chat about the Saudi Arabian market and the opportunities professional web development offers to businesses overall and your company in particular? ",
                        "Yurij",
                        "Malmo",
                        "421659000009084010",
                        false
                },
                {       "Маша - Malmo Founder ",
                        clientName,
                        "deynekamariawv@gmail.com",
                        "3N2wbnsw",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22101759788%22%5D&origin=FACETED_SEARCH&page=100&sid=i2S&titleFreeText=Founder",
                        "Hi. I came across your account and was impressed with your expertise. Would you mind having a quick chat about the Saudi Arabian market and the opportunities professional web development offers to businesses overall and your company in particular? ",
                        "Yurij",
                        "Malmo",
                        "421659000009084010",
                        false
                },
                {       "Михайло - Stuttgart CTO",
                        clientName,
                        "michael.salo1995@gmail.com",
                        "newman1996",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22102473731%22%5D&origin=FACETED_SEARCH&page=18&sid=VuW&titleFreeText=CTO",
                        "Hi. I came across your account and was impressed with your expertise. Would you mind having a quick chat about the Saudi Arabian market and the opportunities professional web development offers to businesses overall and your company in particular? ",
                        "Yurij",
                        "Stuttgart",
                        "421659000009084020",
                        false
                },
                {       "Nikita - London CTO",
                        clientName,
                        "kni2012@ukr.net",
                        "33222200s",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%2290009496%22%2C%22102257491%22%5D&origin=FACETED_SEARCH&page=100&sid=.(w&titleFreeText=CTO",
                        "Hi. I found your account by chance and an idea of a beautiful collaboration emerged in my head. Do you have time for a quick chat to discuss the opportunities I would like to offer to your company? ;)",
                        "Valeriia",
                        "London",
                        "421659000009084025",
                        true
                },
                {       "Наталья- Malmo CEO",
                        clientName,
                        "natalia.marcoon@gmail.com ",
                        "asd321qq",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22101759788%22%5D&origin=FACETED_SEARCH&page=100&sid=KvP&titleFreeText=CEO",
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        "Valeriia",
                        "Malmo",
                        "421659000009084010",
                        false
                },
                {       "Денис - London CEO",
                        clientName,
                        "basdenisphytontm@gmail.com",
                        "asd321qq",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%2290009496%22%2C%22102257491%22%5D&origin=FACETED_SEARCH&page=100&sid=7!!&titleFreeText=CEO",
                        "Hi. I came across your account and was impressed with your expertise. Would you mind having a quick chat about the Saudi Arabian market and the opportunities professional web development offers to businesses overall and your company in particular? ",
                        "Valeriia",
                        "London",
                        "421659000009084025",
                        false
                },
                {       "Настя - Stuttgart CEO",
                        clientName,
                        "anastasiiakuntii@gmail.com",
                        "nastya4141",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22102473731%22%5D&origin=FACETED_SEARCH&page=100&sid=(k5&titleFreeText=CEO",
                        "Hi. I came across your account and found that we have some common interests. Would you like to chat a little about the Australian market and some new tendencies and opportunities within it? ;)",
                        "Alex",
                        "Stuttgart",
                        "421659000009084020",
                        false
                },
                {       "Роксолана - Copenhagen CEO",
                        clientName,
                        "roksolanatrofim@gmail.com ",
                        "89fcmTT88V",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%2290009617%22%5D&origin=FACETED_SEARCH&page=100&sid=l(c&titleFreeText=CEO",
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)",
                        "Alex",
                        "Copenhagen",
                        "421659000009084015",
                        false
                },
                {       "Марьян -  Copenhagen CTO",
                        clientName,
                        "reshetunmaryanwv@gmail.com",
                        "rSbnGaRS",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%2290009617%22%5D&origin=FACETED_SEARCH&page=100&sid=jXF&titleFreeText=CTO",
                        "Hi. I happened upon your account and was really impressed with your work. I was wondering if you would like to chat with me sometime about your business achievements. I think I have some ideas on making your work more efficient, cost-effective and enjoyable ;)",
                        "Alex",
                        "Copenhagen",
                        "421659000009084015",
                        false
                },
                {       "Максим - Malmo co-founder",
                        clientName,
                        "kotokmaksym@gmail.com",
                        "r4E3w2q1",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22101759788%22%5D&origin=FACETED_SEARCH&page=100&sid=lLC&titleFreeText=co-founder",
                        "Hi. I happened upon your account and was really impressed with your work. I was wondering if you would like to chat with me sometime about your business achievements. I think I have some ideas on making your work more efficient, cost-effective and enjoyable ;)",
                        "Yurij",
                        "Malmo",
                        "421659000009084010",
                        false
                },
                {       "Анастасия - Gothenburg CTO",
                        clientName,
                        "vozniakanastasia52@gmail.com",
                        "zdHXF5bf",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%2290010413%22%2C%22104114836%22%5D&origin=FACETED_SEARCH&page=40&sid=5ca&titleFreeText=CTO",
                        "Hi. I stumbled upon your account and noticed that you have expertise in my area of interest. I was wondering if you would mind having a chat about the real estate market in the US, its challenges and opportunities ;)\n",
                        "Pavlo",
                        "Gothenburg",
                        "421659000009084030",
                        false
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
