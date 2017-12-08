
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
import pew.domain.Category;
import pew.domain.NewsObject;
import pew.repository.AuthorRepository;
import pew.repository.NewRepository;

@Controller
public class AuthorController {
    
    @Autowired
    private AuthorRepository autoRepo;
    
    @Autowired
    private NewRepository newRepo;
    
    @GetMapping("/author")
    public String home(Model model){
        model.addAttribute("authors", autoRepo.findAll());
        return "author";
    }
    
    @PostMapping("/author")
    public String addCategory(@RequestParam String name){
        if(name.trim().isEmpty() || autoRepo.findByName(name) != null){
            return "redirect:/author";
        }
        Author auto = new Author();
        auto.setName(name);
        autoRepo.save(auto);
        return "redirect:/author";
    }
    
    @Transactional
    @DeleteMapping("/author/{authorId}")
    public String deleteCategory(@PathVariable Long authorId){
        Author auto = autoRepo.getOne(authorId);
        for(NewsObject n : auto.getNews()){
            n.getAuthors().remove(auto);
            newRepo.save(n);
        }
        autoRepo.deleteById(authorId);
        return "redirect:/author";
    }
}
