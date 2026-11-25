package dsa.upc.edu.listapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import dsa.upc.edu.listapp.github.API;
import dsa.upc.edu.listapp.github.User;
import dsa.upc.edu.listapp.github.UserApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button button = (Button) findViewById(R.id.registerButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText nif = (EditText) findViewById(R.id.nif);
                EditText name = (EditText) findViewById(R.id.name);
                EditText surname = (EditText) findViewById(R.id.surname);
                EditText mail = (EditText) findViewById(R.id.mail);
                EditText password = (EditText) findViewById(R.id.password);

                final TextView textView = (TextView) findViewById(R.id.registerResult);

                if (nif.getText().toString().isEmpty() ||
                        name.getText().toString().isEmpty() ||
                        surname.getText().toString().isEmpty() ||
                        mail.getText().toString().isEmpty() ||
                        password.getText().toString().isEmpty()) {

                    textView.setText("Fill in all fields");
                    return;
                }

                User user = new User(
                        nif.getText().toString(),
                        name.getText().toString(),
                        surname.getText().toString(),
                        mail.getText().toString(),
                        password.getText().toString(),
                        0
                );

                UserApi userService = API.getUserApi();
                Call<Void> call = userService.register(user);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (response.code() == 201) {
                            textView.setText("User registered");
                        }
                        else if (response.code() == 409) {
                            textView.setText("User already exists");
                        }
                        else {
                            textView.setText("Registration error");
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        textView.setText("Something went wrong: " + t.getMessage());
                    }
                });
            }
        });
    }
}