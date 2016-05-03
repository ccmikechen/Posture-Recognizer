package mis.kuas.data;


import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by mingjia on 2016/4/7.
 */
public class StanceDataPoster {

    class PosterThread extends Thread {

        private String url;

        private String param;

        public PosterThread(String url, String param) {
            this.url = url;
            this.param = param;
        }

        public void run() {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, this.param);
            Request request = new Request.Builder()
                    .url(this.url)
                    .post(body)
                    .addHeader("cache-control", "no-cache")
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .build();

            try {
                Response response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("StancePost", "Post stance : " + this.param);
        }

    }

    private String url;

    public StanceDataPoster(String url) throws MalformedURLException {
        this.url = url;
    }

    public void send(String key, String value) {
        new PosterThread(this.url, key + "=" + value).start();
    }


}
