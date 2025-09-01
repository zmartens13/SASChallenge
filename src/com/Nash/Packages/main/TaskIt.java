package com.Nash.Packages.main;

public class TaskIt {

	private static int SAFE_EXIT = 0;

	public static void main(String[] args) {

		TaskManager manager = new TaskManager();

		manager.start();

		int selection = 0;
		
		while (selection != 5) {
			// TODO Make an if then structure, the switch statement has unintended
			// consequences
			selection = manager.mainMenu();
			if (selection == 1) {
				manager.addTask();
			} else if (selection == 2) {
				manager.listTasks();
			} else if (selection == 3) {
				manager.updateTask();
			} else if (selection == 4) {
				manager.deleteTask();
			} else if (selection == 5) {
				System.out.println("Goodbye");
				System.exit(SAFE_EXIT);
			} else {
				System.out.println("Please select a listed option");
			}
		}
	}

}