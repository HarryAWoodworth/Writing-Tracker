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
import org.joda.time.Days;
import org.w3c.dom.Text;

import static com.blueshroom.harry.writingtrackerapplication.AddWordsActivity.MAX;
import static com.blueshroom.harry.writingtrackerapplication.ProjectActivity.INT_ERROR;

public class SetGoalDaysActivity extends AppCompatActivity
{
    SharedPreferences value_restore = getSharedPreferences( getString( R.string.preference_key ) , 0 );
    SharedPreferences.Editor edit = value_restore.edit();

    // TextView for instructions
    TextView mSGDAInstructions;

    // EditText for input
    EditText mSGDAEditText;

    // Back Button
    Button mSGDABackButton;

    // Confirm Button
    Button mSGDAConfirmButton;

    // TextView for current goalDays
    TextView mSGDAGoalDays;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goal_days);

        setUI();

        setDisplay();

    }

    private void setUI()
    {
        mSGDAInstructions = ( TextView ) findViewById( R.id.msgda_instructions );
        mSGDAEditText = ( EditText ) findViewById( R.id.msgda_edit_text );
        mSGDABackButton = ( Button ) findViewById( R.id.msgda_back_button );
        mSGDAConfirmButton = ( Button ) findViewById( R.id.msgda_confirm_button );
        mSGDAGoalDays = ( TextView ) findViewById( R.id.msgda_goal_days );
    }

    private void setDisplay()
    {

       DateTime goalSetStartDate = new DateTime(
                 value_restore.getInt( getString( R.string.goal_set_start_date_year_key ) , INT_ERROR )
               , value_restore.getInt( getString( R.string.goal_set_start_date_month_key ) , INT_ERROR )
               , value_restore.getInt( getString( R.string.goal_set_start_date_day_key ) , INT_ERROR )
               , 0 , 0 , 0 );

        int daysPassed = Days.daysBetween( goalSetStartDate.withTimeAtStartOfDay() , new DateTime().withTimeAtStartOfDay() ).getDays();

        int remaining_goal_days = value_restore.getInt( getString( R.string.goal_days_key ) , INT_ERROR ) - daysPassed;

        String setText = R.string.msgda_goal_days + "" + remaining_goal_days;
        mSGDAGoalDays.setText( setText );
    }

    public void confirm_pressed( View view)
    {
        // Get input
        int input = Integer.parseInt( mSGDAEditText.getText().toString() );

        // Check input
        if( input <= 0 )
        {
            Toast.makeText( SetGoalDaysActivity.this , "" + R.string.toast_input_low , Toast.LENGTH_SHORT ).show();
            mSGDAEditText.setText( "" );
            return;
        }
        if( input >= MAX )
        {
            Toast.makeText( SetGoalDaysActivity.this , "" + R.string.toast_input_max + MAX , Toast.LENGTH_SHORT ).show();
            mSGDAEditText.setText( "" );
            return;
        }

        // Edit mSGDAGoalDays
        String setText = R.string.msgda_goal_days + "" + input;
        mSGDAGoalDays.setText( setText );

        // Set variables in value_restore
        DateTime now = new DateTime();

        edit.putInt( getString( R.string.goal_days_key ) , input );
        edit.putInt( getString( R.string.goal_set_start_date_year_key ) , now.getYear() );
        edit.putInt( getString( R.string.goal_set_start_date_month_key ) , now.getMonthOfYear() );
        edit.putInt( getString( R.string.goal_set_start_date_day_key ) , now.getDayOfMonth() );
        edit.commit();

        // Toast confirmation

        DateTime goalSetStartDate = new DateTime(
                value_restore.getInt( getString( R.string.goal_set_start_date_year_key ) , INT_ERROR )
                , value_restore.getInt( getString( R.string.goal_set_start_date_month_key ) , INT_ERROR )
                , value_restore.getInt( getString( R.string.goal_set_start_date_day_key ) , INT_ERROR )
                , 0 , 0 , 0 );

        int daysPassed = Days.daysBetween( goalSetStartDate.withTimeAtStartOfDay() , new DateTime().withTimeAtStartOfDay() ).getDays();

        int remaining_goal_days = value_restore.getInt( getString( R.string.goal_days_key ) , INT_ERROR ) - daysPassed;

        Toast.makeText( SetGoalDaysActivity.this , "" + R.string.toast_set_goal_days_confirm + remaining_goal_days , Toast.LENGTH_SHORT ).show();
        mSGDAEditText.setText( "" );
    }

    public void back_pressed( View view )
    {
        Intent intent = new Intent( this , ProjectActivity.class );
        startActivity( intent );
    }

}
