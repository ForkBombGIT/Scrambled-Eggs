package forkbomb.scrambledeggs;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Quiz extends Fragment implements View.OnClickListener {
    //controls if button can be pressed
    boolean buttonPress = false;
    //random number gen
    final java.util.Random rnd = new Random();
    //games
    ArrayList<Game> database;
    //used to display eggs
    GridLayout imgGrid;
    final ImageView[] eggs = new ImageView[12];
    //holds length of quiz
    int quizLength;
    //possible flavor text
    TextView flavorText;
    final String[] flavor = {
            "How many questions do you want?",
            "How big do you want your omelette?",
            "How many eggs do you wanna scramble?"
    };

    public Quiz() {
        // Required empty public constructor
    }

    public static Quiz newInstance(ArrayList<Game> games) {
        Quiz fragment = new Quiz();
        Bundle bundle = new Bundle();
        bundle.putSerializable("games", games);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        database = (ArrayList<Game>) Objects.requireNonNull(args).getSerializable("games");
    }

    @Override
    public void onResume() {
        super.onResume();
        buttonPress = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //sets random flavor text
        flavorText = view.findViewById(R.id.flavor);
        flavorText.setText(flavor[rnd.nextInt(flavor.length)]);

        //sets image view array
        imgGrid = view.findViewById(R.id.imggrid);
        for (int i = 0; i < imgGrid.getChildCount(); i++){
            eggs[i] = (ImageView) imgGrid.getChildAt(i);
            eggs[i].setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getActivity()),R.color.itemBorder));
        }

        //sets up seek bar
        SeekBar seekBar = view.findViewById(R.id.wdg_seek);
        seekBar.setProgress(0);
        seekBar.incrementProgressBy(1);
        seekBar.setMax(11);
        quizLength = 2;

        for (int i = 0; i < quizLength;i++){
            eggs[i].setColorFilter(Color.TRANSPARENT);
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //seekVal.setText(String.valueOf(progress + 2));
                quizLength = ++progress;
                for (int i = 0; i < quizLength; i++){
                    eggs[i].setColorFilter(Color.TRANSPARENT);
                }
                for (int i = quizLength; i < 12; i++){
                    eggs[i].setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getActivity()),R.color.itemBorder));
                }
            }
        });
    }

    //handles button press
    public void onClick(View v){
        if (v.getId() == R.id.button) {
            if (!buttonPress) {
                activityStart(QuizActivity.class);
                buttonPress = true;
            }
        }
    }

    //starts an activity
    public void activityStart(Class t){
        if (database.size() > 0) {
            //creates a new intent
            Intent intent = new Intent(getActivity(), t);
            //creates a bundle to send
            Bundle bundle = new Bundle();
            bundle.putSerializable("database", database);
            intent.putExtra("database", bundle);
            intent.putExtra("length",quizLength);
            //starts the new activity
            startActivity(intent);
        }
    }
}
