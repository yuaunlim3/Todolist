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
import org.springframework.web.bind.annotation.RequestParam;

import VTTP_ssf.practice2.Model.Tasks;
import VTTP_ssf.practice2.Model.Users;
import VTTP_ssf.practice2.Services.userServices;
import jakarta.validation.Valid;

@Controller
@RequestMapping
public class UserController {
    private final Logger logger = Logger.getLogger(LoginController.class.getName());

    @Autowired
    private userServices userSrv;

    @GetMapping("tasklist/{username}")
    public String tasklist(@PathVariable String username, Model model) {
        Users user = new Users();
        user.setUsername(username);
        List<List<String>> taskList = userSrv.getList(user);
        model.addAttribute("username", username);
        model.addAttribute("tasklist", taskList);

        return "tasklist";
    }

    @PostMapping("tasklist/{username}")
    public String doneTask(@PathVariable String username,@RequestParam(required = false) String task0, 
    @RequestParam(required = false) String task1,Model model){
        Users user = new Users();
        user.setUsername(username);
        userSrv.doneTask(user, task0, task1);
        List<List<String>> taskList = userSrv.getList(user);
        model.addAttribute("username", username);
        model.addAttribute("tasklist", taskList);

        return "tasklist";
    }

    @GetMapping("add_task/{username}")
    public String addtaskpage(@PathVariable String username, Model model) {
        model.addAttribute("newTask", new Tasks());
        model.addAttribute("username", username);
        return "addTask";
    }

    @PostMapping("add_task/{username}")
    public String addtask(@Valid @ModelAttribute("newTask") Tasks newTask, BindingResult bindings,
            @PathVariable String username, Model model) {

        if (bindings.hasErrors()) {
            logger.warning("ERROR");
            return "addTask";
        }
        Users user = new Users();
        user.setUsername(username);
        logger.info("Username: %s\n".formatted(username));
        userSrv.addTasks(user, newTask);
        model.addAttribute("newTask", new Tasks());
        model.addAttribute("username", username);
        List<String> task = userSrv.currentTask(user);
        model.addAttribute("currentTask", task);
        return "homepage";
    }

}
