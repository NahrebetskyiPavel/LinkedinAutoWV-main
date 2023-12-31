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
        System.out.println();
        System.out.println(response);
    }



    @Test
    public void teest(){
        System.out.println(this.getUnprocessedLinks());
    }
}
