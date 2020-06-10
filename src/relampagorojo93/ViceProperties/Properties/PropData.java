package relampagorojo93.ViceProperties.Properties;

import org.bukkit.Location;

public class PropData {
	private String name;
	private int price;
	private int chests;
	private int membersmax;
	private Location home;
	public PropData(String name, int price, int chests, int membersmax, Location home) {
		this.name = name;
		this.price = price;
		this.chests = chests;
		this.membersmax = membersmax;
		this.home = home;
	}
	public String name() {
		return name;
	}
	public int price() {
		return price;
	}
	public int chests() {
		return chests;
	}
	public int membersmax() {
		return membersmax;
	}
	public Location home() {
		return home;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public void addChest() {
		chests++;
	}
	public void remChest() {
		chests--;
	}
	public void addMemberMax() {
		membersmax++;
	}
	public void remMemberMax() {
		membersmax--;
	}
	public void setHome(Location home) {
		this.home = home;
	}
}
