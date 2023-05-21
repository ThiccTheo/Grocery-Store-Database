import java.util.Scanner;
import java.util.ArrayList;
import java.util.function.Predicate;

public class Main {
	private enum ComparisonOperator {
		Lt, // less than
		Le, // less than or equal to
		Gt, // greater than
		Ge, // greater than or equal to
		Eq, // equal to
		Ne, // not equal to
		UnInit, // uninitialized (for "var" usage)
	}
	
	public static void main(String[] args) {
		var scan = new Scanner(System.in);
		var store = new Store();

		while (store.getIsOpen()) {
			System.out.print("\033[H\033[J");
			System.out.println(store);
			System.out.println("===== Menu =====\n");
			System.out.println(store.options());
			System.out.print("Option: ");
			var opt = scan.nextInt();
			scan.nextLine();
			System.out.print("\033[H\033[J");
			System.out.println(store);
			
			switch (opt) {
			case Store.ADD_OPTION:
				System.out.println("===== Add grocery(ies) =====\n");
				System.out.print("Grocery brand (ex. Nestle): ");
				var brand = scan.nextLine();
					
				System.out.print("\nGrocery name (ex. Crunch): ");
				var name = scan.nextLine();

				System.out.print("\nGrocery price (no $ sign): ");
				var price = scan.nextDouble();

				System.out.print("\nHow many to add: ");
				var count = scan.nextInt();
					
				store.addGroceries(new Grocery(brand, name, price), count);
					
				System.out.print("\nGrocery(ies) added!\n\n<Q> Back to menu: ");
				var quit = scan.nextLine().toUpperCase();

				while (!quit.equals("Q")) quit = scan.nextLine().toUpperCase();
				
				break;
			case Store.REMOVE_OPTION:
				System.out.println("===== Remove grocery(ies) =====\n");
				System.out.print("Grocery ID: ");
				var id = scan.nextInt();

				System.out.print("\nHow many to remove: ");
				count = scan.nextInt();

				store.removeGroceries(id, count);

				System.out.print("\nGrocery(ies) removed!\n\n<Q> Back to menu: ");
				quit = scan.nextLine().toUpperCase();

				while (!quit.equals("Q")) quit = scan.nextLine().toUpperCase();
					
				break;
			case Store.QUERY_OPTION:
				System.out.println("===== Query groceries =====\n");

				/*
				var keywords = "Keywords:\n"
							 + "ID\n"
							 + "BRAND\n"
							 + "NAME\n"
							 + "PRICE\n"
							 + "STOCK\n"
							 + "LT (less than)\n"
							 + "LE (less than or equal to)\n"
							 + "GT (greater than)\n"
							 + "GE (greater than or equal to)\n"
							 + "EQ (equal to)\n"
							 + "NE (not equal to)\n"
							 + "EXIT (end query chain)\n";
				*/
					
				//System.out.println(keywords);
					
				var groceries = store.getGroceries();
					
				System.out.print("Query for: ");
				var in = scan.nextLine();

				while (!in.equals("EXIT")) {
					var regex = "LT|LE|GT|GE|EQ|NE";
					var tokens = in.split(regex);
					var lhs = tokens[0].trim();
					var rhs = tokens[1].trim();
					var op = ComparisonOperator.UnInit;
					
					if (in.contains("LT")) op = ComparisonOperator.Lt;
					else if (in.contains("LE")) op = ComparisonOperator.Le;
					else if (in.contains("GT")) op = ComparisonOperator.Gt;
					else if (in.contains("GE")) op = ComparisonOperator.Ge;
					else if (in.contains("EQ")) op = ComparisonOperator.Eq;
					else if (in.contains("NE")) op = ComparisonOperator.Ne;

					if (lhs.equals("ID")) {
						var tmpId = Integer.parseInt(rhs);
						
						if (op == ComparisonOperator.Lt) groceries = query(groceries, grocery -> grocery.getId() < tmpId);
						else if (op == ComparisonOperator.Le) groceries = query(groceries, grocery -> grocery.getId() <= tmpId);
						else if (op == ComparisonOperator.Gt) groceries = query(groceries, grocery -> grocery.getId() > tmpId);
						else if (op == ComparisonOperator.Ge) groceries = query(groceries, grocery -> grocery.getId() >= tmpId);
						else if (op == ComparisonOperator.Eq) groceries = query(groceries, grocery -> grocery.getId() == tmpId);
						else if (op == ComparisonOperator.Ne) groceries = query(groceries, grocery -> grocery.getId() != tmpId);
						
					} else if (lhs.equals("BRAND"))
						groceries = op == ComparisonOperator.Eq 
							? query(groceries, grocery -> grocery.getBrand().equals(rhs)) 
							: query(groceries, grocery -> !grocery.getBrand().equals(rhs));
						
					else if (lhs.equals("NAME"))
						groceries = op == ComparisonOperator.Eq 
							? query(groceries, grocery -> grocery.getName().equals(rhs)) 
							: query(groceries, grocery -> !grocery.getName().equals(rhs));
						
					else if (lhs.equals("PRICE")) {
						var cost = Double.parseDouble(rhs);
						
						if (op == ComparisonOperator.Lt) groceries = query(groceries, grocery -> grocery.getPrice() < cost);
						else if (op == ComparisonOperator.Le) groceries = query(groceries, grocery -> grocery.getPrice() <= cost);
						else if (op == ComparisonOperator.Gt) groceries = query(groceries, grocery -> grocery.getPrice() > cost);
						else if (op == ComparisonOperator.Ge) groceries = query(groceries, grocery -> grocery.getPrice() >= cost);
						else if (op == ComparisonOperator.Eq) groceries = query(groceries, grocery -> grocery.getPrice() == cost);
						else if (op == ComparisonOperator.Ne) groceries = query(groceries, grocery -> grocery.getPrice() != cost);
						
					} else if (lhs.equals("STOCK")) {
						var stock = Integer.parseInt(rhs);
						
						if (op == ComparisonOperator.Lt) groceries = query(groceries, grocery -> store.getCount(grocery) < stock);
						else if (op == ComparisonOperator.Le) groceries = query(groceries, grocery -> store.getCount(grocery) <= stock);
						else if (op == ComparisonOperator.Gt) groceries = query(groceries, grocery -> store.getCount(grocery) > stock);
						else if (op == ComparisonOperator.Ge) groceries = query(groceries, grocery -> store.getCount(grocery) >= stock);
						else if (op == ComparisonOperator.Eq) groceries = query(groceries, grocery -> store.getCount(grocery) == stock);
						else if (op == ComparisonOperator.Ne) groceries = query(groceries, grocery -> store.getCount(grocery) != stock);
					}

					System.out.print("...");
					in = scan.nextLine();
				}

				System.out.print("\nQuery result: ");

				if (groceries.size() > 0) {
					System.out.println("\n[");
					
					for (var grocery : groceries) 
						System.out.println("\tID: "
										   + grocery.getId() 
										   + " | " 
										   + grocery.toString() 
										   + " | Stock: " 
										   + store.getCount(grocery) 
										   + ",");
					
					System.out.println("]\n");
				} else
					System.out.println("[]\n");
				System.out.print("Groceries queried!\n\n<Q> Back to menu: ");
				quit = scan.nextLine().toUpperCase();

				while (!quit.equals("Q")) quit = scan.nextLine().toUpperCase();
				
				break;
			case Store.SORT_OPTION:
				System.out.println("===== Sort groceries =====\n");
				System.out.print("ASCENDING or DESCENDING order: ");
				var ord = scan.nextLine();
					
				System.out.print("\nSort by (ID/BRAND/NAME/PRICE/STOCK): ");
				var filter = scan.nextLine();
					
				sort(store.getGroceries(), store, filter, ord);
					
				System.out.print("\nGroceries sorted!\n\n<Q> Back to menu: ");
				quit = scan.nextLine().toUpperCase();

				while (!quit.equals("Q")) quit = scan.nextLine().toUpperCase();
					
				break;
			case Store.CLOSE_OPTION:
				store.setIsOpen(false);
				
				break;
			}
		}

		System.out.print("\033[H\033[J");
		scan.close();
	}

