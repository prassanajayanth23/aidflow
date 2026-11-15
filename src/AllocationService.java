public class AllocationService {

    public void dispatch(ResourceRequest[] requests, int count, ReliefCenter[] warehouses) {
        // 1. Sort requests by highest priority score first (Bubble Sort)
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - 1 - i; j++) {
                int score1 = requests[j].getArea().getPriorityScore();
                int score2 = requests[j + 1].getArea().getPriorityScore();

                if (score1 < score2) {
                    ResourceRequest temp = requests[j];
                    requests[j] = requests[j + 1];
                    requests[j + 1] = temp;
                }
            }
        }

        // 2. Allocate stock (skip areas whose access road is flooded/closed)
        for (int i = 0; i < count; i++) {
            ResourceRequest r = requests[i];

            if (!r.getArea().isRoadOpen()) {
                continue; // Trucks cannot reach this area yet
            }

            int remaining = r.getRemaining();

            // Nearest warehouse first
            for (int j = 0; j < warehouses.length && remaining > 0; j++) {
                if (warehouses[j].getName().equals(r.getArea().getNearestWarehouse())) {
                    int given = warehouses[j].allocate(r.getItem(), remaining);
                    r.recordDelivery(warehouses[j].getName(), given);
                    remaining -= given;
                }
            }

            // Fallback to other warehouses
            for (int j = 0; j < warehouses.length && remaining > 0; j++) {
                int given = warehouses[j].allocate(r.getItem(), remaining);
                r.recordDelivery(warehouses[j].getName(), given);
                remaining -= given;
            }
        }

        // 3. Print Dispatch Report
        int totalNeeded = 0, totalDelivered = 0;
        int missingFood = 0, missingWater = 0, missingMedical = 0;
        int blockedCount = 0;

        System.out.println("\n  ==================================================================================");
        System.out.println("    DISPATCH REPORT - BAYPORT");
        System.out.println("  ==================================================================================");
        System.out.printf("  %-4s %-17s %-10s %-9s %-11s %-10s %s%n",
                "#", "AREA", "DANGER", "ITEM", "DELIVERED", "STATUS", "SENT FROM");
        System.out.println("  ----------------------------------------------------------------------------------");

        for (int i = 0; i < count; i++) {
            ResourceRequest r = requests[i];
            totalNeeded += r.getNeeded();
            totalDelivered += r.getDelivered();

            String status = r.getStatus();
            if (!r.getArea().isRoadOpen() && r.getRemaining() > 0) {
                status = "BLOCKED";
                blockedCount++;
            } else {
                int missing = r.getRemaining();
                if (missing > 0) {
                    if (r.getItem().equals("FOOD")) missingFood += missing;
                    else if (r.getItem().equals("WATER")) missingWater += missing;
                    else if (r.getItem().equals("MEDICAL")) missingMedical += missing;
                }
            }

            System.out.printf("  %-4d %-17s %-10s %-9s %-11s %-10s %s%n",
                    (i + 1),
                    r.getArea().getName(),
                    r.getArea().getDanger(),
                    r.getItem(),
                    r.getDelivered() + "/" + r.getNeeded(),
                    status,
                    r.getSentFrom());
        }

        System.out.println("  ----------------------------------------------------------------------------------");
        System.out.println("  Delivered : " + totalDelivered + " / " + totalNeeded + " units");

        if (blockedCount > 0) {
            System.out.println("  Road Alert: " + blockedCount + " request(s) held due to flooded/closed roads.");
        }
        if (missingFood > 0 || missingWater > 0 || missingMedical > 0) {
            System.out.print("  Shortage  : ");
            if (missingFood > 0) System.out.print(missingFood + " FOOD  ");
            if (missingWater > 0) System.out.print(missingWater + " WATER  ");
            if (missingMedical > 0) System.out.print(missingMedical + " MEDICAL");
            System.out.println();
        } else if (blockedCount == 0) {
            System.out.println("  Status    : All requests fulfilled");
        }
        System.out.println("  ==================================================================================");
    }
}