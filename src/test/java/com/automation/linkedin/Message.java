package com.automation.linkedin;

import api.helpers.ZohoCrmHelper;
import com.automation.linkedin.pages.PersonPage;
import com.automation.linkedin.pages.login.SignInPage;
import com.automation.linkedin.pages.messaging.MessagingPage;
import com.codeborne.selenide.*;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.Utils;

import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static utils.Utils.localDateIsBeforeGivenComparison;

public class Message extends Base{
    SignInPage signInPage = new SignInPage();
    MessagingPage messagingPage = new MessagingPage();
    ZohoCrmHelper zoho = new ZohoCrmHelper();
    String chatLeadStatusid = "421659000006918053";
    private String  msg = "Good day to you.\n" +
            "\n" +
            "Quick question - have you thought about modernizing the software you are using? It might be a right decision to start the new year with new IT solutions to scale your business. WiseVision will be happy to help you with that. You can check our portfolio and see for yourself that we are the right choice for a technical vendor: https://drive.google.com/file/d/1W6Tiv-zN_D7DsCapvhHo1PGssDmjTTQN/view?usp=share_link\n" +
            "\n" +
            "We can schedule a quick call if you’re interested. Just let me know when you have free time.\n";

    @SneakyThrows
    @Test(description = "send FollowUp Msg", dataProvider = "dataProviderPeopleSearch", priority = 1)
    public void senddMsg(String linkedInAccount,  String email, String password){
        setupBrowser(true, linkedInAccount);
        openLinkedInLoginPage();
        signInPage.signIn(randomResult, email, password);
        WebDriverRunner.getWebDriver().manage().window().maximize();
        Thread.sleep(10000);
        String  token = zoho.renewAccessToken();

        sendFollowUpSecondMsg(linkedInAccount, token);
        sendFolowUpThirdMsg(linkedInAccount, token);
        sendFolowUpFourtMsg(linkedInAccount, token);
        sendFolowUpFifthMsg(linkedInAccount, token);
        sendFolowUpMeetingMsg(linkedInAccount, token);
        sendFolowUpSevenMsg(linkedInAccount, token);
        sendFolowUpEightMsg(linkedInAccount, token);
        sendFolowUpNineMsg(linkedInAccount, token);
        sendFolowUpMsg(linkedInAccount, token, "Ten automessage");
        sendFolowUpMsg(linkedInAccount, token, "FollowUp first automessage");
        sendFolowUpMsg(linkedInAccount, token, "FollowUp second automessage");
        sendFolowUpMsg(linkedInAccount, token, "FollowUp third automessage");
        sendFolowUpMsg(linkedInAccount, token, "FollowUp forth automessage");
        sendFolowUpMsg(linkedInAccount, token, "FollowUp fifth automessage");
        sendFolowUpMsg(linkedInAccount, token, "FollowUp six automessage");

    }

