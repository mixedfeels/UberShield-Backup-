package com.example.ubershield;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UserConfigActivity extends AppCompatActivity {
    TextView txtNome, txtEmail, txtCorridas, txtSenha;
    RatingBar ratingMedia;
    Button btnEditar, btnSalvar;
    String nomeAtual;
    EditText editNome, editEmail;
    LinearLayout layoutEdicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_config);

        txtNome = findViewById(R.id.txtNome);
        txtEmail = findViewById(R.id.txtEmail);
        txtCorridas = findViewById(R.id.txtCorridas);
        ratingMedia = findViewById(R.id.ratingMedia);
        btnEditar = findViewById(R.id.btnEditar);
        btnSalvar = findViewById(R.id.btnSalvar);
        editNome = findViewById(R.id.editNome);
        editEmail = findViewById(R.id.editEmail);
        layoutEdicao = findViewById(R.id.layoutEdicao);

        nomeAtual = getIntent().getStringExtra("nomeUsuario");
        buscarDadosUsuario(nomeAtual);

        btnEditar.setOnClickListener(v -> exibirDialogSenha());
        btnSalvar.setOnClickListener(v -> confirmarEdicao());
    }

    private void buscarDadosUsuario(String nome) {
        String url = "https://SEU_SERVIDOR/usuario/" + nome;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject user = response.getJSONObject("user");
                        txtNome.setText(user.getString("nome"));
                        txtEmail.setText(user.getString("email"));
                        txtCorridas.setText("Corridas feitas: " + user.getInt("corridas_feitas"));
                        ratingMedia.setRating((float) user.getDouble("media_avaliacao"));

                        editNome.setText(user.getString("nome"));
                        editEmail.setText(user.getString("email"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Erro ao buscar dados", Toast.LENGTH_SHORT).show()
        );

        queue.add(req);
    }

    private void exibirDialogSenha() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirme sua senha");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("Confirmar", (dialog, which) -> {
            txtSenha = input;
            layoutEdicao.setVisibility(View.VISIBLE);
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void confirmarEdicao() {
        String novoNome = editNome.getText().toString().trim();
        String novoEmail = editEmail.getText().toString().trim();
        String senhaHash = String.valueOf(txtSenha.getText().toString().trim().hashCode());

        JSONObject body = new JSONObject();
        try {
            body.put("nome_antigo", nomeAtual);
            body.put("novo_nome", novoNome);
            body.put("novo_email", novoEmail);
            body.put("senha_hash", senhaHash);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "https://SEU_SERVIDOR/editarUsuario";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body,
                response -> {
                    Toast.makeText(this, "Dados atualizados!", Toast.LENGTH_SHORT).show();
                    layoutEdicao.setVisibility(View.GONE);
                    nomeAtual = novoNome;
                    buscarDadosUsuario(nomeAtual);
                },
                error -> Toast.makeText(this, "Erro ao atualizar", Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }
}
