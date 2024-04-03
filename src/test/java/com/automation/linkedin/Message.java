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

                            System.out.println("subject " + subject);
                            System.out.println("equals " +subject.equals(taskName));
                            Thread.sleep(10000);
                            System.out.println("sent msg!!!");
                            {
                                //new PersonPage().sentMsg(description.replace("NAME",leadName));
                                String msg = description.replace("NAME",leadName).replace("\n","\\n");
                                System.out.println(msg);
                                String response = wiseVisionApiHelper.sentMsgImpasto(profileId, email, password, cookie, leadPage, msg);
                                System.out.println("response " + response);
                                Thread.sleep(1000*60);
                                int impastoTaskId = (int) new JSONObject( response ).get("taskId");
                                System.out.println("taskid = " + impastoTaskId);
                                String taskStatus = new JSONObject( wiseVisionApiHelper.impastoGetTaskinfo(profileId, impastoTaskId) ).getString("status");
                                System.out.println("taskStatus " + taskStatus);
                                if (taskStatus.equals("new")) {
                                    Thread.sleep(30000);
                                    taskStatus = new JSONObject( wiseVisionApiHelper.impastoGetTaskinfo(profileId, impastoTaskId) ).getString("status");
                                    System.out.println("taskStatus " + taskStatus);

                                }
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

                            System.out.println("subject " + subject);
                            System.out.println("equals " +subject.equals(taskName));
                            Thread.sleep(10000);
                            System.out.println("sent msg!!!");
                            {
                                //new PersonPage().sentMsg(description.replace("NAME",leadName));
                                String msg = description.replace("NAME",leadName).replace("\n","\\n");
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
                {       "andrei-gorbunkov-a34b4a2aa",
                        "andreiGorbunkov@outlook.de",
                        "33222200Shin",
                        "AQEDAUqQcUgAJO_LAAABjRGv3SIAAAGOmMFYqE0ArnVnmtRxkfVOu6vUysML6PHk2oENpaWG43H6H_RZGisvCqLeBj7azZTBPn0_vjE7zPme8YjHw6GyXwEOBkQvUkqNijYnP9HnwG2A5y5wR9E-hY_q",
                        "Andrei Gorbunkov"
                },

                {       "aline-paul",
                        "aline.paul@outlook.de",
                        "33222200Shin",
                        "AQEDAUt7kBIA4MJgAAABjqNntPAAAAGOx3Q48E4AexTges7j1wsZ0B32R1TEEDrpZi5RTL34iCnfNfDLgcLLiUCrgaNcL-QfMM5rHf08I_PfzQaoWeS8qjOa4GjI54x7SCf4evAsGy05jy5S-NSTp8m-",
                        "Aline Paul"
                },
                {       "paul-bereza",
                        "paul.bereza02@outlook.de",
                        "33222200Shin",
                        "AQEDAUt7kjAFQ5V1AAABjY17BIQAAAGOrchRxk0ANnqFXzwoOoGvqdSns-mBZgVIAigOKbTZJ0RWUBNK8UB64oOwoeTXFih1uxrDgIW8DA6YEpGKavO-W1MdA2YdEUSLqalw5LgoK7VQMCDwi7SmvsPn",
                        "Paul Bereza"
                },
                {       "alessio-vacenko-b506612b3",
                        "alessio.Vacenko@outlook.it",
                        "33222200Shin",
                        "AQEDAUthqywBUW83AAABjoGzPasAAAGOpb_Bq00AYesxSzqz_svmdxwpgeBI81kGKBz9KOqrYeQvGGre8kwdFnnUm2mcCEmofBpk5hydytWEP2hVdpRs910CvZ5kko7h1JcCY1jsGoAdpRxNvdqaCuac",
                        "Alessio Vacenko"
                },
                {       "margit-matthes",
                        "margit.Matthes@outlook.de",
                        "33222200Shin",
                        "AQEDAUuampkCi39pAAABjoGopG0AAAGOpbUobU0AYzgDBXlGuCNM8mjS3qeymKlCs3GDAAAX2yazREUmzNsgqrkCEBZWhzFf7xDJ05XaRilGTXcvDKyDYGYfmp7b-E2i0L4QTVBbKA7d6GA6MsD6y_Hb",
                        "Margit Matthes"
                },

        };
    }
}
