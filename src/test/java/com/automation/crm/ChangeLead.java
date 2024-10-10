package com.automation.crm;

import api.helpers.ZohoCrmHelper;
import com.automation.linkedin.pages.login.SignInPage;
import com.automation.linkedin.pages.search.SearchPeoplePage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.StatusChecker;

import java.util.Random;

import static com.automation.linkedin.Base.openLinkedInLoginPage;
import static com.automation.linkedin.Base.setupBrowser;
import static com.codeborne.selenide.Condition.interactable;
import static com.codeborne.selenide.Selenide.*;

public class ChangeLead {
    ZohoCrmHelper zohoCrmHelper = new ZohoCrmHelper();
    SignInPage signInPage = new SignInPage();
    SearchPeoplePage searchPeoplePage = new SearchPeoplePage();
    Random random = new Random();
    int low = 2000;
    int high = 5000;
    int randomResult = random.nextInt(high-low) + low;
    private static final String Contacted  = "421659000001302293";

    @SneakyThrows
    @Test(description = "add leads from search page", dataProvider = "dataProviderPeopleAddToCRM")
    public void addLeadsToCRM(String profileId, String email, String password, String cookie, String linkedinperson){
        int numberOfProfiles = 50;
        String token = zohoCrmHelper.renewAccessToken();
        System.out.println("Start");
        System.out.println("profileId " + profileId);
        System.out.println("email " + email);
        System.out.println("password " + password);
        System.out.println("cookie " + cookie);
        System.out.println("linkedinperson " + linkedinperson);
        String connectionsListTask = zohoCrmHelper.getConnectionsList(profileId, email, password, cookie, "Recently added", numberOfProfiles);
        Thread.sleep(1000*60);
        System.out.println("connectionsListTask: " + connectionsListTask);
//        System.out.println(connectionsListTask);
        String connectionsListTaskId = String.valueOf(new JSONObject( connectionsListTask ).get("taskId"));
        System.out.println(connectionsListTaskId);
        Thread.sleep(1000*60);
        String connectionsList = zohoCrmHelper.getTaskInfo(connectionsListTaskId, profileId);

while (true){
    if ((String.valueOf(new JSONObject( connectionsList ).get("status")+"").contains("finished"))) break;
    if ((String.valueOf(new JSONObject( connectionsList ).get("status")+"").contains("failed"))) break;
    connectionsList = zohoCrmHelper.getTaskInfo(connectionsListTaskId, profileId);
    Thread.sleep(20*1000);
}

        new StatusChecker().waitForStatus("finished", String.valueOf(new JSONObject( connectionsList ).get("status")), 30000);

        for (int i = 0; i < numberOfProfiles; i++)
        {
            String data = String.valueOf(new JSONObject( connectionsList ).getJSONArray("results").get(i));
            String personName = String.valueOf(new JSONObject( data ).getString("fullName"));
            //String personRef = String.valueOf(new JSONObject( data ).getString("personRef"));
            System.out.println(personName);
            Thread.sleep(randomResult);

                String leadInfoResponseBody = zohoCrmHelper.getLeadInfoByFullName(token, personName);
                System.out.println(leadInfoResponseBody);
                if (leadInfoResponseBody.contains("INVALID_TOKEN")) {
                    token = zohoCrmHelper.renewAccessToken();
                    leadInfoResponseBody = zohoCrmHelper.getLeadInfoByFullName(token, personName);
                }
                if (leadInfoResponseBody.length() > 0 && leadInfoResponseBody.contains("data")) {
                    JSONObject responseBodyJsonObjectLeadInfo = new JSONObject(leadInfoResponseBody);
                    String leadId = responseBodyJsonObjectLeadInfo.getJSONArray("data").getJSONObject(0).getString("id");
                    System.out.println(leadId);
                    System.out.println(personName);
                    if (responseBodyJsonObjectLeadInfo.getJSONArray("data").getJSONObject(0).getString("Lead_Status").equals("Attempted to Contact"))
                    {
                        String changeLeadStatusResponse = zohoCrmHelper.changeLeadStatus(leadId, token, Contacted);
                    JSONObject changeLeadStatusResponseJson = new JSONObject(changeLeadStatusResponse);;
                    System.out.println("code: " + changeLeadStatusResponseJson.getString("code") );
                    System.out.println("\n" );
                    if (changeLeadStatusResponseJson.getString("code").equals("RECORD_NOT_IN_PROCESS")) {
                        System.out.println("Try direct change:\n" + zohoCrmHelper.changeLeadStatus(leadId, token) );
                    };
                    }
//else {
//                       // String Last_Name, String token, String pickList, String LinkedInLink, String leadStatus, String leadCompany, String leadCompanyId, String accountname
//                        String response = zohoCrmHelper.AddLeadToCRM(personName, token, pickList, personRef, "Attempted to Contact", leadCompany, leadCompanyId, linkedinperson);
//                        if (response.contains("INVALID_TOKEN")) {
//                            token = zohoCrmHelper.renewAccessToken();
//                        }
//                    }
                }
            Thread.sleep(randomResult);
        }
    }


//String profileId, String email, String password, String cookie
    @DataProvider(name = "dataProviderPeopleAddToCRM", parallel=false)
    public static Object[][] dataProviderPeopleAddToCRM() {
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
                        "AQEDAUs6XDsDTna2AAABkmcdUxsAAAGSiynXG1YAQZ8RuPjJB9rtujZ9bscvnci6DVZhBN9OCSq-KNUSbs1LWLvIqyD7RtGPOf-NNN_xzh9-875Zan2fAlqK6qg731LrgLpiIFEX6XuZtpJEBnKeJz9t",
                        "Elias Danilov"
                },

