package org.ristek.strokeapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

public class Questions extends Activity {

	private TextView questionText;
	private Button submitButton;
	private RadioButton[] questionButton;
	private int questionId;
	private static Question[] questionList;

	protected void loadQuestion() {
		try {
			BufferedReader file = new BufferedReader(new InputStreamReader(
					getAssets().open("question.txt")));
			int sum = Integer.parseInt(file.readLine());
			questionList = new Question[sum];
			for (int i = 0; i < sum; i++) {
				questionList[i] = new Question(file.readLine(),
						file.readLine(), file.readLine(), file.readLine(),
						file.readLine(), file.readLine(), Integer.parseInt(file
								.readLine()));
			}
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
		// TODO Auto-generated method stub
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