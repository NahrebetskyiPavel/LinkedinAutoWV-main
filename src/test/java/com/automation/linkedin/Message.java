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

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.interactable;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static utils.Utils.localDateIsBeforeGivenComparison;

public class Message extends Base{
    SignInPage signInPage = new SignInPage();
    MessagingPage messagingPage = new MessagingPage();
    ZohoCrmHelper zoho = new ZohoCrmHelper();
    private ElementsCollection closeBtns = $$x("//div[contains(@class,'msg-overlay-bubble-header__controls')]//*[contains(@href,'#close-small')]");
    private ElementsCollection pendingBtn = $$x("//button[contains(@aria-label,'Pending')]//span[normalize-space()='Pending']");

    Boolean msgResult;
    String chatLeadStatusid = "421659000006918053";
    int msgsSentCounter = 0;
    int msgsSentCounterMax = 30;

    private String  msg = "Good day to you.\n" +
            "\n" +
            "Quick question - have you thought about modernizing the software you are using? It might be a right decision to start the new year with new IT solutions to scale your business. WiseVision will be happy to help you with that. You can check our portfolio and see for yourself that we are the right choice for a technical vendor: https://drive.google.com/file/d/1W6Tiv-zN_D7DsCapvhHo1PGssDmjTTQN/view?usp=share_link\n" +
            "\n" +
            "We can schedule a quick call if you’re interested. Just let me know when you have free time.\n";
    List<String> accMsgSeconded = new ArrayList<String>();

    @SneakyThrows
    @Test(description = "send FollowUp Msg", dataProvider = "dataProviderPeopleSearch", priority = 1)
    public void senddMsg(String linkedInAccount,  String email, String password){
        msgsSentCounter = 0;

        setupBrowser(true, linkedInAccount);
        openLinkedInLoginPage();
        signInPage.signIn(randomResult, email, password);
        WebDriverRunner.getWebDriver().manage().window().maximize();
        Thread.sleep(10000);
        if ($x("//iframe[@id='captcha-internal']").is(visible)) throw new Exception("captcha");
        String  token = zoho.renewAccessToken();
        System.out.println("=======================");
        System.out.println("START: " +  linkedInAccount);

        System.out.println("=======================");
        sendFolowUpMsg(linkedInAccount, token, "Second automessage");
        sendFolowUpMsg(linkedInAccount, token, "Third automessage");
        sendFolowUpMsg(linkedInAccount, token, "Fourt automessage");
        sendFolowUpMsg(linkedInAccount, token, "Fifth automessage");
        sendFolowUpMsg(linkedInAccount, token, "Seven automessage");
        sendFolowUpMsg(linkedInAccount, token, "Eight automessage");
        sendFolowUpMsg(linkedInAccount, token, "Nine automessage");
        sendFolowUpMsg(linkedInAccount, token, "Ten automessage");
        sendFolowUpMsg(linkedInAccount, token, "FollowUp first automessage");
        sendFolowUpMsg(linkedInAccount, token, "FollowUp second automessage");
        sendFolowUpMsg(linkedInAccount, token, "FollowUp third automessage");
        sendFolowUpMsg(linkedInAccount, token, "FollowUp forth automessage");
        sendFolowUpMsg(linkedInAccount, token, "FollowUp fifth automessage");
        sendFolowUpMsg(linkedInAccount, token, "FollowUp six automessage");

        sendFolowUpMsg(linkedInAccount, token, "Meeting automessage");
        sendFolowUpMsg(linkedInAccount, token, "Final automessage");

    }


