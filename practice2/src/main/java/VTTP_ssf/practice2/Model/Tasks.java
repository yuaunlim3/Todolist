package VTTP_ssf.practice2.Model;

import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class Tasks {

    
    @NotEmpty(message = "Name cannot be empty")
    private String taskName;

    
    @NotNull(message = "Date cannot be null")
    private LocalDate date;

    
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public String getTaskName() {
        return taskName;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    
    
}
