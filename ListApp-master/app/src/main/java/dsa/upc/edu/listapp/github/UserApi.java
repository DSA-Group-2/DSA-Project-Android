package dsa.upc.edu.listapp.github;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import java.util.HashMap;
import retrofit2.http.GET;
import retrofit2.http.Path;
public interface UserApi {

    @POST("user/register")
    Call<Void> register(@Body User user);

    @POST("user/login")
    Call<HashMap<String, String>> login(@Body Credentials credentials);

    @POST("user/order")
    Call<Order> comprar(@Body Order pedido);

    @GET("user/{nif}")
    Call<User> getUser(@Path("nif") String nif);

}