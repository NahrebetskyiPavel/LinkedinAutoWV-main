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
                {       "alessio-vacenko-b506612b3",
                        "alessio.Vacenko@outlook.it",
                        "33222200Shin",
                        "AQEDAUthqywCOthiAAABj00h33QAAAGPvkp4OE4ASeyim2hF4oGJkCfAbCkjDWmIdJfwVO3XyI1HeaM_0-kmXSiiMnwHo7xnp6urbl5O3VczT6kuWjuBlXHiAKh7-TSsVSn6Vrc2y521ONn_sVF2klqU",
                        "Alessio Vacenko"
                },
                {       "margit-matthes",
                        "margit.Matthes@outlook.de",
                        "33222200Shin",
                        "AQEDAUuampkDA9uSAAABjzNCPogAAAGQ_ZBZflYAcQGZY44l8e-DfVuOMdaqf7pEPd716QnZUeq9kIz3xpoNYRZBKeuPQYaFP6Kh0OUPKe8JBzAwv75HlZAHU91YIzWOdrhUiiB2fW_p0DnIgjP69SY1",
                        "Margit Matthes"
                },
                {       "dmitriy-timashov",
                        "timashov.dmitriy@outlook.de",
                        "33222200Shin",
                        "AQEDAUsszhwD-UTVAAABjzNJL1kAAAGRDOUsJU0A0dHWH0ecrAt2NTgB9FfL2OBPYc-VDpHVur76mm9_p6eHDI3iq9V-6j3i53tKbqGD7fs7r4AthVsnA-TsV7WD6glc4Lla-QIp4VYWW2VMw97SEOoP",
                        "Dmitriy Timashov"
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
                        "AQEDAUtjbVAFBgsLAAABjYNqZdIAAAGPMzVut00ASQCqvztRswdvlx8bV-veaBhTipBEPJIpAdvgAkQHWkxZ1BmRwBk74yqXj_Milu2_xWCAnFN6nfHXW2FD91dKRuryVwMu87BBSdDcO1sXtWVxoefe",
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
                        "AQEDAUs6XDsEfqYcAAABkP2EPEEAAAGRIZDAQU4AdkJev7wq2AxggBPWcOz7VguB6OMSydvlwbEocfw9QDBob9vjG6pXl_V3AZDKnbd2x_2A2kB3C1C6Ektvbc4sdpuwxvhIfea46UjDuPruGy7i5pRF",
                        "Elias Danilov"
                },

                {       "stefania-mykhaylenko",
                        "mykhaylenko.stefania@outlook.fr",
                        "cTsH3KhU",
                        "AQEFAREBAAAAABC4N6IAAAGQ8KFw8gAAAZEsOA_iTQAAtHVybjpsaTplbnRlcnByaXNlQXV0aFRva2VuOmVKeGpaQUFDL2dYTXYwQzBpTWFSV0JBdGFWcmJ4UWhpSkc4V0xnWXpJdTgvVzhQQUNBQ255UWpKXnVybjpsaTplbnRlcnByaXNlUHJvZmlsZToodXJuOmxpOmVudGVycHJpc2VBY2NvdW50OjI2MjE0NTAxOCwzMzgyMTYwMjkpXnVybjpsaTptZW1iZXI6MTI4MDM3MjUxNieoHZWxdgMpDxENEGwgbYhaHl9wjsIuYeHOXLc1rTdj5u59ukdk3F_xHWqb3WsxT5eh5svaR7KnBnUvWmTuQbyit0R5sQ1KKQkfaFGd49oeN64hiUiEhj4vmc52yeE80c86vmZ2Prakzli-kjBGGgxZq38PTY1Z3OONxQkt-YAtB5AhxYmSFw946fxbjNRmn7kGV7c",
                        "Mykhaylenko Stefania"
                },

                {       "eliza-kolner",
                        "eliza.kolner0103@outlook.de",
                        "ek03303KK",
                        "AQEFAREBAAAAAA9zBMEAAAGPTWfEpAAAAY_lKiqTTQAAtHVybjpsaTplbnRlcnByaXNlQXV0aFRva2VuOmVKeGpaQUFDbnRYRkUwRzBrTkNMbXlCYTRIRGhTa1lRSS9uZVhsMHdJN0krNVFZREl3RExpQW5SXnVybjpsaTplbnRlcnByaXNlUHJvZmlsZToodXJuOmxpOmVudGVycHJpc2VBY2NvdW50OjIxMjU2MjgzMywzMDMyMjkxNDUpXnVybjpsaTptZW1iZXI6MTI4MDM3NTY4NWp8bEsKJ75ogALhXQXQOlD3_brt0oR5mr_80U-ICsdrFqy-1_NJDjBInsNdgTVTTTiucKXT3yLe7uO0ozSA-OpT2CoPk-eUXF8diJIz_3Z_ciTBjWjlYNDM16fYlOI0mnZ_P-RdJDWNMvtQwywWon_TNUL5nyhQROKH4yOtj5VTz4BbSV5zMWjs405mZxzJEzUYwds",
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

