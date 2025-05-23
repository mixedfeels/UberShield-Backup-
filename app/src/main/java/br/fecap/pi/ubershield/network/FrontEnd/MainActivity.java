package br.fecap.pi.ubershield.network.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import br.fecap.pi.ubershield.R;

public class MainActivity extends AppCompatActivity {

    private LinearLayout searchBar, configButton, activityButton, accountButton, carLayout, boxLayout, routeLayout;

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

        // Referencia os Botões
        accountButton = findViewById(R.id.accountButton);
        activityButton = findViewById(R.id.activityButton);
//        configButton = findViewById(R.id.configButton);
        searchBar = findViewById(R.id.searchBar);
        carLayout = findViewById(R.id.carLayout);
        boxLayout = findViewById(R.id.boxLayout);
        routeLayout = findViewById(R.id.routeLayout);

        // Trocando Telas
//        configButton.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, AppConfigActivity.class);
//            startActivity(intent);
//        });

        activityButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        accountButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
            startActivity(intent);
        });

        View.OnClickListener commonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        };

        // Todos levam para o Mapa
        routeLayout.setOnClickListener(commonClickListener);
        boxLayout.setOnClickListener(commonClickListener);
        carLayout.setOnClickListener(commonClickListener);
        searchBar.setOnClickListener(commonClickListener);
    }
}
