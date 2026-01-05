package griffith;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Random;

public class assignmentOne {

	// static int random_fighter = new Random().nextInt(1,5);
	static Scanner scanner = new Scanner(System.in);
	static String[] available_attacks = { "Basic attack", "Special attack" };
	static int basic_attacks_needed = 3;

	final static boolean COUNT_METER = true;
	final static boolean NO_METER = false;

	final static boolean PLAYER = true;
	final static boolean NON_PLAYER = false;

	static String[] fighter_options = { "Aatrox", "Kayn", "Kayle", "Pantheon" };

	static File file = new File("src/files/player_statistics.txt");

	static PlayerStatistics stats = new PlayerStatistics();
	

	// maybe let the computer to play the same character as the player and in that
	// case just delete this
	public static String[] removeChosenCharacter(String[] array, int remove_index) {
		/*
		 * if (array == null || remove_index < 0 || remove_index >= array.length){
		 * return array;
		 * } idk what this si for check later
		 */

		String updated_fighter_list[] = new String[array.length - 1];

		// Copy the elements except the index
		// from original array to the other array
		for (int i = 0, k = 0; i < array.length; i++) {
			if (i == remove_index)
				continue;

			updated_fighter_list[k++] = array[i];
		}
		return updated_fighter_list;
	}

	public static void basicAttack(Fighter attacker, Fighter defender, boolean count, boolean player) {
		int dmg_done =  (attacker.getAtk() / defender.getDef());
		defender.setHp(defender.getHp() - dmg_done);
		//Fighter.setBasicAttackDamageDone(dmg_done);
		if (count) {
			attacker.setBasicAttackCount(attacker.getBasicAttacksCount() + 1);
			attacker.setBasicAttackCountForStatistics(attacker.getBasicAttacksCountForStatistics() + 1);
		}
		if(player){
			attacker.setBasicAttackDamageDone(attacker.getBasicAttackDamageDone() + dmg_done);
		}
	}

	public static void specialAttack(Fighter attacker, Fighter defender, boolean player) { //TODO make the bot always use special attack when it's up
	int dmg_done = 0;
		if (attacker.getChampion().equals("Aatrox")) { 
			dmg_done = (attacker.getAtk() / defender.getDef()) * 2;
			defender.setHp(defender.getHp() - dmg_done );
			attacker.setHp(attacker.getHp() + 15);
		} else if (attacker.getChampion().equals("Kayle")) {
			dmg_done = (attacker.getAtk() / (defender.getDef() / 2));
			defender.setHp(defender.getHp() - dmg_done);

		//Patheon and Kayn just affect enemy stats and then basic attack. Which is why they're not contributing to the specialAttackDamage Counter
		} else if (attacker.getChampion().equals("Kayn")) {
			defender.setDef(defender.getDef() - 1); // maybe modify how much defence they lose later on. TODO also fix ArithmeticException :3
			basicAttack(attacker, defender, NO_METER, NON_PLAYER); 

		} else if (attacker.getChampion().equals("Pantheon")) {
			attacker.setAtk(attacker.getAtk() + 5); // maybe modify how much attack they gain later on
			basicAttack(attacker, defender, NO_METER, NON_PLAYER);
		}
		//TODO think about NON_PLAYER here

		if(player){
			attacker.setSpecialAttackDamageDone(attacker.getSpecialAttackDamageDone() + dmg_done);
		}

		// continue
	}

	//TODO was gonna write some comment about creating a method but forgot what the method was gonna be about

