package com.timekeeping.smart.network;

import com.timekeeping.smart.entity.request.DateRequest;
import com.timekeeping.smart.entity.response.ProgressResponse;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Connection_Progress extends Connection_Base {
    public Connection_Progress(DateRequest dateRequest,
                               DataCallback<List<ProgressResponse>> callback) {
        super();
        this.dateRequest = dateRequest;
        this.callback = callback;
    }
    DateRequest dateRequest;
    DataCallback<List<ProgressResponse>> callback = null;
    List<ProgressResponse> progressResponseList = new ArrayList<>();
    @Override
    protected void connectionFinish() {
        // TODO Auto-generated method stub
        super.connectionFinish();
        if (connectSuccess){
            connectSuccessAction();
        }else{
            connectFailAction();
        }
    }

    @Override
    public void connectFailAction() {
        callback.onConnectFail(null);
    }

    @Override
    public void connectSuccessAction() {
        callback.onConnectSuccess(progressResponseList);
    }

    @Override
    public void callStoreProcedure(Connection conn) throws SQLException{
        // TODO Auto-generated method stub
        super.callStoreProcedure(conn);
        String insertStoreProc = "{call [sp_sysTiendoSX_Chart_AD1](?,?,?)}";
        CallableStatement callableStatement = conn.prepareCall(insertStoreProc);
        callableStatement.setString(1, dateRequest.getStartDate());
        callableStatement.setString(2, dateRequest.getEndDate());
        callableStatement.setString(3, dateRequest.getMaNV());
        callableStatement.execute();
        ResultSet reset = callableStatement.getResultSet();
        if (reset != null){
            while(reset.next()){
                ProgressResponse response = new ProgressResponse();
                response.setDUAN(reset.getString("DUAN"));
                response.setID_MAY(reset.getString("ID_MAY"));
                response.setID_NV(reset.getString("ID_NV"));
                response.setID(reset.getString("ID"));
                response.setNAME(reset.getString("NAME"));
                response.setTNGAY(reset.getString("TNGAY"));
                response.setNHOM(reset.getString("NHOM"));
                response.setTEN_NHOM(reset.getString("TEN_NHOM"));
                response.setGHICHU(reset.getString("GHICHU"));
                response.setSTART_DATE(reset.getString("START_DATE"));
                response.setEND_DATE(reset.getString("END_DATE"));
                response.setTIENDO(reset.getString("TIENDO"));
                response.setTRANGTHAI(reset.getString("TRANGTHAI"));
                response.setDATE_FN(reset.getString("DATE_FN"));
                response.setDATE_Duration(reset.getString("DATE_Duration"));
                progressResponseList.add(response);
                connectSuccess = true;
            }
        }
    }
}
