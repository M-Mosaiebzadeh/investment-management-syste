//package ir.maktab.investment;
//
//import ir.maktab.investment.model.User;
//import ir.maktab.investment.model.enums.Role;
//import ir.maktab.investment.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//public class MyCommandLineRunner implements CommandLineRunner {
//    private PasswordEncoder passwordEncoder;
//    private UserRepository userRepository;
//
//    @Autowired
//    public MyCommandLineRunner(PasswordEncoder passwordEncoder, UserRepository userRepository) {
//        this.passwordEncoder = passwordEncoder;
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        User admin = new User();
//        admin.setFirstname("soheil");
//        admin.setLastname("Mz");
//        admin.setPhone("09387078111");
//        admin.setNationalCode("1234567890");
//        admin.setEmail("m123_mosaiebzadeh@yahoo.com");
//
//        admin.setUsername("soheil");
//        admin.setPassword(passwordEncoder.encode("1234567"));
//        admin.setRole(Role.ADMIN);
//        admin.setIsEnabled(true);
//        admin.setIsDeleted(false);
//        userRepository.save(admin);
//
////        User admin2 = new User();
////        admin2.setFirstname("saeed");
////        admin2.setLastname("Mz");
////        admin2.setPhone("09387078333");
////        admin2.setNationalCode("1234567899");
////        admin2.setEmail("mosaiebzadeh@yahoo.com");
////
////        admin2.setUsername("saeed123");
////        admin2.setPassword(passwordEncoder.encode("123456"));
////        admin2.setRole(Role.ADMIN);
//////        responsible.setIsAccountNonExpired(true);
//////        responsible.setIsAccountNonLocked(true);
//////        responsible.setIsCredentialsNonExpired(true);
////        admin2.setIsEnabled(true);
////        userRepository.save(admin2);
//
//        User ali = new User();
//        ali.setFirstname("ali");
//        ali.setLastname("alavi");
//        ali.setPhone("09387078222");
//        ali.setNationalCode("1234567891");
//        ali.setEmail("ali_alavi@yahoo.com");
//
//        ali.setUsername("ali123");
//        ali.setPassword(passwordEncoder.encode("1234567"));
//        ali.setRole(Role.USER);
////        ali.setIsAccountNonExpired(true);
////        ali.setIsAccountNonLocked(true);
////        ali.setIsCredentialsNonExpired(true);
//        ali.setIsEnabled(true);
//        ali.setIsDeleted(false);
//        userRepository.save(ali);
////
////        User responsible = new User();
////        responsible.setFirstname("sara");
////        responsible.setLastname("mohammadi");
////        responsible.setPhone("09387078555");
////        responsible.setNationalCode("1234567895");
////        responsible.setEmail("sara_mohammadi@yahoo.com");
////
////        responsible.setUsername("reza123");
////        responsible.setPassword(passwordEncoder.encode("123456"));
////        responsible.setRole(Role.RESPONSIBLE);
////        responsible.setIsEnabled(true);
////        userRepository.save(responsible);
//    }
//}
