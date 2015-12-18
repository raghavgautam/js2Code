case class Nested(menu: Menu)
case class Menu(id: String, value: String, popup: Popup)
case class Popup(menuitem: oneOfMenuitem)
case class oneOfMenuitem(value: String, onclick: String)