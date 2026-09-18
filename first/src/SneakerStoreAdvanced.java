import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//ENUM
// Enum ek fixed set of constants define karta hai - "magic strings"
// (jaise "SMALL", "MEDIUM" as plain String) use karne se better hai,
// kyunki compiler hi typo ya invalid value pakad lega.
enum ShoeSize {
    UK6, UK7, UK8, UK9, UK10
}


// CUSTOM EXCEPTION
// Apna khud ka exception banate hain jab built-in exceptions (jaise IllegalArgumentException) specific enough nahi lagte.
// "extends Exception" -> checked exception (caller ko handle karna zaroori)
class OutOfStockException extends Exception {
    public OutOfStockException(String message) {
        super(message); // parent Exception class ka constructor call
    }
}

// INTERFACES (Multiple Interface Implementation)
interface SellableItem {
    void sell(int quantity) throws OutOfStockException;
}

// Dusra interface - discountable items ke liye alag contract
interface Discountable {
    double calculateDiscount();
}


// MAIN ENTITY CLASS
// "final" class - matlab is class ko koi aur extend nahi kar sakta.
// Yahan sirf demonstrate karne ke liye final use kiya (design choice).
final class SneakerAdv implements SellableItem, Discountable {

    // static field
    // static field CLASS ke saath associated hota hai, har OBJECT ke saath nahi.
    // Isliye ye sab Sneaker objects ke beech SHARED rehta hai.
    private static int totalSneakersSoldAcrossStore = 0;

    // final fields
    // final matlab constructor mein ek baar set hone ke baad value change nahi ho sakti.
    private final String brand;
    private final String model;
    protected final double price; // protected -> subclass/same package access allowed hai
    private final ShoeSize size;

    private int stockCount; // ye change hota rehta hai (sell/restock), isliye final nahi

    // Constructor overloading with this()
    // Full constructor - sab fields
    public SneakerAdv(String brand, String model, double price, ShoeSize size, int stockCount) {
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.size = size;
        this.stockCount = stockCount;
    }

    // Overloaded constructor - agar stockCount nahi diya, default 0 maan lo.
    // "this(...)" se hum upar wale (main) constructor ko hi call kar rahe hain,
    // taaki logic duplicate na ho.
    public SneakerAdv(String brand, String model, double price, ShoeSize size) {
        this(brand, model, price, size, 0); // delegate to main constructor
        System.out.println("Note: " + model + " created with default stock 0.");
    }

    // static method
    // Static method bhi class-level hota hai - object banaye bina call ho sakta hai.
    // Jaise: SneakerAdv.getTotalSoldAcrossStore()
    public static int getTotalSoldAcrossStore() {
        return totalSneakersSoldAcrossStore;
    }

    // Interface implementations
    @Override
    public void sell(int quantity) throws OutOfStockException {
        // Custom exception throw kar rahe hain jab stock kam ho -
        // ab caller ko try-catch mandatory hoga (checked exception).
        if (quantity > stockCount) {
            throw new OutOfStockException(
                    "Cannot sell " + quantity + " units of " + model
                            + ". Only " + stockCount + " left in stock.");
        }
        stockCount -= quantity;
        totalSneakersSoldAcrossStore += quantity; // static field update - store-wide counter
        System.out.println("Sold " + quantity + " unit(s) of " + model
                + ". Remaining: " + stockCount);
    }

    @Override
    public double calculateDiscount() {
        // Simple rule: agar price 10000 se zyada hai to 15% discount, warna 5%
        return price > 10000 ? price * 0.15 : price * 0.05;
    }

    // final method
    // "final" method - koi subclass isko override nahi kar sakta
    // (yaha class hi final hai, but agar class final na hoti to ye still lock rehta).
    public final void restock(int quantity) {
        stockCount += quantity;
        System.out.println("Restocked " + quantity + " units of " + model);
    }

    public int getStockCount() {
        return stockCount;
    }

    public String getModel() {
        return model;
    }

    // toString() override
    // Default Object.toString() kuch aisa print karta hai: SneakerAdv@1b6d3586
    // (memory address jaisa, useless). Isko override karke readable banate hain.
    @Override
    public String toString() {
        return brand + " " + model + " [Size: " + size + ", Price: " + price
                + ", Stock: " + stockCount + "]";
    }

