package br.fecap.pi.ubershield.network.frontend;
import android.content.Intent;
import android.widget.ImageButton;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;


import br.fecap.pi.ubershield.R;

public class UserConfigActivity extends AppCompatActivity {

    private ImageButton voltarBtn;
    EditText editContact1 = findViewById(R.id.editContact1);
    EditText editContact2 = findViewById(R.id.editContact2);
    EditText editContact3 = findViewById(R.id.editContact3);
    Button btnSaveContacts = findViewById(R.id.btnSaveContacts);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_config);

        SharedPreferences prefs = getSharedPreferences("EmergencyContacts", MODE_PRIVATE);
        editContact1.setText(prefs.getString("contact1", ""));
        editContact2.setText(prefs.getString("contact2", ""));
        editContact3.setText(prefs.getString("contact3", ""));

// Salva ao clicar
        btnSaveContacts.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("contact1", editContact1.getText().toString());
            editor.putString("contact2", editContact2.getText().toString());
            editor.putString("contact3", editContact3.getText().toString());
            editor.apply();

            Toast.makeText(this, "Contatos salvos!", Toast.LENGTH_SHORT).show();
        });

        //unico codigo, usei pro voltarBtn
        voltarBtn = findViewById(R.id.btnBack);
        //eeh, o codigo basico pra ir pra home ao clicar pra voltar
        voltarBtn.setOnClickListener(v -> {
            Intent intent = new Intent(UserConfigActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }


}