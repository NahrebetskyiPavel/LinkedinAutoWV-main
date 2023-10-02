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
                        "Saudi Arabia",
                        "421659000006238011",
                        false
                },
                {       "Маша - Malmo Founder ",
                        clientName,
                        "deynekamariawv@gmail.com",
                        "3N2wbnsw",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22101759788%22%5D&origin=FACETED_SEARCH&page=100&sid=i2S&titleFreeText=Founder",
                        "Hi. I came across your account and was impressed with your expertise. Would you mind having a quick chat about the Saudi Arabian market and the opportunities professional web development offers to businesses overall and your company in particular? ",
                        "Yurij",
                        "Jordan",
                        "421659000008722001",
                        false
                },
                {       "Михайло - Stuttgart CTO",
                        clientName,
                        "michael.salo1995@gmail.com",
                        "newman1996",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22102473731%22%5D&origin=FACETED_SEARCH&page=18&sid=VuW&titleFreeText=CTO",
                        "Hi. I came across your account and was impressed with your expertise. Would you mind having a quick chat about the Saudi Arabian market and the opportunities professional web development offers to businesses overall and your company in particular? ",
                        "Yurij",
                        "Saudi Arabia",
                        "421659000006238011",
                        false
                },
                {       "Nikita - London CTO",
                        clientName,
                        "kni2012@ukr.net",
                        "33222200s",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%2290009496%22%2C%22102257491%22%5D&origin=FACETED_SEARCH&page=100&sid=.(w&titleFreeText=CTO",
                        "Hi. I found your account by chance and an idea of a beautiful collaboration emerged in my head. Do you have time for a quick chat to discuss the opportunities I would like to offer to your company? ;)",
                        "Valeriia",
                        "Ireland",
                        "421659000008722021",
                        true
                },
                {       "Наталья- Malmo CEO",
                        clientName,
                        "natalia.marcoon@gmail.com ",
                        "asd321qq",
                        "",
                        "",
                        "Valeriia",
                        "SouthAfricaFinance",
                        "421659000008722046",
                        false
                },
                {       "Денис - London CEO",
                        clientName,
                        "basdenisphytontm@gmail.com",
                        "asd321qq",
                        "",
                        "",
                        "Valeriia",
                        "Saudi Arabia",
                        "421659000006238011",
                        false
                },
                {       "Настя - Stuttgart CEO",
                        clientName,
                        "anastasiiakuntii@gmail.com",
                        "nastya4141",
                        "",
                        "",
                        "Alex",
                        "Australia Outstaff",
                        "421659000006238006",
                        false
                },
                {       "Роксолана - Copenhagen CEO",
                        clientName,
                        "roksolanatrofim@gmail.com ",
                        "89fcmTT88V",
                        "",
                        "",
                        "Alex",
                        "SouthAfricaFinance",
                        "421659000008722046",
                        false
                },
                {       "Марьян -  Copenhagen CTO",
                        clientName,
                        "reshetunmaryanwv@gmail.com",
                        "rSbnGaRS",
                        "",
                        "",
                        "Alex",
                        "SouthAfricaFinance",
                        "421659000008722046",
                        false
                },
                {       "Максим - Malmo co-founder",
                        clientName,
                        "kotokmaksym@gmail.com",
                        "r4E3w2q1",
                        "",
                        "",
                        "Yurij",
                        "Automotive Apollo",
                        "421659000005684017",
                        false
                },
                {       "Анастасия - Gothenburg CTO",
                        clientName,
                        "vozniakanastasia52@gmail.com",
                        "zdHXF5bf",
                        "",
                        "",
                        "Pavlo",
                        "Jordan",
                        "421659000008722001",
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
