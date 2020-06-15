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
            //playerTurn = !playerTurn;
            if(didOWin()){
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

                return;
            }
            if (checkDraw()){
                return;
            }
/*
            if (checkPreWinState()){
                System.out.println("Mark: " + getResources().getResourceEntryName(mark.getId()));
                mark.setText("X");

                if (didXWin()){
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
                    return;
                }
                if (checkDraw()){
                    return;
                }
                return;

            }
*/
            callMiniMax(0, false);
            for (ButtonAndScore btn : childrenScores) {
                System.out.println(getResources().getResourceEntryName(btn.getButtonId()) + ":" + btn.score);
            }
            makeMove(findBestMove(), false);

            if (didXWin()){
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
            checkDraw();
            }


        }



    ArrayList<ButtonAndScore> childrenScores;

    public Button findBestMove(){
        int max = -1000;
        int bestBtn = 0;

        for (int i = 0; i < childrenScores.size(); i++){
            if (max < childrenScores.get(i).getButtonScore()){
                max = childrenScores.get(i).getButtonScore();
                bestBtn = i;
            }
        }
        System.out.println("Best Move: " + getResources().getResourceEntryName(childrenScores.get(bestBtn).getButtonId()));
        return childrenScores.get(bestBtn).button;
    }

    public void callMiniMax(int depth, boolean playerTurn){
        childrenScores = new ArrayList<>();
        miniMax(depth, playerTurn);
    }

    public int miniMax(int depth, boolean playerTurn){

        ArrayList<Integer> scores = new ArrayList<Integer>();
        ArrayList<Button> availableButtons = new ArrayList<Button>();
        availableButtons = getAvailableButtons();

        if (didXWin())
            return 1;
        if (didOWin())
            return -1;
        if (availableButtons.size() == 0)
            return 0;

        for (int i = 0; i < availableButtons.size(); i++) {
            Button button = availableButtons.get(i);
            if (playerTurn) {
                makeMove(button, true);
                System.out.println("O move: "  + getResources().getResourceEntryName(button.getId()));
                int currentScore = miniMax(depth + 1, false);
                scores.add(currentScore);


            }
            else {
                makeMove(button, false);
                System.out.println("X move: "  + getResources().getResourceEntryName(button.getId()));
                int currentScore = miniMax(depth + 1, true);
                scores.add(currentScore);


                if (depth == 0) {
                    childrenScores.add(new ButtonAndScore(button, currentScore));
                }

            }
            button.setText("");
        }

        for (int score : scores){
            System.out.print(score + " ");
        }
        System.out.println();
        if (playerTurn)
            return getMax(scores);
        else
            return getMin(scores);
    }

    public int getMax(ArrayList<Integer> scores){
        int max = -1000;
        int index = 0;
        for (int i = 0; i < scores.size(); i++){
            if (scores.get(i) > max){
                max = scores.get(i);
                index = i;
            }
        }
        return scores.get(index);
    }

    public int getMin(ArrayList<Integer> scores){
        int min = 1000;
        int index = 0;
        for (int i = 0; i < scores.size(); i++){
            if (scores.get(i) < min){
                min = scores.get(i);
                index = i;
            }
        }
        return scores.get(index);
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
/*
    public boolean isGameOver(){
        if (didXWin() || didOWin() || getAvailableButtons().isEmpty()){
            return true;
        }
        else
            return false;
    }
*/
    public void makeMove(Button button, boolean playerTurn){
        if (playerTurn)
            button.setText("O");
        else
            button.setText("X");
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



