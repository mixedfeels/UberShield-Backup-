package br.fecap.pi.ubershield.network.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// Classes de backend
import br.fecap.pi.ubershield.R;
import br.fecap.pi.ubershield.network.backend.BiometricAuth;
import br.fecap.pi.ubershield.network.backend.EmergencyContactManager;
import br.fecap.pi.ubershield.network.backend.UberShieldService;

public class BiometricOverlayActivity extends AppCompatActivity {

    private static final String TAG = "BiometricOverlay";

    private BiometricAuth biometricAuth;
    private EmergencyContactManager emergencyContactManager;
    private int failedBiometricAttempts = 0;
    private static final int MAX_BIOMETRIC_ATTEMPTS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric_overlay);

        // Inicialize o EmergencyContactManager e BiometricAuth
        this.emergencyContactManager = new EmergencyContactManager(this);
        this.biometricAuth = new BiometricAuth(this);

        TextView buttonSolicitarSuporte = findViewById(R.id.buttonSolicitarSuporte);
        TextView buttonConfirmarEmergencia = findViewById(R.id.buttonConfirmarEmergencia);

        buttonSolicitarSuporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Botão 'Solicitar Suporte' clicado.");
                Toast.makeText(BiometricOverlayActivity.this, "Encaminhando solicitação de suporte...", Toast.LENGTH_SHORT).show();
                triggerEmergencyProtocol("Usuário solicitou suporte.");
                finish();
            }
        });

        buttonConfirmarEmergencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Botão 'Confirmar Emergência' clicado. Iniciando fluxo biométrico.");
                // Quando o usuário clica em "Confirmar Emergência", disparamos o prompt biométrico
                showBiometricPromptFlow();
            }
        });

        // Opcional: Lógica para fechar a Activity ao clicar fora do CardView
        View rootLayout = findViewById(R.id.root_layout);
        if (rootLayout != null) {
            rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    Log.d(TAG, "Clique no fundo da overlay. Não fechando automaticamente para forçar interação.");
                }
            });
        }
    }

    // Inicia o fluxo de autenticação biométrica usando a classe BiometricAuth.
    private void showBiometricPromptFlow() {
        if (biometricAuth != null) {
            biometricAuth.showBiometricPrompt(new BiometricAuth.AuthCallback() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Biometria autenticada com sucesso.");
                    Toast.makeText(BiometricOverlayActivity.this, "Digital confirmada! Viagem pode continuar.", Toast.LENGTH_LONG).show();
                    failedBiometricAttempts = 0; // Reseta as tentativas
                    finish();
                }

                @Override
                public void onFailure() {
                    failedBiometricAttempts++;
                    Log.d(TAG, "Falha na autenticação biométrica. Tentativas: " + failedBiometricAttempts);

                    if (failedBiometricAttempts >= MAX_BIOMETRIC_ATTEMPTS) {
                        Log.d(TAG, "Número máximo de tentativas de biometria excedido. Acionando emergência.");
                        Toast.makeText(BiometricOverlayActivity.this, "Muitas tentativas falhas. Acionando emergência.", Toast.LENGTH_LONG).show();
                        triggerEmergencyProtocol("🚨 ALERTA: Autenticação biométrica falhou após várias tentativas. Usuário pode estar em perigo.");
                        finish();
                    } else {
                        Toast.makeText(BiometricOverlayActivity.this, "Digital não reconhecida. Tente novamente ou use as opções.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Log.e(TAG, "BiometricAuth não inicializado. Não é possível mostrar o prompt biométrico.");
            Toast.makeText(this, "Erro interno de biometria.", Toast.LENGTH_SHORT).show();
        }
    }

    // Aciona o protocolo de emergência, enviando mensagens aos contatos
    private void triggerEmergencyProtocol(String message) {
        if (emergencyContactManager != null) {
            Log.d(TAG, "Acionando protocolo de emergência: " + message);
            emergencyContactManager.sendAlertToContacts(message);
        } else {
            Log.e(TAG, "EmergencyContactManager não inicializado.");
            Toast.makeText(this, "Erro: Gerenciador de contatos de emergência indisponível.", Toast.LENGTH_SHORT).show();
        }
    }

    // Sobrescreve o comportamento do botão Voltar
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Por favor, interaja com as opções ou confirme sua digital.", Toast.LENGTH_LONG).show();
        super.onBackPressed();
    }
}