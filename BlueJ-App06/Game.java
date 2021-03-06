


import java.util.ArrayList;
/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 * 
 * Modified and extended by Your name
 */

public class Game 
{
    private Parser parser;
    public Room currentRoom;
    ArrayList<Item> inventory = new ArrayList<Item>();
    private int score;
    private int energy;
    
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theater, pub, lab, office, lounge, basement, 
        lair, pillar, box, wall, rubble, treasure, cage;
        energy = 50;
      
        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theater = new Room("in a lecture theater");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        lounge = new Room("in the lounge");
        basement = new Room("in the basement");
        lair = new Room("in a secret lair with treasure guarded by a dragon. There is a box south or a pillar north");
        pillar = new Room("hiding behind a pillar. There is some rubble north or a wall east ");
        box = new Room("attempting to hide in a box but the dragon saw you anyway. Game over");
        wall = new Room("crawling behind a short wall. The treasure is just ahead!");
        rubble = new Room("leaping over some rubble and get torched by the dragon. Game over.");
        cage = new Room("stepping into a cage. It shuts behind you. Game over.");
        treasure = new Room("now the owner of lots of money. You Win!");
        
        
        
        // initialise room exits
        outside.setExit("east", theater);
        outside.setExit("south", lab);
        outside.setExit("west", pub);

        theater.setExit("west", outside);
        theater.setExit("north", basement);

        pub.setExit("east", outside);

        lab.setExit("north", outside);
        lab.setExit("east", office);
        lounge.setExit("south", pub);

        office.setExit("west", lab);
        basement.setExit("south", theater);
        basement.setExit("east", lair);
        
        lair.setExit("west", basement);
        lair.setExit("south", box);
        lair.setExit("north", pillar);
        
        pillar.setExit("south", lair);
        pillar.setExit("north", rubble);
        pillar.setExit("east", wall);
        
        wall.setExit("west", pillar);
        wall.setExit("south", treasure);
        wall.setExit("east", cage);
        
        

        currentRoom = outside;  // start game outside
        
        lab.setItem(new Item("Map"));
        pub.setItem(new Item("Matches"));
        lounge.setItem(new Item("Rusty key"));
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        
        while (! finished) 
        {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        
        System.out.println("Your final score was:" + score);
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) 
        {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;
                
            case INVENTORY:
                printInventory();
                break;
                
            case GET:
                getItem(command);
                break;
            
            case ENERGY:
                printEnergy();
                break;
           
            case SCORE:
                printScore();
                break;
                
            

            case QUIT:
                wantToQuit = quit(command);
                break;
        }
        return wantToQuit;
    }
    
    // implementations of user commands:
    
    private void printInventory()
    {
        String output = "";
        for (int i = 0; i < inventory.size(); i++) {
            output += inventory.get(i).getDescription() + " ";
        }
        System.out.println("You currently have:");
        System.out.println(output);
    }

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }
    
    private void printEnergy()
    {
        System.out.println("Your current energy is: " + energy);
    }
    
    private void printScore()
    {
        System.out.println("Your current score is: " + score);
    }
   
    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) 
        {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
            energy = energy - 1;
            score = score + 1;
           
        
    }
    return;
    }
    
    /** 
     * Try to pick up item. If there is an item, pick it 
     * up, otherwise print an error message.
     */
    private void getItem(Command command) 
    {
        if(!command.hasSecondWord()) 
        {
            // if there is no second word, we don't know where to go...
            System.out.println("Get what?");
            return;
        }

        String item = command.getSecondWord();

        // Try to leave current room.
        Item newItem = currentRoom.getItem(item);

        if (newItem == null) {
            System.out.println("That item is not in this room");
        }
        else {
            inventory.add(newItem);
            currentRoom.removeItem(item);
            energy = energy - 5;
            score = score + 10;
            System.out.println("Picked up:" + item);
            
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
