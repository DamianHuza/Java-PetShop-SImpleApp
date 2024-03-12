package entities;

public class User {

    public int id;
    public String name;
    public String password;
    public String email;
    public boolean admin;

    //region GET & SET

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return admin;
    }
    //endregion
}

