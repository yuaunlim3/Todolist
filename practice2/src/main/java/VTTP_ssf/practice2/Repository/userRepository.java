package VTTP_ssf.practice2.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import VTTP_ssf.practice2.Model.Tasks;
import VTTP_ssf.practice2.Model.Users;

@Repository
public class userRepository {

    private final Logger logger = Logger.getLogger(userRepository.class.getName());
    @Autowired
    @Qualifier("redis-object")
    private RedisTemplate template;

    // hexist userID
    // hset userID name username
    public void newUser(Users user) {
        HashOperations<String, String, Object> hashOps = template.opsForHash();
        // check if username is exist in database
        logger.info("New Username added: %s".formatted(user.getUsername()));

        Map<String, Object> infos = new HashMap<>();
        infos.put("username", user.getUsername());
        infos.put("password", user.getPassword());
        List<List<String>> tasklist = new ArrayList<>();
        infos.put("tasklist", tasklist);
        hashOps.putAll(user.getId(), infos);

    }

    public boolean checkUser(Users user) {
        // Get all the ids in data
        Set<String> userIds = template.keys("*");
        for (String userId : userIds) {
            HashOperations<String, String, Object> hashOps = template.opsForHash();
            Object storedUsername = hashOps.get(userId, "username");

            if (storedUsername != null && storedUsername.equals(user.getUsername())) {
                return true;
            }
        }
        return false;
    }

    public boolean checkPassword(Users user, String password) {
        HashOperations<String, String, Object> hashOps = template.opsForHash();
        String id = getID(user);
        Map<String, Object> infos = hashOps.entries(id);
        String checker = (String) infos.get("password");
        if (checker.equals(password)) {
            return true;
        }

        return false;
    }

    public void addTask(Users user, Tasks task) {

        HashOperations<String, String, Object> hashOps = template.opsForHash();
        String id = getID(user);
        Map<String, Object> infos = hashOps.entries(id);
        List<List<String>> tasklist = (List<List<String>>) infos.get("tasklist");
        logger.info("Name: %s, Date %s".formatted(task.getTaskName(), task.getDate().toString()));

        logger.info("LIST OF TASKS: %s\n".formatted(tasklist.toString()));
        List<String> taskInfo = new ArrayList<>();
        taskInfo.add(task.getTaskName());
        taskInfo.add(task.getDate().toString());

        tasklist.add(taskInfo);
        tasklist.sort((task1, task2) -> {
            // Parse the date part (second element in each list) into LocalDate
            LocalDate date1 = LocalDate.parse(task1.get(1)); // task1.get(1) is the date
            LocalDate date2 = LocalDate.parse(task2.get(1)); // task2.get(1) is the date

            // Compare the two dates
            return date1.compareTo(date2);
        });
        infos.put("tasklist", tasklist);
        hashOps.putAll(id, infos);

    }

    public List<List<String>> getList(Users user) {
        HashOperations<String, String, Object> hashOps = template.opsForHash();
        String id = getID(user);
        Map<String, Object> infos = hashOps.entries(id);
        return (List<List<String>>) infos.get("tasklist");
    }

    public void doneTask(Users user, String taskname, String taskDate) {
        List<String> task = new ArrayList<String>();
        task.add(taskname);
        task.add(taskDate);
        HashOperations<String, String, Object> hashOps = template.opsForHash();
        String id = getID(user);
        Map<String, Object> infos = hashOps.entries(id);
        List<List<String>> tasklist = (List<List<String>>) infos.get("tasklist");

        tasklist.remove(task);
        infos.put("tasklist", tasklist);
        hashOps.putAll(id, infos);

    }

    public List<String> currentTask(Users user) {
        HashOperations<String, String, Object> hashOps = template.opsForHash();
        String id = getID(user);
        Map<String, Object> infos = hashOps.entries(id);
        List<List<String>> tasklist = (List<List<String>>) infos.get("tasklist");
        logger.info("tasklist: %s\n".formatted(tasklist.toString()));
        if (tasklist != null && !tasklist.isEmpty()) {
            return tasklist.get(0);
        } else {
            return null;
        }
    }

    private String getID(Users user) {
        Set<String> userIds = template.keys("*");
        if (userIds == null || userIds.isEmpty()) {
            return null;
        }
        for (String userId : userIds) {
            HashOperations<String, String, Object> hashOps = template.opsForHash();
            Object storedUsername = hashOps.get(userId, "username");
            if (storedUsername != null && storedUsername.equals(user.getUsername())) {
                return userId;
            }
        }
        return null;
    }
}
