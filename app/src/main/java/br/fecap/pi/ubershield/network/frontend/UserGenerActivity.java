package br.fecap.pi.ubershield.network.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import br.fecap.pi.ubershield.R;

public class UserGenerActivity extends AppCompatActivity {
    private Spinner spinnerGenero;
    private ImageButton btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_gener);

        spinnerGenero = findViewById(R.id.spinnerGenero);
        btnVoltar = findViewById(R.id.btnVoltar);

        btnVoltar.setOnClickListener(v -> {
            Intent intent = new Intent(UserGenerActivity.this, UserInfoActivity.class);
            startActivity(intent);
        });

        // Cria um ArrayAdapter usando o array de strings e um layout padrão do Android
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.generos_array,
                android.R.layout.simple_spinner_item
        );

        // Especifica o layout a ser usado quando a lista de opções aparece
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenero.setAdapter(adapter);

        // Define o listener para lidar com a seleção de um item no Spinner
        spinnerGenero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGenero = parent.getItemAtPosition(position).toString();
                Log.d("GeneroSelecionado", "Gênero selecionado: " + selectedGenero);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("GeneroSelecionado", "Nenhum gênero selecionado");
            }
        });
    }
}