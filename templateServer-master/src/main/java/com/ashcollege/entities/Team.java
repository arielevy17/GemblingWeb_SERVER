package com.ashcollege.entities;

import java.util.Random;

public class Team {
    private final int MAX_SKILLS_NUM=60;
    Random random = new Random();
    private String name;
    private int point;
    private double skills;
    private boolean home;

    public Team(String name ) {
        this.name = name;
       this.point = 0;
        this.skills = random.nextFloat(MAX_SKILLS_NUM);
    }


    public Team(){};
    public void upDateSkillByHome(){
        if (this.home){
            skills+=5;
        }
    }

    public boolean isHome() {
        return home;
    }

    public void setHome(boolean home) {
        this.home = home;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public double getSkills() {
        return skills;
    }

    public void setSkills(double skills) {
        this.skills = skills;
    }
}
