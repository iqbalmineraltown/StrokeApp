package org.ristek.strokeapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;

public class Questions extends Activity {

	private TextView questionText;
	private Button submitButton;
	private RadioButton[] questionButton;
	private int questionId;
	private static Question[] questionList;

    private String readInput(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader( new InputStreamReader(in));
        String         line;
        StringBuilder  stringBuilder = new StringBuilder();
        String         ls = System.getProperty("line.separator");

        while( ( line = reader.readLine() ) != null ) {
            stringBuilder.append( line );
            stringBuilder.append( ls );
        }

        return stringBuilder.toString();
    }

	protected void loadQuestion() {
		try {
            // mengambil isi questions.json
            String content = readInput(getAssets().open("questions.json"));
            JSONArray qArray = (JSONArray) new JSONTokener(content).nextValue();

            questionList = new Question[qArray.length()];
            for (int i=0; i < questionList.length; i++){
                JSONObject q = qArray.getJSONObject(i);
                JSONArray opt = q.optJSONArray("options");
                questionList[i] = new Question(q.getString("question"),
                        opt.getString(0),opt.getString(1),opt.getString(2),opt.getString(3),opt.getString(4),
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
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		questionText = (TextView) findViewById(R.id.textViewQuestion);
		questionButton = new RadioButton[5];
		questionButton[0] = (RadioButton) findViewById(R.id.radio0);
		questionButton[1] = (RadioButton) findViewById(R.id.radio1);
		questionButton[2] = (RadioButton) findViewById(R.id.radio2);
		questionButton[3] = (RadioButton) findViewById(R.id.radio3);
		questionButton[4] = (RadioButton) findViewById(R.id.radio4);

		questionId = getIntent().getIntExtra("QuestionIndex", 0);

        // Jika merupakan pertanyaan pertama maka shuffle pertanyaan
        if(questionId == 0){
            Collections.shuffle(Arrays.asList(questionList));
        }

		questionText.setText(questionList[questionId].question);
		for (int i = 0; i < questionButton.length; i++) {
			questionButton[i].setText(questionList[questionId].answer[i]);
		}

		addListenerOnButton();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void addListenerOnButton() {

		submitButton = (Button) findViewById(R.id.button1);

		OnClickListener clicked = new OnClickListener() {

			@Override
			public void onClick(View v) {
				int answer = 0;
				for (int i = 0; i < questionButton.length; i++) {
					if(questionButton[i].isChecked()) answer = i; 
				}
				Intent resultIntent = new Intent();
				resultIntent
						.putExtra(
								"QuestionResult",
								(answer == questionList[questionId].trueAnswer));
				setResult(RESULT_OK, resultIntent);
				finish();
			}
		};

		submitButton.setOnClickListener(clicked);

	}

}

class Question {
	String question;
	String[] answer;
	int trueAnswer;

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