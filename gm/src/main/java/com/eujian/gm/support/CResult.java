package com.eujian.gm.support;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
@Data
public class CResult<T>  implements Serializable {


    private static final long serialVersionUID = 1L;


    public static final int SUCCESS = 0;

    public static final int FAIL = 1;


    public static final String SUCCESS_MSG = "success";
    public static final String FAIL_MSG = "fail";


    private int code = SUCCESS;

    private T data;

    private String msg;


    public CResult() {
        super();
    }

    public CResult(T data) {
        super();
        this.data = data;
    }

    public CResult(T data, String msg) {
        super();
        this.data = data;
        this.msg = msg;
    }


    public static <T>CResult<T> success(Object data){
        CResult r = new CResult<>();
        r.setData(data);
        r.setCode(SUCCESS);
        r.setMsg(SUCCESS_MSG);
        return r;
    }

    public static <T>CResult<T>fail(String msg){
        CResult r = new CResult<>();
        r.setCode(FAIL);
        r.setMsg(msg);
        return r;
    }
    public static <T>CResult<T> fail(int code,String msg){
        CResult r = new CResult<>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }


    @JsonIgnore
    public Boolean isRSuccess(){
        return code == SUCCESS;
    }
}
