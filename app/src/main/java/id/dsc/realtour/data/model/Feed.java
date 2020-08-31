package id.dsc.realtour.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


public class Feed implements Parcelable {
    private String caption;
    private List<ParentContentJava> parentContent;
    private String companyLogo;
    private String companyName;
    private String containerType;
    private String mediaValue;
    private String price;
    private String title;
    private String companyID;



    public Feed(String caption, List<id.dsc.realtour.data.model.ParentContentJava> ParentContent,
                String companyLogo, String companyName, String containerType, String mediaValue, String price, String companyID, String title) {
        this.caption = caption;
        this.parentContent = ParentContent;
        this.companyLogo = companyLogo;
        this.companyName = companyName;
        this.containerType = containerType;
        this.mediaValue = mediaValue;
        this.price = price;
        this.companyID = companyID;
        this.title = title;
    }

    public Feed() {
    }

    protected Feed(Parcel in) {
        caption = in.readString();
        companyLogo = in.readString();
        companyName = in.readString();
        containerType = in.readString();
        mediaValue = in.readString();
        price = in.readString();
        companyID = in.readString();
        title = in.readString();
        parentContent = in.createTypedArrayList(ParentContentJava.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(caption);
        dest.writeString(companyLogo);
        dest.writeString(companyName);
        dest.writeString(containerType);
        dest.writeString(mediaValue);
        dest.writeString(price);
        dest.writeString(companyID);
        dest.writeString(title);
        dest.writeTypedList(parentContent);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Feed> CREATOR = new Creator<Feed>() {
        @Override
        public Feed createFromParcel(Parcel in) {
            return new Feed(in);
        }

        @Override
        public Feed[] newArray(int size) {
            return new Feed[size];
        }
    };

    public List<id.dsc.realtour.data.model.ParentContentJava> getParentContent() {
        return parentContent;
    }

    public void setParentContent(List<id.dsc.realtour.data.model.ParentContentJava> parentContent) {
        this.parentContent = parentContent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

}
