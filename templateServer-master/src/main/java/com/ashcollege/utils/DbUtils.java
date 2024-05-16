package com.ashcollege.utils;


import com.ashcollege.entities.User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.sql.Connection;

@Component
public class DbUtils {

    private static final int DEFAULT_ID=-1;
   private Connection connection;


    @PostConstruct
    public void init () {
        createDbConnection("root","1234"); //  חיבור ראשוני למסד הנתונים עם שם המשתמש והסיסמה שהוגדרו למסד הנתונים
    }

    private void createDbConnection(String username, String password){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ash2024", username, password); // פרמטרים: שם סכמה שם משתמש וסיסמה
            System.out.println("Connection successful!");
            System.out.println();
        }catch (Exception e){
            System.out.println("Cannot create DB connection!");
        }
    }

    public boolean checkIfUserExist (String email) {
        boolean isExist = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT email FROM gambling_web_users WHERE email = ?");
            preparedStatement.setString(1,email); //  השוואת הפרמטר שהתקבל בפונקציה למול המשתנה המתבקש בהתאם לאינדקס (שים לב: מתחיל מ1 לא מ0)
            ResultSet resultSet = preparedStatement.executeQuery(); //  הרצת השאילתה והצבתה בתוצאה
            while (resultSet.next()) { // אם קיימת תוצאה
                 isExist = true;
            }
        } catch (SQLException e){
            e.printStackTrace();
                    }
        return isExist;
    }


    public int addUser (User user) {
        int id = DEFAULT_ID;
        try {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO gambling_web_users (name, password,email,balance) VALUES ( ? , ?, ?,? )");
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setDouble(4, user.getBalance());
                preparedStatement.executeUpdate();
                id = login(user.getName(),user.getPassword(),user.getEmail());
        } catch (SQLException e){
            e.printStackTrace();
        }
        return id;
    }

    public double getUserBalance(int id){
        double balance=-1;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT balance FROM gambling_web_users WHERE id = ?");
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                balance =  resultSet.getDouble("balance"); //  חשוב! כך משתמשים בresultSet ע"מ למשוך מידע מהמסד
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return balance;
    }

    public int login (String name, String password,String email) {
        int loginUserId = DEFAULT_ID;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT id FROM gambling_web_users WHERE name = ? AND password = ? AND email = ? ");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
               loginUserId =  resultSet.getInt("id"); //  חשוב! כך משתמשים בresultSet ע"מ למשוך מידע מהמסד
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loginUserId;
    }

    public void updateBalance (int id,double newBalance){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
    "UPDATE gambling_web_users SET balance = ? WHERE id = ?");
        preparedStatement.setDouble(1, newBalance);
        preparedStatement.setInt(2, id);
        int rowsUpdated = preparedStatement.executeUpdate();
    } catch (SQLException e) {
        System.err.println("Error updating object: " + e.getMessage());
    }
}
    public boolean updateName (int id,String newName){
        boolean ans = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE gambling_web_users SET name = ? WHERE id = ?");
            preparedStatement.setString(1, newName);
            preparedStatement.setInt(2, id);
            int rowsUpdated = preparedStatement.executeUpdate();
            ans = true;
        } catch (SQLException e) {
            System.err.println("Error updating object: " + e.getMessage());
        }
        return ans;
    }

    public boolean updatePassword (int id,String newPassword){
        boolean ans = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE gambling_web_users SET password = ? WHERE id = ?");
            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, id);
            int rowsUpdated = preparedStatement.executeUpdate();
            ans = true;
        } catch (SQLException e) {
            System.err.println("Error updating object: " + e.getMessage());
        }
        return ans;
    }

    public boolean updateEmail (int id,String newEmail){
        boolean ans = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE gambling_web_users SET email = ? WHERE id = ?");
            preparedStatement.setString(1, newEmail);
            preparedStatement.setInt(2, id);
            int rowsUpdated = preparedStatement.executeUpdate();
            ans = true;
        } catch (SQLException e) {
            System.err.println("Error updating object: " + e.getMessage());
        }
        return ans;
    }

}
