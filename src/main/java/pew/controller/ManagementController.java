
package pew.controller;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pew.domain.NewsObject;
import pew.repository.AuthorRepository;
import pew.repository.CategoryRepository;
import pew.repository.NewRepository;
import pew.service.ManagementService;

@Controller
public class ManagementController {
    
    @Autowired
    private AuthorRepository autoRepo;
    
    @Autowired
    private CategoryRepository catRepo;
    
    @Autowired
    private NewRepository newRepo;
    
    @Autowired
    private ManagementService mserv;
    
    @Transactional
    @GetMapping("/management")
    public String home(Model model){
        model.addAttribute("news", newRepo.findAllByOrderByDateDesc());
        return "management";
    }
    
    @Transactional
    @GetMapping("/management/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id){
        NewsObject n = newRepo.getOne(id);
        return new ResponseEntity<>(n.getImage(), mserv.getHeaders(n), HttpStatus.CREATED);
    }
    
    @Transactional
    @GetMapping("/management/{id}")
    public String manage(Model model, @PathVariable Long id){
        NewsObject n = newRepo.getOne(id);
        model.addAttribute("newsObject", n);
        model.addAttribute("authors", mserv.getAuthors(n));
        model.addAttribute("categories", mserv.getCategories(n));
        return "manage";
    }
    
    @Transactional
    @PostMapping("/management/{newId}/author")
    public String assignAuthor(@PathVariable Long newId, @RequestParam Long authorId){
        mserv.assignAuthor(newId, authorId);
        return "redirect:/management/"+newId;
    }
    
    @Transactional
    @PostMapping("/management/{newId}/category")
    public String assignCategory(@PathVariable Long newId, @RequestParam Long categoryId){
        mserv.assignCategory(newId, categoryId);
        return "redirect:/management/"+newId;
    }
    
    @Transactional
    @PostMapping("/management/{newId}/category/{categoryId}")
    public String separateCategory(@PathVariable Long newId, @PathVariable Long categoryId){
        mserv.separateCategory(newId, categoryId);
        return "redirect:/management/"+newId;
    }
    
    @Transactional
    @PostMapping("/management/{newId}/author/{authorId}")
    public String separateAuthor(@PathVariable Long newId, @PathVariable Long authorId){
        mserv.separateAuthor(newId, authorId);
        return "redirect:/management/"+newId;
    }
    
    @Transactional
    @DeleteMapping("/management/{id}")
    public String del(@PathVariable Long id){
        mserv.deleteNewsObject(id);
        return "redirect:/management";
    }
}
