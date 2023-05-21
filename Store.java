import java.util.ArrayList;
import java.util.HashMap;

public class Store {
	public Store() {
		columnWidths = new int[] {
			HEADERS[ID_IDX].length(),
			HEADERS[BRAND_IDX].length(),
			HEADERS[NAME_IDX].length(),
			HEADERS[PRICE_IDX].length(),
			HEADERS[STOCK_IDX].length(),
		};

		isOpen = true;
		nextId = 1;
		groceries = new ArrayList<Grocery>();
		inventory = new HashMap<String, Integer>();
	}

	public boolean getIsOpen() { return isOpen; }
	public void setIsOpen(boolean isOpen) { this.isOpen = isOpen; }

	private void addGrocery(Grocery grocery) {
		if (!inventory.containsKey(grocery.toString())) {
			grocery.setId(nextId);
			++nextId;
			
			inventory.put(grocery.toString(), 1);
			groceries.add(grocery);
		} else
			inventory.put(grocery.toString(), inventory.get(grocery.toString()) + 1);
	}

	public void addGroceries(Grocery grocery, int count) {
		for (var i = 0; i < count; ++i) addGrocery(grocery);
		
		resizeTable();
	}

	private void removeGrocery(int id) {
		for (var i = 0; i < groceries.size(); ++i)
			if (groceries.get(i).getId() == id) {
				var grocery = groceries.remove(i);
				inventory.put(grocery.toString(), inventory.get(grocery.toString()) - 1);
				if (inventory.get(grocery.toString()) <= 0)
					inventory.remove(grocery.toString());
				else
					groceries.add(grocery);
				break;
			}
	}
	
	public void removeGroceries(int id, int count) {
		for (var i = 0; i < count; ++i) removeGrocery(id);
		
		resizeTable();
	}

	public void resizeTable() {
		columnWidths = new int[] {
			HEADERS[ID_IDX].length(),
			HEADERS[BRAND_IDX].length(),
			HEADERS[NAME_IDX].length(),
			HEADERS[PRICE_IDX].length(),
			HEADERS[STOCK_IDX].length(),
		};

		for (var grocery : groceries) {
			var id = String.format("%d", grocery.getId());
			var brand = grocery.getBrand();
			var name = grocery.getName();
			var price = String.format("$%.2f", grocery.getPrice());
			var stock = String.format("%d", inventory.get(grocery.toString()));

			if (id.length() > columnWidths[ID_IDX]) columnWidths[ID_IDX] = id.length();
			if (brand.length() > columnWidths[BRAND_IDX]) columnWidths[BRAND_IDX] = brand.length();
			if (name.length() >  columnWidths[NAME_IDX]) columnWidths[NAME_IDX] = name.length();
			if (price.length() > columnWidths[PRICE_IDX]) columnWidths[PRICE_IDX] = price.length();
			if (stock.length() > columnWidths[STOCK_IDX]) columnWidths[STOCK_IDX] = stock.length();
		}
	}

	public String options() {
		return "<1> Add grocery(ies)\n\n"
			 + "<2> Remove grocery(ies)\n\n"
			 + "<3> Query groceries\n\n"
			 + "<4> Sort gorceries\n\n"
			 + "<5> Close store\n";
	}

	public int getCount(Grocery grocery) {
		return inventory.get(grocery.toString());
	}

	public ArrayList<Grocery> getGroceries() { return groceries; }

	public String toString() {
		var table = "";
		
		for (var columnWidth : columnWidths) {
			table += "+";
			
			for (var i = 0; i < columnWidth + 2; ++i) table += "-";
		}
		table += "+\n";
		
		for (var i = 0; i < HEADERS.length; ++i) {
			table += "| " + HEADERS[i] + " ";
			
			for (var j = 0; j < columnWidths[i] - HEADERS[i].length(); ++j) table += " ";
		}
		table += "|\n";

		for (var columnWidth : columnWidths) {
			table += "+";
			
			for (var i = 0; i < columnWidth + 2; ++i) table += "-";
		}
		table += "+\n";

		for (var grocery : groceries) {
			var id = String.format("%d", grocery.getId());
			table += "| " + id + " ";
			
			for (var i = 0; i < columnWidths[ID_IDX] - id.length(); ++i) table += " ";
		
			var brand = grocery.getBrand();
			table += "| " + brand + " ";
			
			for (var i = 0; i < columnWidths[BRAND_IDX] - brand.length(); ++i) table += " ";
			
			var name = grocery.getName();
			table += "| " + name + " ";

			for (var i = 0; i < columnWidths[NAME_IDX] - name.length(); ++i) table += " ";
			
			var price = String.format("$%.2f", grocery.getPrice());
			table += "| " + price + " ";

			for (var i = 0; i < columnWidths[PRICE_IDX] - price.length(); ++i) table += " ";
			
			var stock = String.format("%d", inventory.get(grocery.toString()));
			table += "| " + stock + " ";

			for (var i = 0; i < columnWidths[STOCK_IDX] - stock.length(); ++i) table += " ";
			
			table += "|\n";
		}

		for (var columnWidth : columnWidths) {
			table += "+";
			
			for (var i = 0; i < columnWidth + 2; ++i) table += "-";
		}
		table += "+\n";

		return table;
	}
	
	private static final String[] HEADERS = {"ID", "Brand", "Name", "Price", "Stock"};
	private static final int ID_IDX = 0;
	private static final int BRAND_IDX = 1;
	private static final int NAME_IDX = 2;
	private static final int PRICE_IDX = 3;
	private static final int STOCK_IDX = 4;
	public static final int ADD_OPTION = 1;
	public static final int REMOVE_OPTION = 2;
	public static final int QUERY_OPTION = 3;
	public static final int SORT_OPTION = 4;
	public static final int CLOSE_OPTION = 5;
	private boolean isOpen;
	private int[] columnWidths;
	private int nextId;
	private ArrayList<Grocery> groceries;
	private HashMap<String, Integer> inventory;
}