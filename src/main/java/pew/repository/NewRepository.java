
package pew.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pew.domain.NewsObject;

public interface NewRepository extends JpaRepository<NewsObject, Long>{
    List<NewsObject> findAllByOrderByDateDesc();
}
