case class Nested(menu: Menu)
case class Menu(id: String, value: String, popup: Popup)
case class Popup(menuitems: List[OneOfMenuitems])
case class OneOfMenuitems(value: String, onclick: String)