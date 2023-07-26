package com.timekeeping.smart.network;

import android.location.Location;

import com.timekeeping.smart.BaseApplication;
import com.timekeeping.smart.R;
import com.timekeeping.smart.entity.request.LocationRequest;
import com.timekeeping.smart.entity.response.CheckTimeKeepingResponse;
import com.timekeeping.smart.entity.response.LocationResponse;
import com.timekeeping.smart.utils.Constant;
import com.timekeeping.smart.utils.DeviceUtil;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class Connection_Set_Time_Keeping extends Connection_Base {
    public Connection_Set_Time_Keeping(LocationRequest locationRequest,
                                       DataCallback<CheckTimeKeepingResponse> callback) {
        super();
        this.locationRequest = locationRequest;
        this.callback = callback;
    }

    LocationRequest locationRequest;
    DataCallback<CheckTimeKeepingResponse> callback = null;
    CheckTimeKeepingResponse result = new CheckTimeKeepingResponse();

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
        List<LocationResponse> locationResponseList = new ArrayList<>();
        String insertStoreProc = "{call [sp_SelectLocation_AD](?)}";
        CallableStatement callableStatement = conn.prepareCall(insertStoreProc);
        callableStatement.setString(1, locationRequest.getMaNV());
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
            }
        }
        if (locationResponseList.size() > 0) {
//            Boolean isChecking = false;
            for (LocationResponse item : locationResponseList
            ) {
                if (checkDistance(locationRequest.getLatitude(), locationRequest.getLongitude(),
                        item.getVIDO() , item.getKINHDO(), item.getBANKINH())){
                    String insertStoreProc1 = "{call [sp_InsertTime_AD](?,?,?,?)}";
                    CallableStatement callableStatement1 = conn.prepareCall(insertStoreProc1);
                    callableStatement1.setString(1, locationRequest.getMaNV());
                    callableStatement1.setString(2, locationRequest.getAndroidId());
                    callableStatement1.setString(3, item.getMAVT());
                    callableStatement1.setString(4, "NO");
                    callableStatement1.registerOutParameter(4, Types.VARCHAR);
                    callableStatement1.execute();
//                    ResultSet reset1 = callableStatement1.getResultSet();
                    result.setResult(callableStatement1.getString(4));
//                    if (reset1 != null) {
//                        while (reset1.next()) {
//                            result.setResult(reset1.getString("ketqua"));
//                            result.setChecking(true);
//                            connectSuccess = true;
//                            isChecking = true;
//                        }
//                    }
                    break;
                }
            }
            if(result.getResult().equals(Constant.STORE_RESULT_OK)){
                result.setResult(BaseApplication.context.getString(R.string.str_ok_timekeeping));
                result.setChecking(true);
            }else if(result.getResult().equals("")) {
                result.setResult(BaseApplication.context.getString(R.string.str_out_off_timekeeping));
                result.setChecking(false);
            }else {
                result.setChecking(true);
            }
        }

        closeDatabase();
    }

    private Boolean checkDistance(Double startLatitude, Double startLongitude, Double endLatitude,
                                  Double endLongitude, Integer banKinh) {
        Location selected_location = new Location("locationA");
        selected_location.setLatitude(startLatitude);
        selected_location.setLongitude(startLongitude);
        Location near_locations = new Location("locationB");
        near_locations.setLatitude(endLatitude);
        near_locations.setLongitude(endLongitude);
        float distance = selected_location.distanceTo(near_locations);
        return ((int) distance) < banKinh;
    }
}
