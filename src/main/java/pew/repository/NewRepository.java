
package pew.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pew.domain.NewsObject;

public interface NewRepository extends JpaRepository<NewsObject, Long>{
    
}
