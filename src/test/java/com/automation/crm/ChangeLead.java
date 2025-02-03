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
                        "AQEFAHUBAAAAAA9y_ngAAAGQEc-OggAAAZMAqYavTQAAGHVybjpsaTptZW1iZXI6MTI2NjM4OTU1MsYltKWYUEZFxw9AfILq0Z4E9w7CrRJqmgQoghXasKUll-fouMGn4H89REVuhDBtiIeV8iowzTn1Zqh2zLq3v3wBcYDAE8CScmV3AzfzQD4W1sum6x-21zk0jEJJ5ssgABMB9IcchHvWPRELG6zagWUcmIqS_eeYF6cPe21DyA5Wd4PvTPzU0GaoPnYdub1ublV3mpQ",
                        "Paul Bereza"
                },


                {       "elias-danilov",
                        "elias.danilov@outlook.it",
                        "33222200Shin",
                        "AQEDAUs6XDsBm0ZiAAABlLHEAU0AAAGU1dCFTU4AmJNLdMyqQkjC8aL0p0RuDPeIs1mIN-8ibFeUBETyfqgBIh5vcgRZEC3CT-vwFMsx_O5WI5f-w-kMNJggNqET8rvZYadcTN6Mqpr3Y7Kziw0xU12O|eyJsYW5ndWFnZXMiOlsiZGUtREUiLCJkZSIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJkZS1ERSIsInRpbWVab25lIjoiRXVyb3BlL0JlcmxpbiIsImlwIjoiMzcuMjAxLjE5NC4xNCJ9",
                        "Elias Danilov"
                },

                {       "stefania-mykhaylenko",
                        "mykhaylenko.stefania@outlook.fr",
                        "cTsH3KhU",
                        "AQEDAUxQ7yQFPqqQAAABlLdioOYAAAGU228k5lYAkxeM0zX7iffXvUyHtDQgPdzM9S7zUQhQH82TFWZ-deOSh1RvTfeBgOBDYufpVRgEFoyiP3oRwcHR-a-ybjARnhqudbbLQ25kbZRPewvOlLPidNw7|eyJsYW5ndWFnZXMiOlsiZnItRlIiLCJmciIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJmci1GUiIsInRpbWVab25lIjoiRXVyb3BlL1BhcmlzIiwiaXAiOiIxOTQuNjEuOS4xMCJ9",
                        "Mykhaylenko Stefania"
                },


                {       "patrick-yushko-b2080b2b8",
                        "yushko.patrick@outlook.it",
                        "206GLMC2",
                        "AQEFAHUBAAAAABBl9t0AAAGPfE7YuwAAAZMaRBSVVgAAGHVybjpsaTptZW1iZXI6MTI4MDAxMjQyNMyB0U3N4EXZoT7jRSgrh2ZsRQhw3dIPIjxxs7HK2bI8jIXKrSaNXE-7GhbppvBOQSO2mokFi0nLkNu11TQ3PPReQB8-2boUF0iWaZ7L3W1dFU9X07glDGQPpLsaMRPWQju-eZbs2y2zeE1w8P2PvROcYJDwbJXaXTTxwbFor9oT7iUhsfAU30z5UF7qX0VUHdnorZw",
                        "Yushko Patrick"
                },

                {       "daniele-tsvetkov",
                        "daniele.tsvetkov@outlook.it",
                        "33222200Shin",
                        "AQEDAUtiZkQEzpptAAABjYItJMIAAAGTGkMj1U0ANqMaAUhhe5JbhbT3ijdyc4v_4SRskGVnFTwCtKAhzkpj3VyapsTg4TKp3T5PHg_nN1KKDV2CcuIa7s6Wf9yY-YO8q_z4rJvA0RlrHTDDYsOjnFeS",
                        "Daniele Tsvetkov"
                },

                {       "michael-krusciov",
                        "michael.krusciov@outlook.de",
                        "cTsH3KhU",
                        "AQEDAUwy4cUBsAnPAAABk97M270AAAGUAtlfvU0A0hAgRax7EQj5rVJrY0UuWBlY92z21tNcwcsrBwl7OB4kltr0jRZtAAKKvgHAe39-JTxzTu-LFj0SnqmFW96bOJiGQduF3d2ECvJRGlJh2KMj-ySt",
                        "Michael Krusciov"
                },



                {       "Anastasiia-Vozniak",
                        "vozniakanastasia52@gmail.com",
                        "asd2424qq",
                        "AQEDATp4HzoBa6O7AAABlGlxPOgAAAGUjX3A6E0AwNpK6n3p3Ulp5DO-BwvtTrsYEBYy9LxAD6xohwUCfLQO52TrKY4CuGewUPx04ho-yh9X6s0CZ_L_uBtMC0MvER8xlAxfO5ENZX--bzIUQuvtufWI|eyJsYW5ndWFnZXMiOlsidWstVUEiLCJ1ayIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJ1ay1VQSIsInRpbWVab25lIjoiRXVyb3BlL0tpZXYiLCJpcCI6IjE4OC4xNjMuMjYuNjkifQ==",
                        "Anastasiia Vozniak"
                },
                {       "Art-Stenko",
                        "artstenko@gmail.com",
                        "GOgoCyclone_11",
                        "AQEDAR1-TWgCdaXbAAABlGmXZtsAAAGUjaPq200ANB_Ka-m2kerGHMufTimi96GJQqix1PhIG4UbsQO18B4Fx_BvCg6QYDwRf9t7z5r8HT7TLjo-SzkvuQefyC2E1uMaTLJMEb55CHzjrMVPyq2AnQCQ|eyJsYW5ndWFnZXMiOlsidWstVUEiLCJ1ayIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJ1ay1VQSIsInRpbWVab25lIjoiRXVyb3BlL0tpZXYiLCJpcCI6IjE4OC4xNjMuOTQuMCJ9",
                        "Art Stenko"
                },
                {       "lina Kompanets",
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
                        "AQEDATpm9GsDKEL5AAABlGmOfpAAAAGUjZsCkE0AaltIu290i86NYXPhN4JQpYmmU7CjynGnXnMys33nPKI1fBjCLhq09BcDd53S0DW1-hFjD4G0S8BhBYn2lJTWuh7tP1bkKdDBOWRFRYcQDJum62u6|eyJsYW5ndWFnZXMiOlsidWstVUEiLCJ1ayIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJ1ay1VQSIsInRpbWVab25lIjoiRXVyb3BlL0tpZXYiLCJpcCI6IjE4OC4xNjMuMTUuMTQ0In0=",
                        "Marian Reshetun"
                },
                {       "Anastasiia-Kuntii",
                        "anastasiiakuntii@gmail.com",
                        "33222200Shin",
                        "AQEDASosSSoEwgrNAAABlGmRGiUAAAGUjZ2eJU0AhC51KbwrLYzi4wa2ytQxdxJv4VpDm3awy0TQZEatEjsuQgmEZI5rzK1ANRo6I-kuNg72s-33zhWTr5kS4quQwdpXozxkrjzKyNkESDnlF0vDuas4|eyJsYW5ndWFnZXMiOlsidWstVUEiLCJ1ayIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJ1ay1VQSIsInRpbWVab25lIjoiRXVyb3BlL0tpZXYiLCJpcCI6IjE4OC4xNjMuMjguNTEifQ==",
                        "Anastasiia Kuntii "
                },
                {       "Natalia-Marcun",
                        "natalia.marcoon@gmail.com",
                        "asd321qq",
                        "AQEDATxzPCcDSaa2AAABlGmTliAAAAGUjaAaIE4AWy01oPscxLvE1AGPoHL1b-BM9xTko4B66dc5mgq9BXLfVA3_PgVtwp5_zNEEtAKpvant2d58dlQWprhSe1W83oGvH82--WTubb20UvdZPxeONqCd|eyJsYW5ndWFnZXMiOlsidWstVUEiLCJ1ayIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJ1ay1VQSIsInRpbWVab25lIjoiRXVyb3BlL0tpZXYiLCJpcCI6IjE4OC4xNjMuNjUuMTk0In0=",
                        "Natalia Marcun "
                },
                {       "Aleksandra-Sternenko",
                        "alexandra.sternenko@gmail.com",
                        "asd321qq",
                        "AQEDATxvso0AsoGrAAABlGmcpMYAAAGUjakoxk0At3rdIcYwg58nfPB100bIp55gaun_CLHbmRhw-J9lsRGXjBUy8peYKS15_zVsmeIbGpBPQzmETdRZntEpa4d7CQxIDcAy5Cn7m0nPv-__V08N5W8L|eyJsYW5ndWFnZXMiOlsidWstVUEiLCJ1ayIsImVuLVVTIiwiZW4iXSwibGFuZ3VhZ2UiOiJ1ay1VQSIsInRpbWVab25lIjoiRXVyb3BlL0tpZXYiLCJpcCI6IjE4OC4xNjMuODIuMTgxIn0=",
                        "Aleksandra Sternenko "
                }
        };
    }
}

