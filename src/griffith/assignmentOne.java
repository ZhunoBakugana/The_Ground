package griffith;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.ObjectStreamClass;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Random;

public class assignmentOne {
	
	//maybe let the computer to play the same character as the player and in that case just delete this
	public static String[] removeChosenCharacter(String[] array, int remove_index){
		/*if (array == null || remove_index < 0 || remove_index >= array.length){
			return array;
		} idk what this si for check later*/ 
		
		String updated_fighter_list[] = new String[array.length -1];
		
		
		// Copy the elements except the index
        // from original array to the other array
		for (int i = 0, k = 0; i < array.length; i++){
			if (i ==remove_index)
				continue;
			
			updated_fighter_list[k++] = array[i];
		}
		return updated_fighter_list;
	}

	public static void basicAttack(Fighter attacker, Fighter defender, boolean count){
		defender.setHp(defender.getHp() - (attacker.getAtk() / defender.getDef()));
		if(count){
			attacker.setBasicAttackCount(attacker.getBasicAttacksCount() + 1);
		}
	}

	public static void specialAttack(Fighter attacker, Fighter defender){
    	if(attacker.getChampion().equals("Aatrox")){ //ask prof if we can use enums
       	 	defender.setHp(defender.getHp() - (attacker.getAtk() /defender.getDef()) * 2);
			attacker.setHp(attacker.getHp() + 15);
   	 	}
		else if (attacker.getChampion().equals("Kayle")){
			defender.setHp(defender.getHp() - (attacker.getAtk()/(defender.getDef()/2)));
			
		}
		else if (attacker.getChampion().equals("Kayn")){
			defender.setDef(defender.getDef() - 1); // maybe modify how much defence they lose later on. TODO also fix ArithmeticException :3
			basicAttack(attacker, defender, NO_METER);
			
		}
		else if (attacker.getChampion().equals("Pantheon")){
			attacker.setAtk(attacker.getAtk() + 5); // maybe modify how much attack they gain later on
			basicAttack(attacker, defender, NO_METER);
		}

		//continue
	}
	//TODO maybe somehow make a method for try_catch, cuz we have alot of them here so far

	//static int random_fighter = new Random().nextInt(1,5); 
	
	static String[] available_attacks = {"Basic attack", "Special attack"};

	static int basic_attacks_needed = 3;

	final static boolean COUNT_METER = true;
	final static boolean NO_METER = false;

