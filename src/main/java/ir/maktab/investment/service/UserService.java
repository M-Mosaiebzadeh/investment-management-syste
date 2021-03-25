package ir.maktab.investment.service;

import ir.maktab.investment.model.User;
import ir.maktab.investment.model.VerificationCode;
import ir.maktab.investment.model.enums.Role;
import ir.maktab.investment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private VerificationCodeService verificationCodeService;
//    private AuthenticationProvider authenticationProvider;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       VerificationCodeService verificationCodeService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationCodeService = verificationCodeService;
//        this.authenticationProvider = authenticationProvider;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isPresent())
            return user.get();
        throw new UsernameNotFoundException(String.format("%s not found", username));
    }

    public Boolean saveUserFirstTime(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setIsEnabled(false);
        user.setIsDeleted(false);
        user.setVerificationCode(verificationCodeService.createVerificationCode(new VerificationCode()));
        this.userRepository.save(user);

        return true;
    }

    public String saveResponsibleFirstTime(User user) {
        String password = verificationCodeService.createPasswordCode(user);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.RESPONSIBLE);
        user.setIsEnabled(true);
        user.setIsDeleted(false);
        this.userRepository.save(user);
        return password;
    }


    public Boolean saveUser(User user) {
        userRepository.save(user);
        return true;
    }

    public User editUser(User user, Authentication authentication) {
        //TODO az ostad sefidmoy beporsam shayad beshe role ro ham toye .html ezafe kard
        User oldUser = (User) authentication.getPrincipal();
        user.setRole(oldUser.getRole());

        return user;
    }


    public Boolean saveAdmin(User admin) {
        userRepository.save(admin);
        return true;
    }

    public User editAdmin(User user, Authentication authentication) {
        //TODO az ostad sefidmoy beporsam shayad beshe role ro ham toye .html ezafe kard
        User oldUser = (User) authentication.getPrincipal();
        user.setRole(oldUser.getRole());
        return user;
    }

    public Boolean saveResponsible(User responsible) {
        userRepository.save(responsible);
        return true;
    }

    public User editResponsible(User responsible, Authentication authentication) {
        //TODO az ostad sefidmoy beporsam shayad beshe role ro ham toye .html ezafe kard
        User oldUser = (User) authentication.getPrincipal();
        responsible.setRole(oldUser.getRole());

        return responsible;
    }


    public List<User> loadAllUserWithRole(Role role) {
        return this.userRepository.findUsersByRole(role);
    }

    public User loadById(Long id) {
        Optional<User> user = userRepository.findUsersById(id);
        if (user.isPresent())
            return user.get();
        throw new IllegalArgumentException("User with id is not exist");
    }

    public User loadUserByVerificationCode(String verificationCode) {
        return userRepository.findUsersByVerificationCodeCode(verificationCode);
    }

    public Map<Boolean,String> verifyRegistrationAccount(String verificationCode) {
        User user = userRepository.findUsersByVerificationCodeCode(verificationCode);

        Map<Boolean,String> verificationResult = new HashMap<>();
        if (user == null) {
            verificationResult.put(false,"Code is wrong, make new try...");
        }else if (user.isEnabled()) {
            verificationResult.put(false,"your account is enable now, please sign in");
        }else if (user.getVerificationCode().getExpireTime().before(new Date())) {
            userRepository.delete(user);
            verificationCodeService.delete(user.getVerificationCode());
            verificationResult.put(false,
                    "verification code is expired, register again to receive a new code");
        }else {
            user.setIsEnabled(true);
            userRepository.save(user);
            verificationResult.put(true,"your account has been activated, please sign in");
        }

        return verificationResult;
    }

    public User loadUserByEmail(String email) {
        return userRepository.findUsersByEmail(email);
    }

    public void verifyAccountForForgetPassword(User user) {
        VerificationCode verificationCode = verificationCodeService.createVerificationCode(user.getVerificationCode());
        user.setVerificationCode(verificationCode);
        saveUser(user);
    }

    public void changePassword(User user,String password) {
        user.setPassword(passwordEncoder.encode(password));
        saveUser(user);
    }

    public Boolean isPasswordsEqual(User user, String oldPassword) {

        return passwordEncoder.matches(oldPassword,user.getPassword());
    }

    public User checkUserExistForForgetPassword(String fieldType, String fieldValue) {
        switch (fieldType) {
            case "email":
                return userRepository.findUsersByEmail(fieldValue);
            case "username":
                return userRepository.findUserByUsername(fieldValue).get();
            case "national-code":
                return userRepository.findUsersByNationalCode(fieldValue);
            default:
                return null;
        }
    }

//    public void handyLogin(String username, String password) {
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(username,password);
//
//        Authentication authenticate = authenticationProvider.authenticate(authenticationToken);
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        securityContext.setAuthentication(authenticate);
//    }
}
