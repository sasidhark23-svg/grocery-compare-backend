package com.grocerycompare.backend.config;

import com.grocerycompare.backend.entity.*;
import com.grocerycompare.backend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final PriceRepository priceRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.demo-users.enabled:false}")
    private boolean demoUsersEnabled;

    public DataInitializer(
            UserRepository userRepository,
            ProductRepository productRepository,
            StoreRepository storeRepository,
            PriceRepository priceRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        this.priceRepository = priceRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Seed Admin & Regular User if not existing
        if (demoUsersEnabled && !userRepository.existsByEmail("admin@grocerycompare.com")) {
            userRepository.save(new User("Admin User", "admin@grocerycompare.com", passwordEncoder.encode("admin123"), Role.ROLE_ADMIN));
        }
        if (demoUsersEnabled && !userRepository.existsByEmail("user@grocerycompare.com")) {
            userRepository.save(new User("Demo User", "user@grocerycompare.com", passwordEncoder.encode("user123"), Role.ROLE_USER));
        }

        // Seed Stores if empty
        if (storeRepository.count() == 0) {
            Store rSmart = storeRepository.save(new Store("Reliance Smart", "https://images.unsplash.com/photo-1578916171728-46686eac8d58?w=100", "Mumbai, MH", "https://reliancesmart.in", 4.6));
            Store dMart = storeRepository.save(new Store("DMart", "https://images.unsplash.com/photo-1604719312566-8912e9227c6a?w=100", "Thane, MH", "https://dmart.in", 4.8));
            Store bigBasket = storeRepository.save(new Store("BigBasket", "https://images.unsplash.com/photo-1542838132-92c53300491e?w=100", "Online Direct", "https://bigbasket.com", 4.5));
            Store amazonFresh = storeRepository.save(new Store("Amazon Fresh", "https://images.unsplash.com/photo-1526304640581-d334cdbbf45e?w=100", "Online Nationwide", "https://amazon.in/fresh", 4.7));
            Store blinkit = storeRepository.save(new Store("Blinkit", "https://images.unsplash.com/photo-1578916171728-46686eac8d58?w=100", "Quick Delivery", "https://blinkit.com", 4.4));

            // Seed Products if empty
            if (productRepository.count() == 0) {
                // Dairy
                Product milk = productRepository.save(new Product("Amul Taaza Toned Milk 1L", "Amul", "Dairy", "Fresh toned milk, 100% pasteurized, high calcium content", "https://images.unsplash.com/photo-1563636619-e9143da7973b?w=400", "8901262010012"));
                Product butter = productRepository.save(new Product("Amul Pasteurised Butter 500g", "Amul", "Dairy", "Rich creamy pasteurised butter loved across India", "https://images.unsplash.com/photo-1589985270826-4b7bb135bc9d?w=400", "8901262010029"));
                Product curd = productRepository.save(new Product("Mother Dairy Misti Dahi 400g", "Mother Dairy", "Dairy", "Traditional Bengali sweet curd made with pure milk", "https://images.unsplash.com/photo-1488477181946-6428a0291777?w=400", "8901262010036"));

                // Rice & Grains
                Product rice = productRepository.save(new Product("India Gate Premium Basmati Rice 5kg", "India Gate", "Rice & Grains", "Aromatic long grain basmati rice ideal for biryani and daily meals", "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=400", "8901262020029"));
                Product poha = productRepository.save(new Product("Fortune Thick Poha 1kg", "Fortune", "Rice & Grains", "High fiber flattened rice flakes for quick breakfast poha", "https://images.unsplash.com/photo-1512058564366-18510be2db19?w=400", "8901262020036"));

                // Oils & Ghee
                Product oil = productRepository.save(new Product("Fortune Refined Sunflower Oil 1L", "Fortune", "Oils & Ghee", "Light and healthy refined sunflower oil rich in Vitamin E", "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=400", "8901262030036"));
                Product ghee = productRepository.save(new Product("Cow Ghee 1L Pouch - Amul", "Amul", "Oils & Ghee", "Pure cow ghee made from fresh cream with rich aroma and granular texture", "https://images.unsplash.com/photo-1608686207856-001b95cf60ca?w=400", "8901262030043"));

                // Atta & Flour
                Product atta = productRepository.save(new Product("Aashirvaad Shuddh Chakki Atta 5kg", "Aashirvaad", "Atta & Flour", "100% pure wheat flour ground in traditional stone chakkis", "https://images.unsplash.com/photo-1509440159596-0249088772ff?w=400", "8901262040043"));

                // Spices & Salt
                Product salt = productRepository.save(new Product("Tata Salt Vacuum Evaporated 1kg", "Tata", "Spices & Salt", "Iodized vacuum evaporated salt for everyday cooking", "https://images.unsplash.com/photo-1518110165400-928929e06c27?w=400", "8901262050050"));
                Product chilli = productRepository.save(new Product("Everest Tikhalal Red Chilli Powder 200g", "Everest", "Spices & Salt", "Fine ground red chilli powder for authentic color and spice", "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=400", "8901262050067"));

                // Snacks & Packaged Food
                Product maggi = productRepository.save(new Product("Maggi 2-Minute Masala Instant Noodles 420g", "Maggi", "Snacks & Packaged Food", "Classic 2-minute instant noodles with delicious Indian masala spices", "https://images.unsplash.com/photo-1612927601601-6638404737ce?w=400", "8901262060067"));
                Product biscuits = productRepository.save(new Product("Parle-G Gold Glucose Biscuits 1kg", "Parle", "Snacks & Packaged Food", "India's favorite glucose tea time biscuit pack", "https://images.unsplash.com/photo-1558961363-fa8fdf82db35?w=400", "8901262060074"));

                // Beverages
                Product tea = productRepository.save(new Product("Tata Tea Premium Country's Tea 500g", "Tata Tea", "Beverages", "Blended black tea leaves for rich taste and strong color", "https://images.unsplash.com/photo-1576092768241-dec231879fc3?w=400", "8901262070012"));

                // Seed Prices across stores
                seedPrice(milk, rSmart, 52.0, 3.0, StockStatus.IN_STOCK);
                seedPrice(milk, dMart, 54.0, 1.0, StockStatus.IN_STOCK);
                seedPrice(milk, bigBasket, 58.0, 0.0, StockStatus.IN_STOCK);
                seedPrice(milk, amazonFresh, 56.0, 2.0, StockStatus.IN_STOCK);

                seedPrice(butter, dMart, 240.0, 25.0, StockStatus.IN_STOCK);
                seedPrice(butter, rSmart, 248.0, 15.0, StockStatus.IN_STOCK);
                seedPrice(butter, blinkit, 265.0, 0.0, StockStatus.IN_STOCK);

                seedPrice(curd, dMart, 65.0, 5.0, StockStatus.IN_STOCK);
                seedPrice(curd, rSmart, 68.0, 2.0, StockStatus.IN_STOCK);

                seedPrice(rice, dMart, 540.0, 50.0, StockStatus.IN_STOCK);
                seedPrice(rice, rSmart, 560.0, 30.0, StockStatus.IN_STOCK);
                seedPrice(rice, bigBasket, 599.0, 0.0, StockStatus.IN_STOCK);
                seedPrice(rice, blinkit, 585.0, 15.0, StockStatus.IN_STOCK);

                seedPrice(poha, dMart, 52.0, 8.0, StockStatus.IN_STOCK);
                seedPrice(poha, rSmart, 55.0, 5.0, StockStatus.IN_STOCK);

                seedPrice(oil, rSmart, 135.0, 15.0, StockStatus.IN_STOCK);
                seedPrice(oil, dMart, 138.0, 12.0, StockStatus.IN_STOCK);
                seedPrice(oil, amazonFresh, 145.0, 5.0, StockStatus.IN_STOCK);

                seedPrice(ghee, dMart, 610.0, 40.0, StockStatus.IN_STOCK);
                seedPrice(ghee, rSmart, 625.0, 25.0, StockStatus.IN_STOCK);
                seedPrice(ghee, bigBasket, 650.0, 0.0, StockStatus.IN_STOCK);

                seedPrice(atta, dMart, 245.0, 20.0, StockStatus.IN_STOCK);
                seedPrice(atta, rSmart, 250.0, 15.0, StockStatus.IN_STOCK);
                seedPrice(atta, bigBasket, 265.0, 0.0, StockStatus.IN_STOCK);

                seedPrice(salt, dMart, 24.0, 2.0, StockStatus.IN_STOCK);
                seedPrice(salt, rSmart, 25.0, 1.0, StockStatus.IN_STOCK);
                seedPrice(salt, bigBasket, 28.0, 0.0, StockStatus.IN_STOCK);

                seedPrice(chilli, dMart, 82.0, 8.0, StockStatus.IN_STOCK);
                seedPrice(chilli, rSmart, 85.0, 5.0, StockStatus.IN_STOCK);

                seedPrice(maggi, rSmart, 92.0, 8.0, StockStatus.IN_STOCK);
                seedPrice(maggi, dMart, 94.0, 6.0, StockStatus.IN_STOCK);
                seedPrice(maggi, amazonFresh, 98.0, 2.0, StockStatus.IN_STOCK);

                seedPrice(biscuits, dMart, 115.0, 15.0, StockStatus.IN_STOCK);
                seedPrice(biscuits, rSmart, 120.0, 10.0, StockStatus.IN_STOCK);

                seedPrice(tea, dMart, 290.0, 30.0, StockStatus.IN_STOCK);
                seedPrice(tea, rSmart, 298.0, 20.0, StockStatus.IN_STOCK);
                seedPrice(tea, bigBasket, 320.0, 0.0, StockStatus.IN_STOCK);
            }
        }
    }

    private void seedPrice(Product product, Store store, Double price, Double discount, StockStatus status) {
        Price p = new Price(product, store, price, discount, status);
        priceRepository.save(p);
    }
}
