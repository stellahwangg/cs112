package games;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains methods to represent the Hunger Games using BSTs.
 * Moves people from input files to districts, eliminates people from the game,
 * and determines a possible winner.
 * 
 * @author Pranay Roni
 * @author Maksims Kurjanovics Kravcenko
 * @author Kal Pandit
 */
public class HungerGames {

    private ArrayList<District> districts;  // all districts in Panem.
    private TreeNode            game;       // root of the BST. The BST contains districts that are still in the game.

    /**
     * ***** DO NOT REMOVE OR UPDATE this method *********
     * Default constructor, initializes a list of districts.
     */
    public HungerGames() {
        districts = new ArrayList<>();
        game = null;
        StdRandom.setSeed(2023);
    }

    /**
     * ***** DO NOT REMOVE OR UPDATE this method *********
     * Sets up Panem, the universe in which the Hunger Games takes place.
     * Reads districts and people from the input file.
     * 
     * @param filename will be provided by client to read from using StdIn
     */
    public void setupPanem(String filename) { 
        StdIn.setFile(filename);  // open the file - happens only once here
        setupDistricts(filename); 
        setupPeople(filename);
    }

    /**
     * Reads the following from input file:
     * - Number of districts
     * - District ID's (insert in order of insertion)
     * Insert districts into the districts ArrayList in order of appearance.
     * 
     * @param filename will be provided by client to read from using StdIn
     */
    public void setupDistricts (String filename) {
        // WRITE YOUR CODE HERE
        int districtCount = games.StdIn.readInt();
        for(int i = 0; i < districtCount; i++){
            int districtID = games.StdIn.readInt();
            District district = new District(districtID);
            districts.add(district);
        }

    }

    /**
     * Reads the following from input file (continues to read from the SAME input file as setupDistricts()):
     * Number of people
     * Space-separated: first name, last name, birth month (1-12), age, district id, effectiveness
     * Districts will be initialized to the instance variable districts
     * 
     * Persons will be added to corresponding district in districts defined by districtID
     * 
     * @param filename will be provided by client to read from using StdIn
     */
    public void setupPeople (String filename) {
        // WRITE YOUR CODE HERE
        int numPlayers = games.StdIn.readInt();
        for(int i = 0; i < numPlayers; i++){
            
            String firstName = StdIn.readString();
            String lastName = StdIn.readString();
            int birthMonth = StdIn.readInt();
            int age = StdIn.readInt();
            int districtID = StdIn.readInt();
            int effectiveness = StdIn.readInt();

            boolean tessera = (age >= 12 && age < 18);
            Person person = new Person(birthMonth, firstName, lastName, age, districtID, effectiveness);

            //District district = districts.get(districtID);
            
            for(District district: districts){
                if (district.getDistrictID() == districtID) {
                    if (birthMonth % 2 == 0) {
                    district.addEvenPerson(person);
                } else {
                    district.addOddPerson(person);
                }      
                    break; // Exit the loop once the person is assigned to a district
                }
            }

            if (tessera) {
                person.setTessera(true);
            } else {
                person.setTessera(false);
            }
            
        }
    }

    /**
     * Adds a district to the game BST.
     * If the district is already added, do nothing
     * 
     * @param root        the TreeNode root which we access all the added districts
     * @param newDistrict the district we wish to add
     */
    public void addDistrictToGame(TreeNode root, District newDistrict) {
        // WRITE YOUR CODE HERE
        if(root == null){ //when the tree is empty, the district is the root
            game = new TreeNode(newDistrict, null, null);
            districts.remove(newDistrict);
        } else {
            if(newDistrict.getDistrictID() < root.getDistrict().getDistrictID()){ //comparing whether to go left or right
                //inserting in the left tree
                if(root.getLeft() == null){
                    root.setLeft( new TreeNode(newDistrict, null, null)); //create new TreeNode for left child
                    districts.remove(newDistrict);
                } else {
                    addDistrictToGame(root.getLeft(), newDistrict);
                }
            } else if(newDistrict.getDistrictID() > root.getDistrict().getDistrictID()){
                //inserting in the right tree
                if(root.getRight() == null){
                    root.setRight(new TreeNode(newDistrict, null, null)); //create new TreeNode for right child
                    districts.remove(newDistrict);
                } else {
                    addDistrictToGame(root.getRight(), newDistrict);
                }
            }
        }

    }

    /**
     * Searches for a district inside of the BST given the district id.
     * 
     * @param id the district to search
     * @return the district if found, null if not found
     */
    public District findDistrict(int id) {
        return findDistrictRecursively(game, id);
    }
    
    private District findDistrictRecursively(TreeNode current, int id) {
        if (current == null) {
            return null; // District not found in the BST
        }
    
        District district = current.getDistrict();
        int currentId = district.getDistrictID();
    
        if (id == currentId) {
            return district; // Found the district with the target ID
        } else if (id < currentId) {
            return findDistrictRecursively(current.getLeft(), id); // Search in the left subtree
        } else {
            return findDistrictRecursively(current.getRight(), id); // Search in the right subtree
        }
    }
    

