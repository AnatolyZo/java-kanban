package ru.javakanban;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static Timetable timetable = new Timetable();

    public static void main(String[] args) {
        boolean running = true;
        initializeTimetable();

        while (running) {
            showMenu();
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    showTrainingsForDay();
                    break;
                case 2:
                    showTrainingsForDayAndTime();
                    break;
                case 3:
                    addTraining();
                    break;
                case 4:
                    timetable.getCountByCoaches();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Неверный выбор.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("Выберите действие:");
        System.out.println("1 — Распечатать все тренировки за день");
        System.out.println("2 — Распечатать все тренировки на определенное время");
        System.out.println("3 — Добавить тренировку");
        System.out.println("4 — Показать статистику по проводимым за неделю занятиям тренерами");
        System.out.println("0 — Завершить");
    }

    private static void initializeTimetable() {
        Group group1 = new Group("Аэробика", Age.ADULT, 120);
        Coach coach1 = new Coach("Иванов", "Дмитрий", "Петрович");
        TimeOfDay timeOfDay1 = new TimeOfDay(13, 30);

        Group group2 = new Group("Гимнастика", Age.ADULT, 120);
        Coach coach2 = new Coach("Сергеев", "Геннадий", "Павлович");
        TimeOfDay timeOfDay2 = new TimeOfDay(13, 30);

        Group group3 = new Group("Рукопашный бой", Age.ADULT, 120);
        Coach coach3 = new Coach("Ульянов", "Игорь", "Николаевич");
        TimeOfDay timeOfDay3 = new TimeOfDay(11, 30);

        Group group4 = new Group("Плавание", Age.CHILD, 120);
        Coach coach4 = new Coach("Бокин", "Афанасий", "Егорович");
        TimeOfDay timeOfDay4 = new TimeOfDay(10, 30);

        TrainingSession trainingSession = new TrainingSession(group1, coach1, DayOfWeek.FRIDAY, timeOfDay1);
        TrainingSession trainingSession2 = new TrainingSession(group2, coach2, DayOfWeek.FRIDAY, timeOfDay2);
        TrainingSession trainingSession3 = new TrainingSession(group3, coach3, DayOfWeek.FRIDAY, timeOfDay3);
        TrainingSession trainingSession4 = new TrainingSession(group4, coach4, DayOfWeek.FRIDAY, timeOfDay4);

        timetable.addNewTrainingSession(trainingSession);
        timetable.addNewTrainingSession(trainingSession2);
        timetable.addNewTrainingSession(trainingSession3);
        timetable.addNewTrainingSession(trainingSession4);
    }

    private static void showTrainingsForDay() {
        System.out.println("Выберите день недели, за который хотите получить расписание занятий (1-7)");
        DayOfWeek day = inputDay();
        LinkedHashMap<TimeOfDay, ArrayList<TrainingSession>> requestedTrainingForDay
                = timetable.getTrainingSessionsForDay(day);

        if (!requestedTrainingForDay.isEmpty()) {
            System.out.printf("%s, следующие занятия:%n", day.getRussianName());
            for (ArrayList<TrainingSession> trainingSession : requestedTrainingForDay.values()) {
                for (TrainingSession training : trainingSession) {
                    System.out.println(training.toString());
                }
            }
        } else {
            System.out.println("В выбранный день недели занятий нет.");
        }
    }

    private static void showTrainingsForDayAndTime() {
        System.out.println("Выберите день недели, за который хотите получить расписание занятий (1-7)");
        DayOfWeek day = inputDay();

        System.out.println("Выберите время," +
                " за которое хотите получить расписание занятий (часы работы зала: с 10 до 22)");
        TimeOfDay time = inputTime();
        ArrayList<TrainingSession> requestedTrainingsForDayAndTime
                = timetable.getTrainingSessionsForDayAndTime(day, time);

        if (requestedTrainingsForDayAndTime != null) {
            System.out.printf("%s, следующие занятия в %s:%n", day.getRussianName(), time);
            for (TrainingSession training : requestedTrainingsForDayAndTime) {
                System.out.println(training.getGroup().toString() + training.getCoach().toString());
            }
        } else {
            System.out.println("В выбранные день недели и время занятий нет.");
        }

    }

    private static void addTraining() {
        System.out.println("Выберите группу");
        Group group = inputGroup();

        System.out.println("Выберите тренера");
        Coach coach = inputCoach();

        System.out.println("Выберите день недели");
        DayOfWeek day = inputDay();

        System.out.println("Выберите время");
        TimeOfDay time = inputTime();

        //Исключение ситуации, когда один и тот же тренер ведет 2 занятия одновременно
        for (TrainingSession training : timetable.getTrainingSessionsForDayAndTime(day, time)) {
            if (training.getCoach().equals(coach)) {
                System.out.printf("Тренер %s уже ведет занятие в это время, добавление невозможно%n", coach);
                return;
            }
        }

        TrainingSession trainingSession = new TrainingSession(group, coach, day, time);
        timetable.addNewTrainingSession(trainingSession);
    }

    private static Group inputGroup() {
        for (Group group : Group.getAllGroupsInGym()) {
            System.out.printf("%d - %s%n", Group.getAllGroupsInGym().indexOf(group) + 1, group);
        }

        while (true) {
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice > 0 & choice <= Group.getAllGroupsInGym().size()) {
                return Group.getAllGroupsInGym().get(choice - 1);
            } else {
                System.out.printf("Вводимое число должно быть в диапазоне от 1 до %d, повторите ввод.",
                        Group.getAllGroupsInGym().size());
            }
        }
    }

    private static Coach inputCoach() {
        for (Coach coach : Coach.getAllCoachesInGym()) {
            System.out.printf("%d - %s%n", Coach.getAllCoachesInGym().indexOf(coach) + 1, coach);
        }

        while (true) {
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice > 0 & choice <= Coach.getAllCoachesInGym().size()) {
                return Coach.getAllCoachesInGym().get(choice - 1);
            } else {
                System.out.printf("Вводимое число должно быть в диапазоне от 1 до %d, повторите ввод.",
                        Coach.getAllCoachesInGym().size());
            }
        }
    }

    private static DayOfWeek inputDay() {
        for (DayOfWeek day : DayOfWeek.values()) {
            System.out.printf("%d - %s%n", day.ordinal() + 1, day.getRussianName());
        }

        while (true) {
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice > 0 & choice <= DayOfWeek.values().length) {
                return DayOfWeek.values()[choice - 1];
            } else {
                System.out.println("Вводимое число должно быть в диапазоне от 1 до 7, повторите ввод.");
            }
        }
    }

    private static TimeOfDay inputTime() {
        System.out.println("Введите час");
        int hours;
        int minutes;

        while (true) {
            hours = Integer.parseInt(scanner.nextLine());

            if (hours >= 10 & hours <= 22) {
                break;
            } else {
                System.out.println("Вводимое число должно быть в диапазоне от 10 до 22, повторите ввод.");
            }
        }

        if (hours != 22) {
            System.out.println("Введите минуты");

            while (true) {
                minutes = Integer.parseInt(scanner.nextLine());

                if (minutes >= 0 & minutes <= 59) {
                    break;
                } else {
                    System.out.println("Вводимое число должно быть в диапазоне от 0 до 59, повторите ввод.");
                }
            }
        } else {
            minutes = 0;
        }

        return new TimeOfDay(hours, minutes);
    }
}
