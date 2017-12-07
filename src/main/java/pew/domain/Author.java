
package pew.domain;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Author extends AbstractPersistable<Long>{
    
    private String name;
    
    @ManyToMany
    private List<NewsObject> news;
    
    public void addNew(NewsObject param){
        this.news.add(param);
    }
}
