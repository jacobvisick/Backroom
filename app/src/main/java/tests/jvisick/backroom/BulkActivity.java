package tests.jvisick.backroom;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;
import tests.jvisick.backroom.BackroomObjects.BulkDay;
import tests.jvisick.backroom.BackroomObjects.BulkPerson;
import tests.jvisick.backroom.BackroomObjects.RapidDay;

public class BulkActivity extends AppCompatActivity {
    private String mProcessingType;
    boolean isFormFilled = false;

    TextView mTitleTv, mUnitsProcessedTv;
    EditText mTimeStart, mTimeEnd, mTimeProcessed, mUnitsProcessed, mMinutesPerUnit, mAssociateName;
    Button mPerformanceButton, mGoalButton, mClearButton;
    float mGoalMinutesPerUnit;
    FloatingActionButton mFab;

    float vMinutesPerUnit;
    String vDate, vTimeStart, vTimeEnd;
    int vUnitsProcessed, vMinutesWorked;

    private Realm realm;
    private RealmConfiguration realmConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulk);

        Calendar today = Calendar.getInstance();
        vDate = today.get(Calendar.MONTH) + "/" + today.get(Calendar.DAY_OF_MONTH) + "/" + today.get(Calendar.YEAR);
        Log.d("vDate: ", vDate);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mTitleTv = (TextView) findViewById(R.id.bulk_title);
        mAssociateName = (EditText) findViewById(R.id.associate_name);
        mUnitsProcessedTv = (TextView) findViewById(R.id.units_processed_tv);
        mTimeStart = (EditText) findViewById(R.id.time_start_bulk);
        mTimeEnd = (EditText) findViewById(R.id.time_end);
        mTimeProcessed = (EditText) findViewById(R.id.total_time);
        mUnitsProcessed = (EditText) findViewById(R.id.units_processed);
        mMinutesPerUnit = (EditText) findViewById(R.id.minutes_per_unit);
        mPerformanceButton = (Button) findViewById(R.id.calculate_actual);
        mGoalButton = (Button) findViewById(R.id.calculate_goal);
        mClearButton = (Button) findViewById(R.id.clear_form);

        mProcessingType = getIntent().getStringExtra(MainActivity.BULK_EXTRA);
        mTitleTv.setText(mProcessingType + " Processing");
        mUnitsProcessedTv.setText(mProcessingType + " Processed: ");

        switch (mProcessingType) {
            case "Furniture":
                mGoalMinutesPerUnit = 6.82f;
                break;

            case "Lamps":
                mGoalMinutesPerUnit = 5.04f;
                break;

            case "Wall Art":
                mGoalMinutesPerUnit = 2.07f;
                break;

            default:
                mGoalMinutesPerUnit = 0;
                Toast.makeText(BulkActivity.this, "Bulk type " + mProcessingType + " unknown!", Toast.LENGTH_SHORT).show();
                break;
        }

        mTimeStart.setOnFocusChangeListener(focusChangeListener);
        mTimeEnd.setOnFocusChangeListener(focusChangeListener);
        mUnitsProcessed.setOnFocusChangeListener(focusChangeListener);

        mPerformanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatePerformance();
            }
        });

        mGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateGoal();
            }
        });

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearForm();
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDay();
            }
        });


        // Set up Realm
        realmConfig = new RealmConfiguration.Builder(this)
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();

        realm = Realm.getInstance(realmConfig);


        mFab.forceLayout();
    }

    private void calculatePerformance() {
        int start, end, totalMinutes, units;
        float minsPerUnit;

        if (mTimeStart.getText().length() != 0 &&
                mTimeEnd.getText().length() != 0 &&
                mUnitsProcessed.getText().length() != 0) {
            totalMinutes = getTimeInMinutes(mTimeStart, mTimeEnd, true);
            units = Integer.valueOf(mUnitsProcessed.getText().toString());
            minsPerUnit = totalMinutes / units;

            mTimeProcessed.setText(String.valueOf(totalMinutes));
            mMinutesPerUnit.setText(String.valueOf(minsPerUnit));

            vMinutesPerUnit = minsPerUnit;
            vUnitsProcessed = units;

            isFormFilled = true;
            mFab.show();
        } else {
            Snackbar.make(mPerformanceButton, "Not enough information!", Snackbar.LENGTH_SHORT)
                    .setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Dismisses automatically on click
                        }
                    })
                    .show();
        }
    }

    private void calculateGoal() {
        float totalMinutes, units;

        if (mTimeStart.getText().length() != 0 &&
                mTimeEnd.getText().length() != 0 &&
                mGoalMinutesPerUnit != 0) {
            isFormFilled = true;

            totalMinutes = getTimeInMinutes(mTimeStart, mTimeEnd, false);
            units = totalMinutes / mGoalMinutesPerUnit;

            mTimeProcessed.setText(String.valueOf(totalMinutes));
            mMinutesPerUnit.setText(String.valueOf(mGoalMinutesPerUnit));
            mUnitsProcessed.setText(String.valueOf(units));
        } else {
            Snackbar.make(mGoalButton, "Not enough information!", Snackbar.LENGTH_SHORT)
                    .setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Dismisses automatically on click
                        }
                    })
                    .show();
        }
    }

    private int getTimeInMinutes(EditText startET, EditText endET, boolean isReal) {
        String start = startET.getText().toString();
        String end = endET.getText().toString();
        int hStart, mStart, hEnd, mEnd;

        if (start.contains(":")) {
            String[] split = start.split(":");
            hStart = Integer.valueOf(split[0]);
            mStart = Integer.valueOf(split[1]);
        } else {
            Log.d("getTimeInMinutes()", "Invalid time!");
            return 0;
        }

        if (end.contains(":")) {
            String[] split = end.split(":");
            hEnd = Integer.valueOf(split[0]);
            mEnd = Integer.valueOf(split[1]);
        } else {
            Log.d("getTimeInMinutes()", "Invalid time!");
            return 0;
        }

        if (hEnd < hStart) hEnd += 12;

        int startInMinutes = (hStart * 60) + mStart;
        int endInMinutes = (hEnd * 60) + mEnd;

        if (isReal) {
            vTimeStart = start;
            vTimeEnd = end;
            vMinutesWorked = endInMinutes - startInMinutes;
        }

        return endInMinutes - startInMinutes;
    }

    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (isFormFilled) {
                clearForm();
            } else {
                mFab.hide();
            }
        }
    };

    public void clearForm() {
        mTimeStart.setText("");
        mTimeStart.setHint("0:00");

        mTimeEnd.setText("");
        mTimeEnd.setHint("0:00");

        mTimeProcessed.setText("0");

        mUnitsProcessed.setText("");
        mUnitsProcessed.setHint("0");

        mMinutesPerUnit.setText("0.00");

        mAssociateName.setText("");
        mAssociateName.setHint("Unknown");

        isFormFilled = false;
        mFab.hide();
    }

    private void saveDay() {
        if (isFormFilled) {

            final BulkPerson bulkPerson = new BulkPerson(vDate, mProcessingType, mAssociateName.getText().toString(), vTimeStart, vTimeEnd, vMinutesWorked, vMinutesPerUnit, vUnitsProcessed);

            realm.beginTransaction();
            final RealmResults<BulkPerson> realmResults = realm.where(BulkPerson.class)
                    .equalTo("date", vDate)
                    .equalTo("associateName", mAssociateName.getText().toString(), Case.INSENSITIVE)
                    .equalTo("bulkType", mProcessingType)
                    .findAll();

            if (realmResults.size() > 0) {
                Snackbar.make(mFab, realmResults.size() + " duplicate entrie(s) found!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Replace", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                realmResults.clear();
                                realm.copyToRealm(bulkPerson);
                                Snackbar.make(v, "Entries replaced with current entry!", Snackbar.LENGTH_SHORT).show();
                            }
                        })
                        .setAction("Cancel", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Snackbar.make(v, "Current entry not saved", Snackbar.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            } else {
                realm.copyToRealm(bulkPerson);

                Snackbar.make(mFab, mAssociateName.getText().toString() + "'s " + mProcessingType +
                        " for " + vDate + " saved!", Snackbar.LENGTH_SHORT).show();
            }

            realm.commitTransaction();

            Snackbar.make(mFab, mAssociateName.getText().toString() + "'s " + mProcessingType +
                    " for " + vDate + " saved!", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(mFab, "Please fill out the form!", Snackbar.LENGTH_SHORT).show();
        }

    }

    public BulkDay totalDay() {
        RealmResults<BulkPerson> bulk = realm.where(BulkPerson.class).equalTo("date", vDate).findAll();
        RealmList<BulkPerson> people = new RealmList<>();

        for (BulkPerson person : bulk) {
            people.add(person);
        }

        return new BulkDay(vDate, people);
    }
}
