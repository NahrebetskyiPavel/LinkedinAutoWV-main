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

    @SneakyThrows
    @Test(description = "send FollowUp Msg", dataProvider = "dataProviderPeopleSearch", priority = 1)
    public void senddMsg(String profileId, String email, String password, String cookie, String linkedInAccount ){

        String  token = zoho.renewAccessToken();


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
        sendFolowUpMsg(linkedInAccount, token, "Meeting automessage", profileId,  email,  password,  cookie );

    }


    @SneakyThrows
    public void sendFolowUpMsg(String linkedinAccount, String token, String taskName, String profileId, String email, String password, String cookie ){
        System.out.println("START " + taskName);
        for (int n = 0; n < 1000; n++) {
            String data =  zoho.getLeadList(token, "Contacted", linkedinAccount, n);
            if (data.contains("INVALID_TOKEN")) {
                token = zoho.renewAccessToken();
                data = zoho.getLeadList(token, "Contacted", linkedinAccount, n);

            }

            if (data.isEmpty()) break;
            System.out.println("||==================================================================||");
            JSONObject responseBodyJsonObject = new JSONObject( data );
            //System.out.println(responseBodyJsonObject);
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
                //System.out.println("tasksData length:"+tasksData.getJSONArray("data").length());
                if (tasksData.getJSONArray("data").length() >0){
                    for (int j = 0; j < tasksData.getJSONArray("data").length(); j++) {
                        System.out.println("==================================================================");
                        String status = tasksData.getJSONArray("data").getJSONObject(j).getString("Status");
                        String subject = tasksData.getJSONArray("data").getJSONObject(j).getString("Subject");
                        String taskId = tasksData.getJSONArray("data").getJSONObject(j).getString("id");
                        String description = String.valueOf(tasksData.getJSONArray("data").getJSONObject(j).get("Description"));
                        String duedate = String.valueOf(tasksData.getJSONArray("data").getJSONObject(j).get("Due_Date"));

                        System.out.println(taskId);
                        System.out.println(status);
                        System.out.println(subject);
                        boolean subjectequalstaskName = subject.contains(taskName);
                        System.out.println("subjectequalstaskName= " + subjectequalstaskName);
                        boolean descriptionEqualsNull = description.contains("null");
                        System.out.println("descriptionEqualsNull= " + descriptionEqualsNull);

                        if (subject.contains(taskName) && status.contains("Not Started")  && localDateIsBeforeGivenComparison(duedate) ){
                            for (String acc : accsMsgssent) {
                                if(acc.matches(fullName)){
                                    break;
                                }
                            }
                            System.out.println("subject " + subject);
                            System.out.println("equals " +subject.contains(taskName));
                            Thread.sleep(10000);
                            System.out.println("sent msg!!!");
                            msgsSent += msgsSent;
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
                                }
                                String taskResults ;
                                try {
                                    taskInfo = wiseVisionApiHelper.impastoGetTaskinfo(profileId, impastoTaskId);
                                    taskResults = String.valueOf(new JSONObject( taskInfo ).getJSONArray("results").getJSONObject(0));
                                } catch (Exception e){
                                    Thread.sleep(60*1000);
                                    taskInfo = wiseVisionApiHelper.impastoGetTaskinfo(profileId, impastoTaskId);
                                    if ((String.valueOf(new JSONObject( taskInfo ).getJSONArray("results")+"").contains("Cookie is not valid"))) throw new Exception("Cookie is not valid");
                                    else  {
                                        throw new Exception(e);

                                    }

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
                        if ( subject.contains(taskName) && status.contains("In Progress")  && localDateIsBeforeGivenComparison(duedate) ) {
                            for (String acc : accsMsgssent) {
                                if(acc.matches(fullName)){
                                    break;
                                }
                            }
                            System.out.println("subject " + subject);
                            System.out.println("equals " +subject.contains(taskName));
                            Thread.sleep(10000);
                            System.out.println("sent msg!!!");
                            msgsSent += msgsSent;

                            accsMsgssent.add(fullName);
                            {
                                String msg = description.replace("NAME",leadName).replace("\n","\\n").replace("\r","");
                                System.out.println(msg);
                                String response = wiseVisionApiHelper.sentMsgImpasto(profileId, email, password, cookie, leadPage, msg);
                                System.out.println("response " + response);
                                Thread.sleep(1000*60);
                                int impastoTaskId = (int) new JSONObject( response ).get("taskId");
                                System.out.println("taskid = " + impastoTaskId);
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
                                try {
                                    statusChecker.waitForStatus("finished", taskStatus, 60000);
                                    System.out.println("Status is now 'finished'.");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                zoho.changeTaskStatus(token, taskId,"Closed");
                            }
                        }
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



                {       "paul-bereza",
                        "paul.bereza02@outlook.de",
                        "33222200Shin",
                        "AQEFAHUBAAAAAA9y_ngAAAGQEc-OggAAAZJrHPFmTgAAGHVybjpsaTptZW1iZXI6MTI2NjM4OTU1MmPdIWQ_rYxSbUskA00ccuc4z22iLxS-DaOZi1BasZ1xpanENw1kxjqll7jjjMvxF4OkBMpOKn4UqQjD-TvaFp-4e0OWq0X6l0gfFic2AOw56efsfRQuGFI95O0ZWtrpPi6W-jI21rbodkCxIiNAp1LBj4tOnfQmpv5Uflxk0p_aDoESUjIEY8P8v17cP6ERz1sM1wI",
                        "Paul Bereza"
                },



                {       "elias-danilov",
                        "elias.danilov@outlook.it",
                        "33222200Shin",
                        "AQEDAUs6XDsBmGroAAABkh9htIUAAAGScUK0VFYAiXEckJH2_AWGNuougsbwxE6a8_wIV233ZjJ8wSNWVU4UycjmMxDQ6DPJQ3mU3dM2XphYdEniE1TQlgioI6y90U1mnibqPYSUVtajeEC3B857x8jG",
                        "Elias Danilov"
                },

                {       "stefania-mykhaylenko",
                        "mykhaylenko.stefania@outlook.fr",
                        "cTsH3KhU",
                        "AQEDAUxQ7yQC6PE_AAABklYraWcAAAGSejftZ00ALnZK_q1t4wsugt-ShEMv0MokIMDUu6kARJE2tqjnebepE-EtFZ5vBMNJdN7dkUYxSfMNRKx1_RXbS7BmQUqyyok5fystsu_sgosIsBvWm4DRLi9E",
                        "Mykhaylenko Stefania"
                },

                {       "den-vaviron",
                        "denVavir00@outlook.de",
                        "33222200Shin",
                        "AQEDAUpubFMFCUCvAAABjP1fQdMAAAGSWAXqB00AQjnJ76-7ZRPsHaXwTKEAr9arpU3dbfo75jEv_8uxAMPkA7fzR0PnqAAfWslJWxnzdC-wPWsV9LATEAW0Xn5yYSkq5tt5rta_cTP5SzMxSiEgxA_S",
                        "Den Vaviron"
                },


                {       "max-mikhaylov",
                        "max.Mikhaylov@outlook.de",
                        "33222200Shin",
                        "AQEDAUtQFBQCjJC7AAABjp8_IKQAAAGSWAV9EE0AqZ8aA6NXTjnzopjEk-6RAHYarurRryUBPwgA_1VSTOLTuH10QOR4AyiMEhdKSby_WImQZKuTUPgIVVxPAsBUM-1mI5H1EJPXXLdyQFj7dYO54lRC",
                        "Max Mikhaylov"
                },

                {       "patrick-yushko-b2080b2b8",
                        "yushko.patrick@outlook.it",
                        "206GLMC2",
                        "AQEFAHUBAAAAABBl9t0AAAGPfE7YuwAAAZJl-qvgVgAAGHVybjpsaTptZW1iZXI6MTI4MDAxMjQyNJGhqrGAw0s-VABnEas4z3_d7E7iUoihe4KC5vripjAlG7xljWADvr3hflDc7vYgfidAug0wkMLkBbnQPWneZ2zMEhxGiMz5asYjyKFVbYclxv-15jUg8vRIm55acrrkbjH66U1zPn1iQ-moQxwe45pAyR2xmK1dIYSJXyXYSBy-a5eos-B7BCV-MORD_QUs9mSx72Y",
                        "Yushko Patrick"
                },

                {       "daniele-tsvetkov",
                        "daniele.tsvetkov@outlook.it",
                        "33222200Shin",
                        "AQEDAUtiZkQEzpptAAABjYItJMIAAAGSZfpK6k0Al67Mz7b7jmM9u4FWm6M86kLQXN3qOmjuXHFL5kwL4nmvymFcMgyI_cZCyqk1Eq0Yfyfdrn516hF_A1zQH48tYI2suw5PzolYy7SMU9KYF_jV_OOp",
                        "Daniele Tsvetkov"
                },
        };
    }
}
