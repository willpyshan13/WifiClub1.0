package cn.situne.itee.view;


public class SlidingMenuItem {

    private int menuId;
    private int rowHeight;
    private int menuTextResourceId;

    public SlidingMenuItem(int menuId, int rowHeight, int titleResourceId) {
        this.menuId = menuId;
        this.rowHeight = rowHeight;
        this.menuTextResourceId = titleResourceId;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public int getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(int rowHeight) {
        this.rowHeight = rowHeight;
    }

    public int getMenuTextResourceId() {
        return menuTextResourceId;
    }

    public void setMenuTextResourceId(int menuTextResourceId) {
        this.menuTextResourceId = menuTextResourceId;
    }
}
