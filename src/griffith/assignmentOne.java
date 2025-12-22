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

	public static void basicAttack(Fighter attacker, Fighter defender){
		defender.setHp(defender.getHp() - (attacker.getAtk() / defender.getDef()));
		
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
			basicAttack(attacker, defender);
			
		}
		else if (attacker.getChampion().equals("Pantheon")){
			attacker.setAtk(attacker.getAtk() + 5); // maybe modify how much attack they gain later on
			basicAttack(attacker, defender);
		}

		//continue
	}
	//TODO maybe somehow make a method for try_catch, cuz we have alot of them here so far

	//static int random_fighter = new Random().nextInt(1,5); 
	

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
									fighter_choice = scanner.nextInt() - 1;
								}
								catch(InputMismatchException e){
									System.out.println("\nPlease use numbers for the menu choice.");
									scanner.nextLine();
									continue;
								}
								scanner.nextLine();

								System.out.println(String.format("Player fighter: %s \nBot fighter: %s",fighter_options[fighter_choice], fighter_options[random_fighter]));
								

								int turn = 1;
								
								Fighter player_figther = new Fighter(100, 10, 5, fighter_options[fighter_choice]);
								
								Fighter bot_figther = new Fighter(100, 10, 5, fighter_options[random_fighter]);

								do{
									System.out.println(String.format("\nTurn %s", (turn++)));


									//ask player what attack they wanna use: basic, special etc
									System.out.println("Use Basic Attack, Special Attack"); // TODO make this into ASCII later
									int attack_choice = 0;

									try{
										attack_choice = scanner.nextInt(); //TODO if they input anything thats not the attacks, give them an error and dont let bot attack
									}
									catch(InputMismatchException e){
										System.out.println("\nPlease use numbers for the attack choice.");
										scanner.nextLine();
										continue;
									}
									scanner.nextLine();

									switch(attack_choice){ //player attacks
										case 1 ->{
											basicAttack(player_figther, bot_figther);
											break;
										}

										case 2 ->{
											specialAttack(player_figther, bot_figther);
											break;
										}
										
									}

									int random_attack = new Random().nextInt(1,3); // num range 1-2. consider moving it to main while loop

									switch (random_attack) { //bot attacks
										case 1 ->{
											basicAttack(bot_figther, player_figther);
											System.out.println("Bot chose to use basic attack!\n");
											break;
										}

										case 2 -> {
											specialAttack(bot_figther, player_figther);
											System.out.println(String.format("Bot chose to use %s's special attack!\n",bot_figther.getChampion()));
											break;
										}
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
