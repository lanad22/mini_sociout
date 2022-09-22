/**
 * Interest.java
 *
 * @author: Phuong Do
 * @author: Chris Vo
 * @author: Skyler Berry
 * @author: Paolo Pedrigal
 * @author: Arushi Sharma
 * @author: Shohin Abdulkhamidov
 * CIS 22C Group Project
 */

public class Interest {
    private String interestName;
    private int interestID;

    public Interest(String interestName, int interestID) {
        this.interestName = interestName;
        this.interestID = interestID;
    }

    public Interest(String interest) {
        this.interestName = interest;
    }

    public String toString() {
        return interestName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Interest)) {
            return false;
        }
        // cast and check each field
        else {
            Interest interest = (Interest) o;
            return interestName.equalsIgnoreCase(interest.interestName);
        }
    }

    public int getID() {
        return interestID;
    }

    public String getInterestName() {
        return interestName;
    }

    public void setID(int id) {
        this.interestID = id;
    }

}
