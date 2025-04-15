package org.softuni.exam.structures;

import org.softuni.exam.entities.Airline;
import org.softuni.exam.entities.Deliverer;
import org.softuni.exam.entities.Flight;

import java.sql.Struct;
import java.util.*;
import java.util.stream.Collectors;

public class AirlinesManagerImpl implements AirlinesManager {
    private Map<String, Airline> airlines = new HashMap<>();
    private Map<String, Flight> flights = new HashMap<>();
    private Map<String, Set<Flight>> flightsByAirline = new HashMap<>();
    private Set<Flight> completedFlights = new HashSet<>();

    @Override
    public void addAirline(Airline airline) {
        this.airlines.put(airline.getId(), airline);
        this.flightsByAirline.put(airline.getId(), new HashSet<>());
    }

    @Override
    public void addFlight(Airline airline, Flight flight) {
        if (!this.airlines.containsKey(airline.getId())) {
            throw new IllegalArgumentException();
        }

        this.flights.put(flight.getId(), flight);
        this.flightsByAirline.get(airline.getId()).add(flight);

        if (flight.isCompleted()) {
            this.completedFlights.add(flight);
        }
    }

    @Override
    public boolean contains(Airline airline) {
        return this.airlines.containsKey(airline.getId());
    }

    @Override
    public boolean contains(Flight flight) {
        return this.flights.containsKey(flight.getId());
    }

    @Override
    public void deleteAirline(Airline airline) throws IllegalArgumentException {
        if (!this.airlines.containsKey(airline.getId())) {
            throw new IllegalArgumentException();
        }

        this.airlines.remove(airline.getId());
        Set<Flight> removedFlights = this.flightsByAirline.remove(airline.getId());

        removedFlights.forEach(flight -> this.flights.remove(flight.getId()));
    }

    @Override
    public Iterable<Flight> getAllFlights() {
        return this.flights.values();
    }

    @Override
    public Flight performFlight(Airline airline, Flight flight) throws IllegalArgumentException {
        if (!this.airlines.containsKey(airline.getId()) || !this.flights.containsKey(flight.getId())) {
            throw new IllegalArgumentException();
        }

        this.flights.get(flight.getId()).setCompleted(true);
        this.completedFlights.add(flight);

        return flight;
    }

    @Override
    public Iterable<Flight> getCompletedFlights() {
        return this.completedFlights;
    }

    @Override
    public Iterable<Flight> getFlightsOrderedByNumberThenByCompletion() {
        return this.flights.values()
                .stream()
                .sorted(Comparator.comparing(Flight::isCompleted)
                        .thenComparing(Flight::getNumber))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Airline> getAirlinesOrderedByRatingThenByCountOfFlightsThenByName() {
        return this.airlines.values()
                .stream()
                .sorted(Comparator.comparing(Airline::getRating)
                        .thenComparingInt(f -> this.flightsByAirline.get(f.getId()).size()).reversed()
                        .thenComparing(Airline::getName))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Airline> getAirlinesWithFlightsFromOriginToDestination(String origin, String destination) {
        return this.airlines.values()
                .stream()
                .filter(a -> this.flightsByAirline.get(a.getId()).stream()
                        .anyMatch(flight -> !flight.isCompleted() && flight.getOrigin().equals(origin)
                                && flight.getDestination().equals(destination)))
                .collect(Collectors.toList());
    }
}
