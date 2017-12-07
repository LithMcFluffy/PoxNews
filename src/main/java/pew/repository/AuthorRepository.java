
package pew.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pew.domain.Author;

public interface AuthorRepository extends JpaRepository<Author, Long>{
    Author findByName(String name);
}
