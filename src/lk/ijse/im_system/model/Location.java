package lk.ijse.im_system.model;

public class Location {

    private String  locationId;
    private String locationName;
    private String createdBy;

    public Location() {}

    public Location(String locationId, String locationName, String createdBy) {
        this.setLocationId(locationId);
        this.setLocationName(locationName);
        this.setCreatedBy(createdBy);
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "Location{" +
                "locationId='" + locationId + '\'' +
                ", locationName='" + locationName + '\'' +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}
