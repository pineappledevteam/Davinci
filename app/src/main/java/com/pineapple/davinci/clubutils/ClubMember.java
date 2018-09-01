package com.pineapple.davinci.clubutils;

import com.pineapple.davinci.resources.MyCallback;
import com.pineapple.davinci.resources.MyException;
import com.pineapple.davinci.studentutils.Student;
import com.pineapple.davinci.studentutils.Student_Delegate;

public class ClubMember {

    private String studentUID;

    private Integer rank;
    private final int MEMBER = 0;
    private final int OFFICER = 1;
    private final int MANAGER = 2;


    public ClubMember() {  }

    public ClubMember(String studentUID, Integer rank) {
        this.studentUID = studentUID;
        this.rank = rank;
    }

    public String getStudentUID() { return this.studentUID; }

    public void getStudent(final MyCallback<Student> callback) {
        Student_Delegate.getStudentFromFirebase(studentUID, new MyCallback<Student>() {
            @Override
            public void accept(Student student) {
                callback.accept(student);
            }
        });
    }

    public Integer getRank() { return this.rank; }

    public String getPosition() {
        switch((int) this.rank) {
            case MEMBER:
                return "member";
            case OFFICER:
                return "officer";
            case MANAGER:
                return "manager";
            default:
                getStudent(new MyCallback<Student>() {
                    @Override
                    public void accept(Student student) {
                        throw new MyException("invalid club member created. Student: " + student);
                    }
                });
                return null;
        }
    }
}
