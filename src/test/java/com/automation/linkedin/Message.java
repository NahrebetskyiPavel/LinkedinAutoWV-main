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
            String data =  zoho.getLeadList(token);
            if (data.contains("INVALID_TOKEN")) {
                token = zoho.renewAccessToken();
                data = zoho.getLeadList(token);

            }

            if (data.isEmpty()) break;
            System.out.println("||==================================================================||");
            JSONObject responseBodyJsonObject = new JSONObject( data );
            //System.out.println(responseBodyJsonObject);
            Thread.sleep(10000);
            //System.out.println(responseBodyJsonObject.getJSONArray("data").length());
            for (int i = 0; i < responseBodyJsonObject.getJSONObject("drilldown_data_map").getJSONObject("data_map").length(); i++) {
                String id = responseBodyJsonObject.getJSONObject("drilldown_data_map")
                        .getJSONObject("data_map")
                        .getJSONObject("T")
                        .getJSONArray("rows")
                        .getJSONObject(0)
                        .getJSONArray("cells")
                        .getJSONObject(0)
                        .getString("value");
                JSONObject responseBodyJsonObjectLeadInfo = new JSONObject( zoho.getLeadInfoById(token,id) );

                String leadPage = responseBodyJsonObjectLeadInfo.getJSONArray("data").getJSONObject(0).getString("Website");
                String fullName = responseBodyJsonObjectLeadInfo.getJSONArray("data").getJSONObject(0).getString("Full_Name");;
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
                        boolean subjectequalstaskName = subject.equals(taskName);
                        System.out.println("subjectequalstaskName= " + subjectequalstaskName);
                        boolean descriptionEqualsNull = description.equals("null");
                        System.out.println("descriptionEqualsNull= " + descriptionEqualsNull);

                        if (subject.equals(taskName) && status.equals("Not Started")  && localDateIsBeforeGivenComparison(duedate) ){
                            for (String acc : accsMsgssent) {
                                if(acc.matches(fullName)){
                                    break;
                                }
                            }
                            System.out.println("subject " + subject);
                            System.out.println("equals " +subject.equals(taskName));
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
                                if (taskStatus.equals("new")) {
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
                        if ( subject.equals(taskName) && status.equals("In Progress")  && localDateIsBeforeGivenComparison(duedate) ) {
                            for (String acc : accsMsgssent) {
                                if(acc.matches(fullName)){
                                    break;
                                }
                            }
                            System.out.println("subject " + subject);
                            System.out.println("equals " +subject.equals(taskName));
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
                                if (taskStatus.equals("new")) {
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



           /*     {       "paul-bereza",
                        "paul.bereza02@outlook.de",
                        "33222200Shin",
                        "AQEFAHUBAAAAAA9y_ngAAAGQEc-OggAAAZH516JITQAAGHVybjpsaTptZW1iZXI6MTI2NjM4OTU1MrgmxeadesHM3nKCxrsUsEY7oE2YNlEjDfKcRC4i0jdJYv-0CrELcWvwvgl9Hh_a-UD3BztRMFXG1L-T27TKo8wdUft7g5Rp9vRBKL8EpeN7XvfJL9trBE0Lmp5EWDf6VOBX2WPUshBn-0VfCxKzHXg_8-XsjxcgwJbEGisHf6F8LtRJP1pWyHm0_3_XD2ubXhq_AIM",
                        "Paul Bereza"
                },*/
                {       "margit-matthes",
                        "margit.Matthes@outlook.de",
                        "33222200Shin",
                        "AQEDAUuampkDA9uSAAABjzNCPogAAAGSI-3KcFYAHIw-5yvhTHbx82fm6vv-Y1Mi2sy14qhV9Bgji2cTMx688YRW7xm3m27UthuyFYBwP_3bm3-xx4HvMrrE6SNFHyA_mxhJMohblRI1ujIXQuNSsb-r",
                        "Margit Matthes"
                },

/*
                {       "petr-2",
                        "petr.degtyarev@outlook.de",
                        "33222200Shin",
                        "AQEFAHUBAAAAABBY1JQAAAGQgeLMcgAAAZH_lmMlVgAAGHVybjpsaTptZW1iZXI6MTI2MjA1NjA0Mo7Rm-6Lv2jKPiEOniFR7kwmwFMDkkELi6Xq23khyqad9CW6cCHQRvrKsa-CfDwVTfn5KN8RrniutOJY2hG7DUV-XqHusf1TG82sF9x3NJQlPteaP411vI9CSxoJ14lwT1Rv3R_8wmXDiHCzWY1sl3aK2hqGnPRXj0J2lqzcr0tj8Jb_sAuXiOkWWyFlXeDmCRezMqc",
                        "Petr Degtyarev"
                },



                {       "elias-danilov",
                        "elias.danilov@outlook.it",
                        "33222200Shin",
                        "AQEDAUs6XDsA_omAAAABkgbDT_AAAAGSKs_T8E4Ax5IJY1QH5zDLvpN6s3PWGutRUNTsQ7UGu9igp0XiOsmwHcFE2b0kn7DRTQZ0B_BrVpwh5IsFKPcyIHW6L9QFSsUjvthPZGV9rWz5YSx3YRxxFgP_",
                        "Elias Danilov"
                },

                {       "stefania-mykhaylenko",
                        "mykhaylenko.stefania@outlook.fr",
                        "cTsH3KhU",
                        "AQEDAUxQ7yQDQhhBAAABkgbElqkAAAGSKtEaqU0AcdXQ1OhCbTBKwRpRGBe3nedYojQiyuXYxLU-WUkFKUAsqfN-lNV6_q5yIgu3PbJp6R9-94CbWo6PeAi8v5g6bOA5KmCtinnMcVKtolW7A3SpYekO",
                        "Mykhaylenko Stefania"
                },

                {       "eliza-kolner",
                        "eliza.kolner0103@outlook.de",
                        "ek03303KK",
                        "AQEFAHUBAAAAAA9zBMEAAAGQEbVkiAAAAZH50MUHVgAAGHVybjpsaTptZW1iZXI6MTI4MDM3NTY4NRc0s7DQyLuYWxFXgtWaqFEZefsqBI2BQD9Ksw1A6v09TXzySsQt9gPEAPFTDL-Rdj8tK4m_bAB7Sxu_D8pWyxkE5bqFrEcwyCxLFNz4dF4wXqpf1sipR_bdQv50ZayzOaNukuM0pELAn5PThQ0NrjuQo1GIRHNqMCZZZCp81QVc2ag1WMRG7D528Fe8p7fG_7BG268",
                        "Eliza Kolner"
                },

                {       "den-vaviron",
                        "denVavir00@outlook.de",
                        "33222200Shin",
                        "AQEDAUpubFMFCUCvAAABjP1fQdMAAAGR-fnzok0AzNuSfrVzxRYk5aDPZHgaZCY4_ZBMCwjepDh8OY9qpZM_SXXqcTe91I0zKSPVjAjtcErM0xofUHNyDE2NPSG8dTy0omlQYIiEdkgOYqnrFVL90RH3",
                        "Den Vaviron"
                },


                {       "max-mikhaylov",
                        "max.Mikhaylov@outlook.de",
                        "33222200Shin",
                        "AQEDAUtQFBQCjJC7AAABjp8_IKQAAAGR-fov4E0AXlpi8tDUWl7Z_UhAIUsezBUK2b8L9CnWvpTpkfAsGhzqiFKYmjw6sW6ySHaMXcEM6BlLIHm-o5QnPpMgZLWKyfP14WJvllHMLRkY2Mre-tWbjDfB",
                        "Max Mikhaylov"
                },

                {       "patrick-yushko-b2080b2b8",
                        "yushko.patrick@outlook.it",
                        "206GLMC2",
                        "AQEFAHUBAAAAABBl9t0AAAGPfE7YuwAAAZIFUyztTQAAGHVybjpsaTptZW1iZXI6MTI4MDAxMjQyNIw_zJSyXQaqYm87bAibaWY_M_FQ4Bg-5CUL28zN8lUQPFyF00v6V0cuP6t9kbmFpAjCLTAH9-mmoyuJdjF-4ZN9KPSENSAvqnXKVAvNEpuf7jYbSUgbI1QkZnFA3YZZCO_nBcUNMFI03E40Z1KKhRvNDXxl69iMe4fmMX4k5TXUtuQlYywOVR0Hs5mov5-mATwL9h0",
                        "Yushko Patrick"
                },

                {       "daniele-tsvetkov",
                        "daniele.tsvetkov@outlook.it",
                        "33222200Shin",
                        "AQEDAUtiZkQEzpptAAABjYItJMIAAAGSBVPmX00AjaXdGCNn6BzkFn_qrrP3mksFR2txJvskesqCAkCkgELfnKUw5Va5KTOkVBEx131CuhyZp0u6VRTk1z-8e_qSjlflYaeENa6JLKJaH9k-R3OrLPlF",
                        "Daniele Tsvetkov"
                },*/
        };
    }
}
