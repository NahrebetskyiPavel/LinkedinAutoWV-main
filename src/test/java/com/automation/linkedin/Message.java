package com.automation.linkedin;

import api.helpers.WiseVisionApiHelper;
import api.helpers.ZohoCrmHelper;
import com.automation.linkedin.pages.PersonPage;
import com.automation.linkedin.pages.login.SignInPage;
import com.automation.linkedin.pages.messaging.MessagingPage;
import com.codeborne.selenide.*;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.testng.annotations.*;
import utils.StatusChecker;
import utils.Utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;
import static utils.Utils.localDateIsBeforeGivenComparison;

public class Message extends Base{
    SignInPage signInPage = new SignInPage();
    MessagingPage messagingPage = new MessagingPage();
    WiseVisionApiHelper wiseVisionApiHelper = new WiseVisionApiHelper();
    ZohoCrmHelper zoho = new ZohoCrmHelper();
    StatusChecker statusChecker = new StatusChecker();
    String chatLeadStatusid = "421659000006918053";
    int leadLow = 20;
    int leadsHigh = 30;
    int leadsRandomResult = random.nextInt(leadsHigh-leadLow) + leadLow;
    ArrayList<String> accsMsgssent = new ArrayList<>();
    private String  msg = "Good day to you.\n" +
            "\n" +
            "Quick question - have you thought about modernizing the software you are using? It might be a right decision to start the new year with new IT solutions to scale your business. WiseVision will be happy to help you with that. You can check our portfolio and see for yourself that we are the right choice for a technical vendor: https://drive.google.com/file/d/1W6Tiv-zN_D7DsCapvhHo1PGssDmjTTQN/view?usp=share_link\n" +
            "\n" +
            "We can schedule a quick call if you’re interested. Just let me know when you have free time.\n";
    int msgsSent = 0;
    ArrayList<Integer> taskIdList = new ArrayList<>();
    int msgsSentCounter = 0;
    int msgsSentCounterMax = 30;



    @SneakyThrows
    @Test(description = "send FollowUp Msg", dataProvider = "dataProviderPeopleSearch", priority = 1)
    public void senddMsg(String profileId, String email, String password, String cookie, String linkedInAccount ){

        String  token = zoho.renewAccessToken();
        System.out.println("Acc " + profileId);
        sendFolowUpMsg(linkedInAccount, token,  "autoDailyOne1", profileId,  email,  password,  cookie );
        if (msgsSent == leadsRandomResult )      return;
        sendFolowUpMsg(linkedInAccount, token,  "Second automessage", profileId,  email,  password,  cookie );
        if (msgsSent == leadsRandomResult )      return;
        sendFolowUpMsg(linkedInAccount, token,  "Third automessage", profileId,  email,  password,  cookie );
        if (msgsSent == leadsRandomResult )      return;
        sendFolowUpMsg(linkedInAccount, token, "Fourt automessage", profileId,  email,  password,  cookie );
        if (msgsSent == leadsRandomResult )      return;
        sendFolowUpMsg(linkedInAccount, token, "Fifth automessage", profileId,  email,  password,  cookie );
        if (msgsSent == leadsRandomResult )      return;
        sendFolowUpMsg(linkedInAccount, token, "Six automessage", profileId,  email,  password,  cookie );
        if (msgsSent == leadsRandomResult )      return;
        sendFolowUpMsg(linkedInAccount, token, "Seven automessage", profileId,  email,  password,  cookie );
        if (msgsSent == leadsRandomResult )      return;
        sendFolowUpMsg(linkedInAccount, token, "Eight automessage", profileId,  email,  password,  cookie );
        if (msgsSent == leadsRandomResult )      return;
        sendFolowUpMsg(linkedInAccount, token, "Nine automessage", profileId,  email,  password,  cookie );
        if (msgsSent == leadsRandomResult )      return;
        sendFolowUpMsg(linkedInAccount, token, "Ten automessage", profileId,  email,  password,  cookie );
        if (msgsSent == leadsRandomResult )      return;
        sendFolowUpMsg(linkedInAccount, token, "FollowUp first automessage", profileId,  email,  password,  cookie );
        if (msgsSent == leadsRandomResult )      return;
        sendFolowUpMsg(linkedInAccount, token, "FollowUp second automessage", profileId,  email,  password,  cookie );
        if (msgsSent == leadsRandomResult )      return;
        sendFolowUpMsg(linkedInAccount, token, "FollowUp third automessage", profileId,  email,  password,  cookie );
        if (msgsSent == leadsRandomResult )      return;
        sendFolowUpMsg(linkedInAccount, token, "FollowUp forth automessage", profileId,  email,  password,  cookie );
        if (msgsSent == leadsRandomResult )      return;
        sendFolowUpMsg(linkedInAccount, token, "FollowUp fifth automessage", profileId,  email,  password,  cookie );
        if (msgsSent == leadsRandomResult )      return;
        sendFolowUpMsg(linkedInAccount, token, "FollowUp six automessage", profileId,  email,  password,  cookie );
        if (msgsSent == leadsRandomResult )      return;
       // sendFolowUpMsg(linkedInAccount, token, "Meeting automessage", profileId,  email,  password,  cookie );
        sendFolowUpMsg(linkedInAccount, token, "Final automessage", profileId,  email,  password,  cookie );

    }


