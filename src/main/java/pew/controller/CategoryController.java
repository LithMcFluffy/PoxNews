
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
import pew.service.ManagementService;

@Controller
public class CategoryController {
    
    @Autowired
    private CategoryRepository catRepo;
    
    @Autowired
    private NewRepository newRepo;
    
    @Autowired
    private ManagementService mserv;
    
    @GetMapping("/category")
    public String home(Model model){
        model.addAttribute("categories", catRepo.findAll());
        return "category";
    }
    
    @PostMapping("/category")
    public String addCategory(@RequestParam String name, @RequestParam Integer active){
        if(name.trim().isEmpty() || catRepo.findByName(name) != null){
            return "redirect:/category";
        }
        mserv.createCategory(name, active);
        return "redirect:/category";
    }
    
    @Transactional
    @DeleteMapping("/category/{categoryId}")
    public String deleteCategory(@PathVariable Long categoryId){
        mserv.deleteCategory(categoryId);
        return "redirect:/category";
    }
}
