package ir.maktab.investment.controller;

import ir.maktab.investment.model.*;
import ir.maktab.investment.model.enums.RequestStatus;
import ir.maktab.investment.repository.DocumentFileRepository;
import ir.maktab.investment.service.RequestService;
import ir.maktab.investment.service.RequestSubjectService;
import ir.maktab.investment.service.ResponseService;
import ir.maktab.investment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user/request")
@PreAuthorize("hasRole('USER')")
public class RequestController {
    private DocumentFileRepository documentFileRepository;
    private RequestService requestService;
    private RequestSubjectService requestSubjectService;
    private UserService userService;
    private ResponseService responseService;

    @Autowired
    public RequestController(DocumentFileRepository documentFileRepository,
                             RequestService requestService,
                             RequestSubjectService requestSubjectService,
                             UserService userService,
                             ResponseService responseService) {

        this.documentFileRepository = documentFileRepository;
        this.requestService = requestService;
        this.requestSubjectService = requestSubjectService;
        this.userService = userService;
        this.responseService = responseService;
    }

    @GetMapping("/new-request")
    public String makeRequest(Model model,
                              Authentication authentication
    ) {

        List<RequestSubject> requestSubjects = requestSubjectService.findAll();
        User user = (User) userService.loadUserByUsername(authentication.getName());
        Request request = new Request();
        request.setUser(user);
        request.setStatus(RequestStatus.IN_PROGRESS);
        model.addAttribute("request", request);
        model.addAttribute("requestSubjects", requestSubjects);

        return "/request/request-form";
    }

    @PostMapping("/make-request")
    public String uploadFile(@Valid Request request, BindingResult result,
                             @RequestParam("document") MultipartFile[] files,
                             RedirectAttributes redirectAttributes) throws Exception {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Your request has encountered an error");
            return "redirect:/user/home";
        }
        if (request.getId() == null) {
            RequestSubject requestSubject = requestSubjectService.findById(request.getSubject().getId());
            request.setSubject(requestSubject);
            redirectAttributes.addFlashAttribute("success",
                    "Your request has been successfully submitted");

        } else {
            Set<DocumentFile> oldFiles = requestService.findById(request.getId()).getFiles();
            if (oldFiles.size() != 0)
                oldFiles
                        .stream()
                        .forEach(documentFile -> documentFileRepository.delete(documentFile));

            redirectAttributes.addFlashAttribute("success",
                    "Your request edited successfully");

        }

        request.setDate(new Date());
        requestService.save(request);

        if (!(files[0].getName().equals("") || files[0].getSize() == 0)) {
            Set<DocumentFile> documentFiles = Arrays.stream(files)
                    .map(file -> {
                        DocumentFile document = new DocumentFile();
                        document.setName(StringUtils.cleanPath(file.getOriginalFilename()));
                        // TODO throw IO Exception But dont accept method throws that and need try/catch??
                        try {
                            document.setContent(file.getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        document.setSize(file.getSize());
                        document.setUploadDate(new Date());
                        document.setRequest(request);
                        return document;
                    })
                    .peek(documentFile -> documentFileRepository.save(documentFile))
                    .collect(Collectors.toSet());
        }


        return "redirect:/user/home";
    }

    @GetMapping("/show/all-request")
    public String showAllRequest(Model model, Authentication authentication) {
        List<Request> requests = requestService.findAllRequestByUsernameOfUser(authentication.getName());
        model.addAttribute("requests", requests);
        return "/user/user-request-list";
    }

    @PostMapping("/show/request")
    public String showFullDetailsRequest(@RequestParam("request-id") Long requestId,
                                         Model model) throws Exception {

        Request request = requestService.findById(requestId);
        Response response = request.getResponse();

        model.addAttribute("request", request);
        model.addAttribute("response", response);

        return "/user/user-request";
    }


    @PreAuthorize("hasAnyRole('RESPONSIBLE','USER','ADMIN')")
    @PostMapping("/download")
    public void downloadFile(@RequestParam("file-id") Long id, HttpServletResponse response) throws IOException {
        Optional<DocumentFile> documentFile = documentFileRepository.findById(id);
        if (documentFile.isEmpty()) {
            throw new FileNotFoundException(String.format("File with Id:%s not find", id));
        }
        DocumentFile file = documentFile.get();

        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + file.getName();
        response.setHeader(headerKey, headerValue);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(file.getContent());
        outputStream.close();

    }

    @PostMapping("/close-request")
    public String closeRequest(@RequestParam("request-id") Long requestId) throws Exception {
        Request request = requestService.findById(requestId);
        request.setStatus(RequestStatus.CLOSED);
        requestService.save(request);

        return "redirect:/user/request/show/all-request";
    }

    @PostMapping("/edit-request")
    public String editRequest(@RequestParam("request-id") Long requestId,
                              Model model) throws Exception {
        Request request = requestService.findById(requestId);
        List<RequestSubject> requestSubjects = requestSubjectService.findAll();
        model.addAttribute("request", request);
        model.addAttribute("requestSubjects", requestSubjects);

        return "/request/edit-request-form";
    }
}
