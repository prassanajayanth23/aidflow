import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AllocationService service = new AllocationService();

        Neighborhood[] areas = {
                new Neighborhood("Harbor District", "Coastal flooding, homes submerged", 500, "CRITICAL", "Bayport Central"),
                new Neighborhood("Old Town", "Power outage, roads blocked", 700, "HIGH", "Bayport Central"),
                new Neighborhood("Market Square", "Water rising in lower streets", 450, "MEDIUM", "Bayport Central"),
                new Neighborhood("Hillview", "Safe high ground, sheltering evacuees", 350, "LOW", "Hillview Depot")
        };

        ReliefCenter[] warehouses = {
                new ReliefCenter("Bayport Central", 300, 350, 25),
                new ReliefCenter("Hillview Depot", 200, 250, 15)
        };

        ResourceRequest[] requests = new ResourceRequest[50];
        int count = 0;

        while (true) {
            int totalFood = 0, totalWater = 0, totalMed = 0;
            for (int i = 0; i < warehouses.length; i++) {
                totalFood += warehouses[i].getFood();
                totalWater += warehouses[i].getWater();
                totalMed += warehouses[i].getMedical();
            }

            System.out.println("\n  ======================================================");
            System.out.println("    AIDFLOW | Bayport Relief Dispatch");
            System.out.println("    Requests: " + count + "  |  Stock: " + totalFood + " Food, " + totalWater + " Water, " + totalMed + " Med");
            System.out.println("  ======================================================");
            System.out.println("    1. View Full Overview (Areas, Stock & Requests)");
            System.out.println("    2. Inspect Area & Add Request");
            System.out.println("    3. Run Priority Dispatch");
            System.out.println("    4. Restock Warehouse");
            System.out.println("    5. Change Area Danger Level");
            System.out.println("    6. Toggle Road Access (Open / Flooded)");
            System.out.println("    0. Exit");
            System.out.println("  ------------------------------------------------------");

            int choice = readInt(sc, "  Choose (0-6): ", 0, 6);

            if (choice == 1) {
                System.out.println("\n  AREAS");
                System.out.println("  ----------------------------------------------------------------------");
                System.out.printf("  %-17s %-10s %-7s %-8s %-9s %s%n", "NAME", "DANGER", "POP.", "SCORE", "ROAD", "NEAREST DEPOT");
                for (int i = 0; i < areas.length; i++) {
                    String road = areas[i].isRoadOpen() ? "OPEN" : "CLOSED";
                    System.out.printf("  %-17s %-10s %-7d %-8d %-9s %s%n",
                            areas[i].getName(), areas[i].getDanger(),
                            areas[i].getPopulation(), areas[i].getPriorityScore(),
                            road, areas[i].getNearestWarehouse());
                }

                System.out.println("\n  WAREHOUSES");
                System.out.println("  ----------------------------------------------------------------------");
                System.out.printf("  %-20s %-9s %-9s %s%n", "DEPOT", "FOOD", "WATER", "MEDICAL");
                for (int i = 0; i < warehouses.length; i++) {
                    System.out.printf("  %-20s %-9d %-9d %d%n",
                            warehouses[i].getName(), warehouses[i].getFood(),
                            warehouses[i].getWater(), warehouses[i].getMedical());
                }

                System.out.println("\n  REQUESTS (" + count + ")");
                System.out.println("  ----------------------------------------------------------------------");
                if (count == 0) {
                    System.out.println("  No requests yet. Use Option 2 to add one.");
                } else {
                    System.out.printf("  %-4s %-17s %-9s %-11s %s%n", "#", "AREA", "ITEM", "DELIVERED", "STATUS");
                    for (int i = 0; i < count; i++) {
                        ResourceRequest r = requests[i];
                        String st = (!r.getArea().isRoadOpen() && r.getRemaining() > 0) ? "BLOCKED" : r.getStatus();
                        System.out.printf("  %-4d %-17s %-9s %-11s %s%n",
                                (i + 1), r.getArea().getName(), r.getItem(),
                                r.getDelivered() + "/" + r.getNeeded(), st);
                    }
                }
                pause(sc);

            } else if (choice == 2) {
                System.out.println("\n  SELECT AREA (0 to Back)");
                for (int i = 0; i < areas.length; i++) {
                    String road = areas[i].isRoadOpen() ? "OPEN" : "ROAD CLOSED";
                    System.out.println("    " + (i + 1) + ". " + areas[i].getName()
                            + " [" + areas[i].getDanger() + " | " + road + "]");
                }
                int a = readInt(sc, "  Area (0-4): ", 0, 4);
                if (a == 0) continue;

                Neighborhood selected = areas[a - 1];
                String road = selected.isRoadOpen() ? "OPEN" : "CLOSED (Flooded)";

                System.out.println("\n  [ " + selected.getName().toUpperCase() + " - " + selected.getDanger() + " ]");
                System.out.println("  Situation  : " + selected.getSituation());
                System.out.println("  Access Road: " + road + "  |  Score: " + selected.getPriorityScore()
                        + "  |  Depot: " + selected.getNearestWarehouse());
                System.out.println("  ------------------------------------------------------");

                boolean found = false;
                for (int i = 0; i < count; i++) {
                    if (requests[i].getArea() == selected) {
                        System.out.println("  * " + requests[i].getItem() + ": "
                                + requests[i].getDelivered() + "/" + requests[i].getNeeded()
                                + " (" + requests[i].getStatus() + ")");
                        found = true;
                    }
                }
                if (!found) {
                    System.out.println("  * No requests yet.");
                }
                System.out.println("  ------------------------------------------------------");

                System.out.println("  Request item:  1. Food   2. Water   3. Medical   0. Back");
                int r = readInt(sc, "  Item (0-3): ", 0, 3);
                if (r == 0) continue;

                if (count >= requests.length) {
                    System.out.println("  Request list is full.");
                    pause(sc);
                    continue;
                }

                String item = (r == 1) ? "FOOD" : (r == 2) ? "WATER" : "MEDICAL";
                int qty = readInt(sc, "  Units (1-5000): ", 1, 5000);

                requests[count++] = new ResourceRequest(selected, item, qty);
                System.out.println("  Saved: " + qty + " " + item + " for " + selected.getName() + ".");
                pause(sc);

            } else if (choice == 3) {
                if (count == 0) {
                    System.out.println("\n  No requests to dispatch yet. Use Option 2 first.");
                } else {
                    service.dispatch(requests, count, warehouses);
                }
                pause(sc);

            } else if (choice == 4) {
                System.out.println("\n  RESTOCK WAREHOUSE");
                System.out.println("    1. Bayport Central   2. Hillview Depot   0. Back");
                int w = readInt(sc, "  Warehouse (0-2): ", 0, 2);
                if (w == 0) continue;

                System.out.println("  Item:  1. Food   2. Water   3. Medical");
                int r = readInt(sc, "  Item (1-3): ", 1, 3);
                String item = (r == 1) ? "FOOD" : (r == 2) ? "WATER" : "MEDICAL";

                int qty = readInt(sc, "  Units (1-5000): ", 1, 5000);
                warehouses[w - 1].restock(item, qty);
                System.out.println("  Updated: Added " + qty + " " + item + " to " + warehouses[w - 1].getName() + ".");
                pause(sc);

            } else if (choice == 5) {
                System.out.println("\n  CHANGE DANGER LEVEL (0 to Back)");
                for (int i = 0; i < areas.length; i++) {
                    System.out.println("    " + (i + 1) + ". " + areas[i].getName() + " (" + areas[i].getDanger() + ")");
                }
                int a = readInt(sc, "  Area (0-4): ", 0, 4);
                if (a == 0) continue;

                System.out.println("  New level:  1. CRITICAL   2. HIGH   3. MEDIUM   4. LOW");
                int d = readInt(sc, "  Level (1-4): ", 1, 4);
                String newDanger = (d == 1) ? "CRITICAL" : (d == 2) ? "HIGH" : (d == 3) ? "MEDIUM" : "LOW";

                areas[a - 1].setDanger(newDanger);
                System.out.println("  Updated: " + areas[a - 1].getName() + " is now " + newDanger
                        + " (Score: " + areas[a - 1].getPriorityScore() + ").");
                pause(sc);

            } else if (choice == 6) {
                System.out.println("\n  TOGGLE ROAD ACCESS (0 to Back)");
                for (int i = 0; i < areas.length; i++) {
                    String status = areas[i].isRoadOpen() ? "OPEN" : "CLOSED (Flooded)";
                    System.out.println("    " + (i + 1) + ". " + areas[i].getName() + " [" + status + "]");
                }
                int a = readInt(sc, "  Select area to toggle (0-4): ", 0, 4);
                if (a == 0) continue;

                Neighborhood target = areas[a - 1];
                target.setRoadOpen(!target.isRoadOpen());
                String updated = target.isRoadOpen() ? "OPEN (Cleared for trucks)" : "CLOSED (Flooded - trucks held)";
                System.out.println("  Road Alert: " + target.getName() + " is now " + updated + ".");
                pause(sc);

            } else if (choice == 0) {
                System.out.println("  Exited AidFlow.");
                break;
            }
        }
        sc.close();
    }

    private static int readInt(Scanner sc, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextInt()) {
                int val = sc.nextInt();
                sc.nextLine();
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.println("  Enter a number from " + min + " to " + max + ".");
            } else {
                System.out.println("  Numbers only, please.");
                sc.nextLine();
            }
        }
    }

    private static void pause(Scanner sc) {
        System.out.print("\n  Press Enter to continue...");
        sc.nextLine();
    }
}