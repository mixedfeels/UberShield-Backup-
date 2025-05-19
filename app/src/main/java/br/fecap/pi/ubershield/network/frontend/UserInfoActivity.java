package br.fecap.pi.ubershield.network.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import br.fecap.pi.ubershield.R;

public class UserInfoActivity extends AppCompatActivity {

    private ImageButton btnVoltar;
    private RelativeLayout item_nome, item_genero, item_telefone, item_email, item_idioma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_info);

        btnVoltar = findViewById(R.id.btnVoltar);
        item_nome = findViewById(R.id.item_nome);
        item_genero = findViewById(R.id.item_genero);
        item_telefone = findViewById(R.id.item_telefone);
        item_email = findViewById(R.id.item_email);
        item_idioma = findViewById(R.id.item_idioma);

        btnVoltar.setOnClickListener(v -> {
            finish();
        });

        item_nome.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, UserNameActivity.class);
            startActivity(intent);
        });

        item_genero.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, UserGenerActivity.class);
            startActivity(intent);
        });

        item_telefone.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, UserPhoneActivity.class);
            startActivity(intent);
        });

        item_email.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, UserEmailActivity.class);
            startActivity(intent);
        });

        item_idioma.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, IdiomaActivity.class);
            startActivity(intent);
        });
    }
}