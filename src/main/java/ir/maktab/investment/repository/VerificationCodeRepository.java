package ir.maktab.investment.repository;

import ir.maktab.investment.model.User;
import ir.maktab.investment.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode,Long> {
}
