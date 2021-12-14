package tests.jvisick.backroom;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import tests.jvisick.backroom.BackroomObjects.RapidDay;

public class RapidActivity extends AppCompatActivity {
    boolean isFormFilled = false;

    Button mPerformanceButton, mGoalButton, mClearButton;
    EditText mAssociates, mTimeStart, mTimeEnd, mTimeTotal, mLaborHours, mCartons, mCph;
    FloatingActionButton mFab;

    int vAssociates, vCartons;
    float vTimeStart, vTimeEnd, vTimeTotal, vLaborHours, vCartonsPerHour;
    String vDate;

    private Realm realm;
    private RealmConfiguration realmConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rapid);

        // Set up Realm
        Realm.init(getApplicationContext());
        realmConfig = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();

        realm = Realm.getInstance(realmConfig);

        Calendar today = Calendar.getInstance();
        vDate = today.get(Calendar.MONTH) + "/" + today.get(Calendar.DAY_OF_MONTH) + "/" + today.get(Calendar.YEAR);
        Log.d("vDate: ", vDate);

        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mPerformanceButton = (Button) findViewById(R.id.calculate_actual);
        mGoalButton = (Button) findViewById(R.id.calculate_goal);
        mClearButton = (Button) findViewById(R.id.clear_form);

        mAssociates = (EditText) findViewById(R.id.associate_count);
        mTimeStart = (EditText) findViewById(R.id.time_start);
        mTimeEnd = (EditText) findViewById(R.id.time_end);
        mTimeTotal = (EditText) findViewById(R.id.total_time);
        mLaborHours = (EditText) findViewById(R.id.labor_hours);
        mCartons = (EditText) findViewById(R.id.cartons);
        mCph = (EditText) findViewById(R.id.cph);

        mPerformanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPerformance();
            }
        });

        mGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGoals();
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

        mFab.forceLayout();

        mAssociates.setOnFocusChangeListener(focusChangeListener);
        mTimeStart.setOnFocusChangeListener(focusChangeListener);
        mTimeEnd.setOnFocusChangeListener(focusChangeListener);
        mCartons.setOnFocusChangeListener(focusChangeListener);
        mCph.setOnFocusChangeListener(focusChangeListener);
    }

    private void getPerformance() {
        float associates, cartons, start, end, time, laborHours, cph;

        if (mTimeStart.getText().length() != 0 &&
                mTimeEnd.getText().length() != 0 &&
                mCartons.getText().length() != 0) {
            if (mAssociates.getText().length() != 0) associates = Integer.valueOf(mAssociates.getText().toString());
            else {
                associates = 7;
                mAssociates.setText("7");
            }
            cartons = Float.valueOf(mCartons.getText().toString());
            time = timeToHours(mTimeStart, mTimeEnd, true);
            laborHours = time * associates;
            cph = cartons / laborHours;

            mTimeTotal.setText(String.valueOf(time));
            mLaborHours.setText(String.valueOf(laborHours));
            mCph.setText(String.valueOf(cph));

            vAssociates = Math.round(associates);
            vCartons = Math.round(cartons);
            vTimeTotal = time;
            vLaborHours = laborHours;
            vCartonsPerHour = cph;

            isFormFilled = true;
            mFab.show();
        } else {
            Snackbar.make(mPerformanceButton, "Not enough information!", Snackbar.LENGTH_SHORT)
                    .setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Dismisses automatically
                        }
                    }).show();
        }
    }

    private void getGoals() {
        float associates, start, end, time, laborHours, cartons, cph = 31;

        if (mTimeStart.getText().length() != 0) {
            if (mTimeEnd.getText().length() != 0) {
                if (mAssociates.getText().length() != 0)
                    associates = Integer.valueOf(mAssociates.getText().toString());
                else associates = 7;
                time = timeToHours(mTimeStart, mTimeEnd, false);
                laborHours = time * associates;
                cartons = (float) Math.ceil(laborHours * cph);

                mCartons.setText(String.valueOf(Math.round(cartons)));
                mTimeTotal.setText(String.valueOf(time));
                mLaborHours.setText(String.valueOf(laborHours));
                mCph.setText(String.valueOf(cph));
            } else if (mCartons.getText().length() != 0) {
                if (mAssociates.getText().length() != 0)
                    associates = Integer.valueOf(mAssociates.getText().toString());
                else {
                    associates = 7;
                    mAssociates.setText("7");
                }
                start = parseTime(mTimeStart);
                cartons = Integer.valueOf(mCartons.getText().toString());
                if (mCph.getText().length() != 0) cph = Float.valueOf(mCph.getText().toString());
                else {
                    cph = 31;
                    mCph.setText("31");
                }
                laborHours = cartons / cph;
                time = laborHours / associates;
                end = start + time;

                mTimeTotal.setText(String.valueOf(time));
                mTimeEnd.setText(parseFloatToTime(end));
                mLaborHours.setText(String.valueOf(laborHours));

                isFormFilled = true;
            }
        } else {
            Snackbar.make(mGoalButton, "Not enough information!", Snackbar.LENGTH_SHORT)
                    .setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Dismisses automatically
                        }
                    }).show();
        }

    }

    private float timeToHours(EditText start, EditText finish, boolean isReal) {
        float startHour, startMinute, endHour, endMinute, begin, end;

        if (start.getText().toString().contains(":") &&
                finish.getText().toString().contains(":")) {
            String[] startTime = start.getText().toString().split(":");
            startHour = Integer.parseInt(startTime[0]);
            startMinute = Integer.parseInt(startTime[1]);

            String[] endTime = finish.getText().toString().split(":");
            endHour = Integer.parseInt(endTime[0]);
            endMinute = Integer.parseInt(endTime[1]);

            if (endHour < startHour) endHour += 12;

            begin = startHour + (startMinute / 60);
            end = endHour + (endMinute / 60);

            if (isReal) {
                vTimeStart = begin;
                vTimeEnd = end;
            }

            return end - begin;
        } else {
            Log.d("timeToHours()", "Invalid time!");
            return 0;
        }
    }

    private float parseTime(EditText time) {
        String timeToParse = time.getText().toString();

        if (timeToParse.contains(":")) {
            String [] split = timeToParse.split(":");
            float hour = Float.valueOf(split[0]);
            float minute = Float.valueOf(split[1]);

            return hour + (minute / 60);
        } else {
            return 0;
        }
    }

    private String parseFloatToTime(float hourTime) {
        float decimal = hourTime % 1;
        String minutes = Integer.toString(Math.round(decimal * 60));
        String hours = Integer.toString(Math.round(hourTime - decimal));

        return hours + ":" + minutes;
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

    private void clearForm() {
            mAssociates.setText("");
            mAssociates.setHint("7");

            mTimeStart.setText("");
            mTimeStart.setHint("11:00");

            mTimeEnd.setText("");
            mTimeEnd.setHint("1:00");

            mLaborHours.setText("0.00");

            mTimeTotal.setText("0.00");

            mCartons.setText("");
            mCartons.setHint("0.00");

            mCph.setText("31.00");

            isFormFilled = false;
            mFab.hide();
    }

    private void saveDay() {
        if (isFormFilled) {

            final RapidDay rapidDay = new RapidDay(vDate, vAssociates, vTimeStart, vTimeEnd, vTimeTotal, vLaborHours, vCartons, vCartonsPerHour);

            realm.beginTransaction();
            final RealmResults<RapidDay> realmResults = realm.where(RapidDay.class).equalTo("date", vDate).findAll();

            if (realmResults.size() > 0) {
                Snackbar.make(mFab, realmResults.size() + " duplicate entrie(s) found!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Replace", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                realmResults.clear();
                                Snackbar.make(v, "Entries replaced with current entry!", Snackbar.LENGTH_SHORT).show();
                                realm.copyToRealm(rapidDay);
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
                realm.copyToRealm(rapidDay);
                Snackbar.make(mFab, "Date " + vDate + " of rapid saved!", Snackbar.LENGTH_SHORT).show();
            }

            realm.commitTransaction();

            Snackbar.make(mFab, "Date " + vDate + " of rapid saved!", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(mFab, "Please fill out the form!", Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }
}
