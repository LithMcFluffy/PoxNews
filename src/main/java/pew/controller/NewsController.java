
package pew.controller;

import java.util.ArrayList;
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

@Controller
public class NewsController {
    
    @Autowired
    private AuthorRepository autoRepo;
    
    @Autowired
    private CategoryRepository catRepo;
    
    @Autowired
    private NewRepository newRepo;
    
    @Transactional
    @GetMapping("/news")
    public String home(Model model){
        Pageable pa = PageRequest.of(0, 5, Sort.Direction.DESC, "date");
        model.addAttribute("news", newRepo.findAll(pa));
        model.addAttribute("categories", catRepo.findAll());
        return "news";
    }
    
    @Transactional
    @GetMapping("/news/recent")
    public String sortByReleaseDate(Model model){
        model.addAttribute("news", newRepo.findAllByOrderByDateDesc());
        return "news";
    }
    
    @Transactional
    @GetMapping("/news/category/{name}")
    public String listByCategory(Model model, @PathVariable String name){
        Category cat = catRepo.findByName(name);
        List<NewsObject> news = new ArrayList<>();
        for(NewsObject n : newRepo.findAll()){
            if(n.getCategories().contains(cat)){
                news.add(n);
            }
        }
        model.addAttribute("news", cat.getNews());
        model.addAttribute("categories", catRepo.findAll());
        
        /*
        Category cat = catRepo.findByName(name);
        model.addAttribute("news", cat.getNews());
        model.addAttribute("categories", catRepo.findAll());
        */
        return "news";
    }
    
    @Transactional
    @GetMapping("/news/popular")
    public String listByPopularity(Model model){
        List<NewsObject> news = newRepo.findAll();
        Collections.sort(news,
                 new Comparator<NewsObject>()
                 {
                     public int compare(NewsObject o2,
                                        NewsObject o1)
                     {
                         if (o1.getWeeklyViews()==
                                 o2.getWeeklyViews())
                         {
                             return 0;
                         }
                         else if (o1.getWeeklyViews() <
                                      o2.getWeeklyViews())
                         {
                             return -1;
                         }
                         return 1;
                     }
                 });
        model.addAttribute("news", news);
        model.addAttribute("categories", catRepo.findAll());
        return "news";
    }
    
    @Transactional
    @GetMapping("/news/{id}")
    public String newByid(Model model, @PathVariable Long id){
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
