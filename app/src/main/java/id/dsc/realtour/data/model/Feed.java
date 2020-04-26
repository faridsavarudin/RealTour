package id.dsc.realtour.data.model;

public class Feed {
    private String caption;
    private String companyLogo;
    private String companyName;
    private String containerType;
    private String mediaValue;
    private String title;
    private String price;
    private String companyID;

    public Feed(String caption, String companyLogo, String companyName, String containerType, String mediaValue, String title, String price, String companyID) {
        this.caption = caption;
        this.companyLogo = companyLogo;
        this.companyName = companyName;
        this.containerType = containerType;
        this.mediaValue = mediaValue;
        this.title = title;
        this.price = price;
        this.companyID = companyID;
    }

    public Feed() {
    }

    public String getCaption() {
        return caption;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }


    public String getMediaValue() {
        return mediaValue;
    }

    public void setMediaValue(String mediaValue) {
        this.mediaValue = mediaValue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
