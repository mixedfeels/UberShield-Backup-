package br.fecap.pi.ubershield.network.backend;

import android.content.Context;
import android.util.Log;

public class LocationTracker {
    private double lastLat = -23.5505; // São Paulo como padrão
    private double lastLon = -46.6333;
    private long lastUpdateTime = System.currentTimeMillis();

    public LocationTracker(Context context) {}

    // Atualiza a localização atual
    public void updateLocation(double lat, double lon) {
        this.lastLat = lat;
        this.lastLon = lon;
        lastUpdateTime = System.currentTimeMillis();
    }

    // Verifica se o usuário está parado por mais de 5 minutos
    public boolean isUserIdleOrOffRoute() {
        long timeSinceUpdate = System.currentTimeMillis() - lastUpdateTime;
        boolean isIdle = timeSinceUpdate > 300000; // 5 minutos
        Log.d("UBERSHIELD", "Verificação de ociosidade: " + (isIdle ? "Usuário está parado." : "Usuário ativo."));
        return isIdle;
    }

    public double getLastLat() { return lastLat; }
    public double getLastLon() { return lastLon; }
}
