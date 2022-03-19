package cn.edu.sustech.cs307.service;

import cn.edu.sustech.cs307.database.SQLDataSource;
import cn.edu.sustech.cs307.dto.Department;
import cn.edu.sustech.cs307.dto.Major;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class majService implements MajorService{
    @Override
    public int addMajor(String name, int departmentId) {
        int id=0;
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("select * from addMajort(?::varchar ,?)"))
        {
            stmt.setString(1,name);
            stmt.setInt(2,departmentId);
            ResultSet res=stmt.executeQuery();
            if(res.next()){
                id =res.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public void removeMajor(int majorId) {
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("call removeMajor(?)")){
            stmt.setInt(1,majorId);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Major> getAllMajors() {
        List<Major> list=new ArrayList<>();
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("select * getAllMajors()")){
            ResultSet res=stmt.executeQuery();
            while (res.next()){
                Department department=new Department();
                department.id=res.getInt(2);
                department.name=res.getString(4);
                Major major=new Major();
                major.name=res.getString(3);
                major.id=res.getInt(1);
                major.department=department;
                list.add(major);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Major getMajor(int majorId) {
        Major major=new Major();
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("select * from getMajor(?)")){
            stmt.setInt(1,majorId);
            ResultSet res=stmt.executeQuery();
            while (res.next()){
                Department department=new Department();
                department.id=res.getInt(2);
                department.name=res.getString(4);
                major.name=res.getString(3);
                major.id=res.getInt(1);
                major.department=department;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return major;
    }

    @Override
    public void addMajorCompulsoryCourse(int majorId, String courseId) {
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("call addMajorCompulsoryCourse(?,?::varchar)")){
            stmt.setInt(1,majorId);
            stmt.setString(2,courseId);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addMajorElectiveCourse(int majorId, String courseId) {
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("call addMajorElectiveCourse(?,?::varchar)")){
            stmt.setInt(1,majorId);
            stmt.setString(2,courseId);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
