package br.fecap.pi.ubershield.network.frontend;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import br.fecap.pi.ubershield.R;

public class UserGenerActivity extends AppCompatActivity {
    private Spinner spinnerGenero; // Tornando a variável privada e usando o nome do campo diretamente

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_gener);

        // Obtém a referência ao Spinner do layout
        spinnerGenero = findViewById(R.id.spinnerGenero);

        // Cria um ArrayAdapter usando o array de strings e um layout padrão do Android
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.generos_array, // Array definido em res/values/strings.xml
                android.R.layout.simple_spinner_item
        );

        // Especifica o layout a ser usado quando a lista de opções aparece
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Aplica o adapter ao Spinner
        spinnerGenero.setAdapter(adapter);

        // Define o listener para lidar com a seleção de um item no Spinner
        spinnerGenero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Obtém o item selecionado na posição
                String selectedGenero = parent.getItemAtPosition(position).toString();
                // Faça algo com a seleção (ex: exibir um Toast, salvar em uma variável, etc.)
                Log.d("GeneroSelecionado", "Gênero selecionado: " + selectedGenero);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Chamado quando nenhuma seleção está ativa (pode acontecer com Spinners vazios)
                Log.d("GeneroSelecionado", "Nenhum gênero selecionado");
            }
        });
    }
}