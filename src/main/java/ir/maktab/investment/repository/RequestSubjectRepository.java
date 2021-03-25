package ir.maktab.investment.repository;

import ir.maktab.investment.model.RequestSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestSubjectRepository extends JpaRepository<RequestSubject,Long> {
}
