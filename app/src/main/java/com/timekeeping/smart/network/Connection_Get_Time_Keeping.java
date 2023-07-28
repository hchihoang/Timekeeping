package com.timekeeping.smart.network;

import com.timekeeping.smart.entity.request.DateRequest;
import com.timekeeping.smart.entity.response.LocationResponse;
import com.timekeeping.smart.entity.response.TimeKeepingResponse;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Connection_Get_Time_Keeping extends Connection_Base {
    public Connection_Get_Time_Keeping(DateRequest dateRequest,
                                       DataCallback<List<TimeKeepingResponse>> callback) {
        super();
        this.dateRequest = dateRequest;
        this.callback = callback;
    }

    DateRequest dateRequest;
    DataCallback<List<TimeKeepingResponse>> callback = null;
    List<TimeKeepingResponse> locationResponseList = new ArrayList<>();

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
        String insertStoreProc = "{call [sp_SelectTime_AD](?,?,?)}";
        CallableStatement callableStatement = conn.prepareCall(insertStoreProc);
        callableStatement.setString(1, dateRequest.getMaNV());
        callableStatement.setString(2, dateRequest.getStartDate());
        callableStatement.setString(3, dateRequest.getEndDate());
        callableStatement.execute();
        ResultSet reset = callableStatement.getResultSet();
        if (reset != null) {
            while (reset.next()) {
                TimeKeepingResponse response = new TimeKeepingResponse();
                response.setID_NV(reset.getString("ID_NV"));
                response.setMAVT(reset.getString("MAVT"));
                response.setTRX_DATE(reset.getString("TRX_DATE"));
                response.setGIOCHAM(reset.getString("GIOCHAM"));
                response.setTHIETBI(reset.getString("THIETBI"));
                response.setKINHDO(reset.getDouble("kd"));
                response.setVIDO(reset.getDouble("vd"));
                locationResponseList.add(response);
                connectSuccess = true;
            }
        }

        closeDatabase();
    }
}
