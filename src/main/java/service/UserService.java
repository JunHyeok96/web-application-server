package service;

import db.DataBase;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import model.User;

public class UserService {

  public void saveUser(User user) {
    DataBase.addUser(user);
  }

  public boolean login(String id, String password) {
    Optional<User> user = Optional.ofNullable(DataBase.findUserById(id));
    if(!user.isPresent()){
      return false;
    }
    return user.get().isMatchPassword(password);
  }

  public List<User> findUserList(){
    return new ArrayList<>(DataBase.findAll());
  }

}
