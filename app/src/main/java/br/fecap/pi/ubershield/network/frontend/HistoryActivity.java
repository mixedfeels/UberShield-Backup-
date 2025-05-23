package br.fecap.pi.ubershield.network.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets; // Importe esta classe!
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import br.fecap.pi.ubershield.R;

public class HistoryActivity extends AppCompatActivity {

    private LinearLayout homeButton, configButton, accountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);

        // Referencia os Botões
        homeButton = findViewById(R.id.homeButton);
        configButton = findViewById(R.id.configButton);
        accountButton = findViewById(R.id.accountButton);
        LinearLayout footerLayout = findViewById(R.id.footerLayout);

        ViewCompat.setOnApplyWindowInsetsListener(footerLayout, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    v.getPaddingLeft(),
                    v.getPaddingTop(),
                    v.getPaddingRight(),
                    systemBarsInsets.bottom
            );
            return insets;
        });

        // Trocando Telas
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
            startActivity(intent);
        });

//        configButton.setOnClickListener(v -> {
//            Intent intent = new Intent(HistoryActivity.this, UserConfigActivity.class);
//            startActivity(intent);
//        });

        accountButton.setOnClickListener(v -> {
            Intent intent = new Intent(HistoryActivity.this, UserInfoActivity.class);
            startActivity(intent);
        });
    }
}