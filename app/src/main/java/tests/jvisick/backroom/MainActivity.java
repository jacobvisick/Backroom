package tests.jvisick.backroom;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public final static String BULK_EXTRA = "tests.jvisick.backroom.BULK_EXTRA";

    Button mRapid;
    Button mFurniture;
    Button mLamps;
    Button mWallDec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /** FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); **/

        mRapid = (Button) findViewById(R.id.rapid);
        mFurniture = (Button) findViewById(R.id.furniture);
        mLamps = (Button) findViewById(R.id.lamps);
        mWallDec = (Button) findViewById(R.id.wall_dec);

        mRapid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), RapidActivity.class));
            }
        });

        mFurniture.setOnClickListener(bulkOnClickListener);
        mLamps.setOnClickListener(bulkOnClickListener);
        mWallDec.setOnClickListener(bulkOnClickListener);

        findViewById(R.id.rapid_days).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), RapidDays.class));
            }
        });

    }

    View.OnClickListener bulkOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getBaseContext(), BulkActivity.class);
            intent.putExtra(BULK_EXTRA, ((Button) v).getText().toString());
            startActivity(intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
