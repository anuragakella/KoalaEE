package com.koala.diagnostics;

import com.koala.helpers.ErrorType;

import java.util.ArrayList;
import java.util.List;

public class Diagnostics {
    public List<String> errorsList = new ArrayList<>();
    public void pushError(String errorMessage, int lineno, int charno, ErrorType sender){
        if(sender != ErrorType.RUNTIME_ERROR)
            errorsList.add(errorMessage +  " at " + lineno + ":" + charno);
        else
            errorsList.add(errorMessage + "[Reached end of Input]");
    }
}
