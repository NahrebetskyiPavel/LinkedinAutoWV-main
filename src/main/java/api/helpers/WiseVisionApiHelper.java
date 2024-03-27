package api.helpers;


import lombok.SneakyThrows;
import okhttp3.*;
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
    public String sentMsgImpasto(String profileId, String email, String password, String cookie, String spreadsheetUrl, String message){
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
                "\"message\": \""+message+"\"\n            " +
                "}\n        ]\n    },\n    " +
                "\"meta\": {\n      \"languages\":[\"ua-UA\",\"ua\"],\n      \"language\":\"ua-UA\",\n      \"timeZone\":\"Europe/Kiev\",\n      \"ip\":\"145.224.120.77\"\n    },\n    " +
                "\"callback\": \"https://test.com\"\n}"
        );
        Request request = new Request.Builder()
                .url("https://api.impasto.cpga.systems/api/impasto.script.message_sender")
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
    String resp =    sentMsgImpasto("alexey-fedotov-41a4a42b2","fedotov.alexey@outlook.de","33222200Shin","AQEDAUst11YFB92AAAABjnTUMkEAAAGOmOC2QVYAdaax2yBgrlTn3hSPMb4_zrML0lgNg8_Nq5qXmUj4QP3rTx_dyrF1eAEmzbwEXOL673FlZAdkRqyVZo-KhkJnl4RwmWtXS860aExDWmd0GZSiksPl","https://www.linkedin.com/in/pavel-nagrebetski/","Test");
        System.out.println(resp);
    }
}
