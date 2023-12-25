package com.automation.linkedin;

import api.helpers.ZohoCrmHelper;
import com.automation.linkedin.pages.PersonPage;
import com.automation.linkedin.pages.login.SignInPage;
import com.automation.linkedin.pages.messaging.MessagingPage;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.Utils;

import java.util.List;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static utils.Utils.localDateIsBeforeGivenComparison;

public class Message extends Base{
    SignInPage signInPage = new SignInPage();
    MessagingPage messagingPage = new MessagingPage();
    ZohoCrmHelper zoho = new ZohoCrmHelper();
    private String  msg = "Good day to you.\n" +
            "\n" +
            "Quick question - have you thought about modernizing the software you are using? It might be a right decision to start the new year with new IT solutions to scale your business. WiseVision will be happy to help you with that. You can check our portfolio and see for yourself that we are the right choice for a technical vendor: https://drive.google.com/file/d/1W6Tiv-zN_D7DsCapvhHo1PGssDmjTTQN/view?usp=share_link\n" +
            "\n" +
            "We can schedule a quick call if you’re interested. Just let me know when you have free time.\n";

    @SneakyThrows
    @Test(description = "send FollowUp Msg", dataProvider = "dataProviderPeopleSearch", priority = 1)
    public void sendFollowUpSecondMsg(String name,  String email, String password, String pickList){
        System.out.println("START 2 MSG");
        setupBrowser(true, name);
        openLinkedInLoginPage();
        signInPage.signIn(randomResult, email, password);
        WebDriverRunner.getWebDriver().manage().window().maximize();
        Thread.sleep(10000);
        String  token = zoho.renewAccessToken();

        for (int n = 0; n < 100; n++) {
            String data =  zoho.getLeadList(token, pickList, "Contacted", name, n);
            if (data.isEmpty()) break;
            System.out.println("||==================================================================||");
            JSONObject responseBodyJsonObject = new JSONObject( data );
            //System.out.println(responseBodyJsonObject);
            System.out.println(responseBodyJsonObject.getJSONArray("data").length());
            for (int i = 0; i < responseBodyJsonObject.getJSONArray("data").length(); i++) {
                String id = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("id");
                String leadPage = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Website");
                String fullName = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Full_Name");
                System.out.println(id);
                System.out.println(fullName);
                System.out.println(leadPage);
                String tasks = zoho.getLeadTaskList(id, token);
                if (tasks.isEmpty()) continue;
                JSONObject tasksData = new JSONObject( tasks );
                System.out.println("tasksData length:"+tasksData.getJSONArray("data").length());
                if (tasksData.getJSONArray("data").length() >0){
                    for (int j = 0; j < tasksData.getJSONArray("data").length(); j++) {
                        String status = tasksData.getJSONArray("data").getJSONObject(j).getString("Status");
                        String subject = tasksData.getJSONArray("data").getJSONObject(j).getString("Subject");
                        String taskId = tasksData.getJSONArray("data").getJSONObject(j).getString("id");
                        String description = String.valueOf(new JSONObject( data ).getJSONArray("data").getJSONObject(j).get("Description"));
                        String duedate = tasksData.getJSONArray("data").getJSONObject(j).getString("Due_Date");

                        System.out.println(taskId);
                        System.out.println(status);
                        System.out.println(duedate);
                        System.out.println(subject);
                        if (status.equals("Not Started") &&  subject.contains("Second automessage") && localDateIsBeforeGivenComparison(duedate)){
                            Selenide.open(leadPage);
                            new PersonPage().msgBtn.click();
                            List<String> msgs = $$x("//ul[contains(@class,'msg-s-message-list-content')]//li//a[contains(@class,'app-aware-link')]/span").texts();
                            if (!Utils.areAllElementsEqual(msgs) && !msg.isEmpty()){
                                zoho.changeLeadStatus(id, token, "421659000006918053");
                                continue;
                            }
                            System.out.println("sent msg!!!");
                            if (description.equals("null")) {
                                new PersonPage().sentMsg(msg);
                                zoho.changeTaskStatus(token, taskId,"Closed");
                            }
                            else {
                                new PersonPage().sentMsg(description.replace("NAME",fullName));
                                zoho.changeTaskStatus(token, taskId,"Closed");
                            }
                        };
                    }
                }

                System.out.println();
                System.out.println("\n");
                System.out.println("====================================================================");
                System.out.println("\n");
            }
        }


    }

