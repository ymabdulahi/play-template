// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/yonis.abdulahi/Documents/play/play-template/conf/routes
// @DATE:Tue Jun 07 13:07:23 BST 2022


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
