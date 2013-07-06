package org.ristek.strokeapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.ristek.strokeapp.support.ClockTimer;
import org.ristek.strokeapp.support.GameMode;
import org.ristek.strokeapp.support.SaveManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;

public class LevelQuestion extends Activity implements ClockTimer.TimerListener {

    private final long QUESTION_TIME = 120000;

    private TextView questionText;
    TextView timeText;
    private Button submitButton;
    private RadioButton[] questionButton;
    public int questionId;
    public static Question[] questionList;
    private ClockTimer timer;

    private String readInput(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }

        return stringBuilder.toString();
    }

    protected void loadQuestion() {
        try {
            // mengambil isi questions.json
            String content = readInput(getAssets().open("questions.json"));
            JSONArray qArray = (JSONArray) new JSONTokener(content).nextValue();

            questionList = new Question[qArray.length()];
            for (int i = 0; i < questionList.length; i++) {
                JSONObject q = qArray.getJSONObject(i);
                JSONArray opt = q.optJSONArray("options");
                questionList[i] = new Question(q.getString("question"),
                        opt.getString(0), opt.getString(1), opt.getString(2), opt.getString(3), opt.getString(4),
                        q.getInt("answer-index"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* remove title bar and notification bar */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /* end remove title bar and notification bar */

        if (questionList == null)
            loadQuestion();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_page);

        questionText = (TextView) findViewById(R.id.textViewQuestion);
        questionButton = new RadioButton[5];
        questionButton[0] = (RadioButton) findViewById(R.id.radio0);
        questionButton[1] = (RadioButton) findViewById(R.id.radio1);
        questionButton[2] = (RadioButton) findViewById(R.id.radio2);
        questionButton[3] = (RadioButton) findViewById(R.id.radio3);
        questionButton[4] = (RadioButton) findViewById(R.id.radio4);

        for (RadioButton choice : questionButton) {
            choice.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (RadioButton choice : questionButton) {
                        if (choice != view) {
                            choice.setChecked(false);
                        }
                    }
                }
            });
        }

        questionId = getIntent().getIntExtra("QuestionIndex", 0);

        // Jika merupakan pertanyaan pertama maka shuffle pertanyaan
        if (questionId == 0) {
            Collections.shuffle(Arrays.asList(questionList));
        }
        Log.d("LevelQuestion", "indeks jawaban: " + questionList[questionId].trueAnswer);

        questionText.setText(questionList[questionId].question);
        for (int i = 0; i < questionButton.length; i++) {
            questionButton[i].setText(questionList[questionId].answer[i]);
        }

        timeText = (TextView) findViewById(R.id.questionTimeText);
        if (SaveManager.getMode() == GameMode.NORMAL) timeText.setVisibility(View.INVISIBLE);
        else {
            timer = new ClockTimer(this);
            timer.setTimeLeft(QUESTION_TIME);
            timeText.setText(ClockTimer.timeToString(QUESTION_TIME, ClockTimer.MM_SS));
            timer.start();
        }

        addListenerOnButton();
    }

    public void addListenerOnButton() {

        submitButton = (Button) findViewById(R.id.button1);

        OnClickListener clicked = new OnClickListener() {

            @Override
            public void onClick(View v) {
                int answer = -1;
                for (int i = 0; i < questionButton.length; i++) {
                    if (questionButton[i].isChecked()) answer = i;
                }
                if (answer > -1) {
                    Intent resultIntent = new Intent();
                    resultIntent
                            .putExtra(
                                    "QuestionResult",
                                    (answer == questionList[questionId].trueAnswer));
                    if (SaveManager.getMode() == GameMode.TIME_TRIAL)
                        resultIntent.putExtra("time", QUESTION_TIME - timer.getTimeLeft());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        };

        submitButton.setOnClickListener(clicked);
    }

    @Override
    public void onTimerUpdate(long timeLeft) {
        timeText.setText(ClockTimer.timeToString(timeLeft, ClockTimer.MM_SS));
        if (timeLeft == 0) {
            timer.stop();
            Intent resultIntent = new Intent();
            resultIntent
                    .putExtra(
                            "QuestionResult", false);
            resultIntent.putExtra("time", QUESTION_TIME);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SaveManager.getMode() == GameMode.TIME_TRIAL) {
            timer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (SaveManager.getMode() == GameMode.TIME_TRIAL) {
            timer.stop();
        }
    }


    public class Question {
        public String question;
        public String[] answer;
        public int trueAnswer;

        Question(String question, String answer0, String answer1, String answer2,
                 String answer3, String answer4, int trueAnswer) {
            this.question = question;
            this.answer = new String[5];
            this.answer[0] = answer0;
            this.answer[1] = answer1;
            this.answer[2] = answer2;
            this.answer[3] = answer3;
            this.answer[4] = answer4;
            this.trueAnswer = trueAnswer;
        }
    }
}

