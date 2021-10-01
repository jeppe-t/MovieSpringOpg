package com.example.demo.controllers;

import com.example.demo.repositories.DBManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;


@Controller
public class MovieController {

    public String connector(String SQL){
        try {
            connection = DBManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            Movie tmp = null;
            if (rs.next()) {
                tmp = new Movie(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getString(7));
            }
            return tmp.toString();
        } catch (SQLException ex) {
            return ex.getMessage();
        }
    }
    Connection connection;
    ArrayList<Movie> movieList = new ArrayList<>();
    String db = "";

    // Check if my database is connected to the Tomcat server.

    @GetMapping("/check")
    @ResponseBody
    public String checkConnection() {
        connection = DBManager.getConnection();
        if (connection != null)
            return "Yeah its working";
        else return "Øv øv øv det virker ikke";
    }

    //----------------------------------------------------------------------------------------------------------------//

    //Opg 3.1
    //This end-point welcomes the user and prints out a short description of your application
    //Testlink: http://localhost:8080/

    @GetMapping("/")
    @ResponseBody
    public String greet() {
        return "Hello and welcome to the Movie database";
    }

    //----------------------------------------------------------------------------------------------------------------//

    //Opg 3.2
    //Shown in class This end-points calls a service that finds the first movie from the list and displays the title.
    //Testlink: http://localhost:8080//getFirst

    @GetMapping("/getFirst")
    @ResponseBody
    public String getFirst() {
        return connector( "SELECT * FROM movies WHERE  id = 1;");
    }

    //----------------------------------------------------------------------------------------------------------------//

    //Opg 3.3
    //This end-point calls a service, that finds a single random movie from the list and displays the title.
    //Testlink: http://localhost:8080/getRandom

    @GetMapping("/getRandom")
    @ResponseBody
    public String random() {
        return connector("SELECT * FROM movies order by rand() limit 1");
    }

    //----------------------------------------------------------------------------------------------------------------//

    //Opg 3.4
    /*This end-point calls a service that fetches 10 random movies, maps each result to a Movie model class,
    adds to a Movie Arraylist and prints the result to the browser - sorted in ascending order by popularity
    (Hint: Remember the comparable interface). */
    //Testlink: http://localhost:8080/getTenSortByPopularity

    @GetMapping("/getTenSortByPopularity")
    @ResponseBody
    public String topTen() {
        try {
            connection = DBManager.getConnection();
            String SQL = "SELECT * FROM movies order by rand() limit 10 ;";
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            Movie tmp;
            movieList.clear();
            db ="";
            while (rs.next()) {
                tmp = new Movie(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getString(7));
                movieList.add(tmp);
            }
            movieList.sort(Comparator.comparing(Movie::getPopularity));
            for (int i = 0; i < movieList.size(); i++) {
                db += movieList.get(i);
            }

            return db;
        } catch (
                SQLException ex) {
            return ex.getMessage();
        }

    }

    //----------------------------------------------------------------------------------------------------------------//

    //Opg 3.5
    //This end-point prints how many of the movies of the data-set that won an award.git
    //Testlink: http://localhost:8080/howManyWonAnAward

    @GetMapping("/howManyWonAnAward")
    @ResponseBody
    public String award() {
        int count = 0;
        try {
            String SQLCommand = "SELECT count(*) FROM movies \n" +
                    "where awards=\"Yes\";";
            PreparedStatement ps = connection.prepareStatement(SQLCommand);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
                count=rs.getInt(1);
            String value =Integer.toString(count);
            return value;
        } catch (SQLException ex) {
            return ex.getMessage();

        }

    }

    //----------------------------------------------------------------------------------------------------------------//

    // 3.8 Advanced
    //Import data into an MySQL database. Display all comedies that won an award only using SQL queries.
    //Testlink: http://localhost:8080/howManyComedysWonAnAward

    @GetMapping("/howManyComedysWonAnAward")
    @ResponseBody
    public String comedyAward() {
        try {
            connection = DBManager.getConnection();
            Statement statement = connection.createStatement();
            statement.addBatch("drop table if exists AwardedComedys;");
            statement.addBatch("create table AwardedComedys(id int auto_increment primary key, title varchar(100), " +
                    "year int, length int, subject varchar(50),popularity int, awards varchar(3));");
            statement.addBatch("insert into MovieDatabase.AwardedComedys select id, title, year, length, subject, popularity, " +
                    "awards from movies where subject = \"Comedy\" and awards = \"Yes\";");
            statement.executeBatch();
                    String SQL = "select * from AwardedComedys;";
            PreparedStatement ps = connection.prepareStatement(SQL);
            //ResultSet rs = ps.executeQuery();
            ResultSet rs = statement.executeQuery(SQL);
            Movie tmp;
            String award = "";
            movieList.clear();
            while (rs.next()) {
                tmp = new Movie(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getString(7));
                movieList.add(tmp);
            }
            for (int i = 0; i < movieList.size(); i++) {
                award += movieList.get(i);
            }
            return award;
        } catch (
                SQLException ex) {
            return ex.getMessage();
        }
    }


}
