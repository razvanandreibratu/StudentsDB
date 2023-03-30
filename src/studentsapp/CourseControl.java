package studentsapp;

import java.sql.*;

public abstract class CourseControl {
    public static void createCourseDb()
    {
        String sql = "INSERT INTO courses (course_name, status) VALUES (?, 'primary')";
        try(Connection con = ConnectionDb.getInstance().getConnection())
        {
            DatabaseMetaData metadata = con.getMetaData();
            try(Statement stmt = con.createStatement();
                ResultSet rsCourses = metadata.getTables(null,null, "courses", null);
                ResultSet rsCoursesAss = metadata.getTables(null,null, "course_assignments", null))
            {
                boolean tableCourses = rsCourses.next();
                boolean tableCourseAss = rsCoursesAss.next();
                if(!tableCourses)
                {
                    stmt.execute("CREATE TABLE courses\n" +
                            "(\n" +
                            "    course_id   INT AUTO_INCREMENT NOT NULL\n" +
                            "        PRIMARY KEY,\n" +
                            "    course_name TEXT NOT NULL\n" +
                            "    status      TEXT NOT NULL\n" +
                            ");");
                    PreparedStatement pstmt = con.prepareStatement(sql);
                    for(Courses c: Courses.values())
                    {
                        pstmt.setString(1, c.toString());
                        pstmt.executeUpdate();
                    }
                } else if(!tableCourseAss) {
                    stmt.execute("CREATE TABLE course_assignments (\n" +
                            "course_id INT,\n" +
                            "student_id INT,\n" +
                            "FOREIGN KEY (course_id) REFERENCES courses(course_id),\n" +
                            "FOREIGN KEY (student_id) REFERENCES students(student_id)\n" +
                            ");");

                } else {
                    System.out.println("Already Exists");
                }
            }
        } catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    public static void insertNewCourse(String course, String status)
    {
        try(Connection con = ConnectionDb.getInstance().getConnection();
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO courses(course_name, status) VALUES (?, ?)"))
        {
            pstmt.setString(1, course);
            pstmt.setString(2, status);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void assignStudentAuto(Student student) {

        try (Connection con = ConnectionDb.getInstance().getConnection();
             PreparedStatement coursesStmt = con.prepareStatement("SELECT course_id FROM courses WHERE status = 'primary'");
             PreparedStatement studentsStmt = con.prepareStatement("SELECT student_id FROM students WHERE student_fname = ? AND student_lname = ?");
             PreparedStatement insertStmt = con.prepareStatement("INSERT INTO course_assignments (course_id, student_id) VALUES (?, ?)"))
        {
            studentsStmt.setString(1, student.getFirstName());
            studentsStmt.setString(2, student.getLastName());

            try (ResultSet students = studentsStmt.executeQuery()) {
                if (!students.next()) {
                    throw new SQLException("Student not found: " + student.getFirstName() + " " + student.getLastName());
                }
                int student_id = students.getInt("student_id");

                try (ResultSet courses = coursesStmt.executeQuery()) {
                    while (courses.next()) {
                        int course_id = courses.getInt("course_id");
                        insertStmt.setInt(1, course_id);
                        insertStmt.setInt(2, student_id);
                        insertStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateDb(String course) {

        try(Connection con = ConnectionDb.getInstance().getConnection();
            PreparedStatement coursesStmt = con.prepareStatement("SELECT course_id FROM courses WHERE course_name = ?");
            PreparedStatement studentStmt = con.prepareStatement("SELECT DISTINCT (student_id) FROM course_assignments");
            PreparedStatement insertStmt = con.prepareStatement("INSERT INTO course_assignments (course_id, student_id) VALUES (?, ?)"))
        {
            coursesStmt.setString(1, course);
            try(ResultSet courses = coursesStmt.executeQuery()){
                if(!courses.next())
                {
                    throw new SQLException("Course not found " + course);
                }
                int course_id = courses.getInt("course_id");
                try(ResultSet students = studentStmt.executeQuery()){
                    while(students.next())
                    {
                        int student_id = students.getInt("student_id");
                        insertStmt.setInt(1, course_id);
                        insertStmt.setInt(2, student_id);
                        insertStmt.executeUpdate();
                    }
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    public static void displayCourses()
    {
        try(Connection con = ConnectionDb.getInstance().getConnection())
        {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT course_name,status FROM courses");
            System.out.print("Available courses are: ");
            int counter = 1;
            while(rs.next())
            {
                String course = rs.getString("course_name");
                String status = rs.getString("status");
                System.out.println(Integer.toString(counter++) + "." + course + " " + "status: " + status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void assignCourseToStudent(String fname, String lname, String course){
        try(Connection con = ConnectionDb.getInstance().getConnection();
            PreparedStatement courseStmt = con.prepareStatement("SELECT course_id FROM courses WHERE status = 'optional' AND course_name = ?");
            PreparedStatement studentsStmt = con.prepareStatement("SELECT student_id FROM students WHERE student_fname = ? AND student_lname = ?");
            PreparedStatement insertStmt = con.prepareStatement("INSERT INTO course_assignments (course_id, student_id) VALUES (?,?)"))
        {
            courseStmt.setString(1, course);
            studentsStmt.setString(1, fname);
            studentsStmt.setString(2, lname);

            try(ResultSet cours = courseStmt.executeQuery();
                ResultSet student = studentsStmt.executeQuery();)
            {
                if(!cours.next() || !student.next())
                {
                    throw new SQLException("Incorrect course or student name! Try again!");
                }
                int course_id = cours.getInt("course_id");
                int student_id = student.getInt("student_id");

                insertStmt.setInt(1, course_id);
                insertStmt.setInt(2, student_id);

                insertStmt.executeUpdate();
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

}

