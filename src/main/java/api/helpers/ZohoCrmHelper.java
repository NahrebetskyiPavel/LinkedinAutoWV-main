package api.helpers;


import lombok.SneakyThrows;
import okhttp3.*;
import org.json.JSONObject;
import org.testng.annotations.Test;

public class ZohoCrmHelper {
    public String responseBody;
    @SneakyThrows
    public JSONObject AddLeadToCRM(String Last_Name, String token, String pickList, String LinkedInLink, String leadStatus, String leadCompany, String leadCompanyId){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain;charset=UTF-8");
        RequestBody body = RequestBody.create(mediaType, "{\n    " +
                "\"data\": [\n        " +
                "{\n            " +
                "\"Owner\": {\n                " +
                "\"id\": \"421659000000609236\",\n                " +
                "\"full_name\": \"Anastasiia Kuntii\"\n            },\n            " +
                "\"Lead_Status\": \""+leadStatus+"\",\n            " +
                "\"Email_Opt_Out\": false,\n            " +
                "\"Lead_Source\": \"Sales navigator\",\n            " +
                "\"Industry\": \"-None-\",\n            \"TechStack\": \"-None-\",\n            " +
                "\"Leadscompany\": \"-None-\",\n            " +
                "\"Pick_List_2\": \""+pickList+"\",\n            " +
                "\"OlyaPick\": \"-None-\",\n            " +
                "\"Rating\": \"-None-\",\n            " +
                "\"Company\": \"Test\",\n            " +
                "\"Last_Name\": \""+Last_Name+"\",\n            " +
                "\"LinkedIn_person\": \"-None-\",\n            " +
                "\"Website\": \""+LinkedInLink+"/\",\n            " +
                "\"LeadTestCompany\": " +
                "{\n                " +
                "\"id\": \""+leadCompanyId+"\",\n                " +
                "\"name\": \""+leadCompany+"\"\n            " +
                "}\n        }\n    ],\n    " +
                "\"skip_mandatory\": false\n}");
        Request request = new Request.Builder()
                .url("https://crm.zoho.eu/crm/v2/Leads")
                .method("POST", body)
                .addHeader("X-CRM-ORG", "20080256708")
                .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"")
                .addHeader("X-ZCSRF-TOKEN", "crmcsrfparam=663491d1093474ddbc431314984d718b41e6226cc6c2d80a722b8851700d5e5af060d0a3684867deafd86dc2f2a3dcff1b4d25397c386aa0096106749d699a2e")
                .addHeader("Content-Type", "text/plain;charset=UTF-8")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
                .addHeader("sec-ch-ua-platform", "\"macOS\"")
                .addHeader("Accept", "*/*")
                .addHeader("host", "crm.zoho.eu")
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Cookie", "5ad188d5f9=6dfb6a24a3f5a5d1fd2626c85de190c3; _zcsr_tmp=9e1a5efd-1a1b-4fb5-8601-38f6f471d50e; crmcsr=9e1a5efd-1a1b-4fb5-8601-38f6f471d50e")
                .build();
        Response response = client.newCall(request).execute();
        responseBody = response.body().string();
        JSONObject responseBodyJsonObject = new JSONObject(responseBody);
        return responseBodyJsonObject;
    }

    @SneakyThrows
    public String AddLeadToCRM(String Last_Name, String token, String pickList, String LinkedInLink, String leadStatus, String leadCompany, String leadCompanyId, String accountname){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain;charset=UTF-8");
        RequestBody body = RequestBody.create(mediaType, "{\n    " +
                "\"data\": [\n        " +
                "{\n            " +
                "\"Owner\": {\n                " +
                "\"id\": \"421659000000609236\",\n                " +
                "\"full_name\": \"Anastasiia Kuntii\"\n            },\n            " +
                "\"Lead_Status\": \""+leadStatus+"\",\n            " +
                "\"Email_Opt_Out\": false,\n            " +
                "\"Lead_Source\": \"Sales navigator\",\n            " +
                "\"Industry\": \"-None-\",\n            \"TechStack\": \"-None-\",\n            " +
                "\"Leadscompany\": \"-None-\",\n            " +
                "\"Pick_List_2\": \""+pickList+"\",\n            " +
                "\"OlyaPick\": \"-None-\",\n            " +
                "\"Rating\": \"-None-\",\n            " +
                "\"Company\": \"Test\",\n            " +
                "\"Last_Name\": \""+Last_Name+"\",\n            " +
                "\"LinkedIn_person\": \""+accountname+"\",\n            " +
                "\"Website\": \""+LinkedInLink+"/\",\n            " +
                "\"LeadTestCompany\": " +
                "{\n                " +
                "\"id\": \""+leadCompanyId+"\",\n                " +
                "\"name\": \""+leadCompany+"\"\n            " +
                "}\n        }\n    ],\n    " +
                "\"skip_mandatory\": false\n}");
        Request request = new Request.Builder()
                .url("https://crm.zoho.eu/crm/v2/Leads")
                .method("POST", body)
                .addHeader("X-CRM-ORG", "20080256708")
                .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"")
                .addHeader("X-ZCSRF-TOKEN", "crmcsrfparam=663491d1093474ddbc431314984d718b41e6226cc6c2d80a722b8851700d5e5af060d0a3684867deafd86dc2f2a3dcff1b4d25397c386aa0096106749d699a2e")
                .addHeader("Content-Type", "text/plain;charset=UTF-8")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
                .addHeader("sec-ch-ua-platform", "\"macOS\"")
                .addHeader("Accept", "*/*")
                .addHeader("host", "crm.zoho.eu")
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Cookie", "5ad188d5f9=6dfb6a24a3f5a5d1fd2626c85de190c3; _zcsr_tmp=9e1a5efd-1a1b-4fb5-8601-38f6f471d50e; crmcsr=9e1a5efd-1a1b-4fb5-8601-38f6f471d50e")
                .build();
        Response response = client.newCall(request).execute();
        responseBody = response.body().string();
        //JSONObject responseBodyJsonObject = new JSONObject(responseBody);

        return responseBody;
    }

    @SneakyThrows
    public String renewAccessToken(){
    OkHttpClient client = new OkHttpClient().newBuilder().build();
    MediaType mediaType = MediaType.parse("text/plain");
    RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("client_id","1000.OFY1FUCKNY0TTZPI4DA1P2OGP2ULQI")
            .addFormDataPart("client_secret","e35590f7027c17fb04299e5b8b68da2393100d6f4f")
            .addFormDataPart("refresh_token","1000.5c9f82e89d0f0ec2ace60e78ea322613.a7971d9304333e3da4c662af4b63f3c8")
            .addFormDataPart("grant_type","refresh_token")
            .build();
    Request request = new Request.Builder()
            .url("https://accounts.zoho.eu/oauth/v2/token")
            .method("POST", body)
            .addHeader("Cookie", "_zcsr_tmp=e0dd0a91-84bb-4a94-9162-0f0aa453b633; d4bcc0a499=953809aceaa07cf41469153cac12ef23; iamcsr=e0dd0a91-84bb-4a94-9162-0f0aa453b633")
            .build();
    Response response = client.newCall(request).execute();
    String responseBody = response.body().string();
    JSONObject responseBodyJsonObject = new JSONObject(responseBody);
    return (String) responseBodyJsonObject.get("access_token");
}

    @SneakyThrows
    public Response changeLeadStatus(String leadId, String token, String transition_id){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "\n{\n    \"blueprint\": [\n       {\n            \"transition_id\": \"" + transition_id + "\"\n        }\n    ]\n}\n");
        Request request = new Request.Builder()
                .url("https://crm.zoho.eu/crm/v2/Leads/"+leadId+"/actions/blueprint")
                .method("PUT", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Cookie", "5ad188d5f9=6292d2984ef0821bee3338d1fd76c022; JSESSIONID=3CE4C543322B328AA0D63C186786DDB3; _zcsr_tmp=c3665641-3d33-48e0-90e7-b87732b42ac0; crmcsr=c3665641-3d33-48e0-90e7-b87732b42ac0")
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
        return response;
    }

    @SneakyThrows
    public void changeLeadStatus(String leadId, String token){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"data\":[{\"Lead_Status\":\"Contacted\"}],\"formruleValue\":{\"mandatoryInputNeededElem\":[],\"lrMandatoryElem\":[],\"LayoutRuleHiddenElem\":[]}}");
        Request request = new Request.Builder()
                .url("https://crm.zoho.eu/crm/v2/Leads/"+leadId+"?affected_data=true")
                .method("PUT", body)
                .addHeader("sec-ch-ua", "\"Chromium\";v=\"116\", \"Not)A;Brand\";v=\"24\", \"Google Chrome\";v=\"116\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36")
                .addHeader("Content-Type", "application/json")
                .addHeader("X-CRM-ORG", "20080256708")
                .addHeader("Accept", "*/*")
                .addHeader("X-ZCSRF-TOKEN", "crmcsrfparam=0beba5d8274d7941d891187c60526803d62a97b4c0c21d8d1ccecfa5a0aea3bcacae8ec358fffd0a3f94a1f360e85d50bd87ed0a98496a3adad4b6f3b5ef3ebb")
                .addHeader("X-Client-SubVersion", "35140cbf0cd282bcd24017da374dae75")
                .addHeader("X-Requested-With", "XMLHttpRequest, XMLHttpRequest")
                .addHeader("X-Static-Version", "6999993")
                .addHeader("sec-ch-ua-platform", "\"macOS\"")
                .addHeader("host", "crm.zoho.eu")
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Cookie", "5ad188d5f9=602c68540f2fb9aa8d48ce31dfa8d025; _zcsr_tmp=34c9ebdd-e968-441c-9fae-a3a56dd240bb; crmcsr=34c9ebdd-e968-441c-9fae-a3a56dd240bb")
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }
    @SneakyThrows
    public String getLeadInfoByFullName(String token, String fullName){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://crm.zoho.eu/crm/v2/Leads/search?criteria=((Full_Name:equals:"+fullName+"))&=")
                .method("GET", null)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Cookie", "5ad188d5f9=6292d2984ef0821bee3338d1fd76c022; JSESSIONID=39CD4FB255DE759F7C10D7D57D4912B3; _zcsr_tmp=c3665641-3d33-48e0-90e7-b87732b42ac0; crmcsr=c3665641-3d33-48e0-90e7-b87732b42ac0")
                .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        return responseBody;
    }

    @Test
    public void getToken(){
        System.out.println(this.renewAccessToken());
        }
}
