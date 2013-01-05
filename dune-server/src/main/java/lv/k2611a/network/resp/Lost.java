package lv.k2611a.network.resp;

public class Lost implements Response {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
