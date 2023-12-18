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

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static utils.Utils.localDateIsBeforeGivenComparison;

public class Message extends Base{
    SignInPage signInPage = new SignInPage();
    MessagingPage messagingPage = new MessagingPage();
    ZohoCrmHelper zoho = new ZohoCrmHelper();
    @SneakyThrows
    @Test(description = "send FollowUp Msg", dataProvider = "dataProviderPeopleSearch", priority = 1)
    public void sendFolowUpSecontMsg(String name,  String email, String password, String pickList){

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
                        if (status.equals("Not Started") &&  subject.contains("Second message") && localDateIsBeforeGivenComparison(duedate)){
                            System.out.println("open " + leadPage + " and sent second message to " + fullName);
                            Selenide.open(leadPage);
                     //      if (   $x("//ul[contains(@class,'msg-s-message-list-content')]").exists() &&    $x("//ul[contains(@class,'msg-s-message-list-content')]").text().length()>0)
                            ElementsCollection msgs = $$x("//ul[contains(@class,'msg-s-message-list-content')]//li//a[contains(@class,'app-aware-link')]/span");
                            if (!msgs.isEmpty() & !Utils.areAllElementsEqual(msgs)){
//zoho.changeLeadStatus(id, token, transition_id);
                            }
                            else {
                                new PersonPage().msgBtn.click();
                                if (!$x("//div[contains(@aria-label,'Write a message…')]").is(Condition.visible)) continue;
                                $x("//div[contains(@aria-label,'Write a message…')]").click();
                                if (description.equals("null")){
                                    $x("//div[contains(@aria-label,'Write a message…')]").sendKeys("Good day to you.\n" +
                                        "\n" +
                                        "Quick question - have you thought about modernizing the software you are using? It might be a right decision to start the new year with new IT solutions to scale your business. WiseVision will be happy to help you with that. You can check our portfolio and see for yourself that we are the right choice for a technical vendor: https://drive.google.com/file/d/1W6Tiv-zN_D7DsCapvhHo1PGssDmjTTQN/view?usp=share_link\n" +
                                        "\n" +
                                        "We can schedule a quick call if you’re interested. Just let me know when you have free time.\n");}else{
                                    $x("//div[contains(@aria-label,'Write a message…')]").sendKeys(description);
                                };

                                $x("//button[normalize-space()='Send']").click();
                                Thread.sleep(randomResult);
                                $x("//div[contains(@aria-label,'Messaging')]//div[contains(@class,'msg-overlay-bubble-header__controls')]/button[3]").click();
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
                        if (status.equals("Not Started") &&  subject.contains("Third message") && localDateIsBeforeGivenComparison(duedate)){
                            System.out.println("open " + leadPage + " and sent Third message to " + fullName);
                            Selenide.open(leadPage);
                     //      if (   $x("//ul[contains(@class,'msg-s-message-list-content')]").exists() &&    $x("//ul[contains(@class,'msg-s-message-list-content')]").text().length()>0)
                            ElementsCollection msgs = $$x("//ul[contains(@class,'msg-s-message-list-content')]//li//a[contains(@class,'app-aware-link')]/span");
                            if (!msgs.isEmpty() & !Utils.areAllElementsEqual(msgs)){
//zoho.changeLeadStatus(id, token, transition_id);
                            }
                            else {
                                new PersonPage().msgBtn.click();
                                if (!$x("//div[contains(@aria-label,'Write a message…')]").is(Condition.visible)) continue;
                                $x("//div[contains(@aria-label,'Write a message…')]").click();
                                if (description.equals("null")){
                                    $x("//div[contains(@aria-label,'Write a message…')]").sendKeys("Good day to you.\n" +
                                        "\n" +
                                        "Quick question - have you thought about modernizing the software you are using? It might be a right decision to start the new year with new IT solutions to scale your business. WiseVision will be happy to help you with that. You can check our portfolio and see for yourself that we are the right choice for a technical vendor: https://drive.google.com/file/d/1W6Tiv-zN_D7DsCapvhHo1PGssDmjTTQN/view?usp=share_link\n" +
                                        "\n" +
                                        "We can schedule a quick call if you’re interested. Just let me know when you have free time.\n");}else{
                                    $x("//div[contains(@aria-label,'Write a message…')]").sendKeys(description);
                                };

                                $x("//button[normalize-space()='Send']").click();
                                Thread.sleep(randomResult);
                                $x("//div[contains(@aria-label,'Messaging')]//div[contains(@class,'msg-overlay-bubble-header__controls')]/button[3]").click();
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
                        if (status.equals("Not Started") &&  subject.contains("Fourt message") && localDateIsBeforeGivenComparison(duedate)){
                            System.out.println("open " + leadPage + " and sent Fourt message to " + fullName);
                            Selenide.open(leadPage);
                     //      if (   $x("//ul[contains(@class,'msg-s-message-list-content')]").exists() &&    $x("//ul[contains(@class,'msg-s-message-list-content')]").text().length()>0)
                            ElementsCollection msgs = $$x("//ul[contains(@class,'msg-s-message-list-content')]//li//a[contains(@class,'app-aware-link')]/span");
                            if (!msgs.isEmpty() & !Utils.areAllElementsEqual(msgs)){
//zoho.changeLeadStatus(id, token, transition_id);
                            }
                            else {
                                new PersonPage().msgBtn.click();
                                if (!$x("//div[contains(@aria-label,'Write a message…')]").is(Condition.visible)) continue;
                                $x("//div[contains(@aria-label,'Write a message…')]").click();
                                if (description.equals("null")){
                                    $x("//div[contains(@aria-label,'Write a message…')]").sendKeys("Good day to you.\n" +
                                        "\n" +
                                        "Quick question - have you thought about modernizing the software you are using? It might be a right decision to start the new year with new IT solutions to scale your business. WiseVision will be happy to help you with that. You can check our portfolio and see for yourself that we are the right choice for a technical vendor: https://drive.google.com/file/d/1W6Tiv-zN_D7DsCapvhHo1PGssDmjTTQN/view?usp=share_link\n" +
                                        "\n" +
                                        "We can schedule a quick call if you’re interested. Just let me know when you have free time.\n");}else{
                                    $x("//div[contains(@aria-label,'Write a message…')]").sendKeys(description);
                                };

                                $x("//button[normalize-space()='Send']").click();
                                Thread.sleep(randomResult);
                                $x("//div[contains(@aria-label,'Messaging')]//div[contains(@class,'msg-overlay-bubble-header__controls')]/button[3]").click();
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
    public void sendFolowUpFifthMsg(String name,  String email, String password, String pickList){

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
                        if (status.equals("Not Started") &&  subject.contains("Fifth message") && localDateIsBeforeGivenComparison(duedate)){
                            System.out.println("open " + leadPage + " and sent Fifth message to " + fullName);
                            Selenide.open(leadPage);
                     //      if (   $x("//ul[contains(@class,'msg-s-message-list-content')]").exists() &&    $x("//ul[contains(@class,'msg-s-message-list-content')]").text().length()>0)
                            ElementsCollection msgs = $$x("//ul[contains(@class,'msg-s-message-list-content')]//li//a[contains(@class,'app-aware-link')]/span");
                            if (!msgs.isEmpty() & !Utils.areAllElementsEqual(msgs)){
//zoho.changeLeadStatus(id, token, transition_id);
                            }
                            else {
                                new PersonPage().msgBtn.click();
                                if (!$x("//div[contains(@aria-label,'Write a message…')]").is(Condition.visible)) continue;
                                $x("//div[contains(@aria-label,'Write a message…')]").click();
                                if (description.equals("null")){
                                    $x("//div[contains(@aria-label,'Write a message…')]").sendKeys("Good day to you.\n" +
                                        "\n" +
                                        "Quick question - have you thought about modernizing the software you are using? It might be a right decision to start the new year with new IT solutions to scale your business. WiseVision will be happy to help you with that. You can check our portfolio and see for yourself that we are the right choice for a technical vendor: https://drive.google.com/file/d/1W6Tiv-zN_D7DsCapvhHo1PGssDmjTTQN/view?usp=share_link\n" +
                                        "\n" +
                                        "We can schedule a quick call if you’re interested. Just let me know when you have free time.\n");}else{
                                    $x("//div[contains(@aria-label,'Write a message…')]").sendKeys(description);
                                };

                                $x("//button[normalize-space()='Send']").click();
                                Thread.sleep(randomResult);
                                $x("//div[contains(@aria-label,'Messaging')]//div[contains(@class,'msg-overlay-bubble-header__controls')]/button[3]").click();
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

                {       "Александра - Saudi Arabia Board of directors",
                        "alexandra.sternenko@gmail.com",
                        "asd321qq",
                        "Yurij",
                },
                {       "Маша - Stockholm Founder ",
                        "deynekamariawv@gmail.com",
                        "3N2wbnsw",
                        "Yurij",
                },
                {       "Михайло - Saudi Arabia CFO",
                        "michael.salo1995@gmail.com",
                        "newman1996",
                        "Yurij",
                },
/*                {       "Nikita - Stockholm board of directors",
                        "kni2012@ukr.net",
                        "33222200s",
                        "Hello",

                },*/
                {       "Наталья- Stockholm CEO",
                        "natalia.marcoon@gmail.com ",
                        "33222200Shin",
                        "Valeriia",
                },
                {       "Денис - Saudi Arabia CEO",
                        "basdenisphytontm@gmail.com",
                        "33222200Shin",
                        "Valeriia",
                },
                {       "Настя - Stuttgart CEO",
                        "anastasiiakuntii@gmail.com",
                        "33222200Shin",
                        "Alex",
                },
                {       "Роксолана - Stockholm CFO",
                        "roksolanatrofim@gmail.com ",
                        "89fcmTT88V",
                        "Alex",
                },
                {       "Марьян -  Stockholm CTO",
                        "reshetunmaryanwv@gmail.com",
                        "33222200Shin",
                        "Alex",
                },
                {       "Анастасия - Saudi Arabia owner",
                        "vozniakanastasia52@gmail.com",
                        "33222200Shin",
                        "Valeriia",
                }
        };
    }
}
