package ir.maktab.investment.repository;

import ir.maktab.investment.model.User;
import ir.maktab.investment.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findUserByUsername(String username);

    List<User> findUsersByRole(Role role);

    Optional<User> findUsersById(Long id);

    User findUsersByVerificationCodeCode(String verificationCode);

    User findUsersByEmail(String email);

    User findUsersByNationalCode(String nationalCode);
}
