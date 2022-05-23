// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/vinniebrice/gradsProject/conf/routes
// @DATE:Mon May 23 17:08:12 BST 2022


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
