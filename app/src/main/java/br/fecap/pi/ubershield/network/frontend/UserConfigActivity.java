package br.fecap.pi.ubershield.network.frontend;
import android.content.Intent;
import android.widget.ImageButton;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import br.fecap.pi.ubershield.R;

public class UserConfigActivity extends AppCompatActivity {

    private ImageButton voltarBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_config);

        //unico codigo, usei pro voltarBtn
        voltarBtn = findViewById(R.id.btnBack);
        //eeh, o codigo basico pra ir pra home ao clicar pra voltar
        voltarBtn.setOnClickListener(v -> {
            Intent intent = new Intent(UserConfigActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }


}