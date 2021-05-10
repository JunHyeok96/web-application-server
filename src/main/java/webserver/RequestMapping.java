package webserver;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import webserver.controller.Controller;
import webserver.controller.CreateUserController;
import webserver.controller.ListUserController;
import webserver.controller.LoginController;

public class RequestMapping {

  private static Map<String, Controller> controllerMap = new HashMap<>();

  static {
    controllerMap.put("/user/login", new LoginController());
    controllerMap.put("/user/create", new CreateUserController());
    controllerMap.put("/user/list", new ListUserController());
  }

  public static Optional<Controller> getController(String url){
    return Optional.ofNullable(controllerMap.get(url));
  }
}
