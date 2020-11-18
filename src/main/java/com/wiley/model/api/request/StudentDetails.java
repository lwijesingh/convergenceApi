package com.wiley.model.api.request;

import java.util.ArrayList;
import java.util.List;

public class StudentDetails {
    public int id;
    public String firstName;
    public float mobileNo;
    //List object parameter name must match to the json parameter name
    public List<CourseDetails> coursesAttended = new ArrayList<>();

}
