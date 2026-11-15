# AidFlow — Emergency Relief Dispatch System (Phase 1)

A Java console application for managing disaster relief logistics during a flood emergency in Bayport. Built using core Object-Oriented Programming (OOP) principles, Phase 1 handles priority-based supply allocation, nearest-depot routing, and road blockage management.

## Phase 1 Features

* **Dynamic Priority Ranking:** Automatically sorts requests using a priority score (`Base Danger Level + Population`), ensuring `CRITICAL` zones are served first and larger populations break ties.
* **Nearest-Warehouse Routing:** Pulls inventory from the neighborhood's closest depot first to minimize transit time, falling back to secondary depots only when local stock is depleted.
* **Road Flood & Blockage Control:** Allows dispatchers to toggle road access (`OPEN` / `CLOSED`) for any zone mid-crisis. Blocked zones are held without wasting stock, automatically diverting supplies to the next highest-priority reachable area.
* **Real-Time Inventory & Restocking:** Tracks `FOOD`, `WATER`, and `MEDICAL` supplies across depots and calculates exact citywide shortages after each dispatch run.

## Project Structure

```text
src/
├── Main.java               # Interactive CLI menu and user input handling
├── Neighborhood.java       # Zone details, priority scoring, and road status
├── ReliefCenter.java       # Warehouse inventory and stock allocation
├── ResourceRequest.java    # Request tracking, fulfillment status, and route logs
└── AllocationService.java  # Priority sorting and multi-depot dispatch engine
