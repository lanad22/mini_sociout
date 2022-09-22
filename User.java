import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;

public class User {
    private String name;
    private String userName;
    private String password;
    private String location;
    private BST<User> friends = new BST<>();
    private List<Interest> interests;
    private int point;
    private static int userID = 1;

    /**
     * CONSTRUCTOR
     **/

    public User() {
        this.name = "";
        this.userName = "";
        this.password = "";
        this.location = "";
    }

    public User(String name) {
        this.userName = "";
        this.name = name;
        password = "";
        location = "";

    }

    public User(String userName, String password) {
        this.name = "";
        this.userName = userName;
        this.password = password;
        location = "";
    }

    public User(String name, String userName, String password, String location, ArrayList<User> al, List<Interest> l) {
        NameComparator c = new NameComparator();
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.location = location;
        for (int i = 0; i < al.size(); i++) {
            friends.insert(al.get(i), c);
        }
        this.interests = l;
    }

    public User(String name, String userName, String password, String location, List<Interest> l){
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.location = location;
        this.interests = l;
    }

    /**
     * ACCESSORS
     **/

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public List<Interest> getInterestList() { return interests; }

    public static int getUserID() { return userID++; }

    public String getLocation() { return location; }

    public ArrayList<User> getFriends(){
        return friends.compileTree();
    }

    public ArrayList<User> getFriendsSorted() {
        ArrayList<User> getFriendsSorted = new ArrayList(friends.compileTree());
        getFriendsSorted.sort(new NameComparator());
        return getFriendsSorted;
    }

    public User getFriend(User friend){ return friends.search(friend, new NameComparator()); }

    public int getPoint(){ return point; }

    public boolean hasNoFriends(){ return friends.isEmpty(); }

    public int numFriends(){ return friends.getSize(); }


    /**
     * MUTATORS
     **/

    public void addFriend(User friend){ friends.insert(friend, new NameComparator()); }

    public void removeFriend(User friend){ friends.remove(friend, new NameComparator()); }

    public void setPoint(int point){ this.point = point; }


    /**
     * Additional Operations
     **/

    public String toString() { return "\nName: " + name + "\nUsername: " + userName + "\nInterest(s):\n" + interests; }

    public void printFriends() { friends.inOrderPrint(); }

    // TODO: might delete this later because we have stringFriends. If so, you have to change the call in UserInterface
    public void printFriendsByName() {
        ArrayList<User> friendsList = friends.compileTree();
        friendsList.sort(new NameComparator());
        for (int i = 0; i < friendsList.size(); i++){
            System.out.println((i+1) + ". " + friendsList.get(i).getName());
            System.out.println("User Name: " + friendsList.get(i).getUserName());
        }
    }

    public String stringFriends() {
        String result = "";
        ArrayList<User> friendsList = friends.compileTree();
        friendsList.sort(new NameComparator());
        for (int i = 0; i < friendsList.size(); i++){
            result += friendsList.get(i).getName() + "\n";
        }
        return result;
    }

    /**
     * Determines whether two User objects have the same username and password
     *
     * @param o the User to compare to this User
     * @return whether the two Userss are equal by username and password
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof User)) {
            return false;
        } else {
            User u = (User) o;
            if (this.userName.equals(u.userName) && this.password.equals(u.password))
                return true;

            if (this.name.equalsIgnoreCase(u.name) && this.userName.compareTo(u.userName) == 0 && this.password.compareTo(u.password) == 0) {
                return true;
            } else {
                return false;
            }
        }
    }

} // end of User class

class NameComparator implements Comparator<User> {
    /**
     * Compares the two users by name of the fund uses the String
     * compareTo method to make the comparison
     *
     * @param user1 1st user
     * @param user2 2nd user
     * @return 0 for equal, positive number if user1's name is greater or a negative
     *         number if user2's name is greater
     */
    @Override
    public int compare(User user1, User user2) { return user1.getName().toUpperCase().compareTo(user2.getName().toUpperCase()); }
} // end of NameComparator class

class PointComparator implements Comparator<User>{
    /**
     * Compares the two users by points
     *
     * @param user1 1st user
     * @param user2 2nd user
     * @return difference between user2 and user1's points
     */
    @Override
    public int compare (User user1, User user2){
        return user2.getPoint() - user1.getPoint();
    }
} // end of PointerComparator class