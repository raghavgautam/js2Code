class Nested {

    @SerializedName("menu")
    final Menu menu;
    public Menu getMenu {
        return menu;
    }

}
class Menu {

    @SerializedName("id")
    final String id;
    public String getId {
        return id;
    }

    @SerializedName("value")
    final String value;
    public String getValue {
        return value;
    }

    @SerializedName("popup")
    final Popup popup;
    public Popup getPopup {
        return popup;
    }

}
class Popup {

    @SerializedName("menuitem")
    final oneOfMenuitem menuitem;
    public oneOfMenuitem getMenuitem {
        return menuitem;
    }

}
class oneOfMenuitem {

    @SerializedName("value")
    final String value;
    public String getValue {
        return value;
    }

    @SerializedName("onclick")
    final String onclick;
    public String getOnclick {
        return onclick;
    }

}