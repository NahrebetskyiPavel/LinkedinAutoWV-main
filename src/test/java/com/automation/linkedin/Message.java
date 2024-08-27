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

    @SneakyThrows
    @Test(description = "send FollowUp Msg", dataProvider = "dataProviderPeopleSearch", priority = 1)
    public void senddMsg(String profileId, String email, String password, String cookie, String linkedInAccount ){

        String  token = zoho.renewAccessToken();


        sendFolowUpMsg(linkedInAccount, token,  "Second automessage", profileId,  email,  password,  cookie );
        sendFolowUpMsg(linkedInAccount, token,  "Third automessage", profileId,  email,  password,  cookie );
        sendFolowUpMsg(linkedInAccount, token, "Fourt automessage", profileId,  email,  password,  cookie );
        sendFolowUpMsg(linkedInAccount, token, "Fifth automessage", profileId,  email,  password,  cookie );
        sendFolowUpMsg(linkedInAccount, token, "Six automessage", profileId,  email,  password,  cookie );
        sendFolowUpMsg(linkedInAccount, token, "Seven automessage", profileId,  email,  password,  cookie );
        sendFolowUpMsg(linkedInAccount, token, "Eight automessage", profileId,  email,  password,  cookie );
        sendFolowUpMsg(linkedInAccount, token, "Nine automessage", profileId,  email,  password,  cookie );
        sendFolowUpMsg(linkedInAccount, token, "Ten automessage", profileId,  email,  password,  cookie );
        sendFolowUpMsg(linkedInAccount, token, "FollowUp first automessage", profileId,  email,  password,  cookie );
        sendFolowUpMsg(linkedInAccount, token, "FollowUp second automessage", profileId,  email,  password,  cookie );
        sendFolowUpMsg(linkedInAccount, token, "FollowUp third automessage", profileId,  email,  password,  cookie );
        sendFolowUpMsg(linkedInAccount, token, "FollowUp forth automessage", profileId,  email,  password,  cookie );
        sendFolowUpMsg(linkedInAccount, token, "FollowUp fifth automessage", profileId,  email,  password,  cookie );
        sendFolowUpMsg(linkedInAccount, token, "FollowUp six automessage", profileId,  email,  password,  cookie );

        sendFolowUpMsg(linkedInAccount, token, "Meeting automessage", profileId,  email,  password,  cookie );

    }


    @SneakyThrows
    public void sendFolowUpMsg(String linkedinAccount, String token, String taskName, String profileId, String email, String password, String cookie ){
        System.out.println("START " + taskName);
        int msgsSent = 0;
        for (int n = 0; n < 100; n++) {
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
                            accsMsgssent.add(fullName);
                            msgsSent += msgsSent;
                            if (msgsSent == leadsRandomResult )      break;
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
                            accsMsgssent.add(fullName);
                            msgsSent += msgsSent;
                            if (msgsSent == leadsRandomResult )      break;
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
            if (msgsSent == leadsRandomResult )      break;
            if (msgsSent == leadsRandomResult )      return;

        }
        if (msgsSent == leadsRandomResult )      return;

    }

    @DataProvider(name = "dataProviderPeopleSearch", parallel=true)
    public static Object[][] dataProviderPeopleSearch() {
        return new Object[][]{



                {       "andrei-gorbunkov-a34b4a2aa",
                        "andreiGorbunkov@outlook.de",
                        "33222200Shin",
                        "AQEDAUqQcUgD6EWMAAABkMYKnpEAAAGQ6hcikU4Aiy3NU9_3Nzk5N3dVmWOwFQRegPTvU0TcLHaHej-UIZrZ9tVQknB9_REq00JtwdUeU3NCQyk1u5-k1NZMNCWO9_BC6qJ0VElyNxFrPmhYZT-krtrj",
                        "Andrei Gorbunkov"
                },

                {       "paul-bereza",
                        "paul.bereza02@outlook.de",
                        "33222200Shin",
                        "AQEFAREBAAAAAA9y_ngAAAGPTWyIZQAAAY9xerrsTQAAtHVybjpsaTplbnRlcnByaXNlQXV0aFRva2VuOmVKeGpaQUFDbnRYRkMwRzBrTkJ0UnhBdGNPak1TMFlRSS9uZXNWNHdJN0krWUM4REl3RE1hUW9RXnVybjpsaTplbnRlcnByaXNlUHJvZmlsZToodXJuOmxpOmVudGVycHJpc2VBY2NvdW50OjIxMjU2Mjg0OSwzMDMyMjU2NjUpXnVybjpsaTptZW1iZXI6MTI2NjM4OTU1Mqtahj54xFZSxnrvy1Fhjo_T5CKqWfcLzzEpATi94tKyJSkHYCbgDxcQJJBeH2a8gJu17Nee6uDtPtfo_xlPThNbQRG8NrY1MvzU75pvL6Rvd7fsGhGb9M58udN77XZ1qFysniogLshJfVj7ldODM6ZXSGdf68Gml-blsKo3mCQX9GbJCdGFEc0IjGUj57r7S4gaOng",
                        "Paul Bereza"
                },
                {       "margit-matthes",
                        "margit.Matthes@outlook.de",
                        "33222200Shin",
                        "AQEDAUuampkDA9uSAAABjzNCPogAAAGQ_ZBZflYAcQGZY44l8e-DfVuOMdaqf7pEPd716QnZUeq9kIz3xpoNYRZBKeuPQYaFP6Kh0OUPKe8JBzAwv75HlZAHU91YIzWOdrhUiiB2fW_p0DnIgjP69SY1",
                        "Margit Matthes"
                },
                {       "matthew-martiz-6335072b6",
                        "mMartiz11@outlook.it",
                        "metmar11mmjy",
                        "AQEDAUvkzscEuod7AAABjcxZHVEAAAGPM0jgWU0AxNq9k7vcTnaST2Y01G-gU50hTFao_KHUxDpMO4OkxBjTDSjb2Mt1lbEXESZCcCoYA9Pd0qUZmLLUFDt-Eeh2HHGvi5izLQvjtJbOcQh7J1J7ZZOI",
                        "Matthew Martinez"
                },

                {       "sergio-cheban-7806b82b3",
                        "sergio.Cheban@outlook.it",
                        "33222200Shin",
                        "AQEFAHUBAAAAAA90iAcAAAGPTYrONQAAAZETuAm0TgAAGHVybjpsaTptZW1iZXI6MTI2NDgwNzI0OCXFwy2KaYdoU6Nu0dSgYoJxWO3bRmNhD7hdyBRvXt660aUM5RbH_YpMCXc2ZLhXPFh3-EQomPxP7UlhJIS-cJFPG4iZXyMamW-UjsYlQGaYo2cfp5G611QuXIR9srmOIuSmvjSKxdv1txI040hJDLwdSrcg8kwlzdbfzGEPZDM41WNrfBehocebDF0fxbe3L8azC_0",
                        "Sergio Cheban"
                },

                {       "petr-2",
                        "petr.degtyarev@outlook.de",
                        "33222200Shin",
                        "AQEFAREBAAAAABBY1JQAAAGQgeLMcgAAAZDKCB9kTgAAtHVybjpsaTplbnRlcnByaXNlQXV0aFRva2VuOmVKeGpaQUFDNFM3NVNXQ2FMellFUkV2a3hFVXhnaGhKSGlidFlFYmt2bC9zREl3QWdvRUczZz09XnVybjpsaTplbnRlcnByaXNlUHJvZmlsZToodXJuOmxpOmVudGVycHJpc2VBY2NvdW50OjMyNzgxOTE1NCwzMTk3MDg1MDApXnVybjpsaTptZW1iZXI6MTI2MjA1NjA0MiB5HA7YQmaCnTjG_Jeg9OP193CAUgF3O5NbC4tsQoBwY6Icqn4NqeYKfS1mBhe-GEaBcQzoNO_OHOwtGcd1ljS3ETYlo3cbU7dsgJ3ZKZWDbCPyJjQFuy-maQrDoTH817VKfgGeyfRdWon95WJFBNMfdxeEmEKO___glBu3dHkUCMbTBEszAMmwUmN9QOjpjN4EjlE",
                        "Petr Degtyarev"
                },


                {       "elias-danilov",
                        "elias.danilov@outlook.it",
                        "33222200Shin",
                        "AQEDAUs6XDsABQ7RAAABkRd09roAAAGRO4F6uk0AcXqj9CWjuOSbF4D-tVlHsgKnm_esJ-mQW_HGeDJ1gsE4Y8pb4qxgM7Fl90N9JE3c_PklVqcgAf-IuCOiNKjiXqHAbFmOehvrhE1lRzgFzE6lYPA2",
                        "Elias Danilov"
                },

                {       "stefania-mykhaylenko",
                        "mykhaylenko.stefania@outlook.fr",
                        "cTsH3KhU",
                        "AQEDAUxQ7yQFbUxKAAABkR4emVAAAAGRQisdUE4AXKkOjlDFR1XwSOQ1Iv-5nAT4zMWEiNaKeocaWW43aNtkgktDP89EcljU2nQWzbfW_9AQG-1Ugkrt_XXxbtThiWJoh8OhfhjldfdU4o1AKs9V1TAO",
                        "Mykhaylenko Stefania"
                },

                {       "eliza-kolner",
                        "eliza.kolner0103@outlook.de",
                        "ek03303KK",
                        "AQEFAHUBAAAAAA9zBMEAAAGQEbVkiAAAAZFCK5mfTgAAGHVybjpsaTptZW1iZXI6MTI4MDM3NTY4Nb0sm5eiakdGqkUcQLMPqTb69_RNwy-fZLGthRKzUtY89HZDpX5RFbSZ-UIyV5F8HM7PMY7htFgIn7vWu9FUz0hBZ5tfQfk2YDZxefvpm12ykEyrfU0pa-ZoHidS5M-H0I8t18p1OJYDuM9JMzYjcxAFhuQhTiwEF-JZEjtrjdAz8rDB4eL4mwKnGCgtyaC099Jr7Yo",
                        "Eliza Kolner"
                },
                {       "den-vaviron",
                        "denVavir00@outlook.de",
                        "33222200Shin",
                        "AQEDAUpubFMFCUCvAAABjP1fQdMAAAGQtXessU0ABfMSxtRvOA33jReqC-CJ1_M_lJguSSNtxtfK6oNPSgSEoAjysk8z-9NuyMRgQzXAE2aPNfe2cezizlTWm074u_OxWydkKNdyeD3bKq-pJb0icOlm",
                        "Den Vaviron"
                },


                {       "max-mikhaylov",
                        "max.Mikhaylov@outlook.de",
                        "33222200Shin",
                        "AQEDAUtQFBQCjJC7AAABjp8_IKQAAAGQtXbCFk0APU1awin9vTpbYPFPAwTI09b3wzQTleAm9yk3WJrSzyUhqSO1oAKNUAtMstP37X_J_ilxnTfpDYL7LbEzY75i1-7r49tgyEDTplOmq0bWTgcp2kOh",
                        "Max Mikhaylov"
                },

                {       "anastasiya-boroday-8874362b7",
                        "boroday.anastasiya@outlook.de",
                        "SBZQP0FG",
                        "AQEDAUwPgGcC5KHZAAABjeWXQGgAAAGQuotxjU0AJnPA67zRBmxWTC8FhKcIK88XX08j0QdAq5jPoCwH1-RafIlwA5dTJr_Gyjcntv0RAdjl9EJ0jstEp6D1fR2y1rCIMeebp6Ec6Oaks8kjviNpqi3g",
                        "Boroday Anastasiya"
                },
                {       "patrick-yushko-b2080b2b8",
                        "yushko.patrick@outlook.it",
                        "206GLMC2",
                        "AQEFAREBAAAAABBl9t0AAAGPfE7YuwAAAZC6kOdFTgAAtHVybjpsaTplbnRlcnByaXNlQXV0aFRva2VuOmVKeGpaQUFDa2JDbVR5QmFlTWFxZGhBdE9lRlJGQ09Ja1JKc3pROW1SRjY4ZG9tQkVRRERJUWwzXnVybjpsaTplbnRlcnByaXNlUHJvZmlsZToodXJuOmxpOmVudGVycHJpc2VBY2NvdW50OjM0MTIxMzkzOCwzMjg3NzIyMzEpXnVybjpsaTptZW1iZXI6MTI4MDAxMjQyNBoDBY08vb3502vD4Lj1B4RgmmkK9v0IEWkf_IjnAtvJqOfXzBNNs9vcA8ZklQxTdPNHdwmvFOJtaoTqBK38dvgyvHQacDip21M7eXyIjBtLGsCmWr_PCKUW4xEYLnJG8Dayg7-oVljVDkJ3KBp8X3MVfha5PgpBrMhdlxW-ZLQ11--220ZXBT6eQ9OMTxTMD4IADG4",
                        "Yushko Patrick"
                },

                {       "daniele-tsvetkov",
                        "daniele.tsvetkov@outlook.it",
                        "33222200Shin",
                        "AQEDAUtiZkQEzpptAAABjYItJMIAAAGQupELhk0ApRFD-1Hgo_-4tATx2KxiW0Ckh1_aOHjf1GX1XMSCcNLd_HQbgLydDywC2zLCZQfpTztsCPonqT_Q8MDEcO_2K3taSnfpPvgzWmL_Xedrdm36Fxh0",
                        "Daniele Tsvetkov"
                },
        };
    }
}