    @SneakyThrows
    public void sendFollowUpSecondMsg(String linkedInAccount, String token){
        System.out.println("START 2 MSG");
        for (int n = 0; n < 100; n++) {
            String data =  zoho.getLeadList(token, "Contacted", linkedInAccount, n);
            if (data.isEmpty()) break;
            System.out.println("||==================================================================||");
            JSONObject responseBodyJsonObject = new JSONObject( data );
            //System.out.println(responseBodyJsonObject);
            System.out.println(responseBodyJsonObject.getJSONArray("data").length());
            for (int i = 0; i < responseBodyJsonObject.getJSONArray("data").length(); i++) {
                String id = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("id");
                String leadPage = String.valueOf( responseBodyJsonObject.getJSONArray("data").getJSONObject(i).get("Website") );
                String fullName = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Full_Name");
                String[] fullNameArr = fullName.split(" ");
                String leadName = fullNameArr[0];
                System.out.println(id);
                System.out.println(fullName);
                System.out.println(leadPage);
                if (leadPage.equals("null")) continue;
                String tasks = zoho.getLeadTaskList(id, token);
                if (tasks.isEmpty()) continue;
                JSONObject tasksData = new JSONObject( tasks );
                System.out.println("tasksData length:"+tasksData.getJSONArray("data").length());
                if (tasksData.getJSONArray("data").length() >0){
                    for (int j = 0; j < tasksData.getJSONArray("data").length(); j++) {
                        String status = tasksData.getJSONArray("data").getJSONObject(j).getString("Status");
                        String subject = tasksData.getJSONArray("data").getJSONObject(j).getString("Subject");
                        String taskId = tasksData.getJSONArray("data").getJSONObject(j).getString("id");
                        String description = String.valueOf(tasksData.getJSONArray("data").getJSONObject(j).get("Description"));
                        String duedate = String.valueOf(tasksData.getJSONArray("data").getJSONObject(j).get("Due_Date"));
                        System.out.println("++++++++++++++++++++++++++++++");
                        System.out.println("taskId: " + taskId);
                        System.out.println("status: " + status);
                        System.out.println("duedate: " + duedate);
                        System.out.println("subject: " + subject);
                        System.out.println("description: " + description);
                        System.out.println("++++++++++++++++++++++++++++++");
                        if (status.equals("Not Started") &&  subject.contains("Second automessage") && localDateIsBeforeGivenComparison(duedate) && duedate!="null"){
                            Selenide.open(leadPage);
                            Thread.sleep(10000);
                            if (WebDriverRunner.getWebDriver().getCurrentUrl().contains("404")) continue;
                            new PersonPage().msgBtn.click();
                            List<String> msgs = $$x("//ul[contains(@class,'msg-s-message-list-content')]//li//a[contains(@class,'app-aware-link')]/span").texts();
                            if (!msgs.isEmpty() && !Utils.areAllElementsEqual(msgs)  ){
                              //  zoho.changeLeadStatus(id, token, chatLeadStatusid);
                                continue;
                            }
                            if ( $("h2[id='upsell-modal-header']").is(Condition.visible)) continue;
                            System.out.println("sent msg!!!");
                            if (description.isEmpty() || description.equals("null") ) {
                                new PersonPage().sentMsg(msg);
                                zoho.changeTaskStatus(token, taskId,"Closed");
                            }
                            else {
                                new PersonPage().sentMsg(description.replace("NAME",leadName));
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

   // @SneakyThrows
    //@Test(description = "send FollowUp Msg", dataProvider = "dataProviderPeopleSearch", priority = 2)
    @SneakyThrows
    public void sendFolowUpThirdMsg(String linkedInAccount, String token){
        System.out.println("START 3 MSG");
        for (int n = 0; n < 100; n++) {
            String data =  zoho.getLeadList(token, "Contacted", linkedInAccount, n);
            if (data.isEmpty()) break;
            System.out.println("||==================================================================||");
            JSONObject responseBodyJsonObject = new JSONObject( data );
            //System.out.println(responseBodyJsonObject);
            System.out.println(responseBodyJsonObject.getJSONArray("data").length());
            for (int i = 0; i < responseBodyJsonObject.getJSONArray("data").length(); i++) {
                String id = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("id");
                String leadPage = String.valueOf( responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Website") );
                String fullName = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Full_Name");
                String[] fullNameArr = fullName.split(" ");
                String leadName = fullNameArr[0];
                System.out.println(id);
                System.out.println(fullName);
                System.out.println(leadPage);
                if (leadPage.equals("null")) continue;
                String tasks = zoho.getLeadTaskList(id, token);
                if (tasks.isEmpty()) continue;
                JSONObject tasksData = new JSONObject( tasks );
                System.out.println("tasksData length:"+tasksData.getJSONArray("data").length());
                if (tasksData.getJSONArray("data").length() >0){
                    for (int j = 0; j < tasksData.getJSONArray("data").length(); j++) {
                        String status = tasksData.getJSONArray("data").getJSONObject(j).getString("Status");
                        String subject = tasksData.getJSONArray("data").getJSONObject(j).getString("Subject");
                        String taskId = tasksData.getJSONArray("data").getJSONObject(j).getString("id");
                        String description = String.valueOf(tasksData.getJSONArray("data").getJSONObject(j).get("Description"));
                        String duedate = String.valueOf(tasksData.getJSONArray("data").getJSONObject(j).get("Due_Date"));
                        System.out.println("++++++++++++++++++++++++++++++");
                        System.out.println("taskId: " + taskId);
                        System.out.println("status: " + status);
                        System.out.println("duedate: " + duedate);
                        System.out.println("subject: " + subject);
                        System.out.println("description: " + description);
                        System.out.println("++++++++++++++++++++++++++++++");
                        if (status.equals("Not Started") &&  subject.contains("Third automessage") && localDateIsBeforeGivenComparison(duedate) && duedate!="null"){
                            Selenide.open(leadPage);
                            Thread.sleep(10000);
                            if (WebDriverRunner.getWebDriver().getCurrentUrl().contains("404")) continue;
                            if (new PersonPage().closeBtn.is(Condition.visible)){
                                for (SelenideElement closeBtn:new PersonPage().closeBtns
                                     ) {
                                    closeBtn.click();
                                }
                            }
                            new PersonPage().msgBtn.click();
                            List<String> msgs = $$x("//ul[contains(@class,'msg-s-message-list-content')]//li//a[contains(@class,'app-aware-link')]/span").texts();
                            if (!msgs.isEmpty() && !Utils.areAllElementsEqual(msgs)  ){
                                //  zoho.changeLeadStatus(id, token, chatLeadStatusid);
                                continue;
                            }
                            if ( $("h2[id='upsell-modal-header']").is(Condition.visible)) continue;
                            System.out.println("sent msg!!!");
                            if (description.isEmpty() || description.equals("null") ) {
                                new PersonPage().sentMsg(msg);
                                zoho.changeTaskStatus(token, taskId,"Closed");
                            }
                            else {
                                new PersonPage().sentMsg(description.replace("NAME",leadName));
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

    //@SneakyThrows
    //@Test(description = "send FollowUp Msg", dataProvider = "dataProviderPeopleSearch",priority = 3)
    @SneakyThrows
    public void sendFolowUpFourtMsg(String linkedInAccount, String token){
        System.out.println("START 4 MSG");


        for (int n = 0; n < 100; n++) {
            String data =  zoho.getLeadList(token, "Contacted", linkedInAccount, n);
            if (data.isEmpty()) break;
            System.out.println("||==================================================================||");
            JSONObject responseBodyJsonObject = new JSONObject( data );
            //System.out.println(responseBodyJsonObject);
            System.out.println(responseBodyJsonObject.getJSONArray("data").length());
            for (int i = 0; i < responseBodyJsonObject.getJSONArray("data").length(); i++) {
                String id = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("id");
                String leadPage = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Website");
                String fullName = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Full_Name");
                String[] fullNameArr = fullName.split(" ");
                String leadName = fullNameArr[0];
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
                        String description = String.valueOf(tasksData.getJSONArray("data").getJSONObject(j).get("Description"));
                        String duedate = String.valueOf( tasksData.getJSONArray("data").getJSONObject(j).get("Due_Date"));

                        System.out.println(taskId);
                        System.out.println(status);
                        System.out.println(subject);
                        if (status.equals("Not Started") &&  subject.contains("Fourt automessage") && localDateIsBeforeGivenComparison(duedate)){
                            Selenide.open(leadPage);
                            Thread.sleep(10000);
                            if (WebDriverRunner.getWebDriver().getCurrentUrl().contains("404")) continue;
                            new PersonPage().msgBtn.click();
                            List<String> msgs = $$x("//ul[contains(@class,'msg-s-message-list-content')]//li//a[contains(@class,'app-aware-link')]/span").texts();
                            if (!Utils.areAllElementsEqual(msgs) && !msg.isEmpty()){
                              //  zoho.changeLeadStatus(id, token, chatLeadStatusid);
                                continue;
                            }
                            if ( $("h2[id='upsell-modal-header']").is(Condition.visible)) continue;
                            System.out.println("sent msg!!!");
                            if (description.equals("null")) {
                                new PersonPage().sentMsg(msg);
                                zoho.changeTaskStatus(token, taskId,"Closed");
                            }
                            else {
                                new PersonPage().sentMsg(description.replace("NAME",leadName));
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
    //@SneakyThrows
    //@Test(description = "send FollowUp Msg", dataProvider = "dataProviderPeopleSearch",priority = 4)
    @SneakyThrows
    public void sendFolowUpFifthMsg(String linkedinAccount, String token){
        System.out.println("START 5 MSG");

        for (int n = 0; n < 100; n++) {
            String data =  zoho.getLeadList(token, "Contacted", linkedinAccount, n);
            if (data.isEmpty()) break;
            System.out.println("||==================================================================||");
            JSONObject responseBodyJsonObject = new JSONObject( data );
            //System.out.println(responseBodyJsonObject);
            System.out.println(responseBodyJsonObject.getJSONArray("data").length());
            for (int i = 0; i < responseBodyJsonObject.getJSONArray("data").length(); i++) {
                String id = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("id");
                String leadPage = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Website");
                String fullName = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Full_Name");
                String[] fullNameArr = fullName.split(" ");
                String leadName = fullNameArr[0];                System.out.println(id);
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
                        String description = String.valueOf(tasksData.getJSONArray("data").getJSONObject(j).get("Description"));
                        String duedate = String.valueOf(tasksData.getJSONArray("data").getJSONObject(j).get("Due_Date"));

                        System.out.println(taskId);
                        System.out.println(status);
                        System.out.println(subject);
                        if (status.equals("Not Started") &&  subject.contains("Fifth automessage") && localDateIsBeforeGivenComparison(duedate)){
                            Selenide.open(leadPage);
                            Thread.sleep(10000);
                            if (WebDriverRunner.getWebDriver().getCurrentUrl().contains("404")) continue;
                            new PersonPage().msgBtn.click();
                            List<String> msgs = $$x("//ul[contains(@class,'msg-s-message-list-content')]//li//a[contains(@class,'app-aware-link')]/span").texts();
                            if (!Utils.areAllElementsEqual(msgs) && !msg.isEmpty()){
                               // zoho.changeLeadStatus(id, token, chatLeadStatusid);
                                continue;
                            }
                            if ( $("h2[id='upsell-modal-header']").is(Condition.visible)) continue;
                            System.out.println("sent msg!!!");
                            if (description.equals("null")) {
                                new PersonPage().sentMsg(msg);
                                zoho.changeTaskStatus(token, taskId,"Closed");
                            }
                            else {
                                new PersonPage().sentMsg(description.replace("NAME",leadName));
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
    public void sendFolowUpMeetingMsg(String linkedinAccount, String token){
        System.out.println("START Meeting MSG");

        for (int n = 0; n < 100; n++) {
            String data =  zoho.getLeadList(token, "Contacted", linkedinAccount, n);
            if (data.isEmpty()) break;
            System.out.println("||==================================================================||");
            JSONObject responseBodyJsonObject = new JSONObject( data );
            //System.out.println(responseBodyJsonObject);
            System.out.println(responseBodyJsonObject.getJSONArray("data").length());
            for (int i = 0; i < responseBodyJsonObject.getJSONArray("data").length(); i++) {
                String id = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("id");
                String leadPage = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Website");
                String fullName = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Full_Name");
                String[] fullNameArr = fullName.split(" ");
                String leadName = fullNameArr[0];                System.out.println(id);
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
                        String description = String.valueOf(tasksData.getJSONArray("data").getJSONObject(j).get("Description"));
                        String duedate = String.valueOf(tasksData.getJSONArray("data").getJSONObject(j).get("Due_Date"));

                        System.out.println(taskId);
                        System.out.println(status);
                        System.out.println(subject);
                        if (status.equals("Not Started") &&  subject.contains("Meeting automessage") && localDateIsBeforeGivenComparison(duedate)){
                            Selenide.open(leadPage);
                            Thread.sleep(10000);
                            if (WebDriverRunner.getWebDriver().getCurrentUrl().contains("404")) continue;
                            new PersonPage().msgBtn.click();
                            List<String> msgs = $$x("//ul[contains(@class,'msg-s-message-list-content')]//li//a[contains(@class,'app-aware-link')]/span").texts();
                            if (!Utils.areAllElementsEqual(msgs) && !msg.isEmpty()){
                                // zoho.changeLeadStatus(id, token, chatLeadStatusid);
                                continue;
                            }
                            if ( $("h2[id='upsell-modal-header']").is(Condition.visible)) continue;
                            System.out.println("sent msg!!!");
                            if (description.equals("null")) {
                                new PersonPage().sentMsg("Lets go to meeting");
                                zoho.changeTaskStatus(token, taskId,"Closed");
                            }
                            else {
                                new PersonPage().sentMsg(description.replace("NAME",leadName));
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
    public void sendFolowUpSevenMsg(String linkedinAccount, String token){
        System.out.println("START Meeting MSG");

        for (int n = 0; n < 100; n++) {
            String data =  zoho.getLeadList(token, "Contacted", linkedinAccount, n);
            if (data.isEmpty()) break;
            System.out.println("||==================================================================||");
            JSONObject responseBodyJsonObject = new JSONObject( data );
            //System.out.println(responseBodyJsonObject);
            System.out.println(responseBodyJsonObject.getJSONArray("data").length());
            for (int i = 0; i < responseBodyJsonObject.getJSONArray("data").length(); i++) {
                String id = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("id");
                String leadPage = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Website");
                String fullName = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Full_Name");
                String[] fullNameArr = fullName.split(" ");
                String leadName = fullNameArr[0];                System.out.println(id);
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
                        String description = String.valueOf(tasksData.getJSONArray("data").getJSONObject(j).get("Description"));
                        String duedate = String.valueOf(tasksData.getJSONArray("data").getJSONObject(j).get("Due_Date"));

                        System.out.println(taskId);
                        System.out.println(status);
                        System.out.println(subject);
                        if (status.equals("Not Started") &&  subject.contains("Seven automessage") && localDateIsBeforeGivenComparison(duedate)){
                            Selenide.open(leadPage);
                            Thread.sleep(10000);
                            if (WebDriverRunner.getWebDriver().getCurrentUrl().contains("404")) continue;
                            new PersonPage().msgBtn.click();
                            List<String> msgs = $$x("//ul[contains(@class,'msg-s-message-list-content')]//li//a[contains(@class,'app-aware-link')]/span").texts();
                            if (!Utils.areAllElementsEqual(msgs) && !msg.isEmpty()){
                                // zoho.changeLeadStatus(id, token, chatLeadStatusid);
                                continue;
                            }
                            if ( $("h2[id='upsell-modal-header']").is(Condition.visible)) continue;
                            System.out.println("sent msg!!!");
                            if (description.equals("null")) {
                                new PersonPage().sentMsg("Lets go to meeting");
                                zoho.changeTaskStatus(token, taskId,"Closed");
                            }
                            else {
                                new PersonPage().sentMsg(description.replace("NAME",leadName));
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
    public void sendFolowUpEightMsg(String linkedinAccount, String token){
        System.out.println("START Meeting MSG");

        for (int n = 0; n < 100; n++) {
            String data =  zoho.getLeadList(token, "Contacted", linkedinAccount, n);
            if (data.isEmpty()) break;
            System.out.println("||==================================================================||");
            JSONObject responseBodyJsonObject = new JSONObject( data );
            //System.out.println(responseBodyJsonObject);
            System.out.println(responseBodyJsonObject.getJSONArray("data").length());
            for (int i = 0; i < responseBodyJsonObject.getJSONArray("data").length(); i++) {
                String id = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("id");
                String leadPage = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Website");
                String fullName = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Full_Name");
                String[] fullNameArr = fullName.split(" ");
                String leadName = fullNameArr[0];                System.out.println(id);
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
                        String description = String.valueOf(tasksData.getJSONArray("data").getJSONObject(j).get("Description"));
                        String duedate = String.valueOf(tasksData.getJSONArray("data").getJSONObject(j).get("Due_Date"));

                        System.out.println(taskId);
                        System.out.println(status);
                        System.out.println(subject);
                        if (status.equals("Not Started") &&  subject.contains("Eight automessage") && localDateIsBeforeGivenComparison(duedate)){
                            Selenide.open(leadPage);
                            Thread.sleep(10000);
                            if (WebDriverRunner.getWebDriver().getCurrentUrl().contains("404")) continue;
                            new PersonPage().msgBtn.click();
                            List<String> msgs = $$x("//ul[contains(@class,'msg-s-message-list-content')]//li//a[contains(@class,'app-aware-link')]/span").texts();
                            if (!Utils.areAllElementsEqual(msgs) && !msg.isEmpty()){
                                // zoho.changeLeadStatus(id, token, chatLeadStatusid);
                                continue;
                            }
                            if ( $("h2[id='upsell-modal-header']").is(Condition.visible)) continue;
                            System.out.println("sent msg!!!");
                            if (description.equals("null")) {
                                new PersonPage().sentMsg("Lets go to meeting");
                                zoho.changeTaskStatus(token, taskId,"Closed");
                            }
                            else {
                                new PersonPage().sentMsg(description.replace("NAME",leadName));
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
    public void sendFolowUpNineMsg(String linkedinAccount, String token){
        System.out.println("START Meeting MSG");

        for (int n = 0; n < 100; n++) {
            String data =  zoho.getLeadList(token, "Contacted", linkedinAccount, n);
            if (data.isEmpty()) break;
            System.out.println("||==================================================================||");
            JSONObject responseBodyJsonObject = new JSONObject( data );
            //System.out.println(responseBodyJsonObject);
            System.out.println(responseBodyJsonObject.getJSONArray("data").length());
            for (int i = 0; i < responseBodyJsonObject.getJSONArray("data").length(); i++) {
                String id = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("id");
                String leadPage = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Website");
                String fullName = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Full_Name");
                String[] fullNameArr = fullName.split(" ");
                String leadName = fullNameArr[0];                System.out.println(id);
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
                        String description = String.valueOf(tasksData.getJSONArray("data").getJSONObject(j).get("Description"));
                        String duedate = String.valueOf(tasksData.getJSONArray("data").getJSONObject(j).get("Due_Date"));

                        System.out.println(taskId);
                        System.out.println(status);
                        System.out.println(subject);
                        if (status.equals("Not Started") &&  subject.contains("Nine automessage") && localDateIsBeforeGivenComparison(duedate)){
                            Selenide.open(leadPage);
                            Thread.sleep(10000);
                            if (WebDriverRunner.getWebDriver().getCurrentUrl().contains("404")) continue;
                            new PersonPage().msgBtn.click();
                            List<String> msgs = $$x("//ul[contains(@class,'msg-s-message-list-content')]//li//a[contains(@class,'app-aware-link')]/span").texts();
                            if (!Utils.areAllElementsEqual(msgs) && !msg.isEmpty()){
                                // zoho.changeLeadStatus(id, token, chatLeadStatusid);
                                continue;
                            }
                            if ( $("h2[id='upsell-modal-header']").is(Condition.visible)) continue;
                            System.out.println("sent msg!!!");
                            if (description.equals("null")) {
                                new PersonPage().sentMsg("Lets go to meeting");
                                zoho.changeTaskStatus(token, taskId,"Closed");
                            }
                            else {
                                new PersonPage().sentMsg(description.replace("NAME",leadName));
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
    public void sendFolowUpMsg(String linkedinAccount, String token, String taskName){
        System.out.println("START Meeting MSG");

        for (int n = 0; n < 100; n++) {
            String data =  zoho.getLeadList(token, "Contacted", linkedinAccount, n);
            if (data.isEmpty()) break;
            System.out.println("||==================================================================||");
            JSONObject responseBodyJsonObject = new JSONObject( data );
            //System.out.println(responseBodyJsonObject);
            System.out.println(responseBodyJsonObject.getJSONArray("data").length());
            for (int i = 0; i < responseBodyJsonObject.getJSONArray("data").length(); i++) {
                String id = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("id");
                String leadPage = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Website");
                String fullName = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Full_Name");
                String[] fullNameArr = fullName.split(" ");
                String leadName = fullNameArr[0];                System.out.println(id);
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
                        String description = String.valueOf(tasksData.getJSONArray("data").getJSONObject(j).get("Description"));
                        String duedate = String.valueOf(tasksData.getJSONArray("data").getJSONObject(j).get("Due_Date"));

                        System.out.println(taskId);
                        System.out.println(status);
                        System.out.println(subject);
                        if (status.equals("Not Started") &&  subject.contains(taskName) && localDateIsBeforeGivenComparison(duedate)){
                            Selenide.open(leadPage);
                            Thread.sleep(10000);
                            if (WebDriverRunner.getWebDriver().getCurrentUrl().contains("404")) continue;
                            new PersonPage().msgBtn.click();
                            List<String> msgs = $$x("//ul[contains(@class,'msg-s-message-list-content')]//li//a[contains(@class,'app-aware-link')]/span").texts();
                            if (!Utils.areAllElementsEqual(msgs) && !msg.isEmpty()){
                                // zoho.changeLeadStatus(id, token, chatLeadStatusid);
                                continue;
                            }
                            if ( $("h2[id='upsell-modal-header']").is(Condition.visible)) continue;
                            System.out.println("sent msg!!!");
                            if (description.equals("null")) {
                                new PersonPage().sentMsg("Lets go to meeting");
                                zoho.changeTaskStatus(token, taskId,"Closed");
                            }
                            else {
                                new PersonPage().sentMsg(description.replace("NAME",leadName));
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
                //1
                {       "Aleksandra Sternenko",
                        "alexandra.sternenko@gmail.com",
                        "asd321qq",
                },
                //6
                {       "Natalia Marcun",
                        "natalia.marcoon@gmail.com ",
                        "33222200Shin",
                },

                //9
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

                {       "Dymitr Tolmach",
                        "dymitr.tolmach1012@outlook.com",
                        "33222200Shin",
                },
                {       "Oleg Valter",
                        "ovalter@outlook.co.nz",
                        "Shmee2023",
                },
                {       "Oleg Konorov",
                        "oleg.konorov@outlook.com",
                        "33222200Shin",
                },

                {       "Dmitriy Semiletov",
                        "semi.dima@outlook.com",
                        "33222200Shin",
                },

        };
    }
}
