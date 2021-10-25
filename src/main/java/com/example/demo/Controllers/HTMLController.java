package com.example.demo.Controllers;

import com.example.demo.needName.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import javax.servlet.http.HttpSession;


/**
 * @author roed
 */

@Controller
public class HTMLController {
  SQLcontroller sqLcontroller = new SQLcontroller();

  @GetMapping("/register")
  public String register() {
    return "register";
  }

  @GetMapping("/login")
  public String login(Model model) {
    //counter++;
    //model.addAttribute("counter", counter);
    return "login";
  }

  //user kan erstattes med new User()
  @PostMapping("/validateLogin")
  public String validateLogin(WebRequest request, HttpSession session) {
    User user = new User(request.getParameter("email"), request.getParameter("password"));
    return sqLcontroller.validateUser(user, request, session);
  }

  @PostMapping("/createAccount")
  public String createAccount(WebRequest request){
    return sqLcontroller.createAccount(request);
  }

  @PostMapping("/logout")
  public String logOut(HttpSession session){
    return sqLcontroller.logOut(session);
  }
}
