package com.cbox.common.bean;

public class BaseTreeVO {

    /* 主要信息，从数据库中获取 */
    private String id;
    private String code;// 编码（可空）
    private String name;// 短名称
    private String title;// 长名称（可空）
    private String pid;// 父id

    /*以下信息是用作控制的信息，根据所用的前端树组件而定*/
    private boolean noRemoveBtn;
    private boolean noEditBtn;
    private boolean open;
    // private String icon;// 节点自定义图标的 URL 路径
    private boolean checked;// 节点勾选状态
    private boolean nocheck;// true 表示此节点不显示 checkbox / radio
    private boolean isParent; // 是否父节点
    // ztree树自定义数据信息，不同业务不同含义
    private String diy1;
    private String diy2;
    private String diy3;
    private String diy4;

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    public boolean isNoRemoveBtn() {
        return noRemoveBtn;
    }

    public void setNoRemoveBtn(boolean noRemoveBtn) {
        this.noRemoveBtn = noRemoveBtn;
    }

    public boolean isNoEditBtn() {
        return noEditBtn;
    }

    public void setNoEditBtn(boolean noEditBtn) {
        this.noEditBtn = noEditBtn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isNocheck() {
        return nocheck;
    }

    public void setNocheck(boolean nocheck) {
        this.nocheck = nocheck;
    }

    public String getDiy1() {
        return diy1;
    }

    public void setDiy1(String diy1) {
        this.diy1 = diy1;
    }

    public String getDiy2() {
        return diy2;
    }

    public void setDiy2(String diy2) {
        this.diy2 = diy2;
    }

    public String getDiy3() {
        return diy3;
    }

    public void setDiy3(String diy3) {
        this.diy3 = diy3;
    }

    public String getDiy4() {
        return diy4;
    }

    public void setDiy4(String diy4) {
        this.diy4 = diy4;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean isParent) {
        this.isParent = isParent;
    }

}
