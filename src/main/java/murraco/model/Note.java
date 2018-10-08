package murraco.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

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
}
