package storageService.models;

public class StorageUnit {
    public String id;

    public int totalAvailableSpace;

    public int totalUsedSpace;

    public StorageUnit(String id, int totalAvailableSpace, int totalUsedSpace) {
        this.id = id;
        this.totalAvailableSpace = totalAvailableSpace;
        this.totalUsedSpace = totalUsedSpace;
    }
}
