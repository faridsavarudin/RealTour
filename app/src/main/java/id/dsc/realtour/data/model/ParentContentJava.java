package id.dsc.realtour.data.model;


import android.os.Parcel;
import android.os.Parcelable;

public class ParentContentJava implements Parcelable {
    private String name;
    private String url;

    public ParentContentJava(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public ParentContentJava() {
    }

    protected ParentContentJava(Parcel in) {
        name = in.readString();
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParentContentJava> CREATOR = new Creator<ParentContentJava>() {
        @Override
        public ParentContentJava createFromParcel(Parcel in) {
            return new ParentContentJava(in);
        }

        @Override
        public ParentContentJava[] newArray(int size) {
            return new ParentContentJava[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}


