package org.example.canvasdemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.example.canvasdemo.model.BitmapHelper;
import org.example.canvasdemo.model.CollisionDetector;
import org.example.canvasdemo.model.DifficultyLevel;
import org.example.canvasdemo.model.Enemy;
import org.example.canvasdemo.model.EnemyType;
import org.example.canvasdemo.model.GoldCoin;
import org.example.canvasdemo.model.MovementDirection;

import java.util.ArrayList;
import java.util.Random;

public class MyView extends View{
	
	Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pacman);
	Bitmap goldCoinBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.goldcoin);
	Bitmap enemyBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.enemy);
	Bitmap bossBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.enemy_boss);

    //The coordinates for our dear pacman: (0,0) is the top-left corner
	int pacx = 50;
    int pacy = 400;
    int h,w; //used for storing our height and width
	ArrayList<DifficultyLevel> levels = new ArrayList<DifficultyLevel>();
	boolean levelsGenerated = false;
	DifficultyLevel currentLevel;
	int score = 0;
	int time = 90;
	boolean running = false;

	MovementDirection movementDirection = MovementDirection.IDLE;

	public void move(int x) {
		switch(movementDirection) {
			case LEFT:
				moveLeft(x);
				System.out.println("Moved LEFT by " + x);
				break;
			case RIGHT:
				moveRight(x);
				System.out.println("Moved RIGHT by " + x);
				break;
			case UP:
				moveUp(x);
				System.out.println("Moved UP by " + x);
				break;
			case DOWN:
				moveDown(x);
				System.out.println("Moved DOWN by " + x);
				break;
			default:
				System.out.println("You should try moving...");
		}
	}

    public void moveRight(int x)
	{
		//still within our boundaries?
		if (pacx+x+bitmap.getWidth()<w)
			pacx=pacx+x;
		invalidate(); //redraw everything - this ensures onDraw() is called.
	}

	public void moveLeft(int x)
	{
		//still within our boundaries?
		if (pacx-x>=0)
			pacx=pacx-x;
		invalidate(); //redraw everything - this ensures onDraw() is called.
	}

	public void moveUp(int y)
	{
		//still within our boundaries?
		if (pacy-y>=0)
			pacy=pacy-y;
		invalidate(); //redraw everything - this ensures onDraw() is called.
	}

	public void moveDown(int y)
	{
		//still within our boundaries?
		if (pacy+y+bitmap.getHeight()<h)
			pacy=pacy+y;
		invalidate(); //redraw everything - this ensures onDraw() is called.
	}

	// Generate coins in random locations.
	// Takes amount and canvas as a parameter.
	public ArrayList<GoldCoin> generateCoins(int amount, Canvas canvas) {
		ArrayList<GoldCoin> result = new ArrayList<GoldCoin>();

		Random r = new Random();
		int maxX = canvas.getWidth();
		int maxY = canvas.getHeight();

		for(int i=0; i < amount; i++) {
			// Create the coin, take into account the width and height of the coin bitmap
			result.add(new GoldCoin(r.nextInt(maxX - goldCoinBitmap.getWidth()), r.nextInt(maxY - goldCoinBitmap.getHeight())));
		}

		return result;
	}

	// Generates enemies at random locations starting from the middle on the X axis.
	// Some enemies in a level might be a boss. Bosses are somewhat faster and have
	// different appearance.
	public ArrayList<Enemy> generateEnemies(int amount, Canvas canvas) {
		ArrayList<Enemy> result = new ArrayList<Enemy>();
		int baseSpeed = 7;

		Random r = new Random();
		// Set min/max values for random generator.
		// Make enemies spawn only from the middle of the screen.
		int maxX = canvas.getWidth() - enemyBitmap.getWidth();
		int maxY = canvas.getHeight() - enemyBitmap.getHeight();
		int minX = (canvas.getWidth() / 2) + enemyBitmap.getWidth();
		int minY = enemyBitmap.getHeight()+ 10;

		for(int i = 0; i < amount; i++){
			if((i + 1) % 3 == 0){
				result.add(new Enemy(r.nextInt((maxX - minX) + 1) + minX , r.nextInt((maxY - minY) + 1) + minY, baseSpeed + amount * 2 , EnemyType.BOSS));
			} else {
				result.add(new Enemy(r.nextInt((maxX - minX) + 1) + minX , r.nextInt((maxY - minY) + 1) + minY, baseSpeed + amount, EnemyType.REGULAR));
			}
		}

		return result;
	}

	// Generates increasing difficulty levels based on the amount;
	public void generateDifficultyLevels(int amount, Canvas canvas) {
		//starting values for first level
		int currentCoinAmount = 3;
		int currentEnemyAmount = 1;

		//values to be added for each higher level
		int diffCoinIncrement = 2; //amount of coins
		int diffEnemyIncrement = 1; //enemy amount & speed bonus

		for(int i = 0; i < amount; i++) {
			this.levels.add(new DifficultyLevel(this.generateCoins(currentCoinAmount, canvas), this.generateEnemies(currentEnemyAmount, canvas)));

			// Increase amounts for next level
			currentCoinAmount += diffCoinIncrement;
			currentEnemyAmount += diffEnemyIncrement;
		}

		// Set final level property to true on the last difficulty level in the list.
		this.levels.get(this.levels.size() - 1).setFinalLevel(true);
		this.levelsGenerated = true;
		invalidate();
	}

	public void restartGame() {
		this.levelsGenerated = false;
		this.levels.clear();
		this.score = 0;
		this.time = 90;
		this.currentLevel = null;
		pacx = 50;
		pacy = 400;
		this.movementDirection = MovementDirection.IDLE;
		invalidate();
	}

	/* The next 3 constructors are needed for the Android view system,
	when we have a custom view.
	 */
	public MyView(Context context) {
		super(context);
		
	}
	
	public MyView(Context context, AttributeSet attrs) {
		super(context,attrs);
	}
	
	
	public MyView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context,attrs,defStyleAttr);
	}

	//In the onDraw we put all our code that should be
	//drawn whenever we update the screen.
	@Override
	protected void onDraw(final Canvas canvas) {
		//Here we get the height and weight
		h = canvas.getHeight();
		w = canvas.getWidth();
		System.out.println("h = "+h+", w = "+w);
		//Making a new paint object
		Paint paint = new Paint();

		//setting the color
		canvas.drawColor(Color.BLACK); //clear entire canvas to black color
		
		//setting the color using the format: Transparency, Red, Green, Blue
		paint.setColor(0xff000099);

		//Draw the pacman; Rotate packman based on the direction he's moving
		switch(movementDirection) {
			case LEFT:
				canvas.drawBitmap(BitmapHelper.RotateBitmap(bitmap, movementDirection), pacx, pacy, paint);
			default:
			case RIGHT:
				canvas.drawBitmap(BitmapHelper.RotateBitmap(bitmap, movementDirection), pacx, pacy, paint);
			case UP:
				canvas.drawBitmap(BitmapHelper.RotateBitmap(bitmap, movementDirection), pacx, pacy, paint);
			case DOWN:
				canvas.drawBitmap(BitmapHelper.RotateBitmap(bitmap, movementDirection), pacx, pacy, paint);
		}


		//Generate Levels, Coins and Enemies at the start of a new game
		if(levelsGenerated == false) {
			this.generateDifficultyLevels(3, canvas);
			//Set the current level to level 1
			this.currentLevel = levels.get(0);
		}

		//Draw the coins
		for(GoldCoin gc : currentLevel.getGoldCoins() ) {
			if(!gc.isPickedUp()) { //Only if it hasn't been picked up
				canvas.drawBitmap(goldCoinBitmap, gc.getX(), gc.getY(), paint);
			}
		}

		//Draw the enemies
		for(Enemy e : currentLevel.getEnemies()) {
			if(e.getType().equals(EnemyType.BOSS)) {
				canvas.drawBitmap(bossBitmap, e.getX(), e.getY(), paint);
			} else {
				canvas.drawBitmap(enemyBitmap, e.getX(), e.getY(), paint);
			}
		}


		//Calculate enemy collision
		for(Enemy e : currentLevel.getEnemies()) {
			if(CollisionDetector.isCollisionDetected(pacx, pacy, e.getX(), e.getY())) {
				AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();

				alertDialog.setTitle("Whoops!");
				alertDialog.setIcon(R.mipmap.ic_launcher);
				alertDialog.setCanceledOnTouchOutside(false);
				alertDialog.setMessage("Looks like they got you. Care for another try?");
				alertDialog.setButton("Let's do it!", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						restartGame();
						running = true;
						Toast.makeText(getContext(), "Good Luck!", Toast.LENGTH_SHORT).show();
					}
				});
				pacx = 50;
				movementDirection = MovementDirection.IDLE;
				running = false;
				invalidate();
				alertDialog.show();
			}
		}

        //Move enemies based on the pacman position aka Enemy AI
		if(running) { //only when game is running *fix*
				for (Enemy e: currentLevel.getEnemies()) {
					int currentEnemySpeed = e.getSpeed() / 5; //divided by 5 because of imbalance caused by
					//enemies being able to move diagonally

					if(e.getX() < pacx) { //move right
						if (e.getX()+ currentEnemySpeed + enemyBitmap.getWidth()<w) //calc bounds
							e.setX(e.getX() + currentEnemySpeed);
					} else { //move left if enemy X > pacman X
						if (e.getX() - currentEnemySpeed >= 0) //calc bounds
							e.setX(e.getX() - currentEnemySpeed);
					}

					if(e.getY() < pacy) { //move down
						if (e.getY()+ currentEnemySpeed + enemyBitmap.getHeight()<h) //calc bounds
							e.setY(e.getY() + currentEnemySpeed);
					} else { //move up if enemy Y > pacman Y
						if (e.getY() - currentEnemySpeed >= 0) //calc bounds
							e.setY(e.getY() - currentEnemySpeed);
					}

					// Redraw the ghost
					invalidate();
			}
		}

		// Iterate through the remaining gold coins to check if the pacman
		// is able to pickup any of them
		for(GoldCoin gc : currentLevel.getRemainingGoldCoins()) {
			if(CollisionDetector.isCollisionDetected(pacx, pacy, gc.getX(), gc.getY())) {
				gc.setPicked_up(true);
				score += gc.getScore();

				// If all coins are collected load the next level if available, or congratulate
				// on beating the game.
				if(currentLevel.getRemainingCoinsAmount() == 0) {
					if(currentLevel.isFinalLevel()) { //if current level is not final, load the nex one
						AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();

						alertDialog.setTitle("Astounding!");
						alertDialog.setIcon(R.mipmap.ic_launcher);
						alertDialog.setCanceledOnTouchOutside(false);
						alertDialog.setMessage("You beat me. I name you the Champion!");
						alertDialog.setButton("GIMME MORE!", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								restartGame();
								running = true;
								Toast.makeText(getContext(), "As you wish, Champ!!", Toast.LENGTH_SHORT).show();
							}
						});

						running = false;
						alertDialog.show();

					} else {
						// Congratulations -> next level
						AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();

						alertDialog.setTitle("Good Job!");
						alertDialog.setIcon(R.mipmap.ic_launcher);
						alertDialog.setMessage("You seem to be good at this. I'm increasing the difficulty!");
						alertDialog.setCanceledOnTouchOutside(false);
						alertDialog.setButton("BRING IT!", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								time = 90;
								pacx = 50;
								pacy = canvas.getHeight()/2;
								movementDirection = MovementDirection.IDLE;
								currentLevel = levels.get(levels.indexOf(currentLevel) + 1);
								running = true;
								invalidate();
								Toast.makeText(getContext(), "Here it comes..!", Toast.LENGTH_SHORT).show();
							}
						});

						running = false;
						alertDialog.show();
					}
				}

				invalidate();
			}
		}

		System.out.println("Pac X: " + pacx + " Pac Y: " + pacy);
		System.out.println("Bitmap W:" + bitmap.getWidth() + " H: " + bitmap.getHeight());
		System.out.println("Coins remaining: " + currentLevel.getRemainingCoinsAmount());
		super.onDraw(canvas);
	}

}
