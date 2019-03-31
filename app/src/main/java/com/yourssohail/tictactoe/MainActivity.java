package com.yourssohail.tictactoe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];
    private boolean player1Turn = true;
    private int roundCount;
    private int player1Points;
    private int player2Points;
    private ImageView btReset;
    private TextView tvPlayer1,tvPlayer2;
    final int SETTINGS_ACTIVITY = 1;
    String prefPlayer1Name,prefPlayer2Name;
    int colorId = R.color.colorPrimaryGreen;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_main);



        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Tic Tac Toe Lite");


        tvPlayer1 = findViewById(R.id.tvPlayer1);
        tvPlayer2 = findViewById(R.id.tvPlayer2);



        setName();

        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                String btId = "bt"+i+j;
                int resId = getResources().getIdentifier(btId,"id",getPackageName());
                buttons[i][j] = findViewById(resId);

                buttons[i][j].setOnClickListener(this);
            }
        }

        btReset = findViewById(R.id.btReset);
        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player1Points=0;
                player2Points=0;
                updatePointText();
                resetBoard();
                Toast.makeText(MainActivity.this, "Reset", Toast.LENGTH_SHORT).show();
            }
        });

        setBackgroundColor(colorId);
    }

    @Override
    public void onClick(View v) {
        if(!((Button) v).getText().toString().equals("")){
            return;
        }

        if(player1Turn){
            ((Button) v).setText("X");
        }else{
            ((Button) v).setText("O");
        }

        roundCount++;

        if(checkForWin()){
            if(player1Turn){
                player1Wins();
            }else{
                player2Wins();
            }
        }else if(roundCount == 9){
            draw();
        }else{
            player1Turn = !player1Turn;
        }
    }

    private void player1Wins() {
        player1Points++;
        Toast.makeText(this, prefPlayer1Name+" wins!", Toast.LENGTH_SHORT).show();
        updatePointText();
        resetBoard();
    }



    private void player2Wins() {
        player2Points++;
        Toast.makeText(this, prefPlayer2Name+" wins!", Toast.LENGTH_SHORT).show();
        updatePointText();
        resetBoard();

    }

    private void draw() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        resetBoard();
    }

    private void updatePointText() {
        tvPlayer1.setText(prefPlayer1Name+": "+player1Points);
        tvPlayer2.setText(prefPlayer2Name+": "+player2Points);
    }

    private void resetBoard(){
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                buttons[i][j].setText("");
            }
        }

        roundCount = 0;
        player1Turn = true;



    }

    private boolean checkForWin(){
        String[][] field = new String[3][3];

        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for(int i=0;i<3;i++){
            if(field[i][0].equals(field[i][1])
            && field[i][0].equals(field[i][2])
            && !field[i][0].equals("")){
                return true;
            }
        }

        for(int i=0;i<3;i++){
            if(field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")){
                return true;
            }
        }

        if(field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")){
            return true;
        }

        if(field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")){
            return true;
        }

        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount",roundCount);
        outState.putInt("player1Points",player1Points);
        outState.putInt("player2Points",player2Points);
        outState.putBoolean("player1Turn",player1Turn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.settings:
                startActivityForResult(new Intent(MainActivity.this,Settings.class),SETTINGS_ACTIVITY);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SETTINGS_ACTIVITY){

            setName();
            setBackgroundColor(colorId);
            this.recreate();
        }
    }


    public void setTheme(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        if(sp.getString("color_choices","Green").equals("Green")){
            setTheme(R.style.GreenTheme);
            colorId = R.color.colorPrimaryGreen;

        }else if(sp.getString("color_choices","Orange").equals("Orange")){
            setTheme(R.style.OrangeTheme);
            colorId = R.color.colorPrimaryOrange;

        }else if(sp.getString("color_choices","Red").equals("Red")){
            setTheme(R.style.RedTheme);
            colorId = R.color.colorPrimaryRed;

        }else if(sp.getString("color_choices","Blue").equals("Blue")){
            setTheme(R.style.BlueTheme);
            colorId = R.color.colorPrimaryBlue;

        }else if(sp.getString("color_choices","Yellow").equals("Yellow")){
            setTheme(R.style.YellowTheme);
            colorId = R.color.colorPrimaryYellow;

        }
    }

    private void setName(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        prefPlayer1Name = sp.getString("change_player1_name","Player 1");
        tvPlayer1.setText(prefPlayer1Name+": 0");

        prefPlayer2Name = sp.getString("change_player2_name","Player 2");
        tvPlayer2.setText(prefPlayer2Name+": 0");
    }

    private void setBackgroundColor(int colorId){
        Drawable background = btReset.getBackground();
        Drawable bgTitle;
        if (background instanceof ShapeDrawable) {
            // cast to 'ShapeDrawable'
            ShapeDrawable shapeDrawable = (ShapeDrawable) background;
            shapeDrawable.getPaint().setColor(ContextCompat.getColor(this, colorId));
        } else if (background instanceof GradientDrawable) {
            // cast to 'GradientDrawable'
            GradientDrawable gradientDrawable = (GradientDrawable) background;
            gradientDrawable.setColor(ContextCompat.getColor(this,colorId));
        } else if (background instanceof ColorDrawable) {
            // alpha value may need to be set again after this call
            ColorDrawable colorDrawable = (ColorDrawable) background;
            colorDrawable.setColor(ContextCompat.getColor(this,colorId));
        }

        Button bt;

        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                String btId = "bt"+i+j;
                int resId = getResources().getIdentifier(btId,"id",getPackageName());
                bt = findViewById(resId);
                bgTitle = bt.getBackground();

                if (bgTitle instanceof ShapeDrawable) {
                    // cast to 'ShapeDrawable'
                    ShapeDrawable shapeDrawable = (ShapeDrawable) bgTitle;
                    shapeDrawable.getPaint().setColor(ContextCompat.getColor(this, colorId));
                } else if (bgTitle instanceof GradientDrawable) {
                    // cast to 'GradientDrawable'
                    GradientDrawable gradientDrawable = (GradientDrawable) bgTitle;
                    gradientDrawable.setColor(ContextCompat.getColor(this,colorId));
                } else if (bgTitle instanceof ColorDrawable) {
                    // alpha value may need to be set again after this call
                    ColorDrawable colorDrawable = (ColorDrawable) bgTitle;
                    colorDrawable.setColor(ContextCompat.getColor(this,colorId));
                }

            }
        }

    }


}
