
package pew.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.jpa.domain.AbstractPersistable;

@AllArgsConstructor
@Data
@Entity
public class NewsObject extends AbstractPersistable<Long>{
    
    private String title;
    private String ingress;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] image;
    private String contentType;
    private Long size;
    
    private String text;
    private LocalDateTime releaseDate;
    
    @ManyToMany
    private List<Author> authors;
    
    @ManyToMany
    private List<Category> categories;
    
    public NewsObject() {
        this.releaseDate = LocalDateTime.now();
    }
    
    public void addAuthor(Author param){
        this.authors.add(param);
    }
    
    public void addCategory(Category param){
        this.categories.add(param);
    }
}
