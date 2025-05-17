package br.fecap.pi.UberShield;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GeneroActivity  extends AppCompatActivity {
    private Spinner spinnerGenero;
    private Button btnAtualizar;
    private ImageView backButton;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genero);

        spinnerGenero = findViewById(R.id.spinnerGenero);
        btnAtualizar = findViewById(R.id.btnAtualizar);
        backButton = findViewById(R.id.back_button);

        // Preencher o Spinner
        String[] generos = {"Homem", "Mulher", "Outro"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, generos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenero.setAdapter(adapter);

        // Voltar para a tela anterior
        backButton.setOnClickListener(v -> finish());

        // Atualizar gênero
        btnAtualizar.setOnClickListener(v -> {
            String generoSelecionado = spinnerGenero.getSelectedItem().toString();
            Toast.makeText(this, "Gênero atualizado: " + generoSelecionado, Toast.LENGTH_SHORT).show();
            // OBS.: Aqui pode salvar em banco local ou API --- n esquecer de falar com os meninos da API
        });
    }
}