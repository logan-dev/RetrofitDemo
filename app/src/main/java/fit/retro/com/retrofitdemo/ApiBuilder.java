package fit.retro.com.retrofitdemo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;


import java.io.StringReader;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by LOGAN on 5/1/2016.
 */
public class ApiBuilder {

    private static Retrofit retrofit;
    private static String  BASE_URL="";// add your base URL here
    static ApiEndPoints apiEndPoints;

    public  static ApiEndPoints  getInstance(){

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setLenient()
                .create();

        OkHttpClient okHttpClient = new OkHttpClient();



        if(retrofit!=null){
            apiEndPoints = retrofit.create(ApiEndPoints.class);
            return apiEndPoints;
        }else{

            retrofit = new Retrofit.Builder()

                    .baseUrl(BASE_URL)
                    // set the okhttpclient and add default connect and read timepouts
                    .client(okHttpClient.newBuilder().connectTimeout(60,TimeUnit.SECONDS).readTimeout(30,TimeUnit.SECONDS).build())
                    .addConverterFactory(GsonConverterFactory.create(gson))

                    .build();
            // Create an instance of our GitHub API interface.
            apiEndPoints = retrofit.create(ApiEndPoints.class);
        }
        return apiEndPoints;
    }
}
