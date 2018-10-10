package murraco.controller;

import murraco.dto.AdditionalNodeDTO;
import murraco.dto.NoteResponseDTO;
import murraco.model.Note;
import murraco.model.NoteType;
import murraco.repository.NoteRepository;
import murraco.service.NoteService;
import murraco.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(value = "/my")
    public List<NoteResponseDTO> getMyNotes(HttpServletRequest req) {
        return userService.whoami(req).getNotes().stream()
                .map(note -> modelMapper.map(note, NoteResponseDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}")
    public NoteResponseDTO getNoteById(@PathVariable("id") Long id, HttpServletRequest req) {
        if(noteRepository.existsByUserAndId(userService.whoami(req), id)) {
            return modelMapper.map(noteRepository.findByUserAndId(userService.whoami(req), id), NoteResponseDTO.class);
        }
        return null;
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

    @PostMapping(value = "/additional")
    public Note addNoteInside(@RequestBody AdditionalNodeDTO notes, HttpServletRequest req) {
        if(noteRepository.existsByUserAndId(userService.whoami(req), notes.getNote().getId())
                && !noteRepository.findByUserAndId(userService.whoami(req), notes.getNote().getId()).getNotesInside()
                .contains(noteRepository.findByUserAndId(userService.whoami(req), notes.getAdditionalNote().getId()))) {
            noteRepository.findByUserAndId(userService.whoami(req), notes.getNote().getId()).addNoteInside(
                    noteRepository.findByUserAndId(userService.whoami(req), notes.getAdditionalNote().getId()));
            noteRepository.save(noteRepository.findByUserAndId(userService.whoami(req), notes.getAdditionalNote().getId()));
            noteRepository.save(noteRepository.findByUserAndId(userService.whoami(req), notes.getNote().getId()));
            return notes.getAdditionalNote();
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
            removeInsideNotes(id, req);
            userService.whoami(req).getNotes().remove(noteRepository.findByUserAndId(userService.whoami(req), id));
            userService.save(userService.whoami(req));
            return true;
        }
        return false;
    }

    public void removeInsideNotes(Long id, HttpServletRequest req) {
        removeLinks(id, req);
        clearLinks(id, req);
    }

    public void removeLinks(Long id, HttpServletRequest req) {
        for(Note tempNote : noteRepository.findByUserAndId(userService.whoami(req), id).getNotesInsideOf()) {
            if(tempNote.getNotesInsideOf().contains(noteRepository.findByUserAndId(userService.whoami(req), id))) {
                tempNote.getNotesInsideOf().remove(noteRepository.findByUserAndId(userService.whoami(req), id));
            }
            tempNote.getNotesInside().remove(noteRepository.findByUserAndId(userService.whoami(req), id));
            noteRepository.save(tempNote);
        }
    }

    public void clearLinks(Long id, HttpServletRequest req) {
        for(Note tempNote : noteRepository.findByUserAndId(userService.whoami(req), id).getNotesInside()) {
            tempNote.getNotesInsideOf().remove(noteRepository.findByUserAndId(userService.whoami(req), id));
            noteRepository.save(tempNote);
        }
    }

    @PostMapping(value = "/delete/inside")
    public boolean deleteInsideNote(@RequestBody AdditionalNodeDTO notes, HttpServletRequest req) {
        if(noteRepository.existsByUserAndId(userService.whoami(req), notes.getNote().getId())
                && noteRepository.existsByUserAndId(userService.whoami(req), notes.getAdditionalNote().getId())) {
            removeLinksInsideNote(notes, req);
            saveNotesAfterRemoving(notes, req);
            return true;
        }
        return false;
    }

    public void removeLinksInsideNote(AdditionalNodeDTO notes, HttpServletRequest req) {
        noteRepository.findByUserAndId(userService.whoami(req), notes.getNote().getId()).getNotesInside()
                .remove(noteRepository.findByUserAndId(userService.whoami(req), notes.getAdditionalNote().getId()));
        noteRepository.findByUserAndId(userService.whoami(req), notes.getAdditionalNote().getId()).getNotesInsideOf()
                .remove(noteRepository.findByUserAndId(userService.whoami(req), notes.getNote().getId()));
    }

    public void saveNotesAfterRemoving(AdditionalNodeDTO notes, HttpServletRequest req) {
        noteRepository.save(noteRepository.findByUserAndId(userService.whoami(req), notes.getNote().getId()));
        noteRepository.save(noteRepository.findByUserAndId(userService.whoami(req), notes.getAdditionalNote().getId()));
    }

}
