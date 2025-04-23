package com.example.ubershield;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final String apiKey = "AIzaSyAImNj7rOAA2OooqZ20y-r8Jfbz9Go8gAc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Inicializa o cliente de localização
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtém o fragmento do mapa e inicializa o callback
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Inicializa o serviço Places da Google
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        // Configuração da barra de pesquisa de locais (AutocompleteSupportFragment) AINDA NÃO COMPLETO
        /*
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.getView().setFocusable(true);
        autocompleteFragment.getView().setFocusableInTouchMode(true);
        autocompleteFragment.getView().requestFocus();

        // Define os campos desejados na pesquisa
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));

        // Listener para quando um local for selecionado
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                LatLng destino = place.getLatLng();
                if (destino != null) {
                    selecionarDestino(destino);
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.e("PLACES_ERROR", "Erro ao selecionar local: " + status);
            }
        }); */
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);

        // Verifica e solicita permissões de localização
        if (!hasLocationPermission()) {
            requestLocationPermission();
        } else {
            enableMyLocation();
        }

        // Aplica um estilo personalizado ao mapa
        applyMapStyle();

        // Ajusta a posição do botão de localização
        repositionLocationButton();
    }

    // Verifica se as permissões de localização foram concedidas
    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // Solicita permissões de localização ao usuário
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    // Ativa a localização do usuário no mapa
    private void enableMyLocation() {
        if (hasLocationPermission()) {
            try {
                mMap.setMyLocationEnabled(true);
            } catch (SecurityException e) {
                Log.e("LOCATION_PERMISSION", "Permissão não concedida para mostrar a localização.", e);
            }
        }
    }

    // Aplica um estilo personalizado ao mapa usando um arquivo JSON
    private void applyMapStyle() {
        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style)
            );
            if (!success) {
                Log.e("MAP_STYLE", "Erro ao aplicar o estilo!");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MAP_STYLE", "Arquivo de estilo não encontrado!", e);
        }
    }

    // Ajusta a posição do botão de localização no layout
    private void repositionLocationButton() {
        View mapView = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getView();
        if (mapView != null) {
            View locationButton = mapView.findViewWithTag("GoogleMapMyLocationButton");
            if (locationButton != null) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                layoutParams.setMargins(0, 0, 30, 200);
            }
        }
    }

    // Atualiza a posição do usuário no mapa
    private void updateLocationOnMap(Location location) {
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.clear(); // Remove marcadores antigos
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15)); // Define um nível de zoom
    }

    // Responde à solicitação de permissão do usuário
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            } else {
                Toast.makeText(this, "Permissão de localização negada!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Obtém a rota entre dois pontos usando a API Google Directions
    private void getRoute(LatLng origem, LatLng destino) {
        String url = "https://maps.googleapis.com/maps/api/directions/json?"
                + "origin=" + origem.latitude + "," + origem.longitude
                + "&destination=" + destino.latitude + "," + destino.longitude
                + "&mode=driving"
                + "&key=" + apiKey;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray routes = response.getJSONArray("routes");
                        if (routes.length() > 0) {
                            JSONObject route = routes.getJSONObject(0);
                            JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                            String encodedPolyline = overviewPolyline.getString("points");

                            List<LatLng> points = decodePolyline(encodedPolyline);
                            PolylineOptions polylineOptions = new PolylineOptions()
                                    .addAll(points)
                                    .color(Color.BLUE)
                                    .width(10);
                            mMap.addPolyline(polylineOptions);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("ROUTE_ERROR", "Erro ao obter rota: " + error.getMessage())
        );

        queue.add(jsonObjectRequest);
    }

    // Decodifica a polyline recebida da API para obter os pontos da rota
    private List<LatLng> decodePolyline(String encoded) {
        List<LatLng> polyline = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            polyline.add(new LatLng(lat / 1E5, lng / 1E5));
        }
        return polyline;
    }

    // Seleciona um destino e traça uma rota a partir da localização atual
    private void selecionarDestino(LatLng destino) {
        if (hasLocationPermission()) {
            try {
                fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location != null) {
                        LatLng origem = new LatLng(location.getLatitude(), location.getLongitude());
                        getRoute(origem, destino);
                    }
                });
            } catch (SecurityException e) {
                Log.e("LOCATION_PERMISSION", "Erro ao obter localização para traçar rota.", e);
            }
        } else {
            Toast.makeText(this, "Permissão de localização não concedida.", Toast.LENGTH_SHORT).show();
        }
    }
}