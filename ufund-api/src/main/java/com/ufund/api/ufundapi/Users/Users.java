package com.ufund.api.ufundapi.Users;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.Basket;
import com.ufund.api.ufundapi.Need;

/**
 * DAO interface for keeping track of users.
 * uses JSON to utilize data persistance..
 * 
 * @author dangerously dy/dx
 */

@Component
public class Users {

    /**
     * {@link List list} of all users
     * <p>
     * makes getting all users easier
     */
    private List<User> userList;
    /**
     * {@link Hashmap hashmap} of integers mapped to {@link User users}
     * <p>
     * Used for getting {@link User users} in O(1) time
     * <p>
     * {@link Integer userID} is used for key to help prevent conflicts, keeping
     * writing fast.
     */
    private HashMap<Integer, User> usersMap;
    /**
     * An {@link Integer integer} used for assigning {@link User user} objects
     * userID
     * <p>
     * {@link User users} start with a default userID of -1,
     * so that the {@link #createUser(User) createUser} can constantly add 1 to
     * assignedUID with {@link User users} starting with ID 0
     */
    int assignedUID = -1;

    /** Filename used for the {@link ObjectMapper objectMapper} object. */
    private final String filename = "src/main/java/com/ufund/api/ufundapi/data/users.json";
    /**
     * {@link ObjectMapper objectMapper} object used serialize / deserialize objects
     * to/from Java/JSON
     */
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Creates an {@link Users Users} object then calls {@link #load()} to fill
     * {@link LinkedList userList} and {@link HashMap userMap}}
     * 
     * @throws IOException
     */
    public Users() throws IOException {
        userList = new LinkedList<>();
        usersMap = new HashMap<>();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        load();
    }

    /**
     * takes a {@link User user} and adds it to {@link List userList} and {@link Map
     * usermap}
     * <p>
     * assigns an {@link Integer userID} that is 1 more than the current
     * {@link #assignedUID}
     * <p>
     * calls {@link #addUser(User) addUser} to reduce how much code is rewritten}
     */
    public User createUser(String username) throws IOException {
        synchronized (userList) {
            User user = new User(username);
            if (!userList.contains(user)) {
                assignedUID += 1;
                user.setUserID(assignedUID);
                user.getBasket().setId(assignedUID);
                user.getBasket().setName(user.getBasket().getName() + assignedUID);
                addUser(user);
                return user;
            }
        }
        throw new IOException();
    }

    /**
     * takes a new {@link User user} object and checks {@link Map userMap} for the
     * user id.
     * <p>
     * if found, removes {@link User user} from {@link List userList}, updates
     * {@link Map userMap} and readds to {@link List userList}
     * <p>
     * calls {@link #save() save()} to finalize changes.
     * 
     * @param user
     * @throws IOException
     */
    public void changeUser(User user) throws IOException {
        synchronized (userList) {
            if (usersMap.containsKey(user.getUserID())) {
                userList.remove(usersMap.get(user.getUserID()));
                usersMap.put(user.getUserID(), user);
                userList.add(user);
            }
            save();
        }
    }

    /**
     * takes a {@link User user} and adds it to {@link List userList} and {@link Map
     * userMap}
     */
    private void addUser(User user) throws IOException {
        synchronized (userList) {
            userList.add(user);
            usersMap.put(user.getUserID(), user);
        }
        save();
    }

    /**
     * @return {@link List userList} containing all users
     */
    public List<User> getAllUsers() {
        synchronized (userList) {
            return userList;
        }
    }

    /**
     * @param uid userID
     * @return {@link User user} if user exists
     */
    public User getUser(int uid) {
        if (usersMap.containsKey(uid)) {
            return usersMap.get(uid);
        }
        return null;
    }

    /**
     * takes a {@link Integer uid} and checks {@link HashMap userMap}
     * if found, removes user from both {@link HashMap usermap} and {@link List
     * userList}
     * 
     * @param uid userId of {@link User user}
     */
    public void deleteUser(int uid) throws IOException {
        synchronized (userList) {
            if (usersMap.containsKey(uid)) {
                userList.remove(usersMap.get(uid));
                usersMap.remove(uid);
                save();
            }
        }
    }

    /**
     * takes a {@link User user} and a {@link Need need} and adds to {@link User
     * user}'s favorites by calling {@link User#addFavorite(Need) addFavorite(Need)}
     * <p>
     * first, we remove the user from {@link List userList} to remove the outdated
     * data, then call {@link User#addFavorite(Need) addFavorite(Need)}
     * <p>
     * afterwards, put the updated {@link User user} object into the {@link Map
     * userMap} and {@link List userList}
     * 
     * @param user
     * @param need
     * @throws IOException
     */
    public void addToFavorites(User user, Need need) throws IOException {
        synchronized (userList) {
            if (!user.getFavorites().contains(need)) {
                userList.remove(user);
                user.addFavorite(need);
                usersMap.put(user.getUserID(), user);
                userList.add(user);
                Boolean saveState = save();
                System.out.println(saveState);
            }
        }
    }