    /**
     * Selects two duelers from the tree, following these rules:
     * - One odd person and one even person should be in the pair.
     * - Dueler with Tessera (age 12-18, use tessera instance variable) must be
     * retrieved first.
     * - Find the first odd person and even person (separately) with Tessera if they
     * exist.
     * - If you can't find a person, use StdRandom.uniform(x) where x is the respective 
     * population size to obtain a dueler.
     * - Add odd person dueler to person1 of new DuelerPair and even person dueler to
     * person2.
     * - People from the same district cannot fight against each other.
     * 
     * @return the pair of dueler retrieved from this method.
     */
    public DuelPair selectDuelers() {
        TreeNode rootNode = game;
        DuelPair duelPair = new DuelPair(null, null);
    
        selectOddWithTessera(rootNode, duelPair);
        selectEvenWithTessera(rootNode, duelPair);
    
        // If the pair is still incomplete, proceed with random selections
        if (duelPair.getPerson1() == null || duelPair.getPerson2() == null || 
            (duelPair.getPerson1().getDistrictID() != duelPair.getPerson2().getDistrictID())) {
            selectRandomOddWithoutTessera(rootNode, duelPair);
        }
    
        // Note: Adjusted the condition here
        if (duelPair.getPerson2() == null || duelPair.getPerson1() == null || 
            (duelPair.getPerson2().getDistrictID() != duelPair.getPerson1().getDistrictID())) {
            selectRandomEvenWithoutTessera(rootNode, duelPair);
        }
    
        return duelPair;
    }
    
    private void selectOddWithTessera(TreeNode node, DuelPair duelPair) {
        if (node == null || duelPair.getPerson1() != null) {
            return;
        }
    
        District currentDistrict = node.getDistrict();
        //List<Person> oddPopulation = new ArrayList<>(currentDistrict.getOddPopulation());
    
        for (Person person : currentDistrict.getOddPopulation()) {
            if (person.getTessera()) {
                duelPair.setPerson1(person);
                currentDistrict.getOddPopulation().remove(person);
                //System.out.println("Selected Odd Person with Tessera: " + person);
                break;
            }
            if (duelPair.getPerson2() != null && duelPair.getPerson2().getDistrictID() == person.getDistrictID()) {
                return;  // Back out when a person has been inserted into person1
            }
        }
    
        selectOddWithTessera(node.getLeft(), duelPair);
        selectOddWithTessera(node.getRight(), duelPair);
    }
    
    private void selectEvenWithTessera(TreeNode node, DuelPair duelPair) {
        if (node == null || duelPair.getPerson2() != null) {
            return;
        }
    
        District currentDistrict = node.getDistrict();
        //List<Person> evenPopulation = new ArrayList<>(currentDistrict.getEvenPopulation());
    
        for (Person person : currentDistrict.getEvenPopulation()) {
            if (person.getTessera()) {
                duelPair.setPerson2(person);
                currentDistrict.getEvenPopulation().remove(person);
                //System.out.println("Selected Even Person with Tessera: " + person);
                break;
            }
            if (duelPair.getPerson1() != null && duelPair.getPerson1().getDistrictID() == person.getDistrictID()) {
                return;  // Back out when a person has been inserted into person2
            }
        
        }
    
        selectEvenWithTessera(node.getLeft(), duelPair);
        selectEvenWithTessera(node.getRight(), duelPair);
    }
    
    private void selectRandomOddWithoutTessera(TreeNode node, DuelPair duelPair) {
        if (node == null || duelPair.getPerson1() != null) {
            return;
        }
    
        District currentDistrict = node.getDistrict();
        //List<Person> oddPopulation = new ArrayList<>(currentDistrict.getOddPopulation());
    
        if (!currentDistrict.getOddPopulation().isEmpty()) {
            int randomIndex = StdRandom.uniform(currentDistrict.getOddPopulation().size());
            Person randomOddPerson = currentDistrict.getOddPopulation().get(randomIndex);
    
            // Check if the randomly selected person is not in the same district as person2
            if (!randomOddPerson.getTessera() && 
                (duelPair.getPerson2() == null || randomOddPerson.getDistrictID() != duelPair.getPerson2().getDistrictID())) {
                duelPair.setPerson1(randomOddPerson);
                currentDistrict.getOddPopulation().remove(randomOddPerson);
            }
        }
    
        selectRandomOddWithoutTessera(node.getLeft(), duelPair);
        selectRandomOddWithoutTessera(node.getRight(), duelPair);
    }
    
