package com.ashcollege.entities;

import java.util.Random;

public class Game extends Team{
    private static final char HOME_WIN_SYMBOL ='1';
    private static final char GUEST_WIN_SYMBOL='2';
    private static final char DRAW_WIN_SYMBOL ='x';
    private final double LOWEST_CHANCE_FOR_GOALL= 1.058;
    private final int SKALAR_SPLIT_LEVL_DIFERENCE= 10;
    private final double LOWEST_BET= 1.05;
    private final int NOT_BENEFIT_BET= 1;
    private final double EXTR_ֹPOINT_FOR_THE_HOME_BET = 0.05;
    private final double EXTR_ֹPOINT_FOR_THE_BET = 0.025;
    private final int MAX_SKILLS_NUM=60;
    private final double EXTR_ֹPOINT_FOR_THE_DRAW_BET = 0.25;
    private final double START_PROBABILITY_BET= 1.05;
    private final int POINT_FOR_WIN= 3;
    private final int EXT_SKILL_FOR_WIN= 2;
    private final int POINT_FOR_DRAW= 1;
    private final int START_GAME_GOALLES= 0;
    private final int START_GAME_MINUTE= 0;
    private final int START_BET= 0;
    private final int WHEITTING_FOR_BET_TIME= 3000;
    private final double EXTRA_SKILLS_FOR_HOME= 0.01;
    private final int SMALLEST_LEVEL_DIFRENCE= 0;
    private final double EXTRA_CHANCE_TO_BETTER_BY_SKILLS= 0.075;
    private final double EXTRA_CHANCE_TO_LESS_BETTER_BY_SKILLS= 0.05;
    private final double SKALAR_MULT_CHANCE= 0.010;
    private final double IF_EQUAL_CHANCE= 0.06;
    private final double EXTRA_CHANCE_FOR_HOME= 0.05;
    private final int GAME_MINUTE_NUMBER= 91;
    private final int TIME_SLEEP_BETWEEN_EVERY_MINUTE= 334;

    private Team home;
    private Team guest;
    private double betRatioHome;
    private double betRatioGuest;
    private double betRatioDraw;
    private int homeGoalles;
    private int guestGoalles;
    private char win;
    private int minute;
    private boolean gameWasBegin;
    private boolean gameIsOver;
    Random random = new Random();
    public Game(Team home,Team guest){
        this.home=home;
        this.guest=guest;
        this.home.setHome(true);
        betRat();
        this.win='?';
        this.gameWasBegin=false;
        this.gameIsOver=false;
    }

    public Game(){};


