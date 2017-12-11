
package pew.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pew.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
    Category findByName(String name);
    List<Category> findByActive(Integer active);
}
