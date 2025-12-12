package ru.javakanban;

import java.util.*;

public class Coach implements Comparable<Coach> {

    //фамилия
    private String surname;
    //имя
    private String name;
    //отчество
    private String middleName;
    private int numberOfTrainings = 0;
    private static List<Coach> allCoachesInGym = new ArrayList<>();

    public Coach(String surname, String name, String middleName) {
        this.surname = surname;
        this.name = name;
        this.middleName = middleName;
        allCoachesInGym.add(this);
    }

    public static List<Coach> getAllCoachesInGym() {
        return allCoachesInGym;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getMiddleName() {
        return middleName;
    }

    @Override
    public String toString() {
        return "тренер - " +
                 surname + ' ' +
                 name + ' ' +
                 middleName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Coach coach = (Coach) o;
        return Objects.equals(surname, coach.surname) && Objects.equals(name, coach.name)
                && Objects.equals(middleName, coach.middleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surname, name, middleName);
    }

    public void setNumberOfTrainings() {
        numberOfTrainings++;
    }

    @Override
    public int compareTo(Coach o) {
        return o.numberOfTrainings - this.numberOfTrainings;
    }

    public int getNumberOfTrainings() {
        return numberOfTrainings;
    }
}