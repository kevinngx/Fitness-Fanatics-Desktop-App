package sample;

public class OtherActivity {
    int userId;
    long date;
    String activityType;
    int timeTaken;

    public OtherActivity(int userId, long date, String activityType, int timeTaken) {
        this.userId = userId;
        this.date = date;
        this.activityType = activityType;
        this.timeTaken = timeTaken;
    }

    public OtherActivity() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public int getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(int timeTaken) {
        this.timeTaken = timeTaken;
    }

    @Override
    public String toString() {
        return "OtherActivity{" +
                "userId=" + userId +
                ", date=" + date +
                ", activityType='" + activityType + '\'' +
                ", timeTaken=" + timeTaken +
                '}';
    }
}
