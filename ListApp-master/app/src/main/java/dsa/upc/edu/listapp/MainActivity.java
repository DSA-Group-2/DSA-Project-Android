package dsa.upc.edu.listapp;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.Toast;
import android.widget.TextView;

import java.util.List;
import java.util.HashMap;


import dsa.upc.edu.listapp.github.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Based on http://www.vogella.com/tutorials/AndroidRecyclerView/article.html
//      and https://zeroturnaround.com/rebellabs/getting-started-with-retrofit-2/

public class MainActivity extends AppCompatActivity {

    //usuario a mano para las pruebas
    String usuario = "NIF1";
    double monedas = 100;
    HashMap<String, Integer> inventario = new HashMap<>();

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //guarda usuario y monedas en una variable
        TextView txtUsuario = (TextView) findViewById(R.id.txtUsuario);
        TextView txtMonedas = (TextView) findViewById(R.id.txtMonedas);

        txtUsuario.setText("Usuario: " + usuario);
        txtMonedas.setText("Monedas: " + monedas);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        swipeRefreshLayout = findViewById(R.id.my_swipe_refresh);

        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        // Set the adapter
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);

        doApiCall(null);

        // Manage swipe on items
//        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
//                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//                    @Override
//                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder
//                            target) {
//                        return false;
//                    }
//                    @Override
//                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
//                        adapter.remove(viewHolder.getAdapterPosition());
//                    }
//                };
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
//        itemTouchHelper.attachToRecyclerView(recyclerView);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        doApiCall(swipeRefreshLayout);
                    }
                }
        );

    }

    private void doApiCall(final SwipeRefreshLayout mySwipeRefreshLayout) {
        ProductApi gitHubService = API.getGithub();
        Call<List<Product>> call = gitHubService.products();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                // set the results to the adapter
                adapter.setData(response.body());

                if(mySwipeRefreshLayout!=null) mySwipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                if(mySwipeRefreshLayout!=null) mySwipeRefreshLayout.setRefreshing(false);

                String msg = "Error in retrofit: "+t.toString();
                Log.d(TAG,msg);
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
            }
        });
    }

    public void comprarProducto(Product producto) {

        if (monedas >= producto.price) {

            enviarPedido(producto);

        } else {

            Toast.makeText(this, "No tienes suficientes monedas", Toast.LENGTH_SHORT).show();

        }
    }
    public void mostrarInventario() {

        TextView txtInventario = (TextView) findViewById(R.id.txtInventario);

        String texto = "Inventario:\n";

        for (String producto : inventario.keySet()) {
            texto = texto + producto + " x" + inventario.get(producto) + "\n";
        }

        txtInventario.setText(texto);
    }

    //enviar pedido a la API REST
    public void enviarPedido(Product producto) {

        Order pedido = new Order(usuario, producto.id);

        UserApi userApi = API.getUserApi();

        Call<Order> call = userApi.comprar(pedido);

        call.enqueue(new Callback<Order>() {

            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                Log.d("COMPRA", "Codigo recibido: " + response.code());

                if (response.code() == 201) {

                    monedas = monedas - producto.price;

                    TextView txtMonedas = (TextView) findViewById(R.id.txtMonedas);
                    txtMonedas.setText("Monedas: " + monedas);

                    //añadir producto al inventario
                    if (inventario.containsKey(producto.name)) {
                        inventario.put(producto.name, inventario.get(producto.name) + 1);
                    } else {
                        inventario.put(producto.name, 1);
                    }

                    mostrarInventario();

                    Toast.makeText(MainActivity.this, "Has comprado " + producto.name, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Log.d("COMPRA", "Error: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
