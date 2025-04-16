package craftsmanLab.core;

import craftsmanLab.models.ApartmentRenovation;
import craftsmanLab.models.Craftsman;

import java.util.*;
import java.util.stream.Collectors;

public class CraftsmanLabImpl implements CraftsmanLab {
    private final Map<String, ApartmentRenovation> apartmentRenovations = new LinkedHashMap<>();
    private final Map<String, Craftsman> craftsmen = new HashMap<>();
    private final Map<String, Craftsman> craftsmanByApartment = new HashMap<>();

    @Override
    public void addApartment(ApartmentRenovation job) {
        if (this.apartmentRenovations.containsKey(job.address)) {
            throw new IllegalArgumentException();
        }

        this.apartmentRenovations.put(job.address, job);
    }

    @Override
    public void addCraftsman(Craftsman craftsman) {
        if (this.craftsmen.containsKey(craftsman.name)) {
            throw new IllegalArgumentException();
        }

        this.craftsmen.put(craftsman.name, craftsman);
    }

    @Override
    public boolean exists(ApartmentRenovation job) {
        return this.apartmentRenovations.containsKey(job.address);
    }

    @Override
    public boolean exists(Craftsman craftsman) {
        return this.craftsmen.containsKey(craftsman.name);
    }

    @Override
    public void removeCraftsman(Craftsman craftsman) {
        if (!this.craftsmen.containsKey(craftsman.name) ||
                this.craftsmanByApartment.values().stream().anyMatch(c -> c.name.equals(craftsman.name))) {
            throw new IllegalArgumentException();
        }

        this.craftsmen.remove(craftsman.name);
    }

    @Override
    public Collection<Craftsman> getAllCraftsmen() {
        return this.craftsmen.values();
    }

    @Override
    public void assignRenovations() {
        for (ApartmentRenovation job : this.apartmentRenovations.values()) {
            if (!this.craftsmanByApartment.containsKey(job.address)) {
                Craftsman currentCraftsman = this.getLeastProfitable();
                this.craftsmanByApartment.put(job.address, currentCraftsman);
                currentCraftsman.totalEarnings += job.workHoursNeeded * currentCraftsman.hourlyRate;
                this.craftsmen.put(currentCraftsman.name, currentCraftsman);
            }
        }
    }

    @Override
    public Craftsman getContractor(ApartmentRenovation job) {
        if (!this.craftsmanByApartment.containsKey(job.address)) {
            throw new IllegalArgumentException();
        }

        return this.craftsmanByApartment.get(job.address);
    }

    @Override
    public Craftsman getLeastProfitable() {
        if (this.craftsmen.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return this.craftsmen.values()
                .stream()
                .min(Comparator.comparing(c -> c.totalEarnings))
                .get();
    }

    @Override
    public Collection<ApartmentRenovation> getApartmentsByRenovationCost() {
        return this.apartmentRenovations.values()
                .stream()
                .sorted(Comparator.comparing((ApartmentRenovation a) -> {
                    if (this.craftsmanByApartment.get(a.address) == null) {
                        return a.workHoursNeeded;
                    }

                    return this.craftsmanByApartment.get(a.address).totalEarnings;
                }).reversed()).collect(Collectors.toList());
    }

    @Override
    public Collection<ApartmentRenovation> getMostUrgentRenovations(int limit) {
        if (this.apartmentRenovations.size() < limit) {
            return this.apartmentRenovations.values()
                    .stream()
                    .sorted(Comparator.comparing(a -> a.deadline))
                    .collect(Collectors.toList());
        }


        return this.apartmentRenovations.values()
                .stream()
                .sorted(Comparator.comparing(a -> a.deadline))
                .limit(limit)
                .collect(Collectors.toList());
    }
}
