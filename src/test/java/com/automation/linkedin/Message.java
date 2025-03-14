package com.automation.linkedin;

import api.helpers.WiseVisionApiHelper;
import api.helpers.ZohoCrmHelper;
import com.automation.linkedin.pages.PersonPage;
import com.automation.linkedin.pages.login.SignInPage;
import com.automation.linkedin.pages.messaging.MessagingPage;
import com.codeborne.selenide.*;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.StatusChecker;
import utils.Utils;

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
    int msgsSentCounterMax = 20;


    @SneakyThrows
    @Test(description = "send FollowUp Msg", dataProvider = "dataProviderPeopleSearch", priority = 1)
    public void senddMsg(String profileId, String email, String password, String cookie, String linkedInAccount ){

        String  token = zoho.renewAccessToken();
        System.out.println("Acc " + profileId);
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
            if (taskName.contains("Final automessage")) msgsSentCounter = 0;
           System.out.println("fina msgsSentCounter = " + msgsSentCounter);

            return;
        };

        for (int n = 0; n < 1000; n++) {
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
                String leadPage = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Website");
                String fullName = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Full_Name");
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
                            if (msgsSentCounter > msgsSentCounterMax) break;

                            System.out.println("sent msg from " + linkedinAccount);
                            System.out.println("msgsSent= " + msgsSentCounter);
                            msgsSent = msgsSent + 1;
                            msgsSentCounter = msgsSentCounter+1;

                            accsMsgssent.add(fullName);
                            {

                                String msg = description.replace("NAME",leadName).replace("\n","\\n").replace("\r","");
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
                            if (msgsSentCounter > msgsSentCounterMax) break;

                            System.out.println("sent msg from " + linkedinAccount);
                            System.out.println("msgsSent= " + msgsSentCounter);
                            msgsSent = msgsSent + 1;
                            msgsSentCounter = msgsSentCounter+1;

                            accsMsgssent.add(fullName);
                            {

                                String msg = description.replace("NAME",leadName).replace("\n","\\n").replace("\r","");
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



                {       "Evgeny-Gazitov",
                        "evgeny.gazitov8753@outlook.it",
                        "33222200Shin",
                        "AQEDAUsJgSoAC1l8AAABlZRqM5EAAAGVuHa3kU4AygXdRwA21P6p3EiTnEMFsVNmSSUVDiGJZ5nLS6coGdopDQPLqVA-mTKmgM6CKC74NyHISKeIKmR5CcuBvU1Nt5rMHWaY21AEdYWoMZVW9vEZ8ybu|eyJsYW5ndWFnZXMiOlsiaXQtSVQiLCJpdCIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJpdC1JVCIsInRpbWVab25lIjoiRXVyb3BlL1JvbWUiLCJpcCI6IjkzLjcxLjY1LjExNiJ9",
                        "Evgeny Gazitov"
                },

                {       "Johan-Heinlein",
                        "johan.heinlein@outlook.de",
                        "eGdFPRgS",
                        "AQEDAUxEX5oApf6pAAABjglKvk4AAAGU37pv3E0Aj86fZrt57nf9dDQ9L2ycAOFb1RU57UHtsDbugqCEgQI9RWHVyVFlgLNiv9OrCA8Ljw3_6SwfcVDdvLa4sBTiEznok-P7bnKgFpD9l5u9N5ilTHF_|eyJsYW5ndWFnZXMiOlsiZGUtREUiLCJkZSIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJkZS1ERSIsInRpbWVab25lIjoiRXVyb3BlL0JlcmxpbiIsImlwIjoiMzcuMjAxLjE5OS4yMSJ9",
                        "Johan Heinlein"
                },
                {       "Artemio-Chumakov",
                        "artemio.chumakov1981@outlook.it",
                        "33222200Shin",
                        "AQEDAUtCHbcD5C-bAAABkcaWbF4AAAGU37dZ7E0AvbdmKlhCZSBkmNh-AjzXAruOXXZvwJkbpduIywU_XK8rQ9OA7HvxFIWRKos9zQdvCNqVWfhMTSR_HsNJoGlt2gECiowvI-wx55iz52P1Qvy4Z0Tz|eyJsYW5ndWFnZXMiOlsiaXQtSVQiLCJpdCIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJpdC1JVCIsInRpbWVab25lIjoiRXVyb3BlL1JvbWUiLCJpcCI6bnVsbH0=",
                        "Artemio Chumakov"
                },

                {       "patrick-yushko-b2080b2b8",
                        "yushko.patrick@outlook.it",
                        "206GLMC2",
                        "AQEFAHUBAAAAABBl9t0AAAGPfE7YuwAAAZMaRBSVVgAAGHVybjpsaTptZW1iZXI6MTI4MDAxMjQyNMyB0U3N4EXZoT7jRSgrh2ZsRQhw3dIPIjxxs7HK2bI8jIXKrSaNXE-7GhbppvBOQSO2mokFi0nLkNu11TQ3PPReQB8-2boUF0iWaZ7L3W1dFU9X07glDGQPpLsaMRPWQju-eZbs2y2zeE1w8P2PvROcYJDwbJXaXTTxwbFor9oT7iUhsfAU30z5UF7qX0VUHdnorZw",
                        "Yushko Patrick"
                },


                {       "michael-krusciov",
                        "michael.krusciov@outlook.de",
                        "cTsH3KhU",
                        "AQEDAUwy4cUDGdTqAAABlZRq6nIAAAGVuHduck4Aq9VwpW9HFtzimuKz_fRbUlh-GzDaXSWA94rW2NvQhm_FRlVrEclDfyQgoArFaet-NPhtCyNhbTkDVGka8gaWcFzdZ8hdkCjKVkdyduFkdfUCH8zU|eyJsYW5ndWFnZXMiOlsiZGUtREUiLCJkZSIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJkZS1ERSIsInRpbWVab25lIjoiRXVyb3BlL0JlcmxpbiIsImlwIjoiMTg1LjE4NC4yMDIuMTQzIn0=",
                        "Michael Krusciov"
                },


                {       "Anastasiia-Vozniak",
                        "vozniakanastasia52@gmail.com",
                        "asd2424qq",
                        "AQEDATp4HzoBa6O7AAABlGlxPOgAAAGUjX3A6E0AwNpK6n3p3Ulp5DO-BwvtTrsYEBYy9LxAD6xohwUCfLQO52TrKY4CuGewUPx04ho-yh9X6s0CZ_L_uBtMC0MvER8xlAxfO5ENZX--bzIUQuvtufWI|eyJsYW5ndWFnZXMiOlsidWstVUEiLCJ1ayIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJ1ay1VQSIsInRpbWVab25lIjoiRXVyb3BlL0tpZXYiLCJpcCI6IjE4OC4xNjMuMjYuNjkifQ==",
                        "Anastasiia Vozniak"
                },/*
                {       "Art-Stenko",
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
                {       "Nikita-K",
                        "kni2012@ukr.net",
                        "33222200s",
                        "AQEDASE8mKgD8e3SAAABlGmER_UAAAGUjZDL9VYAMzZSaOOCF1F8PcfZQznfhQYTQutnyrKzLmQzv-g0CmX1Nu-ZlsnpsXHketfREZe-Bl_GFzC5dVXFk1iw8Gw5iD5EDxhi5DmPxWTxZCI58QmHNpm8|eyJsYW5ndWFnZXMiOlsidWstVUEiLCJ1ayIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJ1ay1VQSIsInRpbWVab25lIjoiRXVyb3BlL0tpZXYiLCJpcCI6IjE4OC4xNjMuNjkuMzMifQ==",
                        "Nikita K"
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
                        "AQEDASosSSoEwgrNAAABlGmRGiUAAAGUjZ2eJU0AhC51KbwrLYzi4wa2ytQxdxJv4VpDm3awy0TQZEatEjsuQgmEZI5rzK1ANRo6I-kuNg72s-33zhWTr5kS4quQwdpXozxkrjzKyNkESDnlF0vDuas4|eyJsYW5ndWFnZXMiOlsidWstVUEiLCJ1ayIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJ1ay1VQSIsInRpbWVab25lIjoiRXVyb3BlL0tpZXYiLCJpcCI6IjE4OC4xNjMuMjguNTEifQ==",
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

                {       "Matthew-Martinez",
                        "mMartiz11@outlook.it",
                        "metmar11mmjy",
                        "AQEDAUvkzscEuod7AAABjcxZHVEAAAGVZaJUXk0AZtEe_NDTtXzWjNSIhGHwaK6uxLrfIID9n94Trrah5UBV8qZbnsjj0uRerARQuFR5AHFRd3uAGQFE5uMGzGZIzZNmYbG90TRwHOjFjCHsFnQDGI4f|eyJsYW5ndWFnZXMiOlsiaXQtSVQiLCJpdCIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJpdC1JVCIsInRpbWVab25lIjoiRXVyb3BlL1JvbWUiLCJpcCI6Ijg0LjMzLjI1MS4xNzgifQ==",
                        "Matthew Martinez"
                }
        };
    }
}
