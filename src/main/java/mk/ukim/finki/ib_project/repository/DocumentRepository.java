package mk.ukim.finki.ib_project.repository;

import mk.ukim.finki.ib_project.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document,Long> {
}
