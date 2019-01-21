package com.example.prana.androidguitictactoe;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button[][] grid = new Button[3][3];
    int[][] board = new int[3][3];
    final int BLANK = 0;
    final int X_MOVE = 1;
    final int O_MOVE = 2;
    int combo;
    Button playAgain;
    TextView xWin;
    TextView oWin;
    int xwins = 0;
    int owins = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        grid[0][0] = (Button)this.findViewById(R.id.button1);
        grid[0][0].setOnClickListener(this);
        grid[0][1] = (Button)this.findViewById(R.id.button2);
        grid[0][1].setOnClickListener(this);
        grid[0][2] = (Button)this.findViewById(R.id.button3);
        grid[0][2].setOnClickListener(this);
        grid[1][0] = (Button)this.findViewById(R.id.button4);
        grid[1][0].setOnClickListener(this);
        grid[1][1] = (Button)this.findViewById(R.id.button5);
        grid[1][1].setOnClickListener(this);
        grid[1][2] = (Button)this.findViewById(R.id.button6);
        grid[1][2].setOnClickListener(this);
        grid[2][0] = (Button)this.findViewById(R.id.button7);
        grid[2][0].setOnClickListener(this);
        grid[2][1] = (Button)this.findViewById(R.id.button8);
        grid[2][1].setOnClickListener(this);
        grid[2][2] = (Button)this.findViewById(R.id.button9);
        grid[2][2].setOnClickListener(this);
        playAgain= (Button)this.findViewById(R.id.playAgain);
        playAgain.setOnClickListener(this);
        xWin = this.findViewById(R.id.xWins);
        oWin = this.findViewById(R.id.oWins);
        xWin.setGravity(Gravity.CENTER);
        oWin.setGravity(Gravity.CENTER);
        xWin.setText("X WINS: " + xwins);
        oWin.setText("0 WINS: " + owins);
        for (int x = 0; x < 3 ; x++) {
            for (int i = 0; i < 3 ; i++) {
                grid[x][i].setTextSize(48);
                grid[x][i].setTextColor(Color.BLACK);
                grid[x][i].setBackgroundResource(android.R.drawable.btn_default);

            }
        }

    }
    Typeface boldTypeface;

    {
        boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (b.equals(grid[x][y]) && board[x][y] == BLANK) {

                    b.setTypeface(boldTypeface);
                    b.setText("X");
                    b.setEnabled(false);
                    board[x][y] = X_MOVE;
                    if(checkTie()){
                        freeze();
                        Toast.makeText(this,"ITS A TIE!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (checkWin(X_MOVE)) {
                        freeze();
                        xwins++;
                        xWin.setText("X WINS: " + xwins);
                        Toast.makeText(this,"X WINS!", Toast.LENGTH_LONG).show();
                        setColor(combo);
                        return;
                    }

                    // check win, clear board,
                    aiMove();
                    if(checkTie()){
                        freeze();
                        Toast.makeText(this,"ITS A TIE!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (checkWin(O_MOVE)) {
                        freeze();
                        owins++;
                        oWin.setText("O WINS: " + owins);
                        Toast.makeText(this,"O WINS!", Toast.LENGTH_LONG).show();
                        setColor(combo);
                        return;
                    }

                }
            }
        }
        if (b.equals(playAgain)) {
            clear();
        }
    }


    public void freeze(){
        for(int x = 0; x< 3; x++){
            for(int y = 0; y < 3; y++){
                grid[x][y].setEnabled(false);
            }
        }
    }

    public void clear(){
        for(int x = 0; x < 3; x++){
            for(int y = 0; y < 3; y++){
                grid[x][y].setEnabled(true);
                grid[x][y].setText("");
                board[x][y] = BLANK;
                grid[x][y].setBackgroundResource(android.R.drawable.btn_default);
            }
        }
    }

    public void aiMove(){
        //try to win
        if(aiCheck(O_MOVE) == true){
            return;
        }
        // try to block
        if(aiCheck(X_MOVE)){
            return;
        }
        //picks randomly
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(int x = 0; x < 3; x++){
            for(int y = 0; y < 3; y++){
                if (board[x][y] == BLANK) {
                    list.add(x*10 + y);
                }
            }
        }
        int choice = (int)(Math.random() * list.size());
        int y = list.get(choice) % 10;
        int x = list.get(choice) / 10;
        board[x][y] = O_MOVE;
        grid[x][y].setText("O");
        grid[x][y].setEnabled(false);
    }


    public boolean aiCheck(int player){
        // colomn win
        for(int x = 0; x < 3; x++){
            int oCount = 0;
            int blankCount = 0;
            int blankX = 0;
            int blankY = 0;
            for(int y = 0; y<3; y++){
                if(board[x][y] == BLANK){
                    blankCount++;
                    blankX = x;
                    blankY = y;

                }
                if(board[x][y] == player ){
                    oCount ++;
                }
            }
            if(oCount ==2 && blankCount == 1){
                board[blankX][blankY] = O_MOVE;
                grid[blankX][blankY].setText("O");
                grid[blankX][blankY].setEnabled(false);
                return true;
            }
        }
        // row win
        for(int y = 0; y < 3; y++){
            int oCount = 0;
            int blankCount = 0;
            int blankX = 0;
            int blankY = 0;
            for(int x = 0; x<3; x++){
                if(board[x][y] == BLANK){
                    blankCount++;
                    blankX = x;
                    blankY = y;

                }
                if(board[x][y] == player ){
                    oCount ++;
                }
            }
            if(oCount ==2 && blankCount == 1){
                board[blankX][blankY] = O_MOVE;
                grid[blankX][blankY].setText("O");
                grid[blankX][blankY].setEnabled(false);
                return true;
            }

        }
        /* top left to bottom right */
        int oCount = 0;
        int blankCount = 0;
        int blankX = 0;
        int blankY = 0;
        for(int z = 0; z<3; z++) {
            if (board[z][z] == BLANK) {
                blankCount++;
                blankX = z;
                blankY = z;

            }
            if (board[z][z] == player) {
                oCount++;
            }

        }
        if((oCount == 2) && (blankCount == 1)){
            board[blankX][blankY] = O_MOVE;
            grid[blankX][blankY].setText("O");
            grid[blankX][blankY].setEnabled(false);
            return true;
        }
        // top right to bottom left-----
        oCount = 0;
        blankCount = 0;
        blankX = 0;
        blankY = 0;
        for(int z = 0; z<3; z++) {
            if (board[z][2-z] == BLANK) {
                blankCount++;
                blankX = z;
                blankY = 2-z;

            }
            if (board[z][2-z] == player) {
                oCount++;
            }

        }
        if((oCount == 2) && (blankCount == 1)){
            board[blankX][blankY] = O_MOVE;
            grid[blankX][blankY].setText("O");
            grid[blankX][blankY].setEnabled(false);
            return true;
        }
        return false;
    }

    public boolean checkWin(int player) {
        if (board[0][0] == player && board[0][1] == player && board[0][2] == player) {
            combo = 1;
            return true;
        }
        if (board[1][0] == player && board[1][1] == player && board[1][2] == player) {
            combo = 2;
            return true;
        }
        if (board[2][0] == player && board[2][1] == player && board[2][2] == player) {
            combo = 3;
            return true;
        }
        if (board[0][0] == player && board[1][0] == player && board[2][0] == player) {
            combo = 4;
            return true;
        }
        if (board[0][1] == player && board[1][1] == player && board[2][1] == player) {
            combo = 5;
            return true;
        }
        if (board[0][2] == player && board[1][2] == player && board[2][2] == player) {
            combo = 6;
            return true;
        }
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            combo = 7;
            return true;
        }
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            combo = 8;
            return true;
        } else {
            return false;
        }
    }

    // checks to see if the game results in a tie
    public boolean checkTie() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(board[i][j] == BLANK){
                    return false;
                }
            }

        }
        return true;
    }

    // if a player has won, then sets the winning buttons as green
    public void setColor(int combo) {
        if (combo == 1) {
            grid[0][0].setBackgroundColor(Color.GREEN);
            grid[0][1].setBackgroundColor(Color.GREEN);
            grid[0][2].setBackgroundColor(Color.GREEN);
        } else if (combo == 2) {
            grid[1][0].setBackgroundColor(Color.GREEN);
            grid[1][1].setBackgroundColor(Color.GREEN);
            grid[1][2].setBackgroundColor(Color.GREEN);

        } else if (combo == 3) {
            grid[2][0].setBackgroundColor(Color.GREEN);
            grid[2][1].setBackgroundColor(Color.GREEN);
            grid[2][2].setBackgroundColor(Color.GREEN);

        } else if (combo == 4) {
            grid[0][0].setBackgroundColor(Color.GREEN);
            grid[1][0].setBackgroundColor(Color.GREEN);
            grid[2][0].setBackgroundColor(Color.GREEN);
        } else if (combo == 5) {
            grid[0][1].setBackgroundColor(Color.GREEN);
            grid[1][1].setBackgroundColor(Color.GREEN);
            grid[2][1].setBackgroundColor(Color.GREEN);
        } else if (combo == 6) {
            grid[0][2].setBackgroundColor(Color.GREEN);
            grid[1][2].setBackgroundColor(Color.GREEN);
            grid[2][2].setBackgroundColor(Color.GREEN);
        } else if (combo == 7) {
            grid[0][0].setBackgroundColor(Color.GREEN);
            grid[1][1].setBackgroundColor(Color.GREEN);
            grid[2][2].setBackgroundColor(Color.GREEN);
        } else if (combo == 8) {
            grid[0][2].setBackgroundColor(Color.GREEN);
            grid[1][1].setBackgroundColor(Color.GREEN);
            grid[2][0].setBackgroundColor(Color.GREEN);
        }
    }


}

