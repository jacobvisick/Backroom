package tests.jvisick.backroom.BackroomObjects;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by scott on 3/6/16.
 */
public class RapidDay extends RealmObject {

    private String date;
    private int associateCount, cartons;
    private float startTime, endTime, processingTime, laborHours, cartonsPerHour;
    private float preloadTime = 0;

    public RapidDay() {
        date = "";
        associateCount = 0;
        cartons = 0;
        startTime = 0;
        endTime = 0;
        processingTime = 0;
        laborHours = 0;
        cartonsPerHour = 0;
    }

    public RapidDay(String date, int associateCount, float startTime, float endTime, float processingTime, float laborHours, int cartons, float cartonsPerHour) {
        this.date = date;
        this.associateCount = associateCount;
        this.startTime = startTime;
        this.endTime = endTime;
        this.processingTime = processingTime;
        this.laborHours = laborHours;
        this.cartons = cartons;
        this.cartonsPerHour = cartonsPerHour;
    }

    public RapidDay(String date, int associateCount, float startTime, float endTime, float processingTime, float laborHours, int cartons, float cartonsPerHour, float preloadTime) {
        this.date = date;
        this.associateCount = associateCount;
        this.startTime = startTime;
        this.endTime = endTime;
        this.processingTime = processingTime;
        this.laborHours = laborHours;
        this.cartons = cartons;
        this.cartonsPerHour = cartonsPerHour;
        this.preloadTime = preloadTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAssociateCount() {
        return associateCount;
    }

    public void setAssociateCount(int associateCount) {
        this.associateCount = associateCount;
    }

    public float getStartTime() {
        return startTime;
    }

    public void setStartTime(float startTime) {
        this.startTime = startTime;
    }

    public float getEndTime() {
        return endTime;
    }

    public void setEndTime(float endTime) {
        this.endTime = endTime;
    }

    public float getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(float processingTime) {
        this.processingTime = processingTime;
    }

    public float getLaborHours() {
        return laborHours;
    }

    public void setLaborHours(float laborHours) {
        this.laborHours = laborHours;
    }

    public int getCartons() {
        return cartons;
    }

    public void setCartons(int cartons) {
        this.cartons = cartons;
    }

    public float getCartonsPerHour() {
        return cartonsPerHour;
    }

    public void setCartonsPerHour(float cartonsPerHour) {
        this.cartonsPerHour = cartonsPerHour;
    }

    public float getPreloadTime() {
        return preloadTime;
    }

    public void setPreloadTime(float preloadTime) {
        this.preloadTime = preloadTime;
    }
}
