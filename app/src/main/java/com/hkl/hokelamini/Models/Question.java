package com.hkl.hokelamini.Models;

public class Question {

    long id;
    String question_text;
    String type;
    String options;
    int mandatory;
    long survey_id;
    String created_at;
    private boolean expanded;
    Answer answer;

    public Question(long id, String question_text, String type, String options, int mandatory, long survey_id, String created_at) {
        this.id = id;
        this.question_text = question_text;
        this.type = type;
        this.options = options;
        this.mandatory = mandatory;
        this.survey_id = survey_id;
        this.created_at = created_at;
        this.expanded = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMandatory() {
        return mandatory;
    }

    public void setMandatory(int mandatory) {
        this.mandatory = mandatory;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public long getSurvey_id() {
        return survey_id;
    }

    public void setSurvey_id(long survey_id) {
        this.survey_id = survey_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
}
