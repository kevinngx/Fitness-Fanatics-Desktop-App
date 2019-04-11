package sample;

import java.time.LocalDate;

public class User {

    private int id;
    private String email;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String gender;
    private double bodyFatPercentage;
    private double weight;
    private double height;
    private long dateOfBirth;

    public User(int id, String email, String username, String password, String firstName, String lastName, String gender, double bodyFatPercentage, double weight, double height, long dateOfBirth) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.bodyFatPercentage = bodyFatPercentage;
        this.weight = weight;
        this.height = height;
        this.dateOfBirth = dateOfBirth;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getBodyFatPercentage() {
        return bodyFatPercentage;
    }

    public void setBodyFatPercentage(double bodyFatPercentage) {
        this.bodyFatPercentage = bodyFatPercentage;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }


    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", bodyFatPercentage=" + bodyFatPercentage +
                ", weight=" + weight +
                ", height=" + height +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }

    public double calculateBasalMetabolicRate() {
        double age = ((double) SessionDataHolder.getDateRequested() -  (double) dateOfBirth) / 10000.0;
        return 655 + (9.6 * weight) + (1.8 * height) + (4.7 * age);
    }
}
