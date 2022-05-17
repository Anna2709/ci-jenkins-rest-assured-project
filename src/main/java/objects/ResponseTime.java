package objects;

public class ResponseTime extends UserTime{
    public ResponseTime(){}
    public ResponseTime(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    private String updatedAt;
    public ResponseTime(String name, String job, String updatedAt) {
        super(name, job);
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
