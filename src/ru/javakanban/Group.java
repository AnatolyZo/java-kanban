package ru.javakanban;

import java.util.ArrayList;
import java.util.List;

public class Group {
    //название группы
    private String title;
    //тип (взрослая или детская)
    private Age age;
    //длительность (в минутах)
    private int duration;
    private static List<Group> allGroupsInGym = new ArrayList<>();

    public Group(String title, Age age, int duration) {
        this.title = title;
        this.age = age;
        this.duration = duration;
        allGroupsInGym.add(this);
    }

    public static List<Group> getAllGroupsInGym() {
        return allGroupsInGym;
    }

    public String getTitle() {
        return title;
    }

    public Age getAge() {
        return age;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "Группа - " + title +
                " (" + age.getRussianName() +
                "), продолжительность (минут) - " + duration +
                ", ";
    }
}