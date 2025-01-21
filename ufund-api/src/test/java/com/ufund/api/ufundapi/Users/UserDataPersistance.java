package com.ufund.api.ufundapi.Users;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ufund.api.ufundapi.Need;

public class UserDataPersistance {

    private Users u;
    

    @BeforeEach
    public void setUp() throws IOException{
        u = new Users();
        u.createUser("tester");
    }
    
    @Test
    public void testLoading() throws IOException{
        assert(u.load());
    }

    @Test
    public void testAddFunds()throws IOException{
        u.addFunds(u.getAllUsers().get(1), 10);
        assert(u.getBasket(u.getAllUsers().get(0)).getFunds()>0);
    }

    @Test
    public void testAddFavorite() throws IOException{
        Need need = new Need();
        u.addToFavorites(u.getAllUsers().get(1), need);
        assertNotNull(u.getAllUsers().get(0).getFavorites().get(0));
    }
}
