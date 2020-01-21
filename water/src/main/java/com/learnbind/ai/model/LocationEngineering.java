package com.learnbind.ai.model;

import javax.persistence.*;

@Table(name = "LOCATION_ENGINEERING")
public class LocationEngineering {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column(name = "LOCATION_ID")
    private Long locationId;

    @Column(name = "LOCATION_TRACE_IDS")
    private String locationTraceIds;

    @Column(name = "ENGINEERING_ID")
    private Long engineeringId;

    /**
     * @return ID
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return LOCATION_ID
     */
    public Long getLocationId() {
        return locationId;
    }

    /**
     * @param locationId
     */
    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    /**
     * @return LOCATION_TRACE_IDS
     */
    public String getLocationTraceIds() {
        return locationTraceIds;
    }

    /**
     * @param locationTraceIds
     */
    public void setLocationTraceIds(String locationTraceIds) {
        this.locationTraceIds = locationTraceIds == null ? null : locationTraceIds.trim();
    }

    /**
     * @return ENGINEERING_ID
     */
    public Long getEngineeringId() {
        return engineeringId;
    }

    /**
     * @param engineeringId
     */
    public void setEngineeringId(Long engineeringId) {
        this.engineeringId = engineeringId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", locationId=").append(locationId);
        sb.append(", locationTraceIds=").append(locationTraceIds);
        sb.append(", engineeringId=").append(engineeringId);
        sb.append("]");
        return sb.toString();
    }
}