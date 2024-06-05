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
        String connectionsListTaskId = String.valueOf(new JSONObject( connectionsListTask ).get("taskId"));
        System.out.println(connectionsListTaskId);
        Thread.sleep(1000*60);
        String connectionsList = zohoCrmHelper.getTaskInfo(connectionsListTaskId, "andrei-gorbunkov-a34b4a2aa");


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

                {       "margit-matthes",
                        "margit.Matthes@outlook.de",
                        "33222200Shin",
                        "AQEDAUuampkDA9uSAAABjzNCPogAAAGPV07CiFYAUTlaawCUQBq15anEMNwZmKlCqaTK2oqUSr-P05fihZvoGHPgFk7KZroX9ZKpVEbvEjOLth1xcqkJDG3F_a2_o_3WYwet4mooFR5SjuTy2Z_eezlj",
                        "Margit Matthes"
                },
                {       "andrei-gorbunkov-a34b4a2aa",
                        "andreiGorbunkov@outlook.de",
                        "33222200Shin",
                        "AQEDAUqQcUgEIxsAAAABjvWFnKYAAAGPlUYADFYAombm43_GJ7Tg5JgPeG6gMA7igoCuX850p-7SUHJnbnQrAqxiiJv4ADi_L76d5_T8_1z0Ea_ZO7h2I-35JHOu43bflHEbj-G5kzFRsyhBhkJZwHc8",
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
                        "AQEDAUuampkDA9uSAAABjzNCPogAAAGPV07CiFYAUTlaawCUQBq15anEMNwZmKlCqaTK2oqUSr-P05fihZvoGHPgFk7KZroX9ZKpVEbvEjOLth1xcqkJDG3F_a2_o_3WYwet4mooFR5SjuTy2Z_eezlj",
                        "Margit Matthes"
                },
                {       "dmitriy-timashov",
                        "timashov.dmitriy@outlook.de",
                        "33222200Shin",
                        "AQEDAUsszhwD-UTVAAABjzNJL1kAAAGPV1WzWU0Am_0hKUQRi5bvt9ZVU15-Yig0zqKpUaul-TQl69vjsUu4AX_e5zQ9o0HR6yR3IQZpg56KgFMtjg6JC54K00TwLxRDc-_GMW5K4ECEUrrX8BVS6nZ7",
                        "Dmitriy Timashov"
                },

                {       "eliza-kolner",
                        "eliza.kolner0103@outlook.de",
                        "ek03303KK",
                        "AQEFAREBAAAAAA9zBMEAAAGPTWfEpAAAAY_lKiqTTQAAtHVybjpsaTplbnRlcnByaXNlQXV0aFRva2VuOmVKeGpaQUFDbnRYRkUwRzBrTkNMbXlCYTRIRGhTa1lRSS9uZVhsMHdJN0krNVFZREl3RExpQW5SXnVybjpsaTplbnRlcnByaXNlUHJvZmlsZToodXJuOmxpOmVudGVycHJpc2VBY2NvdW50OjIxMjU2MjgzMywzMDMyMjkxNDUpXnVybjpsaTptZW1iZXI6MTI4MDM3NTY4NWp8bEsKJ75ogALhXQXQOlD3_brt0oR5mr_80U-ICsdrFqy-1_NJDjBInsNdgTVTTTiucKXT3yLe7uO0ozSA-OpT2CoPk-eUXF8diJIz_3Z_ciTBjWjlYNDM16fYlOI0mnZ_P-RdJDWNMvtQwywWon_TNUL5nyhQROKH4yOtj5VTz4BbSV5zMWjs405mZxzJEzUYwds",
                        "Eliza Kolner"
                },

                {       "Matthew Martinez",
                        "mMartiz11@outlook.it",
                        "metmar11mmjy",
                        "AQEDAUvkzscEuod7AAABjcxZHVEAAAGPM0jgWU0AxNq9k7vcTnaST2Y01G-gU50hTFao_KHUxDpMO4OkxBjTDSjb2Mt1lbEXESZCcCoYA9Pd0qUZmLLUFDt-Eeh2HHGvi5izLQvjtJbOcQh7J1J7ZZOI",
                        "Matthew Martinez"
                },

                {       "Sergio Cheban",
                        "sergio.Cheban@outlook.it",
                        "33222200Shin",
                        "AQEDAUtjbVAFBgsLAAABjYNqZdIAAAGPMzVut00ASQCqvztRswdvlx8bV-veaBhTipBEPJIpAdvgAkQHWkxZ1BmRwBk74yqXj_Milu2_xWCAnFN6nfHXW2FD91dKRuryVwMu87BBSdDcO1sXtWVxoefe",
                        "Sergio Cheban"
                },

                {       "Sherida Sluijk",
                        "s.Sluijk@outlook.de",
                        "33222200Shin",
                        "AQEDAUtyJqUALIwdAAABjxTmPLIAAAGPOPLAsk0ApibiizcM0XQOpyUn2klpIGBn5rXDSFfvwHtCMcayud_Iu5IP3PbCe_SypwV4VqRrQInooXtqyzKhPvwbBXFZbFPh9-gDbqvWhBe8zwnbeD7RsPCy",
                        "Sherida Sluijk"
                },

                {       "petr-2",
                        "petr.degtyarev@outlook.de",
                        "33222200Shin",
                        "AQEDAUs5cmoFe9mZAAABjlyBf2oAAAGPMzVXok0Aase-74YwTB2el__HXiApMk1VKjDZtiYnIDeGI_Ez0PCLecWzCQhotwuxGmOsGFdTDsfLoyvCiOiFkdEKpoiqtjPBEqoZR_aSsiFM2OhDXtMNs2cp",
                        "Petr Degtyarev"
                },

                {       "elias-danilov",
                        "elias.danilov@outlook.it",
                        "33222200Shin",
                        "AQEDAUs6XDsD5xZNAAABj9280AUAAAGQAclUBU0AVLD-KLt7E5u2l9vaxLlyJsy4IwByMkkffUZHu29ve_BIdisK6fCkVzQntizDPNKr2zztLhhvP6qpY79-cfeghLQlRQ66Ahhj3QM3WxroRP9rwk-d",
                        "Elias Danilov"
                },

                {       "stefania-mykhaylenko",
                        "mykhaylenko.stefania@outlook.fr",
                        "cTsH3KhU",
                        "AQEDAUxQ7yQCBDVPAAABj86bRwQAAAGP8qfLBE0Ax1VldZxwdUXUKLTFU60qzlcZGvAR2RZG_IkS8ifmuXHdFVYU8Q5I4wPPo9yyB9qNP39-xvU7pluH2AB6hTGmmp9kzRBbTXUoKEVi0mAZ8D2cLJjx",
                        "Mykhaylenko Stefania"
                },

                {       "eliza-kolner",
                        "eliza.kolner0103@outlook.de",
                        "ek03303KK",
                        "AQEFAREBAAAAAA9zBMEAAAGPTWfEpAAAAY_lKiqTTQAAtHVybjpsaTplbnRlcnByaXNlQXV0aFRva2VuOmVKeGpaQUFDbnRYRkUwRzBrTkNMbXlCYTRIRGhTa1lRSS9uZVhsMHdJN0krNVFZREl3RExpQW5SXnVybjpsaTplbnRlcnByaXNlUHJvZmlsZToodXJuOmxpOmVudGVycHJpc2VBY2NvdW50OjIxMjU2MjgzMywzMDMyMjkxNDUpXnVybjpsaTptZW1iZXI6MTI4MDM3NTY4NWp8bEsKJ75ogALhXQXQOlD3_brt0oR5mr_80U-ICsdrFqy-1_NJDjBInsNdgTVTTTiucKXT3yLe7uO0ozSA-OpT2CoPk-eUXF8diJIz_3Z_ciTBjWjlYNDM16fYlOI0mnZ_P-RdJDWNMvtQwywWon_TNUL5nyhQROKH4yOtj5VTz4BbSV5zMWjs405mZxzJEzUYwds",
                        "Eliza Kolner"
                },
                {       "andrey-gorichev",
                        "gorichev.andrey12311@outlook.it",
                        "33222200Shin",
                        "AQEDAUsXduoFXURrAAABjVq7ABQAAAGP47hNrE0AfWZ8_ikQZ884R2ykvvKS1bOA0bUDRo0-6r0_QFmBrzWZvqxX_JCmB-X5RykYyrSh6LzajqQcrohQA8pmVb7XqF8MMY24l9X2v4tQfU3OeyOnIpyd",
                        "Gorichev Andrey"
                },
        };
    }
}

