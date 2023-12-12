package com.automation.linkedin;

import api.helpers.ZohoCrmHelper;
import com.automation.linkedin.pages.PersonPage;
import com.automation.linkedin.pages.login.SignInPage;
import com.automation.linkedin.pages.messaging.MessagingPage;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.$x;

public class Message extends Base{
    SignInPage signInPage = new SignInPage();
    MessagingPage messagingPage = new MessagingPage();
    ZohoCrmHelper zoho = new ZohoCrmHelper();
    @SneakyThrows
    @Test(description = "send FollowUp Msg", dataProvider = "dataProviderPeopleSearch")
    public void sendFolowUpMsg(String name,  String email, String password, String pickList){

        setupBrowser(true, name);
        openLinkedInLoginPage();
        signInPage.signIn(randomResult, email, password);
        WebDriverRunner.getWebDriver().manage().window().maximize();
        String  token = zoho.renewAccessToken();

        for (int n = 0; n < 100; n++) {
            String data =  zoho.getLeadList(token, pickList, "Contacted",name,n);
            //System.out.println(data);
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
                        System.out.println(taskId);
                        System.out.println(status);
                        System.out.println(subject);
                        if (status.equals("Not Started") &&  subject.contains("Second message")){
                            System.out.println("open " + leadPage + " and sent second message to " + fullName);
                            Selenide.open(leadPage);

                            if (false){
                                $x("//div[contains(@aria-label,'Messaging')]//div[contains(@class,'msg-overlay-bubble-header__controls')]/button[3]").click();
                            }
                            else {
                                new PersonPage().msgBtn.click();
                                if (!$x("//div[contains(@aria-label,'Write a message…')]").is(Condition.visible)) continue;
                                $x("//div[contains(@aria-label,'Write a message…')]").click();
                                $x("//div[contains(@aria-label,'Write a message…')]").sendKeys("Hello.\n" +
                                        "How are you doing?\n");
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
                        "asd321qq",
                        "Valeriia",
                },
                {       "Денис - Saudi Arabia CEO",
                        "basdenisphytontm@gmail.com",
                        "asd321qq",
                        "Valeriia",
                },
                {       "Настя - Stuttgart CEO",
                        "anastasiiakuntii@gmail.com",
                        "nastya4141",
                        "Alex",
                },
                {       "Роксолана - Stockholm CFO",
                        "roksolanatrofim@gmail.com ",
                        "89fcmTT88V",
                        "Alex",
                },
                {       "Марьян -  Stockholm CTO",
                        "reshetunmaryanwv@gmail.com",
                        "rSbnGaRS",
                        "Alex",
                },
                {       "Анастасия - Saudi Arabia owner",
                        "vozniakanastasia52@gmail.com",
                        "zdHXF5bf",
                        "Valeriia",
                }
        };
    }
}
