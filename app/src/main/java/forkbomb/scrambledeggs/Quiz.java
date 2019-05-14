package forkbomb.scrambledeggs;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class Quiz extends Fragment {
    //random number gen
    java.util.Random rnd = new Random();
    //games
    ArrayList<Game> database;
    //used to display eggs
    GridLayout imgGrid;
    ImageView[] eggs = new ImageView[12];
    //holds length of quiz
    int quizLength;

    //possible flavor text
    TextView flavorText;
    String[] flavor = {
            "How many questions do you want?",
            "How big do you want your omelette?",
    };

    public Quiz() {
        // Required empty public constructor
    }

    public static Quiz newInstance() {
        Quiz fragment = new Quiz();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //sets random flavor text
        flavorText = view.findViewById(R.id.flavor);
        flavorText.setText(flavor[rnd.nextInt(flavor.length)]);

        //sets imageview array
        imgGrid = view.findViewById(R.id.imggrid);
        for (int i = 0; i < imgGrid.getChildCount(); i++){
            eggs[i] = (ImageView) imgGrid.getChildAt(i);
            eggs[i].setColorFilter(getResources().getColor(R.color.itemBorder));
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
                    eggs[i].setColorFilter(getResources().getColor(R.color.itemBorder));
                }
            }
        });
    }
}
