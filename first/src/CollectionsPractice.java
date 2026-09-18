import java.util.*;

public class CollectionsPractice {
    public static void main(String[] args) {

        System.out.println("LIST");
        // ordered, duplicates allowed, index based access
        List<String> cities = new ArrayList<>();
        cities.add("Delhi");
        cities.add("Mumbai");
        cities.add("Chandigarh");
        cities.add("Delhi"); // dup is fine in a list
        System.out.println(cities);
        System.out.println("index 1: " + cities.get(1));
        cities.remove("Delhi"); // removes first match
        System.out.println("after remove: " + cities);

        // linkedlist version, better for add/remove at ends
        LinkedList<String> tasks = new LinkedList<>();
        tasks.add("Task 1");
        tasks.addFirst("Urgent");
        tasks.addLast("Low Priority");
        System.out.println("linkedlist: " + tasks);


        System.out.println("SET");
        // no duplicates, hashset order is random basically
        Set<String> colors = new HashSet<>();
        colors.add("Red");
        colors.add("Green");
        colors.add("Red"); // ignored, already there
        System.out.println(colors);

        // linkedhashset keeps insertion order
        Set<String> orderedColors = new LinkedHashSet<>();
        orderedColors.add("Third");
        orderedColors.add("First");
        orderedColors.add("Second");
        System.out.println("insertion order kept: " + orderedColors);

        // treeset auto sorts
        Set<Integer> sorted = new TreeSet<>();
        sorted.add(50);
        sorted.add(10);
        sorted.add(30);
        System.out.println("auto sorted: " + sorted);


        System.out.println("MAP");
        // key-value pairs, keys are unique
        Map<String, Integer> marks = new HashMap<>();
        marks.put("Aman", 85);
        marks.put("Priya", 92);
        marks.put("Aman", 90); // overwrites old value
        System.out.println(marks);
        System.out.println("aman: " + marks.get("Aman"));

        // looping a map
        for (Map.Entry<String, Integer> e : marks.entrySet()) {
            System.out.println(e.getKey() + " -> " + e.getValue());
        }

        // getOrDefault avoids null checks
        System.out.println("rahul (not there): " + marks.getOrDefault("Rahul", 0));

        // treemap sorts by key
        Map<String, Integer> treeMap = new TreeMap<>();
        treeMap.put("Banana", 40);
        treeMap.put("Apple", 60);
        System.out.println("sorted by key: " + treeMap);


        System.out.println("QUEUE");
        // fifo - first in first out
        Queue<String> line = new LinkedList<>();
        line.offer("Customer A");
        line.offer("Customer B");
        System.out.println("next up: " + line.peek());
        System.out.println("serving: " + line.poll());
        System.out.println("left: " + line);

        // deque - both ends, can act like stack too
        Deque<String> stack = new ArrayDeque<>();
        stack.push("Page 1");
        stack.push("Page 2");
        System.out.println("stack: " + stack);
        System.out.println("back to: " + stack.pop());

        // priorityqueue - smallest comes out first
        Queue<Integer> pq = new PriorityQueue<>();
        pq.offer(50);
        pq.offer(10);
        pq.offer(30);
        System.out.print("priority order: ");
        while (!pq.isEmpty()) {
            System.out.print(pq.poll() + " ");
        }
        System.out.println();
    }
}