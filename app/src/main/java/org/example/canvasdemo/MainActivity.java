package org.example.canvasdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.example.canvasdemo.model.MovementDirection;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.RunnableFuture;

public class MainActivity extends Activity implements OnClickListener{
	
	MyView myView;
	private final int STEP = 10;

	private Timer timer;
	private int counter = 0;
	TextView textView, scoreView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Define the buttons
		Button buttonRight = (Button) findViewById(R.id.rightButton);
		Button buttonLeft = (Button) findViewById(R.id.leftButton);
		Button buttonUp = (Button) findViewById(R.id.upButton) ;
		Button buttonDown = (Button) findViewById(R.id.downButton);
		textView = (TextView) findViewById(R.id.textView);
		scoreView = (TextView) findViewById(R.id.pointsTextView);

		myView = (MyView) findViewById(R.id.gameView);

		//listeners of our pacman controls
		buttonRight.setOnClickListener(this);
		buttonLeft.setOnClickListener(this);
		buttonUp.setOnClickListener(this);
		buttonDown.setOnClickListener(this);

		// Initialize the timer
		timer = new Timer();
		myView.running = true;

		TimerTask tt = new TimerTask() {
			@Override
			public void run() {
				TimerMethod();
			}
		};

		TimerTask tt2 = new TimerTask() {
			@Override
			public void run() {
				GameTimerMethod();
			}
		};

		// Movement Timer
		timer.schedule(tt, 0, 100);
		// Game Time Timer
		timer.schedule(tt2, 0, 1000);
	}

	private void TimerMethod() {
		this.runOnUiThread(Timer_Tick);
	}

	private void GameTimerMethod() {
		this.runOnUiThread(GameTimerTick);
	}

	private Runnable Timer_Tick = new Runnable() {
		@Override
		public void run() {
			if(myView.running) {
				counter++;
				myView.move(30);
				scoreView.setText("Score: " + myView.score);
			}
		}
	};

	private Runnable GameTimerTick = new Runnable() {
		@Override
		public void run() {
			if(myView.running) {
				myView.time--;
				textView.setText("Time: " + myView.time);

				//GameOver
				if(myView.time == 0)
				{
					AlertDialog alertDialog = new AlertDialog.Builder(myView.getContext()).create();

					alertDialog.setTitle("Whoops!");
					alertDialog.setIcon(R.mipmap.ic_launcher);
					alertDialog.setCanceledOnTouchOutside(false);
					alertDialog.setMessage("Looks like you need more pratice.");
					alertDialog.setButton("Okay..", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							myView.restartGame();
							myView.running = true;
							Toast.makeText(myView.getContext(), "Good Luck!", Toast.LENGTH_SHORT).show();
						}
					});
					myView.running = false;
					alertDialog.show();
				}
			}
		}
	};

	@Override
	protected void onStop() {
		super.onStop();

		timer.cancel();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_restart) {
			counter = 0;
			textView.setText("Timer Value: " + counter);
			myView.restartGame();
		}
		if (id == R.id.action_pause) {
			if(myView.running) {
				myView.running = false;
				item.setIcon(R.drawable.ic_play_button);
				System.out.println("Game paused.");
			} else if(!myView.running){
				myView.running = true;
				item.setIcon(R.drawable.ic_pause_button);
				System.out.println("Game resumed.");
			}
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.leftButton) {
			myView.movementDirection = MovementDirection.LEFT;
		}
		if(view.getId() == R.id.rightButton) {
			myView.movementDirection = MovementDirection.RIGHT;
		}
		if(view.getId() == R.id.upButton) {
			myView.movementDirection = MovementDirection.UP;
		}
		if(view.getId() == R.id.downButton) {
			myView.movementDirection = MovementDirection.DOWN;
		}
	}
}
