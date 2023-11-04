package User_data;


public class FundData {
    private int id;
    private double fund;
    private double totalSalary;
    private double roomPrizeBkash;
    private double roomPrizeManual;
    private String date;

    public FundData(int id, double fund, double totalSalary, double roomPrizeBkash, double roomPrizeManual, String date) {
        this.id = id;
        this.fund = fund;
        this.totalSalary = totalSalary;
        this.roomPrizeBkash = roomPrizeBkash;
        this.roomPrizeManual = roomPrizeManual;
        this.date = date;
    }

    // Empty constructor
    public FundData() {
        // Empty constructor
    }

    // Getters and setters for the fields

    public int getId() {
        return id;
    }

    public double getFund() {
        return fund;
    }

    public double getTotalSalary() {
        return totalSalary;
    }

    public double getRoomPrizeBkash() {
        return roomPrizeBkash;
    }

    public double getRoomPrizeManual() {
        return roomPrizeManual;
    }

    public String getDate() {
        return date;
    }
}

