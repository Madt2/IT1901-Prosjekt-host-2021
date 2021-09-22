package core;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String userName;
    private String password;
    private List<String> myWorkouts = new ArrayList<String>();
    private List<String> myHistory = new ArrayList<String>();

    public User(String userName, String password) {
        setUserName(userName);
        setPassword(password);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }


}
