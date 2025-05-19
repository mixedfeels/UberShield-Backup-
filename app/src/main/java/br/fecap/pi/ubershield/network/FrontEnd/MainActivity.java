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

    private ImageButton accountButton, homeButton, configButton;
    private Button buttonProsseguir;
    private LinearLayout homeButtonContainer;
    private LinearLayout configButtonContainer;
    private LinearLayout activityButtonContainer;
    private LinearLayout accountButtonContainer;


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
        accountButton = findViewById(R.id.accountButton);
        homeButton = findViewById(R.id.homeButton);
        configButton = findViewById(R.id.configButton);
        buttonProsseguir = findViewById(R.id.buttonProsseguir);
        homeButtonContainer = findViewById(R.id.homeButtonContainer);
        configButtonContainer = findViewById(R.id.configButtonContainer);
        activityButtonContainer = findViewById(R.id.activityButtonContainer);
        accountButtonContainer = findViewById(R.id.accountButtonContainer);

        // aqui eu configuro o botao homeButton para sempre ir pra tela de config do usuario ( copia e cola se tiver um accountButton na sua activity )
        accountButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
            startActivity(intent);
        });

        homeButtonContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Ao pressionar o botão de prosseguir com a viagem ele vai para o Mapa
        buttonProsseguir.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
        });

        configButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserConfigActivity.class);
                startActivity(intent);
            }
        });


//        activityButtonContainer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, YourActivitiesActivity.class);
//                startActivity(intent);
//            }
//        });

        accountButtonContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}
