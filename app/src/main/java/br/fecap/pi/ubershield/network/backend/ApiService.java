package br.fecap.pi.ubershield.network.backend;

import okhttp3.*;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;

public class ApiService {
    private static final String BASE_URL = "https://ubershieldAPP.azurewebsites.net/";

    //se for aumentar o tempo de timout coloca aqui  VV
    private static final OkHttpClient client = new OkHttpClient();

    // Interface de callback principal
    public interface Callback {
        void onResponse(String status, String message);
        void onError(String errorMessage);
    }

    // Interface de callback para salt
    public interface SaltCallback {
        void onSaltReceived(String salt);
        void onError(String errorMessage);
    }

    // Método atualizado para criação de usuário
    public static void criarUsuario(String nome, String email, String senhaHash, String salt, Callback callback) {
        RequestBody body = new FormBody.Builder()
                .add("nome", nome)
                .add("email", email)
                .add("senha_hash", senhaHash)
                .add("salt", salt)
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL + "criarUsuario")
                .post(body)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");
                        callback.onResponse(status, message);
                    } catch (JSONException e) {
                        callback.onError("Erro ao parsear a resposta JSON");
                    }
                } else {
                    callback.onError("Erro de servidor: " + response.message());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Erro de conexão: " + e.getMessage());
            }
        });
    }

    //Método para buscar salt
    public static void buscarSalt(String username, SaltCallback callback) {
        Request request = new Request.Builder()
                .url(BASE_URL + "buscarSalt?nome=" + username)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        String salt = jsonResponse.getString("salt");
                        callback.onSaltReceived(salt);
                    } else {
                        callback.onError("Erro ao buscar salt: " + response.message());
                    }
                } catch (JSONException e) {
                    callback.onError("Erro ao parsear resposta");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Erro de conexão: " + e.getMessage());
            }
        });
    }
}