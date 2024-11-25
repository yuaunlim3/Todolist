package VTTP_ssf.practice2.Services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import VTTP_ssf.practice2.Model.Tasks;
import VTTP_ssf.practice2.Model.Users;
import VTTP_ssf.practice2.Repository.userRepository;

@Service
public class userServices {

    
    @Autowired
    private userRepository userRepo;

    public void newUser(Users user) {
        String id = UUID.randomUUID().toString().substring(0, 8);
        user.setId(id);
        userRepo.newUser(user);
    }

    public boolean checkUser(Users user){
        return userRepo.checkUser(user);
    }

    public boolean checkPassword(Users user, String password){
        return userRepo.checkPassword(user, password);
    }

    public void addTasks(Users user, Tasks task){
        userRepo.addTask(user, task);
    }

    public List<List<String>> getList(Users user){
        return userRepo.getList(user);
    }

    public void doneTask(Users user, String taskName, String taskDate){
        userRepo.doneTask(user, taskName, taskDate);
    }

    public List<String> currentTask(Users user){
        return userRepo.currentTask(user);
    }

}
