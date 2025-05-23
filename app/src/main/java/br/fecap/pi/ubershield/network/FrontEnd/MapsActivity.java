package br.fecap.pi.ubershield.network.frontend;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

// Classe UberShieldService
import br.fecap.pi.ubershield.R;
import br.fecap.pi.ubershield.network.backend.UberShieldService;

public class MapsActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int SMS_PERMISSION_REQUEST_CODE = 2;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Location lastLocation;
    private long lastLocationTime = 0;
    private boolean isOverlayActive = false;

    private UberShieldService uberShieldService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Inicializa o serviço de segurança
        uberShieldService = new UberShieldService(this);

        // Inicializa o provedor de localização
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Botão de segurança
        ImageButton safetyButton = findViewById(R.id.safetyButton);
        safetyButton.setOnClickListener(v -> {
            checkSmsPermission(); // Verificar permissão SMS
            uberShieldService.startSafetyCheck();
            isOverlayActive = true;
        });

        // Inicia atualizações de localização
        startLocationUpdates();

        // NOVO: Verifica e solicita a permissão de SMS ao iniciar a MapsActivity
        checkSmsPermission();
    }

    private void startLocationUpdates() {
        // Verifica permissão de localização
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        // Configura a requisição de localização
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(60000); // Atualiza a cada 1 minuto
        locationRequest.setFastestInterval(50000);
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY); // Alta precisão

        // Callback para atualizações de localização
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location currentLocation = locationResult.getLastLocation();
                if (currentLocation != null) {
                    // Verifica se a localização anterior existe
                    if (lastLocation != null) {
                        float distance = currentLocation.distanceTo(lastLocation);
                        if (distance < 10) { // Considera parado se mover menos de 10 metros
                            long timeStopped = System.currentTimeMillis() - lastLocationTime;
                            if (timeStopped > 60000 && !isOverlayActive) { // Se estiver parado há mais de 1 minuto E a overlay NÃO estiver ativa
                                Log.d("MapsActivity", "Detectado tempo de parada excedente.");
                                checkSmsPermission();
                                runOnUiThread(() -> {
                                    uberShieldService.startSafetyCheck();
                                    isOverlayActive = true;
                                });
                            }
                        } else {
                            // Se o veículo se moveu, reseta o tempo de parada
                            lastLocationTime = System.currentTimeMillis();
                        }
                    }

                    // Atualiza última localização e tempo
                    lastLocation = currentLocation;
                    lastLocationTime = System.currentTimeMillis();
                }
            }
        };

        // Inicia atualizações
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());
    }

    @Override
    protected void onResume() {
        super.onResume();
        isOverlayActive = false;
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Para atualizações quando a activity for pausada
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    // NOVO MÉTODO: Verifica e solicita a permissão SEND_SMS
    private void checkSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("MapsActivity", "Solicitando permissão SEND_SMS.");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    SMS_PERMISSION_REQUEST_CODE);
        } else {
            Log.d("MapsActivity", "Permissão SEND_SMS já concedida.");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
                Toast.makeText(this, "Permissão de localização concedida", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissão de localização negada", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == SMS_PERMISSION_REQUEST_CODE) { // Lida com a resposta da permissão de SMS
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissão de SMS concedida!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissão de SMS negada. Não será possível enviar alertas de emergência via SMS.", Toast.LENGTH_LONG).show();
            }
        }
    }
}