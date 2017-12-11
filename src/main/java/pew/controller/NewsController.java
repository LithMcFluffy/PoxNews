
package pew.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pew.domain.Category;
import pew.domain.NewsObject;
import pew.repository.AuthorRepository;
import pew.repository.CategoryRepository;
import pew.repository.NewRepository;
import pew.service.NewsService;

@Controller
public class NewsController {
    
    @Autowired
    private AuthorRepository autoRepo;
    
    @Autowired
    private CategoryRepository catRepo;
    
    @Autowired
    private NewRepository newRepo;
    
    @Autowired 
    private NewsService nserv;
    
    @Transactional
    @GetMapping("/news")
    public String home(Model model){
        Pageable pa = PageRequest.of(0, 5, Sort.Direction.DESC, "date");
        model.addAttribute("news", newRepo.findAll(pa));
        model.addAttribute("recentNews", newRepo.findAll(pa));
        model.addAttribute("categories", catRepo.findByActive(1));
        model.addAttribute("popularNews", nserv.getPopularNews());
        return "news";
    }
    
    @Transactional
    @GetMapping("/news/recent")
    public String listByReleaseDate(Model model){
        Pageable pa = PageRequest.of(0, 5, Sort.Direction.DESC, "date");
        model.addAttribute("news", newRepo.findAllByOrderByDateDesc());
        model.addAttribute("recentNews", newRepo.findAll(pa));
        model.addAttribute("categories", catRepo.findByActive(1));
        model.addAttribute("popularNews", nserv.getPopularNews());
        return "news";
    }
    
    @Transactional
    @GetMapping("/news/category/{name}")
    public String listByCategory(Model model, @PathVariable String name){
        Pageable pa = PageRequest.of(0, 5, Sort.Direction.DESC, "date");
        Category cat = catRepo.findByName(name);
        model.addAttribute("news", cat.getNews());
        model.addAttribute("recentNews", newRepo.findAll(pa));
        model.addAttribute("categories", catRepo.findByActive(1));
        model.addAttribute("popularNews", nserv.getPopularNews());
        return "news";
    }
    
    @Transactional
    @GetMapping("/news/popular")
    public String listByPopularity(Model model){
        Pageable pa = PageRequest.of(0, 5, Sort.Direction.DESC, "date");
        model.addAttribute("news", nserv.getAllPopularNews());
        model.addAttribute("recentNews", newRepo.findAll(pa));
        model.addAttribute("categories", catRepo.findByActive(1));
        model.addAttribute("popularNews", nserv.getPopularNews());
        return "news";
    }
    
    @Transactional
    @GetMapping("/news/{id}")
    public String newByid(Model model, @PathVariable Long id){
        NewsObject n = newRepo.getOne(id);
        n.view();
        newRepo.save(n);
        model.addAttribute("newsObject", n);
        Pageable pa = PageRequest.of(0, 5, Sort.Direction.DESC, "date");
        model.addAttribute("recentNews", newRepo.findAll(pa));
        model.addAttribute("categories", catRepo.findByActive(1));
        model.addAttribute("popularNews", nserv.getPopularNews());
        return "new";
    }
    
    @Transactional
    @GetMapping("/news/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id){
        NewsObject n = newRepo.getOne(id);
        return new ResponseEntity<>(n.getImage(), nserv.getHeaders(n), HttpStatus.CREATED);
    }
}
