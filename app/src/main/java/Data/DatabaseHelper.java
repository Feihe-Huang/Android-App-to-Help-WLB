package Data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import Model.User;
import Util.Tools;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;

    public DatabaseHelper(@Nullable Context context) {
        super(context, "project1.db", null, 4);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // 执行sql语句，创建用户table
        db.execSQL("CREATE TABLE Users ( id INTEGER PRIMARY KEY, userName TEXT, password TEXT, email TEXT UNIQUE)");
        db.execSQL("CREATE TABLE Tasks (id INTEGER PRIMARY KEY, Date TEXT, Task TEXT, Details TEXT, Category TEXT);");
        db.execSQL("CREATE TABLE Avoid (id INTEGER PRIMARY KEY, Date TEXT, Task TEXT, Details TEXT, Category TEXT);");
        db.execSQL("CREATE TABLE Done (id INTEGER PRIMARY KEY, Date TEXT, Task TEXT, Details TEXT, Category TEXT);");
        db.execSQL("CREATE TABLE Category (Category TEXT);");
        db.execSQL("CREATE TABLE Notes ( id INTEGER PRIMARY KEY AUTOINCREMENT, Content TEXT NOT NULL, Time TEXT NOT NULL);");
    }

    // 从数据库中拿到所有的labels，并一个个存入categoryData array中
    public void getCategories() {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM Category", null);
        Tools.categoryData = new ArrayList<>();
        while (cursor.moveToNext()) {
            Tools.categoryData.add(cursor.getString(0));
        }
    }

    // 添加一个category到数据库
    public void addCategory(String label) {
        ContentValues con = new ContentValues();
        con.put("Category", label);
        this.getWritableDatabase().insertOrThrow("Category", "", con);

    }

    // 从数据库删除一个category
    public void deleteCategory(String category) {
        this.getWritableDatabase().delete("Category", "Category='" + category + "'", null);
    }

    // 得到数据库中现有的category的数量
    public int getCategoriesAmount(String category){
        int count=0;
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM Tasks WHERE Category='" + category + "'", null);
        count= cursor.getCount();
        cursor.close();
        return  count;
    }

    // 从数据库中拿到所有task的信息，并分类处理
    public void getTasks() {
        // 拿信息
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM Tasks", null);
        Tools.taskData = new ArrayList<>();
        while (cursor.moveToNext()) {
            // 五种信息，分别存入五个string的列表中
            String[] temp = new String[5];
            temp[0] = (cursor.getString(0)); // ID
            temp[1] = (cursor.getString(1)); // date
            temp[2] = (cursor.getString(2)); // task
            temp[3] = (cursor.getString(3)); // detail
            temp[4] = (cursor.getString(4)); // category
            Tools.taskData.add(temp);
        }
    }

    // 添加一个任务到数据库中选定的table
    public void addDataToTable(String table, int id, String date, String task, String detail, String category) {
        ContentValues con = new ContentValues();
        con.put("id", id);
        con.put("Date", date);
        con.put("Task", task);
        con.put("Details", detail);
        con.put("Category", category);
        this.getWritableDatabase().insertOrThrow( table, null, con);
    }

    // 从数据库中删除一个task
    public void deleteTask(int id) {
        String getTaskName="";
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM Tasks WHERE id ='"+id+"'", null);
        if (cursor.moveToFirst()) {
            getTaskName=(cursor.getString(2));
        }
        this.getWritableDatabase().delete("Tasks", "id='" + id + "'", null);
        this.getWritableDatabase().delete("Avoid", "Task='" + getTaskName + "'", null);
        this.getWritableDatabase().delete("Done", "Task='" + getTaskName+ "'", null);
    }

    // 在删除一个task之后更新ID
    public void updateTaskId(int i) {
        int j = i - 1;
        this.getWritableDatabase().execSQL("UPDATE Tasks SET id='" + j + "' WHERE id='" + i + "'");
    }

    // 从数据库中拿到所有avoid task的信息，并分类处理
    public void getAvoidedTasks() {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM Avoid", null);
        Tools.avoidData = new ArrayList<>();
        while (cursor.moveToNext()) {
            String[] temp = new String[5];
            temp[0] = (cursor.getString(0));
            temp[1] = (cursor.getString(1));
            temp[2] = (cursor.getString(2));
            temp[3] = (cursor.getString(3));
            temp[4] = (cursor.getString(4));
            Tools.avoidData.add(temp);
        }
    }

    // 从数据库中拿到所有done task的信息，并分类处理
    public void getDoneTasks() {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM DONE", null);
        Tools.doneData = new ArrayList<>();
        while (cursor.moveToNext()) {
            String[] temp = new String[5];
            temp[0] = (cursor.getString(0));
            temp[1] = (cursor.getString(1));
            temp[2] = (cursor.getString(2));
            temp[3] = (cursor.getString(3));
            temp[4] = (cursor.getString(4));
            Tools.doneData.add(temp);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS Users");
//        onCreate(db);
    }

    public void show_user(int uid) {
        // 拿信息
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM Users WHERE id =" + uid, null);
        Tools.userData = new ArrayList<>();
        while (cursor.moveToNext()) {
            // 3种信息，分别存入3个string的列表中
            String[] temp = new String[3];
            temp[0] = (cursor.getString(1)); // username
            temp[1] = (cursor.getString(2)); // password
            temp[2] = (cursor.getString(3)); // email
            Tools.userData.add(temp);
        }
    }

    // 实现增删改查几种操作
    // 在数据库中增加一名用户
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Tools.username, user.getUserName());
        values.put(Tools.password, user.getPassword());
        values.put(Tools.email, user.getEmail());

        long res = db.insert("Users", null, values);
        Log.d("Saved!", "saved to DB");
        db.close();
        return res;

    }


    //更改数据库中用户的个人信息
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Tools.username, user.getUserName());
        values.put(Tools.password, user.getPassword());
        values.put(Tools.email, user.getEmail());
        return db.update( "Users", values, Tools.user_id + "=?", new String[]{String.valueOf(user.getId())});
    }

    // 得到数据库中用户数据表中的数据数量
    public int getUsersCount() {
        String countQuery = "SELECT * FROM Users";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }

    // 判断用户账号密码是否正确且匹配
    public Boolean checkUser(String email, String password) {
        String[] columns = {Tools.user_id};
        SQLiteDatabase db = getReadableDatabase();
        String selection = Tools.email + "=?" + " and " + Tools.password + "=?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query("Users", columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count > 0)
            return true;
        else
            return false;
    }

    @SuppressLint("Range")
    public String selectOneUserSendUserName(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM Users WHERE " + Tools.email + " = '"+email.trim()+"'" +" and "+ Tools.password + " = '"+password.trim()+"'" , null);
        c.moveToFirst();

        int x = 0 ;
        String y = "";
        while (c != null) {
            x = Integer.parseInt(c.getString(c.getColumnIndex(Tools.user_id)));
            y = c.getString(c.getColumnIndex(Tools.username));
            Log.d("tagOneUser", Integer.toString(x) );
            Log.d("tagOneUser", y );
            break;
        }
        c.moveToNext();
        return y;
    }

    @SuppressLint("Range")
    public int selectOneUserSendId(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM Users WHERE " + Tools.email + " = '"+email.trim()+"'" +" and "+ Tools.password + " = '"+password.trim()+"'" , null);
        c.moveToFirst();

        int x = 0 ;
        String y = "";
        while (c != null) {
            x = Integer.parseInt(c.getString(c.getColumnIndex(Tools.user_id)));
            y = c.getString(c.getColumnIndex(Tools.username));
            Log.d("tagOneUser", Integer.toString(x) );
            Log.d("tagOneUser", y );
            break;
        }
        c.moveToNext();
        return x;
    }


}

