package tests.jvisick.backroom;

import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import tests.jvisick.backroom.BackroomObjects.RapidDay;

public class RapidDays extends AppCompatActivity {

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapid_days);

        // Set up realm
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();

        realm = Realm.getInstance(realmConfiguration);

        RealmResults<RapidDay> rapidDayRealmResults = realm.where(RapidDay.class).findAll();
        final RapidDayAdapter adapter = new RapidDayAdapter(this, R.id.list_view, rapidDayRealmResults, true);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
    }
}
