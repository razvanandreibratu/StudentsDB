package studentsapp;

import java.util.Scanner;

public class StudentApp {
    public static void main(String[] args){

        Scanner in = new Scanner(System.in);
        boolean run = true;

        //initialize the tables for students db
        CourseControl.createCourseDb();
        StudentController.createStudentDb();

        System.out.println("Hello ADMIN, please enter the command:\n 1-Add students to db\n 2-Display DB \n 3-Enter another course\n 4-Display courses\n 5-Assign student to a course\n 6-EXIT");
        while(run)
        {
            System.out.print("Your command: ");
            int command = Integer.parseInt(in.nextLine());
            if(command == 1)
            {
                System.out.print("Enter the amount of students you want to add to the db: ");
                int student_number = Integer.parseInt(in.nextLine());
                while(student_number > 0)
                {
                    Student student = new Student();
                    student_number--;
                    StudentController.insertStudent(student);
                    CourseControl.assignStudentAuto(student);
                }
            } else if (command == 2)
            {
                StudentController.displayStudentsDb();
            } else if (command == 3) {
                System.out.print("Enter course name: ");
                String course = in.nextLine();
                System.out.print("Enter course status: ");
                String status = in.nextLine().toLowerCase().trim();

                if (course.trim().matches("[a-zA-Z]+") && status.equals("primary"))
                {
                    CourseControl.insertNewCourse(course, status);
                    CourseControl.updateDb(course);
                }else if (status.equals("optional"))
                {
                    CourseControl.insertNewCourse(course, status);
                }else {
                    System.out.println("Incorrect typo");
                }
            } else if (command == 4)
            {
                CourseControl.displayCourses();
            } else if (command == 5) {

                System.out.print("Provide student first name: ");
                String first_name = in.nextLine();
                System.out.print("Provide student last name: ");
                String last_name = in.nextLine();
                System.out.println("What OPTIONAL course you want to assign the student: ");
                String course = in.nextLine();

                CourseControl.assignCourseToStudent(first_name, last_name, course);
            } else if (command == 6){
                System.out.println("Goodbye!!!");
                run = false;
            }
        }
    }
}
