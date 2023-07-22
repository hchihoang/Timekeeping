package com.timekeeping.smart.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.timekeeping.smart.BuildConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class Connection_Base {
    private static final String Tag = "Connection_Base";
    private String connectionString;
    private String user;
    private String pass;
    private String ip;
    private String port;
    private String database;
    protected boolean connectSuccess;
    protected boolean searchingIsDone;
    protected boolean networkIsError;

    //    protected String tableName;
    private ConnectToServer connectToServer;
    private Connection conn = null;

    public Connection_Base() {
        connectSuccess = true;
        searchingIsDone = false;
        networkIsError = false;
        ip = BuildConfig.BASE_URL;
        port = BuildConfig.BASE_PORT;
        user = BuildConfig.BASE_USER;
        pass = BuildConfig.BASE_PASSWORK;
        database = BuildConfig.BASE_DATABASE;
        connectionString = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + database + ";user=" + user + ";password=" + pass;
        connectToServer = new ConnectToServer();
    }

    /*
    Connect to server, return Result Set after select table: tableName
     */
    private void connection() {
        try {
            String driver = "net.sourceforge.jtds.jdbc.Driver";
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(connectionString, user, pass);
            callStoreProcedure(conn);
        } catch (SQLException e) {
            Log.w(Tag, e);
            connectSuccess = false;
        } catch (Exception e) {
            Log.w(Tag, e);
            connectSuccess = false;
        }
    }

    public void callStoreProcedure(Connection conn2) throws SQLException {
    }

//	protected abstract void getDataInTable(Statement stmt) throws SQLException;

    public abstract void connectFailAction();

    public abstract void connectSuccessAction();


    private class ConnectToServer extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (isWifiConnected()) {
                connection();
            } else {
                networkIsError = true;
                connectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void searchingIsDone) {
            connectionFinish();
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    protected void connectionFinish() {
    }

    public void closeDatabase() {
        CloseConnection closeConnection = new CloseConnection(conn);
        closeConnection.execute();
    }

    public void execute() {
        connectToServer.execute();
    }

    public void cancel() {
        connectToServer.cancel(true);
    }

    private class CloseConnection extends AsyncTask<Void, Void, Void> {

        public Connection conn;

        public CloseConnection(Connection conn) {
            this.conn = conn;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if ((conn != null) && (!conn.isClosed())) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                Log.w(Tag, e);
            }
            return null;
        }
    }

    private boolean isWifiConnected() {
        return true;
    }

}
