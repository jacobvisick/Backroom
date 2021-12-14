package tests.jvisick.backroom.BackroomObjects;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by scott on 3/6/16.
 */
public class BulkDay extends RealmObject {
    private RealmList<BulkPerson> individualPerformance;
    private String date;

    private float fMinutesProcessed = 0, fUnitsProcessed = 0, fMinsPerUnit = 0;
    private float lMinutesProcessed = 0, lUnitsProcessed = 0, lMinsPerUnit = 0;
    private float wMinutesProcessed = 0, wUnitsProcessed = 0, wMinsPerUnit = 0;

    public BulkDay() {
        individualPerformance = new RealmList<>();
    }

    public BulkDay(String date, RealmList<BulkPerson> individualPerformance) {
        this.individualPerformance = individualPerformance;
        this.date = date;

        for (BulkPerson person : individualPerformance) {
            switch (person.getBulkType()) {
                case "Furniture": fMinutesProcessed += person.getTotalTime();
                    fUnitsProcessed += person.getUnitsProcessed();
                    break;
                case "Lamps": lMinutesProcessed += person.getTotalTime();
                    lUnitsProcessed += person.getUnitsProcessed();
                    break;
                case "Wall Art": wMinutesProcessed += person.getTotalTime();
                    wUnitsProcessed += person.getUnitsProcessed();
                    break;
                default: //Do nothing
                    break;
            }
        }

        fMinsPerUnit = fMinutesProcessed / fUnitsProcessed;
        lMinsPerUnit = lMinutesProcessed / lUnitsProcessed;
        wMinsPerUnit = wMinutesProcessed / wUnitsProcessed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public RealmList<BulkPerson> getIndividualPerformance() {
        return individualPerformance;
    }

    public void setIndividualPerformance(RealmList<BulkPerson> individualPerformance) {
        this.individualPerformance = individualPerformance;
    }

    public float getfMinutesProcessed() {
        return fMinutesProcessed;
    }

    public void setfMinutesProcessed(float fMinutesProcessed) {
        this.fMinutesProcessed = fMinutesProcessed;
    }

    public float getfUnitsProcessed() {
        return fUnitsProcessed;
    }

    public void setfUnitsProcessed(float fUnitsProcessed) {
        this.fUnitsProcessed = fUnitsProcessed;
    }

    public float getfMinsPerUnit() {
        return fMinsPerUnit;
    }

    public void setfMinsPerUnit(float fMinsPerUnit) {
        this.fMinsPerUnit = fMinsPerUnit;
    }

    public float getlMinutesProcessed() {
        return lMinutesProcessed;
    }

    public void setlMinutesProcessed(float lMinutesProcessed) {
        this.lMinutesProcessed = lMinutesProcessed;
    }

    public float getlUnitsProcessed() {
        return lUnitsProcessed;
    }

    public void setlUnitsProcessed(float lUnitsProcessed) {
        this.lUnitsProcessed = lUnitsProcessed;
    }

    public float getlMinsPerUnit() {
        return lMinsPerUnit;
    }

    public void setlMinsPerUnit(float lMinsPerUnit) {
        this.lMinsPerUnit = lMinsPerUnit;
    }

    public float getwMinutesProcessed() {
        return wMinutesProcessed;
    }

    public void setwMinutesProcessed(float wMinutesProcessed) {
        this.wMinutesProcessed = wMinutesProcessed;
    }

    public float getwUnitsProcessed() {
        return wUnitsProcessed;
    }

    public void setwUnitsProcessed(float wUnitsProcessed) {
        this.wUnitsProcessed = wUnitsProcessed;
    }

    public float getwMinsPerUnit() {
        return wMinsPerUnit;
    }

    public void setwMinsPerUnit(float wMinsPerUnit) {
        this.wMinsPerUnit = wMinsPerUnit;
    }
}
