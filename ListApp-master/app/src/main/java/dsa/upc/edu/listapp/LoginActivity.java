package dsa.upc.edu.listapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

import dsa.upc.edu.listapp.github.API;
import dsa.upc.edu.listapp.github.Credentials;
import dsa.upc.edu.listapp.github.UserApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class
LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button = (Button) findViewById(R.id.loginButton);
        Button goRegister = (Button) findViewById(R.id.registerButton2);

        goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText name = (EditText) findViewById(R.id.name);
                EditText password = (EditText) findViewById(R.id.password);

                final TextView textView = (TextView) findViewById(R.id.registerResult);

                if (name.getText().toString().isEmpty() ||
                        password.getText().toString().isEmpty()) {

                    textView.setText("Fill in all fields");
                    return;
                }

                Credentials credentials = new Credentials(name.getText().toString(),
                        password.getText().toString()
                );

                UserApi userService = API.getUserApi();
                Call<Void> call = userService.login(credentials);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (response.code() == 200) {
                            textView.setText("Correct login");
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                        else if (response.code() == 401) {
                            textView.setText("Incorrect username or password");
                        }
                        else {
                            textView.setText("Error" + response.code());
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