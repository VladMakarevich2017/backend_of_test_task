package murraco.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Note {
    @Id
    @GeneratedValue
    private long id;

    private String name;

    @Column(columnDefinition="TEXT")
    private String note;

    @Enumerated(EnumType.ORDINAL)
    NoteType type;

    @ManyToMany
    private List<Note> notesInsideOf = new ArrayList<>();

    @ManyToMany
    private List<Note> notesInside = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public void addNoteInside(Note note) {
        notesInside.add(note);
        note.getNotesInsideOf().add(this);
    }

    public void addNoteInsideOf(Note note) {
        notesInsideOf.add(note);
        note.getNotesInside().add(this);
    }

    public NoteType getType() {
        return type;
    }

    public void setType(NoteType type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Note> getNotesInsideOf() {
        return notesInsideOf;
    }

    public void setNotesInsideOf(List<Note> notesInsideOf) {
        this.notesInsideOf = notesInsideOf;
    }

    public List<Note> getNotesInside() {
        return notesInside;
    }

    public void setNotesInside(List<Note> notesInside) {
        this.notesInside = notesInside;
    }

}
