public class ReliefCenter {
    private String name;
    private int food;
    private int water;
    private int medical;

    public ReliefCenter(String name, int food, int water, int medical) {
        this.name = name;
        this.food = food;
        this.water = water;
        this.medical = medical;
    }

    // Gives up to what is available in stock and updates remaining count
    public int allocate(String item, int needed) {
        int given = 0;
        if (item.equals("FOOD")) {
            given = Math.min(food, needed);
            food -= given;
        } else if (item.equals("WATER")) {
            given = Math.min(water, needed);
            water -= given;
        } else if (item.equals("MEDICAL")) {
            given = Math.min(medical, needed);
            medical -= given;
        }
        return given;
    }

    public void restock(String item, int amount) {
        if (item.equals("FOOD")) {
            food += amount;
        } else if (item.equals("WATER")) {
            water += amount;
        } else if (item.equals("MEDICAL")) {
            medical += amount;
        }
    }

    public String getName() { return name; }
    public int getFood() { return food; }
    public int getWater() { return water; }
    public int getMedical() { return medical; }
}