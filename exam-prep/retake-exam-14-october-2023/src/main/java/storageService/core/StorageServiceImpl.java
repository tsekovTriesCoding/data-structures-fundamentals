package storageService.core;


import storageService.models.Box;
import storageService.models.StorageUnit;

import java.util.*;
import java.util.stream.Collectors;

public class StorageServiceImpl implements StorageService {
    private Map<String, StorageUnit> storage = new HashMap<>();
    private Map<String, Box> boxes = new HashMap<>();
    private Map<String, List<Box>> boxesByStorageUnit = new HashMap<>();
    private StorageUnit mostAvailableSpaceUnit;

    @Override
    public void rentStorage(StorageUnit unit) {
        if (this.storage.containsKey(unit.id)) {
            throw new IllegalArgumentException("Storage already exists");
        }

        if (this.storage.isEmpty()) {
            this.mostAvailableSpaceUnit = unit;
        } else {
            int toRentFreeSpace = unit.totalAvailableSpace - unit.totalUsedSpace;
            int mostAvailableSpaceUnit = this.mostAvailableSpaceUnit.totalAvailableSpace - this.mostAvailableSpaceUnit.totalUsedSpace;
            if (toRentFreeSpace > mostAvailableSpaceUnit) {
                this.mostAvailableSpaceUnit = unit;
            }
        }

        this.storage.put(unit.id, unit);
        this.boxesByStorageUnit.put(unit.id, new ArrayList<>());
    }

    @Override
    public void storeBox(Box box) {
        if (isStored(box)) {
            throw new IllegalArgumentException("Box already exists");
        }

        StorageUnit mostAvailableSpaceUnit = this.getMostAvailableSpaceUnit();

        int boxVolume = box.width * box.depth * box.height;

        if (mostAvailableSpaceUnit.totalAvailableSpace - mostAvailableSpaceUnit.totalUsedSpace < boxVolume) {
            throw new IllegalArgumentException();
        }

        if (this.storage.values().stream().noneMatch(s -> s.totalAvailableSpace - s.totalUsedSpace >= boxVolume)) {
            throw new IllegalArgumentException();
        }

        this.boxes.put(box.id, box);
        this.storage.get(mostAvailableSpaceUnit.id).totalUsedSpace += boxVolume;

        this.boxesByStorageUnit.get(mostAvailableSpaceUnit.id).add(box);
    }

    @Override
    public boolean isStored(Box box) {
        return this.boxes.containsKey(box.id);
    }

    @Override
    public boolean isRented(StorageUnit unit) {
        return this.storage.containsKey(unit.id);
    }

    @Override
    public boolean contains(StorageUnit unit, String boxId) {
        List<Box> boxes = this.boxesByStorageUnit.get(unit.id);

        if (boxes == null) {
            return false;
        }

        return boxes.stream().anyMatch(box -> box.id.equals(boxId));
    }

    @Override
    public Box retrieve(StorageUnit unit, String boxId) {
        if (!this.boxes.containsKey(boxId) ||
                this.boxesByStorageUnit.get(unit.id).stream().noneMatch(box -> box.id.equals(boxId))) {
            throw new IllegalArgumentException();
        }

        Box toRemove = this.boxes.get(boxId);
        int volume = toRemove.depth * toRemove.width * toRemove.height;
        this.boxes.remove(boxId);
        this.boxesByStorageUnit.get(unit.id).remove(toRemove);
        this.storage.get(unit.id).totalUsedSpace -= volume;

        return toRemove;
    }

    @Override
    public int getTotalFreeSpace() {
        return this.storage.values()
                .stream()
                .mapToInt(s -> s.totalAvailableSpace - s.totalUsedSpace)
                .sum();
    }

    @Override
    public StorageUnit getMostAvailableSpaceUnit() {
        if (this.storage.isEmpty()) {
            throw new IllegalArgumentException("Storage is empty");
        }

        return this.mostAvailableSpaceUnit;
    }

    @Override
    public Collection<Box> getAllBoxesByVolume() {
        return this.boxes.values()
                .stream()
                .sorted((b1, b2) -> {
                    int box1Volume = b1.height * b1.depth * b1.width;
                    int box2Volume = b2.height * b2.depth * b2.width;

                    int result = Integer.compare(box1Volume, box2Volume);

                    if (result == 0) {
                        result = Integer.compare(b2.height, b1.height);
                    }

                    return result;
                }).collect(Collectors.toList());
    }

    @Override
    public Collection<StorageUnit> getAllUnitsByFillRate() {
        return this.storage.values()
                .stream()
                .sorted((s1, s2) -> {
                    int storage1Percentage = (s1.totalAvailableSpace - s1.totalUsedSpace) / s1.totalAvailableSpace;
                    int storage2Percentage = (s2.totalAvailableSpace - s2.totalUsedSpace) / s2.totalAvailableSpace;

                    int result = Integer.compare(storage2Percentage, storage1Percentage);

                    if (result == 0) {
                        result = Integer.compare(s2.totalAvailableSpace, s1.totalAvailableSpace);
                    }

                    return result;
                }).collect(Collectors.toList());
    }
}
