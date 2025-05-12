package br.fecap.pi.ubershield.network.backend;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.widget.Toast;
import android.util.Log;

public class EmergencyContactManager {
    private final Context context;

    public EmergencyContactManager(Context context) {
        this.context = context;
    }

    // Envia alerta para os contatos de emergência
    public void sendAlertToContacts(String message) {
        String[] contacts = getEmergencyContacts();
        SmsManager sms = SmsManager.getDefault();

        for (String number : contacts) {
            if (!number.isEmpty()) {
                try {
                    Log.d("UBERSHIELD", "Enviando SMS para: " + number);
                    sms.sendTextMessage(number, null, message, null, null);
                } catch (Exception e) {
                    Log.e("UBERSHIELD", "Erro ao enviar SMS para " + number + ": " + e.getMessage());
                    Toast.makeText(context, "Erro ao enviar SMS para " + number, Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("UBERSHIELD", "Número de contato vazio. Ignorando.");
            }
        }
        Toast.makeText(context, "Alertas enviados!", Toast.LENGTH_SHORT).show();
        Log.d("UBERSHIELD", "Mensagens de emergência enviadas.");
    }

    // Recupera os contatos de emergência salvos
    private String[] getEmergencyContacts() {
        SharedPreferences prefs = context.getSharedPreferences("EmergencyContacts", Context.MODE_PRIVATE);
        return new String[]{
                prefs.getString("contact1", ""),
                prefs.getString("contact2", ""),
                prefs.getString("contact3", "")
        };
    }
}
