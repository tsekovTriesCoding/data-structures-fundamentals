package org.softuni.exam.structures;

import org.softuni.exam.entities.Deliverer;
import org.softuni.exam.entities.Package;

import java.util.*;
import java.util.stream.Collectors;

public class DeliveriesManagerImpl implements DeliveriesManager {
    private Map<String, Deliverer> deliverers = new LinkedHashMap<>();
    private Map<String, Package> packages = new LinkedHashMap<>();
    private Map<String, Integer> packagesByDeliverer = new LinkedHashMap<>();
    private Map<String, Package> unnasignedPackages = new LinkedHashMap<>();

    @Override
    public void addDeliverer(Deliverer deliverer) {
        this.deliverers.put(deliverer.getId(), deliverer);
        this.packagesByDeliverer.put(deliverer.getId(), 0);
    }

    @Override
    public void addPackage(Package _package) {
        this.packages.put(_package.getId(), _package);
        this.unnasignedPackages.put(_package.getId(), _package);
    }

    @Override
    public boolean contains(Deliverer deliverer) {
        return this.deliverers.containsKey(deliverer.getId());
    }

    @Override
    public boolean contains(Package _package) {
        return this.packages.containsKey(_package.getId());
    }

    @Override
    public Iterable<Deliverer> getDeliverers() {
        return this.deliverers.values();
    }

    @Override
    public Iterable<Package> getPackages() {
        return this.packages.values();
    }

    @Override
    public void assignPackage(Deliverer deliverer, Package _package) throws IllegalArgumentException {
        if (!this.deliverers.containsKey(deliverer.getId()) || !this.packages.containsKey(_package.getId())) {
            throw new IllegalArgumentException();
        }

        this.packagesByDeliverer.put(deliverer.getId(), this.packagesByDeliverer.get(deliverer.getId()) + 1);
        this.unnasignedPackages.remove(_package.getId());
    }

    @Override
    public Iterable<Package> getUnassignedPackages() {
        return this.unnasignedPackages.values();
    }

    @Override
    public Iterable<Package> getPackagesOrderedByWeightThenByReceiver() {
        return this.packages.values()
                .stream()
                .sorted(Comparator.comparingDouble(Package::getWeight).reversed()
                        .thenComparing(Package::getReceiver))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Deliverer> getDeliverersOrderedByCountOfPackagesThenByName() {
        return this.deliverers.values()
                .stream()
                .sorted(Comparator.comparingInt((Deliverer d) -> this.packagesByDeliverer.get(d.getId())).reversed()
                        .thenComparing(Deliverer::getName))
                .collect(Collectors.toList());
    }
}
