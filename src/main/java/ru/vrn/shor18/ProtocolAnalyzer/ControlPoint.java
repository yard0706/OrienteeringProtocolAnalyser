package ru.vrn.shor18.ProtocolAnalyzer;

public class ControlPoint {
    private String duration;
    private String pointNumber;
    private String place;

    @Override
    public String toString() {
        return "ControlPoint{" +
                "duration='" + duration + '\'' +
                ", pointNumber='" + pointNumber + '\'' +
                ", place='" + place + '\'' +
                '}';
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPointNumber() {
        return pointNumber;
    }

    public void setPointNumber(String pointNumber) {
        this.pointNumber = pointNumber;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
