
package pew.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pew.domain.NewsObject;
import pew.repository.NewRepository;

@Controller
public class NewsObjectController {
    
    @Autowired
    private NewRepository newRepo;
    
    @GetMapping("/newsobject")
    public String home(Model model){
        return "newsobject";
    }
    
    @PostMapping("/newsobject")
    public String add(@RequestParam String title, @RequestParam String ingress,
             @RequestParam("file") MultipartFile file, @RequestParam String text) throws IOException {
        if(title.trim().isEmpty() || ingress.trim().isEmpty() || text.trim().isEmpty()){
            return "redirect:/management";
        }
        if (file.getContentType().contains("image/")) {
            NewsObject n = new NewsObject();
            n.setTitle(title);
            n.setIngress(ingress);
            n.setImage(file.getBytes());
            n.setContentType(file.getContentType());
            n.setSize(file.getSize());
            n.setText(text);
            newRepo.save(n);
        }
        return "redirect:/management";
    }
}
