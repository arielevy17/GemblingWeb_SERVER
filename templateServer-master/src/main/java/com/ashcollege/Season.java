package com.ashcollege;

import com.ashcollege.entities.Game;
import com.ashcollege.entities.Team;
import java.util.*;
public class Season extends Game {
    public final int BET_TIME = 30000;
    public final int START_CYCLE_NUMBER = 1;
    private static final int GAME_ONE_ID=1;
    private static final int GAME_TWO_ID=2;
    private static final int GAME_THREE_ID=3;
    private static final int GAME_FOUR_ID=4;
    private Team team1 = new Team("Arsenal");
    private Team team2 = new Team("Chelsea");
    private Team team3 = new Team("Liverpool");
    private Team team4 = new Team("Manchester City");
    private Team team5 = new Team("Manchester United");
    private Team team6 = new Team("Tottenham Hotspur");
    private Team team7 = new Team("Aston Villa");
    private Team team8 = new Team("West Ham United");
    private Team [] teams={team1,team2,team3,team4,team5,team6,team7,team8};
    private Team [] arrangedTeamsByPoint; // מערך הטבלה
    private Game game1;
    private Game game2;
    private Game game3;
    private Game game4;
    private int cycleNumber;
    private boolean timeToBet;

    public Season() {
        this.game1=new Game(teams[0],teams[7]);
        this.game2=new Game(teams[1],teams[6]);
        this.game3=new Game(teams[2],teams[5]);
        this.game4=new Game(teams[3],teams[4]);
        this.cycleNumber=START_CYCLE_NUMBER;
        this.timeToBet = true;
        this.arrangedTeamsByPoint = this.teams;
    }

        public void passUpSeasonTeam(){ // TODO להוסיף ל8 משחקים
        this.cycleNumber++;
        switch (this.cycleNumber) {
            case GAME_ONE_ID: // פתרון להמשך של הthred לחלק הבא של הפונ'
               // משחק ראשון כבר מותאם מעצם בניית המשחקים
                System.out.println("errors in cycleNumber");
                break;

            case GAME_TWO_ID: // מעבר למחזור 2
                this.game1.setGuest(teams[2]);
                this.game2.setHome(teams[1]);
                this.game2.setGuest(teams[3]);
                this.game3.setHome(teams[4]);
                this.game3.setGuest(teams[7]);
                this.game4.setHome(teams[5]);
                this.game4.setGuest(teams[6]);
                this.game1.betRat();
                this.game2.betRat();
                this.game3.betRat();
                this.game4.betRat();
                break;

            case GAME_THREE_ID: //מעבר למחזור 3
                this.game1.setGuest(this.teams[3]);
                this.game2.setHome(this.teams[2]);
                this.game2.setGuest(this.teams[1]);
                this.game3.setHome(this.teams[5]);
                this.game3.setGuest(this.teams[7]);
                this.game4.setHome(this.teams[4]);
                this.game4.setGuest(this.teams[6]);
                this.game1.betRat();
                this.game2.betRat();
                this.game3.betRat();
                this.game4.betRat();
                break;

            case GAME_FOUR_ID: // מעבר למחזור 4
                this.game1.setGuest(this.teams[4]);
                this.game2.setHome(this.teams[1]);
                this.game2.setGuest(this.teams[5]);
                this.game3.setHome(this.teams[2]);
                this.game3.setGuest(this.teams[6]);
                this.game4.setHome(this.teams[3]);
                this.game4.setGuest(this.teams[7]);
                this.game1.betRat();
                this.game2.betRat();
                this.game3.betRat();
                this.game4.betRat();
                break;




            default:
                System.out.println("cases over!");
        }
        System.out.println("teams change");
    }

    public void margeTeams() { // סידור המערך ע"י ניקוד
        Arrays.sort(arrangedTeamsByPoint, Comparator.comparingInt(Team::getPoint).reversed());
    }

    public Team[] getArrangedTeamsByPoint() {
        return arrangedTeamsByPoint;
    }

    public boolean isTimeToBet() {
        return timeToBet;
    }

    public void setTimeToBet(boolean timeToBet) {
        this.timeToBet = timeToBet;
    }

    public int getCycleNumber() {
        return cycleNumber;
    }

    public void setCycleNumber(int cycleNumber) {
        this.cycleNumber = cycleNumber;
    }

    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public Team getTeam3() {
        return team3;
    }

    public void setTeam3(Team team3) {
        this.team3 = team3;
    }

    public Team getTeam4() {
        return team4;
    }

    public void setTeam4(Team team4) {
        this.team4 = team4;
    }

    public Team getTeam5() {
        return team5;
    }

    public void setTeam5(Team team5) {
        this.team5 = team5;
    }

    public Team getTeam6() {
        return team6;
    }

    public void setTeam6(Team team6) {
        this.team6 = team6;
    }

    public Team getTeam7() {
        return team7;
    }

    public void setTeam7(Team team7) {
        this.team7 = team7;
    }

    public Team getTeam8() {
        return team8;
    }

    public void setTeam8(Team team8) {
        this.team8 = team8;
    }

    public Team[] getTeams() {
        return teams;
    }

    public void setTeams(Team[] teams) {
        this.teams = teams;
    }

    public Game getGame1() {
        return game1;
    }

    public void setGame1(Game game1) {
        this.game1 = game1;
    }

    public Game getGame2() {
        return game2;
    }

    public void setGame2(Game game2) {
        this.game2 = game2;
    }

    public Game getGame3() {
        return game3;
    }

    public void setGame3(Game game3) {
        this.game3 = game3;
    }

    public Game getGame4() {
        return game4;
    }

    public void setGame4(Game game4) {
        this.game4 = game4;
    }

    public void startGameSeason(){
        System.out.println("------------------------!!!!!!!!!!!------------------");
        this.game1.playGame();
        this.game2.playGame();
        this.game3.playGame();
        this.game4.playGame();
    }

    public void whiteForBets(){
        Thread wait = new Thread(() -> {
            try {
                Thread.sleep(BET_TIME);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.timeToBet = false;
        });wait.start();
    }

    public boolean isGameStart(){
        if (game1.isGameWasBegin() && game2.isGameWasBegin() && game3.isGameWasBegin() && game4.isGameWasBegin()){
            return true;
        }
        else return false;
    }

    public boolean isGamesOvers(){
        if (game1.isGameIsOver() && game2.isGameIsOver() && game3.isGameIsOver() && game4.isGameIsOver()){
            return true;
        }
        else return false;
    }

}
