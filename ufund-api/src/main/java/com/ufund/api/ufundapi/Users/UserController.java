package com.ufund.api.ufundapi.Users;

import java.io.Console;
import java.io.IOException;
import java.util.List;
import java.util.logging.ConsoleHandler;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.Basket;
import com.ufund.api.ufundapi.Need;
import com.ufund.api.ufundapi.NeedController;
import com.ufund.api.ufundapi.NeedFileDAO;


/**REST Controller for the {@link User user} class.
 * Uses {@link Users users} to control {@link User user} objects
 * 
 * @author dangerously dy/dx
 */
@RequestMapping("/api")
@RestController
public class UserController {

    public Users users;
    /**This variable is used to keep track of the currently logged in user to simplify requests. */
    User currentlyLoggedIn;
    public NeedController needController;

    public UserController(Users use){
        this.users = use;
        currentlyLoggedIn=null;
    }
    
    /**
     * responds to {@link org.springframework.web.bind.annotation.PostMapping POST} requests from the /signup page<p>
     * if the user already exists in users, return {@link HttpStatus.Series HttpStatus.CONFLICT}<p>
     * else, creates the user with {@link Users#createUser(User) createUser(User)}
     * @param user
     * @return {@link ResponseEntity ResponseEntity}<{@link User User}>
     */
    @PostMapping("/signup")
    public ResponseEntity<User> createUser(@RequestBody String username){
        try{
            for (User existingUser : users.getAllUsers()) {
                if (existingUser.getUsername().equals(username)) {
                    return new ResponseEntity<>(HttpStatus.CONFLICT); // Username already taken
                }
            }
        
            currentlyLoggedIn = users.createUser(username);
            return new ResponseEntity<>(currentlyLoggedIn, HttpStatus.CREATED);
        }
        catch(IOException e){return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);}    
    }
    
    /**
     * reponds to {@link org.springframework.web.bind.annotation.GetMapping GET} requests from the /login page<p>
     * takes a {@link String username} and {@link String password} then checks all users with {@link Users#getAllUsers() getAllUsers()}<p>
     * assuming a user that matches the username and password is fine, returns a {@link ResponseEntity ResponseEntity}<{@link User User}> with {@link HttpStatus.Series HttpStatus.OK}
     * @param username
     * @param password
     * @return {@link ResponseEntity ResponseEntity}
     */
    @PostMapping("/login")
    public ResponseEntity<User> getUser(@RequestBody String username){
        System.out.println("Received username: " + username);
        for(User u : users.getAllUsers()){
            if(u.getUsername().equals(username)){
                currentlyLoggedIn = u;
                System.out.println("LOGGED IN");
                return new ResponseEntity<User>(u, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // /**
    //  * responds to {@link org.springframework.web.bind.annotation.GetMapping GET} requests from the /users page <p>
    //  * First it checks if the {@link User user} requesting the page has a permission value of 3 or greater @see {@link User user} for more information regarding permission values<p>
    //  * if user has the required permissions, returns a list of all currently created users.
    //  * @return {@link ResponseEntity ResponseEntity}
    //  */
    // @GetMapping("/users")
    // public ResponseEntity<List<User>> getAllUsers(){
    //     if(currentlyLoggedIn!=null && currentlyLoggedIn.getPermissions()>=3){
    //         return new ResponseEntity<List<User>>(users.getAllUsers(), HttpStatus.OK);
    //     }
    //     return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    // }

    /**
     * responds to {@link org.springframework.web.bind.annotation.GetMapping GET} requests from the /user/home page <p>
     * Acts as a way to get the currently logged in user without /login or /signup
     * @return {@link ResponseEntity ResponseEntity}
     */
    @GetMapping("/user/home")
    public ResponseEntity<User> getHome(){
    if(currentlyLoggedIn!=null){
        return new ResponseEntity<>(currentlyLoggedIn, HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
   }

    /**
     * responds to {@link org.springframework.web.bind.annotation.GetMapping GET} requests from the /logout page <p>
     * logs out the current {@link User user} occupying currentlyLoggedIn
     * @return {@link ResponseEntity ResponseEntity}
     */
    @GetMapping("/logout")
    public ResponseEntity<User> logout(){
        currentlyLoggedIn=null;
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // /**
    //  * responds to {@link org.springframework.web.bind.annotation.PutMapping PUT} requests from the user/{id} page <p>
    //  * Checks the currently logged in users permission level, then calls {@link Users#changeUser(User) changeUser(User)}<p>
    //  * @see Users#changeUser(User) changeUser(User)
    //  * @param user
    //  * @return
    //  */
    // @PutMapping("user/{id}")
    // public ResponseEntity<User> changeUser(@RequestBody User user){
    //     if((currentlyLoggedIn!=null) && (currentlyLoggedIn.getPermissions()<=3)){
    //         try{
    //         users.changeUser(user);
    //         return new ResponseEntity<>(user, HttpStatus.OK);
    //         }
    //         catch(IOException e){return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);}
    //     }
    //     return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    // }

    /**
     * responds to {@link org.springframework.web.bind.annotation.GetMapping GET} requests to 'user/basket'
     * @return {@link ResponseEntity ResponseEntity}<{@link Basket Basket}>
     */
    @GetMapping("user/basket")
    public ResponseEntity<Basket> getBasket(){
        if(currentlyLoggedIn!=null){
            return new ResponseEntity<>(currentlyLoggedIn.getBasket(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

      /**
     * responds to {@link org.springframework.web.bind.annotation.PostMapping POST} requests to 'user/basket'
     * @param need
     * @return {@link ResponseEntity ResponseEntity}<{@link Basket Basket}>
     */
    @PostMapping("user/basket")
    public ResponseEntity<Basket> addToBasket(@RequestBody Need need){
        if(currentlyLoggedIn!=null){
            try{
                users.addtoBasket(currentlyLoggedIn, need);
                return new ResponseEntity<>(currentlyLoggedIn.getBasket(), HttpStatus.OK);
            }
            catch(IOException e){return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);}
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("user/basket/funds")
    public ResponseEntity<Basket> addFundsToBasket(@RequestBody String amount){
        if(currentlyLoggedIn!=null){
           int num = Integer.parseInt(amount);
            try{
                users.addFunds(currentlyLoggedIn, num);
                return new ResponseEntity<>(currentlyLoggedIn.getBasket(), HttpStatus.OK);
            }
            catch(IOException e){return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);}
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * responds to {@link org.springframework.web.bind.annotation.GetMapping GET} requests to 'user/favorites'
      * @return {@link ResponseEntity ResponseEntity}<{@link List List}<{@link Need Need}>>
     */
    @GetMapping("user/favorites")
    public ResponseEntity<List<Need>> getFavorites(){
        if(currentlyLoggedIn!=null){
            return new ResponseEntity<>(currentlyLoggedIn.getFavorites(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * responds to {@link org.springframework.web.bind.annotation.PostMapping POST} requests to 'user/favorites'
     * @param need
     * @return {@link ResponseEntity ResponseEntity}<{@link List List}<{@link Need Need}>>
     */
    @PostMapping("user/favorites")
    public ResponseEntity<List<Need>> addFavorite(@RequestBody Need need){
        if(currentlyLoggedIn!=null){
            try{
                System.out.println("trying to call users.addToFavorites");
                users.addToFavorites(currentlyLoggedIn, need);
                return new ResponseEntity<>(currentlyLoggedIn.getFavorites(), HttpStatus.OK);
            }
            catch(IOException e){return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);}
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("user/favorites/delete")
    public ResponseEntity<List<Need>> removeFromFavorites(@RequestBody Need need){
        if (currentlyLoggedIn != null) {
            try {
                // Remove the need from the user's basket
                users.removeFromFavorites(currentlyLoggedIn, need);
                return new ResponseEntity<>(currentlyLoggedIn.getFavorites(), HttpStatus.OK);
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("user/basket/delete/{id}")
    public ResponseEntity<Basket> deleteFromBasket(@PathVariable int id) {
        if (currentlyLoggedIn != null) {
            try {
                // Remove the need from the user's basket
                users.removeFromBasket(currentlyLoggedIn, id);
                return new ResponseEntity<>(currentlyLoggedIn.getBasket(), HttpStatus.OK);
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // @PostMapping("/checkout")
    // // Checkout basket
    // public ResponseEntity<String> checkoutBasket() {
    //     if (currentlyLoggedIn.getBasket().getNeeds().isEmpty()) {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    //                 .body("Basket is empty! Cannot checkout.");
    //     }

    //     // Calculate total amount of money
    //     double totalAmount = currentlyLoggedIn.getBasket().getNeeds().stream()
    //             .mapToDouble(need -> need.getCost() * need.getQuantity())
    //             .sum();

    //     // Update quantities in the cupboard
    //     for (Need basketNeed : currentlyLoggedIn.getBasket().getNeeds()) {
    //         Need cupboardNeed = needController.getNeed(basketNeed.getId()).getBody();
    //         System.out.println("cuboard need = " + cupboardNeed.getName());

    //         // if (cupboardNeed == null) {
    //         //     return ResponseEntity.status(HttpStatus.NOT_FOUND)
    //         //             .body("Need not found in cupboard: " + basketNeed.getName());
    //         // }

    //         // // Not enough stock in the cupboard
    //         // if (cupboardNeed.getQuantity() < basketNeed.getQuantity()) {
    //         //     return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    //         //             .body("Not enough stock for: " + basketNeed.getName());
    //         // }

    //         // Subtract the quantity and save the updated need
    //         // cupboardNeed.setQuantity(cupboardNeed.getQuantity() - basketNeed.getQuantity());
            
    //         //try {
    //             needController.deleteNeed(cupboardNeed.getId());
    //         // } catch (IOException e) {
    //         //     e.printStackTrace();
    //         //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //         //             .body("Error updating cupboard for: " + basketNeed.getName());
    //         // }
    //     }

    //     // Clear basket after checkout
    //     currentlyLoggedIn.getBasket().clearNeeds();
    //     currentlyLoggedIn.getFavorites().removeAll(null);
    //     // try {
    //     //     // Save empty basket
    //     //     //BasketPersistence.saveBasket(basket);
            
    //     // } catch (IOException e) {
    //     //     e.printStackTrace();
    //     //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //     //             .body("Error processing checkout. Please try again.");
    //     // }

    //     // Return success message with total amount
    //     return ResponseEntity.status(HttpStatus.OK)
    //             .body("Checkout successful! Total amount donated: $" + totalAmount);
    // }

}
