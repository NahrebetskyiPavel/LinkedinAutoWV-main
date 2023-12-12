package com.automation.linkedin;

import api.helpers.ZohoCrmHelper;
import com.automation.linkedin.pages.login.SignInPage;
import com.automation.linkedin.pages.messaging.MessagingPage;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.interactable;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class Messaging extends Base{
    SignInPage signInPage = new SignInPage();
    MessagingPage messagingPage = new MessagingPage();
    ZohoCrmHelper zoho = new ZohoCrmHelper();
    @SneakyThrows
    @Test(description = "send FollowUp Msg", dataProvider = "dataProviderPeopleSearch")
    public void sendFolowUpMsg(String name, String clientName, String email, String password, String msgLink, String msg, String pickList, String leadCompany, String leadCompanyId){
        setupBrowser(true, name);
        openLinkedInLoginPage();
        signInPage.signIn(randomResult, email, password);
        WebDriverRunner.getWebDriver().manage().window().maximize();
        String  token = zoho.renewAccessToken();

        String data =  zoho.getLeadList(token, "Yurij", "Contacted","Михайло",0);
        //System.out.println(data);
        System.out.println("||==================================================================||");
        JSONObject responseBodyJsonObject = new JSONObject( data );
        //System.out.println(responseBodyJsonObject);
        System.out.println(responseBodyJsonObject.getJSONArray("data").length());
        for (int i = 0; i < 200; i++) {
            String id = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("id");
            String leadPage = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Website");
            String fullName = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Full_Name");
            System.out.println(id);
            System.out.println(fullName);
            System.out.println(leadPage);
            String tasks = zoho.getLeadTaskList(id, token);
            JSONObject tasksData = new JSONObject( tasks );
            System.out.println(tasksData.getJSONArray("data"));
            System.out.println("tasksData length:"+tasksData.getJSONArray("data").length());
            if (tasksData.getJSONArray("data").length() >0){
                for (int j = 0; j < tasksData.getJSONArray("data").length(); j++) {
                    String status = tasksData.getJSONArray("data").getJSONObject(j).getString("Status");
                    String subject = tasksData.getJSONArray("data").getJSONObject(j).getString("Subject");
                    System.out.println(status);
                    System.out.println(subject);
                    if (status.equals("Not Started") &&  subject.contains("Second message")){
                        System.out.println("open" + leadPage + " and sent second message to " + fullName);
                        Selenide.open(leadPage);

                    };
                }
            }
            System.out.println();
            System.out.println("\n");
            System.out.println("====================================================================");
            System.out.println("\n");
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
                {       "Михайло",
                        clientName,
                        "michael.salo1995@gmail.com",
                        "newman1996",
                        "https://www.linkedin.com/",
                        "How are you doing?",
                        "Pavlo",
                        "Australia Linkedin",
                        leadCompanyAustraliaId
                },
        };
    }
}
