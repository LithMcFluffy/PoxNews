
package pew.service;

import java.io.IOException;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pew.domain.Author;
import pew.domain.Category;
import pew.domain.NewsObject;
import pew.repository.AuthorRepository;
import pew.repository.CategoryRepository;
import pew.repository.NewRepository;

@Service
public class ManagementService {
    
    @Autowired
    private AuthorRepository autoRepo;
    
    @Autowired
    private CategoryRepository catRepo;
    
    @Autowired
    private NewRepository newRepo;
    
    @Transactional
    public void createNewsObject(String title, String ingress, MultipartFile file, String text) throws IOException {
        NewsObject n = new NewsObject();
        n.setTitle(title);
        n.setIngress(ingress);
        n.setImage(file.getBytes());
        n.setContentType(file.getContentType());
        n.setSize(file.getSize());
        n.setText(text);
        newRepo.save(n);
    }
    
    @Transactional
    public void createAuthor(String name){
        Author auto = new Author();
        auto.setName(name);
        autoRepo.save(auto);
    }
    
    @Transactional
    public void createCategory(String name, Integer active){
        Category cat = new Category();
        cat.setName(name);
        cat.setActive(active);
        catRepo.save(cat);
    }
    
    @Transactional
    public void assignAuthor(Long newId, Long authorId){
        NewsObject n = newRepo.getOne(newId);
        Author a = autoRepo.getOne(authorId);
        n.addAuthor(a);
        a.addNew(n);
        newRepo.save(n);
        autoRepo.save(a);
    }
    
    @Transactional
    public void assignCategory(Long newId, Long categoryId){
        NewsObject n = newRepo.getOne(newId);
        Category c = catRepo.getOne(categoryId);
        n.addCategory(c);
        c.addNew(n);
        newRepo.save(n);
        catRepo.save(c);
    }
    
    @Transactional
    public void separateAuthor(Long newId, Long authorId){
        NewsObject n = newRepo.getOne(newId);
        Author a = autoRepo.getOne(authorId);
        n.getAuthors().remove(a);
        a.getNews().remove(n);
        newRepo.save(n);
        autoRepo.save(a);
    }
    
    @Transactional
    public void separateCategory(Long newId, Long categoryId){
        NewsObject n = newRepo.getOne(newId);
        Category c = catRepo.getOne(categoryId);
        n.getCategories().remove(c);
        c.getNews().remove(n);
        newRepo.save(n);
        catRepo.save(c);
    }
    
    @Transactional
    public void deleteNewsObject(Long id){
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
    }
    
    @Transactional
    public void deleteAuthor(Long authorId){
        Author auto = autoRepo.getOne(authorId);
        for(NewsObject n : auto.getNews()){
            n.getAuthors().remove(auto);
            newRepo.save(n);
        }
        autoRepo.deleteById(authorId);
    }
    
    @Transactional
    public void deleteCategory(Long categoryId){
        Category cat = catRepo.getOne(categoryId);
        for(NewsObject n : cat.getNews()){
            n.getCategories().remove(cat);
            newRepo.save(n);
        }
        catRepo.deleteById(categoryId);
    }
    
    @Transactional
    public List<Author> getAuthors(NewsObject n){
        List<Author> authors = autoRepo.findAll();
        for(Author a : n.getAuthors()){
            if (authors.contains(a)) authors.remove(a);
        }
        return authors;
    }
    
    @Transactional
    public List<Category> getCategories(NewsObject n){
        List<Category> categories = catRepo.findAll();
        for(Category c : n.getCategories()) {
            if (categories.contains(c)) categories.remove(c);
        }
        return categories;
    }
    
    @Transactional
    public HttpHeaders getHeaders(NewsObject n){
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(n.getContentType()));
        headers.setContentLength(n.getSize());
        return headers;
    }
}
