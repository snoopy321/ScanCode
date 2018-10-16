package com.snoopy.scancode.entity;


/*
listview中的单个商品信息
 */
public class GoodItem {

    //商品名称
    private String gName;
    //商品重量信息
    private String gheight;
    //商品数量
    private String gCount;
    //商品价格
    private String gMoney;

    public GoodItem(String gName, String gheight, String gCount, String gMoney) {
        this.gName = gName;
        this.gheight = gheight;
        this.gCount = gCount;
        this.gMoney = gMoney;
    }

    public String getgName() {
        return gName;
    }

    public void setgName(String gName) {
        this.gName = gName;
    }

    public String getGheight() {
        return gheight;
    }

    public void setGheight(String gheight) {
        this.gheight = gheight;
    }

    public String getgCount() {
        return gCount;
    }

    public void setgCount(String gCount) {
        this.gCount = gCount;
    }

    public String getgMoney() {
        return gMoney;
    }

    public void setgMoney(String gMoney) {
        this.gMoney = gMoney;
    }


}
