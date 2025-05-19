package br.fecap.pi.ubershield.network.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import br.fecap.pi.ubershield.R;

public class UserPhoneActivity extends AppCompatActivity {
    private ImageButton btnVoltar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_phone);

        btnVoltar = findViewById(R.id.btnVoltar);

        btnVoltar.setOnClickListener(v -> {
            Intent intent = new Intent(UserPhoneActivity.this, UserInfoActivity.class);
            startActivity(intent);
        });
    }
}