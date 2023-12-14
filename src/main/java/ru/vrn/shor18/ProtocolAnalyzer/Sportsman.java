package ru.vrn.shor18.ProtocolAnalyzer;

import java.util.List;

public class Sportsman {
    private String group;
    private String name;
    private String description;
    private String resultPlace;
    private String resultDuration;
    private List<ControlPoint> controlPoints;
    private List<ControlPoint> cumulativeControlPoints;

    @Override
    public String toString() {
        return "Sportsman{" +
                "group='" + group + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", resultPlace='" + resultPlace + '\'' +
                ", resultDuration='" + resultDuration + '\'' +
                ", controlPoints=" + controlPoints +
                ", cumulativeControlPoints=" + cumulativeControlPoints +
                '}';
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResultPlace() {
        return resultPlace;
    }

    public void setResultPlace(String resultPlace) {
        this.resultPlace = resultPlace;
    }

    public List<ControlPoint> getControlPoints() {
        return controlPoints;
    }

    public void setControlPoints(List<ControlPoint> controlPoints) {
        this.controlPoints = controlPoints;
    }

    public String getResultDuration() {
        return resultDuration;
    }

    public void setResultDuration(String resultDuration) {
        this.resultDuration = resultDuration;
    }

    public List<ControlPoint> getCumulativeControlPoints() {
        return cumulativeControlPoints;
    }

    public void setCumulativeControlPoints(List<ControlPoint> cumulativeControlPoints) {
        this.cumulativeControlPoints = cumulativeControlPoints;
    }
}
