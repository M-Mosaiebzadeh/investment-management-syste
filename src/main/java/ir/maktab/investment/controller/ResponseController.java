package ir.maktab.investment.controller;

import ir.maktab.investment.model.DocumentFile;
import ir.maktab.investment.model.Request;
import ir.maktab.investment.model.Response;
import ir.maktab.investment.model.User;
import ir.maktab.investment.model.enums.RequestStatus;
import ir.maktab.investment.repository.DocumentFileRepository;
import ir.maktab.investment.service.RequestService;
import ir.maktab.investment.service.ResponseService;
import ir.maktab.investment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/responsible/response")
public class ResponseController {
    private ResponseService responseService;
    private RequestService requestService;
    private UserService userService;

    @Autowired
    public ResponseController(ResponseService responseService,
                              RequestService requestService,
                              UserService userService) {
        this.responseService = responseService;
        this.requestService = requestService;
        this.userService = userService;
    }

    @GetMapping("/show/all-request")
    public String showAllInProgressRequest(Model model) {
        List<Request> requests = requestService.findAllByOrderByDateDesc();
        model.addAttribute("requests",requests);
        return "/responsible/user-request-list";
    }

    @PostMapping("/show/request")
    public String showFullDetailsRequest(@RequestParam("request-id") Long requestId,
                                         Model model) throws Exception {

        Request request = requestService.findById(requestId);
        Response response = request.getResponse();

        model.addAttribute("request", request);
        model.addAttribute("response", response);

        return "/responsible/responsible-request";
    }


    @PostMapping ("/answer")
    public String answer(@RequestParam("request-id") Long id, Model model,
                         Authentication authentication,
                         Response response) {
        User responsible = (User) userService.loadUserByUsername(authentication.getName());
        response.setResponsible(responsible);
        model.addAttribute("response",response);
        model.addAttribute("requestId",id);
        return "/response/response-form";
    }


    @PostMapping("/user-information")
    public String userInformation(@RequestParam("user-id") Long id, Model model) {

        User user = userService.loadById(id);
        model.addAttribute("user",user);
        return "/responsible/user-information-and-requests";
    }

    @PostMapping("/save-response")
    public String saveResponse(Response response,
                               @RequestParam("requestId") Long requestId,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) throws Exception {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error","There was a problem registering the answer\n" +
                    "Check the status before answering again");
            return "redirect:/responsible/home";
        }


        response.setDate(new Date());
        responseService.save(response);

        Request request = requestService.findById(requestId);
        request.setStatus(RequestStatus.ANSWERED);
        request.setResponse(response);
        requestService.save(request);

        redirectAttributes.addFlashAttribute("success","Your answer has been successfully submitted");

        return "redirect:/responsible/home";

    }
}
