package com.ridango.game.model;

public class CharacterToken {
    private char value;
    private Boolean isHidden = true;

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public Boolean getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(Boolean isHidden) {
        this.isHidden = isHidden;
    }

}
