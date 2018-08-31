package mis.kuas.cnn;

import java.io.Serializable;

/**
 * Created by mingjia on 2016/10/6.
 */
public class StanceType implements Serializable {

    private String code;
    private String name;
    private String description;
    private int imageId;

    public StanceType(String code, String name, int imageId) {
        this(code, name);
        this.setImageId(imageId);
    }

    public StanceType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return this.code + " " + this.name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public boolean equals(StanceType type) {
        return type.getCode().equals(this.code);
    }
}
