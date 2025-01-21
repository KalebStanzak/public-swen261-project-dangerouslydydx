
package com.ufund.api.ufundapi;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.Users.UserController;
import java.text.DecimalFormat;

@RestController
@RequestMapping("/api/basket")
public class BasketController {
    // rounding
    private static final DecimalFormat df = new DecimalFormat("0.00");
    // cupboard
    private final NeedDAO cupboard;
    // get current user controller
    private final UserController userController;

    public BasketController(NeedDAO cupboard, UserController userController) {
        this.cupboard = cupboard;
        this.userController = userController;
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout() throws IOException {
        // response entity for current basket stuff
        ResponseEntity<Basket> responseEntity = userController.getBasket();
        Basket basket = responseEntity.getBody();
        // check basket empty
        if (basket == null || basket.getNeeds().isEmpty()) {
            return ResponseEntity.badRequest().body("Basket is empty. Nothing to checkout.");
        }
        // validation
        for (Need item : basket.getNeeds()) {
            Need cupboardItem = cupboard.getNeed(item.getId());
            if (cupboardItem == null || cupboardItem.getQuantity() < item.getQuantity()) {
                return ResponseEntity.badRequest().body("Insufficient quantity for item: " + item.getName());
            }
        }

        // calculate total amount of money
        double totalAmount = basket.getNeeds().stream()
                .mapToDouble(need -> need.getCost() * need.getQuantity())
                .sum();

        // update cupboard quantities
        for (Need item : basket.getNeeds()) {
            Need cupboardItem = cupboard.getNeed(item.getId());
            cupboardItem.setQuantity(cupboardItem.getQuantity() - item.getQuantity());
        }

        // clear the Basket
        basket.clearNeeds();
        // save new empty basket
        BasketPersistence.saveBasket(basket);
        return ResponseEntity.ok("Checkout successful! Total amount donated: $" + df.format(totalAmount));
    }
}
