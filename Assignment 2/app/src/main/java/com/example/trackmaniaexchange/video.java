package com.example.trackmaniaexchange;

public class video {
    private String videoTitle;
    private String channelName;
    private String videoId;
    private String thumbnailURL;

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public video(String videoTitle, String channelName, String videoId, String thumbnailURL) {
        this.videoTitle = videoTitle;
        this.channelName = channelName;
        this.videoId = videoId;
        this.thumbnailURL = thumbnailURL;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    @Override
    public String toString() {
        return "video{" +
                "videoTitle='" + videoTitle + '\'' +
                ", channelName='" + channelName + '\'' +
                ", videoId='" + videoId + '\'' +
                ", thumbnailURL='" + thumbnailURL + '\'' +
                '}';
    }
}