    @SneakyThrows
    @Test(description = "send FollowUp Msg", dataProvider = "dataProviderPeopleSearch", priority = 2)
    public void sendFolowUpThirdMsg(String name,  String email, String password, String pickList){
        System.out.println("START 3 MSG");

        setupBrowser(true, name);
        openLinkedInLoginPage();
        signInPage.signIn(randomResult, email, password);
        WebDriverRunner.getWebDriver().manage().window().maximize();
        String  token = zoho.renewAccessToken();

        for (int n = 0; n < 100; n++) {
            String data =  zoho.getLeadList(token, pickList, "Contacted", name, n);
            if (data.isEmpty()) break;
            System.out.println("||==================================================================||");
            JSONObject responseBodyJsonObject = new JSONObject( data );
            //System.out.println(responseBodyJsonObject);
            System.out.println(responseBodyJsonObject.getJSONArray("data").length());
            for (int i = 0; i < responseBodyJsonObject.getJSONArray("data").length(); i++) {
                String id = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("id");
                String leadPage = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Website");
                String fullName = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Full_Name");
                System.out.println(id);
                System.out.println(fullName);
                System.out.println(leadPage);
                String tasks = zoho.getLeadTaskList(id, token);
                if (tasks.isEmpty()) continue;
                JSONObject tasksData = new JSONObject( tasks );
                System.out.println(tasksData.getJSONArray("data"));
                System.out.println("tasksData length:"+tasksData.getJSONArray("data").length());
                if (tasksData.getJSONArray("data").length() >0){
                    for (int j = 0; j < tasksData.getJSONArray("data").length(); j++) {
                        String status = tasksData.getJSONArray("data").getJSONObject(j).getString("Status");
                        String subject = tasksData.getJSONArray("data").getJSONObject(j).getString("Subject");
                        String taskId = tasksData.getJSONArray("data").getJSONObject(j).getString("id");
                        String description = String.valueOf(new JSONObject( data ).getJSONArray("data").getJSONObject(j).get("Description"));
                        String duedate = tasksData.getJSONArray("data").getJSONObject(j).getString("Due_Date");

                        System.out.println(taskId);
                        System.out.println(status);
                        System.out.println(subject);
                        if (status.equals("Not Started") &&  subject.contains("Third automessage") && localDateIsBeforeGivenComparison(duedate)){
                            Selenide.open(leadPage);
                            new PersonPage().msgBtn.click();
                            List<String> msgs = $$x("//ul[contains(@class,'msg-s-message-list-content')]//li//a[contains(@class,'app-aware-link')]/span").texts();
                            if (!Utils.areAllElementsEqual(msgs) && !msg.isEmpty()){
                                zoho.changeLeadStatus(id, token, "421659000006918053");
                                continue;
                            }
                            System.out.println("sent msg!!!");
                            if (description.equals("null")) {
                                new PersonPage().sentMsg(msg);
                                zoho.changeTaskStatus(token, taskId,"Closed");
                            }
                            else {
                                new PersonPage().sentMsg(description.replace("NAME",fullName));
                                zoho.changeTaskStatus(token, taskId,"Closed");
                            }
                        };
                    }
                }

                System.out.println();
                System.out.println("\n");
                System.out.println("====================================================================");
                System.out.println("\n");
            }
        }


    }

