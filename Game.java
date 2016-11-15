package mastermind;

import java.util.Random;

public class Game {

private Color [] key=new Color [4];//holds the sequence the player must guess
private Guess [] guesses;//holds guess objects--the guesses of the user for reference
private int [][] results;/*holds the status of each peg--red, white, or none
this is NOT for player use but rather for the programmers'/computer's use for future advancements
such as tracking logical consistency*/
private int guessTracker;/*this keeps track of what position we are at in the guesses array and what
row we are in in the results array*/

/**Constructor*/
public Game(){
	this.key=generateKey();//calls method to create a random sequence to guess
	this.guessTracker=0;
	//the following hardcoded as the player can only have 10 guesses and the sequence can only be four long
	guesses=new Guess[10];
	results=new int[10][4];
	
}

/**generateKey--generates a random sequence to be guessed by player
 * this is a private method only used within this class
 * @return an array of the sequence of the enum type Color*/
private Color[] generateKey(){

	//in a for loop to populate each of 4 spaces in array
	for(int index=0;index<key.length;index++){
		/*create a Random obj to generate a number randomly 0-5
		 * purposely not doing 1-6 because ordinality of enum starts at 0 */
		Random rand=new Random();
		int num=rand.nextInt(6);
		
		Color col=Color.values()[num];
		
	
		key[index]=col;//set each location to the color determined by the Random 
	}//for
	return key;
}//generateKey()

/**getGuesses: a getter method to return the array of all past guesses
 * @return the array of past guesses*/
public Guess[] getGuesses(){
	if(this.guessTracker==0){
		//TODO then need to let user know because empty array
	}
	return this.guesses;
}

/**getNumGuesses: a getter
 * @return the number of guesses made so far*/
public int getNumGuesses(){
	return this.guessTracker;
}

public void checkGuess(Color[]attempt){
	
	int numReds=0;
	int numWhites=0;
	boolean found=false;
	
	for(int index=0;index<key.length;index++){
		
		//if right color right place the row of the turn up to in game and the column up to in loop
		//gets a 1 to symbolize a red
		if(attempt[index].equals(key[index])){
			results[guessTracker][index]=1;
			numReds++;//and total red pegs updated
			found=true;
			
		}//if
		else{//search through rest of array (start at next subscript) to see if it is in the wrong spot
			for(int x=index+1;x<key.length;x++){
				if(attempt[x].equals(key[x])){
					results[guessTracker][index]=2;/*for white--**Must be [index] as opposed to [x]
					because x has changed now, it was incremented so if want the status of the 
					guess in position [1] for example [x] will equal the position of where it was found
					whereas [index] will still be on the position of the peg you are checking*/
					numWhites++;
					found=true;					
				}//if
			}//for
			//if it was not found anywhere, then the status of that peg is a 0--the color not in it at all
			if(!found){
				results[guessTracker][index]=0;
			}
			
		}//else
	
	}//for
	
	//create new guess object
	Guess guess=new Guess(numReds,numWhites,attempt);
	//insert that new object into the guesses array
	guesses[guessTracker]=guess;
	//increment the guess tracker for the next turn
	guessTracker++;
	
	
}//checkGuess


}//Game class