    private void selectRandomEvenWithoutTessera(TreeNode node, DuelPair duelPair) {
        if (node == null || duelPair.getPerson2() != null) {
            return;
        }
    
        District currentDistrict = node.getDistrict();
        //List<Person> evenPopulation = new ArrayList<>(currentDistrict.getEvenPopulation());
    
        if (!currentDistrict.getEvenPopulation().isEmpty()) {
            int randomIndex = StdRandom.uniform(currentDistrict.getEvenPopulation().size());
            Person randomEvenPerson = currentDistrict.getEvenPopulation().get(randomIndex);
    
            // Check if the randomly selected person is not in the same district as person1
            if (!randomEvenPerson.getTessera() && 
                (duelPair.getPerson1() == null || randomEvenPerson.getDistrictID() != duelPair.getPerson1().getDistrictID())) {
                duelPair.setPerson2(randomEvenPerson);
                currentDistrict.getEvenPopulation().remove(randomEvenPerson);
            }
        }
    
        selectRandomEvenWithoutTessera(node.getLeft(), duelPair);
        selectRandomEvenWithoutTessera(node.getRight(), duelPair);
    }
    
    /**
     * Deletes a district from the BST when they are eliminated from the game.
     * Districts are identified by id's.
     * If district does not exist, do nothing.
     * 
     * This is similar to the BST delete we have seen in class.
     * 
     * @param id the ID of the district to eliminate
     */
    public void eliminateDistrict(int id) {
        game = eliminateDistrictRecursively(game, id);
    }
    
    private TreeNode eliminateDistrictRecursively(TreeNode ptr, int id) {
        if (ptr == null) {
            return ptr;
        }
    
        int currentId = ptr.getDistrict().getDistrictID();
    
        if (id < currentId) {
            ptr.setLeft(eliminateDistrictRecursively(ptr.getLeft(), id));
        } else if (id > currentId) {
            ptr.setRight(eliminateDistrictRecursively(ptr.getRight(), id));
        } else {
            // Node to delete found
            if (ptr.getLeft() == null) {
                return ptr.getRight();

            } else if (ptr.getRight() == null) {
                return ptr.getLeft();

            } else {
                // Node has two children, find the in-order successor
                TreeNode smallest = findSmallestNode(ptr.getRight());
                ptr.setDistrict(smallest.getDistrict());
                ptr.setRight(eliminateDistrictRecursively(ptr.getRight(), smallest.getDistrict().getDistrictID()));
            }
        }
    
        return ptr;
    }
    
    private TreeNode findSmallestNode(TreeNode node) {
        if (node.getLeft() == null) {
            return node;
        } else {
            return findSmallestNode(node.getLeft());
        }
    }   
    

    /**
     * Eliminates a dueler from a pair of duelers.
     * - Both duelers in the DuelPair argument given will duel
     * - Winner gets returned to their District
     * - Eliminate a District if it only contains a odd person population or even
     * person population
     * 
     * @param pair of persons to fight each other.
     */
    public void eliminateDueler(DuelPair pair) {
        Person person1 = pair.getPerson1();
        Person person2 = pair.getPerson2();
    
        if (person1 == null || person2 == null) {
            // Incomplete duel, return the person to their district
            if (person1 != null) {
                returnPersonToDistrict(person1);
            }
            if (person2 != null) {
                returnPersonToDistrict(person2);
            }
        } else {
            // Complete duel, determine the winner and loser
            Person winner = person1.duel(person2);
            Person loser = (winner == person1) ? person2 : person1; // Determine the loser
    
            // Return the winner to their district
            returnPersonToDistrict(winner);
    
            // Check if the loser's district has reached zero population
            District loserDistrict = findDistrict(loser.getDistrictID());
            District winnerDistrict = findDistrict(winner.getDistrictID());
    
            if (winnerDistrict.getOddPopulation().size() == 0 || winnerDistrict.getEvenPopulation().size() == 0) {
                eliminateDistrict(winnerDistrict.getDistrictID());
            }
    
            // Ensure the loser is removed from the district when it reaches zero population
            if (loserDistrict.getOddPopulation().isEmpty() && loserDistrict.getEvenPopulation().isEmpty()) {
                eliminateDistrict(loserDistrict.getDistrictID());
            }
        }
    }
    
    private void returnPersonToDistrict(Person person) {
        District district = findDistrict(person.getDistrictID());
    
        if (person.getBirthMonth() % 2 == 0) {
            district.getEvenPopulation().add(person);
        } else {
            district.getOddPopulation().add(person);
        }
    }    
    
    /**
     * ***** DO NOT REMOVE OR UPDATE this method *********
     * 
     * Obtains the list of districts for the Driver.
     * 
     * @return the ArrayList of districts for selection
     */
    public ArrayList<District> getDistricts() {
        return this.districts;
    }

    /**
     * ***** DO NOT REMOVE OR UPDATE this method *********
     * 
     * Returns the root of the BST
     */
    public TreeNode getRoot() {
        return game;
    }
}
