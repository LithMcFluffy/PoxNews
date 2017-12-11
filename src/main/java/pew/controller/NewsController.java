
package pew.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
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
    
    @PostConstruct
    public void testData(){
        Category cat1 = new Category();
        Category cat2 = new Category();
        Category cat3 = new Category();
        Category cat4 = new Category();
        Category cat5 = new Category();
        
        cat1.setName("Urheilu");
        cat2.setName("Kotimaa");
        cat3.setName("Ulkomaa");
        cat4.setName("Politiikka");
        cat5.setName("Esports");
        
        cat1.setActive(1);
        cat2.setActive(0);
        cat3.setActive(0);
        cat4.setActive(1);
        cat5.setActive(1);
        
        catRepo.save(cat1);
        catRepo.save(cat2);
        catRepo.save(cat3);
        catRepo.save(cat4);
        catRepo.save(cat5);
    }
    
    @Transactional
    @GetMapping("/news")
    public String home(Model model){
        Pageable pa = PageRequest.of(0, 5, Sort.Direction.DESC, "date");
        model.addAttribute("news", newRepo.findAll(pa));
        model.addAttribute("recentNews", newRepo.findAll(pa));
        model.addAttribute("categories", catRepo.findByActive(1));
        
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
        if(news.size() > 5){
            model.addAttribute("popularNews", news.subList(0, 5));
        }else{
            model.addAttribute("popularNews", news);
        }
        return "news";
    }
    
    @Transactional
    @GetMapping("/news/recent")
    public String listByReleaseDate(Model model){
        model.addAttribute("news", newRepo.findAllByOrderByDateDesc());
        Pageable pa = PageRequest.of(0, 5, Sort.Direction.DESC, "date");
        model.addAttribute("recentNews", newRepo.findAll(pa));
        model.addAttribute("categories", catRepo.findByActive(1));
        
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
        if(news.size() > 5){
            model.addAttribute("popularNews", news.subList(0, 5));
        }else{
            model.addAttribute("popularNews", news);
        }
        return "news";
    }
    
    @Transactional
    @GetMapping("/news/category/{name}")
    public String listByCategory(Model model, @PathVariable String name){
        Category cat = catRepo.findByName(name);
        /*List<NewsObject> news = new ArrayList<>();
        for(NewsObject n : newRepo.findAll()){
            if(n.getCategories().contains(cat)){
                news.add(n);
            }
        }*/
        model.addAttribute("news", cat.getNews());
        Pageable pa = PageRequest.of(0, 5, Sort.Direction.DESC, "date");
        model.addAttribute("recentNews", newRepo.findAll(pa));
        model.addAttribute("categories", catRepo.findByActive(1));
        
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
        if(news.size() > 5){
            model.addAttribute("popularNews", news.subList(0, 5));
        }else{
            model.addAttribute("popularNews", news);
        }
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
        
        Pageable pa = PageRequest.of(0, 5, Sort.Direction.DESC, "date");
        model.addAttribute("recentNews", newRepo.findAll(pa));
        model.addAttribute("categories", catRepo.findByActive(1));
        if(news.size() > 5){
            model.addAttribute("popularNews", news.subList(0, 5));
        }else{
            model.addAttribute("popularNews", news);
        }
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
        if(news.size() > 5){
            model.addAttribute("popularNews", news.subList(0, 5));
        }else{
            model.addAttribute("popularNews", news);
        }
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
