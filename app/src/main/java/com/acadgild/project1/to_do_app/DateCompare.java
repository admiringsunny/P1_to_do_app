package com.acadgild.project1.to_do_app;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;


public class DateCompare implements Comparator<Task> {
    DateFormat f = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public int compare(Task lhs, Task rhs) {
        String leftDate = lhs.getTargetDate();
        String rightDate = rhs.getTargetDate();

        try {
            return f.parse(leftDate).compareTo(f.parse(rightDate));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
