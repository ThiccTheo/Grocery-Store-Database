public class Grocery {
	public Grocery(String brand, String name, double price) {
		this.id = 0;
		this.brand = brand;
		this.name = name;
		this.price = price;
	}

	public String getBrand() { return brand; }
	public void setBrand(String brand) { this.brand = brand; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public double getPrice() { return price; }
	public void setPrice(double price) { this.price = price; }

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public String toString() {
		return String.format("Brand: %s | Name: %s | Price: $%.2f", brand, name, price);
	}

	private int id;
	private String brand;
	private String name;
	private double price;
}