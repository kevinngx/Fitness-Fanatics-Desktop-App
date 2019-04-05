package sample;

public class SessionDataHolder {

    public static User user;
    public static String currentPage;
    public static String previousPage;

    public static User getUser() {
        return user;
    }

    public static void setUser(User inputUser) {
        user = inputUser;
    }
}