    @SneakyThrows
    @Test(description = "send FollowUp Msg", dataProvider = "dataProviderPeopleSearch",priority = 3)
    public void sendFolowUpFourtMsg(String name,  String email, String password, String pickList){
        System.out.println("START 4 MSG");

        setupBrowser(true, name);
        openLinkedInLoginPage();
        signInPage.signIn(randomResult, email, password);
        WebDriverRunner.getWebDriver().manage().window().maximize();
        String  token = zoho.renewAccessToken();

        for (int n = 0; n < 100; n++) {
            String data =  zoho.getLeadList(token, pickList, "Contacted", name, n);
            if (data.isEmpty()) break;
            System.out.println("||==================================================================||");
            JSONObject responseBodyJsonObject = new JSONObject( data );
            //System.out.println(responseBodyJsonObject);
            System.out.println(responseBodyJsonObject.getJSONArray("data").length());
            for (int i = 0; i < responseBodyJsonObject.getJSONArray("data").length(); i++) {
                String id = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("id");
                String leadPage = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Website");
                String fullName = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Full_Name");
                System.out.println(id);
                System.out.println(fullName);
                System.out.println(leadPage);
                String tasks = zoho.getLeadTaskList(id, token);
                if (tasks.isEmpty()) continue;
                JSONObject tasksData = new JSONObject( tasks );
                System.out.println(tasksData.getJSONArray("data"));
                System.out.println("tasksData length:"+tasksData.getJSONArray("data").length());
                if (tasksData.getJSONArray("data").length() >0){
                    for (int j = 0; j < tasksData.getJSONArray("data").length(); j++) {
                        String status = tasksData.getJSONArray("data").getJSONObject(j).getString("Status");
                        String subject = tasksData.getJSONArray("data").getJSONObject(j).getString("Subject");
                        String taskId = tasksData.getJSONArray("data").getJSONObject(j).getString("id");
                        String description = String.valueOf(new JSONObject( data ).getJSONArray("data").getJSONObject(j).get("Description"));
                        String duedate = tasksData.getJSONArray("data").getJSONObject(j).getString("Due_Date");

                        System.out.println(taskId);
                        System.out.println(status);
                        System.out.println(subject);
                        if (status.equals("Not Started") &&  subject.contains("Fourt automessage") && localDateIsBeforeGivenComparison(duedate)){
                            Selenide.open(leadPage);
                            new PersonPage().msgBtn.click();
                            List<String> msgs = $$x("//ul[contains(@class,'msg-s-message-list-content')]//li//a[contains(@class,'app-aware-link')]/span").texts();
                            if (!Utils.areAllElementsEqual(msgs) && !msg.isEmpty()){
                                zoho.changeLeadStatus(id, token, "421659000006918053");
                                continue;
                            }
                            System.out.println("sent msg!!!");
                            if (description.equals("null")) {
                                new PersonPage().sentMsg(msg);
                                zoho.changeTaskStatus(token, taskId,"Closed");
                            }
                            else {
                                new PersonPage().sentMsg(description.replace("NAME",fullName));
                                zoho.changeTaskStatus(token, taskId,"Closed");
                            }
                        };
                    }
                }

                System.out.println();
                System.out.println("\n");
                System.out.println("====================================================================");
                System.out.println("\n");
            }
        }

    }
    @SneakyThrows
    @Test(description = "send FollowUp Msg", dataProvider = "dataProviderPeopleSearch",priority = 4)
    public void sendFolowUpFifthMsg(String linkedinAccount,  String email, String password, String pickList){
        System.out.println("START 5 MSG");

        setupBrowser(true, linkedinAccount);
        openLinkedInLoginPage();
        signInPage.signIn(randomResult, email, password);
        WebDriverRunner.getWebDriver().manage().window().maximize();
        String  token = zoho.renewAccessToken();

        for (int n = 0; n < 100; n++) {
            String data =  zoho.getLeadList(token, pickList, "Contacted", linkedinAccount, n);
            if (data.isEmpty()) break;
            System.out.println("||==================================================================||");
            JSONObject responseBodyJsonObject = new JSONObject( data );
            //System.out.println(responseBodyJsonObject);
            System.out.println(responseBodyJsonObject.getJSONArray("data").length());
            for (int i = 0; i < responseBodyJsonObject.getJSONArray("data").length(); i++) {
                String id = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("id");
                String leadPage = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Website");
                String fullName = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Full_Name");
                System.out.println(id);
                System.out.println(fullName);
                System.out.println(leadPage);
                String tasks = zoho.getLeadTaskList(id, token);
                if (tasks.isEmpty()) continue;
                JSONObject tasksData = new JSONObject( tasks );
                System.out.println(tasksData.getJSONArray("data"));
                System.out.println("tasksData length:"+tasksData.getJSONArray("data").length());
                if (tasksData.getJSONArray("data").length() >0){
                    for (int j = 0; j < tasksData.getJSONArray("data").length(); j++) {
                        String status = tasksData.getJSONArray("data").getJSONObject(j).getString("Status");
                        String subject = tasksData.getJSONArray("data").getJSONObject(j).getString("Subject");
                        String taskId = tasksData.getJSONArray("data").getJSONObject(j).getString("id");
                        String description = String.valueOf(new JSONObject( data ).getJSONArray("data").getJSONObject(j).get("Description"));
                        String duedate = tasksData.getJSONArray("data").getJSONObject(j).getString("Due_Date");

                        System.out.println(taskId);
                        System.out.println(status);
                        System.out.println(subject);
                        if (status.equals("Not Started") &&  subject.contains("Fifth automessage") && localDateIsBeforeGivenComparison(duedate)){
                            Selenide.open(leadPage);
                            new PersonPage().msgBtn.click();
                            List<String> msgs = $$x("//ul[contains(@class,'msg-s-message-list-content')]//li//a[contains(@class,'app-aware-link')]/span").texts();
                            if (!Utils.areAllElementsEqual(msgs) && !msg.isEmpty()){
                                zoho.changeLeadStatus(id, token, "421659000006918053");
                                continue;
                            }
                            System.out.println("sent msg!!!");
                            if (description.equals("null")) {
                                new PersonPage().sentMsg(msg);
                                zoho.changeTaskStatus(token, taskId,"Closed");
                            }
                            else {
                                new PersonPage().sentMsg(description.replace("NAME",fullName));
                                zoho.changeTaskStatus(token, taskId,"Closed");
                            }
                        };
                    }
                }

                System.out.println();
                System.out.println("\n");
                System.out.println("====================================================================");
                System.out.println("\n");
            }
        }

    }

    @DataProvider(name = "dataProviderPeopleSearch", parallel=true)
    public static Object[][] dataProviderPeopleSearch() {
        return new Object[][]{

                {       "Anastasiia K.",
                        "anastasiiakuntii@gmail.com",
                        "33222200Shin",
                        "Alex",
                }
        };
    }
}
