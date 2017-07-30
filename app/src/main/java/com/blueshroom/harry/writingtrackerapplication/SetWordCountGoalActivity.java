/**
 * Written 4/5/2017
 */

package com.blueshroom.harry.writingtrackerapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.blueshroom.harry.writingtrackerapplication.AddWordsActivity.MAX;
import static com.blueshroom.harry.writingtrackerapplication.ProjectActivity.INT_ERROR;

public class SetWordCountGoalActivity extends AppCompatActivity
{
    SharedPreferences value_restore = getSharedPreferences( getString( R.string.preference_key ) , 0 );
    SharedPreferences.Editor edit = value_restore.edit();

    // TextView
    TextView mInstructionTextView;

    // EditText
    EditText mWordGoalEditText;

    // TextView to show current word goal
    TextView mWordGoalViewText;

    // TextView to show current word total
    TextView mWordTotalViewText;

    // Button Add
    Button mSetWordGoalButton;

    // Button Back
    Button mBackButton;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_set_word_count_goal );

        setUI();

        setDisplay();

    }



    private void setUI()
    {
        mWordGoalViewText = ( TextView ) findViewById( R.id.word_goal_view_text );
        mWordTotalViewText = ( TextView ) findViewById( R.id.word_total_view_text );
        mInstructionTextView = ( TextView ) findViewById( R.id.word_count_instruction_text_view );
        mWordGoalEditText = ( EditText ) findViewById( R.id.word_goal_edit_text );
        mSetWordGoalButton = ( Button ) findViewById( R.id.set_word_goal_button );
        mBackButton = ( Button ) findViewById( R.id.back_button_goal );
    }

    private void setDisplay()
    {
        String setText = "" + R.string.word_count_goal_text + value_restore.getInt( getString( R.string.word_count_goal_key ) , INT_ERROR);
        mWordGoalViewText.setText( setText );

        setText = "" + R.string.word_count_text + value_restore.getInt( getString( R.string.word_count_key ) , INT_ERROR);
        mWordTotalViewText.setText( setText );
    }

    // Goes back to ProjectActivity
    public void back_pressed( View view )
    {
        Intent intent = new Intent( this , ProjectActivity.class );
        startActivity( intent );
    }

    // Set the word goal
    public void setWordGoal( View view )
    {
        int wordCount = value_restore.getInt( getString( R.string.word_count_key ), INT_ERROR );

        // Get input
        int input = Integer.parseInt( mWordGoalEditText.getText().toString() );

        // Check input, display toasts for input error
        if( input <= wordCount )
        {
            Toast.makeText( SetWordCountGoalActivity.this , "" + R.string.toast_input_low_word_count + wordCount , Toast.LENGTH_SHORT ).show();
            mWordGoalEditText.setText( "" );
            return;
        }
        if ( input >= MAX )
        {
            Toast.makeText( SetWordCountGoalActivity.this , "" + R.string.toast_input_max + MAX , Toast.LENGTH_SHORT ).show();
            mWordGoalEditText.setText( "" );
            return;
        }

        // Edit the Word Count Goal in SharedPreferences
        edit.putInt( getString( R.string.word_count_goal_key ) , input );
        edit.commit();

        // Toast
        Toast.makeText( SetWordCountGoalActivity.this , R.string.toast_change_word_goal + input , Toast.LENGTH_LONG ).show();

        // Set TextView
        String setText = "" + R.string.word_count_goal_text + value_restore.getInt( getString( R.string.word_count_goal_key ) , INT_ERROR);
        mWordGoalViewText.setText( setText );

    }

}
