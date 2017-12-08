
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
import pew.domain.Category;
import pew.domain.NewsObject;
import pew.repository.CategoryRepository;
import pew.repository.NewRepository;

@Controller
public class CategoryController {
    
    @Autowired
    private CategoryRepository catRepo;
    
    @Autowired
    private NewRepository newRepo;
    
    @GetMapping("/category")
    public String home(Model model){
        model.addAttribute("categories", catRepo.findAll());
        return "category";
    }
    
    @PostMapping("/category")
    public String addCategory(@RequestParam String name){
        if(name.trim().isEmpty() || catRepo.findByName(name) != null){
            return "redirect:/category";
        }
        Category cat = new Category();
        cat.setName(name);
        catRepo.save(cat);
        return "redirect:/category";
    }
    
    @Transactional
    @DeleteMapping("/category/{categoryId}")
    public String deleteCategory(@PathVariable Long categoryId){
        Category cat = catRepo.getOne(categoryId);
        for(NewsObject n : cat.getNews()){
            n.getCategories().remove(cat);
            newRepo.save(n);
        }
        catRepo.deleteById(categoryId);
        return "redirect:/category";
    }
}
