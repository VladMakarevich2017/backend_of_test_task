package murraco.controller;

import murraco.dto.AdditionalNodeDTO;
import murraco.dto.NoteResponseDTO;
import murraco.dto.RenameTypeDTO;
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

    @GetMapping(value = "/mytypes")
    public List<String> getMyNoteTypes(HttpServletRequest req) {
        return userService.whoami(req).getNoteTypes();
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

    @PostMapping(value = "/checktype")
    public boolean checkType(@RequestBody String type, HttpServletRequest req) {
        if(type != null && userService.whoami(req).getNoteTypes().contains(type)) return true;
        return false;
    }

    @PostMapping(value = "/delete/type")
    public boolean removeType(@RequestBody String type, HttpServletRequest req) {
        if(userService.whoami(req).getNoteTypes().contains(type)) {
            for(Note note: userService.whoami(req).getNotes()) {
                if(note.getType().toLowerCase() == type.toLowerCase()) {
                    deleteNote(note.getId(), req);
                }
            }
            userService.whoami(req).getNoteTypes().remove(type);
            userService.save(userService.whoami(req));
            return true;
        }
        return false;
    }

    @PostMapping(value = "/renametype")
    public List<String> renameType(@RequestBody RenameTypeDTO types, HttpServletRequest req) {
        List<String> tempList = new ArrayList<>();
        if(userService.whoami(req).getNoteTypes().contains(types.getOldType())
                && !userService.whoami(req).getNoteTypes().contains(types.getNewType())) {
            changeNotesType(types, req);
            userService.whoami(req).getNoteTypes().set(userService.whoami(req).getNoteTypes().indexOf(types.getOldType()), types.getNewType());
            userService.save(userService.whoami(req));
            tempList.add(types.getNewType());
            return tempList;
        }
        return null;
    }

    public void changeNotesType(RenameTypeDTO types, HttpServletRequest req) {
        for(Note note: userService.whoami(req).getNotes()) {
            if(note.getType().toLowerCase().equals(types.getOldType().toLowerCase())) {
                note.setType(types.getNewType());
                noteRepository.save(note);
            }
        }
    }

    @PostMapping(value = "/addtype")
    public List<String> addNewNoteType(@RequestBody String type, HttpServletRequest req) {
        List<String> tempList = new ArrayList<>();
        if(type != null && type.length() != 0 && !userService.whoami(req).getNoteTypes().contains(type)) {
            userService.whoami(req).getNoteTypes().add(type);
            userService.save(userService.whoami(req));
            tempList.add(type);
            return tempList;
        }
        return null;
    }

    @PostMapping(value = "/update")
    public NoteResponseDTO updateNote(@RequestBody Note note, HttpServletRequest req) {
        if(noteRepository.existsByUserAndId(userService.whoami(req), note.getId())) {
            return modelMapper.map(noteService.updateNote(noteRepository.findByUserAndId(userService.whoami(req), note.getId()), note), NoteResponseDTO.class);
        }
        return null;
    }

    @PostMapping(value = "/additional")
    public Note addNoteInside(@RequestBody AdditionalNodeDTO notes, HttpServletRequest req) {
        if(noteRepository.existsByUserAndId(userService.whoami(req), notes.getNote().getId())
                && !noteRepository.findByUserAndId(userService.whoami(req), notes.getNote().getId()).getNotesInside()
                .contains(noteRepository.findByUserAndId(userService.whoami(req), notes.getAdditionalNoteId()))) {
            noteRepository.findByUserAndId(userService.whoami(req), notes.getNote().getId()).addNoteInside(
                    noteRepository.findByUserAndId(userService.whoami(req), notes.getAdditionalNoteId()));
            noteRepository.save(noteRepository.findByUserAndId(userService.whoami(req), notes.getAdditionalNoteId()));
            noteRepository.save(noteRepository.findByUserAndId(userService.whoami(req), notes.getNote().getId()));
            return noteRepository.findByUserAndId(userService.whoami(req), notes.getAdditionalNoteId());
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
                && noteRepository.existsByUserAndId(userService.whoami(req), notes.getAdditionalNoteId())) {
            removeLinksInsideNote(notes, req);
            saveNotesAfterRemoving(notes, req);
            return true;
        }
        return false;
    }

    public void removeLinksInsideNote(AdditionalNodeDTO notes, HttpServletRequest req) {
        noteRepository.findByUserAndId(userService.whoami(req), notes.getNote().getId()).getNotesInside()
                .remove(noteRepository.findByUserAndId(userService.whoami(req), notes.getAdditionalNoteId()));
        noteRepository.findByUserAndId(userService.whoami(req), notes.getAdditionalNoteId()).getNotesInsideOf()
                .remove(noteRepository.findByUserAndId(userService.whoami(req), notes.getNote().getId()));
    }

    public void saveNotesAfterRemoving(AdditionalNodeDTO notes, HttpServletRequest req) {
        noteRepository.save(noteRepository.findByUserAndId(userService.whoami(req), notes.getNote().getId()));
        noteRepository.save(noteRepository.findByUserAndId(userService.whoami(req), notes.getAdditionalNoteId()));
    }

}
