class Nested {

    @SerializedName("menu")
    private final scalaOutput.Menu menu = null;
    public scalaOutput.Menu getMenu() {
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
    private final scalaOutput.Popup popup = null;
    public scalaOutput.Popup getPopup() {
        return popup;
    }

}
class Popup {

    @SerializedName("menuitems")
    private final List<scalaOutput.OneOfMenuitems> menuitems = null;
    public List<scalaOutput.OneOfMenuitems> getMenuitems() {
        return menuitems;
    }

}
class OneOfMenuitems {

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