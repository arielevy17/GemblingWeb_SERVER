package com.ashcollege.controllers;

import com.ashcollege.BetComponent;
import com.ashcollege.Persist;
import com.ashcollege.Season;
import com.ashcollege.entities.*;
import com.ashcollege.responses.BasicResponse;
import com.ashcollege.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


@RestController
public class GeneralController {
    private static final int GAME_ONE_ID=1;
private static final int GAME_TWO_ID=2;
private static final int GAME_THREE_ID=3;
private static final int GAME_FOUR_ID=4;
    private static final int USER_LOGIN_DETAIL_WRONG=1;
    private static final int USER_ALREADY_EXIST=2;
    private static final int USER_PASSWORD_IS_WEEK=3;
    private static final int YOUR_BALANCE_IS_SMALLER_THEN_BET=4;
    private static final int GAME_WAS_BEGIN=5;
    private static final int BALANCE_START_SUM=1000;
    private final int ALL_GAME_NUMBER=4;
    private final int PASS_TO_THE_NEXT=1;
    private final int GAME_TIME=30000;
    private final int PASSWORD_LENGTH=9;
    private final int BET_TIME=30000;
    private int id;

    private Season season = new Season(); // שיבוץ הקבוצות לרשימה פעם1:
    List<User> usersList = new ArrayList<>();

    @Autowired
    private DbUtils dbUtils;

    @Autowired
    private Persist persist;

        // חיבור למסד נתונים ושאיבת נתונים ממנו עובד. לראות האם ואיך למשוך את הID לבדוק אם יש בו צורך בכלל . ולראות איפה נכנס סכום כסף
    private void createDbConnection(String username, String password){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ash2024", username, password); // פרמטרים: שם סכמה שם משתמש וסיסמה
            System.out.println("Connection successful!");
            System.out.println();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT name,password,email,balance FROM gambling_web_users"); // משיכת הפרמטרים האלה
            ResultSet resultSet = preparedStatement.executeQuery(); // הרצת השאילתה והחזרת אוביקט של הפרמטרים הנ"ל
            while (resultSet.next()) {
                String nameFromDb = resultSet.getString(1); // לקיחת השדה הנ"ל מהשורה המוחזרת ממסד הנתונים
                String passwordFromDb = resultSet.getString(2);
                String emailFromDb = resultSet.getString(3);
                double balanceFromDb = resultSet.getDouble(4);
                User appropriateUser = new User(nameFromDb, passwordFromDb, emailFromDb,balanceFromDb);
                usersList.add(appropriateUser);
                System.out.println();
            }
        }catch (Exception e){
            System.out.println("Cannot create DB connection!");
        }
    }

        @RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
        public Object hello() {
            return "Hello From Server";
        }

        @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
        public BasicResponse checkDetails (String name, String password, String email) {
            BasicResponse basicResponse = new BasicResponse();
            basicResponse.setId(dbUtils.login(name,password,email));
            this.id = basicResponse.getId();
            basicResponse.updateSuccess();
            if (!basicResponse.getSuccess()){
                basicResponse.setErrorCode(USER_LOGIN_DETAIL_WRONG);
            }

            return basicResponse;
        }

    @RequestMapping(value = "/get_id_for_user" ,method = {RequestMethod.GET, RequestMethod.POST})
    public double getIdForUser() {
        return this.id;
    }
    @RequestMapping(value = "/get_balance_for_user" ,method = {RequestMethod.GET, RequestMethod.POST})
    public double getBalanceForUser() {
        return dbUtils.getUserBalance(this.id);
    }

    @RequestMapping(value = "/get_season_games" ,method = {RequestMethod.GET, RequestMethod.POST})
    public Season getSeasonGames() {
        return this.season;
    }


    @RequestMapping(value = "/get_game_golles" ,method = {RequestMethod.GET, RequestMethod.POST})
    public Season getBetGolles() {
        if (this.season.getCycleNumber() <= ALL_GAME_NUMBER) {
            this.season.startGameSeason();
            while (true) {
                if (this.season.getCycleNumber() <= ALL_GAME_NUMBER && this.season.isGamesOvers()) {
                    this.season.margeTeams();
                    this.season.passUpSeasonTeam();
                    break;
                }
            }
        }
            return this.season;
        }



    @RequestMapping(value = "/is_game_start" ,method = {RequestMethod.GET, RequestMethod.POST})
            public boolean isGameStart() {
            return this.season.isGameWasBegin();
        }


    @RequestMapping(value = "/send_bet" ,method = {RequestMethod.GET, RequestMethod.POST})
    public  BasicResponse setBetDetails(int gameId,String betAmount, char myGuess) {
        BasicResponse basicResponse = new BasicResponse();
        int castBetAmount=Integer.parseInt(betAmount);
        int thisCycleNumber = this.season.getCycleNumber();
        if (!this.season.isGameWasBegin()) {
            while (true) {
                if (this.season.getCycleNumber() == thisCycleNumber+PASS_TO_THE_NEXT){
                    Game gamblingGame = null;
                    switch (gameId){
                        case GAME_ONE_ID:
                            gamblingGame = this.season.getGame1();
                            break;
                        case GAME_TWO_ID:
                            gamblingGame = this.season.getGame2();
                            break;
                        case GAME_THREE_ID:
                            gamblingGame = this.season.getGame3();
                            break;
                        case GAME_FOUR_ID:
                            gamblingGame = this.season.getGame4();
                            break;
                    }
                    User user = new User(this.id, this.getBalanceForUser());
                    if (castBetAmount > user.getBalance()) {
                        basicResponse.setErrorCode(YOUR_BALANCE_IS_SMALLER_THEN_BET);
                        return basicResponse;
                    }
                    BetComponent betComponent = new BetComponent(user, gamblingGame, castBetAmount, myGuess);
                    while (true){
                        if (betComponent.getGameOfGambling().isGameIsOver()){
                            basicResponse.setBalance(betComponent.updateBalance()); // עדכון balance בשרת
                            dbUtils.updateBalance(this.id,betComponent.updateBalance()); // עדכון balance בDB
                            break;
                        }
                    }
                    break;
                }
            }

        } else {
            basicResponse.setErrorCode(GAME_WAS_BEGIN);
            return basicResponse;
        }
         return basicResponse;
    }

    @RequestMapping(value = "/sing_up", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse singUp (String name, String password, String email) {
            User userToAdd = new User(name,password,email,BALANCE_START_SUM);
        BasicResponse basicResponse = new BasicResponse();
        if (password.length()>=PASSWORD_LENGTH) {  // להוסיף עוד תנאים לסיסמא חזקה
            if (!dbUtils.checkIfUserExist(email)) {
               basicResponse.setId((dbUtils.addUser(userToAdd)));
               basicResponse.updateSuccess(); // אם קיים ID אזי הSUCCESS יהפוך לTRUE
            } else {
                basicResponse.setErrorCode(USER_ALREADY_EXIST);
            }
        } else {
            basicResponse.setErrorCode(USER_PASSWORD_IS_WEEK);
        }
          return basicResponse;
    }


    }

