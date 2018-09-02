package forkbomb.scrambledeggs;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class QuizActivity extends AppCompatActivity {
    //controls what questions will be used for the QuizActivity
    private QuestionHandler questionHandler;
    //controls what question the user is on
    private int questionNumber = 0;

    //view elements
    //changes the text for the question
    TextView question;
    ListView answers;

    //adapter for filling listview
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_downicon512));

        //used for handling questions, answers, and user selections
        questionHandler = new QuestionHandler();

        //sets up activity elements
        question = (TextView) findViewById(R.id.tv_question);
        answers = (ListView) findViewById(R.id.answers);

        //event handling for when item in list view is clicked
        answers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //checks if question is not selected
                if (!questionHandler.quizQuestions[questionNumber].userAnswers.contains(answers.getItemAtPosition(position).toString())) {
                    //switches color, adds to array
                    ((TextView)view).setBackground(getDrawable(R.drawable.listview_entries_selected));
                    ((TextView)view).setTextColor(getResources().getColor(R.color.colorWhite));
                    ((FloatingActionButton)getWindow().getDecorView().findViewById(R.id.button)).setImageResource(R.drawable.nexticon512);
                    questionHandler.quizQuestions[questionNumber].userAnswers.add(answers.getItemAtPosition(position).toString());
                }
                //checks if question is selected
                else {
                    //switches color, removes from array
                    view.setBackgroundColor(Color.TRANSPARENT);
                    ((TextView)view).setTextColor(getResources().getColor(R.color.colorBlack));
                    ((TextView)view).setBackground(getDrawable(R.drawable.listview_entries_default));
                    questionHandler.quizQuestions[questionNumber].userAnswers.remove(answers.getItemAtPosition(position).toString());
                    if (questionHandler.quizQuestions[questionNumber].userAnswers.size() == 0)
                        ((FloatingActionButton)getWindow().getDecorView().findViewById(R.id.button)).setImageResource(R.drawable.refreshicon512);
                }
            }
        });
        generateAnswers();
        handleQuiz();
    }

    public void generateAnswers(){
        for (int i = 0; i < 4; i++)
            questionHandler.quizQuestions[questionNumber].displayedAnswers[i] = questionHandler.generateAnswer(questionNumber);
    }

    //controls QuizActivity
    public void handleQuiz(){
        question.setText(questionHandler.quizQuestions[questionNumber].question);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, questionHandler.quizQuestions[questionNumber].displayedAnswers){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextSize(21);
                textView.setTextAlignment(convertView.TEXT_ALIGNMENT_CENTER);
                textView.setBackground(getContext().getDrawable(R.drawable.listview_entries_default));
                return textView;
            }
        };
        answers.setAdapter(adapter);
    }

    //handles button press
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button:
                //resets displayed answers array
                questionHandler.quizQuestions[questionNumber].displayedAnswers = new String[4];
                Log.i("t",Integer.toString(questionHandler.quizQuestions[questionNumber].userAnswers.size()));
                if (questionHandler.quizQuestions[questionNumber].userAnswers.size() > 0) {
                    ((FloatingActionButton)getWindow().getDecorView().findViewById(R.id.button)).setImageResource(R.drawable.refreshicon512);
                    //increments question number
                    if (questionNumber < getIntent().getIntExtra("length", 2) - 1)
                        questionNumber++;
                    else questionNumber = 0;
                    //generates new answers
                    for (int i = 0; i < 4; i++)
                        questionHandler.quizQuestions[questionNumber].displayedAnswers[i] = questionHandler.generateAnswer(questionNumber);
                }
                else {
                    for (int i = 0; i < 4; i++) {
                        questionHandler.quizQuestions[questionNumber].displayedAnswers[i] = questionHandler.generateAnswer(questionNumber);
                    }
                }

                handleQuiz();
                break;
            default:
                break;
        }
    }

    //navbar back button press
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    //back button press event
    @Override
    public void onBackPressed() {
        //kills the activity
        finish();
    }

}
