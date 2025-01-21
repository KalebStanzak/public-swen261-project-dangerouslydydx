package com.ufund.api.ufundapi.Users;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufund.api.ufundapi.Basket;
import com.ufund.api.ufundapi.Need;

/**Class representing a single user
 * 
 * @author dangerously dy/dx
 */
public class User {
    @JsonProperty("uid") private int userID;
    @JsonProperty("username") private String userName;
    //@JsonProperty("password") private String password;
    /**permissions is an {@link Integer int} used for determining the access the user has.
     * a higher value indicates a higher access value, higher values can do all that lower values can.<p>
     * <b>PLEASE USE THIS TABLE WHEN DETERMINING A USERS PERMISSION VALUE</b>
     * <ul>
     * <li>0: NONE  -- user has no permissions, this is the lowest level of access</li>
     * <li>1: READ  -- user has permissions to READ the cupboard, this is the default level of access </li>
     * <li>2: WRITE -- user has permissions to WRITE to cupboard </li>
     * <li>3: READ_USERS -- user has permissions to READ all {@link User user} objects</li>
     * </ul>
    */
    @JsonProperty("permissions") private int permissions;
    @JsonProperty("basket") private Basket basket;
    @JsonProperty("favorites") private List<Need> favorites;

    /**
     * used for creating new {@link User user} objects <p>
     * userID gets set to -1 when initializing as a temp value, this gets changed in {@link Users#createUser(User) createUser(User)}
     * permissions defaults to 1, which is the value used for only reading needs
     * @param {@link String name}
     * @param {@link String pass}
     */
    public User(String name){
        this.permissions=1;
        this.userID=-1;
        this.userName=name;
        this.basket = new Basket(userID, "userBasket", 0.0);
        this.favorites = new LinkedList<>();
    }
    public User(@JsonProperty("username") String name, @JsonProperty("permissions") int permission){
        this.userName=name;
        this.basket = new Basket(userID, "userBasket", 0.0);
        this.favorites = new LinkedList<>();
        this.permissions=permission;
    }

    /**
     * @return {@link Integer userID}
     */
    public int getUserID() {
        return userID;
    }
    /**
     * @return {@link String username}
     */
    public String getUsername(){
        return userName;
    }

    /**
     * takes a new {@link Integer integer} and assigns to to {@link User user} object's userID<p>
     * {@link User user} id's are given by {@link Users#createUser(User) createUser}
     * @param new_uid new userID
     */
    public void setUserID(int new_uid){
        userID=new_uid;
    }

    /**
     * @return {@link Integer permissions}
     */
    public int getPermissions(){
        return permissions;
    }

    /**
     * checks if passed {@link Integer i} value is between 0 and 4, if it is, sets permission value to i.
     * @param i
     */
    public void setPermissions(int i){
        if(i>=0 && i<=4){
            permissions=i;
        }
    }

    /**
     * @return {@link Basket basket}
     */
    public Basket getBasket(){
        return basket;
    }

    /**
     * @return {@link List favorites}
     */
    public List<Need> getFavorites(){
        return favorites;
    }

    /**
     * takes a {@link Need need} object and adds it to {@link User user}'s favorites.
     * @param need
     */
    public void addFavorite(Need need){
        favorites.add(need);
    }

    /**
     * removes an {@link Need need} object from {@link User user}'s favorites.
     * @param need
     */
    public void removeFavorite(Need need){
        favorites.remove(need);
    }


    @Override
    public String toString(){
        return "USERID: "+ userID + "   USERNAME: " + userName + "\n";
    }

    /**
     * Allows checking against any {@link Object object}, but only returns true if:<p>
     * &emsp;{@link Object object} is of type {@link User user}<p>
     * &emsp;The {@link User user} that's calling matches UserID with a typecasted {@link Object object} using the {@link #getUserID()} function
     * @param other any {@link Object object}
     * @return {@link Boolean boolean}
     */
    @Override
    public boolean equals(Object other){
        if(other instanceof User){
            User o = (User)other;
            if(o.getUserID() < 0 || this.userID < 0){
                return(this.userName.equals(o.getUsername()));
            }
            return (this.userName.equals(o.getUsername()) || this.userID == o.getUserID());
        }
        return false;
    }
}