    // מחולל תוצאות
public void playGame() {
    this.minute = START_GAME_MINUTE;
    this.homeGoalles = START_GAME_GOALLES;
    this.guestGoalles = START_GAME_GOALLES;
    this.gameWasBegin=true;
    this.gameIsOver=false;
    try {
        Thread minutePass = new Thread(() -> {  // עידכון תוצאה בלייב
            double homeGoalChance = START_GAME_GOALLES;;
            double guestGoalChance = START_GAME_GOALLES;
            double bestTeamGoalChance = START_BET;
            double lessTeamGoalChance = START_BET;

            if (this.home.getSkills()==this.guest.getSkills()){ // מיקרה קיצון של שיוויון מוחלט
                this.home.setSkills(this.home.getSkills()+EXTRA_SKILLS_FOR_HOME);
            }

           try{
            Thread.sleep(WHEITTING_FOR_BET_TIME); // זמן הימורים בין מחזור למחזור: 30 שניות
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

           this.gameWasBegin =true; // סוף זמן הימורים
            int levelDifferences=levelDifferences();
            if (levelDifferences>SMALLEST_LEVEL_DIFRENCE) {
                bestTeamGoalChance = EXTRA_CHANCE_TO_BETTER_BY_SKILLS + levelDifferences * SKALAR_MULT_CHANCE;
                lessTeamGoalChance = EXTRA_CHANCE_TO_LESS_BETTER_BY_SKILLS - levelDifferences * SKALAR_MULT_CHANCE;
            } else {
                bestTeamGoalChance=lessTeamGoalChance=IF_EQUAL_CHANCE;
            }

            if (homeIsBetter()){
                homeGoalChance=bestTeamGoalChance;
                guestGoalChance=lessTeamGoalChance;
            } else {
                guestGoalChance=bestTeamGoalChance;
                homeGoalChance =lessTeamGoalChance;
            }
            homeGoalChance+= EXTRA_CHANCE_FOR_HOME; // תוספת ביתיות

            System.out.println(this.home.getSkills());
           System.out.println(this.guest.getSkills());
           System.out.println( levelDifferences+"הפרשי רמה: ");
            for (int i = 0; i < GAME_MINUTE_NUMBER; i++) {
                try {
                    Thread.sleep(TIME_SLEEP_BETWEEN_EVERY_MINUTE);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (this.minute>=GAME_MINUTE_NUMBER){ //איבטוח שהמשחק לא יהיה מעל 90 דק'
                    break;
                }
                // אופן חישוב

                double ratioHome=homeGoalChance+random.nextDouble();
                //System.out.println("ratioHome= "+ratioHome);
                double ratioGuest=guestGoalChance+random.nextDouble();
                //System.out.println("ratioGuest= "+ratioGuest);
                if ( ratioHome>= LOWEST_CHANCE_FOR_GOALL) {
                    this.homeGoalles++;
                    System.out.println("home goal hapand------" + homeGoalles + ":" + guestGoalles);
                }
                else if ( ratioGuest>= LOWEST_CHANCE_FOR_GOALL) {
                    this.guestGoalles++;
                    System.out.println("guest goal hapand------" + homeGoalles + ":" + guestGoalles);
                }
               // System.out.println("min: " + minute);
                this.minute++;
            }
            this.setWin(); // לגנרל קונטרולר אם לא עובד
            this.gameIsOver=true;
            this.gameWasBegin=false;
        }); minutePass.start();
    } catch (Exception e) {
        e.printStackTrace();
    }
  }


    public double getBetRatioHome() {
        return betRatioHome;
    }
 public int levelDifferences(){
     // פונקציה המחליטה אילו הפרשי רמות בין הקבוצות:
     double remainder = 0;
     if (homeIsBetter()){
         remainder = this.home.getSkills()-this.guest.getSkills();
     } else {
         remainder = this.guest.getSkills() -this. home.getSkills();
     }

     // הפרשי רמות
     int levelDifferences=(int)remainder/SKALAR_SPLIT_LEVL_DIFERENCE;
     return levelDifferences;
 }
    public void setBetRatioHome(double betRatioHome) {
        this.betRatioHome = betRatioHome;
    }

    public double getBetRatioGuest() {
        return betRatioGuest;
    }

    public void setBetRatioGuest(double betRatioGuest) {
        this.betRatioGuest = betRatioGuest;
    }

    public int getHomeGoalles() {
        return homeGoalles;
    }

    public void setHomeGoalles(int homeGoalles) {
        this.homeGoalles = homeGoalles;
    }

    public int getGuestGoalles() {
        return guestGoalles;
    }

    public void setGuestGoalles(int guestGoalles) {
        this.guestGoalles = guestGoalles;
    }

    public char getWin() {
        return win;
    }

    public void setWin() {
        if (homeGoalles>guestGoalles){
            this.win= HOME_WIN_SYMBOL;
            this.home.setSkills(home.getSkills()+EXT_SKILL_FOR_WIN);
            this.guest.setSkills(guest.getSkills()-EXT_SKILL_FOR_WIN);
            this.home.setPoint(home.getPoint()+POINT_FOR_WIN);
        } else if (guestGoalles>homeGoalles){
            this.win=GUEST_WIN_SYMBOL;
            this.guest.setSkills(guest.getSkills()+EXT_SKILL_FOR_WIN);
            this.home.setSkills(home.getSkills()-EXT_SKILL_FOR_WIN);
            this.guest.setPoint(guest.getPoint()+POINT_FOR_WIN);
        } else {
            this.win= DRAW_WIN_SYMBOL;
            this.home.setPoint(home.getPoint()+POINT_FOR_DRAW);
            this.guest.setPoint(guest.getPoint()+POINT_FOR_DRAW);
        }
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    //  מחלק יחסי הימור
    public void betRat(){
        this.betRatioDraw=START_PROBABILITY_BET+levelDifferences()*EXTR_ֹPOINT_FOR_THE_DRAW_BET; // תיקו
        if (homeIsBetter()){
         this.betRatioHome=START_PROBABILITY_BET+(MAX_SKILLS_NUM-home.getSkills())*EXTR_ֹPOINT_FOR_THE_BET;
         this.betRatioGuest=betRatioHome+(levelDifferences()*EXTR_ֹPOINT_FOR_THE_BET);
        } else {
            this.betRatioGuest=START_PROBABILITY_BET+(MAX_SKILLS_NUM-home.getSkills())*EXTR_ֹPOINT_FOR_THE_BET;
            this.betRatioHome=betRatioHome+(levelDifferences()*EXTR_ֹPOINT_FOR_THE_BET);
        }
        betRatioHome+=EXTR_ֹPOINT_FOR_THE_HOME_BET;

        //  ווידוא שהיחס הימור לא קטן מ1
        if (this.betRatioHome<NOT_BENEFIT_BET){
            this.betRatioHome+=LOWEST_BET;
        }
        if (this.betRatioGuest<NOT_BENEFIT_BET){
            this.betRatioGuest+=LOWEST_BET;
        }
        if (this.betRatioDraw<NOT_BENEFIT_BET){
            this.betRatioDraw+=LOWEST_BET;
        }
    }
    public boolean homeIsBetter(){
        boolean ans = false;
        if (this.home.getSkills()>this.guest.getSkills()){
            ans= true;
        }
        return  ans;
    }

    public double getBetRatioDraw() {
        return betRatioDraw;
    }

    public void setBetRatioDraw(double betRatioDraw) {
        this.betRatioDraw = betRatioDraw;
    }

    public Team getHome() {
        return home;
    }

    public void setHome(Team home) {
        this.home = home;
    }

    public Team getGuest() {
        return guest;
    }

    public void setGuest(Team guest) {
        this.guest = guest;
    }

    public boolean isGameWasBegin() {
        return gameWasBegin;
    }

    public void setGameWasBegin(boolean gameWasBegin) {
        this.gameWasBegin = gameWasBegin;
    }

    public boolean isGameIsOver() {
        return gameIsOver;
    }

    public void setGameIsOver(boolean gameIsOver) {
        this.gameIsOver = gameIsOver;
    }
}
