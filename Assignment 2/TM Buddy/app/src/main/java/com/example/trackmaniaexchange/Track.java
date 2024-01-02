package com.example.trackmaniaexchange;

public class Track {
    private int trackID;
    private String mapName;
    private String authorName;

    public Track(int trackID, String mapName, String authorName) {
        this.trackID = trackID;
        this.mapName = mapName;
        this.authorName = authorName;
    }

    public int getTrackID() {
        return trackID;
    }

    public void setTrackID(int trackID) {
        this.trackID = trackID;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @Override
    public String toString() {
        return "Track{" +
                "trackID=" + trackID +
                ", mapName='" + mapName + '\'' +
                ", authorName='" + authorName + '\'' +
                '}';
    }
}
