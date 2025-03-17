package com.example.ubershield.network;

import android.util.Log;
import okhttp3.*;

import java.io.IOException;

public class ApiService {
    private static final String BASE_URL = "http://SEU_IP:3000";
    private static final OkHttpClient client = new OkHttpClient();

    // Método GET para buscar todos os usuários
    public static void getUsuarios(Callback callback) {
        Request request = new Request.Builder()
                .url(BASE_URL + "/tudo")
                .build();

        client.newCall(request).enqueue(callback);
    }

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

        client.newCall(request).enqueue(callback);
    }
}
