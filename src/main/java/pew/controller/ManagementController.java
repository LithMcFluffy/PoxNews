
package pew.controller;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import pew.repository.CategoryRepository;
import pew.repository.NewRepository;

@Controller
public class ManagementController {
    
    @Autowired
    private AuthorRepository autoRepo;
    
    @Autowired
    private CategoryRepository catRepo;
    
    @Autowired
    private NewRepository newRepo;
    
    @GetMapping("/management")
    public String home(Model model){
        model.addAttribute("news", newRepo.findAll());
        return "management";
    }
    
    @Transactional
    @GetMapping("/management/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id){
        NewsObject n = newRepo.getOne(id);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(n.getContentType()));
        headers.setContentLength(n.getSize());

        return new ResponseEntity<>(n.getImage(), headers, HttpStatus.CREATED);
    }
    
    @Transactional
    @GetMapping("/management/{id}")
    public String manage(Model model, @PathVariable Long id){
        NewsObject n = newRepo.getOne(id);
        model.addAttribute("newsObject", n);
 
        List<Author> authors = autoRepo.findAll();
        List<Category> categories = catRepo.findAll();
        for(Author a : n.getAuthors()){
            if (authors.contains(a)) authors.remove(a);
        }
        for(Category c : n.getCategories()) {
            if (categories.contains(c)) categories.remove(c);
        }
        
        model.addAttribute("authors", authors);
        model.addAttribute("categories", categories);
        return "manage";
    }
    
    @Transactional
    @PostMapping("/management/{newId}/author")
    public String assignAuthor(@PathVariable Long newId, @RequestParam Long authorId){
        NewsObject n = newRepo.getOne(newId);
        Author a = autoRepo.getOne(authorId);
        n.addAuthor(a);
        a.addNew(n);
        newRepo.save(n);
        autoRepo.save(a);
        return "redirect:/management/"+newId;
    }
    
    @Transactional
    @PostMapping("/management/{newId}/category")
    public String assignCategory(@PathVariable Long newId, @RequestParam Long categoryId){
        NewsObject n = newRepo.getOne(newId);
        Category c = catRepo.getOne(categoryId);
        n.addCategory(c);
        c.addNew(n);
        newRepo.save(n);
        catRepo.save(c);
        return "redirect:/management/"+newId;
    }
    
    @Transactional
    @DeleteMapping("/management/{id}")
    public String del(@PathVariable Long id){
        NewsObject n = newRepo.getOne(id);
        for(Category c : n.getCategories()){
            c.getNews().remove(n);
            catRepo.save(c);
        }
        for(Author a : n.getAuthors()){
            a.getNews().remove(n);
            autoRepo.save(a);
        }
        newRepo.deleteById(id);
        return "redirect:/management";
    }
}
