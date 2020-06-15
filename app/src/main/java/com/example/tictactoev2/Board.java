package com.example.tictactoev2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tictactoev2.R;

import java.util.ArrayList;

public class Board extends AppCompatActivity implements View.OnClickListener {

    public Button[][] buttons = new Button[3][3];

    public int player1Score;
    public int player2Score;
    public TextView player1Text;
    public TextView player2Text;

    public TextView player1ScoreText;
    public TextView player2ScoreText;

    public boolean playerTurn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);

        Button resetButton = findViewById(R.id.button_reset);
        resetButton.setOnClickListener(this);


        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonName = "button_" + i + j;
                int buttonID = getResources().getIdentifier(buttonName, "id", getPackageName());
                buttons[i][j] = findViewById(buttonID);
                buttons[i][j].setOnClickListener(this);


            }
        }
        player1Text = findViewById(R.id.p1);
        player2Text = findViewById(R.id.p2);
        player1Text.setOnClickListener(this);
        player2Text.setOnClickListener(this);
        player1ScoreText = findViewById(R.id.p1_score);
        player2ScoreText = findViewById(R.id.p2_score);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_reset)
            reset();
        else if (v.getId() == R.id.p1){
            AlertDialog.Builder player1 = new AlertDialog.Builder((Board.this));
            final EditText input = new EditText(this);
            player1.setView(input);
            player1.setTitle("Enter name for Player 1");
            player1.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    player1Text.setText(input.getText());
                }
            });
            player1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            player1.show();
        }
        else if (v.getId() == R.id.p2){
            AlertDialog.Builder player1 = new AlertDialog.Builder((Board.this));
            final EditText input = new EditText(this);
            player1.setView(input);
            player1.setTitle("Enter name for Player 2");
            player1.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    player2Text.setText(input.getText());
                }
            });
            player1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            player1.show();
        }
        else
            click(v);
    }

    public void reset() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        playerTurn = true;
        player1Score = 0;
        player1ScoreText.setText("0");
        player2Score = 0;
        player2ScoreText.setText("0");
    }

    public void click(View v) {
        if (!("".contentEquals(((Button) v).getText()))){
            return;
        }
        if (playerTurn){
            ((Button) v).setText("X");
        }
        else {
            ((Button) v).setText("O");
        }
        if (getAvailableButtons().isEmpty()){
            AlertDialog.Builder draw = new AlertDialog.Builder(Board.this);
            draw.setMessage("IT'S A DRAW!");
            draw.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            buttons[i][j].setText("");
                        }
                    }
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
        if (checkWin(buttons)) {

            if (playerTurn) {
                player1Score++;
                player1ScoreText.setText(Integer.toString(player1Score));
                AlertDialog.Builder draw = new AlertDialog.Builder(Board.this);
                draw.setMessage("PLAYER 1 WINS!");
                draw.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 3; j++) {
                                buttons[i][j].setText("");
                            }
                        }
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
            else {
                player2Score++;
                player2ScoreText.setText(Integer.toString(player2Score));
                AlertDialog.Builder draw = new AlertDialog.Builder(Board.this);
                draw.setMessage("PLAYER 2 WINS!");
                draw.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 3; j++) {
                                buttons[i][j].setText("");
                            }
                        }
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

        }
        playerTurn = !playerTurn;

    }

    public static boolean checkWin(Button[][] buttons){

        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().toString().equals(buttons[i][1].getText().toString()) &&
                    buttons[i][1].getText().toString().equals(buttons[i][2].getText().toString()) &&
                    !(buttons[i][0].getText().toString().equals(""))) {
                return true;
            }
        }
        for (int j = 0; j < 3; j++) {
            if (buttons[0][j].getText().toString().equals(buttons[1][j].getText().toString()) &&
                    buttons[1][j].getText().toString().equals(buttons[2][j].getText().toString()) &&
                    !(buttons[0][j].getText().toString().equals("")) ) {
                return true;
            }
        }
        if (buttons[0][0].getText().toString().equals(buttons[1][1].getText().toString()) &&
                buttons[1][1].getText().toString().equals(buttons[2][2].getText().toString()) &&
                !(buttons[0][0].getText().toString().equals(""))) {
            return true;
        }
        if (buttons[0][2].getText().toString().equals(buttons[1][1].getText().toString()) &&
                buttons[1][1].getText().toString().equals(buttons[2][0].getText().toString()) &&
                !(buttons[0][2].getText().toString().equals(""))) {
            return true;
        }

        return false;
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

}
