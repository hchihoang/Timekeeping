package com.timekeeping.smart.network;
import com.timekeeping.smart.entity.request.LoginRequest;
import com.timekeeping.smart.entity.response.Decentralization;
import com.timekeeping.smart.entity.response.LoginResponse;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Connection_Login extends Connection_Base {
    public Connection_Login(LoginRequest loginRequest,
                            DataCallback<LoginResponse> callback) {
        super();
        this.loginRequest = loginRequest;
        this.callback = callback;
    }
    LoginRequest loginRequest;
    DataCallback<LoginResponse> callback = null;
    LoginResponse loginResponse = new LoginResponse();

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
        callback.onConnectSuccess(loginResponse);
    }

    @Override
    public void callStoreProcedure(Connection conn) throws SQLException{
        // TODO Auto-generated method stub
        super.callStoreProcedure(conn);

        String insertStoreProc = "{call [sp_sysLogin_AD](?,?,?)}";
        CallableStatement callableStatement = conn.prepareCall(insertStoreProc);
        callableStatement.setString(1, loginRequest.getUserName());
        callableStatement.setString(2, loginRequest.getPassWork());
        callableStatement.setString(3, loginRequest.getPassWork());
        callableStatement.execute();
        ResultSet reset = callableStatement.getResultSet();
        if (reset != null){
            while(reset.next()){
                loginResponse.setUsername(reset.getString("ID"));
                loginResponse.setMaNV(reset.getString("ID"));
                loginResponse.setName(reset.getString("NAME"));
                connectSuccess = true;
            }
        }
        if (connectSuccess){
            String insertStoreProc1 = "{call [sp_SelectQuyen_AD](?)}";
            CallableStatement callableStatement1 = conn.prepareCall(insertStoreProc1);
            callableStatement1.setString(1, loginResponse.getMaNV());
            callableStatement1.execute();
            ResultSet reset1 = callableStatement1.getResultSet();
            if (reset1 != null){
                ArrayList<Decentralization> listDecentralization = new ArrayList<>();
                while(reset1.next()){
                    Decentralization decentralization = new Decentralization();
                    decentralization.setID(reset1.getString("ID"));
                    decentralization.setMenuID(reset1.getString("MenuID"));
                    decentralization.setNAME_VN(reset1.getString("NAME_VN"));
                    decentralization.setXem(reset1.getInt("Xem"));
                    decentralization.setUserID(reset1.getString("UserID"));
                    listDecentralization.add(decentralization);
                }
                reset1.close();
                loginResponse.setListDecentralization(listDecentralization);
            }
        }
        closeDatabase();
    }
}
