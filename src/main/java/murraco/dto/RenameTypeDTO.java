package murraco.dto;

import io.swagger.annotations.ApiModelProperty;

public class RenameTypeDTO {
    @ApiModelProperty(position = 0)
    private String oldType;

    @ApiModelProperty(position = 1)
    private String newType;

    public String getOldType() {
        return oldType;
    }

    public void setOldType(String oldType) {
        this.oldType = oldType;
    }

    public String getNewType() {
        return newType;
    }

    public void setNewType(String newType) {
        this.newType = newType;
    }
}
