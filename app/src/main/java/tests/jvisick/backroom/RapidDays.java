package tests.jvisick.backroom;

import android.support.annotation.WorkerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmResults;
import tests.jvisick.backroom.BackroomObjects.RapidDay;

public class RapidDays extends AppCompatActivity {

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapid_days);

        // Set up realm
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
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