	public static boolean combatLogic(String[] fighter_options, int fighter_choice, int random_bot) {
		int turn = 1;
		boolean player_won = false;
		Fighter player_figther = new Fighter(100, 10, 5, fighter_options[fighter_choice]);
		Fighter bot_figther = new Fighter(100, 10, 5, fighter_options[random_bot]);
		do {
			System.out.println(String.format("\nTurn %s", (turn)));
			// ask player what attack they wanna use: basic, special etc
			System.out.println("Choose attack type!");
			for (int i = 0; i < available_attacks.length; i++) {
				System.out.print(String.format("%d.%s ", (i + 1), available_attacks[i]));
			}
			// System.out.println("1.Basic Attack, 2.Special Attack"); // TODO make this
			// into ASCII later
			int attack_choice;
			try {
				attack_choice = scanner.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("\nPlease use numbers for the attack choice!");
				scanner.nextLine();
				continue;
			}
			scanner.nextLine();
			if (attack_choice > 0 && attack_choice <= 2) { // TODO adjust attack_choice range once
															// we
															// got all the attack types
				switch (attack_choice) { // TODO maybe add an option to quit game
					case 1 -> {
						basicAttack(player_figther, bot_figther, COUNT_METER, PLAYER);
						System.out.println(
								"basic attack count is: " + player_figther.getBasicAttacksCount()); // remove
																									// after
																									// testing
																									// is
																									// complete
						break;
					}
					case 2 -> {
						if (player_figther.getBasicAttacksCount() < basic_attacks_needed) {
							System.out
									.println(String.format("\nPlease use %s more basic attacks!\n",
											(basic_attacks_needed
													- player_figther.getBasicAttacksCount())));
							continue;
						} else {
							specialAttack(player_figther, bot_figther, PLAYER);
							player_figther.setBasicAttackCount(player_figther.getBasicAttacksCount() - basic_attacks_needed); // special
																									// attacks
																									// consume
																									// the
																									// basic-attack
																									// meter
							// System.out.println("basic attack count value after special attack is:
							// " +
							// player_figther.getBasicAttacksCount());
						}
						break;
					}
				}
				int random_attack = new Random().nextInt(1, 3); // TODO adjust range once all
																// attacks
																// have been added. num range 1-2.
																// consider moving it to main while
																// loop
				switch (random_attack) {
					case 1 -> {
						basicAttack(bot_figther, player_figther, COUNT_METER, NON_PLAYER);
						break;
					}
					case 2 -> {
						if (bot_figther.getBasicAttacksCount() != basic_attacks_needed) {
							basicAttack(bot_figther, player_figther, COUNT_METER, NON_PLAYER);
						} else {
							specialAttack(bot_figther, player_figther, NON_PLAYER);
							bot_figther.setBasicAttackCount(
									bot_figther.getBasicAttacksCount() - basic_attacks_needed); // special
																								// attacks
																								// consume
																								// the
																								// basic-attack
																								// meter
						}
						break;
					}
				}
				System.out.println(
						String.format("Bot chose to use %s!\n",
								available_attacks[random_attack - 1]));
			} else {
				System.out.println("Please choose a viable attack!");
				continue;
			}

			// increase turn count by 1
			turn++;
			// print out updated player and bot stats
			System.out.println(String.format("Player Stats: Hp: %s, Atk: %s, Def: %s",
					player_figther.getHp(), player_figther.getAtk(), player_figther.getDef()));
			System.out.println(String.format("Bot Stats: Hp: %s, Atk: %s, Def: %s\n",
					bot_figther.getHp(), bot_figther.getAtk(), bot_figther.getDef()));

			if (player_figther.getHp() <= 0) {
				player_won = false; // idk maybe edit this or player_won initialization
			} else if (bot_figther.getHp() <= 0) {
				player_won = true;
			}

		} while (player_figther.getHp() > 0 && bot_figther.getHp() > 0); // keep going until either player or bot wins
		stats.addStats(player_figther, bot_figther); //update combat stats after round

		//update games played/won
		stats.games_played++;
		if(player_won){
			stats.games_won++;
		}
		else{
			stats.games_lost++;
		}

		saveStats(); //update player statistics 

		return player_won;
	}

	public static boolean continue_fight(String choice) {
		boolean rerun;
		if (choice.equals("r")) {
			rerun = true;
		} else {
			rerun = false;
		}

		return rerun;
	}
	int random_player_fighter = new Random().nextInt(fighter_options.length); // num range: 1-3*/
	
	public static int randomFighter(){ // making random_fighter into static wouldn't be able to change each time, the player wishes to re-fight in random mode, hence why this method exists
		int random_fighter = new Random().nextInt(fighter_options.length); // num range: 1-3
		return random_fighter;
	}

	public static void saveStats(){
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
			oos.writeObject(stats);
			//oos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void loadStats(){
		if (!file.exists() || file.length() == 0) { //if a file doesn't exist or if it's empty we create new stats
			stats = new PlayerStatistics();
			return;//once new stats are created we immediately exit this method as to not trigger EOFException
			}
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {//the program tries reading the header immediately from the file, since serialized files start with a header and empty files wouldn't have one, we get a EOFException. hence why we have the above if()
				stats = (PlayerStatistics) ois.readObject(); //ois.readObject() returns an object and we cast it to PlayerStatistics
			} 
			
			 catch (IOException | ClassNotFoundException e) {
				stats = new PlayerStatistics();
				e.printStackTrace();
			} 
	}

