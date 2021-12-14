package tests.jvisick.backroom.BackroomObjects;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by scott on 3/6/16.
 */
public class BulkPerson extends RealmObject {

    private String bulkType, associateName, startTime, endTime, date;
    private float totalTime, minutesPerUnit;
    private int unitsProcessed;

    public BulkPerson() {
        startTime = "";
        endTime = "";
        totalTime = 0;
        minutesPerUnit = 0;
        unitsProcessed = 0;
        bulkType = "";
        associateName = "";
    }

    public BulkPerson(String date, String startTime, String endTime, float totalTime, float minutesPerUnit, int unitsProcessed, String bulkType) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalTime = totalTime;
        this.minutesPerUnit = minutesPerUnit;
        this.unitsProcessed = unitsProcessed;
        this.bulkType = bulkType;

        associateName = "Unknown";
    }

    public BulkPerson(String date, String bulkType, String associateName, String startTime, String endTime, float totalTime, float minutesPerUnit, int unitsProcessed) {
        this.date = date;
        this.bulkType = bulkType;
        this.associateName = associateName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalTime = totalTime;
        this.minutesPerUnit = minutesPerUnit;
        this.unitsProcessed = unitsProcessed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBulkType() {
        return bulkType;
    }

    public void setBulkType(String bulkType) {
        this.bulkType = bulkType;
    }

    public String getAssociateName() {
        return associateName;
    }

    public void setAssociateName(String associateName) {
        this.associateName = associateName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public float getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(float totalTime) {
        this.totalTime = totalTime;
    }

    public float getMinutesPerUnit() {
        return minutesPerUnit;
    }

    public void setMinutesPerUnit(float minutesPerUnit) {
        this.minutesPerUnit = minutesPerUnit;
    }

    public int getUnitsProcessed() {
        return unitsProcessed;
    }

    public void setUnitsProcessed(int unitsProcessed) {
        this.unitsProcessed = unitsProcessed;
    }
}
