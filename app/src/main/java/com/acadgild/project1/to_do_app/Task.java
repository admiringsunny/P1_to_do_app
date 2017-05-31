package com.acadgild.project1.to_do_app;


public class Task {
    private int id;
    private String title;
    private String description;
    private String targetdate;
    private int status; // 0 - False, 1 - True

    public Task(int id, String taskTitle, String taskDescription, String targetDate) {
        this.id = id;
        this.title = taskTitle;
        this.description = taskDescription;
        this.targetdate = targetDate;
        this.status = 0;
    }

    public Task() {
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskTitle() {
        return title;
    }

    public void setTaskTitle(String taskTitle) {
        this.title = taskTitle;
    }

    public String getTaskDescription() {
        return description;
    }

    public void setTaskDescription(String taskDescription) {
        this.description = taskDescription;
    }

    public String getTargetDate() {
        return targetdate;
    }

    public void setTargetDate(String targetDate) {
        this.targetdate = targetDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
