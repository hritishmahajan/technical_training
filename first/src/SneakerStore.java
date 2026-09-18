import java.util.ArrayList;
import java.util.List;

// INTERFACE (Abstraction)
// "Contract" - jo bhi class ise implement karegi, usko sell() aur
// checkAvailability() define karna hi padega.
interface Sellable {
    void sell(int quantity);
    boolean checkAvailability(int quantity);
}


// ABSTRACT CLASS (Abstraction + Encapsulation)
// Sneaker ek "template" hai - common fields/methods yaha, lekin
// discount calculate karne ka tarika har sneaker type ke liye alag hoga,
// isliye calculateDiscount() ko abstract rakhenge.
abstract class Sneaker implements Sellable {
    // Private fields -> encapsulation, directly bahar se change nahi ho sakte
    private String brand;
    private String model;
    private double price;
    private int stockCount;

    public Sneaker(String brand, String model, double price, int stockCount) {
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.stockCount = stockCount;
    }

    // Getters (controlled read access)
    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double getPrice() {
        return price;
    }

    public int getStockCount() {
        return stockCount;
    }

    // Overloaded restock methods (Compile-time Polymorphism)
    // Ek version - sirf quantity leta hai
    public void restock(int quantity) {
        stockCount += quantity;
        System.out.println("Restocked " + quantity + " units of " + model
                + ". New stock: " + stockCount);
    }

    // Dusra version - quantity ke saath ek reason bhi leta hai (overloaded)
    public void restock(int quantity, String reason) {
        stockCount += quantity;
        System.out.println("Restocked " + quantity + " units of " + model
                + " (Reason: " + reason + "). New stock: " + stockCount);
    }

    // Interface methods implemented here (common logic sab child classes ke liye)
    @Override
    public boolean checkAvailability(int quantity) {
        return stockCount >= quantity;
    }

    @Override
    public void sell(int quantity) {
        // Validation yaha hi ho raha hai - encapsulation ka fayda
        if (checkAvailability(quantity)) {
            stockCount -= quantity;
            System.out.println("Sold " + quantity + " unit(s) of " + model
                    + ". Remaining stock: " + stockCount);
        } else {
            System.out.println("Sorry! Not enough stock for " + model
                    + ". Available: " + stockCount);
        }
    }

    // Abstract method - har sneaker type apna discount logic khud define karega
    abstract double calculateDiscount();

    // Concrete (already implemented) helper method - common for all
    public void showFinalPrice() {
        double discount = calculateDiscount();
        double finalPrice = price - discount;
        System.out.println(brand + " " + model + " | MRP: " + price
                + " | Discount: " + discount + " | Final Price: " + finalPrice);
    }
}


// INHERITANCE + POLYMORPHISM (Runtime)

// Running shoes - seasonal 20% discount
class RunningShoe extends Sneaker {
    private String cushioningType; // extra field jo sirf RunningShoe ke paas hai

    public RunningShoe(String brand, String model, double price, int stockCount, String cushioningType) {
        super(brand, model, price, stockCount); // parent constructor call
        this.cushioningType = cushioningType;
    }

    @Override
    double calculateDiscount() {
        // 20% flat discount running shoes pe (seasonal offer)
        return getPrice() * 0.20;
    }

    public void showCushioningInfo() {
        System.out.println(getModel() + " has " + cushioningType + " cushioning.");
    }
}

// Basketball shoes - flat 500 off (no percentage)
class BasketballShoe extends Sneaker {
    private boolean ankleSupport;

    public BasketballShoe(String brand, String model, double price, int stockCount, boolean ankleSupport) {
        super(brand, model, price, stockCount);
        this.ankleSupport = ankleSupport;
    }

    @Override
    double calculateDiscount() {
        // Flat 500 discount - chahe price kuch bhi ho
        return 500;
    }

    public void showAnkleSupportInfo() {
        System.out.println(getModel() + (ankleSupport ? " has" : " does NOT have") + " ankle support.");
    }
}

// Casual sneakers - no discount by default
class CasualSneaker extends Sneaker {
    private final String style;

    public CasualSneaker(String brand, String model, double price, int stockCount, String style) {
        super(brand, model, price, stockCount);
        this.style = style;
    }

    @Override
    double calculateDiscount() {
        // Casual sneakers pe abhi koi discount nahi hai
        return 0;
    }

    public void showStyleInfo() {
        System.out.println(getModel() + " style: " + style);
    }
}


// MAIN CLASS - Store simulation
public class SneakerStore {
    public static void main(String[] args) {

        System.out.println("---- Creating Store Inventory ----");
        RunningShoe nikeRun = new RunningShoe("Nike", "Air Zoom Pegasus", 8000, 10, "Air Zoom");
        BasketballShoe jordan = new BasketballShoe("Jordan", "Air Jordan 1", 12000, 5, true);
        CasualSneaker vans = new CasualSneaker("Vans", "Old Skool", 4500, 15, "Skater/Retro");

        // Saari sneakers ek common list mein daal rahe hain -> Polymorphism ka setup
        // (List<Sneaker> hai, par actual objects alag-alag types ke hain)
        List<Sneaker> inventory = new ArrayList<>();
        inventory.add(nikeRun);
        inventory.add(jordan);
        inventory.add(vans);

        System.out.println("\n---- Showing Final Prices (Runtime Polymorphism) ----");
        // Yaha loop mein hum Sneaker reference use kar rahe hain,
        // lekin calculateDiscount() ka actual behavior runtime pe decide hota hai
        // depending on the real object type (Running/Basketball/Casual)
        for (Sneaker s : inventory) {
            s.showFinalPrice();
        }

        System.out.println("\n---- Extra info specific to each type ----");
        nikeRun.showCushioningInfo();
        jordan.showAnkleSupportInfo();
        vans.showStyleInfo();

        System.out.println("\n---- Selling & Stock Management ----");
        jordan.sell(2);       // valid sale
        jordan.sell(10);      // invalid - not enough stock

        System.out.println("\n---- Restocking (Overloading demo) ----");
        vans.restock(20);                       // version 1 - sirf quantity
        vans.restock(10, "Festive season demand"); // version 2 - quantity + reason

        System.out.println("\n---- Final Inventory Check ----");
        for (Sneaker s : inventory) {
            System.out.println(s.getModel() + " -> Stock left: " + s.getStockCount());
        }

    }
}