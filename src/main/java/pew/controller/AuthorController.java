
package pew.controller;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pew.domain.Author;
import pew.domain.NewsObject;
import pew.repository.AuthorRepository;
import pew.repository.NewRepository;
import pew.service.ManagementService;

@Controller
public class AuthorController {
    
    @Autowired
    private AuthorRepository autoRepo;
    
    @Autowired
    private NewRepository newRepo;
    
    @Autowired
    private ManagementService mserv;
    
    @GetMapping("/author")
    public String home(Model model){
        model.addAttribute("authors", autoRepo.findAll());
        return "author";
    }
    
    @PostMapping("/author")
    public String addAuthor(@RequestParam String name){
        if(name.trim().isEmpty() || autoRepo.findByName(name) != null){
            return "redirect:/author";
        }
        mserv.createAuthor(name);
        return "redirect:/author";
    }
    
    @Transactional
    @DeleteMapping("/author/{authorId}")
    public String deleteAuthor(@PathVariable Long authorId){
        mserv.deleteAuthor(authorId);
        return "redirect:/author";
    }
}
