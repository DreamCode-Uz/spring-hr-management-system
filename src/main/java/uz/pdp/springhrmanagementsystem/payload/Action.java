package uz.pdp.springhrmanagementsystem.payload;

public class Action {
    public static final String BATH_URL = "http://localhost:8080/api";
    public static final String SENDING_VERIFY_USER_URL = "http://localhost:8080/api/auth/verify?email=%s&emailCode=%s";
    public static final String SENDING_VERIFY_TASK_URL = "http://localhost:8080/api/task/activated/";
}
