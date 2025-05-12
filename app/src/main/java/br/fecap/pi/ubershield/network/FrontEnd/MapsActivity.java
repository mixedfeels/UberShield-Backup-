package br.fecap.pi.ubershield.network.frontend;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.*;

import br.fecap.pi.ubershield.R;
import br.fecap.pi.ubershield.network.backend.UberShieldService;

public class MapsActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Location lastLocation;
    private long lastLocationTime = 0;

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
        Button safetyButton = findViewById(R.id.safetyButton);
        safetyButton.setOnClickListener(v -> uberShieldService.startSafetyCheck());

        // Inicia atualizações de localização
        startLocationUpdates();
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
        locationRequest.setFastestInterval(50000); // Intervalo mais rápido
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
                            if (timeStopped > 60000) { // Se estiver parado há mais de 1 minuto
                                Toast.makeText(MapsActivity.this, "Você está parado há mais de 1 minuto!", Toast.LENGTH_SHORT).show();
                            }
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
    protected void onPause() {
        super.onPause();
        // Para atualizações quando a activity for pausada
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates(); // Se o usuário concedeu a permissão
            } else {
                Toast.makeText(this, "Permissão de localização negada", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
