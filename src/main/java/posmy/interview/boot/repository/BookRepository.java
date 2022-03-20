package posmy.interview.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import posmy.interview.boot.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}
