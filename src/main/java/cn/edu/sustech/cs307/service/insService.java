package cn.edu.sustech.cs307.service;

import cn.edu.sustech.cs307.database.SQLDataSource;
import cn.edu.sustech.cs307.dto.CourseSection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class insService implements InstructorService{
    @Override
    public void addInstructor(int userId, String firstName, String lastName) {
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("call addInstructor(?,?::varchar ,?::varchar )")){
            stmt.setInt(1,userId);
            stmt.setString(2,firstName);
            stmt.setString(3,lastName);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<CourseSection> getInstructedCourseSections(int instructorId, int semesterId) {
        List<CourseSection> list=new ArrayList<>();
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("select * from getInstructedCourseSections(?,?)")){
            stmt.setInt(1,instructorId);
            stmt.setInt(2,semesterId);
            ResultSet res =stmt.executeQuery();
            while (res.next()) {
                CourseSection courseSection=new CourseSection();
                courseSection.id=res.getInt(1);
                courseSection.name=res.getString(2);
                courseSection.totalCapacity=res.getInt(3);
                courseSection.leftCapacity=res.getInt(4);
                list.add(courseSection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
