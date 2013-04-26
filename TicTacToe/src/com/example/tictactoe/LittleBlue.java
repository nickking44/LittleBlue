package com.example.tictactoe;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Point;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


/**
 * The following code is a good starting place for tic tac toe app.
 * There is no scoring logic on at the moment and the computer only
 * goes first once once in this version. consecutive resets will always
 * the user go first.
 * 
 * UPDATE. Scoring logic has been added. Tilt to reset.
 * 
 * There is a method to check the player two's move but it is not
 * implemented anywhere. or tested.
 * 
 * UPDATE
 * 
 * Done
 * 
 * otherwise have fun
 * 
 * @author Nick King
 * 
 *
 */
public class LittleBlue extends Activity {

	
	final static String playerOne = "Computer";
	final static String playerTwo = "PlayerTwo";
	List<List<Point>> grid_;
	List<Move> takenSquares_;  
	boolean gameOn_;
	boolean playersTurn_;
	boolean playersGo_;
	
	int p1Score = 0;
	int p2Score = 0;


	List<Possibility> possibilities;
	
	TextView positionOne;
	TextView positionTwo;
	TextView positionThree;
	TextView positionFour;
	TextView positionFive;
	TextView positionSix;
	TextView positionSeven;
	TextView positionEight;
	TextView positionNine;
	
	TextView playerOneScore;
	TextView playerTwoScore;
	
	RelativeLayout computerWins;
	TextView computerWinsText;

	TextView replay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
    	//Get ride of the window decorations.
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        //Going to use xml for layout as the game is pretty finite.
	    setContentView(R.layout.activity_main);
    
        replay=(TextView)findViewById(R.id.replay); 

	    
        positionOne=(TextView)findViewById(R.id.one); 
        positionTwo=(TextView)findViewById(R.id.two); 
        positionThree=(TextView)findViewById(R.id.three); 
        positionFour=(TextView)findViewById(R.id.four); 
        positionFive=(TextView)findViewById(R.id.five); 
        positionSix=(TextView)findViewById(R.id.six); 
        positionSeven=(TextView)findViewById(R.id.seven); 
        positionEight=(TextView)findViewById(R.id.eight); 
        positionNine=(TextView)findViewById(R.id.nine); 
        positionOne.setClickable(true);
        
        playerOneScore=(TextView)findViewById(R.id.player_one_score); 
        playerTwoScore=(TextView)findViewById(R.id.player_two_score);
        
        computerWinsText =(TextView)findViewById(R.id.computer_wins_text);
        computerWins=(RelativeLayout)findViewById(R.id.computer_wins); 
        computerWins.setVisibility(View.GONE);
        
        setOnclickButton(positionOne, new Point(0,0));
        setOnclickButton(positionTwo, new Point(1,0));
        setOnclickButton(positionThree, new Point(2,0));
        setOnclickButton(positionFour, new Point(0,1));
        setOnclickButton(positionFive, new Point(1,1));
        setOnclickButton(positionSix, new Point(2,1));
        setOnclickButton(positionSeven, new Point(0,2));
        setOnclickButton(positionEight, new Point(1,2));
        setOnclickButton(positionNine, new Point(2,2));

        replay.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

