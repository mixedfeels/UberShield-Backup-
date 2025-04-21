package com.example.ubershield;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private ImageButton accountButton, homeButton, configButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referencia dos botoes
        accountButton = findViewById(R.id.accountbutton);
        homeButton = findViewById(R.id.homebutton);
        configButton = findViewById(R.id.configbutton);

        // aqui eu configuro o botao homeButton para sempre ir pra tela de config do usuario ( copia e cola se tiver um accountButton na sua activity )
        accountButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UserConfigActivity.class);
            startActivity(intent);
        });

        // aqui eu configuro padrao pra o botao homeButton sempre voltar pra MainActivity ( tela principal do nosso app ) COPIA E COLA NA SUA ACTIVITY CASO TENHA UM homeButton tb
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // remove a tela antiga da pilha
        });

        // mesma coisa dos de cima, só que pra configuracoes do app.
        configButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AppConfigActivity.class);
            startActivity(intent);
        });
    }
}
