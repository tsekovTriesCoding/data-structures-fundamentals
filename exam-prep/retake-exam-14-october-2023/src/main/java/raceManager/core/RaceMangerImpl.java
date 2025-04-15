package raceManager.core;

import raceManager.models.Athlete;

import java.util.*;
import java.util.stream.Collectors;

public class RaceMangerImpl implements RaceManger {
    private List<Athlete> allAthletes = new ArrayList<>();
    private Deque<Athlete> enrolledAthletes = new ArrayDeque<>();
    private Set<Athlete> startedAthletes = new LinkedHashSet<>();
    private Deque<Athlete> finishedAtlethes = new ArrayDeque<>();
    private List<Athlete> notFinishedAtlethes = new ArrayList<>();

    @Override
    public void enroll(Athlete athlete) {
        if (this.enrolledAthletes.contains(athlete)) {
            throw new IllegalArgumentException();
        }

        this.enrolledAthletes.offer(athlete);
        this.allAthletes.add(athlete);
    }

    @Override
    public boolean isEnrolled(Athlete athlete) {
        return this.enrolledAthletes.contains(athlete);
    }

    @Override
    public void start() {
        if (this.enrolledAthletes.isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.startedAthletes.add(this.enrolledAthletes.poll());
    }

    @Override
    public void retire(Athlete athlete) {
        if (!this.startedAthletes.contains(athlete)) {
            throw new IllegalArgumentException();
        }

        this.startedAthletes.remove(athlete);
        this.notFinishedAtlethes.add(athlete);
    }

    @Override
    public void finish(Athlete athlete) {
        if (!this.startedAthletes.contains(athlete)) {
            throw new IllegalArgumentException();
        }

        this.startedAthletes.remove(athlete);
        this.finishedAtlethes.push(athlete);
    }

    @Override
    public Athlete getLastFinishedAthlete() {
        if (this.finishedAtlethes.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return this.finishedAtlethes.getFirst();
    }

    @Override
    public int currentRacingCount() {
        if (this.allAthletes.isEmpty() || this.startedAthletes.isEmpty() || this.finishedAtlethes.size() == this.allAthletes.size()) {
            return 0;
        }

        return this.startedAthletes.size();
    }

    @Override
    public Collection<Athlete> getAllAthletesByAge() {
        return this.allAthletes
                .stream()
                .sorted(Comparator.comparingInt(a -> a.age))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Athlete> getAllNotFinishedAthletes() {
        List<Athlete> notFinishedAthletes = new ArrayList<>(this.notFinishedAtlethes);

        this.enrolledAthletes
                .stream()
                .filter(athlete -> !this.startedAthletes.contains(athlete))
                .forEach(notFinishedAthletes::add);

        return notFinishedAthletes
                .stream()
                .sorted(Comparator.comparing(athlete -> athlete.name))
                .collect(Collectors.toList());
    }

    @Override
    public Iterator<Athlete> getScoreBoard() {
        return this.finishedAtlethes.iterator();
    }
}
