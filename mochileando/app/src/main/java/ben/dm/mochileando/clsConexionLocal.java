package ben.dm.mochileando;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Benito on 20/08/2015.
 */
public class clsConexionLocal extends SQLiteOpenHelper {

    clsConexionLocal(Context context, String nombre, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, nombre, factory, version);
    }
    //creamos la tabla
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table cliente(codigo integer primary key)");
    }

    //borrar la tabla y crear la nueva tabla
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnte, int versionNue) {
        db.execSQL("drop table if exists cliente");
        db.execSQL("create table cliente(codigo integer primary key)");
    }
}
