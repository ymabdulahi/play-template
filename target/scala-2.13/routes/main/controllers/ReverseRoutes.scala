// @GENERATOR:play-routes-compiler
// @SOURCE:/Users/vinniebrice/gradsProject/conf/routes
// @DATE:Thu May 12 15:50:39 BST 2022

import play.api.mvc.Call


import _root_.controllers.Assets.Asset

// @LINE:2
package controllers {

  // @LINE:4
  class ReverseApplicationController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:11
    def findByBook(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "library/byBook")
    }
  
    // @LINE:5
    def create(): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "library")
    }
  
    // @LINE:6
    def find(id:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "library/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("id", id)))
    }
  
    // @LINE:10
    def deleteAll(): Call = {
      
      Call("DELETE", _prefix + { _defaultPrefix } + "library/allBook")
    }
  
    // @LINE:9
    def delete(id:String): Call = {
      
      Call("DELETE", _prefix + { _defaultPrefix } + "library/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("id", id)))
    }
  
    // @LINE:7
    def update(id:String): Call = {
      
      Call("PUT", _prefix + { _defaultPrefix } + "library/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("id", id)))
    }
  
    // @LINE:8
    def upsert(id:String): Call = {
      
      Call("PUT", _prefix + { _defaultPrefix } + "library/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("id", id)))
    }
  
    // @LINE:4
    def index(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "library")
    }
  
  }

  // @LINE:2
  class ReverseHomeController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:2
    def index(): Call = {
      
      Call("GET", _prefix)
    }
  
  }

  // @LINE:14
  class ReverseAssets(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:14
    def versioned(file:Asset): Call = {
      implicit lazy val _rrc = new play.core.routing.ReverseRouteContext(Map(("path", "/public"))); _rrc
      Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[play.api.mvc.PathBindable[Asset]].unbind("file", file))
    }
  
  }


}
