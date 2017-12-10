
package pew.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Transactional
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
    
    @ElementCollection
    @CollectionTable(name = "views")
    @MapKeyColumn(name = "date")
    @Column(name = "amount")
    private Map<String, Long> views;
    
    public NewsObject() {
        this.releaseDate = LocalDateTime.now();
        views = new HashMap<String, Long>();
    }
    
    public void view(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String s = dateFormat.format(date);
        if(views.containsKey(s)) {
            views.put(s, views.get(s)+1);
        }else{
            views.put(s, 1L);
        }
    }
    
    public Long getViews(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return views.get(dateFormat.format(date));
    }
    
    public Long getViewsFromWeek(){
        if(this.views.isEmpty()) return 0L;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        LocalDate today = LocalDate.now();
        Long result = getViews();
        if(this.views.size() < 7){
            for(int i=1; i<this.views.size(); i++){
                result += this.views.get(dateFormat.format(today.minusDays(i)));
            }
        }else{
            for(int i=1; i<7; i++){
                result += this.views.get(dateFormat.format(today.minusDays(i)));
            }
        }
        return result;
    }
    
    public void addAuthor(Author param){
        this.authors.add(param);
    }
    
    public void addCategory(Category param){
        this.categories.add(param);
    }
}
