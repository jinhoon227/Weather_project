package com.yogo.pjh.weather_projcect_v10;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MyClosetRequest extends StringRequest {
    final static private String URL="pjtakeclothe.php";
    private Map<String,String> parameters;

    public MyClosetRequest(String userID,Response.Listener<String> listener)
    {
        super(Method.POST,URL,listener,null);
        parameters=new HashMap<>();
        parameters.put("userID",userID);
    }


    @Override
    public Map<String, String> getParams()
    {
        return parameters;
    }

}