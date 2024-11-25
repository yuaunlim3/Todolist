package VTTP_ssf.practice2.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class Users {

    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 2, max = 32, message = "Name must between 2 to 32 characters")
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 2, max = 32, message = "Password must between 2 to 32 characters")
    private String password;
    private String id;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
