package ir.maktab.investment.repository;

import ir.maktab.investment.model.Request;
import ir.maktab.investment.model.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseRepository extends JpaRepository<Response,Long> {

}
