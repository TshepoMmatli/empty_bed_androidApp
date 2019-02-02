package za.co.reverside.empty_bed.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Account {

    private String name;
    private String type;
    private String token;

    public Account() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
