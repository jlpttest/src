package com.example.baymax.smarthome;

/**
 * Created by BayMax on 5/1/2019.
 */

public class Model {
    public String value;
    public Model(){
    }
    public Model(String value){
        this.value=value;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
