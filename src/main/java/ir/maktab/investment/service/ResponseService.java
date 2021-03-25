package ir.maktab.investment.service;

import ir.maktab.investment.model.Response;
import ir.maktab.investment.repository.ResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResponseService {
    private ResponseRepository responseRepository;

    @Autowired
    public ResponseService(ResponseRepository responseRepository) {
        this.responseRepository = responseRepository;
    }

    public void save(Response response) {
        responseRepository.save(response);
    }

    public Response findById(Long id) throws Exception {
        Optional<Response> response = responseRepository.findById(id);
        if (response.isPresent())
            return response.get();

        throw new Exception();
    }

//    public Response findResponseByEndRequestId(Long id) {
//        return responseRepository.findResponseByEndRequestId(id);
//    }
}
