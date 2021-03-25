package ir.maktab.investment.service;

import ir.maktab.investment.model.RequestSubject;
import ir.maktab.investment.repository.RequestSubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class RequestSubjectService {
    private RequestSubjectRepository requestSubjectRepository;

    @Autowired
    public RequestSubjectService(RequestSubjectRepository requestSubjectRepository) {
        this.requestSubjectRepository = requestSubjectRepository;
    }

    public List<RequestSubject> findAll() {
        return requestSubjectRepository.findAll();
    }

    public RequestSubject findById(Long id) throws Exception {
        Optional<RequestSubject> requestSubject = requestSubjectRepository.findById(id);
        if (requestSubject.isPresent())
            return requestSubject.get();
        throw new Exception();
    }

    public void save(RequestSubject requestSubject) {
        requestSubjectRepository.save(requestSubject);
    }

}
