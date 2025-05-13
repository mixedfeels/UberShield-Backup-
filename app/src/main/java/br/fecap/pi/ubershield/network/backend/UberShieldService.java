package br.fecap.pi.ubershield.network.backend;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class UberShieldService {

    private final Context context;
    private final EmergencyContactManager contactManager;
    private final BiometricAuth biometricAuth;
    private int failedAttempts = 0;

    private static final String TAG = "UBERSHIELD";

    public UberShieldService(Context context) {
        this.context = context;
        this.contactManager = new EmergencyContactManager(context);
        this.biometricAuth = new BiometricAuth(context);
    }

    // Inicia a verificação de segurança
    public void startSafetyCheck() {
        new Handler(Looper.getMainLooper()).post(() -> {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("Verificação de Segurança")
                    .setMessage("Você está bem?")
                    .setCancelable(false)
                    .setPositiveButton("Sim", (d, w) -> startBiometricVerification())
                    .setNegativeButton("Não", (d, w) -> triggerEmergencyProtocol())
                    .create();
            dialog.show();
        });
    }

    // Inicia a verificação biométrica usando a classe BiometricAuth
    private void startBiometricVerification() {
        biometricAuth.showBiometricPrompt(new BiometricAuth.AuthCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Biometria autenticada com sucesso.");
                Toast.makeText(context, "Verificação bem-sucedida!", Toast.LENGTH_SHORT).show();
                failedAttempts = 0;
            }

            @Override
            public void onFailure() {
                failedAttempts++;
                Log.d(TAG, "Falha na autenticação biométrica. Tentativas: " + failedAttempts);
                if (failedAttempts >= 2) {
                    triggerEmergencyProtocol();
                } else {
                    Toast.makeText(context, "Falha na autenticação. Tente novamente.", Toast.LENGTH_SHORT).show();
                    startBiometricVerification(); // Tenta novamente
                }
            }
        });
    }

    // Aciona o protocolo de emergência
    private void triggerEmergencyProtocol() {
        Log.d(TAG, "Executando protocolo de emergência. Enviando mensagens aos contatos.");
        contactManager.sendAlertToContacts("🚨 ALERTA DE EMERGÊNCIA! O usuário pode estar em perigo.");
    }
}
