package com.example.hokelamini.Models;

public class Answer {

    long id;
    String answer;
    long question_id;
    long user_id;

    public Answer(){

    }

    public Answer(String answer, long question_id, long user_id) {
        this.answer = answer;
        this.question_id = question_id;
        this.user_id = user_id;
    }

    public Answer(String answer) {
        this.answer = answer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public long getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(long question_id) {
        this.question_id = question_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
}
