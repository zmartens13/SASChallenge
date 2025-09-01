package com.Nash.Packages.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.Nash.Packages.domain.Status;
import com.Nash.Packages.domain.Task;

public class TaskManager {

	ArrayList<Task> taskList = new ArrayList<Task>();

	ArrayList<String> stringTaskList;

	Scanner in = new Scanner(System.in);

	public void start() {

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			//TODO remove
			System.out.println("Shutdown detected. Cleaning up...");
			saveTaskList();
		}));

		loadTaskList();

	}

	/**
	 * 
	 */
	public int mainMenu() {
		updateStringList();
		System.out.println("Welcome to Task-It");
		System.out.println("What would you like to do?");
		ArrayList<String> topMenu = new ArrayList<String>(Arrays.asList("Add a Task", "List current Tasks",
				"Update current Tasks", "Delete current Tasks", "Exit"));

		return displayOptions(topMenu);

		// Deciding whether to use if-then-else statements or a switch statement is
		// based
		// on readability and the expression that the statement is testing. An
		// if-then-else
		// statement can test expressions based on ranges of values or conditions,
		// whereas a
		// switch statement tests expressions based only on a single integer, enumerated
		// value, or String object.
	}

	private void updateStringList() {
		//stringTaskList = taskList.stream().map(Task::toString).collect(Collectors.toCollection(ArrayList::new));
		// Create an updated arraylist in a new variable then assign the stringtasklist to that new heap space
		ArrayList<String> updatedList = new ArrayList<String>();
		for (Task t : taskList) {
			updatedList.add(t.toString());
		}

	}

	/**
	 * 
	 * @param choices An Arraylist of strings that are to be displayed
	 * @return Returns the value received from user input
	 */
	private int displayOptions(ArrayList<String> choices) {
		int index = 1;
		for (String choice : choices) {
			// Stringbuilder is more efficient
			System.out.println(index + ". " + choice);
			index++;
		}
		return waitForInput();
	}

	/**
	 * 
	 * @return
	 */
	private int waitForInput() {
		System.out.println("");
		System.out.print("Enter Choice: ");
		// Once System.in is closed it cannot be reopened
		try {
			int option = in.nextInt();
			// Needed to digest the enter key from user
			in.nextLine();
			return option;
		} catch (InputMismatchException e) {
			System.out.println("You must enter a number");
			in.nextLine();
			return waitForInput();
		} catch (NoSuchElementException e) {
			System.out.println("Invalid input");
			in.nextLine();
			return waitForInput();
		}

	}

	/**
	 * Called when user chooses the list task option on main menu 
	 */
	void listTasks() {
		int listChoice = -1;
		ArrayList<String> listMenu = new ArrayList<String>(
				Arrays.asList("Completed Tasks", "Pending Tasks", "All Tasks", "Go back"));
		while (listChoice != 0) {
			System.out.println("What type of tasks would you like to list?");
			listChoice = displayOptions(listMenu);
			// Could add conditional logic when tasklist is empty
			// or could be granular and display a info msg when there are no tasks
			if (listChoice == 1) {
				for (Task t : taskList) {
					if (t.getStatus() == Status.COMPLETE) {
						System.out.println(t.toString());
					}
				}
			} else if (listChoice == 2) {
				for (Task t : taskList) {
					if (t.getStatus() == Status.PENDING) {
						System.out.println(t.toString());
					}
				}
			} else if (listChoice == 3) {
				for (Task t : taskList) {
					System.out.println(t.toString());
				}
			} else if (listChoice == 4) {
				listChoice = 0;
			}
			System.out.println("=============================");
		}
		//mainMenu();
		System.out.println();

	}

	/**
	 * 
	 */
	void addTask() {
		int addChoice = 1;
		System.out.println("Would you like to create a new task?: ");
		ArrayList<String> yesNo = new ArrayList<String>(Arrays.asList("Yes", "No"));
		addChoice = displayOptions(yesNo);

		while (addChoice == 1) {
			System.out.println("What would you like to title the new task?: ");
			String title = in.nextLine();
			System.out.println("What is the description of the new task?: ");
			String description = in.nextLine();
			System.out.println("What is the due date of the new task?: ");
			// Add try catch to format the date
			LocalDate date = validateDate();
			Task toAdd = new Task(title, description, date);
			taskList.add(toAdd);
			System.out.println("Task \"" + title + "\" has been added");
			System.out.println("=============================");
			System.out.println("Would you like to add another task?: ");

			addChoice = displayOptions(yesNo);
		}

		//mainMenu();
	}

	/**
	 * 
	 */
	void updateTask() {
		// Task updateThis
		int updateChoice = 1;

		ArrayList<String> printable = stringTaskList;

		while (updateChoice == 1 && taskList.size() > 0) {

			ArrayList<String> yesNo = new ArrayList<String>(Arrays.asList("Yes", "No"));
			System.out.println("Which task would you like to update?");
			// Adjusting index for user readable input
			try {
				int updateIndex = displayOptions(printable) - 1;
				if (updateIndex == -1) {
					break;
				}
				System.out.println(
						"Are you sure you want to change status of the task: \"" + taskList.get(updateIndex) + "\"");
				updateChoice = displayOptions(yesNo);
				if (updateChoice == 1) {

					Task tasktoUpdate = taskList.get(updateIndex);
					Status s = (tasktoUpdate.getStatus() == Status.PENDING) ? Status.COMPLETE : Status.PENDING;
					tasktoUpdate.setStatus(s);
					System.out.println("Task \"" + taskList.get(updateIndex).getTitle() + "\" has been updated to "
							+ s.toString());

				} else {
					System.out.println("Task \"" + taskList.get(updateIndex).getTitle() + "\" has not been updated");
				}
				System.out.println("=============================");
				System.out.println("Would you like to update another task?: ");
			} catch (IndexOutOfBoundsException e) {
				System.out.println("=============================");
				System.out.println(
						"That is not a valid task number please select again, \nor choose zero to return to the main menu");
				System.out.println("=============================");
				continue;
			}

			updateChoice = displayOptions(yesNo);
			// Update the list of current tasks before displaying to user
			updateStringList();
		}

		//mainMenu();

	}

	/**
	 * 
	 */
	void deleteTask() {

		/**
		 * This code block may be able to be separated to a method since it is called a
		 * few times
		 */
		int deleteChoice = 1;

		while (deleteChoice == 1 && taskList.size() > 0) {

			System.out.println("Which task would you like to delete?");
			ArrayList<String> yesNo = new ArrayList<String>(Arrays.asList("Yes", "No"));

			try {
				// Adjusting index for user readable input
				int deleteIndex = displayOptions(stringTaskList) - 1;
				if (deleteIndex == -1) {
					break;
				}
				System.out.println("Are you sure you want to delete task: \"" + taskList.get(deleteIndex) + "\"");
				deleteChoice = displayOptions(yesNo);
				if (deleteChoice == 1) {
					System.out.println("Task \"" + taskList.get(deleteIndex).getTitle() + "\" has been deleted");
					taskList.remove(deleteIndex);
				} else {
					System.out.println("Task \"" + taskList.get(deleteIndex).getTitle() + "\" has not been deleted");
				}
				System.out.println("=============================");
				System.out.println("Would you like to delete another task?: ");
			} catch (IndexOutOfBoundsException e) {
				System.out.println("=============================");
				System.out.println(
						"That is not a valid task number please select again, \nor choose zero to return to the main menu");
				System.out.println("=============================");

				continue;
			}

			deleteChoice = displayOptions(yesNo);

			// Update the list of current tasks before displaying to user
			updateStringList();
		}
		//mainMenu();
	}

	/**
	 * Helper method that ingests and validates if the given date is in the correct
	 * format
	 * 
	 * @return return the given date, formatted correctly
	 */
	private LocalDate validateDate() {
		LocalDate date = null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		while (date == null) {
			System.out.print("Enter a date (dd/MM/yyyy): ");
			String input = in.nextLine();

			try {
				date = LocalDate.parse(input, formatter);
			} catch (DateTimeParseException e) {
				System.out.println("Invalid date format. Please try again.");
			}
		}

		return date;
	}

	private void saveTaskList() {
		// Save taskList to a file

		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("savedTaskList.ser"))) {
			out.writeObject(taskList);
			System.out.println("Tasks saved to file.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadTaskList() {
		System.out.println("Loading Tasks from file");
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("savedTaskList.ser"));
			taskList = (ArrayList<Task>) in.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
