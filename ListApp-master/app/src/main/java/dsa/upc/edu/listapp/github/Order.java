package dsa.upc.edu.listapp.github;

import java.util.HashMap;

public class Order {

    public String dni; //id del usuario de la compra
    public HashMap<String, Integer> productList; //inventario

    public Order(String dni, String idProducto) {

        this.dni = dni;

        productList = new HashMap<>();
        productList.put(idProducto, 1);
    }
}
