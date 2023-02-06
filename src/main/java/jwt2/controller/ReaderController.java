package jwt2.controller;

import jwt2.security.PersonDetails;
import jwt2.service.ReaderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//@RestController
@Controller
@RequestMapping("/readers")
public class ReaderController {
    private final ReaderService readerService;
    private final ModelMapper modelMapper;

    @Autowired
    public ReaderController(ReaderService readerService, ModelMapper modelMapper) {
        this.readerService = readerService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public String getAllReaders(Model model){
        model.addAttribute("allReaders", readerService.getAllReaders());
        return "/reader/all_readers";
    }

    @GetMapping("/showinfo")
    @ResponseBody
    public String getHello(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        System.out.println(personDetails.getUsername());

        return personDetails.getUsername();
    }
}