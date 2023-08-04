package com.automation.crm;

import api.helpers.ZohoCrmHelper;
import com.automation.linkedin.pages.PersonPage;
import com.automation.linkedin.pages.login.SignInPage;
import com.automation.linkedin.pages.messaging.MessagingPage;
import com.automation.linkedin.pages.search.SearchPeoplePage;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Condition.visible;

public class AddLeads {
    ZohoCrmHelper zohoCrmHelper = new ZohoCrmHelper();
    SignInPage signInPage = new SignInPage();
    SearchPeoplePage searchPeoplePage = new SearchPeoplePage();

    @SneakyThrows
    @Test(description = "add leads from search page", dataProvider = "dataProviderPeopleAddToCRM")
    public void addLeadsToCRM(String name, String email, String password, String pickList, String leadCompany, String leadCompanyId){

        Selenide.open("https://www.linkedin.com/login");
        WebDriverRunner.getWebDriver().quit();
        Selenide.open("https://www.linkedin.com/login");
        signInPage.loginField.shouldBe(visible).setValue(email);
        signInPage.passwordField.shouldBe(visible).setValue(password).pressEnter();
        Thread.sleep(2*1000);
        Selenide.open("https://www.linkedin.com/search/results/people/?network=%5B%22F%22%5D&origin=MEMBER_PROFILE_CANNED_SEARCH&sid=D)s");
        WebDriverRunner.getWebDriver().manage().window().maximize();
        String token = zohoCrmHelper.renewAccessToken();
        for (int i = 0; i < 100; i++) {
            for (SelenideElement person:searchPeoplePage.PersonPages
            ) {
                String personRef = person.getAttribute("href");
                String[] personNamearr = person.find(By.cssSelector("span")).text().split("\\s");
                String personName = personNamearr[0] + " " + personNamearr[1];

                System.out.println(personName);
                System.out.println(token);
                System.out.println(personRef);
                zohoCrmHelper.AddLeadToCRM(personName, token, pickList, personRef, "Contacted", leadCompany, leadCompanyId);
                Thread.sleep(500);
                Selenide.executeJavaScript("window.scrollTo(2000, document.body.scrollHeight)");
                System.out.println("\n" + WebDriverRunner.getWebDriver().getCurrentUrl() );
/*           System.out.println(personRef);
            Selenide.executeJavaScript("window.open(\'" + personRef + "\')");
            Selenide.switchTo().window(1);
            Thread.sleep(5000);
           personPage.moreBtn.click();
           Thread.sleep(2000);
           Selenide.closeWindow();
           switchTo().window(0);*/
            }
            Thread.sleep( 1000);
            searchPeoplePage.nextPageBtn.shouldBe(visible).click();
            Thread.sleep( 1000);
        }
    }

    @DataProvider(name = "dataProviderPeopleAddToCRM", parallel=true)
    public static Object[][] dataProviderPeopleAddToCRM() {
       String leadCompanyId ="421659000004663019";
        return new Object[][]{
/*                {       "Анастасия - Kuwait, Kuwait City",
                        "cartoonpictureee@gmail.com",
                        "zdHXF5bf",
                        "Yurij"},*/

/*                {       "Софія- Finland, Helsinki",
                        "podliesnasofia@gmail.com",
                        "7riuwotu949",
                        "Alex"
                },*/

/*                {       "Маша - United Arab Emirates, Abu Dhabi",
                        "deynekamariawv@gmail.com",
                        "3N2wbnsw",
                        "Yurij"
                },*/

/*                {       "Михайло - United Arab Emirates, Dubai",
                        "michael.salo1995@gmail.com",
                        "newman1996",
                        "Yurij"
                },*/

/*                {       "Александра - Bahrain, Manama",
                        "alexandra.sternenko@gmail.com",
                        "asd321qq",
                        "Valeriia"
                },*/

/*                {       "Роксолана- United Arab Emirates, Sharjah (CEO)",
                        "roksolanatrofim@gmail.com ",
                        "89fcmTT88V",
                        "Alex"},*/

/*                {       "Марьян- Bahrain, Muharraq",
                        "reshetunmaryanwv@gmail.com",
                        "rSbnGaRS",
                        "Alex"
                },*/

/*                {       "Максим- Kuwait, Al Farwaniyah",
                        "kotokmaksym@gmail.com",
                        "r4E3w2q1",
                        "Yurij"
                },*/

/*                {       "Наталья- USA, Phoenix, AZ, CEO, Hospitals and Health Care",
                        "natalia.marcoon@gmail.com",
                        "asd321qq",
                        "Valeriia"
                },*/

                {       "Денис - Czechia, Prague",
                        "basdenisphytontm@gmail.com",
                        "asd321qq",
                        "Pavlo",
                        "testLeadComapny"
                },
        };
    }
}

