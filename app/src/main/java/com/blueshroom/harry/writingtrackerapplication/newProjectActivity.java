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

import org.joda.time.DateTime;

import java.util.ArrayList;

public class newProjectActivity extends AppCompatActivity
{
    private EditText mNpaNameEditText;

    private EditText mNpaWordcountEditText;

    private EditText mNpaGoalDaysEditText;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_new_project );

        setUI();
    }

    private void setUI()
    {
        mNpaNameEditText = ( EditText ) findViewById( R.id.npa_name_edittext );
        mNpaWordcountEditText = ( EditText ) findViewById( R.id.npa_wordcount_edittext );
        mNpaGoalDaysEditText = ( EditText ) findViewById( R.id.npa_goaldays_edittext );
    }


    public void confirm( View v )
    {
        /////////////// Input Checking ////////////////////////
        String nameInput = mNpaNameEditText.getText().toString().trim();
        ArrayList<String> errors = new ArrayList<String>();
        // if any of the editTexts are empty or unchanged, call invalidInputError
        if( nameInput.equals( R.string.npa_name_edittext ) || nameInput.isEmpty() || nameInput.length() == 0 || nameInput.equals( "" ) || nameInput == null )
            errors.add( "Project Name" );
        ////////////////////////////////////
        String wordCountText = mNpaWordcountEditText.getText().toString();
        int wordCount = 0;
        try {
            wordCount = Integer.parseInt( wordCountText );
        } catch (NumberFormatException e) {
            errors.add( "Word Count" );
        }
        if( wordCount <= 0 && !errors.contains("Word Count") )
            errors.add( "Word Count" );
        //////////////////////////////
        String goalDaysText = mNpaGoalDaysEditText.getText().toString();
        int goalDays = 0;
        try {
            goalDays = Integer.parseInt( goalDaysText );
        } catch (NumberFormatException e) {
            errors.add( "Goal Days" );
        }
        if( goalDays <= 0 && !errors.contains("Goal Days") )
            errors.add( "Goal Days" );
        /////////////////////////////////
        //toast
        if(errors.size() != 0 ) {
            String toastPart = "";
            for (String error : errors)
                toastPart = toastPart + error + ", ";
            Toast.makeText(newProjectActivity.this, toastPart + "have invalid input.", Toast.LENGTH_LONG).show();
            return;
        } else {
            ///////////////////////////////////////////////////////

            // set values in the sharedPreferences
            SharedPreferences value_restore = getSharedPreferences(getString(R.string.preference_key), 0);
            SharedPreferences.Editor edit = value_restore.edit();

            edit.putString( getString( R.string.project_title_key ) , nameInput ); // project title
            edit.putInt( getString( R.string.word_count_goal_key ) , wordCount ); // wordCountGoal
            edit.putInt( getString( R.string.word_count_key ) , 0 ); // wordCount (0)
            edit.putInt( getString( R.string.goal_days_key ) , goalDays );

            DateTime today = new DateTime();
            edit.putInt( getString( R.string.start_date_day_key ) , today.getDayOfMonth() ); // day
            edit.putInt( getString( R.string.start_date_month_key ) , today.getMonthOfYear() ); // month
            edit.putInt( getString( R.string.start_date_year_key ) , today.getYear() ); // year
            edit.putInt( getString( R.string.goal_set_start_date_day_key ) , today.getDayOfMonth() ); // goal set day
            edit.putInt( getString( R.string.goal_set_start_date_month_key ) , today.getMonthOfYear() ); // goal set month
            edit.putInt( getString( R.string.goal_set_start_date_year_key ) , today.getYear() ); // goal set year

            edit.putInt( getString( R.string.largest_word_addition_key ) , 0 ); // largest value (0)
            edit.putInt( getString( R.string.average_words_per_day_key ) , 0 ); // average words per day (0)
            edit.putInt( getString( R.string.total_inputs_key ) , 0 ); // totalInput

            edit.putBoolean( getString( R.string.new_project_key ) , false );

            edit.commit();

            // start projectActivity
            Intent intent = new Intent( this , ProjectActivity.class );
            startActivity( intent );
        }

    }

}
