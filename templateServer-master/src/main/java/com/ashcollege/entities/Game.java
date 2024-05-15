package com.ashcollege.entities;

import com.ashcollege.BetComponent;

import java.util.Random;

public class Game extends Team{
    private final double EXTR_ֹPOINT_FOR_THE_HOME_BET = 0.05;
    private final double EXTR_ֹPOINT_FOR_THE_BET = 0.025;
    private final int MAX_SKILLS_NUM=60;
    private final double EXTR_ֹPOINT_FOR_THE_DRAW_BET = 0.25;
    private final double START_PROBABILITY_BET= 1.05;
    private final int POINT_FOR_WIN= 3;
    private final int EXT_SKILL_FOR_WIN= 2;
    private final int POINT_FOR_DRAW= 1;
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
    this.minute = 0;
    this.homeGoalles = 0;
    this.guestGoalles = 0;
    this.gameWasBegin=true;
    this.gameIsOver=false;
    try {
        Thread minutePass = new Thread(() -> {  // עידכון תוצאה בלייב
            double homeGoalChance = 0;
            double guestGoalChance = 0;
            double bestTeamGoalChance = 0;
            double lessTeamGoalChance = 0;

            if (this.home.getSkills()==this.guest.getSkills()){ // מיקרה קיצון של שיוויון מוחלט
                this.home.setSkills(this.home.getSkills()+0.01);
            }

           try{
            Thread.sleep(3000); // זמן הימורים בין מחזור למחזור: 30 שניות
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

           this.gameWasBegin =true; // סוף זמן הימורים
            int levelDifferences=levelDifferences();
            if (levelDifferences>0) {
                bestTeamGoalChance = 0.075 + levelDifferences * 0.010;
                lessTeamGoalChance = 0.05 - levelDifferences * 0.010;
            } else {
                bestTeamGoalChance=lessTeamGoalChance=0.06;
            }

            if (homeIsBetter()){
                homeGoalChance=bestTeamGoalChance;
                guestGoalChance=lessTeamGoalChance;
            } else {
                guestGoalChance=bestTeamGoalChance;
                homeGoalChance =lessTeamGoalChance;
            }
            homeGoalChance+= 0.05; // תוספת ביתיות

            System.out.println(this.home.getSkills());
           System.out.println(this.guest.getSkills());
           System.out.println( levelDifferences+"הפרשי רמה: ");
            for (int i = 0; i <= 90; i++) {
                try {
                    Thread.sleep(334);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (this.minute>=91){ //איבטוח שהמשחק לא יהיה מעל 90 דק'
                    break;
                }
                // אופן חישוב

                double ratioHome=homeGoalChance+random.nextDouble();
                //System.out.println("ratioHome= "+ratioHome);
                double ratioGuest=guestGoalChance+random.nextDouble();
                //System.out.println("ratioGuest= "+ratioGuest);
                if ( ratioHome>= 1.058) {
                    this.homeGoalles++;
                    System.out.println("home goal hapand------" + homeGoalles + ":" + guestGoalles);
                }
                else if ( ratioGuest>= 1.058) {
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
     int levelDifferences=(int)remainder/10;
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
            this.win='1';
            this.home.setSkills(home.getSkills()+EXT_SKILL_FOR_WIN);
            this.guest.setSkills(guest.getSkills()-EXT_SKILL_FOR_WIN);
            this.home.setPoint(home.getPoint()+POINT_FOR_WIN);
        } else if (guestGoalles>homeGoalles){
            this.win='2';
            this.guest.setSkills(guest.getSkills()+EXT_SKILL_FOR_WIN);
            this.home.setSkills(home.getSkills()-EXT_SKILL_FOR_WIN);
            this.guest.setPoint(guest.getPoint()+POINT_FOR_WIN);
        } else {
            this.win='x';
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
        if (this.betRatioHome<1){
            this.betRatioHome+=1.05;
        }
        if (this.betRatioGuest<1){
            this.betRatioGuest+=1.05;
        }
        if (this.betRatioDraw<1){
            this.betRatioDraw+=1.05;
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
