package smith.lib.net.imguruploader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class SImgurUploader {
    
    private final OkHttpClient client = new OkHttpClient();
    
    public static final MediaType MEDIA_TYPE_PNG = MediaType.get("image/png");
    public static final MediaType MEDIA_TYPE_JPG = MediaType.get("image/jpg");
    
    private MediaType mediaType;
    
    private String clientId;
    private String accessToken;
    
    private int maxRetries;
    
    public SImgurUploader(String clientId, String accessToken) {
        this.clientId = clientId;
        this.accessToken = accessToken;
        this.mediaType = MEDIA_TYPE_PNG;
    }
    
    public void setMaxRetries(int retries) {
        this.maxRetries = retries;
    }
    
    public void setMediaType(MediaType type) {
        this.mediaType = type;
    }
    
    public void uploadImage(File file, SImgurResponse imgurResponse) {
        String url = "https://api.imgur.com/3/image";
        RequestBody requestBody = new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("image", file.getName(),
                RequestBody.create(file, mediaType))
            .build();

        Request request = new Request.Builder()
            .header("Authorization", "Bearer " + accessToken)
            .header("Client-ID", "Client-ID " + clientId)
            .url(url)
            .post(requestBody)
            .build();

        int retries = 0;
        while (retries < maxRetries) {
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        imgurResponse.onError(json.getString("error"));
                    } catch (JSONException e) {
                        retries++;
                        if (retries == maxRetries) {
                            imgurResponse.onError("JSONException: " + e.getMessage());
                        }
                    }
                } else {
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        imgurResponse.onSuccess(json.getJSONObject("data").getString("link"));
                    } catch (JSONException e) {
                        retries++;
                        if (retries == maxRetries) {
                            imgurResponse.onError("JSONException: " + e.getMessage());
                        }
                    }
                }
                break;
            } catch (IOException e) {
                retries++;
                if (retries == maxRetries) {
                    imgurResponse.onError("IOException: " +e.getMessage());
                }
            }
        }
    }
}
