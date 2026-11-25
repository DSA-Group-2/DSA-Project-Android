package dsa.upc.edu.listapp.github;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {

    @POST("user/register")
    Call<Void> register(@Body User user);

}