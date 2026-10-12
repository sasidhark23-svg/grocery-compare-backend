package com.grocerycompare.backend.config;

import com.grocerycompare.backend.entity.Price;
import com.grocerycompare.backend.entity.Product;
import com.grocerycompare.backend.entity.StockStatus;
import com.grocerycompare.backend.entity.Store;
import com.grocerycompare.backend.repository.PriceRepository;
import com.grocerycompare.backend.repository.ProductRepository;
import com.grocerycompare.backend.repository.StoreRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/** Seeds clearly illustrative catalog data without replacing existing records. */
@Component
public class DemoCatalogSeeder implements CommandLineRunner {
    private static final String OPEN_FOOD_FACTS = "https://images.openfoodfacts.org/images/products/";
    private static final String OPEN_PRODUCTS_FACTS = "https://images.openproductsfacts.org/images/products/";
    private static final String OPEN_BEAUTY_FACTS = "https://images.openbeautyfacts.org/images/products/";

    private final ProductRepository products;
    private final StoreRepository stores;
    private final PriceRepository prices;

    public DemoCatalogSeeder(ProductRepository products, StoreRepository stores, PriceRepository prices) {
        this.products = products;
        this.stores = stores;
        this.prices = prices;
    }

    @Override
    public void run(String... args) {
        List<Store> demoStores = List.of(
                ensureStore("Reliance Smart", "Mumbai, MH", "https://reliancesmart.in"),
                ensureStore("DMart", "Thane, MH", "https://dmart.in"),
                ensureStore("BigBasket", "Online", "https://bigbasket.com"),
                ensureStore("Amazon Fresh", "Online", "https://amazon.in/fresh"),
                ensureStore("Blinkit", "Quick delivery", "https://blinkit.com")
        );

        List<Item> catalog = List.of(
                item("Amul Taaza Toned Milk", "Amul", "Dairy", "1 L", 58, productImage("890/126/215/0064/front_en.37.400.jpg")),
                item("Amul Gold Full Cream Milk", "Amul", "Dairy", "1 L", 68, productImage("890/126/226/0107/front_en.3.400.jpg")),
                item("Mother Dairy Toned Milk", "Mother Dairy", "Dairy", "1 L", 56, productImage("890/164/892/1325/front_en.3.400.jpg")),
                item("Amul Pasteurised Butter", "Amul", "Dairy", "500 g", 285, productImage("890/126/201/0023/front_en.7.400.jpg")),
                item("Mother Dairy Classic Curd", "Mother Dairy", "Dairy", "400 g", 45, productImage("890/164/802/2169/front_en.15.400.jpg")),
                item("Paneer Classic", "Amul", "Dairy", "200 g", 90,
                        commonsImage("2/27/Barbequed_Paneer_Tikka.jpg", "500px-Barbequed_Paneer_Tikka.jpg")),
                item("Fresh Tomato", "Farm Fresh", "Vegetables", "1 kg", 38,
                        commonsImage("f/f3/Bounty_of_Fresh_Tomatoes.jpg", "500px-Bounty_of_Fresh_Tomatoes.jpg")),
                item("Red Onion", "Farm Fresh", "Vegetables", "1 kg", 42,
                        commonsImage("2/2f/Bulk_red_onions_vegetables%2C_April_2023.jpg", "500px-Bulk_red_onions_vegetables%2C_April_2023.jpg")),
                item("Potato", "Farm Fresh", "Vegetables", "1 kg", 32,
                        commonsImage("2/2d/Liat_Portal_for_Foodie_Disorder_-_Potatoes_from_San_Francisco_farmers_market.jpg", "500px-Liat_Portal_for_Foodie_Disorder_-_Potatoes_from_San_Francisco_farmers_market.jpg")),
                item("Green Capsicum", "Farm Fresh", "Vegetables", "500 g", 36,
                        commonsImage("8/82/Chopped_Green_Bell_Pepper.jpg", "500px-Chopped_Green_Bell_Pepper.jpg")),
                item("Carrot", "Farm Fresh", "Vegetables", "500 g", 32,
                        commonsImage("b/ba/Carrot_on_plate.jpg", "500px-Carrot_on_plate.jpg")),
                item("Cucumber", "Farm Fresh", "Vegetables", "500 g", 28,
                        commonsImage("7/72/Cucumber_on_tomato_-_20180903_130208.jpg", "500px-Cucumber_on_tomato_-_20180903_130208.jpg")),
                item("Robusta Banana", "Farm Fresh", "Fruits", "1 dozen", 55,
                        productImage("000/059/000/1285/front_en.3.400.jpg")),
                item("Alphonso Mango", "Farm Fresh", "Fruits", "1 kg", 180,
                        commonsImage("8/88/Alphonso_Ripe_Mango_from_north_India.jpg", "500px-Alphonso_Ripe_Mango_from_north_India.jpg")),
                item("Nagpur Orange", "Farm Fresh", "Fruits", "1 kg", 95,
                        productImage("890/437/765/2288/front_en.3.400.jpg")),
                item("Royal Gala Apple", "Farm Fresh", "Fruits", "1 kg", 165,
                        productImage("000/000/045/5664/front_en.3.400.jpg")),
                item("Green Seedless Grapes", "Farm Fresh", "Fruits", "500 g", 75,
                        productImage("000/001/006/6096/front_en.20.400.jpg")),
                item("Pomegranate", "Farm Fresh", "Fruits", "1 kg", 155,
                        commonsImage("c/c8/Pomegranate_%28fruit%29.jpg", "500px-Pomegranate_%28fruit%29.jpg")),
                item("India Gate Classic Basmati Rice", "India Gate", "Rice & Grains", "1 kg", 145, productImage("069/022/530/3903/front_en.5.400.jpg")),
                item("Fortune Everyday Basmati Rice", "Fortune", "Rice & Grains", "1 kg", 105,
                        productImage("931/004/722/0363/front_en.3.400.jpg")),
                item("Tata Sampann Brown Rice", "Tata Sampann", "Rice & Grains", "1 kg", 115,
                        commonsImage("5/5c/Brown_Rice.jpg", "500px-Brown_Rice.jpg")),
                item("Fortune Poha", "Fortune", "Rice & Grains", "1 kg", 68,
                        productImage("890/600/881/2817/front_en.7.400.jpg")),
                item("Aashirvaad Chakki Atta", "Aashirvaad", "Rice & Grains", "5 kg", 265,
                        productImage("890/172/512/1136/front_en.3.400.jpg")),
                item("Tata Sampann Toor Dal", "Tata Sampann", "Pulses", "1 kg", 185, productImage("890/404/392/6216/front_en.5.400.jpg")),
                item("Tata Sampann Moong Dal", "Tata Sampann", "Pulses", "1 kg", 155,
                        productImage("890/404/392/6315/front_en.23.400.jpg")),
                item("Tata Sampann Chana Dal", "Tata Sampann", "Pulses", "1 kg", 105, productImage("890/404/392/6643/front_en.3.400.jpg")),
                item("Tata Sampann Masoor Dal", "Tata Sampann", "Pulses", "1 kg", 125, productImage("890/404/392/6650/front_en.3.400.jpg")),
                item("Rajma Chitra", "Tata Sampann", "Pulses", "500 g", 95,
                        commonsImage("4/43/Kinnauri_Rajma.jpg", "500px-Kinnauri_Rajma.jpg")),
                item("Kabuli Chana", "Tata Sampann", "Pulses", "500 g", 82, productImage("890/404/392/6704/front_en.3.400.jpg")),
                item("Maggi 2-Minute Masala Noodles", "Maggi", "Snacks", "Pack of 4", 56,
                        productImage("890/105/885/1328/front_en.17.400.jpg")),
                item("Parle-G Original Biscuits", "Parle", "Snacks", "800 g", 88,
                        productImage("890/171/912/3870/front_en.27.400.jpg")),
                item("Britannia Good Day Butter Cookies", "Britannia", "Snacks", "600 g", 110,
                        productImage("890/106/309/2822/front_en.4.400.jpg")),
                item("Lay's Classic Salted Chips", "Lay's", "Snacks", "3 x 48 g", 60, productImage("890/149/110/1837/front_en.26.400.jpg")),
                item("Haldiram's Bhujia Sev", "Haldiram's", "Snacks", "400 g", 105,
                        productImage("890/400/440/0021/front_en.3.400.jpg")),
                item("Sunfeast Dark Fantasy Choco Fills", "Sunfeast", "Snacks", "300 g", 115, productImage("890/172/501/5275/front_en.51.400.jpg")),
                item("Tata Tea Premium", "Tata Tea", "Beverages", "500 g", 285, productImage("890/105/201/0615/front_en.18.400.jpg")),
                item("Bru Instant Coffee", "Bru", "Beverages", "100 g", 165, productImage("890/103/053/5895/front_en.3.400.jpg")),
                item("Tropicana Mixed Fruit Juice", "Tropicana", "Beverages", "1 L", 120, productImage("890/208/000/2733/front_en.3.400.jpg")),
                item("Coca-Cola Original Taste", "Coca-Cola", "Beverages", "750 ml", 45,
                        commonsImage("d/d6/Bottle_of_Coca-Cola_with_a_glass_%2852267699916%29.jpg", "500px-Bottle_of_Coca-Cola_with_a_glass_%2852267699916%29.jpg")),
                item("Bisleri Packaged Drinking Water", "Bisleri", "Beverages", "1 L", 20,
                        productImage("890/601/729/0064/front_en.3.400.jpg")),
                item("Vim Dishwash Liquid", "Vim", "Household Essentials", "750 ml", 145,
                        openProductsImage("894/110/061/8073/front_bn.3.400.jpg")),
                item("Surf Excel Easy Wash Detergent", "Surf Excel", "Household Essentials", "1 kg", 125,
                        openProductsImage("890/910/602/6346/front_en.8.400.jpg")),
                item("Ariel Matic Liquid Detergent", "Ariel", "Household Essentials", "1 L", 235,
                        commonsImage("a/a0/Ariel_pods.jpg", "500px-Ariel_pods.jpg")),
                item("Lizol Citrus Floor Cleaner", "Lizol", "Household Essentials", "1 L", 195,
                        openProductsImage("894/110/050/7070/front_en.3.400.jpg")),
                item("Harpic Power Plus Toilet Cleaner", "Harpic", "Household Essentials", "1 L", 105,
                        openProductsImage("890/139/615/2002/front_en.21.400.jpg")),
                item("Colgate Strong Teeth Toothpaste", "Colgate", "Household Essentials", "200 g", 115,
                        commonsImage("0/0b/2022_Colgate_Toothpaste_for_Russia_market_assorted.jpg", "500px-2022_Colgate_Toothpaste_for_Russia_market_assorted.jpg")),
                item("Dove Cream Beauty Bathing Bar", "Dove", "Household Essentials", "Pack of 3", 165,
                        openBeautyImage("872/018/226/4664/front_en.12.400.jpg")),
                item("Parachute Coconut Oil", "Parachute", "Household Essentials", "500 ml", 155,
                        productImage("890/108/800/0451/front_en.18.400.jpg")),
                item("Tata Salt", "Tata", "Household Essentials", "1 kg", 28, productImage("890/404/390/1015/front_en.34.400.jpg")),
                item("Fortune Sunflower Oil", "Fortune", "Household Essentials", "1 L", 155, productImage("890/600/728/0242/front_en.18.400.jpg"))
        );

        int index = 1;
        for (Item item : catalog) {
            String barcode = "89012630" + String.format("%05d", index++);
            Product product = products.findByBarcode(barcode).orElseGet(() -> {
                Product created = new Product(item.name(), item.brand(), item.category(),
                        demoDescription(item), item.image(), barcode);
                created.setUnit(item.unit());
                return products.save(created);
            });
            if (product.getUnit() == null) {
                product.setUnit(item.unit());
                products.save(product);
            }
            if (!item.image().equals(product.getImageUrl())) {
                product.setImageUrl(item.image());
                products.save(product);
            }

            double[] multipliers = {1.00, 0.97, 1.04, 1.07, 1.02};
            for (int storeIndex = 0; storeIndex < demoStores.size(); storeIndex++) {
                Store store = demoStores.get(storeIndex);
                if (prices.findByProductIdAndStoreId(product.getId(), store.getId()).isEmpty()) {
                    double samplePrice = Math.round(item.price() * multipliers[storeIndex]);
                    prices.save(new Price(product, store, samplePrice, 0.0, StockStatus.IN_STOCK));
                }
            }
        }
    }

