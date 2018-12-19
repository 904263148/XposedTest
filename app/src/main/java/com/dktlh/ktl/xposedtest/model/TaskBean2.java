package com.dktlh.ktl.xposedtest.model;

public class TaskBean2 {
    private Long time;
    private String mark;
    //0:未处理,1:已处理
    private int stste;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public int getStste() {
        return stste;
    }

    public void setStste(int stste) {
        this.stste = stste;
    }
}