	public static ArrayList<Grocery> query(ArrayList<Grocery> groceries, Predicate<Grocery> filter) {
		var out = new ArrayList<Grocery>();
		
		for (var grocery : groceries) 
			if (filter.test(grocery)) out.add(grocery);
		
		return out;
	}

	// Extremely messy and unoptimized code; I didn't feel like refactoring this
	public static void sort(ArrayList<Grocery> groceries, Store store, String filter, String ord) {
		filter = filter.toUpperCase();
		ord = ord.toUpperCase();
		var swaps = 0;
		
		if (ord.equals("ASCENDING")) {
			if (filter.equals("ID"))
				while (true) {
					swaps = 0;
					for (var i = 0; i < groceries.size() - 1; ++i)
						if (groceries.get(i).getId() > groceries.get(i + 1).getId()) {
							var tmp = groceries.get(i);
							groceries.set(i, groceries.get(i + 1));
							groceries.set(i + 1, tmp);
							++swaps;
						}
					
					if (swaps == 0)
						break;
				}
			else if (filter.equals("BRAND"))
				while (true) {
					swaps = 0;
					for (var i = 0; i < groceries.size() - 1; ++i)
						if (groceries.get(i).getBrand().compareTo(groceries.get(i + 1).getBrand()) > 0) {
							var tmp = groceries.get(i);
							groceries.set(i, groceries.get(i + 1));
							groceries.set(i + 1, tmp);
							++swaps;
						}
					
					if (swaps == 0)
						break;
				}
			else if (filter.equals("NAME"))
				while (true) {
					swaps = 0;
					for (var i = 0; i < groceries.size() - 1; ++i)
						if (groceries.get(i).getName().compareTo(groceries.get(i + 1).getName()) > 0) {
							var tmp = groceries.get(i);
							groceries.set(i, groceries.get(i + 1));
							groceries.set(i + 1, tmp);
							++swaps;
						}
					
					if (swaps == 0)
						break;
				}
			else if (filter.equals("PRICE"))
				while (true) {
					swaps = 0;
					for (var i = 0; i < groceries.size() - 1; ++i)
						if (groceries.get(i).getPrice() > groceries.get(i + 1).getPrice()) {
							var tmp = groceries.get(i);
							groceries.set(i, groceries.get(i + 1));
							groceries.set(i + 1, tmp);
							++swaps;
						}
					
					if (swaps == 0)
						break;
				}
			else if (filter.equals("STOCK"))
				while (true) {
					swaps = 0;
					for (var i = 0; i < groceries.size() - 1; ++i)
						if (store.getCount(groceries.get(i)) > store.getCount(groceries.get(i + 1))) {
							var tmp = groceries.get(i);
							groceries.set(i, groceries.get(i + 1));
							groceries.set(i + 1, tmp);
							++swaps;
						}
					
					if (swaps == 0)
						break;
				}
		} else if (ord.equals("DESCENDING")) {
			if (filter.equals("ID"))
				while (true) {
					swaps = 0;
					for (var i = 0; i < groceries.size() - 1; ++i)
						if (groceries.get(i).getId() < groceries.get(i + 1).getId()) {
							var tmp = groceries.get(i);
							groceries.set(i, groceries.get(i + 1));
							groceries.set(i + 1, tmp);
							++swaps;
						}
					
					if (swaps == 0)
						break;
				}
			else if (filter.equals("BRAND"))
				while (true) {
					swaps = 0;
					for (var i = 0; i < groceries.size() - 1; ++i)
						if (groceries.get(i).getBrand().compareTo(groceries.get(i + 1).getBrand()) < 0) {
							var tmp = groceries.get(i);
							groceries.set(i, groceries.get(i + 1));
							groceries.set(i + 1, tmp);
							++swaps;
						}
					
					if (swaps == 0)
						break;
				}
			else if (filter.equals("NAME"))
				while (true) {
					swaps = 0;
					for (var i = 0; i < groceries.size() - 1; ++i)
						if (groceries.get(i).getName().compareTo(groceries.get(i + 1).getName()) < 0) {
							var tmp = groceries.get(i);
							groceries.set(i, groceries.get(i + 1));
							groceries.set(i + 1, tmp);
							++swaps;
						}
					
					if (swaps == 0)
						break;
				}
			else if (filter.equals("PRICE"))
				while (true) {
					swaps = 0;
					for (var i = 0; i < groceries.size() - 1; ++i)
						if (groceries.get(i).getPrice() < groceries.get(i + 1).getPrice()) {
							var tmp = groceries.get(i);
							groceries.set(i, groceries.get(i + 1));
							groceries.set(i + 1, tmp);
							++swaps;
						}
					
					if (swaps == 0)
						break;
				}
			else if (filter.equals("STOCK"))
				while (true) {
					swaps = 0;
					for (var i = 0; i < groceries.size() - 1; ++i)
						if (store.getCount(groceries.get(i)) < store.getCount(groceries.get(i + 1))) {
							var tmp = groceries.get(i);
							groceries.set(i, groceries.get(i + 1));
							groceries.set(i + 1, tmp);
							++swaps;
						}
					
					if (swaps == 0)
						break;
				}
		}
	}
}