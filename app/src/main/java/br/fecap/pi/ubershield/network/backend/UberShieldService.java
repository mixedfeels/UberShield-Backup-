package br.fecap.pi.ubershield.network.backend;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import br.fecap.pi.ubershield.network.frontend.BiometricOverlayActivity;

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

    // Método principal para iniciar a verificação de segurança
    public void startSafetyCheck() {
        Log.d(TAG, "Iniciando verificação de segurança - Abrindo BiometricOverlayActivity.");
        // Iniciamos a BiometricOverlayActivity
        Intent intent = new Intent(context, br.fecap.pi.ubershield.network.frontend.BiometricOverlayActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    // Permitindo que a BiometricOverlayActivity chame essas funções
    public BiometricAuth getBiometricAuth() {
        return biometricAuth;
    }

    public EmergencyContactManager getEmergencyContactManager() {
        return contactManager;
    }

    public void onBiometricSuccess() {
        Log.d(TAG, "Biometria confirmada na overlay.");
    }

    public void onBiometricFailedPermanently() {
        Log.d(TAG, "Biometria falhou após várias tentativas ou cancelamento.");
    }
}