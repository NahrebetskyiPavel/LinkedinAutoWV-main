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

import static com.codeborne.selenide.Condition.interactable;
import static com.codeborne.selenide.Condition.visible;
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
        openLinkedInLoginPage();
        signInPage.signIn(randomResult*60, email, password);
        Selenide.open(searchLink);
        WebDriverRunner.getWebDriver().manage().window().maximize();
        String token = zohoCrmHelper.renewAccessToken();
        for (int i = 0; i < 5; i++) {
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
                personPage.moreBtn.shouldBe(interactable, Duration.ofSeconds(15));
                personPage.moreBtn.click();
                personPage.addLead(msg.replace("NAME", personNamearr[0]));
                if (personPage.limitAlertHeader.isDisplayed()){
                    System.out.println("\n========================================================================\n" +"Out of requests"+ "\n========================================================================\n");
                    WebDriverRunner.getWebDriver().quit(); }
                zohoCrmHelper.AddLeadToCRM(personName, token, pickList, personRef, "Attempted to Contact", leadCompany, leadCompanyId, name);
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
                {       "Анастасия - Amsterdam Head of",
                        clientName,
                        "vozniakanastasia52@gmail.com",
                        "zdHXF5bf",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22102011674%22%5D&network=%5B%22O%22%5D&origin=FACETED_SEARCH&page=15&sid=*Qg&titleFreeText=Head%20of",
                        "Hey NAME! I stumbled upon your profile and noticed that you have extensive knowledge in the same business field as me. I was wondering if you'd mind answering a few questions about the Amsterdam market :)",
                        "Yurij",
                        leadCompanyName,
                        leadCompanyGamblingId
                },
                //+ не трогать Юра
/*               {       "Маша - Australia CEO",
                        clientName,
                        "deynekamariawv@gmail.com",
                        "3N2wbnsw",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22101452733%22%5D&origin=FACETED_SEARCH&page=33&sid=U~l&titleFreeText=CEO",
                        "Hello! \n I've reached out to invite you to explore our services for your company. My team is skilled in web and app development and can provide tailored solutions to suit your needs. Let's chat and see how we can help!",
                        "Yurij",
                        "Australia Linkedin",
                        leadCompanyAustraliaId
                },*/
                {       "Михайло - USA",
                        clientName,
                        "michael.salo1995@gmail.com",
                        "newman1996",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22101452733%22%5D&network=%5B%22O%22%5D&origin=FACETED_SEARCH&page=15&sid=H84&titleFreeText=founder",
                        "Hello NAME!  I see that we operate in same business domain. Will be appreciated a lot if you accept my invite to scale the network :)",
                        "Yurij",
                        "Australia Linkedin",
                        leadCompanyAustraliaId
                },
/*                {       "Максим- Australia Founder",
                        clientName,
                        "kotokmaksym@gmail.com",
                        "r4E3w2q1",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22101452733%22%5D&network=%5B%22O%22%5D&origin=FACETED_SEARCH&page=60&sid=H84&titleFreeText=founder",
                        "Hello! \n I've reached out to invite you to explore our services for your company. My team is skilled in web and app development and can provide tailored solutions to suit your needs. Let's chat and see how we can help!",
                        "Yurij",
                        "Australia Linkedin",
                        leadCompanyAustraliaId
                },*/
/*                {       "Наталья- Amsterdam founder",
                        clientName,
                        "natalia.marcoon@gmail.com ",
                        "asd321qq",
                        "https://www.linkedin.com/search/results/people/?eventAttending=%5B%227067465124453707776%22%5D&origin=EVENT_PAGE_CANNED_SEARCH&page=20&sid=gsp",
                        "Hey there! I stumbled upon your profile and noticed that you have extensive knowledge in the same business field as me. I was wondering if you'd mind answering a few questions about the Amsterdam market :)",
                        "Valeriia",
                        "Amsterdam LinkedIn",
                        leadCompanyAmsterdamId
                },*/
                {       "Александра - Amsterdam co-founder",
                        clientName,
                        "alexandra.sternenko@gmail.com",
                        "asd321qq",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22102011674%22%5D&network=%5B%22O%22%5D&origin=FACETED_SEARCH&page=5&sid=L9V&titleFreeText=co-founder",
                        "Hey NAME! I stumbled upon your profile and noticed that you have extensive knowledge in the same business field as me. I was wondering if you'd mind answering a few questions about the Amsterdam market :)",
                        "Valeriia",
                        "Amsterdam LinkedIn",
                        leadCompanyAmsterdamId
                },
/*                {       "Софія- Cyprus",
                        clientName,
                        "sofi.podlesna@gmail.com",
                        "7riuwotu949",
                        "",
                        "Hello NAME!  I see that we operate in same business domain. Will be appreciated a lot if you accept my invite to scale the network :)",
                        "Alex",
                        leadCompanyName,
                        leadCompanyGamblingId
                },*/
/*                {       "Роксолана- CEO gamba Australia Austria",
                        clientName,
                        "roksolanatrofim@gmail.com ",
                        "89fcmTT88V",
                        "",
                        "Hello!  I see that we operate in same business domain. Will be appreciated a lot if you accept my invite to scale the network :)",
                        "Alex",
                        leadCompanyName,
                        leadCompanyGamblingId
                },*/
                {       "Марьян- Amsterdam CEO",
                        clientName,
                        "reshetunmaryanwv@gmail.com",
                        "rSbnGaRS",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%2290010383%22%5D&network=%5B%22O%22%5D&origin=FACETED_SEARCH&page=15&sid=PbD&titleFreeText=CEO",
                        "Hey NAME! I stumbled upon your profile and noticed that you have extensive knowledge in the same business field as me. I was wondering if you'd mind answering a few questions about the Amsterdam market :)",
                        "Alex",
                        "Amsterdam LinkedIn",
                        leadCompanyAmsterdamId
                },
                {       "Настя Amsterdam Director",
                        clientName,
                        "anastasiiakuntii@gmail.com",
                        "nastya4141",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%2290010383%22%2C%22102011674%22%5D&network=%5B%22O%22%5D&origin=FACETED_SEARCH&page=15&sid=Ph5&titleFreeText=director",
                        "Hey NAME! I stumbled upon your profile and noticed that you have extensive knowledge in the same business field as me. I was wondering if you'd mind answering a few questions about the Amsterdam market :)",
                        "Alex",
                        "Amsterdam LinkedIn",
                        leadCompanyAmsterdamId
                },
                {       "Денис Australia CTO",
                        clientName,
                        "basdenisphytontm@gmail.com",
                        "asd321qq",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22101452733%22%5D&network=%5B%22O%22%5D&origin=FACETED_SEARCH&page=15&sid=r6h&titleFreeText=COO",
                        "Hey NAME! I stumbled upon your profile and noticed that you have extensive knowledge in the same business field as me. I was wondering if you'd mind answering a few questions about the Australian market :)",
                        "Pavlo",
                        "Australia Linkedin",
                        leadCompanyAustraliaId
                },
/*                {       "Nikita Australia",
                        clientName,
                        "kni2012@ukr.net",
                        "33222200s",
                        "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22101452733%22%5D&network=%5B%22O%22%5D&origin=FACETED_SEARCH&page=20&sid=U!%40&titleFreeText=co-founder",
                        "Hey NAME! I stumbled upon your profile and noticed that you have extensive knowledge in the same business field as me. I was wondering if you'd mind answering a few questions about the Australian market :)",
                        "Valeriia",
                        "Australia Linkedin",
                        leadCompanyAustraliaId
                },*/
/*                {       "",
                        clientName,
                        "",
                        "",
                        "",
                        "",
                        leadCompanyName,
                        leadCompanyId
                },*/
        };
    }
}
