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
            .addFormDataPart("refresh_token","1000.5c16097e68bb85b70827854238182a8d.be312f9967b99c9aab09bfa3683d3126")
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
    public String changeLeadStatus(String leadId, String token, String transition_id){
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
        String responseBody = response.body().string();
        System.out.println(responseBody);
        return responseBody;
    }
    @SneakyThrows
    public String directChangeLeadStatus(String leadId, String token, String statusName){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"data\":[{\"Lead_Status\":\""+statusName+"\"}],\"formruleValue\":{\"mandatoryInputNeededElem\":[],\"lrMandatoryElem\":[],\"LayoutRuleHiddenElem\":[]}}");
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
        return response.body().string();
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
    @SneakyThrows
    public String getLeadInfoById(String token, String id){

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://crm.zoho.eu/crm/v2/Leads/"+id)
                .method("GET", null)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Cookie", "5ad188d5f9=0b1c728d9192d8d95f6e853307767d39; JSESSIONID=DFE1D3B3557FE73191D0807E48EA5E38; _zcsr_tmp=5b05ab02-20bd-49f1-b875-28359859b9f2; crmcsr=5b05ab02-20bd-49f1-b875-28359859b9f2")
                .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        return responseBody;
    }

@SneakyThrows
public String getLeadList(String token, String pickList, String status){
    OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
    MediaType mediaType = MediaType.parse("text/plain");
    RequestBody body = RequestBody.create(mediaType, "");
    Request request = new Request.Builder()
            .url("https://crm.zoho.eu/crm/v2/Leads/search?criteria=((Pick_List_2:equals:"+ pickList +")and(Lead_Status:equals:"+status+"))")
            .method("GET", null)
            .addHeader("Authorization", "Bearer " + token)
            .addHeader("Cookie", "5ad188d5f9=0b1c728d9192d8d95f6e853307767d39; JSESSIONID=AA5DC595CE762FED1E55A34A07767F17; _zcsr_tmp=5b05ab02-20bd-49f1-b875-28359859b9f2; crmcsr=5b05ab02-20bd-49f1-b875-28359859b9f2")
            .build();
    Response response = client.newCall(request).execute();
    String responseBody = response.body().string();

    return responseBody;
}
@SneakyThrows
public String getLeadList(String token, String pickList, String status, String linkedInAccount, int pagenum){
    OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
    MediaType mediaType = MediaType.parse("text/plain");
    RequestBody body = RequestBody.create(mediaType, "");
    Request request = new Request.Builder()
            .url("https://crm.zoho.eu/crm/v2/Leads/search?criteria=((Pick_List_2:equals:"+pickList+")and(Lead_Status:equals:"+status+")and(LinkedIn_person:starts_with:"+linkedInAccount+"))&page="+pagenum+"")
            .method("GET", null)
            .addHeader("Authorization", "Bearer " + token)
            .addHeader("Cookie", "5ad188d5f9=1086874d1f4b79ca34a3e357de49b547; _zcsr_tmp=9615c3e5-2287-4b6e-b579-a7ecab0f032b; crmcsr=9615c3e5-2287-4b6e-b579-a7ecab0f032b")
            .build();
    Response response = client.newCall(request).execute();
    String responseBody = response.body().string();

    return responseBody;
}
@SneakyThrows
public String getLeadList(String token, int page, String leadStatus, String linkedInPerson, String pickList2){
    OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
    MediaType mediaType = MediaType.parse("text/plain");
    RequestBody body = RequestBody.create(mediaType, "");
    Request request = new Request.Builder()
            .url("https://crm.zoho.eu/crm/v2/Leads/search?criteria=(" +
                    "(Lead_Status:equals:"+leadStatus+")" +
                    "and" +
                    "(LinkedIn_person:equals:"+linkedInPerson+")" +
                    "and" +
                    "(Pick_List_2:equals:"+pickList2+")" +
                    ")&page="+page+"")
            .method("GET", null)
            .addHeader("Authorization", "Bearer " + token)
            .addHeader("Cookie", "5ad188d5f9=e15fa1fb2ef12974089db8189e83c147; JSESSIONID=591C868A9EBD5A345F9CA1BB5B797A80; _zcsr_tmp=c1f27ecb-3f30-48fd-a5a9-15553eefd43c; crmcsr=c1f27ecb-3f30-48fd-a5a9-15553eefd43c")
            .build();
    Response response = client.newCall(request).execute();
    return response.body().string();
}
@SneakyThrows
public String getLeadList(String token, int page, String leadStatus, String linkedInPerson){
    OkHttpClient client = new OkHttpClient().newBuilder()
            .build();
    MediaType mediaType = MediaType.parse("text/plain");
    RequestBody body = RequestBody.create(mediaType, "");
    Request request = new Request.Builder()
            .url("https://crm.zoho.eu/crm/v2/Leads/search?criteria=(" +
                    "(Lead_Status:equals:"+leadStatus+")" +
                    "and" +
                    "(LinkedIn_person:equals:"+linkedInPerson+")" +
                    ")" +
                    "&page="+page+"")
            .method("GET", null)
            .addHeader("Authorization", "Bearer " + token)
            .addHeader("Cookie", "5ad188d5f9=e15fa1fb2ef12974089db8189e83c147; JSESSIONID=591C868A9EBD5A345F9CA1BB5B797A80; _zcsr_tmp=c1f27ecb-3f30-48fd-a5a9-15553eefd43c; crmcsr=c1f27ecb-3f30-48fd-a5a9-15553eefd43c")
            .build();
    Response response = client.newCall(request).execute();
    return response.body().string();
}

    @SneakyThrows
    public String getTaskList(String token){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://crm.zoho.eu/crm/v2/Tasks")
                .method("GET", null)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Cookie", "5ad188d5f9=0b1c728d9192d8d95f6e853307767d39; JSESSIONID=DFE1D3B3557FE73191D0807E48EA5E38; _zcsr_tmp=5b05ab02-20bd-49f1-b875-28359859b9f2; crmcsr=5b05ab02-20bd-49f1-b875-28359859b9f2")
                .build();
        Response response = client.newCall(request).execute();

        String responseBody = response.body().string();
        return responseBody;
    }

    @SneakyThrows
    public String getLeadTaskList(String leadId, String token){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://crm.zoho.eu/crm/v2/Leads/"+leadId+"/Tasks")
                .method("GET", null)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Cookie", "5ad188d5f9=1086874d1f4b79ca34a3e357de49b547; _zcsr_tmp=9615c3e5-2287-4b6e-b579-a7ecab0f032b; crmcsr=9615c3e5-2287-4b6e-b579-a7ecab0f032b")
                .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        return responseBody;
    }
    @Test
    public void test(){
        String token = this.renewAccessToken();
        String data =  this.getLeadList(token, "Yurij", "Contacted");
        System.out.println(data);
        System.out.println("||==================================================================||");
        JSONObject responseBodyJsonObject = new JSONObject( data );
        System.out.println(responseBodyJsonObject.getJSONArray("data").length());
        System.out.println(responseBodyJsonObject.getJSONArray("data").getJSONObject(199).getString("id"));
    }


    @Test
    public void test2(){
        String token = this.renewAccessToken();
        String data =  this.getTaskList(token);
        //System.out.println(data);
        System.out.println("||==================================================================||");
        JSONObject responseBodyJsonObject = new JSONObject( data );
        System.out.println(responseBodyJsonObject.getJSONArray("data").length());
        String id = responseBodyJsonObject.getJSONArray("data").getJSONObject(0).getJSONObject("What_Id").getString("id");
        System.out.println();
        String data2 =  this.getLeadInfoById(token,id) ;
        JSONObject responseBodyJsonObject2 = new JSONObject( data2 );
        System.out.println(data2);
        String web = String.valueOf(responseBodyJsonObject2.getJSONArray("data").getJSONObject(0));
        String web1 = String.valueOf(responseBodyJsonObject2.getJSONArray("data").getJSONObject(0).getString("Website"));
        System.out.println(web);
        System.out.println(web1);
        System.out.println(responseBodyJsonObject.getJSONArray("data").getJSONObject(0).getJSONObject("What_Id").getString("name"));
    }

    @Test
    public void test3(){
        String token = this.renewAccessToken();
        String data =  this.getLeadList(token, "Yurij", "Contacted","Михайло",0);
        //System.out.println(data);
        System.out.println("||==================================================================||");
        JSONObject responseBodyJsonObject = new JSONObject( data );
        //System.out.println(responseBodyJsonObject);
        System.out.println(responseBodyJsonObject.getJSONArray("data").length());
        for (int i = 0; i < 200; i++) {
            String id = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("id");
            String website = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Website");
            String fullName = responseBodyJsonObject.getJSONArray("data").getJSONObject(i).getString("Full_Name");
            System.out.println(id);
            System.out.println(fullName);
            System.out.println(website);
            String tasks = this.getLeadTaskList(id, token);
            JSONObject tasksData = new JSONObject( tasks );
            System.out.println(tasksData.getJSONArray("data"));
            System.out.println("tasksData length:"+tasksData.getJSONArray("data").length());
if (tasksData.getJSONArray("data").length() >0){
    for (int j = 0; j < tasksData.getJSONArray("data").length(); j++) {
        String status = tasksData.getJSONArray("data").getJSONObject(j).getString("Status");
        String subject = tasksData.getJSONArray("data").getJSONObject(j).getString("Subject");
        String taskId = tasksData.getJSONArray("data").getJSONObject(j).getString("id");
        System.out.println("taskId:" + taskId);
        System.out.println(status);
        System.out.println(subject);
                if (status.equals("Not Started") &&  subject.contains("Second message")){
            System.out.println("open" + website + " and sent second message to " + fullName);
        };
    }
}
            System.out.println();
            System.out.println("\n");
            System.out.println("====================================================================");
            System.out.println("\n");
        }


    }

        @SneakyThrows
        public String changeTaskStatus(String token, String taskID, String status) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");

            // Define the task data to update the status to "Closed"
            String taskData = "{\"data\":[{\"id\":\"" + taskID + "\",\"Status\":\""+status+"\"}]}";

            RequestBody body = RequestBody.create(mediaType, taskData);

            Request request = new Request.Builder()
                    .url("https://crm.zoho.eu/crm/v2/Tasks/" + taskID)
                    .method("PUT", body)
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            System.out.println(responseBody);
            return responseBody;
        }



    @Test
    public void getToken(){
        String token = this.renewAccessToken();
        String data = this.getLeadList( token, 1,  "Waiting",  "Anastasiia K.",  "Anastasia");
        System.out.println(new JSONObject( data ).getJSONArray("data").length());
        System.out.println(new JSONObject( data ).getJSONArray("data").getJSONObject(50).getString("id"));
        System.out.println(new JSONObject( data ).getJSONArray("data").getJSONObject(50).getString("Website"));
        }
    @Test
    public void getToken1(){
        String token = this.renewAccessToken();
      //  System.out.println(this.changeTaskStatus(token, "421659000009278012","Closed"));
        System.out.println(token);
      //  System.out.println(        this.getLeadList( "1000.e55641d1c24325701b254720c4a027d4.e4ae5ca1fe748b8b35963200e33b9468", 0,  "Waiting",  "Anastasiia") );
    }
}
