package com.octaviolomeli.coinCollector.core;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@RestController
public class EntriesController {

    @GetMapping("/data")
    public Entries data() {
        /*
        [
            ["seed", "keyPresses"]
            ["seed", "keyPresses"]
            ["seed", "keyPresses"]
        ]
         */
        String url = "jdbc:postgresql://localhost:5432/CoinCollector";
        String username = "postgres";
        String password = "******";
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM saved_worlds");
            String[][] result = new String[9][2];
            int index = 0;
            while (rs.next() && index < 10) {
                result[index][0] = rs.getString(2);
                result[index][1] = rs.getString(3);
                index++;
            }
            con.close();
            return new Entries(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
