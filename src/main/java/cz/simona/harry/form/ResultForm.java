package cz.simona.harry.form;

import java.util.*;

public class ResultForm {
    private List<String> result;


    public ResultForm(List<String> result) {
        this.result = result;
    }

    public List<String> getResult(){
        return result;
    }

    public void setResult(List<String> newValue) {
        result = newValue;
    }
}
