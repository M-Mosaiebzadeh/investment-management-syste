package ir.maktab.investment.repository;

import ir.maktab.investment.model.Request;
import ir.maktab.investment.model.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request,Long> {


    List<Request> findAllByUserUsernameOrderByDateDesc(String username);

    List<Request> findAllByStatusOrderByDate(RequestStatus status);

    List<Request> findAllByOrderByDateDesc();

    Request findRequestByResponseId(Long responseId);
}

