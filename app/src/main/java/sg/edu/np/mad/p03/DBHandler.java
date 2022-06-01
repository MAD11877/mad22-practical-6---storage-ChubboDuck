package sg.edu.np.mad.p03;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    static ArrayList<User> userList;

    public DBHandler(Context c) {
        super(c, "users.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE User(Username Text, Descr Text, ID Integer Primary Key AutoIncrement, Followed TinyInt)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS User");
        onCreate(db);
    }


    public ArrayList<User> getUsers(){
        String query = "SELECT * FROM User";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        userList = new ArrayList<User>();

        while (cursor.moveToNext()){
            Boolean followValue = (cursor.getInt(3) == 1 ? true : false );
            User user = new User(cursor.getString(0), cursor.getString(1),
                    cursor.getInt(2), followValue);
            userList.add(user);
        }

        cursor.close();
        db.close();
        return userList;
    }

    public void updateUser(User user){
        Integer followValue = (user.Followed ? 1 : 0);
        String query = "UPDATE User SET Followed = " + followValue + " WHERE ID = " + user.Id;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    public void insertUser(User user){ // just used to populate the db in the first place (not used unless i messed up and have to restart)
        SQLiteDatabase db = this.getWritableDatabase();
        int followValue = 0;
        if (user.Followed) {
            followValue = 1;
        }
        db.execSQL("INSERT INTO User VALUES(\"" + user.Name + "\",\""  + user.Description + "\"," +
                " \"" + user.Id + "\", " + followValue + ")");
        db.close();
    }
}
