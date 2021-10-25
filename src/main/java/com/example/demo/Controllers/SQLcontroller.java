package com.example.demo.Controllers;

import com.example.demo.DBManager.DBManager;
import com.example.demo.needName.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


@Controller
public class SQLcontroller {
  Connection connection;
  PreparedStatement ps;
  boolean bol;
  ResultSet rs;
  ArrayList<User> users = new ArrayList<>();


  public void query(String sqlCommand) {
    try {
      connection = DBManager.getConnection();
      ps = connection.prepareStatement(sqlCommand);
      bol = ps.execute();
    } catch (SQLException ex) {
      System.out.println(ex.getMessage());
    }
  }

  public ResultSet load(String sqlCommand) {
    try {
      connection = DBManager.getConnection();
      ps = connection.prepareStatement(sqlCommand);
      rs = ps.executeQuery();
    } catch (SQLException ex) {
      System.out.println(ex.getMessage());
    }
    return rs;
  }

  public ArrayList<User> getResults(ResultSet rs) {
    try {
      users.clear();
      while (rs.next()) {
        users.add(new User(rs.getString(2), rs.getString(3), rs.getString(4)));
      }
    } catch (SQLException ex) {
      System.out.println(ex.getMessage());
    }
    return users;
  }

  public String validateUser(User user, WebRequest request, HttpSession session) {
    users = getResults(load("Select * from website.userdata"));
    for (int i = 0; i < users.size(); i++) {
      if (users.get(i).getEmail().equals(user.getEmail()) && users.get(i).getPassword().equals(user.getPassword())) {
        if (session.getAttribute("user") == null) {
          request.setAttribute("user", user, WebRequest.SCOPE_SESSION);
        }
        return "index";
      }
    }
    return "redirect:/";
  }

  public String createAccount(WebRequest request) {
    query("insert into UserData(Name, Email, Password) values(" + "\"" + request.getParameter("name") + "\", \"" +
        request.getParameter("email") + "\", \"" + request.getParameter("password") + "\")");
    return reloadUsers();
  }

  public String reloadUsers() {
    users = getResults(load("Select * from website.userdata"));
    return "login";
  }

  public String logOut(HttpSession session){
    session.removeAttribute("user");
    return "index";
  }
}