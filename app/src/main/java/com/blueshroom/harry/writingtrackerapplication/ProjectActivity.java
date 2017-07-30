/**
 * Written by Harry Woodworth
 * Started 3/20/2017
 *
 *
 * Ideas to add:
 *  - Cool fonts
 *  - Notifications during the evening if the user hasn't submitted words
 *      - Options screen to change said notifications
 *  - Multiple Projects
 *  - Recorded history of word additions
 *      - Be able to save that history to the device in pdf or something
 *      - Share work history with social media
 *          - Share total words with social media
 *  - A page that links to writing helpful resources on the net
 *  - Remove word additions
 *  - Add word additions to any day in the past
 *  - A setting that allows the user to display certain information
 *  - An "About" page
 *      - A "Donate" Button on the page (Unless you have some other form of microtransaction)
 *  - An intro screen
 *  - Add toasts for milestones
 *  - Allow user to select a date, then return what the average words per day would need to be in order to complete the goal word count by that date
 *
 * Ideas you need to implement:
 *  - New project input screen
 *  - reset project button
 *
 */

package com.blueshroom.harry.writingtrackerapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import static android.R.id.edit;

public class ProjectActivity extends AppCompatActivity
{

    // SharedPreferences stuff
    public static final String ERROR = "ERROR";
    public static final int INT_ERROR = -1;

    // Name of the project and the TextView displaying the project title
    private TextView mProjectTitle;
    private String projectTitle;

    // TextView that shows the percentage of the goal word count completed
    private TextView mPercentCompleted;
    private int percentCompleted;

    // ProgressBar that displays the current progress based on the user's word goal and number of words they've completed
    private ProgressBar mProgressBar;

    // Clickable ( to edit ) TextView and number of words set as the goal
    private TextView mWordCountGoal;
    private int wordCountGoal;

    ///////////////////// Implement /////////////////////

    // TextView for user's word count
    private TextView mWordCount;
    private int wordCount;

    // Clickable ( to edit ) TextView for how many days the User wants to finish in
    private TextView mGoalDays;
    private int goalDays;

    // TextView for the predicted number of days to finish based on averageWordsPerDay
    private TextView mPredictedDays;

    // TextView to display the averageWordsPerDay, and totalDays variable to be updated every day
    private TextView mAverageWordsPerDay;
    private int averageWordsPerDay;
    private int totalInputs;

    // TextView to display the averageWordsPerDayToCompleteByGoalDate
    private TextView mAverageWordsPerDayToCompleteByGoalDate;

    // TextView to display the date started, is only set once during new project setup
    private TextView mStartDate;
    private DateTime startDate;
    int startDateYear;
    int startDateMonth;
    int startDateDay;

    // TextView to display the largest word addition, possibly updated on every user word addition
    private TextView mLargestWordAddition;
    private int largestWordAddition;

    // Button that opens WordAdditionActivity
    private Button mWordAdditionButton;

    // Boolean to see if it is a new project
    boolean newProject;

    // Calender to hold the day that the most recent goal was set
    // begins as the startDate, changed every setGoalDays()
    private DateTime goalSetStartDate;
    int goalSetStartDateYear;
    int goalSetStartDateMonth;
    int goalSetStartDateDay;

