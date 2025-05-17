package com.seuapp.uberShield;

import static android.os.Build.VERSION_CODES.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EmailActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email); // use o nome correto do seu XML

        // Referências aos elementos
        editTextEmail = findViewById(R.id.editTextEmail);
        backButton = findViewById(R.id.back_button);
        Button btnAtualizar = findViewById(R.id.btnAtualizar);

        // Ação para botão de voltar
        backButton.setOnClickListener(view -> finish());

        // Ação do botão Atualizar
        btnAtualizar.setOnClickListener(view -> {
            String email = editTextEmail.getText().toString().trim();
            if (!email.isEmpty()) {
                // Aqui você pode salvar o email em SharedPreferences, Firebase, etc.
                Toast.makeText(EmailActivity.this, "E-mail atualizado: " + email, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EmailActivity.this, "Digite um e-mail válido", Toast.LENGTH_SHORT).show();
            }
        });

        // Detectar clique no "X" para limpar (opcional, pois o ícone não tem função nativa)
        editTextEmail.setOnTouchListener((v, event) -> {
            final int DRAWABLE_END = 2;
            if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (editTextEmail.getRight() - editTextEmail.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
                    editTextEmail.setText(""); // limpa o texto
                    return true;
                }
            }
            return false;
        });
    }
}
