/**
 * UserInterface.java
 *
 * @author: Phuong Do
 * @author: Chris Vo
 * @author: Skyler Berry
 * @author: Paolo Pedrigal
 * @author: Arushi Sharma
 * @author: Shohin Abdulkhamidov
 * CIS 22C Group Project
 */

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;


public class UserInterface {
    public static void main(String[] args) throws IOException {
        int num_interests = 22;
        int num_users = 16;
        HashTable<Interest> interests = new HashTable<>(3 * num_interests);
        HashTable<User> names = new HashTable<>(3 * num_users); //
        HashTable<User> userLogin = new HashTable<>(3 * num_users);

        ArrayList<User> userID = new ArrayList<>();
        ArrayList<BST<User>> userInterest = new ArrayList<>();
        List<String> passwords = new List<>();
        List<String> usernames = new List<>();

        Graph network = new Graph(num_users);

        String name, userName, password, location, friendName, interestName;
        int numFriend;
        int numInterest;
        int count = 0;

        NameComparator c = new NameComparator();

        File file = new File("users.txt");
        Scanner input = new Scanner(file);

        // reading in from users.txt and creating base Users
        while (input.hasNext()) {
            // reading in
            name = input.nextLine();
            userName = input.nextLine();
            password = input.nextLine();
            location = input.nextLine();

            usernames.addLast(userName);
            passwords.addLast(password);

            // processing the interests for this specific user
            numInterest = input.nextInt();
            input.nextLine();
            List<Interest> l = new List<>();
            for (int i = 0; i < numInterest; i++) {
                interestName = input.nextLine();
                Interest temp = new Interest(interestName);

                if (!interests.contains(temp, temp.getInterestName())) { // new interest
                    temp.setID(count);
                    userInterest.add(new BST<>()); // add to BST<User> that share the same interest
                    count++;
                    interests.put(temp, temp.getInterestName()); // add to Interest HashTable
                } else {
                    temp.setID(interests.get(temp, temp.getInterestName()).getID()); // existing interest, set id, no need to add to HashTable again
                }
                l.addLast(temp); // add to list of the user's interests
            }

            // creating the user's list of friends
            numFriend = input.nextInt();
            input.nextLine();

            ArrayList<User> al = new ArrayList<>();
            for (int i = 0; i < numFriend; i++) {
                friendName = input.nextLine();
                User userTemp = new User(friendName);
                al.add(userTemp);
            }

            input.nextLine(); // reading blank line between users

            User user = new User(name, userName, password, location, al, l);

            userID.add(new User());
            userID.set(User.getUserID() - 1, user);//arraylist<user> index by userID

            l.placeIterator();
            for (int i = 0; i < l.getLength(); i++) {
                int index = l.getIterator().getID();
                userInterest.get(index).insert(user, c);
                l.advanceIterator();
            }// getting BST of users who share the same interest, indexing by interestID

            userLogin.put(user, user.getUserName() + user.getPassword());
            names.put(user, user.getName());
        }

        input.close();

        //re-populate BST with the whole object
        for (int i = 0; i < userID.size(); i++) {
            User u = userID.get(i);
            for (int j = 0; j < userID.size(); j++) {
                User friend = u.getFriend(new User(userID.get(j).getName()));
                if (friend != null) {
                    if (userID.get(j).getFriend(new User(u.getName())) != null) {
                        u.removeFriend(friend); // remove the friend that only has the name
                        u.addFriend(userID.get(j)); // re-insert the user with the rest of the information
                    }
                }
            }
        }

        //new StartFrame(userLogin, names, interests, userInterest, userID, network);//GUI
        Scanner keyboard = new Scanner(System.in);

        menu(keyboard, num_users, userLogin, names, interests, userInterest, userID, network, usernames, passwords);
        keyboard.close();
    }