	public static void main(String[] args){

		Scanner scanner = new Scanner(System.in);

		String[] fighter_options = {"Aatrox", "Kayn", "Kayle", "Pantheon"}; // TODO ASCII art of champions + their names in borders

		while(true){

		int random_fighter = new Random().nextInt(1,5); //num range: 1-4.

			/*File myObj = new File("src/files/menu.txt");

			try (Scanner myReader = new Scanner(myObj)) {
				while (myReader.hasNextLine()) {
					String data = myReader.nextLine();
					System.out.println(data);
				}
    		} 	3
			catch (FileNotFoundException e) {
				System.out.println("An error occurred.");
	 			e.printStackTrace();
			}*/
			
			System.out.println("Choose options from 1 to 5: ");
			int menu_choice = 0; 

			try{
				menu_choice = scanner.nextInt();
			}
			catch(InputMismatchException e){
				System.out.println("Please use numbers for the menu choice.");
				scanner.nextInt();
			}
			scanner.nextLine();

			switch(menu_choice){
				case 1 -> {
					//story mode
					// TODO to unlock Aatrox, they must beat Story Mode
				}

				case 2 -> {
					//free to play

					System.out.println("Choose a character(C) or play a random(R) one?");
					
					String random_choice_or_not = scanner.nextLine().replaceAll("\\s+", "").toUpperCase();
					
					if (random_choice_or_not.matches("[CR]")){
						switch(random_choice_or_not){
							case "C" -> {
								System.out.println("You can choose from: \n");
								for(int i = 0; i< fighter_options.length;i++){
									System.out.println((i+1) + "." + fighter_options[i]);
								}

								System.out.println("Make your choice! \n");
								int fighter_choice = 0;
								
								try{
									fighter_choice = scanner.nextInt() - 1; //TODO this line could be giving IndexOutOfBoundException, please have a look
									System.out.println("This is the value of fighter choice: "+ fighter_choice + "\n");
								}
								catch(InputMismatchException e){
									System.out.println("\nPlease use numbers for the fighter choice!");
									scanner.nextLine();
									continue; // TODO maybe make the player go back to the start of this case instead of menu 
								}
								scanner.nextLine();

								System.out.println(String.format("Player fighter: %s \nBot fighter: %s",fighter_options[fighter_choice], fighter_options[random_fighter]));
								

								int turn = 1;
								
								Fighter player_figther = new Fighter(100, 10, 5, fighter_options[fighter_choice]);
								
								Fighter bot_figther = new Fighter(100, 10, 5, fighter_options[random_fighter]);

								do{
									System.out.println(String.format("\nTurn %s", (turn++))); //TODO the counter shouldn't go up in case of a triggered check/error that doesn't allow the player or bot to attack


									//ask player what attack they wanna use: basic, special etc
									System.out.println("Choose attack type!");

									for (int i = 0; i < available_attacks.length; i++){
										System.out.print(String.format("%d.%s ", (i+1), available_attacks[i]));

									}

									//System.out.println("1.Basic Attack, 2.Special Attack"); // TODO make this into ASCII later
									
									int attack_choice = 0;

									try{
										attack_choice = scanner.nextInt() ; 
									}
									catch(InputMismatchException e){
										System.out.println("\nPlease use numbers for the attack choice.");
										scanner.nextLine();
										continue;
									}
									scanner.nextLine();
									
									/*if attack choice = 2 while basic attack meters inst 3, give user error message telling them to basic attack */


									if(attack_choice > 0 && attack_choice <=2){ //TODO adjust attack_choice range once we got all the attack types
										switch(attack_choice){ //player attacks
											case 1 ->{
												basicAttack(player_figther, bot_figther, COUNT_METER);
												//player_figther.setBasicAttackCount(player_figther.getBasicAttacksCount() + 1);
												System.out.println("basic attack count is: "+player_figther.getBasicAttacksCount()); //remove after testing is complete
												break;
											}

											case 2 ->{
												if(player_figther.getBasicAttacksCount() != basic_attacks_needed){
													//System.out.println(String.format("\nPlease use %s basic attacks first.\n", basic_attacks_needed));
													 //will reset basic attack counter
													continue;
												}
												else{
													specialAttack(player_figther, bot_figther);
													player_figther.setBasicAttackCount(player_figther.getBasicAttacksCount() - basic_attacks_needed); // special attacks consume the basic-attack meter

													//System.out.println("basic attack count value after special attack is: " + player_figther.getBasicAttacksCount());
												}
												
												break;
											}
										
										}

										int random_attack = new Random().nextInt(1,3); //TODO adjust range once all attacks have been added. num range 1-2. consider moving it to main while loop
										switch (random_attack) {
											case 1 -> {
												basicAttack(bot_figther, player_figther, COUNT_METER);
												break;
												

											}
											case 2 -> {
												if(bot_figther.getBasicAttacksCount() !=basic_attacks_needed){
													basicAttack(bot_figther, player_figther, COUNT_METER);
												}
												else{												
													specialAttack(bot_figther, player_figther);
													bot_figther.setBasicAttackCount(bot_figther.getBasicAttacksCount() - basic_attacks_needed); // special attacks consume the basic-attack meter


												}
												break;

											}
										}
										System.out.println(String.format("Bot chose to use %s!\n", available_attacks[random_attack-1]));
									}
									else{
										System.out.println("Please choose a viable attack!");
										continue;
									}
									//print out updated player and bot stats
									System.out.println(String.format("Player Stats: Hp: %s, Atk: %s, Def: %s", player_figther.getHp(), player_figther.getAtk(), player_figther.getDef()));
									System.out.println(String.format("Bot Stats: Hp: %s, Atk: %s, Def: %s\n", bot_figther.getHp(), bot_figther.getAtk(), bot_figther.getDef()));

									//break; // remove after combat logic is implemented
								}
								while(player_figther.getHp() > 0 && bot_figther.getHp() > 0 ); //keep going until either player or bot wins
								

							}
							case "R" -> {
								//System.out.println(character_choice);
								System.out.println(fighter_options[random_fighter]);
							}
						}
					}
					else{
						System.out.println("Please enter either C or R!\n");
					}


				}

				case 3 -> {
					//settings
				}

				case 4 ->{
					//player statistics
				}

				case 5 ->{
					return;
				} 
			}
			
			
			//return;
		}
	}

}
