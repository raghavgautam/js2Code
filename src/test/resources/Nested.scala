case class Nested(menu: Menu)
case class Menu(id: String, value: String, popup: Popup)
case class Popup(menuitem: OneOfMenuitem)
case class OneOfMenuitem(value: String, onclick: String)