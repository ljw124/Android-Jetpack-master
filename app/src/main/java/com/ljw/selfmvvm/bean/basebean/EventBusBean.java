package com.ljw.selfmvvm.bean.basebean;

import java.io.Serializable;

/**
 * Create by Ljw on 2019/12/16 14:25
 * EventBus 统一管理
 */
public class EventBusBean implements Serializable {
    //1:刷新首页
    private int type;
    private Object value;

    public EventBusBean(int type, Object value) {
        this.type = type;
        this.value = value;
    }

    public EventBusBean(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