                {       "stefania-mykhaylenko",
                        "mykhaylenko.stefania@outlook.fr",
                        "cTsH3KhU",
                        "AQEDAUxQ7yQFeDJ8AAABknU5x2gAAAGSmUZLaE4AzooC_CrpC7EIj8BxhtBVzzNfQ_1EJ36bJyg8HfCnpnBxmeETpqzh4VbZU14vmKtOIr5EAFmzzot3t9OsQlzoPFCj9-IPMvq3pIV4T1GISAN8t-wG",
                        "Mykhaylenko Stefania"
                },

                {       "den-vaviron",
                        "denVavir00@outlook.de",
                        "33222200Shin",
                        "AQEDAUpubFMFCUCvAAABjP1fQdMAAAGSfA_8DE0AnZeA7m5pUCCSc00VyGY3dqXBirHDa5HXFDIUjdig0sdqyEqRtKZsQtnfcjmbDB58wMHS3uHVaEHP0T6hxFlzV-3868RnKT9KqKYvnnYya_zaRDmm",
                        "Den Vaviron"
                },


                {       "max-mikhaylov",
                        "max.Mikhaylov@outlook.de",
                        "33222200Shin",
                        "AQEDAUtQFBQCjJC7AAABjp8_IKQAAAGSfA_4Jk0ALptb50ypM8w9vm4kI3vNGuU_jQzT7RhBU0kvZSlj8c3pRzlwqVGD70umAHxExqKQlr_PkyiT8SU564g-0BjvZd9AWbnZHbRY2LPs8YoTSfKSulJk",
                        "Max Mikhaylov"
                },

                {       "patrick-yushko-b2080b2b8",
                        "yushko.patrick@outlook.it",
                        "206GLMC2",
                        "AQEFAHUBAAAAABBl9t0AAAGPfE7YuwAAAZKKB79JTgAAGHVybjpsaTptZW1iZXI6MTI4MDAxMjQyNCkImZePKBQ_wsgIfQWUSS0i6-dmUGCWTHTk9knhrciGs3m02ZJRFeH-yFBI0TCyHMX5Kd8EPiHgBntFKAhretAjvkccDcc8ygEzCHuxRi0ZPGGzGcZOSLlZN-3pq6L99DGZNdps7hvn5kW1HqfbKbIlI-rEU5bi2o8fboIgbmCz1bPljvmACEwrIX3kU2cNmKWDRt0",
                        "Yushko Patrick"
                },

                {       "daniele-tsvetkov",
                        "daniele.tsvetkov@outlook.it",
                        "33222200Shin",
                        "AQEDAUtiZkQEzpptAAABjYItJMIAAAGSigdqoE0AXvavHfjpr35J7Ncy9oEmlKpEz-K2MwJfaNqhtNxtQCdk9YWFkTIPRA_fwnwXbgrd6xM2FYW5iFsyKfTA5O_T7Z9M-JuJadMsZ8a_lEd_3k-HxK5P",
                        "Daniele Tsvetkov"
                },



                {       "miyana-emelianenko",
                        "emelianenko.miyana@outlook.de",
                        "CcDZPmv7",
                        "AQEDAUxZNjwAZkXFAAABj3bhQFAAAAGSfA_45k0AcUX_bAux95m756HmwN4awvumZN5titR4mJEcfzgIGfrmSUGUxxodDMxaCnZgddCc_4fc6JNYFR1zDCMA42VfhT7O_b4jWlvUo7Xq2x1gOlGP_Uid",
                        "Emelianenko Miyana"
                },


                {       "fidel-salyenko-8183a92b3",
                        "fidel.Salyenkooo1962@outlook.it",
                        "33222200Shin",
                        "AQEDAUtXv1QFgKDNAAABjYINrOwAAAGSe6mwBk0AvkCEMAw8ut9IAFe4uGHktJwhemlh6fn5-Xf84DoHPQmLV6tapcXldABJPGS4MxJz2L-UX05-MOVIPJXHToDJNsIgx7c9pOTacl19FfCTT1Et7prO",
                        "Fidel Salyenko"
                },

                {       "michael-krusciov",
                        "michael.krusciov@outlook.de",
                        "cTsH3KhU",
                        "AQEDAUwy4cUF84KUAAABkleNDrMAAAGSe5mSs1YAHhd65fXcQTNNfl652iE7etF1UlPZ3AKKE3xOhcttytUL87noJsRaIRz_Ec-rVStVen51EaJkZjfqXlxVb6-F2tK0pykFxWcjY8BqXSfDIsYMYx8f",
                        "Michael Krusciov"
                },


                {       "michael-zhmorshchuk-3161302b2",
                        "zhmorshchuk.michael@outlook.de",
                        "33222200Shin",
                        "AQEDAUsgIMIDmVD3AAABkTQUZv8AAAGSjt6EoU0AV2dCQldBkYHaNYTBVXuhnEhimF-2mdUXitJuX7y4KSTuleT7KK62teqN0ntBlDO6Mf2juliqKmJ4Wy6GgTWe10Jaa9eEUp3AVXHHP1zf_6MOIZqj",
                        "Zhmorshchuk Michael"
                },


                {       "kenan-strelbytsky-364ba22b8",
                        "strelbytsky.kenan@outlook.de",
                        "ygm9ijzZ",
                        "AQEDAUxZvXwBvIgNAAABj3xBliYAAAGSigrIwU0AcgYVfHaTonyE1HaLyc5y0kx5MKcWw2YbYqyCdFQbs5Di068bge4ojQloWC5XLUBq3a1ymp0Nbbad12hE1JhivoPhwxuBXboJYb-S9K2DxVQ4iApa",
                        "Strelbytsky Kenan"
                },



        };
    }
}

