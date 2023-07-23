package com.timekeeping.smart.network;

import com.timekeeping.smart.entity.request.LocationRequest;
import com.timekeeping.smart.entity.response.LocationResponse;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Connection_Set_Location extends Connection_Base {
    public Connection_Set_Location(LocationRequest locationRequest,
                                   DataCallback<String> callback) {
        super();
        this.locationRequest = locationRequest;
        this.callback = callback;
    }

    LocationRequest locationRequest;
    DataCallback<String> callback = null;
    String result = "";

    @Override
    protected void connectionFinish() {
        // TODO Auto-generated method stub
        super.connectionFinish();
        if (connectSuccess) {
            connectSuccessAction();
        } else {
            connectFailAction();
        }
    }

    @Override
    public void connectFailAction() {
        callback.onConnectFail(null);
    }

    @Override
    public void connectSuccessAction() {
        callback.onConnectSuccess(result);
    }

    @Override
    public void callStoreProcedure(Connection conn) throws SQLException {
        // TODO Auto-generated method stub
        super.callStoreProcedure(conn);
        String insertStoreProc = "{call [sp_InsertLocation_AD](?,?,?,?)}";
        CallableStatement callableStatement = conn.prepareCall(insertStoreProc);
        callableStatement.setString(1, locationRequest.getNameLocation());
        callableStatement.setString(2, locationRequest.getAndroidId());
        callableStatement.setString(3,  String.valueOf(+locationRequest.getLongitude()));
        callableStatement.setString(4, String.valueOf(locationRequest.getLatitude()));
        callableStatement.execute();
        ResultSet reset = callableStatement.getResultSet();
        if (reset != null) {
            while (reset.next()) {
                result = reset.getString("ketqua");
                connectSuccess = true;
            }
        }
        closeDatabase();
    }
}
