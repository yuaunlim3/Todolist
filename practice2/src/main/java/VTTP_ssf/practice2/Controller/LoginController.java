package VTTP_ssf.practice2.Controller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import VTTP_ssf.practice2.Model.Users;
import VTTP_ssf.practice2.Services.userServices;
import jakarta.validation.Valid;

@Controller
@RequestMapping({ "/", "/index.html" })
public class LoginController {
    private final Logger logger = Logger.getLogger(LoginController.class.getName());

    @Autowired
    private userServices userSrv;

    @GetMapping("/loginpage")
    public String loginpage(Model model) {
        model.addAttribute("user", new Users());
        return "index";
    }

    @PostMapping("/loginpage")
    public String login(@Valid @ModelAttribute("user") Users currUser, BindingResult bindings, Model model) {
        logger.info("Name: %s Password: %s/n".formatted(currUser.getUsername(), currUser.getPassword()));
        if (bindings.hasErrors()) {
            logger.warning("ERROR");
            return "index";
        }
        if(!userSrv.checkUser(currUser)){
            logger.info("Not in database");
            FieldError err1 = new FieldError("user", "username", "User does not exist");
            bindings.addError(err1);
            FieldError err2 = new FieldError("user", "password","");
            bindings.addError(err2);
            return "index";
        }
        if (!userSrv.checkPassword(currUser, currUser.getPassword())) {
            FieldError err2 = new FieldError("user", "password", "Password is incorrect");
            bindings.addError(err2);
            return "index";
        }
        String username = currUser.getUsername();
        model.addAttribute("username", username);
        return "redirect:/homepage/" + username;
    }

    
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("newUser") Users newUser, BindingResult bindings, Model model) {
        if (bindings.hasErrors()) {
            logger.warning("ERROR");
            return "createAccount";
        }
        if(userSrv.checkUser(newUser)){
            FieldError err = new FieldError("newUser", "name", "User is already created");
            bindings.addError(err);
            return "createAccount";
        }
        userSrv.newUser(newUser);
        String username = newUser.getUsername();
        model.addAttribute("username", username);
        return "redirect:/homepage/" + username;
    }

    @GetMapping("/homepage/{username}")
    public String showHomepage(@PathVariable String username, Model model) {
        Users user = new Users();
        user.setUsername(username);
        List<String> task = userSrv.currentTask(user);
        model.addAttribute("username", username);
        model.addAttribute("currentTask", task);
        return "homepage";
    }

    @PostMapping("/homepage/{username}")
    public String backHomepage(@PathVariable String username, Model model) {
        Users user = new Users();
        user.setUsername(username);
        List<String> task = userSrv.currentTask(user);
        model.addAttribute("username", username);
        model.addAttribute("currentTask", task);
        return "homepage";
    }

    @GetMapping("/createaccount")
    public String showSignupForm(Model model) {
        model.addAttribute("newUser", new Users());
        return "createAccount"; 
    }
}
