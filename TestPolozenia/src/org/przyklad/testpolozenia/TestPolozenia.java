/***
 * Wyci�g z ksi��ki "Hello, Android!",
 * opublikowanej przez wydawnictwo Helion S.A.
 * Niniejszy kod jest chroniony prawami autorskimi. Nie mo�e zosta� wykorzystany do utworzenia 
 * materia��w treningowych, kurs�w, ksi��ek, artyku��w, itp. Prosimy o kontakt w razie
 * pojawienia si� w�tpliwo�ci.
 * Nie gwarantujemy, �e niniejszy kod b�dzie si� nadawa� do jakiegokolwiek celu. 
 * Wi�cej informacji mo�na znale�� na stronie http://www.pragmaticprogrammer.com/titles/eband.
***/
package org.przyklad.testpolozenia;

import java.util.List;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.widget.TextView;

public class TestPolozenia extends Activity implements
    LocationListener {
                 
    private static final String[] A = { "nieprawid�owa" , "niedost�pna" , "dok�adna" , 
        "zgrubna" };
    private static final String[] P = { "nieprawid�owa" , "niedost�pna" , "niska" , 
        "�rednia" , "wysoka" };
    private static final String[] S = { "nieczynny" ,
        "tymczasowo niedost�pny" , "dost�pny" };
    
    private LocationManager menedzer;
    private TextView wyjscie;
    private String najlepszy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        menedzer = (LocationManager) getSystemService(LOCATION_SERVICE);
        wyjscie = (TextView) findViewById(R.id.wyjscie);

        dziennik("Dostawcy po�o�enia:" );
        //wyrzucDostawcow();

        Criteria kryteria = new Criteria();
        najlepszy = menedzer.getBestProvider(kryteria, true);
        dziennik("\nNajlepszym dostawc� jest: " + najlepszy);

        dziennik("\nLokalizacja (pocz�wszy od ostatniej znanej):" );
        Location lokacja = menedzer.getLastKnownLocation(najlepszy);
        wyrzucLokacje(lokacja);
    }
                
    @Override
    protected void onResume() {
        super.onResume();
        // Uruchamia aktualizacje (dokumentacja zaleca op�nienie >= 60000 ms)
        menedzer.requestLocationUpdates(najlepszy, 15000, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Zatrzymuje aktualizacje podczas wstrzymania aplikacji
        menedzer.removeUpdates(this);
    }
                
    public void onLocationChanged(Location lokacja) {
        wyrzucLokacje(lokacja);
    }

    public void onProviderDisabled(String dostawca) {
        dziennik("\nWy��czony dostawca: " + dostawca);
    }

    public void onProviderEnabled(String dostawca) {
        dziennik("\nW��czony dostawca: " + dostawca);
    }

    public void onStatusChanged(String dostawca, int status,
            Bundle dodatki) {
        dziennik("\nZmiana stanu dostawcy: " + dostawca + ", status="
                + S[status] + ", dodatkowe=" + dodatki);
    }
                
    /** Wy�wietla ci�g znak�w w oknie wynikowym */
    private void dziennik(String string) {
        wyjscie.append(string + "\n" );
    }

    /** Wy�wietla informajce od wszystkich dostawc�w po�o�enia */
    private void wyrzucDostawcow() {
        List<String> dostawcy = menedzer.getAllProviders();
        for (String dostawca : dostawcy) {
            wyrzucDostawce(dostawca);
        }
    }

    /** Wy�wietla informacje od jednego dostawcy po�o�enia */
    private void wyrzucDostawce(String dostawca) {
        LocationProvider info = menedzer.getProvider(dostawca);
        StringBuilder konstruktor = new StringBuilder();
        konstruktor.append("LocationProvider[" )
            .append("nazwa=" )
            .append(info.getName())
            .append(",uruchomiony=" )
            .append(menedzer.isProviderEnabled(dostawca))
            .append(",Dok�adno�� (getAccuracy)=" )
            .append(A[info.getAccuracy() + 1])
            .append(",Koszty energetyczne (getPowerRequirement)=" )
            .append(P[info.getPowerRequirement() + 1])
            .append(",Koszty pieni�ne (hasMonetaryCost)=" )
            .append(info.hasMonetaryCost())
            .append(",Wie�a nadawcza (requiresCell)=" )
            .append(info.requiresCell())
            .append(",Sie� (requiresNetwork)=" )
            .append(info.requiresNetwork())
            .append(",Satelita (requiresSatellite)=" )
            .append(info.requiresSatellite())
            .append(",Obs�uga wysoko�ci (supportsAltitude)=" )
            .append(info.supportsAltitude())
            .append(",Obs�uga pelengu (supportsBearing)=" )
            .append(info.supportsBearing())
            .append(",Obs�uga pr�dko�ci (supportsSpeed)=" )
            .append(info.supportsSpeed())
            .append("]" );
        dziennik(konstruktor.toString());
    }

    /** Opisuje dan� lokalizacj�, mo�e przyj�� warto�� null */
    private void wyrzucLokacje(Location lokacja) {
        if (lokacja == null)
            dziennik("\nLokacja[nieznana]" );
        else
            dziennik("\n" + lokacja.toString());
    }
}
