package api.helpers;


import lombok.SneakyThrows;
import okhttp3.*;
import org.json.JSONObject;
import org.testng.annotations.Test;
public class WiseVisionApiHelper {
    @SneakyThrows
    public String getUnprocessedLinks(){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://zoho.wisevision.pp.ua/linkedin-url/unprocessed-link")
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    @SneakyThrows
    public void postLinkedinPersonData(String linkedinUrl, String personName, String aboutPerson, String workHistory,String location){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    " +
                "\"linkedinUrl\": \""+linkedinUrl.replace("\n","")+"\",\n    " +
                "\"personName\": \""+personName.replace("\n","")+"\",\n    " +
                "\"aboutPerson\": \""+aboutPerson.replace("\n","")+"\",\n    " +
                "\"location\": \""+location.replace("\n","")+"\",\n    " +
                "\"workHistory\": \""+workHistory.replace("\n","")+"\"\n}");
        Request request = new Request.Builder()
                .url("https://zoho.wisevision.pp.ua/linkedin")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();

    }
    @SneakyThrows
    public void SendMsgToTelegram(String chatId, String botToken, String text){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    \"chat_id\": \""+chatId+"\",\n    \"text\": \""+text+"\",\n    \"disable_notification\": false\n}");
        Request request = new Request.Builder()
                .url("https://api.telegram.org/bot"+botToken+"/sendMessage")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();

    }
    @SneakyThrows
    public String impastoAddToFriends(String profileId, String email, String password, String cookie, String spreadsheetUrl){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    " +
                "\"profileId\": \""+profileId+"\",\n    " +
                "\"data\": {\n        " +
                "\"email\": \""+email+"\",\n        " +
                "\"password\": \""+password+"\",\n        " +
                "\"cookie\": \""+cookie+"\",\n        " +
                "\"messages\": [\n\n            {\n                " +
                "\"spreadsheetUrl\": \""+spreadsheetUrl+"/\"\n            }\n            \n        ]\n    },\n    " +
                "\"meta\": " +
                "{\n      " +
                "\"languages\":[\"ua-UA\",\"ua\"],\n      " +
                "\"language\":\"ua-UA\",\n      " +
                "\"timeZone\":\"Europe/Kiev\",\n      " +
                "\"ip\":\"145.224.120.77\"\n    },\n    " +
                "\"callback\": \"https://test.com\"\n}");
        Request request = new Request.Builder()
                .url("https://api.impasto.cpga.systems/api/impasto.script.auto_connect")
                .method("POST", body)
                .addHeader("Authorization", "b9cb85f7-3211-4e13-b45f-748cbbc71bc1")
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println(responseBody);
        return responseBody;
    }

    @SneakyThrows
    public String impastoAddToFriends(String profileId, String email, String password, String cookie, String spreadsheetUrl, String inviteMsg){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    " +
                "\"profileId\": \""+profileId+"\",\n    " +
                "\"data\": {\n        " +
                "\"email\": \""+email+"\",\n        " +
                "\"password\": \""+password+"\",\n        " +
                "\"cookie\": \""+cookie+"\",\n        " +
                "\"messages\": [\n            {\n                " +
                "\"spreadsheetUrl\": \""+spreadsheetUrl+"/\",\n                " +
                "\"message\": \""+inviteMsg+"\"\n            " +
                "}\n            \n        ]\n    },\n    " +
                "\"meta\": {\n      \"languages\":[\"ua-UA\",\"ua\"],\n      \"language\":\"ua-UA\",\n      \"timeZone\":\"Europe/Kiev\",\n      \"ip\":\"145.224.120.77\"\n    },\n    " +
                "\"callback\": \"https://test.com\"\n" +
                "}");
        Request request = new Request.Builder()
                .url("https://api.impasto.cpga.systems/api/impasto.script.auto_connect")
                .method("POST", body)
                .addHeader("Authorization", "b9cb85f7-3211-4e13-b45f-748cbbc71bc1")
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println(responseBody);
        return responseBody;
    }

    @SneakyThrows
    public String impastoGetTaskinfo(String profileId, int tasId){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://api.impasto.cpga.systems/api/task/"+tasId+"/info/"+profileId+"")
                .method("POST", body)
                .addHeader("Authorization", "b9cb85f7-3211-4e13-b45f-748cbbc71bc1")
                .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println(responseBody);
        return responseBody;
    }



    @Test
    public void teest(){
    //  String data =  impastoGetTaskinfo("alexey-fedotov-41a4a42b2", 307);
      //  System.out.println(new JSONObject( data ).getString("status"));

  String a = impastoAddToFriends("alexey-fedotov-41a4a42b2", "fedotov.alexey@outlook.de", "33222200Shin", "AQEDAUst11YDXkwtAAABjnWOXgkAAAGOmZriCU0AtuKG_eCf1YjB0qq1iJOy2u8pEpMBJD3vUW7eOV7cjVEkEoyTiU3TX_9cs5fUcXjKXxrCxSKbE3MihEPEUG972caFxWvP_tXkX_q5OAjvRcyflMR5", "AQEDAUst11YDXkwtAAABjnWOXgkAAAGOmZriCU0AtuKG_eCf1YjB0qq1iJOy2u8pEpMBJD3vUW7eOV7cjVEkEoyTiU3TX_9cs5fUcXjKXxrCxSKbE3MihEPEUG972caFxWvP_tXkX_q5OAjvRcyflMR5");
        System.out.println();
    }
}
