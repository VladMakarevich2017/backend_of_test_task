package murraco.dto;

import io.swagger.annotations.ApiModelProperty;
import murraco.model.NoteType;

public class NoteDataDTO {
    @ApiModelProperty(position = 0)
    private long id;
    @ApiModelProperty(position = 1)
    private String name;
    @ApiModelProperty(position = 2)
    private String note;
    @ApiModelProperty(position = 3)
    NoteType type;

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

    public NoteType getType() {
        return type;
    }

    public void setType(NoteType type) {
        this.type = type;
    }
}