    // Variable to hold how many days have passed since the goalDays was last set
    int daysPassed;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        // onCreate stuff, setting the view to this activity
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_project );

        // The sharedPreferences to get values from
        SharedPreferences value_restore = getSharedPreferences( getString( R.string.preference_key ) , 0 );
        SharedPreferences.Editor edit = value_restore.edit();

        //edit.putBoolean( getString( R.string.new_project_key ) , true );

        edit.commit();

        // Set up the instances of the UI
        setUI();

        newProject = value_restore.getBoolean( getString( R.string.new_project_key ) , true );

        // If newProject, start newProjectActivity
        if( newProject )
        {
            Intent intent = new Intent( this , newProjectActivity.class );
            startActivity( intent );
        }
        // Else return all values to those in the SharedPreferences
        else
        {
            // Set variables based on stored info on the user's device
            retrieveVariables(value_restore);

            // Set the UI based on the variables
            setDisplay();
        }
    }

    // Set the UI
    private void setUI()
    {
        mProjectTitle = ( TextView ) findViewById( R.id.project_title );
        mPercentCompleted = ( TextView ) findViewById( R.id.percent_completed );
        mProgressBar = ( ProgressBar ) findViewById( R.id.progress_bar );
        mWordCountGoal = ( TextView ) findViewById( R.id.word_count_goal );
        mWordCount = ( TextView ) findViewById( R.id.word_count );
        mGoalDays = ( TextView ) findViewById ( R.id.goal_days );
        mPredictedDays = ( TextView ) findViewById ( R.id.predicted_days );
        mAverageWordsPerDay = ( TextView ) findViewById ( R.id.average_words_per_day );
        mAverageWordsPerDayToCompleteByGoalDate = ( TextView ) findViewById ( R.id.average_words_per_day_needed_to_meet_goal );
        mStartDate = ( TextView ) findViewById ( R.id.start_date );
        mLargestWordAddition = ( TextView ) findViewById ( R.id.largest_word_addition );
        mWordAdditionButton = ( Button ) findViewById ( R.id.word_addition_button );
    }

    private void retrieveVariables( SharedPreferences value_restore )
    {
        projectTitle = value_restore.getString( getString( R.string.project_title_key ) , ERROR );
        wordCountGoal = value_restore.getInt( getString( R.string.word_count_goal_key ) , INT_ERROR );
        wordCount = value_restore.getInt( getString( R.string.word_count_key ) , INT_ERROR );
        startDateYear = value_restore.getInt( getString( R.string.start_date_year_key ), INT_ERROR );
        startDateMonth = value_restore.getInt( getString( R.string.start_date_month_key ) , INT_ERROR );
        startDateDay = value_restore.getInt( getString( R.string.start_date_day_key ) , INT_ERROR );
        startDate = new DateTime( startDateYear , startDateMonth , startDateDay , 0 , 0 , 0 );
        largestWordAddition = value_restore.getInt( getString( R.string.largest_word_addition_key ), INT_ERROR );
        totalInputs = value_restore.getInt( getString( R.string.total_inputs_key ) , INT_ERROR );
        averageWordsPerDay = value_restore.getInt( getString( R.string.average_words_per_day_key ) , INT_ERROR );
        goalSetStartDateYear = value_restore.getInt( getString( R.string.goal_set_start_date_year_key ) , INT_ERROR );
        goalSetStartDateMonth = value_restore.getInt( getString( R.string.goal_set_start_date_month_key ) , INT_ERROR );
        goalSetStartDateDay = value_restore.getInt( getString( R.string.goal_set_start_date_day_key ) , INT_ERROR );

        goalSetStartDate = new DateTime( goalSetStartDateYear , goalSetStartDateMonth , goalSetStartDateDay , 0 , 0 , 0 );

        goalDays = value_restore.getInt( getString( R.string.goal_days_key ) , INT_ERROR );
    }

    // Calculate variables and set the activity to display values
    private void setDisplay()
    {
        daysPassed = Days.daysBetween( goalSetStartDate.withTimeAtStartOfDay() , new DateTime().withTimeAtStartOfDay() ).getDays();

        percentCompleted = (int)(( (double )wordCount / (double)wordCountGoal ) * 100.0);
        int averageWordsPerDayToCompleteByGoalDate = ( wordCountGoal - wordCount ) / ( goalDays - daysPassed ) ;

        int predictedDays;
        if(averageWordsPerDay != 0) predictedDays = ( int )( ( wordCountGoal - wordCount ) / averageWordsPerDay );
        else predictedDays = -1;

        // Set the name of the project to String project_title
        mProjectTitle.setText( projectTitle );

        // Set mPercentCompleted, add exclamation points at milestones
        if( percentCompleted >= 90 )
            mPercentCompleted.setText( "" + percentCompleted + getString(R.string.percent_completed_text4) );
        else if( percentCompleted >= 75 )
            mPercentCompleted.setText( "" + percentCompleted + getString(R.string.percent_completed_text3) );
        else if( percentCompleted >= 50 )
            mPercentCompleted.setText( "" + percentCompleted + getString(R.string.percent_completed_text2) );
        else if( percentCompleted >= 25 )
            mPercentCompleted.setText( "" + percentCompleted + getString(R.string.percent_completed_text1) );
        else
            mPercentCompleted.setText( "" + percentCompleted + getString(R.string.percent_completed_text) );

        // Initialize the mProgress Bar with 100 as the max (Calculating the percent complete out of 100%)
        // Set the progress percentCompleted
        mProgressBar.setMax( 100 );
        mProgressBar.setProgress( percentCompleted );

        // Set mWordCountGoal
        String setText = "" + getString(R.string.word_count_goal_text) + " " + wordCountGoal;
        mWordCountGoal.setText( setText );

        // predicted days
        String predictedDaysSetText = "" + getString(R.string.predicted_text) + " " + predictedDays;
        mPredictedDays.setText( predictedDaysSetText );

        // goal days
        String goalDaysSetText = "" + getString(R.string.goal_text) + " " + ( goalDays - daysPassed );
        mGoalDays.setText( goalDaysSetText );

        // average words per day
        String averageWordsSetText = "" + getString(R.string.average_text) + " " + averageWordsPerDay;
        mAverageWordsPerDay.setText( averageWordsSetText );

        // avg +
        String awpdtcbgdSetText = "" + getString(R.string.average_plus_text) + " " + averageWordsPerDayToCompleteByGoalDate;
        mAverageWordsPerDayToCompleteByGoalDate.setText( awpdtcbgdSetText );

        // start date
        String dateStartedSetText = "" + getString(R.string.date_text) + " " + startDateMonth + "/" + startDateDay + "/" + startDateYear;
        mStartDate.setText( dateStartedSetText );

        // largest
        String largestSetText = "" + getString(R.string.largest_text) + " " + largestWordAddition;
        mLargestWordAddition.setText( largestSetText );

        // word count
        String wordCountSetText = "" + getString(R.string.word_count_text) + " " + wordCount;
        mWordCount.setText( wordCountSetText );
    }

    // Activates when mWordCountGoalText is pressed
    // Open a window activity where the user inputs a new word count, set wordCountTotal to that input
    public void setWordCountGoal( View v )
    {
        Intent intent = new Intent( this , SetWordCountGoalActivity.class );
        startActivity( intent );
    }

    // Activates when mGoalDate is pressed
    // Open a window activity where the user inputs a new date, set goalDate to that input
    public void setGoalDays( View v )
    {
        Intent intent = new Intent( this , SetGoalDaysActivity.class );
        startActivity( intent );
    }

    public void addWords( View v )
    {
        Intent intent = new Intent( this , AddWordsActivity.class );
        startActivity( intent );
    }

    public void reset( View v )
    {
        SharedPreferences value_restore = getSharedPreferences( getString( R.string.preference_key ) , 0 );
        SharedPreferences.Editor edit = value_restore.edit();
        edit.putBoolean( getString( R.string.new_project_key ) , true );
        edit.commit();
    }

}
