package com.bunon;

import com.bunon.model.*;
import com.bunon.repository.*;
import com.bunon.service.PassportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final WeaverRepository weaverRepository;
    private final LoomRepository loomRepository;
    private final SubOrderRepository subOrderRepository;
    private final ProductRepository productRepository;
    private final PassportService passportService;
    private final ProductPassportRepository passportRepository;
    private final CraftSealRepository sealRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, WeaverRepository weaverRepository, LoomRepository loomRepository,
                      SubOrderRepository subOrderRepository, ProductRepository productRepository,
                      PassportService passportService, ProductPassportRepository passportRepository,
                      CraftSealRepository sealRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.weaverRepository = weaverRepository;
        this.loomRepository = loomRepository;
        this.subOrderRepository = subOrderRepository;
        this.productRepository = productRepository;
        this.passportService = passportService;
        this.passportRepository = passportRepository;
        this.sealRepository = sealRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            createAccountsAndShop();
        }
        giveEveryoneAUsername();
        addDemoSeal();
    }

    private void createAccountsAndShop() {
        String hash = passwordEncoder.encode("bunon123");

        userRepository.save(new User("Bunon Admin", "admin@bunon.test", "01700000001", hash, Role.ADMIN));
        User customer = new User("Nadia Customer", "customer@bunon.test", "01700000002", hash, Role.CUSTOMER);
        customer.setDeliveryAddress("House 12, Road 4, Dhanmondi, Dhaka");
        userRepository.save(customer);
        User weaverUser = userRepository.save(new User("Rahima Begum", "weaver@bunon.test", "01700000003", hash, Role.WEAVER));
        User coopUser = userRepository.save(new User("Karim Uddin", "coop@bunon.test", "01700000004", hash, Role.COOPERATIVE_MEMBER));

        // If you ran the very first version, the demo weaver already exists: just connect it to the new login account.
        Weaver existing = weaverRepository.findAll().stream().findFirst().orElse(null);
        weaverRepository.save(new Weaver("Karim Uddin", "Sirajganj", "Khadi and lungi", "Cooperative member.", coopUser));
        if (existing != null) {
            existing.setUser(weaverUser);
            weaverRepository.save(existing);
            return;
        }

        Weaver rahima = new Weaver("Rahima Begum", "Tangail", "Jamdani and saree",
                "Third-generation weaver from Tangail working on a pit loom.", weaverUser);
        rahima.setShopAddress("Baju Bazar Road, Tangail Sadar");
        rahima = weaverRepository.save(rahima);

        Loom pitLoom = loomRepository.save(new Loom("Pit loom", 40, "ACTIVE", rahima));
        Loom frameLoom = loomRepository.save(new Loom("Frame loom", 30, "ACTIVE", rahima));
        subOrderRepository.save(new SubOrder("Wedding saree order", 60, pitLoom));
        subOrderRepository.save(new SubOrder("Gamcha batch", 20, frameLoom));

        Product saree = product("Jamdani Saree, Peacock Border", "Saree", "Jamdani", "Cotton and muslin", 8500, 2, false, null, rahima, pitLoom);
        product("Nakshi Kantha Throw", "Kantha", "Kantha stitch", "Recycled cotton", 3200, 5, false, null, rahima, frameLoom);
        product("Indigo Khadi Panjabi", "Panjabi", "Khadi", "Handspun cotton", 2400, 0, true, LocalDate.now().plusDays(14), rahima, pitLoom);
        product("Rajshahi Silk Scarf", "Scarf", "Silk", "Mulberry silk", 1800, 7, false, null, rahima, frameLoom);

        ProductPassport passport = passportRepository.findByProductId(saree.getId()).orElseThrow();
        passportService.addEntry(passport, "YARN_DYED", "Cotton yarn dyed with natural indigo");
        passportService.addEntry(passport, "WOVEN", "Woven over 12 days on the pit loom");
        passportService.addEntry(passport, "FINISHED", "Finishing and washing completed");
    }

    private void giveEveryoneAUsername() {
        for (User user : userRepository.findAll()) {
            if (user.getUsername() == null || user.getUsername().isBlank()) {
                String base = user.getEmail().split("@")[0].toLowerCase().replaceAll("[^a-z0-9_]", "");
                if (base.length() < 3) {
                    base = base + "user";
                }
                String candidate = base;
                int number = 1;
                while (userRepository.existsByUsername(candidate)) {
                    candidate = base + number++;
                }
                user.setUsername(candidate);
                userRepository.save(user);
            }
        }
    }

    private void addDemoSeal() {
        if (sealRepository.count() == 0) {
            weaverRepository.findByUserEmail("weaver@bunon.test").ifPresent(weaver -> {
                String evidence = "Workshop visit and NID check passed (demo data)";
                sealRepository.save(new CraftSeal("Gold", evidence, LocalDate.now().minusDays(30), weaver, null));
                productRepository.findByWeaverId(weaver.getId()).stream().findFirst().ifPresent(product ->
                        sealRepository.save(new CraftSeal("Gold", "Pattern and loom verified (demo data)",
                                LocalDate.now().minusDays(20), weaver, product)));
            });
        }
    }

    private Product product(String title, String category, String weave, String material, double price,
                            int stock, boolean preorder, LocalDate expected, Weaver weaver, Loom loom) {
        Product p = new Product();
        p.setTitle(title);
        p.setCategory(category);
        p.setWeaveType(weave);
        p.setMaterial(material);
        p.setPrice(price);
        p.updateAvailability(stock, expected, preorder);
        p.setWeaver(weaver);
        p.setLoom(loom);
        p = productRepository.save(p);
        passportService.createFor(p);
        return p;
    }
}
