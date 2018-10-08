package murraco.controller;

import murraco.model.Note;
import murraco.model.NoteType;
import murraco.repository.NoteRepository;
import murraco.service.NoteService;
import murraco.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/notes")
@CrossOrigin
public class NoteController {
    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/my")
    public List<Note> getMyNotes(HttpServletRequest req) {
        return userService.whoami(req).getNotes();
    }

    @PostMapping(value = "/add")
    public Note addNewNote(@RequestBody String type, HttpServletRequest req) {
        return noteService.createNote(userService.whoami(req), type);
    }

    @PostMapping(value = "/update")
    public Note updateNote(@RequestBody Note note, HttpServletRequest req) {
        if(noteRepository.existsByUserAndId(userService.whoami(req), note.getId())) {
            return noteService.updateNote(noteRepository.findByUserAndId(userService.whoami(req), note.getId()), note);
        }
        return null;
    }

    @GetMapping(value = "/types")
    public List<String> getTypesOfNotes(HttpServletRequest req) {
        List<String> types = new ArrayList<>();
        NoteType[] tempTypes = NoteType.values();
        for(int i = 0; i < tempTypes.length; i++) types.add(tempTypes[i].name().toLowerCase());
        return types;
    }

    @PostMapping(value = "/delete")
    public boolean deleteNote(@RequestBody Long id, HttpServletRequest req) {
        if(noteRepository.existsByUserAndId(userService.whoami(req), id)) {
            userService.whoami(req).getNotes().remove(noteRepository.findByUserAndId(userService.whoami(req), id));
            userService.save(userService.whoami(req));
            return true;
        }
        return false;
    }

}
