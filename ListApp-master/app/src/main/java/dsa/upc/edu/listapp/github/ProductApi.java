package dsa.upc.edu.listapp.github;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductApi {
    @GET("product")
    Call<List<Product>> products(
         );

}