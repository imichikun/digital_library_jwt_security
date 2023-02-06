package jwt2.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class PersonDTO {

    @NotEmpty(message = "Username should not be empty")
    @Size(min = 4, max = 10, message = "Username should be not less than 4 and greater than 10 symbols")
    private String username;

    @NotEmpty(message = "Password should not be empty")
    @Size(min = 4, max = 10, message = "Username should be not less than 4 and greater than 10 symbols")
    private String password;

    public PersonDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public PersonDTO() {
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}