            	reinitialise();

            }
            
        });
	
        playersTurn_ = false;
        takenSquares_ = new ArrayList<Move>();
    	//grid_ = createGrid();//removed
    	computersTurn();

    }
  
    
    public void setOnclickButton(TextView button, Point coords) {
    	
    	final Point bp_ = coords;
    	final TextView bt_ = button;
    	
        
    	bt_.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

            	
            	Log.v(getClass().getSimpleName(), "click");
            	if(playersTurn_ == false)
            			return;
            	bt_.setText("0");
             	boolean check = checkPlayersMove(bp_);
            	if(check) {
            		
            		updateScores(playerOne);
            		computerWinsText.setText("Player Two Wins");
            		disableSquares();
            		return;
            	}
            	takenSquares_.add(new Move(playerTwo, bp_));
            	playersTurn_ = false;
            	bt_.setClickable(false);
            	computersTurn();
	
            }
            
        });
        
    	
    	
    	
    }
    
    /**
     * And all that
     */
    public void reinitialise() {

        playersTurn_ = true;
        takenSquares_ = new ArrayList<Move>();
        positionOne.setText("");
        positionTwo.setText("");
        positionThree.setText("");
        positionFour.setText("");
        positionFive.setText("");
        positionSix.setText("");
        positionSeven.setText("");
        positionEight.setText("");
        positionNine.setText("");
        positionOne.setClickable(true);
        positionTwo.setClickable(true);
        positionThree.setClickable(true);
        positionFour.setClickable(true);
        positionFive.setClickable(true);
        positionSix.setClickable(true);
        positionSeven.setClickable(true);
        positionEight.setClickable(true);
        positionNine.setClickable(true);
        computerWins.setVisibility(View.GONE);
        
        if(!playersGo_) {
        
        	playersTurn_ = true;
        	playersGo_ = true;
        
    	} else {
        
    		gameOn_ = false;
    		playersGo_ = false;
        	playersTurn_ = false;
    		computersTurn();
        
    	}
	
    }
    
    
    
    public void disableSquares() {
    	
        positionOne.setClickable(false);
        positionTwo.setClickable(false);
        positionThree.setClickable(false);
        positionFour.setClickable(false);
        positionFive.setClickable(false);
        positionSix.setClickable(false);
        positionSeven.setClickable(false);
        positionEight.setClickable(false);
        positionNine.setClickable(false);
	
    }
    //leave out if using xml for layout
    //maybe use for window focus change. We'll see...
    /*
    private List<List<Point>> createGrid() {

    	List<List<Point>> grid = new ArrayList<List<Point>>();

    	for(int i=0;i<3;i++) {
    	
    		List<Point> row = new ArrayList<Point>();
    	
    		for(int j=0;j<3;j++) {
    		
    			Point square = new Point(i,j);
    			row.add(square);
    		
    		}
    		
    		grid.add(row);
    	
    	}
    	
    	return grid;

    }
    */

    
    /**
     * Checks if game has been played yet and makes first move.
     * Always starts on the center. with the gameOn_ set to false.
     * sets the turn to player on successful completion
     */
    public void computersTurn() {

    	//In any one game session the computer moves first.
    	if(!gameOn_) {
    	
    		Move move = new Move(playerOne, new Point(1,1));
    		takenSquares_.add(move);
    		positionFive.setText("X");
    		positionFive.setClickable(false);
    		gameOn_ = true;
    		playersTurn_ = true;
    		return;
    	
    	}

    	checkBoard();
    	playersTurn_ = true;

    }
    
    

    /**
     * Creates a new possibilities array for that turn.
     * Checks the three axis for results which are appended to
     * possibilities array.
     */
    private void checkBoard(){

    	possibilities = new ArrayList<Possibility>();

    	//check for sneaking starting corner manuvours by the human
    	if (takenSquares_.size() == 1) {
    		
    		if((usedByPlayer(playerTwo, new Point(0,0))) 
    			|| (usedByPlayer(playerTwo, new Point(2,0))) 
    			|| (usedByPlayer(playerTwo, new Point(0,2))) 
    			|| (usedByPlayer(playerTwo, new Point(2,2)))) {
    			
    			possibilities.add(new Possibility(5, new Point(1,1)));

    		}

    	}
	
    	//carry on
    	for(int i=0;i<3;i++) {
    	
    		for(int j=0;j<3;j++) {
    	
    			//check to see if square ids used
    			boolean used = isUsed(new Point(i,j));
    			if (!used) {

    				checkVertical(new Point(i,j));
    				checkHorizontal(new Point(i,j));
    				checkDiagonal(new Point(i,j));
    				
    			} 

    		}

    	}

    	
    	Collections.sort(possibilities, new Comparator<Possibility>(){
    		  public int compare(Possibility p1, Possibility p2) {
    		    return new Integer(p1.getRate()).compareTo(new Integer(p2.getRate()));
    		  }
    	});

    	// if there are possibilities the computer selects the highest.
    	//TODO add a randomizer if top results are more than one.
    	if (possibilities.size() != 0) {
    		takenSquares_.add(new Move(playerOne, possibilities.get(possibilities.size() -1).getPosition()));
    		paintBoard(possibilities.get(possibilities.size() -1).getPosition());
    		if (possibilities.get(possibilities.size() -1).getRate() == 3) {
    			
    			computerWins.setVisibility(View.VISIBLE);
    			computerWinsText.setText("Little Blue Blocks\n woooooooo");
    			
    		} else if (possibilities.get(possibilities.size() -1).getRate() == 4) {
    			
        		updateScores(playerTwo);
    			computerWins.setVisibility(View.VISIBLE);
    			computerWinsText.setText("Little Blue Wins\n woooooooo\nwwwoooooooo");
    			disableSquares();
    			
    		}
    	}
    	
    }
    
    /**
     * Used to determine if square is in use
     * @param checkPoint
     * @return
     * TODO Pass in takensQuares_
     */
    private boolean isUsed(Point checkPoint) {

		Point checkPoint_ = checkPoint;

		Iterator<Move> checkIterator = takenSquares_.iterator();
		while(checkIterator.hasNext()) {
		
			if (checkIterator.next().getPosition().equals(checkPoint_)) {
	
				return true;
			
			}

		}

		return false;

    }

    
    
    /**
     * Used to check if a square is used by either player.
     * @param player
     * @param pointToCheck
     * @return
     * TODO Pass in takenSquares_
     */
    private boolean usedByPlayer(String player, Point pointToCheck) {

		Point cp_ = pointToCheck;
		String player_ = player;
		Iterator<Move> checkIterator = takenSquares_.iterator();
		while(checkIterator.hasNext()) {

			Move move = checkIterator.next();
			
			if (move.getPosition().equals(cp_)) { 
					if (move.getPlayer().equals(player_)) 
						return true;
			
			}

		}

		return false;
		
    }
        
    
    public boolean checkPlayersMove(Point playersMove) {

    	
    	Point pm_ = playersMove;
    	//satisfy one of these or return false.
    	//TODO . find a faster way.
    	if (pm_.x == 0) {
			if (usedByPlayer(playerTwo, new Point(pm_.x + 1,pm_.y))
					&& usedByPlayer(playerTwo, new Point(pm_.x + 2,pm_.y))) {
				return true;
			}
		}
    	
    	if (pm_.y == 0) {
			if (usedByPlayer(playerTwo, new Point(pm_.x,pm_.y + 1))
					&& usedByPlayer(playerTwo, new Point(pm_.x,pm_.y + 2))) {
				return true;
			}

		}
    	
    	if (pm_.x == 1) {
			if (usedByPlayer(playerTwo, new Point(pm_.x - 1,pm_.y))
					&& usedByPlayer(playerTwo, new Point(pm_.x + 1,pm_.y))) {
				return true;
			}
		}
    	
    	if (pm_.y == 1) {
			if (usedByPlayer(playerTwo, new Point(pm_.x,pm_.y - 1))
					&& usedByPlayer(playerTwo, new Point(pm_.x,pm_.y + 1))) {
				return true;
			}

		}
    	
    	if (pm_.x == 2) {
			if (usedByPlayer(playerTwo, new Point(pm_.x - 2,pm_.y))
					&& usedByPlayer(playerTwo, new Point(pm_.x - 1,pm_.y))) {
				return true;
			}
		}
    	
    	if (pm_.y == 2) {
			if (usedByPlayer(playerTwo, new Point(pm_.x,pm_.y - 2))
					&& usedByPlayer(playerTwo, new Point(pm_.x,pm_.y - 1))) {
				return true;
			}

		}
    	
    	if (pm_.x == 0 && pm_.y == 0) {
    	
    		if (usedByPlayer(playerTwo, new Point(pm_.x+1,pm_.y + 1))
    				&& usedByPlayer(playerTwo, new Point(pm_.x+2,pm_.y + 2))) {
    			return true;
    		}

    	}
    	
    	if (pm_.x == 2 && pm_.y == 0) {
      
    		
    		if (usedByPlayer(playerTwo, new Point(pm_.x-1,pm_.y + 1))
    				&& usedByPlayer(playerTwo, new Point(pm_.x-2,pm_.y + 2))) {
    			return true;
    		}

    	}
    	
    	if (pm_.x == 0 && pm_.y == 2) {
        	
    		if (usedByPlayer(playerTwo, new Point(pm_.x+1,pm_.y - 1))
    				&& usedByPlayer(playerTwo, new Point(pm_.x+2,pm_.y - 2))) {
    			return true;
    		}

    	}
    	if (pm_.x == 2 && pm_.y == 2) {
        	
    		if (usedByPlayer(playerTwo, new Point(pm_.x-1,pm_.y - 1))
    				&& usedByPlayer(playerTwo, new Point(pm_.x-2,pm_.y - 2))) {
    			return true;
    		}

    	}
    	
    	if (pm_.x == 1 && pm_.y == 1) {
        	
    		if (usedByPlayer(playerTwo, new Point(pm_.x-1,pm_.y - 1))
    				&& usedByPlayer(playerTwo, new Point(pm_.x+1,pm_.y + 1))) {
    			return true;
    		}
    		
    		if (usedByPlayer(playerTwo, new Point(pm_.x+1,pm_.y + 1))
    				&& usedByPlayer(playerTwo, new Point(pm_.x-1,pm_.y - 1))) {
    			return true;
    		}

    	}
    	
		return false;
	
    }
    
    
	/**
	 * Used to determine if there are any diagonal moves. 
	 * Moves are rated and added to the current possibilities array.
	 * 
	 * This checkHorizontal and check vertical cascade through scenarios
	 * to determine a rating for each possibility. This could be broken down
	 * to provide a skill level switch.
	 * 
	 * @param pointToCheck
	 * @return
	 * TODO. Change return type to array and pass possibilities in.
	 */
	private boolean checkDiagonal(Point pointToCheck) {
    
		Point cp_ = pointToCheck;
		
		//ignore even numbers
		if ((cp_.x == 1 && cp_.y == 0)
					|| (cp_.x == 1 && cp_.y == 2) 
					|| (cp_.x == 0 && cp_.y == 1)
					|| (cp_.x == 2 && cp_.y == 1)){
			return false;
		}
		
    	if (cp_.x == 0 && cp_.y == 0) {
    		
    		if (usedByPlayer(playerTwo, new Point(cp_.x + 1, pointToCheck.y + 1)) && usedByPlayer(playerTwo, new Point(cp_.x + 2, pointToCheck.y + 2))) {

    			possibilities.add(new Possibility(3, cp_));
    			return true;
    			
    		} else {

        		if (usedByPlayer(playerOne, new Point(cp_.x + 1, pointToCheck.y + 1)) || usedByPlayer(playerOne, new Point(cp_.x + 2, pointToCheck.y + 2))) {
 
            		if (usedByPlayer(playerOne, new Point(cp_.x + 1, pointToCheck.y + 1)) && usedByPlayer(playerOne, new Point(cp_.x + 2, pointToCheck.y + 2))) {

            			possibilities.add(new Possibility(4, cp_));
            			return true;
            		
            		} else {
            			
            			possibilities.add(new Possibility(2, cp_));
            			return true;
            			
            		}
	
        		}	
    		
    		}
    		
    	}
		
    	if (cp_.x == 2 && cp_.y == 0) {
    		
    		if (usedByPlayer(playerTwo, new Point(cp_.x -1, pointToCheck.y + 1)) 
    						&& usedByPlayer(playerTwo, new Point(cp_.x - 2, pointToCheck.y + 2))) {

    			possibilities.add(new Possibility(3, cp_));
    			return true;
    			
    		} else {

        		if (usedByPlayer(playerOne, new Point(cp_.x - 1, pointToCheck.y + 1)) 
        					|| usedByPlayer(playerOne, new Point(cp_.x - 2, pointToCheck.y + 2))) {
 
            		if (usedByPlayer(playerOne, new Point(cp_.x - 1, pointToCheck.y + 1)) 
            				&& usedByPlayer(playerOne, new Point(cp_.x - 2, pointToCheck.y + 2))) {

            			possibilities.add(new Possibility(4, cp_));
            			return true;
            		
            		} else {
            			
            			possibilities.add(new Possibility(2, cp_));
            			return true;
            			
            		}
	
        		}	
    		
    		}
    		
    	}
    	
    	
    	if (cp_.x == 0 && cp_.y == 2) {
    		
    		if (usedByPlayer(playerTwo, new Point(cp_.x +1, pointToCheck.y - 1)) 
    						&& usedByPlayer(playerTwo, new Point(cp_.x + 2, pointToCheck.y - 2))) {

    			possibilities.add(new Possibility(3, cp_));
    			return true;
    			
    		} else {

        		if (usedByPlayer(playerOne, new Point(cp_.x + 1, pointToCheck.y - 1)) 
        					|| usedByPlayer(playerOne, new Point(cp_.x + 2, pointToCheck.y - 2))) {
 
            		if (usedByPlayer(playerOne, new Point(cp_.x + 1, pointToCheck.y - 1)) 
            				&& usedByPlayer(playerOne, new Point(cp_.x + 2, pointToCheck.y - 2))) {

            			possibilities.add(new Possibility(4, cp_));
            			return true;
            		
            		} else {
            			
            			possibilities.add(new Possibility(2, cp_));
            			return true;
            			
            		}
	
        		}	
    		
    		}
    		
    	}
    	
    	if (cp_.x == 2 && cp_.y == 2) {
    		
    		if (usedByPlayer(playerTwo, new Point(cp_.x -1, pointToCheck.y - 1)) 
    						&& usedByPlayer(playerTwo, new Point(cp_.x - 2, pointToCheck.y - 2))) {

    			possibilities.add(new Possibility(3, cp_));
    			return true;
    			
    		} else {

        		if (usedByPlayer(playerOne, new Point(cp_.x - 1, pointToCheck.y - 1)) 
        					|| usedByPlayer(playerOne, new Point(cp_.x - 2, pointToCheck.y - 2))) {
 
            		if (usedByPlayer(playerOne, new Point(cp_.x - 1, pointToCheck.y - 1)) 
            				&& usedByPlayer(playerOne, new Point(cp_.x - 2, pointToCheck.y - 2))) {

            			possibilities.add(new Possibility(4, cp_));
            			return true;
            		
            		} else {
            			
            			possibilities.add(new Possibility(2, cp_));
            			return true;
            			
            		}
	
        		}	
    		
    		}
    		
    	}
    	
    	
    	if (cp_.x == 1 && cp_.y == 1) {
    		
    		if (usedByPlayer(playerTwo, new Point(cp_.x -1, pointToCheck.y - 1)) 
    						&& usedByPlayer(playerTwo, new Point(cp_.x + 1, pointToCheck.y + 1))) {

    			possibilities.add(new Possibility(3, cp_));
    			return true;
    			
    		} else if (usedByPlayer(playerTwo, new Point(cp_.x +1, pointToCheck.y + 1)) 
						&& usedByPlayer(playerTwo, new Point(cp_.x - 1, pointToCheck.y - 1))) {

    			possibilities.add(new Possibility(3, cp_));
    			return true;
		
    		} else {

        		if ((usedByPlayer(playerOne, new Point(cp_.x - 1, pointToCheck.y - 1)) 
        					|| usedByPlayer(playerOne, new Point(cp_.x + 1, pointToCheck.y + 1)))
        							||
        							(usedByPlayer(playerOne, new Point(cp_.x + 1, pointToCheck.y + 1)) 
        		        					|| usedByPlayer(playerOne, new Point(cp_.x - 1, pointToCheck.y - 1)))){
 
            		if ((usedByPlayer(playerOne, new Point(cp_.x - 1, pointToCheck.y - 1)) 
            				&& usedByPlayer(playerOne, new Point(cp_.x + 1, pointToCheck.y + 1))) 
            				||
            				(usedByPlayer(playerOne, new Point(cp_.x + 1, pointToCheck.y + 1)) 
                    				&& usedByPlayer(playerOne, new Point(cp_.x - 1, pointToCheck.y - 1)))){
            			
            			possibilities.add(new Possibility(4, cp_));
            			return true;
            		
            		} else {
            			
            			possibilities.add(new Possibility(2, cp_));
            			return true;
            			
            		}
	
        		}	
    		
    		}
    		
    	}
    	
    	
		return false;
	}
		
	/**
	 * Used to determine if there are any vertical moves. 
	 * Moves are rated and added to the current possibilities array.
	 * @param pointToCheck
	 * @return
	 * TODO. Change return type to array and pass possibilities in.
	 */
	private boolean checkVertical(Point pointToCheck) {

		Point cp_ = pointToCheck;
		//First check impending doom from flesh player.
    	if (cp_.y == 0) {
		
    		if (usedByPlayer(playerTwo, new Point(cp_.x, pointToCheck.y + 1)) && usedByPlayer(playerTwo, new Point(cp_.x, pointToCheck.y + 2))) {

    			possibilities.add(new Possibility(3, cp_));
    			return true;
    			
    		}
    		
    	}
		
    	if (cp_.y == 1) {
    		
    		if (usedByPlayer(playerTwo, new Point(cp_.x, pointToCheck.y - 1)) && usedByPlayer(playerTwo, new Point(cp_.x, pointToCheck.y +1))) {

				possibilities.add(new Possibility(3, cp_));
    			return true;
    			
    		}
    		
    	}
    	
    	if (cp_.y == 2) {
    		
    		if (usedByPlayer(playerTwo, new Point(cp_.x, pointToCheck.y - 1)) && usedByPlayer(playerTwo, new Point(cp_.x, pointToCheck.y - 2))) {

				possibilities.add(new Possibility(3, cp_));
    			return true;
    			
    		}
    		
    	}
    	
		//Then check good moves for yourself, the computer)
    	if (cp_.y == 0) {
    	
    		if (isUsed(new Point(cp_.x, pointToCheck.y + 1)) && !usedByPlayer(playerOne, new Point(cp_.x, pointToCheck.y + 1)))
    				return false;
    		if (isUsed(new Point(cp_.x, pointToCheck.y + 2)) && !usedByPlayer(playerOne, new Point(cp_.x, pointToCheck.y + 2)))
					return false;
    		if (usedByPlayer(playerOne, new Point(cp_.x, pointToCheck.y + 1)) || usedByPlayer(playerOne, new Point(cp_.x, pointToCheck.y + 2))) {
				
        		if (usedByPlayer(playerOne, new Point(cp_.x, pointToCheck.y + 1)) && usedByPlayer(playerOne, new Point(cp_.x, pointToCheck.y + 2))) {
	
    				possibilities.add(new Possibility(4, cp_));
        			return true;
		
        		} else {
        			
    				possibilities.add(new Possibility(2, cp_));
        			return true;

        		}

    		}
			possibilities.add(new Possibility(1, cp_));
			return true;

    	} else if (pointToCheck.y == 1) {
    		
    		if (isUsed(new Point(cp_.x, pointToCheck.y - 1)) && !usedByPlayer(playerOne, new Point(cp_.x, pointToCheck.y - 1)))
				return false;
			if (isUsed(new Point(cp_.x, pointToCheck.y + 1)) && !usedByPlayer(playerOne, new Point(cp_.x, pointToCheck.y + 1)))
					return false;
			if (usedByPlayer(playerOne, new Point(cp_.x, pointToCheck.y - 1)) || usedByPlayer(playerOne, new Point(cp_.x, pointToCheck.y + 1))) {
				
	    		if (usedByPlayer(playerOne, new Point(cp_.x, pointToCheck.y - 1)) && usedByPlayer(playerOne, new Point(cp_.x, pointToCheck.y + 1))) {
	
					possibilities.add(new Possibility(4, cp_));
	    			return true;
		
	    		} else {
	    			
					possibilities.add(new Possibility(2, cp_));
	    			return true;
	
	    		}
	
			}
			possibilities.add(new Possibility(1, cp_));
			return true;
    		
    	} else if (pointToCheck.y == 2) {

    		if (isUsed(new Point(cp_.x, pointToCheck.y - 1)) && !usedByPlayer(playerOne, new Point(cp_.x, pointToCheck.y - 1)))
    				return false;
    		if (isUsed(new Point(cp_.x, pointToCheck.y - 2)) && !usedByPlayer(playerOne, new Point(cp_.x, pointToCheck.y - 2)))
					return false;
    		if (usedByPlayer(playerOne, new Point(cp_.x, pointToCheck.y - 1)) || usedByPlayer(playerOne, new Point(cp_.x, pointToCheck.y - 2))) {
    				
	    		if (usedByPlayer(playerOne, new Point(cp_.x, pointToCheck.y - 1)) && usedByPlayer(playerOne, new Point(cp_.x, pointToCheck.y - 2))) {
	
					possibilities.add(new Possibility(4, cp_));
	    			return true;
		
	    		} else {
	    			
					possibilities.add(new Possibility(2, cp_));
	    			return true;
	
	    		}
	
			}
			possibilities.add(new Possibility(1, cp_));
			return true;	

    	}
	
    	return false;

    }
	
	/**
	 * Used to determine if there are any horizontal moves. 
	 * Moves are rated and added to the current possibilities array.
	 * @param pointToCheck
	 * @return
	 * TODO. Change return type to array and pass possibilities in.
	 */
	private boolean checkHorizontal(Point pointToCheck) {

		Point cp_ = pointToCheck;

		//as vertical
    	if (cp_.x == 0) {
		
    		if (usedByPlayer(playerTwo, new Point(cp_.x + 1, pointToCheck.y)) && usedByPlayer(playerTwo, new Point(cp_.x + 2, pointToCheck.y))) {

    			possibilities.add(new Possibility(3, cp_));
    			return true;
    			
    		}
    		
    	}
		
    	if (cp_.x == 1) {
    		
    		if (usedByPlayer(playerTwo, new Point(cp_.x - 1, pointToCheck.y)) && usedByPlayer(playerTwo, new Point(cp_.x +1, pointToCheck.y))) {

				possibilities.add(new Possibility(3, cp_));
    			return true;
    			
    		}
    		
    	}
    	
    	if (cp_.x == 2) {
    		
    		if (usedByPlayer(playerTwo, new Point(cp_.x - 1, pointToCheck.y)) && usedByPlayer(playerTwo, new Point(cp_.x - 2, pointToCheck.y))) {

				possibilities.add(new Possibility(3, cp_));
    			return true;
    			
    		}
    		
    	}
	
		//as vertical
    	if (cp_.x == 0) {
    	
    		if (isUsed(new Point(cp_.x + 1, pointToCheck.y)) && !usedByPlayer(playerOne, new Point(cp_.x + 1, pointToCheck.y)))
    				return false;
    		if (isUsed(new Point(cp_.x + 2, pointToCheck.y)) && !usedByPlayer(playerOne, new Point(cp_.x + 2, pointToCheck.y)))
					return false;
    		if (usedByPlayer(playerOne, new Point(cp_.x + 1, pointToCheck.y)) || usedByPlayer(playerOne, new Point(cp_.x + 2, pointToCheck.y))) {
				
        		if (usedByPlayer(playerOne, new Point(cp_.x + 1, pointToCheck.y)) && usedByPlayer(playerOne, new Point(cp_.x + 2, pointToCheck.y))) {
	
    				possibilities.add(new Possibility(4, cp_));
        			return true;
		
        		} else {
        			
    				possibilities.add(new Possibility(2, cp_));
        			return true;

        		}

    		}
			possibilities.add(new Possibility(1, cp_));
			return true;

    	} else if (pointToCheck.x == 1) {
    		
    		if (isUsed(new Point(cp_.x - 1, pointToCheck.y)) && !usedByPlayer(playerOne, new Point(cp_.x - 1, pointToCheck.y)))
				return false;
			if (isUsed(new Point(cp_.x + 1, pointToCheck.y)) && !usedByPlayer(playerOne, new Point(cp_.x + 1, pointToCheck.y)))
					return false;
			if (usedByPlayer(playerOne, new Point(cp_.x - 1, pointToCheck.y)) || usedByPlayer(playerOne, new Point(cp_.x + 1, pointToCheck.y))) {
				
	    		if (usedByPlayer(playerOne, new Point(cp_.x - 1, pointToCheck.y)) && usedByPlayer(playerOne, new Point(cp_.x + 1, pointToCheck.y))) {
	
					possibilities.add(new Possibility(4, cp_));
	    			return true;
		
	    		} else {
	    			
					possibilities.add(new Possibility(2, cp_));
	    			return true;
	
	    		}
	
			}
			possibilities.add(new Possibility(1, cp_));
			return true;
    		
    	} else if (pointToCheck.x == 2) {

    		if (isUsed(new Point(cp_.x - 1, pointToCheck.y)) && !usedByPlayer(playerOne, new Point(cp_.x - 1, pointToCheck.y)))
    				return false;
    		if (isUsed(new Point(cp_.x - 2, pointToCheck.y)) && !usedByPlayer(playerOne, new Point(cp_.x - 2, pointToCheck.y)))
					return false;
    		if (usedByPlayer(playerOne, new Point(cp_.x - 1, pointToCheck.y)) || usedByPlayer(playerOne, new Point(cp_.x - 2, pointToCheck.y))) {
    				
	    		if (usedByPlayer(playerOne, new Point(cp_.x - 1, pointToCheck.y)) && usedByPlayer(playerOne, new Point(cp_.x - 2, pointToCheck.y))) {
	
					possibilities.add(new Possibility(4, cp_));
	    			return true;
		
	    		} else {
	    			
					possibilities.add(new Possibility(2, cp_));
	    			return true;
	
	    		}
	
			}
			possibilities.add(new Possibility(1, cp_));
			return true;	

    	}
	
    	return true;

    }

	
	public void updateScores(String player) {
		
		if(player == playerOne) {

			p1Score =  p1Score + 1;
			playerOneScore.setText("" + p1Score);
		
		} else {

			p2Score += 1;
			playerTwoScore.setText("" + p2Score);

		}

	}
	
	public void paintBoard(Point point) {
		
		Point point_ = point;

		if ((point_.x == 0) && (point_.y == 0)) {
			
			positionOne.setText("X");
			positionOne.setClickable(false);
			
		} else if ((point_.x == 1) && (point_.y == 0)) {
			
			positionTwo.setText("X");
			positionTwo.setClickable(false);
			
		} else if ((point_.x == 2) && (point_.y == 0)) {
			
			positionThree.setText("X");
			positionThree.setClickable(false);
			
		} else if ((point_.x == 0) && (point_.y == 1)) {
			
			positionFour.setText("X");
			positionFour.setClickable(false);
			
		} else if ((point_.x == 1) && (point_.y == 1)) {
			
			positionFive.setText("X");
			positionFive.setClickable(false);
			
		} else if ((point_.x == 2) && (point_.y == 1)) {
			
			positionSix.setText("X");
			positionSix.setClickable(false);
			
		} else if ((point_.x == 0) && (point_.y == 2)) {
			
			positionSeven.setText("X");
			positionSeven.setClickable(false);
			
		} else if ((point_.x == 1) && (point_.y == 2)) {
			
			positionEight.setText("X");
			positionEight.setClickable(false);
			
		} else if ((point_.x == 2) && (point_.y == 2)) {
			
			positionNine.setText("X");
			positionNine.setClickable(false);
			
		}
	
	}
 
}

	
class Move	{

	String player_;
	Point position_;

	public Move(String player,Point position) {
		player_=player;
		position_=position;
	}

	public String getPlayer()	{
		return player_;
	}

	public Point getPosition() {
		return position_;
	}


}


class Possibility	{
	
	int rate_;
	Point position_;

	public Possibility(int rate,Point position) {
		rate_=rate;
		position_=position;
	}

	public int getRate()	{
		return rate_;
	}

	public Point getPosition() {
		return position_;
	}

}
