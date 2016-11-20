package cs414.a5.cs414g.backend;

import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by TatroIII on 10/21/2016.
 */
public class Dice {

	private int die1;
	private int die2;
	private Random random;
	private Calendar calendar;
	long seconds;
	Random randomInt;

	public Dice(){
		this.calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		long seconds = calendar.getTimeInMillis() / 1000L;
		random = new Random(seconds);
	}

	private int rollADie(){
		return random.nextInt(6)+1;
	}

	public int rollDice(){
		die1=rollADie();
		die2=rollADie();
		return die1+die2;
	}

	public boolean rolledDouble(){

		boolean toReturn;
		toReturn = false;
		if(die1==die2){
			toReturn = true;
		}
		return toReturn;
	}

	public int getDie1() {
		return die1;
	}

	public int getDie2() {
		return die2;
	}
}