    // equals() override
    // Do sneakers ko "equal" tab maanenge jab brand + model + size same ho
    // (chahe alag object ho, alag stock ho).
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;               // same reference -> trivially equal
        if (obj == null || getClass() != obj.getClass()) return false; // type mismatch
        SneakerAdv other = (SneakerAdv) obj;         // safe cast - type already checked
        return brand.equals(other.brand)
                && model.equals(other.model)
                && size == other.size;
    }

    // hashCode() override
    // Jab bhi equals() override karte hain, hashCode() bhi override karna
    // best practice hai (warna HashMap/HashSet mein weird behavior aa sakta hai).
    @Override
    public int hashCode() {
        return Objects.hash(brand, model, size);
    }
}


// COMPOSITION ("has-a" relationship)
// Store "is not a" Sneaker - Store "has" sneakers. Isliye yaha inheritance
// nahi, composition use karenge (ek class dusri class ko field ke roop mein hold kare).
class Store {
    private String storeName;
    private List<SneakerAdv> inventory; // Composition -> Store HAS-A list of SneakerAdv

    public Store(String storeName) {
        this.storeName = storeName;
        this.inventory = new ArrayList<>();
    }

    public void addSneaker(SneakerAdv sneaker) {
        inventory.add(sneaker);
        System.out.println("Added to " + storeName + ": " + sneaker);
    }

    public List<SneakerAdv> getInventory() {
        return inventory;
    }
}

public class SneakerStoreAdvanced {
    public static void main(String[] args) {

        System.out.println("---- Constructor Overloading (this()) ----");
        SneakerAdv nike = new SneakerAdv("Nike", "Air Max", 11000, ShoeSize.UK9, 8);
        SneakerAdv puma = new SneakerAdv("Puma", "RS-X", 6000, ShoeSize.UK8); // uses overloaded ctor

        System.out.println("\n---- Composition (Store HAS sneakers) ----");
        Store store = new Store("Sneaker World - Chandigarh");
        store.addSneaker(nike);
        store.addSneaker(puma);

        System.out.println("\n---- toString() in action ----");
        // println() internally calls toString() - ab readable output milega
        System.out.println(nike);
        System.out.println(puma);

        System.out.println("\n---- Selling with Custom Exception Handling ----");
        try {
            nike.sell(3);   // valid
            nike.sell(10);  // invalid - triggers OutOfStockException
        } catch (OutOfStockException e) {
            // Checked exception hai, isliye handle karna zaroori tha
            System.out.println("Error caught: " + e.getMessage());
        }

        System.out.println("\n---- static field/method demo ----");
        // Object banaye bina bhi static method call kar sakte hain
        System.out.println("Total sneakers sold across store so far: "
                + SneakerAdv.getTotalSoldAcrossStore());

        System.out.println("\n---- equals() & hashCode() demo ----");
        SneakerAdv nikeDuplicate = new SneakerAdv("Nike", "Air Max", 11000, ShoeSize.UK9, 2);
        // Same brand/model/size -> equals() ke hisaab se TRUE, chahe stock/price object alag ho
        System.out.println("nike.equals(nikeDuplicate) = " + nike.equals(nikeDuplicate));
        System.out.println("nike.equals(puma) = " + nike.equals(puma));

        System.out.println("\n---- instanceof + downcasting demo ----");
        Object mysteryItem = puma; // upcast to Object reference (generic type)
        if (mysteryItem instanceof SneakerAdv) {
            // Safe downcast - pehle instanceof se confirm kar liya ki type sahi hai
            SneakerAdv confirmedSneaker = (SneakerAdv) mysteryItem;
            System.out.println("Downcast successful: " + confirmedSneaker.getModel());
        }

        System.out.println("\n---- Discount check via interface reference ----");
        // Discountable reference se sirf discount-related method dikhega (abstraction)
        Discountable discountableNike = nike;
        System.out.println("Discount on " + nike.getModel() + " = " + discountableNike.calculateDiscount());


    }
}