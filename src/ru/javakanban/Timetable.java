package ru.javakanban;

import java.util.*;

public class Timetable {

    private Map<DayOfWeek, TreeMap<TimeOfDay, ArrayList<TrainingSession>>> timetable = new HashMap<>();

    public Timetable() {
        timetable.put(DayOfWeek.MONDAY, new TreeMap<>());
        timetable.put(DayOfWeek.TUESDAY, new TreeMap<>());
        timetable.put(DayOfWeek.WEDNESDAY, new TreeMap<>());
        timetable.put(DayOfWeek.THURSDAY, new TreeMap<>());
        timetable.put(DayOfWeek.FRIDAY, new TreeMap<>());
        timetable.put(DayOfWeek.SATURDAY, new TreeMap<>());
        timetable.put(DayOfWeek.SUNDAY, new TreeMap<>());
    }


    public void addNewTrainingSession(TrainingSession trainingSession) {
        TreeMap<TimeOfDay, ArrayList<TrainingSession>> dayTrainings
                = timetable.get(trainingSession.getDayOfWeek());
        /*При добавлении новой тренировки сразу увеличиваем их количество у соответствующего тренера
          для упрощения вывода списка проведенных занятий по каждому тренеру*/
        trainingSession.getCoach().setNumberOfTrainings();

        /*Для случая, если на данный день  в указанное время не запланировано никаких занятий,
          то добавляем в хэш-таблицу время занятия в качестве ключа и само занятие*/
        if (!dayTrainings.containsKey(trainingSession.getTimeOfDay())) {
            ArrayList<TrainingSession> newTraining = new ArrayList<>();
            newTraining.add(trainingSession);
            dayTrainings.put(trainingSession.getTimeOfDay(), newTraining);
        } else {
            /*Для случая, если на данный день в указанное время уже есть запланированые занятий,
              то добавляем лишь занятие*/
            dayTrainings.get(trainingSession.getTimeOfDay()).add(trainingSession);
        }
    }

    public TreeMap<TimeOfDay, ArrayList<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.get(dayOfWeek);
    }

    public ArrayList<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        return timetable.get(dayOfWeek).get(timeOfDay);
    }

    public void getCountByCoaches() {
        Collections.sort(Coach.getAllCoachesInGym());
        for (Coach coach : Coach.getAllCoachesInGym()) {
            System.out.println((Coach.getAllCoachesInGym().indexOf(coach) + 1) + ". "
                    + coach + ", всего тренировок - " + coach.getNumberOfTrainings());
        }
    }
}
