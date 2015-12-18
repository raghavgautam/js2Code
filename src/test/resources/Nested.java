class Nested {

    @SerializedName("menu")
    private final Menu menu = null;
    public Menu getMenu() {
        return menu;
    }

}
class Menu {

    @SerializedName("id")
    private final String id = null;
    public String getId() {
        return id;
    }

    @SerializedName("value")
    private final String value = null;
    public String getValue() {
        return value;
    }

    @SerializedName("popup")
    private final Popup popup = null;
    public Popup getPopup() {
        return popup;
    }

}
class Popup {

    @SerializedName("menuitem")
    private final OneOfMenuitem menuitem = null;
    public OneOfMenuitem getMenuitem() {
        return menuitem;
    }

}
class OneOfMenuitem {

    @SerializedName("value")
    private final String value = null;
    public String getValue() {
        return value;
    }

    @SerializedName("onclick")
    private final String onclick = null;
    public String getOnclick() {
        return onclick;
    }

}