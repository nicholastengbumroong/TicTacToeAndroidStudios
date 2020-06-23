package com.example.tictactoev2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Computer extends Board implements View.OnClickListener {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        player1Text.setClickable(false);
        player2Text.setClickable(false);
        TextView player2Text = findViewById(R.id.p2);
        player2Text.setText("Computer");

        AlertDialog.Builder firstMove = new AlertDialog.Builder((Computer.this));
        firstMove.setMessage("SELECT WHO GOES FIRST");
        firstMove.setPositiveButton("Computer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int x = (int) (Math.random() * 3);
                int y = (int) (Math.random() * 3);
                String buttonName = "button_" + x + y;
                int buttonID = getResources().getIdentifier(buttonName, "id", getPackageName());
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (buttonID == buttons[i][j].getId()){
                            buttons[i][j].setText("X");
                        }
                    }
                }
            }

        });
        firstMove.setNegativeButton("Player 1", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = firstMove.show();

        TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER_HORIZONTAL);

        Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 10;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_reset)
            reset();
        else if (!((Button) v).getText().equals(""))
            return;
        else {
            ((Button) v).setText("O");
            // After player move, checks to see if player won
            if (didOWin()){
                OWon();
                return;
            }
            // if player did not win, checks to see if draw
            if (checkDraw()){
                return;
            }
            // if there is no draw, ai makes move
            System.out.println("Calculating best move");
            makeBestMove();

            // after ai move, checks to see if ai won
            if (didXWin()){
                XWon();
            }
            // if ai did not win, checks to see if draw
            checkDraw();
            }
        }

    // computer is X and maximizer
    // player is O and minimizer
    public void makeBestMove(){
        //Computer makes move
       int bestScore = -100;
       Button move = null;
       ArrayList<Button> availableButtons = getAvailableButtons();
       ArrayList<ButtonAndScore> childrenScores = new ArrayList<>();
        for (Button button: availableButtons) {
            //System.out.println("Depth: " + 0 + "; " + "X Move: " + getResources().getResourceEntryName(button.getId()));
            button.setText("X");
            int score = miniMax(1, false);
            button.setText("");
            childrenScores.add(new ButtonAndScore(button, score));
            if (score > bestScore){
                bestScore = score;
                move = button;
            }
        }
        for(ButtonAndScore scores: childrenScores){
            System.out.println(getResources().getResourceEntryName(scores.getButtonId()) + ":" + scores.getButtonScore());
        }
        move.setText("X");
    }

    public int miniMax(int depth, boolean isMaximizing) {

        ArrayList<Button> availableButtons = getAvailableButtons();

        if (didXWin())
            return 10;
        if (didOWin())
            return -10;
        if (availableButtons.size() == 0)
            return 0;

        for (Button button: availableButtons) {
            if (isMaximizing) {
                int bestScore = -1000;
                //System.out.println("Depth: " + depth + "; " + "X Move: " + getResources().getResourceEntryName(button.getId()));
                button.setText("X");
                int score = miniMax(depth + 1, false);
                button.setText("");
                bestScore = Math.max(score, bestScore);
                //System.out.println("Max: " + bestScore);
                return bestScore;
            } else {
                int bestScore = 1000;
                //System.out.println("Depth: " + depth + "; " + "O Move: " + getResources().getResourceEntryName(button.getId()));
                button.setText("O");
                int score = miniMax(depth + 1, true);
                button.setText("");
                bestScore = Math.min(score, bestScore);
                //System.out.println("Min: " + bestScore);
                return bestScore;
            }
        }
        return 0;
    }


   public ArrayList<Button> getAvailableButtons(){
       ArrayList<Button> availableButtons = new ArrayList<Button>();
       for (int i = 0; i < 3; i++){
           for (int j = 0; j < 3; j++){
               if (buttons[i][j].getText().equals(""))
                   availableButtons.add(buttons[i][j]);
           }
       }
       return availableButtons;
   }

   public boolean didXWin(){
       if ((buttons[0][0].getText().equals(buttons[1][1].getText()) && buttons[1][1].getText().equals(buttons[2][2].getText())
               && buttons[1][1].getText().equals("X")) || (buttons[2][0].getText().equals(buttons[1][1].getText()) && buttons[1][1].getText().equals(buttons[0][2].getText()) && buttons[1][1].getText().equals("X")))
           return true;
       for (int i = 0; i < 3; i++){
           if ((buttons[i][0].getText().equals(buttons[i][1].getText()) && buttons[i][1].getText().equals(buttons[i][2].getText()) &&
                   buttons[i][1].getText().equals("X")) || (buttons[0][i].getText().equals(buttons[1][i].getText()) && buttons[1][i].getText().equals(buttons[2][i].getText()) && buttons[1][i].getText().equals("X")))
               return true;
       }
       return false;
   }

   public boolean didOWin(){
       if ((buttons[0][0].getText().equals(buttons[1][1].getText()) && buttons[1][1].getText().equals(buttons[2][2].getText())
               && buttons[1][1].getText().equals("O")) || (buttons[2][0].getText().equals(buttons[1][1].getText()) && buttons[1][1].getText().equals(buttons[0][2].getText()) && buttons[1][1].getText().equals("O")))
           return true;
       for (int i = 0; i < 3; i++){
           if ((buttons[i][0].getText().equals(buttons[i][1].getText()) && buttons[i][1].getText().equals(buttons[i][2].getText()) &&
                   buttons[i][1].getText().equals("O")) || (buttons[0][i].getText().equals(buttons[1][i].getText()) && buttons[1][i].getText().equals(buttons[2][i].getText()) && buttons[1][i].getText().equals("O")))
               return true;
       }
       return false;
   }

    public void XWon(){
        player2Score++;
        player2ScoreText.setText(Integer.toString(player2Score));

        AlertDialog.Builder draw = new AlertDialog.Builder(Computer.this);
        draw.setMessage("COMPUTER WINS!");
        draw.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                softReset();
            }
        });
        AlertDialog dialog = draw.show();

        TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER_HORIZONTAL);

        final Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        positiveButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
        positiveButton.setLayoutParams(positiveButtonLL);
    }

    public void OWon(){
        player1Score++;
        player1ScoreText.setText(Integer.toString(player1Score));
        AlertDialog.Builder draw = new AlertDialog.Builder(Computer.this);
        draw.setMessage("PLAYER 1 WINS!");
        draw.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                softReset();
            }
        });
        AlertDialog dialog = draw.show();

        TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER_HORIZONTAL);

        final Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        positiveButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
        positiveButton.setLayoutParams(positiveButtonLL);
    }

    public boolean checkDraw(){
        if (getAvailableButtons().isEmpty()){
            AlertDialog.Builder draw = new AlertDialog.Builder(Computer.this);
            draw.setMessage("IT'S A DRAW!");
            draw.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    softReset();
                }
            });
            AlertDialog dialog = draw.show();

            TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
            messageView.setGravity(Gravity.CENTER_HORIZONTAL);

            final Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
            positiveButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
            positiveButton.setLayoutParams(positiveButtonLL);
            return true;
        }
        return false;
    }

    @Override
    public void reset(){
        super.reset();

        AlertDialog.Builder firstMove = new AlertDialog.Builder((Computer.this));
        firstMove.setMessage("SELECT WHO GOES FIRST");
        firstMove.setPositiveButton("Computer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int x = (int) (Math.random() * 3);
                int y = (int) (Math.random() * 3);
                String buttonName = "button_" + x + y;
                int buttonID = getResources().getIdentifier(buttonName, "id", getPackageName());
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (buttonID == buttons[i][j].getId()){
                            buttons[i][j].setText("X");
                        }
                    }
                }
            }

        });
        firstMove.setNegativeButton("Player 1", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = firstMove.show();

        TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER_HORIZONTAL);

        Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 10;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);
    }


    public void softReset(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        AlertDialog.Builder firstMove = new AlertDialog.Builder((Computer.this));
        firstMove.setMessage("SELECT WHO GOES FIRST");
        firstMove.setPositiveButton("Computer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int x = (int) (Math.random() * 3);
                int y = (int) (Math.random() * 3);
                String buttonName = "button_" + x + y;
                int buttonID = getResources().getIdentifier(buttonName, "id", getPackageName());
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (buttonID == buttons[i][j].getId()){
                            buttons[i][j].setText("X");
                        }
                    }
                }
            }

        });
        firstMove.setNegativeButton("Player 1", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = firstMove.show();

        TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER_HORIZONTAL);

        Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 10;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);

    }
}




