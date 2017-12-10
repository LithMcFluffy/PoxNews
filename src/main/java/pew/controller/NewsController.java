
package pew.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pew.domain.Author;
import pew.domain.Category;
import pew.domain.NewsObject;
import pew.repository.AuthorRepository;
import pew.repository.CategoryRepository;
import pew.repository.NewRepository;

@Controller
public class NewsController {
    
    @Autowired
    private AuthorRepository autoRepo;
    
    @Autowired
    private CategoryRepository catRepo;
    
    @Autowired
    private NewRepository newRepo;
    
    @GetMapping("/news")
    public String home(Model model){
        model.addAttribute("news", newRepo.findAll());
        return "news";
    }
    
    @Transactional
    @GetMapping("/news/{id}")
    public String manage(Model model, @PathVariable Long id){
        NewsObject n = newRepo.getOne(id);
        n.view();
        newRepo.save(n);
        model.addAttribute("newsObject", n);
        return "new";
    }
    
    @Transactional
    @GetMapping("/news/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id){
        NewsObject n = newRepo.getOne(id);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(n.getContentType()));
        headers.setContentLength(n.getSize());
        
        return new ResponseEntity<>(n.getImage(), headers, HttpStatus.CREATED);
    }
}