    private Store ensureStore(String name, String location, String website) {
        return stores.findByStoreNameIgnoreCase(name).orElseGet(() -> stores.save(
                new Store(name, "https://images.unsplash.com/photo-1578916171728-46686eac8d58?w=200&auto=format&fit=crop&q=80",
                        location, website, null)));
    }

    private static Item item(String name, String brand, String category, String unit, double price, String image) {
        return new Item(name, brand, category, unit, price, image);
    }

    private static String productImage(String path) {
        return OPEN_FOOD_FACTS + path;
    }

    private static String openProductsImage(String path) {
        return OPEN_PRODUCTS_FACTS + path;
    }

    private static String openBeautyImage(String path) {
        return OPEN_BEAUTY_FACTS + path;
    }

    private static String commonsImage(String filePath, String thumbnailName) {
        return "https://thumb.wikimedia.org/wikipedia/commons/thumb/" + filePath + "/" + thumbnailName;
    }

    private static String demoDescription(Item item) {
        String use = switch (item.category()) {
            case "Dairy" -> "A chilled everyday dairy essential for breakfast and home cooking";
            case "Vegetables" -> "A fresh produce staple for salads, sides, and everyday meals";
            case "Fruits" -> "A fresh fruit choice for snacks, lunchboxes, and desserts";
            case "Rice & Grains" -> "A pantry staple for breakfast, lunch, or family meals";
            case "Pulses" -> "A dried pulse pantry staple for dals, curries, and hearty meals";
            case "Snacks" -> "A ready-to-enjoy packaged snack for tea breaks and sharing";
            case "Beverages" -> "A drink for everyday refreshment at home or on the go";
            default -> "A household essential for everyday cleaning and personal care";
        };
        return use + ". Sample pack size: " + item.unit() + ". Included for comparison demonstrations.";
    }

    private record Item(String name, String brand, String category, String unit, double price, String image) { }
}