    public void removeFromFavorites(User user, Need need) throws IOException {
        synchronized (userList) {
            userList.remove(user);
            user.removeFavorite(need);
            usersMap.put(user.getUserID(), user);
            userList.add(user);
            Boolean saveState = save();
            System.out.println(saveState);
        }
    }

    /**
     * takes a {@link User user} object and returns its {@link Basket basket}
     * 
     * @param user
     * @return {@link Basket basket}
     */
    public Basket getBasket(User user) {
        synchronized (userList) {
            return user.getBasket();
        }
    }

    /**
     * takes a {@link User user} and {@link Need need} and adds it to {@link User
     * user}'s {@link Basket basket}
     * then, calls {@link Users#save() save()} to save data changes.
     * 
     * @param user
     * @param need
     * @throws IOException
     */
    public void addtoBasket(User user, Need need) throws IOException {
        synchronized (userList) {
            Basket basket = user.getBasket();
            Set<Need> needs = basket.getNeeds();
            Need basketNeed = needs.stream()
                    .filter(n -> n.getId() == need.getId())
                    .findFirst()
                    .orElse(null);
            // check if there is sufficient quantity available
            if (need.getQuantity() <= 0) {
                throw new IllegalStateException("No quantity left for the requested need.");
            }
            if (basketNeed != null && basketNeed.getQuantity() + 1 > need.getQuantity()) {
                throw new IllegalStateException("No quantity left for the requested need.");
            } else if (basketNeed != null) {
                // increment the quantity in the basket if it already exists
                basketNeed.setQuantity(basketNeed.getQuantity() + 1);

            } else {
                user.getBasket().addNeed(new Need(need.getId(), need.getName(), need.getCost(), 1,
                        need.getType()));
            }
        }
        save();
    }

    public void removeFromBasket(User user, int id) throws IOException {
        synchronized (userList) {
            Basket basket = user.getBasket();
            Set<Need> needs = basket.getNeeds();

            // find the need in the basket
            Need basketNeed = needs.stream()
                    .filter(n -> n.getId() == id)
                    .findFirst()
                    .orElse(null);
            // decrement the quantity or remove the need if the quantity reaches 0
            if (basketNeed.getQuantity() > 1) {
                basketNeed.setQuantity(basketNeed.getQuantity() - 1);
            } else {
                needs.remove(basketNeed); // remove the need when quantity is 0
            }
            System.out.println("need with id " + id + " removed.");
        }
        save();
    }

    /**
     * adds an {@link Integer amount} to the {@link User user}'s {@link Basket
     * basket}.
     * 
     * @param user
     * @param amount
     * @throws IOException
     */
    public void addFunds(User user, int amount) throws IOException {
        synchronized (userList) {
            user.getBasket().setFunds(user.getBasket().getFunds() + amount);
            save();
        }
    }

    /**
     * uses {@link ObjectMapper objectMapper} to write the {@link List userList to
     * the data file.}
     * uses {@link ObjectMapper#writerWithDefaultPrettyPrinter()
     * writerWithDefaultPrettyPrinter()} to write the objects in a more human
     * friendly way.
     * uses {@link PrintWriter PrintWriter} to wipe the file before writing with
     * {@link ObjectMapper ObjectMapper} to prevent the file from being filled with
     * duplicate values.
     * 
     * @return {@link Boolean boolean}
     * @throws IOException
     */
    private boolean save() throws IOException {
        PrintWriter wiper = new PrintWriter(new FileWriter(filename), false);
        wiper.write(" ");
        wiper.close();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filename), userList);
        return true;
    }

    /**
     * uses {@link ObjectMapper objectMapper} object to read from JSON file.
     * <p>
     * writes the loaded values into a {@link User User} array first, then goes
     * through the list to add all current users into {@link LinkedList userList}
     * and {@link HashMap userMap}
     * 
     * @return {@link Boolean Boolean}
     * @throws IOException
     */
    @PostConstruct
    boolean load() throws IOException {
        User[] currentUsers = objectMapper.readValue(new File(filename), User[].class);

        for (User u : currentUsers) {
            usersMap.put(u.getUserID(), u);
            // checking uid for highest uid in JSON file, then setting it to be equal to it.
            // we dont care about setting it to 1 above highest because createUser already
            // increments the assignedUID
            if (assignedUID < u.getUserID()) {
                assignedUID = u.getUserID();
            }
        }
        for (int i : usersMap.keySet()) {
            userList.add(usersMap.get(i));
        }
        return true;
    }

}
