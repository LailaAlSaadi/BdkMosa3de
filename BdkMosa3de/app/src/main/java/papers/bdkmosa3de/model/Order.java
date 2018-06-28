package papers.bdkmosa3de.model;

public class Order {
    public String uid;
    public String date;
    public String area;
    public String details;
    public String serviceType;
    public String toTime;
    private String fromTime;

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }
}
