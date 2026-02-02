package com.devtrack.repository;

import com.devtrack.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByUserUserIdOrderByCreatedAtDesc(Long userId);

    List<Note> findByUserUserIdAndIsPinnedTrueOrderByCreatedAtDesc(Long userId);

    List<Note> findByUserUserIdAndTagsContainingIgnoreCase(Long userId, String tag);
}
