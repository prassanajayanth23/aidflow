public class ResourceRequest {
    private Neighborhood area;
    private String item;
    private int needed;
    private int delivered;
    private String sentFrom;

    public ResourceRequest(Neighborhood area, String item, int needed) {
        this.area = area;
        this.item = item;
        this.needed = needed;
        this.delivered = 0;
        this.sentFrom = "";
    }

    public void recordDelivery(String warehouseName, int amount) {
        if (amount > 0) {
            delivered += amount;
            if (sentFrom.isEmpty()) {
                sentFrom = warehouseName + " (" + amount + ")";
            } else {
                sentFrom += " + " + warehouseName + " (" + amount + ")";
            }
        }
    }

    public int getRemaining() {
        return needed - delivered;
    }

    public String getStatus() {
        if (delivered == 0) {
            return "WAITING";
        } else if (delivered < needed) {
            return "PARTIAL";
        } else {
            return "DONE";
        }
    }

    public Neighborhood getArea() { return area; }
    public String getItem() { return item; }
    public int getNeeded() { return needed; }
    public int getDelivered() { return delivered; }
    public String getSentFrom() {
        if (sentFrom.isEmpty()) {
            return "-";
        }
        return sentFrom;
    }
}