package murraco.dto;

import io.swagger.annotations.ApiModelProperty;
import murraco.model.Note;

public class AdditionalNodeDTO {
    @ApiModelProperty(position = 0)
    private Note note;

    @ApiModelProperty(position = 1)
    private Note additionalNote;

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public Note getAdditionalNote() {
        return additionalNote;
    }

    public void setAdditionalNote(Note additionalNote) {
        this.additionalNote = additionalNote;
    }
}