    @SneakyThrows
    public void sendFolowUpMsg(String linkedinAccount, String token, String taskName){
        System.out.println("START " + taskName);
        System.out.println("msgsSentCounter =" + msgsSentCounter);
        if (msgsSentCounter > msgsSentCounterMax) {
            if (taskName.contains("Final automessage")) msgsSentCounter= 0;
            return;
        };
        for (int n = 0; n < 100; n++) {
            if (msgsSentCounter > msgsSentCounterMax) break;
            String data =  zoho.getLeadList(token, "Contacted", linkedinAccount, n);
            if (data.isEmpty()) break;
            System.out.println("||==================================================================||");
            JSONObject responseBodyJsonObject = new JSONObject( data );
            //System.out.println(responseBodyJsonObject);
            for (int i = 0; i < responseBodyJsonObject.getJSONArray("data").length(); i++) {
                if (msgsSentCounter > msgsSentCounterMax) break;
                String id = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("id");
                if (responseBodyJsonObject.getJSONArray("data").getJSONObject(i).get("Website") == null) continue;
                if (responseBodyJsonObject.getJSONArray("data").getJSONObject(i).get("Website") == "null") continue;
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
                //System.out.println("tasksData: " + tasksData);
                if (!String.valueOf(tasksData).contains("data")){
                    System.out.println("no data");
                    break;
                }
                //System.out.println(tasksData.getJSONArray("data"));
                //System.out.println("tasksData length:"+tasksData.getJSONArray("data").length());
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
                        if (status.contains("Not Started")  &&  subject.contains(taskName) && localDateIsBeforeGivenComparison(duedate)){
                            System.out.println(taskId);
                            System.out.println(status);
                            System.out.println(subject);
                            Selenide.open(leadPage);
                            Thread.sleep(10000);
                            //if (WebDriverRunner.getWebDriver().getCurrentUrl().contains("404")) continue;
                            //if (pendingBtn.last().is(Condition.visible)) continue;
                            if (closeBtns.size()>0){
                                if (closeBtns.first().is(Condition.visible)){
                                    for (SelenideElement closeBtn:closeBtns
                                         ) {
                                        closeBtn.click();
                                    }
                                }
                            }
                            if ($x("//h2[contains(text(),'This page doesn’t exist')]").is(visible)) continue;
                            if ($x("//h2[contains(text(),'Something went wrong')]").is(visible)) continue;
                            if ($x("//h1[contains(text(),'your account is temporarily restricted')]").is(visible)) {
                                throw new Exception("your account is temporarily restricted");
                            };
                            new PersonPage().msgBtn.click();
                            List<String> msgs = $$x("//ul[contains(@class,'msg-s-message-list-content')]//li//a[contains(@class,'app-aware-link')]/span").texts();
                            if (!Utils.areAllElementsEqual(msgs) && !msg.isEmpty()){
                                // zoho.changeLeadStatus(id, token, chatLeadStatusid);
                                continue;
                            }
                            if ( $("h2[id='upsell-modal-header']").is(Condition.visible)) continue;
                            System.out.println("sent msg!!!");
                            if (msgsSentCounter > msgsSentCounterMax) break;
                            msgsSentCounter = msgsSentCounter+1;
                            System.out.println("msgsSentCounter =" + msgsSentCounter);

                            if (description.contains("null")) {
                                if (accMsgSeconded.contains(fullName)){continue;}
                                accMsgSeconded.add(fullName);
                                msgResult = new PersonPage().sentMsg("Lets go to meeting");
                                if (!msgResult) continue;
                                zoho.changeTaskStatus(token, taskId,"Closed");
                            }
                            else {
                                if (accMsgSeconded.contains(fullName)){continue;}
                                accMsgSeconded.add(fullName);
                                msgResult = new PersonPage().sentMsg(description.replace("NAME",leadName));
                                if (!msgResult) continue;
                                zoho.changeTaskStatus(token, taskId,"Closed");
                            }
                        };
                        if (status.contains("In Progress")  &&  subject.contains(taskName) && localDateIsBeforeGivenComparison(duedate)){
                            System.out.println(taskId);
                            System.out.println(status);
                            System.out.println(subject);
                            if (msgsSentCounter > msgsSentCounterMax) break;
                            Selenide.open(leadPage);
                            Thread.sleep(10000);
                            if (WebDriverRunner.getWebDriver().getCurrentUrl().contains("404")) continue;
                            //if (!$x("//main//span[contains(text(),'Pending')]").is(Condition.visible)) continue;
                            //if (!new PersonPage().msgBtn.is(Condition.visible)) continue;
                            if (new PersonPage().closeBtn.is(interactable)) new PersonPage().closeBtn.click();
                            if (new PersonPage().closeBtn.is(interactable)) new PersonPage().closeBtn.click();
                            if (new PersonPage().closeBtn.is(interactable)) new PersonPage().closeBtn.click();
                            if (!new PersonPage().msgBtn.is(interactable)) continue;

                            new PersonPage().msgBtn.click();
                            Thread.sleep(5000);

                            List<String> msgs = $$x("//ul[contains(@class,'msg-s-message-list-content')]//li//a[contains(@class,'app-aware-link')]/span").texts();
                            if (!Utils.areAllElementsEqual(msgs) && !msg.isEmpty()){
                                // zoho.changeLeadStatus(id, token, chatLeadStatusid);
                                continue;
                            }
                            if ( $("h2[id='upsell-modal-header']").is(Condition.visible)) continue;
                            System.out.println("sent msg!!");
                            if (msgsSentCounter > msgsSentCounterMax) break;

                            msgsSentCounter = msgsSentCounter+1;
                            System.out.println("msgsSentCounter =" + msgsSentCounter);

                            if (description.contains("null")) {
                                msgResult = new PersonPage().sentMsg("Hello how are you doing");
                                if (!msgResult) continue;
                                zoho.changeTaskStatus(token, taskId,"Closed");
                            }
                            else {
                                msgResult = new PersonPage().sentMsg(description.replace("NAME",leadName));
                                if (!msgResult) continue;
                                zoho.changeTaskStatus(token, taskId,"Closed");
                            }
                        };
                    }
                }

                System.out.println();
                System.out.println("\n");
                //System.out.println("====================================================================");
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
                //2
                {       "Natalia Marcun",
                        "natalia.marcoon@gmail.com ",
                        "asd321qq",

                },

                //6
                {       "Anastasiia Kuntii",
                        "anastasiiakuntii@gmail.com",
                        "33222200Shin",

                },
                //7
                {       "Marian Reshetun",
                        "reshetunmaryanwv@gmail.com",
                        "33222200Shin",

                },
                {       "Anastasiia Vozniak",
                        "vozniakanastasia52@gmail.com",
                        "asd2424qq",

                },

                //9
                {       "Maria Deyneka",
                        "deynekamariawv@gmail.com",
                        "3N2wbnsw",
                },

                //11



                //14
                {
                        "Nikita K",
                        "kni2012@ukr.net",
                        "33222200s",
                },
                {
                        "lina Kompanets",
                        "ekompanets02@gmail.com",
                        "35ulurev",
                },




        };
    }
}
