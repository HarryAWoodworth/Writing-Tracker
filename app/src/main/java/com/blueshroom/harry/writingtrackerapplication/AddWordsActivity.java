/**
 * Written 4/5/2017
 * Add a TextView to show total words completed
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

import static com.blueshroom.harry.writingtrackerapplication.ProjectActivity.INT_ERROR;

public class AddWordsActivity extends AppCompatActivity
{
    public static final int MAX = Integer.MAX_VALUE;
    private int wordsAdded = 0, wordCount;

    // TextView for instructions
    TextView mInstructionTextView;

    // TextView for words added
    TextView mTotalWordsAddedTextView;

    // TextView for words removed
    TextView mWordsRemovedTextView;

    // TextView for word count
    TextView mWordCount;

    // EditText
    EditText mWordEditText;

    // Back Button
    Button mBackButton;

    // AddWords Button
    Button mAddWordsButton;

    // Undo Button
    Button mUndoButton;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_words );

        SetUI();
    }

    private void SetUI()
    {
        mWordCount = ( TextView ) findViewById( R.id.total_words_completed_view_text );
        mInstructionTextView = ( TextView ) findViewById( R.id.instruction_text_view );
        mTotalWordsAddedTextView = ( TextView ) findViewById( R.id.total_words_added_text_view );
        mWordsRemovedTextView = ( TextView ) findViewById( R.id.words_removed_text_view );
        mWordEditText = ( EditText ) findViewById( R.id.word_edit_text );
        mBackButton = ( Button ) findViewById( R.id.back_button );
        mAddWordsButton = ( Button ) findViewById( R.id.add_words_button );
        mUndoButton = ( Button ) findViewById( R.id.undo_button );

        String totalWordsAddedSetText = "" + getString(R.string.total_words_added_text_view) + " " + "0";
        mTotalWordsAddedTextView.setText( totalWordsAddedSetText );

        String totalWordsRemovedSetText = "" + getString(R.string.words_removed_text_view) + " " + "0";
        mWordsRemovedTextView.setText( totalWordsRemovedSetText );

    }

    // Gets input from the editText , checks 0 < input < MAX , adds to wordCount , and changes largestWordAddition is applicable
    public void addWords( View view )
    {
        // Get input
        int input = Integer.parseInt( mWordEditText.getText().toString() );

        // Check input, display toasts for input error
        if( input <= 0 )
        {
            Toast.makeText( AddWordsActivity.this , getString(R.string.toast_input_low) , Toast.LENGTH_SHORT ).show();
            mWordEditText.setText( "" );
            return;
        }
        if ( input >= MAX )
        {
            Toast.makeText( AddWordsActivity.this , "" + getString(R.string.toast_input_max + MAX) , Toast.LENGTH_SHORT ).show();
            mWordEditText.setText( "" );
            return;
        }

        SharedPreferences value_restore = getSharedPreferences( getString( R.string.preference_key ) , 0 );
        SharedPreferences.Editor edit = value_restore.edit();

        int largestWordAddition = value_restore.getInt( getString( R.string.largest_word_addition_key ) , INT_ERROR );

        // If word addition is the largest, update largestWordAddition and show a toast
        if( input > largestWordAddition )
        {
            largestWordAddition = input;
            edit.putInt( getString( R.string.largest_word_addition_key ) , input);
            edit.commit();
            Toast.makeText( AddWordsActivity.this , "" + getString(R.string.largest_word_addition_toast) + largestWordAddition , Toast.LENGTH_SHORT ).show();
        }

        // Add to wordsAdded
        wordsAdded += input;

        // Retrieve wordCount
        wordCount = value_restore.getInt( getString( R.string.word_count_key ) , INT_ERROR );

        // Replace wordCount in preferences with wordCount + input
        wordCount += wordsAdded;
        edit.putInt( getString( R.string.word_count_key ) , wordCount );
        edit.commit();

        // Display a toast of total words added
        Toast.makeText( AddWordsActivity.this , getString( R.string.toast_input1) + wordsAdded + getString(R.string.toast_input2) , Toast.LENGTH_LONG ).show();

        // Set TextView
        String totalWordsAddedSetText = "" + getString(R.string.total_words_added_text_view) + wordsAdded;
        mTotalWordsAddedTextView.setText( totalWordsAddedSetText );

        // Set TextView 2
        String setText = "" + getString(R.string.word_count_text) + value_restore.getInt( getString( R.string.word_count_key ) , INT_ERROR );
        mWordCount.setText( setText );

        int totalInputs = value_restore.getInt( getString( R.string.total_inputs_key ) , INT_ERROR);
        totalInputs++;
        edit.putInt( getString( R.string.total_inputs_key ) , totalInputs );
        edit.commit();
        int avgWords = value_restore.getInt( getString( R.string.average_words_per_day_key ) , INT_ERROR );

        if(avgWords == 0)
        {
            avgWords = wordsAdded;
            edit.putInt( getString( R.string.average_words_per_day_key ) , avgWords );
            edit.commit();
        } else {
            int num = (int) (avgWords * (totalInputs - 1));
            num += wordsAdded;
            avgWords = num / totalInputs;
            edit.putInt(getString(R.string.average_words_per_day_key), (int) avgWords);
            edit.commit();
        }

    }

    // Subtracts wordsAdded from wordCount
    public void undoWordAddition( View view )
    {
        SharedPreferences value_restore = getSharedPreferences( getString( R.string.preference_key ) , 0 );
        SharedPreferences.Editor edit = value_restore.edit();

        // Subtract all the words added in this session
        wordCount = value_restore.getInt( getString( R.string.word_count_key ) , INT_ERROR );
        wordCount -= wordsAdded;
        edit.putInt( getString( R.string.word_count_key ) ,  wordCount );
        edit.commit();

        // Toast
        Toast.makeText( AddWordsActivity.this , getString(R.string.toast_undo) + wordsAdded + getString(R.string.toast_input2) , Toast.LENGTH_LONG ).show();

        // Set TextView
        String totalWordsRemovedSetText = "" + getString(R.string.words_removed_text_view) + wordsAdded;
        mWordsRemovedTextView.setText( totalWordsRemovedSetText );

        // Reset wordsAdded
        wordsAdded = 0;

    }

    // Goes back to ProjectActivity and increment totalInputs, change averageWordsPerDay
    public void back_pressed( View view )
    {
        /**
        SharedPreferences value_restore = getSharedPreferences( getString( R.string.preference_key ) , 0 );
        SharedPreferences.Editor edit = value_restore.edit();

        if( wordsAdded > 0 )
        {
            int totalInputs = value_restore.getInt( getString( R.string.total_inputs_key ) , INT_ERROR);
            totalInputs++;
            edit.putInt( getString( R.string.total_inputs_key ) , totalInputs );
            edit.commit();
            int avgWords = value_restore.getInt( getString( R.string.average_words_per_day_key ) , INT_ERROR );

            if(avgWords == 0)
            {
                avgWords = wordsAdded;
                edit.putInt( getString( R.string.average_words_per_day_key ) , avgWords );
                edit.commit();
            } else {
                int num = (int) (avgWords * (totalInputs - 1));
                num += wordsAdded;
                avgWords = num / totalInputs;
                edit.putInt(getString(R.string.average_words_per_day_key), (int) avgWords);
                edit.commit();
            }
        }
 **/
        Intent intent = new Intent( this , ProjectActivity.class );
        startActivity( intent );
    }

}
