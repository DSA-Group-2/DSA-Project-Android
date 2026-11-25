package dsa.upc.edu.listapp.github;
import java.util.HashMap;

public class User {

    private String nif;
    private String name;
    private String surname;
    private String mail;
    private String password;
    private double dinero;
    private HashMap<String, Integer> inventario;

    public User(String nif, String name, String surname,
                String mail, String password, double dinero) {

        this.nif = nif;
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.password = password;
        this.dinero = dinero;
    }

    public double getDinero() {return dinero;}
    public HashMap<String, Integer> getInventario() {return inventario;}
}