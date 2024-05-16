package com.ashcollege;

import com.ashcollege.entities.Game;
import com.ashcollege.entities.Team;
import com.ashcollege.entities.User;
import com.ashcollege.utils.DbUtils;

public class BetComponent  {
    private static final int DEAFULT_BET_AMOUNT=0;
    private static final char HOME_CHOOSE='1';
    private static final char GUEST_CHOOSE='2';
    private static final char DRAW_CHOOSE='x';

    private User gambler;
    private Game gameOfGambling;
    private int betAmount;
    private double upBalance;
    private char myGuess;
    private DbUtils dbUtils;

    public BetComponent(User gambler, Game gameOfGambling ,int betAmount, char myGuess) {
        this.gambler = gambler;
        this.gameOfGambling = gameOfGambling;
        this.betAmount=betAmount;
        this.myGuess=myGuess;
        this.upBalance = gambler.getBalance();
    }

    public BetComponent(Team home,Team guest){
        Game gameOfGambling = new Game(home,guest);
        this.gameOfGambling=gameOfGambling;
        this.betAmount=DEAFULT_BET_AMOUNT;
    }

    public double getUpBalance() {
        return upBalance;
    }

    public void setUpBalance(double upBalance) {
        this.upBalance = upBalance;
    }

    public int getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(int betAmount) {
        this.betAmount = betAmount;
    }
    public double updateBalance() { // TODO: ****  לוודא שאכן מתעדכן במשתמש אחרת להוסיף משתמש בקלאס של משחק
        double updateBalance = this.gambler.getBalance() - this.betAmount;
        if (updateBalance >= DEAFULT_BET_AMOUNT ) {
            if (gameOfGambling.getWin() == this.myGuess) { // אם המהמר צדק
                if (myGuess == HOME_CHOOSE) { // קבוצת בית ניצחה
                    updateBalance += this.betAmount * this.gameOfGambling.getBetRatioHome();
                } else if (myGuess == GUEST_CHOOSE) { //  קבוצת חוץ ניצחה
                    updateBalance += this.betAmount * this.gameOfGambling.getBetRatioGuest();
                } else { // תיקו
                    updateBalance += this.betAmount * this.gameOfGambling.getBetRatioDraw();
                }
            }
            this.upBalance =updateBalance;
            this.gambler.setBalance(this.upBalance); // עידכון באלנס של המשתמש בשרת
        }
        this.upBalance =updateBalance;
        return this.upBalance;
    }

    public User getGambler() {
        return gambler;
    }

    public void setGambler(User gambler) {
        this.gambler = gambler;
    }

    public Game getGameOfGambling() {
        return gameOfGambling;
    }

    public void setGameOfGambling(Game gameOfGambling) {
        this.gameOfGambling = gameOfGambling;
    }

    public char getMyGuess() {
        return myGuess;
    }

    public void setMyGuess(char myGuess) {
        this.myGuess = myGuess;
    }
}
