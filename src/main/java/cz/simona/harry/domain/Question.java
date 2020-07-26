package cz.simona.harry.domain;

import java.util.*;

public class Question {

    private Integer quesId;
    private String question;
    private String rightAnswer;
    private Integer level;
    private String option_a;
    private String option_b;
    private String option_c;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question)) return false;
        Question question1 = (Question) o;
        return Objects.equals(getQuesId(), question1.getQuesId()) &&
                Objects.equals(getQuestion(), question1.getQuestion()) &&
                Objects.equals(getRightAnswer(), question1.getRightAnswer()) &&
                Objects.equals(getLevel(), question1.getLevel()) &&
                Objects.equals(getOption_a(), question1.getOption_a()) &&
                Objects.equals(getOption_b(), question1.getOption_b()) &&
                Objects.equals(getOption_c(), question1.getOption_c());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQuesId(), getQuestion(), getRightAnswer(), getLevel(), getOption_a(), getOption_b(), getOption_c());
    }

    public Question(){}

    public Question(Integer ques_id, String question, String right_answer, Integer level, String option_a, String option_b, String option_c) {
        this.quesId = ques_id;
        this.question = question;
        this.rightAnswer = right_answer;
        this.level = level;
        this.option_a = option_a;
        this.option_b = option_b;
        this.option_c = option_c;
    }

    public Integer getQuesId() {
        return quesId;
    }

    public void setQuesId(Integer newValue) {
        quesId = newValue;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String newValue) {
        question = newValue;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String newValue) {
        rightAnswer = newValue;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer newValue) {
        level = newValue;
    }

    public String getOption_a() {
        return option_a;
    }

    public void setOption_a(String newValue) {
        option_a = newValue;
    }

    public String getOption_b() {
        return option_b;
    }

    public void setOption_b(String newValue) {
        option_b = newValue;
    }

    public String getOption_c() {
        return option_c;
    }

    public void setOption_c(String newValue) {
        option_c = newValue;
    }
}
