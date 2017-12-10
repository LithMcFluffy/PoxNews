
package pew.domain;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Transactional
public class Category extends AbstractPersistable<Long>{
    
    private String name;
    
    @Lob
    @ManyToMany
    private List<NewsObject> news;
    
    public void addNew(NewsObject param){
        this.news.add(param);
    }
}
