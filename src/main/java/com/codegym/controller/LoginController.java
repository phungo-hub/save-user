package com.codegym.controller;

import com.codegym.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@SessionAttributes("user")
public class LoginController {
    @ModelAttribute("user")
    public User returnUser() {
        return new User();
    }

    @GetMapping({"/login", "/"})
    public ModelAndView showLogin(@CookieValue(value = "setUser", defaultValue = "")
                                  String setUser) {
        Cookie cookie = new Cookie("setUser", setUser);
        return new ModelAndView("/login", "cookieValue", cookie);
    }

    @PostMapping("/do-login")
    public ModelAndView login(@CookieValue(value="setUser", defaultValue = "") String setUser,
                              @ModelAttribute("user") User user,
                              HttpServletResponse response,
                              HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/login");
        if (user.getEmail().equals("maitruong@gmail.com") &&
        user.getPassword().equals("123")) {
            if(user.getEmail() != null){
                setUser = user.getEmail();
            }

            Cookie cookie = new Cookie("setUser", setUser);
            cookie.setMaxAge(24 * 60 * 60);
            response.addCookie(cookie);

            Cookie[] cookies = request.getCookies();
            for (Cookie ck : cookies) {
                if (ck.getName().equals("setUser")) {
                    modelAndView.addObject("cookieValue", ck);
                    break;
                }
            }
            modelAndView.addObject("message", "Login success, Welcome");
        } else {
            user.setEmail("");
            Cookie cookie = new Cookie("setUser", setUser);
            modelAndView.addObject("cookieValue", cookie);
            modelAndView.addObject("message", "Login failed. Try again");
        }
        return modelAndView;
    }
}
