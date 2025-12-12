package ru.javakanban;

import java.util.*;

public class Timetable {

    private Map<DayOfWeek, LinkedHashMap<TimeOfDay, ArrayList<TrainingSession>>> timetable = new HashMap<>();

    private Map<TimeOfDay, ArrayList<TrainingSession>> mapForSorting = new TreeMap<>();

    public Timetable() {
        timetable.put(DayOfWeek.MONDAY, new LinkedHashMap<>());
        timetable.put(DayOfWeek.TUESDAY, new LinkedHashMap<>());
        timetable.put(DayOfWeek.WEDNESDAY, new LinkedHashMap<>());
        timetable.put(DayOfWeek.THURSDAY, new LinkedHashMap<>());
        timetable.put(DayOfWeek.FRIDAY, new LinkedHashMap<>());
        timetable.put(DayOfWeek.SATURDAY, new LinkedHashMap<>());
        timetable.put(DayOfWeek.SUNDAY, new LinkedHashMap<>());
    }


    public void addNewTrainingSession(TrainingSession trainingSession) {
        LinkedHashMap<TimeOfDay, ArrayList<TrainingSession>> dayTrainings
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
            /*С помощью TreeMap сортируем хэш-таблицу времен тренировок и самих занятий,
              затем возвращаем все в LinkedHashMap для сохранения отсортированного порядка, а также
              для более быстрой работы метода getTrainingSessionsForDay*/
            mapForSorting.putAll(dayTrainings);
            dayTrainings.clear();
            dayTrainings.putAll(mapForSorting);
            mapForSorting.clear();
        } else {
            /*Для случая, если на данный день в указанное время уже есть запланированые занятий,
              то добавляем лишь занятие*/
            dayTrainings.get(trainingSession.getTimeOfDay()).add(trainingSession);
        }
    }

    public LinkedHashMap<TimeOfDay, ArrayList<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
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
