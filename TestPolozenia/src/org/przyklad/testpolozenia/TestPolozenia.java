/***
 * Wyci¹g z ksi¹¿ki "Hello, Android!",
 * opublikowanej przez wydawnictwo Helion S.A.
 * Niniejszy kod jest chroniony prawami autorskimi. Nie mo¿e zostaæ wykorzystany do utworzenia 
 * materia³ów treningowych, kursów, ksi¹¿ek, artyku³ów, itp. Prosimy o kontakt w razie
 * pojawienia siê w¹tpliwoœci.
 * Nie gwarantujemy, ¿e niniejszy kod bêdzie siê nadawa³ do jakiegokolwiek celu. 
 * Wiêcej informacji mo¿na znaleŸæ na stronie http://www.pragmaticprogrammer.com/titles/eband.
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
                 
    private static final String[] A = { "nieprawid³owa" , "niedostêpna" , "dok³adna" , 
        "zgrubna" };
    private static final String[] P = { "nieprawid³owa" , "niedostêpna" , "niska" , 
        "œrednia" , "wysoka" };
    private static final String[] S = { "nieczynny" ,
        "tymczasowo niedostêpny" , "dostêpny" };
    
    private LocationManager menedzer;
    private TextView wyjscie;
    private String najlepszy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        menedzer = (LocationManager) getSystemService(LOCATION_SERVICE);
        wyjscie = (TextView) findViewById(R.id.wyjscie);

        dziennik("Dostawcy po³o¿enia:" );
        //wyrzucDostawcow();

        Criteria kryteria = new Criteria();
        najlepszy = menedzer.getBestProvider(kryteria, true);
        dziennik("\nNajlepszym dostawc¹ jest: " + najlepszy);

        dziennik("\nLokalizacja (pocz¹wszy od ostatniej znanej):" );
        Location lokacja = menedzer.getLastKnownLocation(najlepszy);
        wyrzucLokacje(lokacja);
    }
                
    @Override
    protected void onResume() {
        super.onResume();
        // Uruchamia aktualizacje (dokumentacja zaleca opóŸnienie >= 60000 ms)
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
        dziennik("\nWy³¹czony dostawca: " + dostawca);
    }

    public void onProviderEnabled(String dostawca) {
        dziennik("\nW³¹czony dostawca: " + dostawca);
    }

    public void onStatusChanged(String dostawca, int status,
            Bundle dodatki) {
        dziennik("\nZmiana stanu dostawcy: " + dostawca + ", status="
                + S[status] + ", dodatkowe=" + dodatki);
    }
                
    /** Wyœwietla ci¹g znaków w oknie wynikowym */
    private void dziennik(String string) {
        wyjscie.append(string + "\n" );
    }

    /** Wyœwietla informajce od wszystkich dostawców po³o¿enia */
    private void wyrzucDostawcow() {
        List<String> dostawcy = menedzer.getAllProviders();
        for (String dostawca : dostawcy) {
            wyrzucDostawce(dostawca);
        }
    }

    /** Wyœwietla informacje od jednego dostawcy po³o¿enia */
    private void wyrzucDostawce(String dostawca) {
        LocationProvider info = menedzer.getProvider(dostawca);
        StringBuilder konstruktor = new StringBuilder();
        konstruktor.append("LocationProvider[" )
            .append("nazwa=" )
            .append(info.getName())
            .append(",uruchomiony=" )
            .append(menedzer.isProviderEnabled(dostawca))
            .append(",Dok³adnoœæ (getAccuracy)=" )
            .append(A[info.getAccuracy() + 1])
            .append(",Koszty energetyczne (getPowerRequirement)=" )
            .append(P[info.getPowerRequirement() + 1])
            .append(",Koszty pieniê¿ne (hasMonetaryCost)=" )
            .append(info.hasMonetaryCost())
            .append(",Wie¿a nadawcza (requiresCell)=" )
            .append(info.requiresCell())
            .append(",Sieæ (requiresNetwork)=" )
            .append(info.requiresNetwork())
            .append(",Satelita (requiresSatellite)=" )
            .append(info.requiresSatellite())
            .append(",Obs³uga wysokoœci (supportsAltitude)=" )
            .append(info.supportsAltitude())
            .append(",Obs³uga pelengu (supportsBearing)=" )
            .append(info.supportsBearing())
            .append(",Obs³uga prêdkoœci (supportsSpeed)=" )
            .append(info.supportsSpeed())
            .append("]" );
        dziennik(konstruktor.toString());
    }

    /** Opisuje dan¹ lokalizacjê, mo¿e przyj¹æ wartoœæ null */
    private void wyrzucLokacje(Location lokacja) {
        if (lokacja == null)
            dziennik("\nLokacja[nieznana]" );
        else
            dziennik("\n" + lokacja.toString());
    }
}
