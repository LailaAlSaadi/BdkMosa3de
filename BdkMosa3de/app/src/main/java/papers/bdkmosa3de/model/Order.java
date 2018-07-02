package papers.bdkmosa3de.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Order {
    public String uid;
    public String date;
    public String area;
    public String details;
    public String serviceType;
    public String toTime;
    public String fromTime;
    public String location;
    private String createdDate;

    public Order(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        createdDate = simpleDateFormat.format(new Date());


    }
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

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