    @SneakyThrows
    public void sendFolowUpMsg(String linkedinAccount, String token, String taskName, String profileId, String email, String password, String cookie ){
        System.out.println("START " + taskName);



        if (msgsSentCounter > msgsSentCounterMax) {
            msgsSentCounter = 0;
            throw new Exception("msg limit" + linkedinAccount);

        };

        for (int n = 0; n < 1000; n++) {
            if (msgsSentCounter > msgsSentCounterMax) {
                msgsSentCounter = 0;
                throw new Exception("msg limit" + linkedinAccount);
            };
            String data =  zoho.getLeadList(token, "Contacted", linkedinAccount, n);
            if (data.contains("INVALID_TOKEN")) {
                token = zoho.renewAccessToken();
                data = zoho.getLeadList(token, "Contacted", linkedinAccount, n);

            }

            if (data.isEmpty()) break;
            //System.out.println("||==================================================================||");
            JSONObject responseBodyJsonObject = new JSONObject( data );
           // System.out.println(responseBodyJsonObject);
            Thread.sleep(10000);
            //System.out.println(responseBodyJsonObject.getJSONArray("data").length());
            for (int i = 0; i < responseBodyJsonObject.getJSONArray("data").length(); i++) {
                String id = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("id");
//                String leadPage = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Website");
                String leadPage;
                try {
                     leadPage = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Website");
                }catch (Exception e){
                    System.out.println("JSONException occurred for index " + id + ". Skipping this entry.");
                    continue;
                }
                String fullName = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Full_Name").replace("?","");
                String[] fullNameArr = fullName.split(" ");
                String leadName = fullNameArr[0];
                System.out.println(id);
                System.out.println(fullName);
                System.out.println(leadPage);
                String tasks = zoho.getLeadTaskList(id, token);
                if (tasks.contains("INVALID_TOKEN")) {
                    token = zoho.renewAccessToken();
                    tasks = zoho.getLeadTaskList(id, token);

                }
                if (tasks.isEmpty()) continue;
                while (true){
                    if (tasks.contains("{\"data\":[{")) break;
                    tasks = zoho.getLeadTaskList(id, token);
                    //System.out.println(tasks);
                    if (tasks.contains("INVALID_TOKEN")) {
                        token = zoho.renewAccessToken();
                        tasks = zoho.getLeadTaskList(id, token);

                    }

                    Thread.sleep(10*1000);

                }
                JSONObject tasksData = new JSONObject( tasks );
                //System.out.println(tasksData.getJSONArray("data"));
               // System.out.println("tasksData length:"+tasksData.getJSONArray("data").length());
                if (tasksData.getJSONArray("data").length() >0){
                    for (int j = 0; j < tasksData.getJSONArray("data").length(); j++) {
                        //System.out.println("==================================================================");
                        String status = tasksData.getJSONArray("data").getJSONObject(j).getString("Status");
                        String subject = tasksData.getJSONArray("data").getJSONObject(j).getString("Subject");
                        String taskId = tasksData.getJSONArray("data").getJSONObject(j).getString("id");
                        String description = String.valueOf(tasksData.getJSONArray("data").getJSONObject(j).get("Description"));
                        String duedate = String.valueOf(tasksData.getJSONArray("data").getJSONObject(j).get("Due_Date"));

                        System.out.println(taskId);
                        System.out.println(status);
                        System.out.println(subject);
                        System.out.println("duedate: " + duedate);
                        boolean subjectequalstaskName = subject.contains(taskName);
                        System.out.println("subjectequalstaskName= " + subjectequalstaskName);
                        boolean descriptionEqualsNull = description.contains("null");
                        System.out.println("descriptionEqualsNull= " + descriptionEqualsNull);

                        if (subject.contains(taskName/* + " from " + linkedinAccount*/) && status.contains("Not Started")  && localDateIsBeforeGivenComparison(duedate) ){
                            for (String acc : accsMsgssent) {
                                if(acc.matches(fullName)){
                                    break;
                                }
                            }
                            System.out.println("subject " + subject);
                            System.out.println("equals " +subject.contains(taskName));
                            Thread.sleep(10000);
                            if (msgsSentCounter > msgsSentCounterMax) {
                                msgsSentCounter = 0;
                                throw new Exception("msg limit" + linkedinAccount);
                            };

                            System.out.println("sent msg from " + linkedinAccount);
                            System.out.println("msgsSent= "+ linkedinAccount + " " + msgsSentCounter);
                            msgsSent = msgsSent + 1;
                            msgsSentCounter = msgsSentCounter+1;

                            accsMsgssent.add(fullName);
                            {

                                String msg = description.replace("NAME",leadName).replace("\n","\\n").replace("\r","");
                                System.out.println("leadPage" + leadPage);
                                System.out.println(msg);
                                String response = wiseVisionApiHelper.sentMsgImpasto(profileId, email, password, cookie, leadPage, msg);
                                System.out.println("response " + response);
                                Thread.sleep(1000*60);
                                int impastoTaskId = (int) new JSONObject( response ).get("taskId");
                                String taskInfo = wiseVisionApiHelper.impastoGetTaskinfo(profileId, impastoTaskId);

                                while (true){
                                    Thread.sleep( 60 * 1000);
                                    taskInfo = wiseVisionApiHelper.impastoGetTaskinfo(profileId, impastoTaskId);
                                    if (taskInfo.contains("finished")) break;
                                    if (taskInfo.contains("failed")) break;
                                    System.out.println(taskInfo);
                                }
                                String taskResults = "";
                                try {
                                    taskInfo = wiseVisionApiHelper.impastoGetTaskinfo(profileId, impastoTaskId);
                                    taskResults = String.valueOf(new JSONObject( taskInfo ).getJSONArray("results").getJSONObject(0));
                                    if      ( taskResults.contains("error") ) {
                                        msgsSentCounter = 0;
                                        throw new Exception(taskResults + "\n" + linkedinAccount);
                                    }

                                } catch (Exception e){
                                    Thread.sleep(60*1000);
                                    taskInfo = wiseVisionApiHelper.impastoGetTaskinfo(profileId, impastoTaskId);
                                    if      ( taskInfo .contains("Cookie is not valid") ) throw new Exception("Cookie is not valid "  +linkedinAccount);
                                    if      ( taskInfo.contains("Page did not loaded completely") ) continue;
                                    if      ( taskInfo.contains("Profile entity URN not found") ) continue;
                                    if      ( taskInfo.contains("404 page not found") ) continue;
                                    if      ( taskInfo.contains("socket hang up") ) continue;
                                    if      ( taskInfo.contains("Сould not open the messenger") ) continue;
                                    if      ( taskInfo.contains("end of central directory record signature not found") ) continue;
                                    if      ( taskInfo.contains("Can not send message to profile. Send message button is not found") ) continue;
                                    else if (taskInfo.contains("Request failed with status code 59")) {Thread.sleep(1000*60*10); continue;}
                                    else if (taskInfo.contains("Navigation timeout of 30000 ms exceeded")) { continue;}
                                    else if (taskInfo.contains("write EPROTO")) {
                                        msgsSentCounter = 0;
                                        throw new Exception("write EPROTO proxy err "  +linkedinAccount);
                                    }
                                    else if (taskInfo.contains("is currently locked")) {
                                        msgsSentCounter = 0;
                                        throw new Exception("Profile" +linkedinAccount+ " is currently locked");
                                    }
                                    else if (taskInfo.contains("Profile Johan-Heinlein is currently locked")) {
                                        msgsSentCounter = 0;
                                        throw new Exception("Profile "+linkedinAccount+" is currently locked");
                                    }
                                    if      ( taskResults.contains("error") ) {
                                        msgsSentCounter = 0;
                                        throw new Exception(taskResults + "\n" + linkedinAccount);
                                    }
                                    else {
                                        msgsSentCounter = 0;
                                        throw new Exception(
                                            taskInfo + "\n\n\n\n" +
                                            String.valueOf(new JSONObject( taskInfo ).getJSONArray("results").getJSONObject(0)) + " "+  linkedinAccount+
                                            "\n\n\n\n" + e); }
                                }

                                System.out.println("taskid = " + impastoTaskId);
                                String taskStatus = new JSONObject( wiseVisionApiHelper.impastoGetTaskinfo(profileId, impastoTaskId) ).getString("status");
                                System.out.println("taskStatus " + taskStatus);
                                if (taskStatus.contains("new")) {
                                    Thread.sleep(30000);
                                    taskStatus = new JSONObject( wiseVisionApiHelper.impastoGetTaskinfo(profileId, impastoTaskId) ).getString("status");
                                    System.out.println("taskStatus " + taskStatus);

                                }
                                if (taskResults.contains("error") && taskResults.contains("Invalid url")) continue;

                                try {
                                    statusChecker.waitForStatus("finished", taskStatus, 60000);


                                    System.out.println("Status is now 'finished'.");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                zoho.changeTaskStatus(token, taskId,"Closed");
                            }
                        }
                        if (subject.contains(taskName/* + " from " + linkedinAccount*/) && status.contains("In Progress")  && localDateIsBeforeGivenComparison(duedate) ){
                            for (String acc : accsMsgssent) {
                                if(acc.matches(fullName)){
                                    break;
                                }
                            }
                            System.out.println("subject " + subject);
                            System.out.println("equals " +subject.contains(taskName));
                            Thread.sleep(10000);
                            if (msgsSentCounter > msgsSentCounterMax) {
                                msgsSentCounter = 0;
                                throw new Exception("msg limit" + linkedinAccount);
                            };

                            System.out.println("sent msg from " + linkedinAccount);
                            System.out.println("msgsSent= "+ linkedinAccount + " " + msgsSentCounter);
                            msgsSent = msgsSent + 1;
                            msgsSentCounter = msgsSentCounter+1;

                            accsMsgssent.add(fullName);
                            {

                                String msg = description.replace("NAME",leadName).replace("\n","\\n").replace("\r","");
                                System.out.println("leadPage: "+leadPage);
                                System.out.println(msg);
                                String response = wiseVisionApiHelper.sentMsgImpasto(profileId, email, password, cookie, leadPage, msg);
                                System.out.println("response " + response);
                                Thread.sleep(1000*60);
                                int impastoTaskId = (int) new JSONObject( response ).get("taskId");
                                String taskInfo = wiseVisionApiHelper.impastoGetTaskinfo(profileId, impastoTaskId);

                                while (true){
                                    Thread.sleep( 60 * 1000);
                                    taskInfo = wiseVisionApiHelper.impastoGetTaskinfo(profileId, impastoTaskId);
                                    if (taskInfo.contains("finished")) break;
                                    if (taskInfo.contains("failed")) break;
                                    System.out.println(taskInfo);
                                }
                                String taskResults ;
                                try {
                                    taskInfo = wiseVisionApiHelper.impastoGetTaskinfo(profileId, impastoTaskId);
                                    taskResults = String.valueOf(new JSONObject( taskInfo ).getJSONArray("results").getJSONObject(0));

                                } catch (Exception e){
                                    Thread.sleep(60*1000);
                                    taskInfo = wiseVisionApiHelper.impastoGetTaskinfo(profileId, impastoTaskId);
                                    if      ( taskInfo .contains("Cookie is not valid") ) throw new Exception("Cookie is not valid");
                                    else if (taskInfo.contains("Request failed with status code 59")) {Thread.sleep(1000*60*10); continue;}
                                    else if (taskInfo.contains("Navigation timeout of 30000 ms exceeded")) { continue;}
                                    else if (taskInfo.contains("write EPROTO")) { throw new Exception("write EPROTO proxy err");}
                                    else { throw new Exception(
                                            taskInfo + "\n\n\n\n" +
                                            String.valueOf(new JSONObject( taskInfo ).getJSONArray("results").getJSONObject(0)) +
                                            "\n\n\n\n" + e); }
                                }

                                System.out.println("taskid = " + impastoTaskId);
                                System.out.println("acc = " + linkedinAccount);
                                String taskStatus = new JSONObject( wiseVisionApiHelper.impastoGetTaskinfo(profileId, impastoTaskId) ).getString("status");
                                System.out.println("taskStatus " + taskStatus);
                                if (taskStatus.contains("new")) {
                                    Thread.sleep(30000);
                                    taskStatus = new JSONObject( wiseVisionApiHelper.impastoGetTaskinfo(profileId, impastoTaskId) ).getString("status");
                                    System.out.println("taskStatus " + taskStatus);

                                }
                                if (taskResults.contains("error") && taskResults.contains("Invalid url")) continue;

                                try {
                                    statusChecker.waitForStatus("finished", taskStatus, 60000);


                                    System.out.println("Status is now 'finished'.");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                zoho.changeTaskStatus(token, taskId,"Closed");
                            }
                        }
                        //Fourt automessageArt Stenko
/*
                        if (subject.contains(taskName*/
/* + " from " + linkedinAccount*//*
) && status.contains("In Progress")  && localDateIsBeforeGivenComparison(duedate) ) {
                            for (String acc : accsMsgssent) {
                                if(acc.matches(fullName)){
                                    break;
                                }
                            }
                            System.out.println("subject " + subject);
                            System.out.println("equals " +subject.contains(taskName));
                            Thread.sleep(10000);
                            if (msgsSentCounter > msgsSentCounterMax) break;
                            System.out.println("sent msg from " + linkedinAccount);

                            System.out.println("msgsSent= " + msgsSent);
                            msgsSent += msgsSent;
                            msgsSentCounter = msgsSentCounter+1;

                            accsMsgssent.add(fullName);
                            {
                                String msg = description.replace("NAME",leadName).replace("\n","\\n").replace("\r","");
                             System.out.println(msg);
                                String response = wiseVisionApiHelper.sentMsgImpasto(profileId, email, password, cookie, leadPage, msg);
                         System.out.println("response " + response);
                                Thread.sleep(1000*60);
                                int impastoTaskId = (int) new JSONObject( response ).get("taskId");

                                System.out.println("taskid = " + impastoTaskId);
                                taskIdList.add(impastoTaskId);
                                for (int task:taskIdList
                                     ) {
                          System.out.println(task);
                                }
                                String taskStatus = new JSONObject( wiseVisionApiHelper.impastoGetTaskinfo(profileId, impastoTaskId) ).getString("status");
                                if (taskStatus.contains("new")) {
                                    Thread.sleep(30000);
                                    taskStatus = new JSONObject( wiseVisionApiHelper.impastoGetTaskinfo(profileId, impastoTaskId) ).getString("status");
                                    System.out.println("taskStatus " + taskStatus);

                                }
                                while (true){
                                    Thread.sleep( 60 * 1000);
                                    if (taskStatus.contains("finished")) break;
                                    if (taskStatus.contains("failed")) break;
                                    taskStatus = new JSONObject( wiseVisionApiHelper.impastoGetTaskinfo(profileId, impastoTaskId) ).getString("status");
                                }
                                    System.out.println("taskStatus " + taskStatus);
                                if      ( wiseVisionApiHelper.impastoGetTaskinfo(profileId, impastoTaskId).contains("Cookie is not valid") ) throw new Exception("Cookie is not valid");
                                try {
                                    statusChecker.waitForStatus("finished", taskStatus, 60000);
                                    System.out.println("Status is now 'finished'.");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                zoho.changeTaskStatus(token, taskId,"Closed");
                            }
                        }
*/
                    }
                }

            }


        }

    }

    @DataProvider(name = "dataProviderPeopleSearch", parallel=true)
    public static Object[][] dataProviderPeopleSearch() {
        return new Object[][]{



                {       "Anastasiia-Vozniak",
                        "vozniakanastasia52@gmail.com",
                        "asd2424qq",
                        "AQEDATp4HzoBa6O7AAABlGlxPOgAAAGXEbOOrE0APHvoDhd1VxScrrvYy4ZDdj9v37FoXjTV3W7TDIALap-vw4XXA0iXfeHLh1WDh4vgOp9sW3xHZuEkOq70yIWaUTxkMgvBkCcKOb8ECNFQtQ4Q4HWk|eyJsYW5ndWFnZXMiOlsiZGUtREUiLCJkZSIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJkZS1ERSIsInRpbWVab25lIjoiRXVyb3BlL0JlcmxpbiIsImlwIjoiMTg4LjI0NS4xOTkuMjA1In0=",
                        "Anastasiia Vozniak"
                },
      /*          {       "Art-Stenko",
                        "artstenko@gmail.com",
                        "GOgoCyclone_11",
                        "AQEDAR1-TWgCdaXbAAABlGmXZtsAAAGU71o8iU0AX2vCEj5S0ms2kzRxYBRPWlb-BlkleFw59RilRgTGUZKEKeA3LHcXC5v2PEGQ4htj0m20Zgs8vTv6FxAjj6ZwLwCao7rehPhPpbiaXZbxwkBFGHGs|eyJsYW5ndWFnZXMiOlsiZGUtREUiLCJkZSIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJkZS1ERSIsInRpbWVab25lIjoiRXVyb3BlL0JlcmxpbiIsImlwIjoiMTg4LjI0NS4xOTkuMjA1In0=",
                        "Art Stenko"
                },*/
                {       "lina-Kompanets",
                        "ekompanets02@gmail.com",
                        "35ulurev",
                        "AQEDASj3SfwEGLIvAAABlGmC3Y4AAAGU70nIQk0Ai4A6U01EkeKnuSgP56RlxVoG-olHOnD8HPJtTuBozbjz6UZCSih9CDJB8pukeO7YCWMq24saiShfxJkZyoo_ZtfaW5TZm_l29SXGTBWvz5JSGdgZ|eyJsYW5ndWFnZXMiOlsiZGUtREUiLCJkZSIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJkZS1ERSIsInRpbWVab25lIjoiRXVyb3BlL0JlcmxpbiIsImlwIjoiMTg4LjI0NS4xOTkuMjA1In0=",
                        "lina Kompanets"
                },
                {       "Maria-Deyneka",
                        "deynekamariawv@gmail.com",
                        "3N2wbnsw",
                        "AQEDATpgt8MFzfWtAAABlGmMmFwAAAGUjZkcXE0AvlevlyzCIVWISv_-5GXi67C29LB7IMZOc9D-uWJDSDvBtN_p16tzzEquHGNEL-C4ct8uXrUwfsbpeY4tDJ2IqPq83BQfohaEMNtZrSAr8KWxPqlw|eyJsYW5ndWFnZXMiOlsidWstVUEiLCJ1ayIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJ1ay1VQSIsInRpbWVab25lIjoiRXVyb3BlL0tpZXYiLCJpcCI6IjE3OC4xMzYuMzcuMjE5In0=",
                        "Maria Deyneka"
                },
                {       "Marian-Reshetun",
                        "reshetunmaryanwv@gmail.com",
                        "33222200Shin",
                        "AQEDATpm9GsDKEL5AAABlGmOfpAAAAGU_02FZE0ALTKPrP2OgK7G6YxUJw3HzHzxtFy65g0LZreuBTXCZ_7dufkRTICalYOnUuAkHioX3kupbIyjpHnuLlB2ML8h7MX7BLxvoRhl7K9IOQ5bmWjI2z6H|eyJsYW5ndWFnZXMiOlsiZGUtREUiLCJkZSIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJkZS1ERSIsInRpbWVab25lIjoiRXVyb3BlL0JlcmxpbiIsImlwIjoiMTg4LjI0NS4xOTkuMjA1In0=",
                        "Marian Reshetun"
                },
                {       "Anastasiia-Kuntii",
                        "anastasiiakuntii@gmail.com",
                        "33222200Shin",
                        "AQEDASosSSoEwgrNAAABlGmRGiUAAAGWFJ_Oyk0AVf9s64tNXp9utdzLLtA4-RfY7IQGKOj1WaK53_a9RUWEukQPl5ikFxiNr4od_4czY7qH_8HLdSJokHhqNdo4vLZr3mjQcpFnA9LchRzOZyoB8t6F|eyJsYW5ndWFnZXMiOlsiZGUtREUiLCJkZSIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJkZS1ERSIsInRpbWVab25lIjoiRXVyb3BlL0JlcmxpbiIsImlwIjoiMTg4LjI0NS4xOTkuMjA1In0=",
                        "Anastasiia Kuntii"
                },
                {       "Natalia-Marcun",
                        "natalia.marcoon@gmail.com",
                        "asd321qq",
                        "AQEDATxzPCcDSaa2AAABlGmTliAAAAGUjaAaIE4AWy01oPscxLvE1AGPoHL1b-BM9xTko4B66dc5mgq9BXLfVA3_PgVtwp5_zNEEtAKpvant2d58dlQWprhSe1W83oGvH82--WTubb20UvdZPxeONqCd|eyJsYW5ndWFnZXMiOlsidWstVUEiLCJ1ayIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJ1ay1VQSIsInRpbWVab25lIjoiRXVyb3BlL0tpZXYiLCJpcCI6IjE4OC4xNjMuNjUuMTk0In0=",
                        "Natalia Marcun"
                },
                {       "Aleksandra-Sternenko",
                        "alexandra.sternenko@gmail.com",
                        "asd321qq",
                        "AQEDATxvso0AsoGrAAABlGmcpMYAAAGUjakoxk0At3rdIcYwg58nfPB100bIp55gaun_CLHbmRhw-J9lsRGXjBUy8peYKS15_zVsmeIbGpBPQzmETdRZntEpa4d7CQxIDcAy5Cn7m0nPv-__V08N5W8L|eyJsYW5ndWFnZXMiOlsidWstVUEiLCJ1ayIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJ1ay1VQSIsInRpbWVab25lIjoiRXVyb3BlL0tpZXYiLCJpcCI6IjE4OC4xNjMuODIuMTgxIn0=",
                        "Aleksandra Sternenko"
                },



                {       "Danylo-Lytvyn",
                        "wisevision.beast@gmail.com",
                        "1171534Oli35Wisew",
                        "AQEDATP_TCAAuywlAAABldbcruUAAAGXsScUFk0AOc7inBUneOcbMGoBD8W00yoN_anzd0g-zqmqSrS2sJBs5yB1aWk9RlgCgm1rD04cWaqlxAp_uV2ZP1L1AyNzg5GvvDkI1FKkLLaUV1HEUxgw39G5|eyJsYW5ndWFnZXMiOlsiZGUtREUiLCJkZSIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJkZS1ERSIsInRpbWVab25lIjoiRXVyb3BlL0JlcmxpbiIsImlwIjoiMTg4LjI0NS4xOTkuMjA1In0=",
                        "Danylo Lytvyn"
                },
                {       "Oksana-Dovhan",
                        "wisevision.office@gmail.com",
                        "33222200Shin",
                        "AQEDAUAUTMkEs1LVAAABmDc1ZWIAAAGYW0HpYk0AlH2UqwO74wBOuB9tLdbJN5fb2co3E4Kae_5tofOEspzM4miib5HlDRlgtpnn8dRgsYp_H2x-OR73I6dFdIbyFF8Uo0s1OsCkI0lMok7KC3Y1bxUS|eyJsYW5ndWFnZXMiOlsiZGUtREUiLCJkZSIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJkZS1ERSIsInRpbWVab25lIjoiRXVyb3BlL0JlcmxpbiIsImlwIjoiMTg4LjI0NS4xOTkuMjA1In0=",
                        "Oksana Dovhan"
                },

/*
                {       "Nikita-K",
                        "kni2012@ukr.net",
                        "33222200s",
                        "AQEFAHQBAAAAABaVI1cAAAGXpvlluAAAAZh5_xgdTgAAF3VybjpsaTptZW1iZXI6NTU3NjE5MzY4NzaKh4x7AWz29T-hnPdkRfmTRKL-4re_ezvssfq6bXTqVsAIi8_vCIq-JgcpuGx4HcH9gDdPBju2L0OpkY2H3ELL78H_dawKYmZ6UVUquNXjAzf1U_i-jBE2jzsvXGKz-xCFX-aG-kRjJMm6ud2X_Jm24UyLACOx4DAsPvlZmAphJCxKIzJAiNCTMb27_XD3uYJfWQ|eyJsYW5ndWFnZXMiOlsiZGUtREUiLCJkZSIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJkZS1ERSIsInRpbWVab25lIjoiRXVyb3BlL0JlcmxpbiIsImlwIjoiMTg4LjI0NS4xOTkuMjA1In0=",
                        "Nikita K"
                }*/

        };
    }
}
