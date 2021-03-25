package ir.maktab.investment.service;

import ir.maktab.investment.model.Request;
import ir.maktab.investment.model.enums.RequestStatus;
import ir.maktab.investment.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestService {
    private RequestRepository requestRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public void save(Request request) {
        requestRepository.save(request);
    }

    public List<Request> findAllRequestByUsernameOfUser(String username) {
        return requestRepository.findAllByUserUsernameOrderByDateDesc(username);
    }

    public List<Request> findAllByStatusOrderByRequestDate(RequestStatus status) {
        return requestRepository.findAllByStatusOrderByDate(status);
    }

    public List<Request> findAllByOrderByDateDesc() {
        return requestRepository.findAllByOrderByDateDesc();
    }

    public Request findById(Long id) throws Exception {
        Optional<Request> request = requestRepository.findById(id);
        if (request.isPresent())
            return request.get();
        throw new Exception();
    }

    public Request findRequestByResponseId(Long responseId) {
        return requestRepository.findRequestByResponseId(responseId);
    }
}
