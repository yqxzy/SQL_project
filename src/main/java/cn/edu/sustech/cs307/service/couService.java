package cn.edu.sustech.cs307.service;

import cn.edu.sustech.cs307.database.SQLDataSource;
import cn.edu.sustech.cs307.dto.*;
import cn.edu.sustech.cs307.dto.prerequisite.AndPrerequisite;
import cn.edu.sustech.cs307.dto.prerequisite.CoursePrerequisite;
import cn.edu.sustech.cs307.dto.prerequisite.Prerequisite;

import javax.annotation.Nullable;
import javax.persistence.criteria.CriteriaBuilder;
import java.sql.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class couService implements CourseService{
    @Override
    public void addCourse(String courseId, String courseName, int credit, int classHour, Course.CourseGrading grading, @Nullable Prerequisite prerequisite) {
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("call addCourse(?::varchar ,?::varchar ,? ,?,?,?)")){
            stmt.setString(1,courseId);
            stmt.setString(2,courseName);
            stmt.setInt(3,credit);
            stmt.setInt(4,classHour);
            stmt.setString(5, String.valueOf(grading));
            int a_o_l=0;int cnt=0;int parent=0;
           if(prerequisite.getClass()==AndPrerequisite.class){
               a_o_l=0;
               PreparedStatement stmt1 =connection.prepareStatement("call add_prerequisite(?,?,?,?)");
               cnt++;
               stmt1.setString(1,courseId);
               stmt1.setInt(4,a_o_l);
               parent = cnt;
              for(int i=0;i<((AndPrerequisite) prerequisite).terms.size();i++){
                  if(((AndPrerequisite) prerequisite).terms.get(i).getClass()== CoursePrerequisite.class){

                  }
              }
           }if(prerequisite.getClass()== CoursePrerequisite.class){
               stmt.setString(6, String.valueOf(prerequisite));
            }else {

            }
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int addCourseSection(String courseId, int semesterId, String sectionName, int totalCapacity) {
        int id=0;
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("select * from addCourseSection(?::varchar ,?,?::varchar ,? )")){
            stmt.setString(1,courseId);
            stmt.setInt(2,semesterId);
            stmt.setString(3,sectionName);
            stmt.setInt(4,totalCapacity);
            ResultSet res =stmt.executeQuery();
            id=res.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public int addCourseSectionClass(int sectionId, int instructorId, DayOfWeek dayOfWeek, Set<Short> weekList, short classStart, short classEnd, String location) {
        int id=0;
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("select * from addCourseSectionClass(?,?,?::varchar,?::integer[],?,?,?::varchar)")){
            stmt.setInt(1,sectionId);
            stmt.setInt(2,instructorId);
            stmt.setString(3, String.valueOf(dayOfWeek));
            stmt.setArray(4, (Array) weekList);
            stmt.setInt(5,classStart);
            stmt.setInt(6,classEnd);
            stmt.setString(7,location);
            ResultSet res =stmt.executeQuery();
            id=res.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }



    @Override
    public void removeCourse(String courseId) {
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("call removeCourse(?::varchar)")){
            stmt.setString(1,courseId);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void removeCourseSection(int sectionId) {
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("call removeCourseSection(?)")){
            stmt.setInt(1,sectionId);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void removeCourseSectionClass(int classId) {
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("call removeCourseSectionClass(?)")){
            stmt.setInt(1,classId);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Course> getAllCourses() {
        List<Course> list=new ArrayList<>();
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("select * from getAllCourses()")){
            ResultSet res =stmt.executeQuery();
            while (res.next()){
               Course course=new Course();
                course.id=res.getString(1);
                course.name=res.getString(2);
                course.classHour=res.getInt(4);
                course.credit=res.getInt(3);
                course.grading= Course.CourseGrading.valueOf(res.getString(5));
                list.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<CourseSection> getCourseSectionsInSemester(String courseId, int semesterId) {
        List<CourseSection> list=new ArrayList<>();
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("select * from getCourseSectionsInSemester(?::varchar ,?)")){
            stmt.setString(1,courseId);
            stmt.setInt(2,semesterId);
            ResultSet res =stmt.executeQuery();
            while (res.next()){
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

    @Override
    public Course getCourseBySection(int sectionId) {
        Course course=new Course();
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("select * from getCourseBySection(?)")){
            stmt.setInt(1,sectionId);
            ResultSet res =stmt.executeQuery();
            if (res.next()){
                course.id=res.getString(1);
                course.name=res.getString(2);
                course.classHour=res.getInt(4);
                course.credit=res.getInt(3);
                course.grading= Course.CourseGrading.valueOf(res.getString(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return course;
    }

    @Override
    public List<CourseSectionClass> getCourseSectionClasses(int sectionId) {
        List<CourseSectionClass> list=new ArrayList<>();
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("select * from getCourseSectionClasses(?)")){
            stmt.setInt(1,sectionId);
            ResultSet res =stmt.executeQuery();
            while (res.next()){
                CourseSectionClass courseSectionClass=new CourseSectionClass();
                courseSectionClass.id=res.getInt(1);
                Instructor instructor=new Instructor();
                instructor.id=res.getInt(2);
                instructor.fullName=res.getString(3);
                courseSectionClass.instructor=instructor;
                courseSectionClass.dayOfWeek=DayOfWeek.valueOf(res.getString(4));
                courseSectionClass.classBegin= (short) res.getInt(6);
                courseSectionClass.classEnd= (short) res.getInt(7);
                courseSectionClass.location=res.getString(8);
                courseSectionClass.weekList= (Set<Short>) res.getArray(5);
                list.add(courseSectionClass);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public CourseSection getCourseSectionByClass(int classId) {
        CourseSection courseSection=new CourseSection();
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("select * from getCourseSectionByClass(?)")){
            stmt.setInt(1,classId);
            ResultSet res =stmt.executeQuery();
            if (res.next()){
                courseSection.id=res.getInt(1);
                courseSection.name=res.getString(2);
                courseSection.totalCapacity=res.getInt(3);
                courseSection.leftCapacity=res.getInt(4);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseSection;
    }

    @Override
    public List<Student> getEnrolledStudentsInSemester(String courseId, int semesterId) {
        List<Student> list =new ArrayList<>();
        try (
                Connection connection= SQLDataSource.getInstance().getSQLConnection();
                PreparedStatement stmt =connection.prepareStatement("select * from getEnrolledStudentsInSemester(?::varchar ,?)")){
            stmt.setString(1,courseId);
            stmt.setInt(2,semesterId);
            ResultSet res =stmt.executeQuery();
            while (res.next()){
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
