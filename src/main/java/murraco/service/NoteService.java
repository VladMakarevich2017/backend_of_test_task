package murraco.service;

import murraco.model.Note;
import murraco.model.NoteType;
import murraco.model.User;
import murraco.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserService userService;

    public Note createNote(User user, String type) {
        Note note = new Note();
        note.setType(defineNoteType(type));
        note.setName("New note");
        user.addNote(note);
        noteRepository.save(note);
        userService.save(user);
        return note;
    }

    public Note updateNote(Note oldNote, Note newNote) {
        oldNote.setName(newNote.getName());
        oldNote.setType(newNote.getType());
        oldNote.setNote(newNote.getNote());
        noteRepository.save(oldNote);
        return oldNote;
    }

    public NoteType defineNoteType(String type) {
        NoteType[] tempTypes = NoteType.values();
        for(int i = 0; i < tempTypes.length; i++) {
            if(type.toLowerCase().equals(tempTypes[i].name().toLowerCase())) {
                return tempTypes[i];
            }
        }
        return null;
    }

}
