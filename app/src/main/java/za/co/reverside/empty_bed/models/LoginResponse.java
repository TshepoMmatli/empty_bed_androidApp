package za.co.reverside.empty_bed.models;


import lombok.Data;

@Data
public class LoginResponse {

    private String name, token, type;

}
