package com.example.tictactoev2;

import android.widget.Button;

public class ButtonAndScore {

    public Button button;
    public int score;

    public ButtonAndScore(Button button, int score){
        this.button = button;
        this.score = score;
    }

    public int getButtonId() {
        return button.getId();
    }

    public int getButtonScore() {
        return score;
    }

}
