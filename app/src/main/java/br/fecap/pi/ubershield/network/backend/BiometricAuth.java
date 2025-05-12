package br.fecap.pi.ubershield.network.backend;
import android.util.Log;
import android.content.Context;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import java.util.concurrent.Executor;

public class BiometricAuth {
    private final Context context;

    public interface AuthCallback {
        void onSuccess();
        void onFailure();
    }

    public BiometricAuth(Context context) {
        this.context = context;
    }

    public void showBiometricPrompt(AuthCallback callback) {
        Log.d("UBERSHIELD", "Exibindo prompt biométrico personalizado.");
        Executor executor = ContextCompat.getMainExecutor(context);

        BiometricPrompt.AuthenticationCallback authCallback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                Log.d("UBERSHIELD", "Biometria autenticada com sucesso (via BiometricAuth).");
                callback.onSuccess();
            }

            @Override
            public void onAuthenticationFailed() {
                Log.d("UBERSHIELD", "Falha na autenticação biométrica (via BiometricAuth).");
                callback.onFailure();
            }
        };

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Confirmação de Segurança")
                .setSubtitle("Toque no sensor biométrico para confirmar")
                .setNegativeButtonText("Cancelar")
                .build();

        if (context instanceof FragmentActivity) {
            new BiometricPrompt((FragmentActivity) context, executor, authCallback).authenticate(promptInfo);
        }
    }
}