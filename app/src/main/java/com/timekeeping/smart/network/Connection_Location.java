package com.timekeeping.smart.network;

import com.timekeeping.smart.entity.response.LocationResponse;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Connection_Location extends Connection_Base {
    public Connection_Location(String maNV,
                               DataCallback<List<LocationResponse>> callback) {
        super();
        this.maNV = maNV;
        this.callback = callback;
    }

    String maNV;
    DataCallback<List<LocationResponse>> callback = null;
    List<LocationResponse> locationResponseList = new ArrayList<>();

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
        callback.onConnectSuccess(locationResponseList);
    }

    @Override
    public void callStoreProcedure(Connection conn) throws SQLException {
        // TODO Auto-generated method stub
        super.callStoreProcedure(conn);
        String insertStoreProc = "{call [sp_SelectLocation_AD](?)}";
        CallableStatement callableStatement = conn.prepareCall(insertStoreProc);
        callableStatement.setString(1, maNV);
        callableStatement.execute();
        ResultSet reset = callableStatement.getResultSet();
        if (reset != null) {
            while (reset.next()) {
                LocationResponse response = new LocationResponse();
                response.setMAVT(reset.getString("MAVT"));
                response.setTENVT(reset.getString("TENVT"));
                response.setKINHDO(reset.getDouble("KINHDO"));
                response.setVIDO(reset.getDouble("VIDO"));
                response.setGHICHU(reset.getString("GHICHU"));
                response.setBANKINH(reset.getInt("BANKINH"));
                locationResponseList.add(response);
                connectSuccess = true;
            }
        }
        closeDatabase();
    }
}
