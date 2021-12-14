package tests.jvisick.backroom.BackroomObjects;

import io.realm.annotations.Required;

/**
 * Created by scott on 3/6/16.
 */
public class Day {
    @Required
    private BulkDay bulkDay;
    @Required
    private RapidDay rapidDay;
    @Required
    private String date;

    public Day(String date, BulkDay bulkDay, RapidDay rapidDay) {
        this.date = date;
        this.bulkDay = bulkDay;
        this.rapidDay = rapidDay;
    }

    public String getDate() {
        return date;
    }

    public BulkDay getBulkDay() {
        return bulkDay;
    }

    public RapidDay getRapidDay() {
        return rapidDay;
    }

    public void setBulkDay(BulkDay bulkDay) {
        this.bulkDay = bulkDay;
    }

    public void setRapidDay(RapidDay rapidDay) {
        this.rapidDay = rapidDay;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
