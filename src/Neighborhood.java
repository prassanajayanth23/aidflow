public class Neighborhood {
    private String name;
    private String situation;
    private int population;
    private String danger; // CRITICAL, HIGH, MEDIUM, LOW
    private String nearestWarehouse;
    private boolean roadOpen;

    public Neighborhood(String name, String situation, int population, String danger, String nearestWarehouse) {
        this.name = name;
        this.situation = situation;
        this.population = population;
        this.danger = danger;
        this.nearestWarehouse = nearestWarehouse;
        this.roadOpen = true; // Open by default
    }

    public int getPriorityScore() {
        int base = 1000;
        if (danger.equals("CRITICAL")) {
            base = 4000;
        } else if (danger.equals("HIGH")) {
            base = 3000;
        } else if (danger.equals("MEDIUM")) {
            base = 2000;
        }
        return base + population;
    }

    public void setDanger(String danger) { this.danger = danger; }
    public void setRoadOpen(boolean roadOpen) { this.roadOpen = roadOpen; }
    public boolean isRoadOpen() { return roadOpen; }
    public String getName() { return name; }
    public String getSituation() { return situation; }
    public int getPopulation() { return population; }
    public String getDanger() { return danger; }
    public String getNearestWarehouse() { return nearestWarehouse; }
}