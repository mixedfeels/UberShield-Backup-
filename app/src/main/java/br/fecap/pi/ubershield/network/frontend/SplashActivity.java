package br.fecap.pi.ubershield.network.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;

import br.fecap.pi.ubershield.R;

public class SplashActivity extends AppCompatActivity {
    private TextView uberShieldText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        uberShieldText = findViewById(R.id.uberShieldText);

        // Fade in (1s)
        uberShieldText.animate()
                .alpha(1f)
                .setDuration(1000)
                .withEndAction(() -> new Handler().postDelayed(() -> {
                    // Fade out (1s)
                    uberShieldText.animate()
                            .alpha(0f)
                            .setDuration(1000)
                            .withEndAction(() -> {
                                // Abre tela de login
                                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                finish();
                            }).start();
                }, 1000)) // espera 2 segundos visível
                .start();
    }
}
