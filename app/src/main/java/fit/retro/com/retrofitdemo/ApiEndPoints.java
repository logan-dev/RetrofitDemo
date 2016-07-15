package fit.retro.com.retrofitdemo;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by LOGAN on 5/1/2016.
 */
public interface ApiEndPoints {


    @GET("/api/v1/user")
    Call<User> getUser();


    @FormUrlEncoded
    @POST("/api/v1/createUser")
    Call<User> createUserWithFieldParam(@Field("first_name")String fname,@Field("last_name") String lastname);

    @POST("/api/v1/createUserBody")
    Call<User> createUser(@Body User user);


    @PUT("/api/v1/updateUser")
    Call<User> updateUser(@Body User user);


    @Multipart
    @POST("/api/v1/uploadPic")
    Call<PicData> upload(@Part("description") RequestBody description,
                              @Part MultipartBody.Part file);
}
