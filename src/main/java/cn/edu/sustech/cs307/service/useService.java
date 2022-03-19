package cn.edu.sustech.cs307.service;

import cn.edu.sustech.cs307.database.SQLDataSource;
import cn.edu.sustech.cs307.dto.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class useService implements UserService  {
    @Override
    public void removeUser(int userId) {
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("call removeUser(?)")){
            stmt.setInt(1,userId);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> list=new ArrayList<>();
        try (Connection connection= SQLDataSource.getInstance().getSQLConnection()){
            PreparedStatement stmt =connection.prepareStatement("select * from getAllStudent()");
            ResultSet res =stmt.executeQuery();
            while(res.next()){
                Department department=new Department();
                department.id=res.getInt(6);
                department.name=res.getString(7);
                Major major=new Major();
                major.id=res.getInt(2);
                major.name=res.getString(5);
                major.department=department;
                Student student=new Student();
                student.id=res.getInt(1);
                student.fullName=res.getString(3);
                student.major= major;
                student.enrolledDate=res.getDate(4);
                list.add(student);
            }
            PreparedStatement stmt1 =connection.prepareStatement("select * from getAllInstructor()");
            ResultSet res1 =stmt1.executeQuery();
            while(res1.next()){
                Instructor instructor=new Instructor();
                instructor.id=res.getInt(1);
                instructor.fullName=res.getString(2);
                list.add(instructor);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public User getUser(int userId) {
        try (Connection connection= SQLDataSource.getInstance().getSQLConnection()){
            PreparedStatement stmt =connection.prepareStatement("select * from getUser(?)");
            stmt.setInt(1,userId);
            ResultSet res =stmt.executeQuery();
            if(res.next()){
                Student student=new Student();
                Department department=new Department();
                department.id=res.getInt(6);
                department.name=res.getString(7);
                Major major=new Major();
                major.id=res.getInt(2);
                major.name=res.getString(5);
                major.department=department;
                student.id=res.getInt(1);
                student.fullName=res.getString(3);
                student.major= major;
                student.enrolledDate=res.getDate(4);
                return student;
            }
            PreparedStatement stmt1 =connection.prepareStatement("select * from getInstructor(?)");
            stmt.setInt(1,userId);
            ResultSet res1 =stmt1.executeQuery();
            if(res1.next()){
                Instructor instructor=new Instructor();
                instructor.id=res.getInt(1);
                instructor.fullName=res.getString(2);
                return instructor;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
