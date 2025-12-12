import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import ru.javakanban.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class TimetableTest {
    @AfterEach
    void clearGetAllCoachesInGym() {
        Coach.getAllCoachesInGym().clear();
    }

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        Assertions.assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());
        //Проверить, что за вторник не вернулось занятий
        Assertions.assertEquals(0,timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).size());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        Assertions.assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());
        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        Assertions.assertEquals(2, timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY).size());
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                Assertions.assertEquals(thursdayChildTrainingSession,
                        timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY).get(new TimeOfDay(13, 0)).getFirst());
            } else {
                Assertions.assertEquals(thursdayAdultTrainingSession,
                        timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY).get(new TimeOfDay(20, 0)).getFirst());
            }
        }
        // Проверить, что за вторник не вернулось занятий
        Assertions.assertEquals(0, timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).size());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        Assertions.assertEquals(1, timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(13,0)).size());
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        Assertions.assertNull(timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(14,0)));
    }

    @Test
    void testGetTrainingSessionsForDayAndTimeForSameTime() {
        Timetable timetable = new Timetable();

        Group group1 = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession1 = new TrainingSession(group1, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        Group group2 = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach2 = new Coach("Сергеев", "Геннадий", "Павлович");
        TrainingSession singleTrainingSession2 = new TrainingSession(group2, coach2,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession1);
        timetable.addNewTrainingSession(singleTrainingSession2);

        //Проверить, что за понедельник в 13:00 вернулось два занятия
        Assertions.assertEquals(2,
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13,0)).size());
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        Assertions.assertNull(timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(14,0)));
    }

    //Проверяем, что при разовом использовании метода addNewTrainingSession() происходит добавление одной тренировки
    @Test
    void testAddNewTrainingSessionSingleTrainingSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        int countOfTrainingSessions = 0;

        // Переменная для учета пустых ячеек, которая подтверждает, что больше не было добавлений
        int countOfEmptyFields = 0;

        for (int i = 0; i < DayOfWeek.values().length; i++) {
            LinkedHashMap<TimeOfDay, ArrayList<TrainingSession>> dayTrainings =
                    timetable.getTrainingSessionsForDay(DayOfWeek.values()[i]);
            if (dayTrainings.isEmpty()) {
                countOfEmptyFields++;
            } else {
                for (Map.Entry<TimeOfDay, ArrayList<TrainingSession>> entry : dayTrainings.entrySet()) {
                    if (entry.getKey().equals(new TimeOfDay(13, 0))
                            & entry.getValue().getFirst().equals(singleTrainingSession)
                            & entry.getValue().size() == 1) {
                        countOfTrainingSessions++;
                    }
                }
            }
        }

        Assertions.assertEquals(1, countOfTrainingSessions);
        Assertions.assertEquals(6, countOfEmptyFields);
    }

    @Test
    void testAddNewTrainingSessionFiveTrainingSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession trainingSession1 = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession trainingSession2 = new TrainingSession(group, coach,
                DayOfWeek.TUESDAY, new TimeOfDay(13, 0));
        TrainingSession trainingSession3 = new TrainingSession(group, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(13, 0));
        TrainingSession trainingSession4 = new TrainingSession(group, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession trainingSession5 = new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(13, 0));

        ArrayList<TrainingSession> trainingSessions = new ArrayList<>();
        trainingSessions.add(trainingSession1);
        trainingSessions.add(trainingSession2);
        trainingSessions.add(trainingSession3);
        trainingSessions.add(trainingSession4);
        trainingSessions.add(trainingSession5);

        timetable.addNewTrainingSession(trainingSession1);
        timetable.addNewTrainingSession(trainingSession2);
        timetable.addNewTrainingSession(trainingSession3);
        timetable.addNewTrainingSession(trainingSession4);
        timetable.addNewTrainingSession(trainingSession5);

        int countOfTrainingSessions = 0;
        int countOfEmptyFields = 0;

        for (int i = 0; i < DayOfWeek.values().length; i++) {
            LinkedHashMap<TimeOfDay, ArrayList<TrainingSession>> dayTrainings =
                    timetable.getTrainingSessionsForDay(DayOfWeek.values()[i]);
            if (dayTrainings.isEmpty()) {
                countOfEmptyFields++;
            } else {
                for (Map.Entry<TimeOfDay, ArrayList<TrainingSession>> entry : dayTrainings.entrySet()) {
                    if (entry.getKey().equals(new TimeOfDay(13, 0))
                            & trainingSessions.contains(entry.getValue().getFirst()) & entry.getValue().size() == 1) {
                        countOfTrainingSessions++;
                    }
                }
            }
        }

        Assertions.assertEquals(5, countOfTrainingSessions);
        Assertions.assertEquals(2, countOfEmptyFields);
    }

    //Проверка подсчета количества занятий у тренеров, когда есть несколько занятий в одно и то же время
    @Test
    void testGetCountByCoachesAddSameTime() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Дмитрий", "Петрович");
        Coach coach2 = new Coach("Сергеев", "Геннадий", "Павлович");
        Coach coach3 = new Coach("Ульянов", "Игорь", "Николаевич");
        Group group1 = new Group("Аэробика", Age.ADULT, 120);
        Group group2 = new Group("Гимнастика", Age.ADULT, 120);

        TrainingSession trainingSession1 = new TrainingSession(group1, coach1, DayOfWeek.MONDAY,
                new TimeOfDay(15, 0));
        TrainingSession trainingSession2 = new TrainingSession(group2, coach3, DayOfWeek.MONDAY,
                new TimeOfDay(15, 0));
        TrainingSession trainingSession3 = new TrainingSession(group1, coach2, DayOfWeek.MONDAY,
                new TimeOfDay(13, 0));
        TrainingSession trainingSession4 = new TrainingSession(group2, coach3, DayOfWeek.MONDAY,
                new TimeOfDay(13, 0));
        TrainingSession trainingSession5 = new TrainingSession(group1, coach2, DayOfWeek.MONDAY,
                new TimeOfDay(11, 0));
        TrainingSession trainingSession6 = new TrainingSession(group2, coach3, DayOfWeek.MONDAY,
                new TimeOfDay(11, 0));

        timetable.addNewTrainingSession(trainingSession1);
        timetable.addNewTrainingSession(trainingSession2);
        timetable.addNewTrainingSession(trainingSession3);
        timetable.addNewTrainingSession(trainingSession4);
        timetable.addNewTrainingSession(trainingSession5);
        timetable.addNewTrainingSession(trainingSession6);

        //Проверяем подсчет количества занятий
        Assertions.assertEquals(3, coach3.getNumberOfTrainings());
        Assertions.assertEquals(1, coach1.getNumberOfTrainings());
        Assertions.assertEquals(2, coach2.getNumberOfTrainings());

        timetable.getCountByCoaches();

        //Проверяем порядок правильность сортировки по количеству занятий
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                Assertions.assertEquals(coach3, Coach.getAllCoachesInGym().get(0));
            } else if (i == 1) {
                Assertions.assertEquals(coach2, Coach.getAllCoachesInGym().get(1));
            } else {
                Assertions.assertEquals(coach1, Coach.getAllCoachesInGym().get(2));
            }
        }
    }

    //Проверка подсчета количества занятий у тренеров, когда занятия проходят в разное время
    @Test
    void testGetCountByCoachesAddDifferentTime() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Дмитрий", "Петрович");
        Coach coach2 = new Coach("Сергеев", "Геннадий", "Павлович");
        Coach coach3 = new Coach("Ульянов", "Игорь", "Николаевич");
        Group group1 = new Group("Аэробика", Age.ADULT, 120);
        Group group2 = new Group("Гимнастика", Age.ADULT, 120);

        TrainingSession trainingSession1 = new TrainingSession(group1, coach1, DayOfWeek.MONDAY,
                new TimeOfDay(15, 0));
        TrainingSession trainingSession2 = new TrainingSession(group2, coach3, DayOfWeek.MONDAY,
                new TimeOfDay(14, 0));
        TrainingSession trainingSession3 = new TrainingSession(group1, coach2, DayOfWeek.MONDAY,
                new TimeOfDay(13, 0));
        TrainingSession trainingSession4 = new TrainingSession(group2, coach3, DayOfWeek.MONDAY,
                new TimeOfDay(12, 0));
        TrainingSession trainingSession5 = new TrainingSession(group1, coach2, DayOfWeek.MONDAY,
                new TimeOfDay(11, 0));
        TrainingSession trainingSession6 = new TrainingSession(group2, coach3, DayOfWeek.MONDAY,
                new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(trainingSession1);
        timetable.addNewTrainingSession(trainingSession2);
        timetable.addNewTrainingSession(trainingSession3);
        timetable.addNewTrainingSession(trainingSession4);
        timetable.addNewTrainingSession(trainingSession5);
        timetable.addNewTrainingSession(trainingSession6);

        Assertions.assertEquals(3, coach3.getNumberOfTrainings());
        Assertions.assertEquals(1, coach1.getNumberOfTrainings());
        Assertions.assertEquals(2, coach2.getNumberOfTrainings());

        timetable.getCountByCoaches();

        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                Assertions.assertEquals(coach3, Coach.getAllCoachesInGym().get(0));
            } else if (i == 1) {
                Assertions.assertEquals(coach2, Coach.getAllCoachesInGym().get(1));
            } else {
                Assertions.assertEquals(coach1, Coach.getAllCoachesInGym().get(2));
            }
        }
    }

    //Проверка подсчета количества занятий у тренеров, когда занятия проходят в разные дни
    @Test
    void testGetCountByCoachesAddDifferentDays() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Дмитрий", "Петрович");
        Coach coach2 = new Coach("Сергеев", "Геннадий", "Павлович");
        Coach coach3 = new Coach("Ульянов", "Игорь", "Николаевич");
        Group group1 = new Group("Аэробика", Age.ADULT, 120);
        Group group2 = new Group("Гимнастика", Age.ADULT, 120);

        TrainingSession trainingSession1 = new TrainingSession(group1, coach1, DayOfWeek.MONDAY,
                new TimeOfDay(15, 0));
        TrainingSession trainingSession2 = new TrainingSession(group2, coach3, DayOfWeek.TUESDAY,
                new TimeOfDay(14, 0));
        TrainingSession trainingSession3 = new TrainingSession(group1, coach2, DayOfWeek.WEDNESDAY,
                new TimeOfDay(13, 0));
        TrainingSession trainingSession4 = new TrainingSession(group2, coach3, DayOfWeek.THURSDAY,
                new TimeOfDay(12, 0));
        TrainingSession trainingSession5 = new TrainingSession(group1, coach2, DayOfWeek.FRIDAY,
                new TimeOfDay(11, 0));
        TrainingSession trainingSession6 = new TrainingSession(group2, coach3, DayOfWeek.SATURDAY,
                new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(trainingSession1);
        timetable.addNewTrainingSession(trainingSession2);
        timetable.addNewTrainingSession(trainingSession3);
        timetable.addNewTrainingSession(trainingSession4);
        timetable.addNewTrainingSession(trainingSession5);
        timetable.addNewTrainingSession(trainingSession6);

        Assertions.assertEquals(3, coach3.getNumberOfTrainings());
        Assertions.assertEquals(1, coach1.getNumberOfTrainings());
        Assertions.assertEquals(2, coach2.getNumberOfTrainings());

        timetable.getCountByCoaches();

        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                Assertions.assertEquals(coach3, Coach.getAllCoachesInGym().get(0));
            } else if (i == 1) {
                Assertions.assertEquals(coach2, Coach.getAllCoachesInGym().get(1));
            } else {
                Assertions.assertEquals(coach1, Coach.getAllCoachesInGym().get(2));
            }
        }
    }
}

