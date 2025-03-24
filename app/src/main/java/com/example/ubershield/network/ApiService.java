package com.example.ubershield.network;


import okhttp3.*;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;



public class ApiService {

    private static final String BASE_URL = "https://ubershieldAPP.azurewebsites.net/"; // link do servidor na Azure
    private static final OkHttpClient client = new OkHttpClient();

    // Método POST para criar um usuário
    public static void criarUsuario(String nome, String senha, Callback callback) {
        RequestBody body = new FormBody.Builder()
                .add("nome", nome)
                .add("senha", senha)
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + "/criarUsuario")
                .post(body)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Parse the JSON response
                    try {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.getString("message");

                        // Pass the result to the callback (handle in activity)
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

    // Interface de callback para obter a resposta
    public interface Callback {
        void onResponse(String status, String message);
        void onError(String errorMessage);
    }
}
