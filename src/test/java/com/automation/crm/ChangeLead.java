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
                        "AQEFAHUBAAAAAA9y_ngAAAGQEc-OggAAAZH516JITQAAGHVybjpsaTptZW1iZXI6MTI2NjM4OTU1MrgmxeadesHM3nKCxrsUsEY7oE2YNlEjDfKcRC4i0jdJYv-0CrELcWvwvgl9Hh_a-UD3BztRMFXG1L-T27TKo8wdUft7g5Rp9vRBKL8EpeN7XvfJL9trBE0Lmp5EWDf6VOBX2WPUshBn-0VfCxKzHXg_8-XsjxcgwJbEGisHf6F8LtRJP1pWyHm0_3_XD2ubXhq_AIM",
                        "Paul Bereza"
                },
                {       "margit-matthes",
                        "margit.Matthes@outlook.de",
                        "33222200Shin",
                        "AQEDAUuampkDA9uSAAABjzNCPogAAAGSI-3KcFYAHIw-5yvhTHbx82fm6vv-Y1Mi2sy14qhV9Bgji2cTMx688YRW7xm3m27UthuyFYBwP_3bm3-xx4HvMrrE6SNFHyA_mxhJMohblRI1ujIXQuNSsb-r",
                        "Margit Matthes"
                },


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
                },
        };
    }
}

