package dsa.upc.edu.listapp.github;

public class User {

    private String nif;
    private String name;
    private String surname;
    private String mail;
    private String password;
    private int dinero;

    public User(String nif, String name, String surname,
                String mail, String password, int dinero) {

        this.nif = nif;
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.password = password;
        this.dinero = dinero;
    }
}