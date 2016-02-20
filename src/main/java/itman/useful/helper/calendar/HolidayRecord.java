package itman.useful.helper.calendar;

public class HolidayRecord {
	private String date;
	private String name;
	private String isHoliday;
	private String holidayCategory;
	private String description;

	public String getDate() {
		return date;
	}

	public String getDescription() {
		return description;
	}

	public String getHolidayCategory() {
		return holidayCategory;
	}

	public String getName() {
		return name;
	}

	public boolean isHoliday() {
		return this.isHoliday.equals("是") ? true : false;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setHolidayCategory(String holidayCategory) {
		this.holidayCategory = holidayCategory;
	}

	public void setIsHoliday(String isHoliday) {
		this.isHoliday = isHoliday;
	}

	public void setName(String name) {
		this.name = name;
	}
}
