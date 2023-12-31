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

import java.time.Duration;
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
    @Test(description = "add leads from search page", dataProvider = "dataProviderPeopleSearch")
    public void addLeads(String name, String clientName, String email, String password, String searchLink, String msg, String pickList, String leadCompany, String leadCompanyId){
        setupBrowser(true, name);
        Thread.sleep(randomResult*3);
        openLinkedInLoginPage();
        signInPage.signIn(randomResult, email, password);
        Selenide.open(searchLink);
        WebDriverRunner.getWebDriver().manage().window().maximize();
        String token = zohoCrmHelper.renewAccessToken();
        for (int i = 0; i < 10; i++) {
            Thread.sleep(randomResult);
            for (SelenideElement person:searchPeoplePage.PersonPages
            ) {
                Thread.sleep(200);
                String personRef = person.getAttribute("href");
                if (person.text().contains("LinkedIn Member")) continue;

                String[] personNamearr = person.find(By.cssSelector("span")).text().split("\\s");
                String personName = personNamearr[0] + " " + personNamearr[1];
                System.out.println(personName);
                Thread.sleep(randomResult);
                Selenide.executeJavaScript("window.scrollTo(2000, document.body.scrollHeight)");

            Selenide.executeJavaScript("window.open(\'" + personRef + "\')");
                Thread.sleep(randomResult*3);
            Selenide.switchTo().window(1);
                Thread.sleep(randomResult);
                closeMsgPopups();
                personPage.addLead(msg.replace("NAME", personNamearr[0]));
               String response = zohoCrmHelper.AddLeadToCRM(personName, token, pickList, personRef, "Attempted to Contact", leadCompany, leadCompanyId, name);
               if (response.contains("INVALID_TOKEN")) {
                   token = zohoCrmHelper.renewAccessToken();
                   zohoCrmHelper.AddLeadToCRM(personName, token, pickList, personRef, "Attempted to Contact", leadCompany, leadCompanyId, name);
               }
                Selenide.closeWindow();
                switchTo().window(0);
                //if (zohoCrmHelper.responseBody.contains("DUPLICATE_DATA")){break;}
                System.out.println("\n" + WebDriverRunner.getWebDriver().getCurrentUrl() );
            }
            Thread.sleep(randomResult);
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
                {       "Анастасия ",
                        clientName,
                        "vozniakanastasia52@gmail.com",
                        "zdHXF5bf",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22100907646%22%2C%22104853962%22%5D&origin=FACETED_SEARCH&page=70&sid=2zm&titleFreeText=CEO",
                        "Hi, there. I happened upon your account and was really impressed with your work. Our CEO will be in Stockholm between 19 and 24 of September. He was wondering if you would like to have a cup of coffee with me and talk about your business development and possible cooperation between our companies",
                        "Yurij",
                        "Sweden (LinkedIn)",
                        "421659000004662167"
                },
               {       "Маша ",
                        clientName,
                        "deynekamariawv@gmail.com",
                        "3N2wbnsw",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22100459316%22%5D&origin=FACETED_SEARCH&page=5&sid=HEI&titleFreeText=ceo",
                        "Hi. I came across your account and was impressed with your expertise. Would you mind having a quick chat about the Saudi Arabian market and the opportunities professional web development offers to businesses overall and your company in particular? ",
                        "Yurij",
                        "Saudi Arabia",
                        "421659000006238011"
                },
/*                {       "Михайло",
                        clientName,
                        "michael.salo1995@gmail.com",
                        "newman1996",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22100459316%22%5D&origin=FACETED_SEARCH&page=10&sid=!qL&titleFreeText=co-founder",
                        "Hi. I came across your account and was impressed with your expertise. Would you mind having a quick chat about the Saudi Arabian market and the opportunities professional web development offers to businesses overall and your company in particular? ",
                        "Yurij",
                        "Saudi Arabia",
                        "421659000006238011"
                },*/
                {       "Максим",
                        clientName,
                        "kotokmaksym@gmail.com",
                        "r4E3w2q1",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22103644278%22%5D&network=%5B%22O%22%5D&origin=FACETED_SEARCH&page=10&sid=kFh&titleFreeText=Dealer%20Principal",
                        "Hi, my name is Maks. I am VP of engineering at software company Wise Vision. We help dealership companies improve business metrics via modern IT solutions.\n" +
                                "Will be appreciated a lot for accepting this invite.",
                        "Yurij",
                        "Automotive Apollo",
                        "421659000005684017"
                },
                {       "Наталья",
                        clientName,
                        "natalia.marcoon@gmail.com ",
                        "asd321qq",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22100907646%22%2C%22104853962%22%5D&origin=FACETED_SEARCH&page=70&sid=1R7&titleFreeText=Owner",
                        "Hi, there. I happened upon your account and was really impressed with your work. Our CEO will be in Stockholm between 19 and 24 of September. He was wondering if you would like to have a cup of coffee with me and talk about your business development and possible cooperation between our companies",
                        "Valeriia",
                        "Sweden (LinkedIn)",
                        "421659000004662167"
                },
                {       "Александра",
                        clientName,
                        "alexandra.sternenko@gmail.com",
                        "asd321qq",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22100459316%22%5D&origin=FACETED_SEARCH&page=5&sid=Snt&titleFreeText=founder",
                        "Hi. I came across your account and was impressed with your expertise. Would you mind having a quick chat about the Saudi Arabian market and the opportunities professional web development offers to businesses overall and your company in particular? ",
                        "Valeriia",
                        "Saudi Arabia",
                        "421659000006238011"
                },
               {       "Роксолана ",
                        clientName,
                        "roksolanatrofim@gmail.com ",
                        "89fcmTT88V",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22100907646%22%2C%22104853962%22%5D&origin=FACETED_SEARCH&page=70&sid=Nw9&titleFreeText=founder",
                        "Hi, there. I happened upon your account and was really impressed with your work. Our CEO will be in Stockholm between 19 and 24 of September. He was wondering if you would like to have a cup of coffee with me and talk about your business development and possible cooperation between our companies",
                        "Alex",
                        "Sweden (LinkedIn)",
                        "421659000004662167"
                },
                {       "Марьян",
                        clientName,
                        "reshetunmaryanwv@gmail.com",
                        "rSbnGaRS",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22104853962%22%2C%22100907646%22%5D&origin=FACETED_SEARCH&page=70&sid=z8-&titleFreeText=cto",
                        "Hi, there. I happened upon your account and was really impressed with your work. Our CEO will be in Stockholm between 19 and 24 of September. He was wondering if you would like to have a cup of coffee with me and talk about your business development and possible cooperation between our companies",
                        "Alex",
                        "Sweden (LinkedIn)",
                        "421659000004662167"
                },
                {       "Настя ",
                        clientName,
                        "anastasiiakuntii@gmail.com",
                        "nastya4141",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22101452733%22%5D&industry=%5B%2296%22%2C%221594%22%2C%2211%22%5D&origin=FACETED_SEARCH&page=15&sid=KYk&titleFreeText=Founder",
                        "Hi. I came across your account and found that we have some common interests. Would you like to chat a little about the Australian market and some new tendencies and opportunities within it? ;)\n",
                        "Alex",
                        "Australia Outstaff",
                        "421659000006238006"
                },
/*                {       "Денис ",
                        clientName,
                        "basdenisphytontm@gmail.com",
                        "asd321qq",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22100459316%22%5D&origin=FACETED_SEARCH&page=5&sid=*b*&titleFreeText=Director%20of%20Human%20Resources",
                        "Hi. I came across your account and was impressed with your expertise. Would you mind having a quick chat about the Saudi Arabian market and the opportunities professional web development offers to businesses overall and your company in particular? ",
                        "Pavlo",
                        "Saudi Arabia",
                        "421659000006238011"
                },*/
/*                {       "Nikita ",
                        clientName,
                        "kni2012@ukr.net",
                        "33222200s",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22100907646%22%2C%22104853962%22%2C%2290010409%22%5D&industry=%5B%2296%22%2C%224%22%5D&origin=FACETED_SEARCH&page=10&sid=Lu9&titleFreeText=CTO",
                        "Hi, there. I happened upon your account and was really impressed with your work. I will be in Stockholm between 19 and 24 of September. I was wondering if you would like to have a cup of coffee with me and talk about your business development and possible cooperation between our companies :)",
                        "Valeriia",
                        "Munich",
                        "421659000006238021"
                },*/

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
                },*/
/*                {       "Test",
                        clientName,
                        "wisevision1985@gmail.com",
                        "33222200Shin",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22103644278%22%5D&network=%5B%22O%22%5D&origin=FACETED_SEARCH&page=100&sid=Ng9",
                        "Hi, I want expand my contact, thx for accepting",
                        "Pavlo",
                        "Test",
                        "421659000007355929"
                },*/
/*                {       "Test",
                        clientName,
                        "wisevision1986@gmail.com",
                        "33222200Shin",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22103644278%22%5D&network=%5B%22O%22%5D&origin=FACETED_SEARCH&page=90&sid=Ng9",
                        "Hi, I want expand my contact, thx for accepting",
                        "Pavlo",
                        "Test",
                        "421659000007355929"
                },*/

        };
    }
}
