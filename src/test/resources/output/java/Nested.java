/*
 * Copyright 2016 rgautam
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

class Nested {

    @SerializedName("menu")
    private final output.scala.Menu menu = null;
    public output.scala.Menu getMenu() {
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
    private final output.scala.Popup popup = null;
    public output.scala.Popup getPopup() {
        return popup;
    }

}
class Popup {

    @SerializedName("menuitems")
    private final List<output.scala.OneOfMenuitems> menuitems = null;
    public List<output.scala.OneOfMenuitems> getMenuitems() {
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