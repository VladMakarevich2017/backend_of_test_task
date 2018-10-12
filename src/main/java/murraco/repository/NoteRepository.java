package murraco.repository;

import murraco.model.Note;
import murraco.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    Note findByUserAndId(User user, Long id);
    void removeByUserAndAndId(User user, Long id);
    void removeByType(String type);
    boolean existsByUserAndId(User user, Long id);
}
