package murraco.dto;

import io.swagger.annotations.ApiModelProperty;
import murraco.model.Note;

public class AdditionalNodeDTO {
    @ApiModelProperty(position = 0)
    private Note note;

    @ApiModelProperty(position = 1)
    private Long additionalNoteId;

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public Long getAdditionalNoteId() {
        return additionalNoteId;
    }

    public void setAdditionalNoteId(Long additionalNoteId) {
        this.additionalNoteId = additionalNoteId;
    }
}
