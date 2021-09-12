package com.boss.imuno.data;

public class User {

    private int Age;
    private int Weight;
    private double Level;
    private int Height;
    private int Race;
    private int Minutes;

    private double TargetLevel = 0;
    private double TotalAmount = 0;
    private double RecommendedAmount = 0;

    public User(){}

    public User(int age, int weight, double level, int height, int race, int minutes){
        Age = age;
        Weight = weight;
        Level = level;
        Minutes = minutes;
        Height = height;
        Race = race;

        double BMI = weight/ Math.pow(height/100.0, 2);

        double sunlightFactor;

        switch(Race){
            case 1:
                sunlightFactor = Minutes/15.0;
                break;
            case 2:
                sunlightFactor = Minutes/30.0;
                break;
            case 3:
                sunlightFactor = Minutes/40.0;
                break;
            case 4:
                sunlightFactor = Minutes/60.0;
                break;
            default:
                sunlightFactor = 1;
                break;

        }

        if(level == 0){
            TargetLevel = 60.062;
            TotalAmount = 0;
            RecommendedAmount = (weight * 60);
            RecommendedAmount = RecommendedAmount - RecommendedAmount*sunlightFactor*0.4;
        } else{
            TargetLevel = 60.062;
            TotalAmount = 60-Level;
            RecommendedAmount = ((8.52 - (TargetLevel-Level)) + (0.074 * Age) - (0.2 * BMI) + (1.74 * 4.4) - (0.62 * Level))/(-0.002);
            RecommendedAmount = RecommendedAmount - RecommendedAmount*sunlightFactor*0.4;
        }
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int weight) {
        Weight = weight;
    }

    public double getLevel() {
        return Level;
    }

    public void setLevel(double level) {
        Level = level;
    }

    public int getMinutes() {
        return Minutes;
    }

    public void setMinutes(int minutes) {
        Minutes = minutes;
    }

    public double getTargetLevel() {
        return TargetLevel;
    }

    public void setTargetLevel(double targetLevel) {
        TargetLevel = targetLevel;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }

    public double getRecommendedAmount() {
        return RecommendedAmount;
    }

    public void setRecommendedAmount(double recommendedAmount) {
        RecommendedAmount = recommendedAmount;
    }

    public int getHeight() {
        return Height;
    }

    public void setHeight(int height) {
        Height = height;
    }

    public int getRace() {
        return Race;
    }

    public void setRace(int race) {
        Race = race;
    }
}
