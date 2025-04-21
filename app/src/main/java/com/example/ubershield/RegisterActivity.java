package com.example.ubershield;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ubershield.network.ApiService;
import com.example.ubershield.network.Encriptador.PasswordHasher;

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        // tudo que ocorre aqui é a ligacao do front com o back.
        usernameEditText = findViewById(R.id.RegisterUsername);
        emailEditText = findViewById(R.id.RegisterEmail);
        passwordEditText = findViewById(R.id.RegisterSenha);
        confirmPasswordEditText = findViewById(R.id.RegisterConfirmarSenha);
        registerButton = findViewById(R.id.login_btn);

        //formatando a informacao do front end para ser utilizavel em string aqui
        registerButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();


            //check se as senhas coincidem ou se os campos nao estao preenchidos
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
            // VERIFICACAO DE EMAIL DO ANDROID. NAO UTILIZAR ATE O FINAL DO APP.
            /*} else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show();*/
            } else {
                registerUser(username, email, password);
            }
        });
    }


    // encriptador
    private void registerUser(String username, String email, String password) {
        String salt = UUID.randomUUID().toString();
        String hashedPassword = PasswordHasher.hashPassword(password, salt);

        ApiService.criarUsuario(
                username,
                email,
                hashedPassword,
                salt,
                new ApiService.Callback() {
                    @Override
                    public void onResponse(String status, String message) {
                        runOnUiThread(() -> {
                            if ("success".equals(status)) {
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                            }
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        runOnUiThread(() ->
                                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show());
                    }
                }
        );
    }
}