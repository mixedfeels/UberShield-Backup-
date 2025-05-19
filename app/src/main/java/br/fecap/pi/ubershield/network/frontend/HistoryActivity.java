package br.fecap.pi.ubershield.network.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import br.fecap.pi.ubershield.R;

public class HistoryActivity extends AppCompatActivity {

    private LinearLayout homeButton, configButton, activityButton, accountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);

        // Referencia os Botões
        homeButton = findViewById(R.id.homeButton);
        configButton = findViewById(R.id.configButton);
        accountButton = findViewById(R.id.accountButton);
        activityButton = findViewById(R.id.activityButton);

        // Trocando Telas
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
            startActivity(intent);
        });

//        configButton.setOnClickListener(v -> {
//            Intent intent = new Intent(HistoryActivity.this, UserConfigActivity.class);
//            startActivity(intent);
//        });

        activityButton.setOnClickListener(v -> {
            Intent intent = new Intent(HistoryActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        accountButton.setOnClickListener(v -> {
            Intent intent = new Intent(HistoryActivity.this, UserInfoActivity.class);
            startActivity(intent);
        });
    }
}