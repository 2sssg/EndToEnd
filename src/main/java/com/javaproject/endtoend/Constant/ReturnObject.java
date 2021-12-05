package com.javaproject.endtoend.Constant;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;

@Data
public class ReturnObject {
    private boolean isSuccess;

    private String errorName;

    private String errorContent;

    private JSONObject responseOBJ;

    public ReturnObject(){
        this.isSuccess = true;
        this.responseOBJ = new JSONObject();
    }
    public <T> void addJsonObject(String key,T value){
        this.responseOBJ.put(key, value);
    }

    public void setError(String errorName,String errorContent){
        this.errorName = errorName;
        this.errorContent = errorContent;
    }

}
