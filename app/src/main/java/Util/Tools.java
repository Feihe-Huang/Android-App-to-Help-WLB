package Util;

import java.util.ArrayList;

public class Tools {
    public static final String TABLE_NAME = "Users";


    //Table Columns
    public static final String user_id = "id";
    public static final String username = "userName";
    public static final String password = "password";
    public static final String email = "email";


    public static ArrayList<String[]> userData =new ArrayList<>();
    public static ArrayList<String> categoryData = new ArrayList<String>();
    public static ArrayList<String[]> avoidData =new ArrayList<>();
    public static ArrayList<String[]> doneData =new ArrayList<>();
    public static ArrayList<String[]> taskData =new ArrayList<>();
    public static Boolean isTodaySelected=true;
}
