package com.example.ubershield;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ubershield.network.Encriptador.PasswordHasher;
import org.json.JSONObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.Username);
        passwordEditText = findViewById(R.id.RegisterSenha);
        loginButton = findViewById(R.id.login_btn);
        registerButton = findViewById(R.id.register_btn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim(); // formatacao do user, trim pra tirar espaco
                String password = passwordEditText.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    fazerLoginFinal(username, password); // Chama a função de login
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    // requisição de login PELO AMOR NAOOOO TIRAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA <3
    private void fazerLoginFinal(String username, String password) {
        OkHttpClient client = new OkHttpClient();
        String saltUrl = "https://ubershieldAPP.azurewebsites.net/buscarSalt";

        RequestBody body = new FormBody.Builder()
                .add("nome", username)
                .build();

        Request saltRequest = new Request.Builder()
                .url(saltUrl)
                .post(body)
                .build();

        client.newCall(saltRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Erro ao buscar salt", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        JSONObject json = new JSONObject(responseBody);
                        String salt = json.getString("salt");

                        // Faz o hash com sua classe
                        String senhaHash = PasswordHasher.hashPassword(password, salt);

                        // Agora envia para o backend
                        fazerLoginComHash(username, senhaHash);
                    } catch (Exception e) {
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Erro ao processar salt", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Usuário não encontrado", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void fazerLoginComHash(String username, String senhaHash) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://ubershieldAPP.azurewebsites.net/user";

        RequestBody formBody = new FormBody.Builder()
                .add("nome", username)
                .add("senha_hash", senhaHash)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivity.this, "Login realizado com sucesso", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Login falhou: " + response.message(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

}