	public static void main(String[] args) {
		loadStats(); //load player statistics at game start

		 // TODO ASCII art of champions + their
																				// names in borders
		while (true) {

			
			
			/*
			 * File myObj = new File("src/files/menu.txt");
			 * 
			 * try (Scanner myReader = new Scanner(myObj)) {
			 * while (myReader.hasNextLine()) {
			 * String data = myReader.nextLine();
			 * System.out.println(data);
			 * }
			 * } 3
			 * catch (FileNotFoundException e) {
			 * System.out.println("An error occurred.");
			 * e.printStackTrace();
			 * }
			 */

			System.out.println("Choose options from 1 to 5: "); // TODO maybe make the 1-5, not static
			int menu_choice;

			try {
				menu_choice = scanner.nextInt(); // TODO. players are able to enter an out of range number and the
													// program prompts them to enter a choice again, which is fine but
													// maybe consider giving players an error message
			} catch (InputMismatchException e) {
				System.out.println("Please use numbers for the menu choice!");
				scanner.nextLine();
				continue;
			}
			scanner.nextLine();

			switch (menu_choice) {
				case 1 -> {
					// story mode
					// TODO to unlock Aatrox, they must beat Story Mode
				}

				case 2 -> {
					// free to play
					System.out.println("Choose a character(C) or play a random(R) one?");

					String random_choice_or_not = scanner.nextLine().replaceAll("\\s+", "").toUpperCase();
					while (true) { // in case the user uses a character that's not a number for the character
									// choise, we'll keep asking them to do so
						if (random_choice_or_not.equals("C")) {
							System.out.println("You can choose from: \n");
							for (int i = 0; i < fighter_options.length; i++) {
								System.out.println((i + 1) + "." + fighter_options[i]);
							}

							System.out.println("Make your choice! \n");
							int fighter_choice;

							try {
								fighter_choice = scanner.nextInt() - 1;
								System.out.println(
										String.format("This is the value of fighter choice: %s", fighter_choice));
							} catch (InputMismatchException e) {
								System.out.println("\nPlease use numbers for the fighter choice!");
								scanner.nextLine();
								continue;
							}

							try {
								System.out.println(String.format("Player fighter: %s \nBot fighter: %s",
										fighter_options[fighter_choice], fighter_options[randomFighter()])); 
							} catch (ArrayIndexOutOfBoundsException e) {
								System.out.println("Choice out of range!");
								scanner.nextLine();
								continue;
							}
							scanner.nextLine();
							

							if (combatLogic(fighter_options, fighter_choice, randomFighter())) { 
								System.out.println("Player won!");
							} else {
								System.out.println("Bot won!");
							}

							// TODO after combat message + "fight again" logic should be a method since both game modes will need it
							//String fight_on;
							System.out.print("\nWould you like to continue fighting(r) or click any other key to go back to menu? ");
							String fight_on = scanner.nextLine().replaceAll("\\s+", "").toLowerCase(); 

							if (continue_fight(fight_on)) {
								continue;
							} else {
								break;
							}
						}

						else if (random_choice_or_not.equals("R")) {
							
							System.out.println(String.format("Player fighter is: %s",fighter_options[randomFighter()]));
							System.out.println(String.format("Bot fighter is: %s",fighter_options[randomFighter()]));

							if (combatLogic(fighter_options, randomFighter(), randomFighter())) { 
								System.out.println("Player won!");
							} else {
								System.out.println("Bot won!");
							}				
							//String fight_on;
							System.out.print("\nWould you like to continue fighting(r) or click any other key to go back to menu? ");
							String fight_on = scanner.nextLine().replaceAll("\\s+", "").toLowerCase();
							

							if (continue_fight(fight_on)) {
								continue;
							} else {
								break;
							}		

							//System.out.println(fighter_options[random_fighter]);
							//break;
						}

						else {
							// prints out error message and restarts the main loop
							System.out.println("Please enter either C or R!\n");
							break;
						}
					}
				}

				case 3 -> {
					// settings
				}

				case 4 -> {
					// player statistics
					//maybe transfer code into method
					System.out.println("Basic damage done: " + stats.basic_attack_dmg_done);
					System.out.println("Special damage done: " + stats.special_attack_dmg_done);
					System.out.println("Basic attacks: " + stats.basic_attack_counter);
					System.out.println("Games played: " + stats.games_played);
					System.out.println("Games won: " + stats.games_won);
					System.out.println("Games lost: " + stats.games_lost);
				}

				case 5 -> {
					return;
				}
			}

			// return;
		}
	}

}