    // additional method to run the menu options
    static public void menu(Scanner keyboard, int num_users, HashTable<User> login, HashTable<User> names, HashTable<Interest> interestTable, ArrayList<BST<User>> usersInterest, ArrayList<User> userID, Graph network, List<String> usernames, List<String> passwords) {

        // Login
        String inputUsername, inputPassword, inputName, inputCity;
        int inputNumInterest = 0;
        User tempUser;

        int strikes = 0; // user has 3 strikes to correctly input login info

        do {
            // login to pre-existing account
            System.out.print("Please enter your username: ");
            inputUsername = keyboard.nextLine();
            System.out.print("Please enter your password: ");
            inputPassword = keyboard.nextLine();
            tempUser = new User(inputUsername, inputPassword); // tempUser to search for in the HashTable<User> allUsers;

            if (login.get(tempUser, inputUsername + inputPassword) != null) { // If the login exists
                System.out.println("\nWelcome, " + login.get(tempUser, inputUsername + inputPassword).getName() + "!");
                break;
            } else {
                strikes++;
                if (strikes == 3) {
                    System.out.print("\nWe don't have your account on file...");

                    System.out.print("\n\nLet's create an account for you!");

                    System.out.print("\nEnter your name: ");
                    inputName = keyboard.next();
                    inputName += keyboard.nextLine();

                    System.out.print("Enter your city: ");
                    inputCity = keyboard.next();
                    inputCity += keyboard.nextLine();

                    // if user inputs an existing username and password combination
                    boolean invalid1 = true;
                    do {
                        System.out.print("Enter your new username: ");
                        inputUsername = keyboard.next();
                        inputUsername += keyboard.nextLine();

                        System.out.print("Enter your new password: ");
                        inputPassword = keyboard.next();
                        inputPassword += keyboard.nextLine();

                        if (login.contains(new User(inputUsername, inputPassword), inputUsername + inputPassword)) {
                            System.out.println("\nUsername/password is already taken!\n");
                        } else if (usernames.linearSearch(inputUsername) == -1 && passwords.linearSearch(inputPassword) != -1) {
                            System.out.println("\nPassword is taken!\n");
                        } else if (usernames.linearSearch(inputUsername) != -1 && passwords.linearSearch(inputPassword) == -1) {
                            System.out.println("\nUsername is taken!\n");
                        } else {
                            invalid1 = false;
                        }

                    } while (invalid1);

                    usernames.addLast(inputUsername);
                    passwords.addLast(inputPassword);

                    System.out.println("\nHow many interests do you have?");

                    boolean invalid = true;
                    do {
                        try {
                            System.out.print("\nPlease enter at least 2: ");
                            inputNumInterest = keyboard.nextInt();
                            if (inputNumInterest < 2) {
                                System.out.println("Please enter a valid number.");
                            } else {
                                invalid = false;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Please enter a valid number.");
                            keyboard.nextLine();
                        }
                    } while (invalid);


                    List<Interest> inputInterest = new List<>();
                    for (int i = 0; i < inputNumInterest; i++) {
                        System.out.print("\nEnter your interest: ");
                        String interestName = keyboard.next();
                        interestName += keyboard.nextLine();
                        Interest newInterest = new Interest(interestName);
                        if (!interestTable.contains(newInterest, interestName)) {
                            usersInterest.add(new BST<>());
                            newInterest.setID(usersInterest.size() - 1);
                            interestTable.put(newInterest, interestName);

                        } else {
                            int id = interestTable.get(newInterest, interestName).getID();
                            newInterest.setID(id);

                        }
                        inputInterest.addLast(newInterest);
                    }
                    tempUser = new User(inputName, inputUsername, inputPassword, inputCity, inputInterest);

                    num_users++;
                    login.put(tempUser, inputUsername + inputPassword);
                    names.put(tempUser, inputName);
                    userID.add(tempUser);

                    System.out.println("\nYour profile has been created! Welcome " + inputName + "!");

                } else {
                    System.out.println("\nIncorrect login! You have " + (3 - strikes) + " attempts remaining.\n");
                }
            }

        } while (strikes < 3);

        User mainUser = login.get(tempUser, inputUsername + inputPassword);

        boolean choseToExit = false;
        while (!choseToExit) {
            System.out.print("\nPlease select from the following options:\n(1) View your friends\n(2) Search for a new friend\n(3) Get a recommendation for new friends\n(4) Exit\nEnter your choice: ");

            String menuOption;
            menuOption = keyboard.nextLine();
            switch (menuOption) {
                case "1":
                    // view friends
                    viewFriends(keyboard, mainUser);
                    break;
                case "2":
                    // search for new friends
                    searchFriends(keyboard, names, usersInterest, mainUser, interestTable);
                    break;
                case "3":
                    friendRec(keyboard, names, network, userID, usersInterest, mainUser);
                    break;
                case "4":
                    choseToExit = true;
                    saveData(userID);
                    System.out.println("\nGoodbye!");
                    break;
                default:
                    System.out.println("Invalid menu option number. Please try again.");
                    break;
            }

        }
    }

    static public void viewFriends(Scanner keyboard, User mainUser) {
        if (mainUser.hasNoFriends()) {
            System.out.println("Your friend list is empty. Please search and add friends to your friend list.");
        } else {
            System.out.println("\nHere's the list of your friends: ");
            mainUser.printFriendsByName();
            System.out.println();
            System.out.print("\nWould you like to select a friend to view profile (S) or remove a friend (R) or exit (X)? ");
            String viewOption = keyboard.nextLine();

            if (viewOption.equalsIgnoreCase("S")) {
                User friend = new User();
                if (mainUser.getFriends().size() == 1) {
                    friend = mainUser.getFriendsSorted().get(0);
                } else {
                    System.out.print("Please enter the friend you want to select (1-" + mainUser.getFriends().size() + "): ");
                    boolean more = true;
                    while (more) {
                        try {
                            int option = keyboard.nextInt();
                            if (option > mainUser.getFriends().size() || option <= 0) {
                                System.out.println("Invalid number!\n");
                                System.out.print("Please enter the friend you want to select (1-" + mainUser.getFriends().size() + "): ");
                            } else {
                                keyboard.nextLine(); // clear buffer
                                friend = mainUser.getFriendsSorted().get(option - 1);
                                more = false;
                            }
                        } catch (InputMismatchException e) {
                            keyboard.nextLine();
                            System.out.println("Invalid number!\n");
                            System.out.print("Please enter the friend you want to select (1-" + mainUser.getFriends().size() + "): ");
                        }
                    }
                }

                if (friend == null) {
                    System.out.println("The friend you entered is not on your friend list.");
                } else {
                    System.out.println(friend + "\n" + friend.getName() + "'s friends: ");
                    friend.printFriendsByName();
                    System.out.println();
                }

            } else if (viewOption.equalsIgnoreCase("R")) {
                User friend = new User();
                if (mainUser.getFriends().size() == 1) {
                    friend = mainUser.getFriendsSorted().get(0);
                } else {
                    System.out.print("Please enter the friend you want to remove (1-" + mainUser.getFriends().size() + "): ");
                    boolean more = true;
                    while (more) {
                        try {
                            int option = keyboard.nextInt();
                            if (option > mainUser.getFriends().size() || option <= 0) {
                                System.out.println("Invalid number!\n");
                                System.out.print("Please enter the friend you want to remove (1-" + mainUser.getFriends().size() + "): ");
                            } else {
                                keyboard.nextLine(); // clear buffer
                                friend = mainUser.getFriendsSorted().get(option - 1);
                                more = false;
                            }
                        } catch (InputMismatchException e) {
                            keyboard.nextLine();
                            System.out.println("Invalid number!\n");
                            System.out.print("Please enter the friend you want to select (1-" + mainUser.getFriends().size() + "): ");
                        }
                    }
                }
                if (friend == null) {
                    System.out.println("The friend you entered is not on your friend list.");
                } else {
                    mainUser.removeFriend(friend);
                    System.out.println(friend.getName() + " has been removed.\n");

                }

            } else if (viewOption.equalsIgnoreCase("X")) {
                return;
            } else {
                System.out.println("Invalid option!");
            }
        }
    }

    static public void searchFriends(Scanner keyboard, HashTable<User> names, ArrayList<BST<User>> usersInterest, User mainUser, HashTable<Interest> interestTable) {
        System.out.print("\nWould you like to search by name (N) or search by interest (I)? "); // could change this to simply enter a string, and WE do the evaluation, could be a bit tricky
        // search that string within in the interests, if that interest doesn't exist, display error
        String searchOption = keyboard.nextLine();

        // search by name
        if (searchOption.equalsIgnoreCase("N")) {
            System.out.print("\nPlease enter the name of the friend you'd like to search for: ");
            String friendName = keyboard.next();
            friendName += keyboard.nextLine();

            List<User> possibleFriends = names.getBucketList(friendName);
            if (possibleFriends.isEmpty()) { // user doesn't exist
                System.out.println("Sorry that user does not exist!");
            } else {
                //names.printTable();
                List<User> displayList = new List<>();
                possibleFriends.placeIterator();
                while (!possibleFriends.offEnd()) {
                    User user = possibleFriends.getIterator();
                    if (user.getName().equalsIgnoreCase(friendName)) {
                        displayList.addLast(user);
                    }
                    possibleFriends.advanceIterator();
                }
                displayList.placeIterator();
                displayList.printNumberedList();

                if (displayList.isEmpty()) {
                    System.out.println("Sorry, no users by that name exist.");
                    return;
                }

                System.out.print("Would you like to add friend to your friend list? (Y/N): ");
                String addOption = keyboard.nextLine();
                if (addOption.equalsIgnoreCase("Y")) {
                    if (displayList.getLength() == 1) {
                        displayList.iteratorToIndex(1);
                        User friendToAdd = displayList.getIterator();
                        add(names, mainUser, friendToAdd);
                    } else {
                        boolean more = true;
                        System.out.print("\nEnter the user you'd like to add (1-" + displayList.getLength() + "): ");
                        while (more) {
                            try {
                                int option = keyboard.nextInt();
                                if (option > displayList.getLength() || option <= 0) {
                                    System.out.println("Invalid number.\n");
                                    System.out.print("Enter the user you'd like to add (1-" + displayList.getLength() + "): ");
                                } else {
                                    keyboard.nextLine(); // clear buffer
                                    displayList.iteratorToIndex(option);
                                    User friendToAdd = displayList.getIterator();
                                    add(names, mainUser, friendToAdd);
                                    more = false;
                                }
                            } catch (InputMismatchException e) {
                                keyboard.nextLine();
                                System.out.println("Invalid number.\n");
                                System.out.print("Enter the user you'd like to add (1-" + displayList.getLength() + "): ");
                            }
                        }
                    }
                } else if (addOption.equalsIgnoreCase("N")) {
                    return;
                } else {
                    System.out.println("Invalid option! Returning to main menu.");
                }
            }
        }
        // search by interest
        else if (searchOption.equalsIgnoreCase("I")) {
            System.out.print("\nPlease enter the interest you'd like to search for: ");
            String interestName = keyboard.next();
            interestName += keyboard.nextLine();

            Interest temp = new Interest(interestName);

            // search for that interest's id in the HashTable<Interests> if the interest exists
            if (!(interestTable.contains(temp, interestName))) {
                System.out.println("Sorry, no users have that interest!");
            } else {
                int interestID = interestTable.get(temp, interestName).getID();

                BST<User> treeInterest = usersInterest.get(interestID);
                treeInterest.remove(mainUser, new NameComparator());
                if (treeInterest.isEmpty()) {
                    System.out.println("Sorry, no other users share that interest");
                } else {
                    ArrayList<User> sameInterest = treeInterest.compileTree();
                    for (int i = 0; i < sameInterest.size(); i++) {
                        System.out.println((i + 1) + ". " + sameInterest.get(i));
                    }

                    System.out.print("\nWould you like to add friend to your friend list? (Y/N): ");
                    String addOption = keyboard.nextLine();

                    if (addOption.equalsIgnoreCase("Y")) {
                        if (sameInterest.size() == 1) {
                            User friendToAdd = sameInterest.get(0);
                            add(names, mainUser, friendToAdd);
                        } else {
                            boolean more = true;
                            System.out.print("Enter the user you'd like to add (1-" + sameInterest.size() + "): ");
                            while (more) {
                                try {
                                    int option = keyboard.nextInt();
                                    if (option > sameInterest.size() || option <= 0) {
                                        System.out.println("Invalid number.\n");
                                        System.out.print("Enter the user you'd like to add (1-" + sameInterest.size() + "): ");
                                    } else {
                                        keyboard.nextLine(); // clear buffer
                                        User friendToAdd = sameInterest.get(option - 1);
                                        add(names, mainUser, friendToAdd);
                                        more = false;
                                    }
                                } catch (InputMismatchException e) {
                                    keyboard.nextLine();
                                    System.out.println("Invalid number.\n");
                                    System.out.print("Enter the user you'd like to add (1-" + sameInterest.size() + "): ");
                                }
                            }
                        }

                    } else if (addOption.equalsIgnoreCase("N")) {
                        return;
                    } else {
                        System.out.println("Invalid option! Returning to main menu.");
                    }
                }
            }
        } else {
            System.out.println("Invalid option!");
        }
    }


    static public void friendRec(Scanner keyboard, HashTable<User> names, Graph network, ArrayList<User> userID, ArrayList<BST<User>> usersInterest, User mainUser) {
        if (mainUser.hasNoFriends()) {
            System.out.println("\nWe cannot find anyone in your circle.\nPlease select search for a new friend by interest (2) in the main menu!\n");
            return;
        }
        ArrayList<Integer> mutualInterest = new ArrayList<>();
        ArrayList<Integer> mutualFriends = new ArrayList<>();
        ArrayList<Integer> points = new ArrayList<>();


        for (int i = 0; i < userID.size(); i++) {
            ArrayList<User> friendList = userID.get(i).getFriends(); // getting array list of friends for every user
            for (int j = 0; j < friendList.size(); j++) {   // iterating through friends array list at userID i
                User userFriend = friendList.get(j);
                int friendIndex = userID.indexOf(userFriend);
                network.addDirectedEdge(i, friendIndex);
            }
        }

        network.BFS(userID.indexOf(mainUser));

        for (int i = 0; i < userID.size(); i++) {
            int friendCount = 0;
            int distance;
            mutualInterest.add(0);
            points.add(0);
            mutualFriends.add(0);
            if (i == userID.indexOf(mainUser)) {
                distance = 0;
            } else {
                distance = network.getDistance(i);
            }

            if (distance < 2) {
                points.set(i, 0);
                mutualFriends.set(i, 0);

            } else {
                if (distance == 2) {
                    ArrayList<User> friendList = userID.get(i).getFriends();
                    for (int j = 0; j < friendList.size(); j++) {
                        if (friendList.get(j).getFriend(mainUser) != null) {
                            friendCount++;
                        }
                    }
                    mutualFriends.set(i, friendCount);
                    points.set(i, 100 * (mutualFriends.get(i)));

                } else {
                    points.set(i, 100 - distance);
                }

                List<Interest> friendInterest = userID.get(i).getInterestList();
                friendInterest.placeIterator();

                while (!friendInterest.offEnd()) {
                    Interest interest = friendInterest.getIterator();
                    BST<User> sameInterest = usersInterest.get(interest.getID());
                    if (sameInterest.search(mainUser, new NameComparator()) != null) {
                        mutualInterest.set(i, mutualInterest.get(i) + 1);
                        points.set(i, points.get(i) + 1);
                    }
                    friendInterest.advanceIterator();
                }
            }
        }
        System.out.println(points);
        for (int i = 0; i < points.size(); i++) {
            userID.get(i).setPoint(points.get(i));
        }
        ArrayList<User> userPoint = new ArrayList<>(userID);
        userPoint.sort(new PointComparator());

        ArrayList<User> display = new ArrayList<>();

        for (int i = 0; i < userPoint.size(); i++) {

            if (userPoint.get(i).getPoint() > 0) {
                display.add(userPoint.get(i));
            }
        }
        System.out.println(mutualInterest);
        for (int i = 0; i < display.size(); i++) {
            System.out.print("\n" + (i + 1) + ". " + userPoint.get(i).getName());
            int index = userID.indexOf(display.get(i));
            if (mutualFriends.get(index) >= 1 && mutualInterest.get(index) >= 1) {
                System.out.print(" -- has " + mutualFriends.get(index) + " mutual friend(s)" + " and " + mutualInterest.get(index) + " mutual interest(s) with you");
            } else if (mutualFriends.get(index) >= 1) {
                System.out.print(" -- has " + mutualFriends.get(index) + " mutual friend(s) with you");
            } else if (mutualFriends.get(index) < 100 && mutualInterest.get(index) >= 1) {
                System.out.print(" -- is a mutual friend of a mutual friend and " + mutualInterest.get(index) + " mutual interest(s) with you");
            } else if (mutualFriends.get(index) < 100) {
                System.out.print(" -- is a mutual friend of a mutual friend");
            }
        }


        System.out.println();
        System.out.print("\nWould you like to add friend to your friend list? (Y/N): ");
        String addOption = keyboard.nextLine();


        if (addOption.equalsIgnoreCase("Y")) {
            boolean more = true;
            System.out.print("\nEnter the user you'd like to add (1-" + display.size() + "): ");
            while (more) {
                try {
                    int option = keyboard.nextInt();
                    if (option > display.size() || option <= 0) {
                        System.out.println("Invalid option!\n");
                        System.out.print("Enter the user you'd like to add (1-" + display.size() + "): ");
                    } else {
                        keyboard.nextLine(); // clear buffer
                        User friendToAdd = display.get(option - 1);
                        add(names, mainUser, friendToAdd);
                        more = false;
                    }

                } catch (InputMismatchException e) {
                    keyboard.nextLine();
                    System.out.println("Invalid option!\n");
                    System.out.print("Enter the user you'd like to add (1-" + display.size() + "): ");
                }
            }

        } else if (addOption.equalsIgnoreCase("N")) {
            return;
        } else {
            System.out.println("Invalid option! Returning to main menu.");
        }

    }

    static public void saveData(ArrayList<User> userID) {

        try {
            FileWriter myWriter = new FileWriter("records.txt", false);

            // access all users and read their data into the file upon exit (therefore using the most recently updated data)
            for (int i = 0; i < userID.size(); i++) {
                String name = userID.get(i).getName();
                String userName = userID.get(i).getUserName();
                String password = userID.get(i).getPassword();
                String location = userID.get(i).getLocation();
                int numInterests = userID.get(i).getInterestList().getLength();
                String interest = userID.get(i).getInterestList() + "";
                int numFriends = userID.get(i).numFriends();
                String friends = userID.get(i).stringFriends();

                myWriter.write(name + "\n" + userName + "\n" + password + "\n" + location + "\n"
                        + numInterests + "\n" + interest + numFriends + "\n" + friends + "\n");
            }

            myWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred with writing to the file.");
        }

    }

    static public void add(HashTable<User> names, User mainUser, User friendToAdd) {

        String friendName = friendToAdd.getName();
        String friend = friendToAdd.getUserName() + friendToAdd.getPassword();
        String main = mainUser.getUserName() + mainUser.getPassword();

        if (main.equals(friend)) { // edge case: adding yourself as a friend
            System.out.println("You cannot add yourself as a friend!");
        } else if (mainUser.getFriends().contains(friendToAdd)) { // edge case: adding an existing friend
            System.out.println("You are already friends with " + friendName + "!");
        } else if (!names.contains(new User(friendToAdd.getUserName(), friendToAdd.getPassword()), friendName)) { // edge case: adding a nonexistent User
            System.out.println("Sorry, that user does not exist!");
        } else { // general case: the user exists, but is not a friend yet
            mainUser.addFriend(friendToAdd);
            friendToAdd.addFriend(mainUser);
            System.out.println(friendName + " has been added to your friend list.");
        }
    }
}