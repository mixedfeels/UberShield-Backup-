package br.fecap.pi.ubershield.network.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;

import br.fecap.pi.ubershield.R;

public class UserConfigActivity extends AppCompatActivity {

    // Botão para voltar à tela principal
    private ImageButton voltarBtn;

    // Campos para inserir os contatos de emergência
    private EditText editContact1;
    private EditText editContact2;
    private EditText editContact3;

    // Botão para salvar os contatos
    private Button btnSaveContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_config);

        // Inicializa os campos de contato
        editContact1 = findViewById(R.id.editContact1);
        editContact2 = findViewById(R.id.editContact2);
        editContact3 = findViewById(R.id.editContact3);

        // Inicializa o botão de salvar
        btnSaveContacts = findViewById(R.id.btnSaveContacts);

        // Recupera os contatos salvos anteriormente
        SharedPreferences prefs = getSharedPreferences("EmergencyContacts", MODE_PRIVATE);
        editContact1.setText(prefs.getString("contact1", ""));
        editContact2.setText(prefs.getString("contact2", ""));
        editContact3.setText(prefs.getString("contact3", ""));

        // Salva os contatos ao clicar no botão
        btnSaveContacts.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("contact1", editContact1.getText().toString());
            editor.putString("contact2", editContact2.getText().toString());
            editor.putString("contact3", editContact3.getText().toString());
            editor.apply();

            Toast.makeText(this, "Contatos salvos!", Toast.LENGTH_SHORT).show();
        });

        // Botão para voltar à tela principal
        voltarBtn = findViewById(R.id.btnBack);
        voltarBtn.setOnClickListener(v -> {
            Intent intent = new Intent(UserConfigActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
