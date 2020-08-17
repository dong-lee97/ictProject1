package com.example.ictproject;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ictproject.upload.CompanyUpload;
import com.example.ictproject.upload.Upload;
import com.google.gson.Gson;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendNotification {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static void sendNotification(final String regToken, final String title, final String message, final Object object){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... parms) {
                try {
                    String type = "";
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    Gson gson = new Gson();
                    String data = gson.toJson(object);
                    dataJson.put("data", data);

                    if(object instanceof CompanyUpload) { //회사에서 연락할 경우
                        type = "companyUpload";
                        dataJson.put("type", type);
                    } else if(object instanceof Upload) { //개인이 연락할 경우
                        type = "upload";
                        dataJson.put("type", type);
                    }
                    json.put("data", dataJson);

                    JSONObject notiJson = new JSONObject();
                    notiJson.put("body", message);
                    notiJson.put("title", title);
                    json.put("notification", notiJson);
                    json.put("to", regToken);
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization", "key=" + "AAAA0TV_NE0:APA91bFvLLU3K1RgqFKR5G4n0ND8SazhR3krVs2ZEiryZson" +
                                    "XMvo9RfD_96opBty1v2ICEhtNKlfiQyP7JfsdrBQyworXWmJvK92jNKjfNm9kAaSQCSIptRkcvhTEZFcCyBvVUlxn3w2")
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                }catch (Exception e){
                    Log.d("error",e+"");
                }
                return null;
            }
        }.execute();
    }
}
