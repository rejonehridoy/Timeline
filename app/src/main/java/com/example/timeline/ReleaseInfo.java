package com.example.timeline;

public class ReleaseInfo {
    private String id,bug,description,downloadLink,releaseDate,version,versionType;

    public ReleaseInfo() {
    }

    public ReleaseInfo(String id, String bug, String description, String downloadLink, String releaseDate, String version, String versionType) {
        this.id = id;
        this.bug = bug;
        this.description = description;
        this.downloadLink = downloadLink;
        this.releaseDate = releaseDate;
        this.version = version;
        this.versionType = versionType;
    }

    public String getId() {
        return id;
    }

    public String getBug() {
        return bug;
    }

    public String getDescription() {
        return description;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getVersion() {
        return version;
    }

    public String getVersionType() {
        return versionType;
    }
}
