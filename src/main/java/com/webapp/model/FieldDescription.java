package com.webapp.model;

@SuppressWarnings("unused")
public class FieldDescription {

    private String name;
    private String rusName;
    private String width;
    private String type;
    private boolean isHidden;

    private boolean isTechnical;

    public FieldDescription(String name, String rusName, String type, String width, boolean isHidden, boolean isTechnical) {
        this.name = name;
        this.rusName = rusName;
        this.width = width;
        this.isHidden = isHidden;
        this.isTechnical = isTechnical;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRusName() {
        return rusName;
    }

    public void setRusName(String rusName) {
        this.rusName = rusName;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public boolean isTechnical() {
        return isTechnical;
    }

    public void setTechnical(boolean technical) {
        isTechnical = technical;
    }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

}
