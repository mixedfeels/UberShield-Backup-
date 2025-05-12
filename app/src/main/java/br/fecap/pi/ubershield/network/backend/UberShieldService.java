package br.fecap.pi.ubershield.network.backend;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;

public class UberShieldService {

    private final Context context;
    private final EmergencyContactManager contactManager;
    private int failedAttempts = 0;

    private static final String TAG = "UBERSHIELD";

    public UberShieldService(Context context) {
        this.context = context;
        this.contactManager = new EmergencyContactManager(context);
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

    // Inicia a verificação biométrica
    private void startBiometricVerification() {
        Executor executor = ContextCompat.getMainExecutor(context);
        BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                Log.d(TAG, "Biometria autenticada com sucesso.");
                Toast.makeText(context, "Verificação bem-sucedida!", Toast.LENGTH_SHORT).show();
                failedAttempts = 0;
            }

            @Override
            public void onAuthenticationFailed() {
                failedAttempts++;
                if (failedAttempts >= 2) {
                    triggerEmergencyProtocol();
                } else {
                    Toast.makeText(context, "Falha na autenticação. Tente novamente.", Toast.LENGTH_SHORT).show();
                    startBiometricVerification();
                }
            }
        };

        BiometricPrompt biometricPrompt = new BiometricPrompt((FragmentActivity) context, executor, callback);

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Confirmação de Segurança")
                .setSubtitle("Toque no sensor biométrico para confirmar")
                .setNegativeButtonText("Cancelar")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    // Aciona o protocolo de emergência
    private void triggerEmergencyProtocol() {
        Log.d(TAG, "Executando protocolo de emergência. Enviando mensagens aos contatos.");
        contactManager.sendAlertToContacts("🚨 ALERTA DE EMERGÊNCIA! O usuário pode estar em perigo.");
    }
}