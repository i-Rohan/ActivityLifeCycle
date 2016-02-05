package com.blank.rohansharma.activitylifecycle;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity
{
    String tag= "LifeCycleEvents",database="Logcat",table="Log";
    SQLiteDatabase Database;
    EditText add;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add=(EditText)findViewById(R.id.editText);
        tv=(TextView)findViewById(R.id.text);
        Database=openOrCreateDatabase(database, Context.MODE_ENABLE_WRITE_AHEAD_LOGGING,null);
        Database.execSQL("CREATE TABLE IF NOT EXISTS " + table + "(log TEXT);");
        Toast toast = Toast.makeText(MainActivity.this,"onCreate", Toast.LENGTH_SHORT);
        toast.show();
        Log.d(tag, "onCreate()");
        tv.append("D/LifeCycleEvents: onCreate()\n");
        Database.execSQL("INSERT INTO " + table + " VALUES('" + "D/LifeCycleEvents: onCreate()" + "');");
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Toast toast = Toast.makeText(MainActivity.this,"onStart",Toast.LENGTH_SHORT);
        toast.show();
        Log.d(tag, "onStart()");
        tv.append("D/LifeCycleEvents: onStart()\n");
        Database.execSQL("INSERT INTO " + table + " VALUES('" + "D/LifeCycleEvents: onStart()"+"');");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Toast toast = Toast.makeText(MainActivity.this,"onResume",Toast.LENGTH_SHORT);
        toast.show();
        Log.d(tag, "onResume()");
        tv.append("D/LifeCycleEvents: onResume()\n");
        Database.execSQL("INSERT INTO " + table + " VALUES('" + "D/LifeCycleEvents: onResume()" + "');");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Toast toast = Toast.makeText(MainActivity.this,"onPause",Toast.LENGTH_SHORT);
        toast.show();
        Log.d(tag, "onPause()");
        tv.append("D/LifeCycleEvents: onPause()\n");
        Database.execSQL("INSERT INTO " + table + " VALUES('" + "D/LifeCycleEvents: onPause()"+"');");
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        Toast toast = Toast.makeText(MainActivity.this,"onRestart",Toast.LENGTH_SHORT);
        toast.show();
        Log.d(tag, "onRestart()");
        tv.append("D/LifeCycleEvents: onRestart()\n");
        Database.execSQL("INSERT INTO " + table + " VALUES('" + "D/LifeCycleEvents: onRestart()" + "');");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Toast toast = Toast.makeText(MainActivity.this,"onStop",Toast.LENGTH_SHORT);
        toast.show();
        Log.d(tag, "onStop()");
        tv.append("D/LifeCycleEvents: onStop()\n");
        Database.execSQL("INSERT INTO " + table + " VALUES('" + "D/LifeCycleEvents: onStop()" + "')");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Toast toast = Toast.makeText(MainActivity.this, "onDestroy", Toast.LENGTH_SHORT);
        toast.show();
        Log.d(tag, "onDestroy()");
        tv.append("D/LifeCycleEvents: onDestroy()\n");
        Database.execSQL("INSERT INTO " + table + " VALUES('" + "D/LifeCycleEvents: onDestroy()" + "')");
    }

    public void onClickAppend(View v)
    {
        Database.execSQL("INSERT INTO " + table + " VALUES('"+"\n" +add.getText() + "\n" + "')");
        Log.d(tag,"\n"+add.getText().toString()+"\n");
        tv.append("\n\n"+add.getText()+"\n\n");
        add.setText("");
    }

    public void onClickView(View v)
    {
        Cursor c=Database.rawQuery("SELECT * FROM " + table, null);
        StringBuffer buffer=new StringBuffer();
        while(c.moveToNext())
            buffer.append(c.getString(0)+"\n");
        showMessage("Logcat", buffer.toString());
    }

    public void onClickDel(View v)
    {
        Cursor c=Database.rawQuery("SELECT * FROM " + table, null);
        if(c.getCount()==0)
        {
            Toast toast = Toast.makeText(this, "Error! Underflow", Toast.LENGTH_SHORT);
            toast.show();
        }
        if(c.moveToFirst())
        {
            Database.execSQL("DELETE FROM "+table);
            Toast toast = Toast.makeText(this, "Successfully Deleted All!", Toast.LENGTH_SHORT);
            toast.show();
        }
        add.setText("");
        tv.setText("");
    }

    public void onClickSave(View v)
    {
        File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS + "/Activity Life Cycle");
        File data = Environment.getDataDirectory();
        if(!sd.exists())
            sd.mkdir();
        File logcat = new File(sd,"Logcat.csv");
        try
        {
            logcat.createNewFile();
            FileOutputStream fOut = new FileOutputStream(logcat);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            Database = this.openOrCreateDatabase(database,MODE_PRIVATE, null);
            Cursor c = Database.rawQuery("SELECT * FROM "+table, null);
            if(c!= null)
            {
                if (c.moveToFirst())
                {
                    while(c.moveToNext())
                    {
                        String Log = c.getString(0);
                        myOutWriter.append(Log);
                        myOutWriter.append("\n");
                    }
                }
                c.close();
                myOutWriter.close();
                fOut.close();
            }
            showMessage("Database Exported to CSV","Export location:\n/Documents/Activity Life Cycle/Logcat.csv");
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}