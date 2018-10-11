package murraco.dto;

import io.swagger.annotations.ApiModelProperty;
import murraco.model.Note;
import murraco.model.NoteType;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

public class NoteResponseDTO {
    @ApiModelProperty(position = 0)
    private long id;
    @ApiModelProperty(position = 1)
    private String name;
    @ApiModelProperty(position = 2)
    private String note;
    @ApiModelProperty(position = 3)
    private String type;
    @ApiModelProperty(position = 4)
    private List<NoteDataDTO> notesInside = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<NoteDataDTO> getNotesInside() {
        return notesInside;
    }

    public void setNotesInside(List<NoteDataDTO> notesInside) {
        this.notesInside = notesInside;
    }
}
