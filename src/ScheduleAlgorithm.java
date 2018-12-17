import java.util.ArrayList;
import java.util.Collections;

public class ScheduleAlgorithm {

	/*-match based on course code
     -put in all codes (optional times)
     - if times is selected or only one option grab that course
    - else
          -put in any that are the only one for a specific time slot
if still more to put in and all equally important
          -count how many each coursecode comes up
          -pick the one that comes up least 
	 */
	public  static CourseSchedule createSchedule(ArrayList<String> requestedCourses, ArrayList<Course> courses, String semester )
	{
		ArrayList<ArrayList<Course>> allCourses = new ArrayList<ArrayList<Course>>(); //this will hold the arrayLists of all
				//the arraylists of the courses that match the users requested courses
		
		//get an individual array of all the options available for each requested course code
		for(int i =0; i< requestedCourses.size(); i++)
		{	
			ArrayList<Course> courseOptions = searchCourses(courses, requestedCourses.get(i));
			allCourses.add(courseOptions);
		}
		
		//sort the array so we will have the list with the course thats offered the least times first
		//this is not efficient but it seems like the only way to accomplish what we need 
		//since otherwise (if we keep selecting the smallest size array) we will not know which courses were taken care of yet
		//unless we remove them but them the loop will be messed up.....
		Collections.sort(allCourses, new ArrayListSizeComparator());
		
		//actual schedule for the user
		CourseSchedule courseSchedule = new CourseSchedule(18, semester);
		
		ArrayList<Course> coursesToAdd;
		
		boolean courseAdded;
		
		//we will go through and keep on trying to add the courses until the arrayList is empty
		for(int i =0; i< allCourses.size();i++) 
		{	
			coursesToAdd = allCourses.get(i); //get the first arraylist of the courses that were selected for this user to choose from
			courseAdded = false; //reset the flag for each new courseCode we are trying to add to the schedule
			
			//loop through the individual array to try to add the course to the schedule.
			//once its been added successfully, break out of loop
			for(int j = 0; j< coursesToAdd.size();j++)
			{	
				try {
					courseSchedule.addCourse(coursesToAdd.get(j));
					courseAdded = true;
					break;
				}catch(TimeslotConflictException e)
				{
					//now we will do nothing, as the loop will continue
				}catch(CreditOverflowException e )
				{
					System.out.println(e); //credits overflow, what do we want it to do now?
				}
			}
			
			if(!courseAdded)
			{
				//what to do? this course was not able to be added in a way that doesn't conflict
				//display error message of sorts or collect all the courses that weren't added and somehow send that back
				//perhaps to the gui to be gathered for one error message at the bottom of the schedule.....???
			}
		}
		
		return courseSchedule;
		
		
	}
	
	
	public static ArrayList<Course> searchCourses(ArrayList<Course> courses, String courseCode)
	{
		ArrayList<Course> selectedCourses = new ArrayList<Course>();
		
		for(Course course : courses)
		{
			if(course.getCode().equalsIgnoreCase(courseCode))
			{
				selectedCourses.add(course);
			}
		}
		
		return selectedCourses;
	}
	
}