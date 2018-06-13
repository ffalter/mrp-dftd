package controls;

import simulation.Environment;

/**
 * "static" class to parse ticks in time
 * @author Florian
 *
 */

public class TickParser {
	
	/**
	 * 
	 * @param ticks
	 * @return how many ticks are equal one day
	 */
	public static double getTicksPerDay ()
	{
		int ticksPerYear = Environment.getInstance().getTicksPerYear();
		double ticksPerDay = ticksPerYear/365.;
		
		return ticksPerDay;
	}
	
	
	/**
	 * 
	 * @param ticks
	 * @return how many ticks are equal one year
	 */
	public static double getTicksPerYear ()
	{
		return Environment.getInstance().getTicksPerYear();
	}
	
	
	
	/**
	 * 
	 * @param ticks
	 * @return exact amount of days from ticks with decimals
	 */
	public static double getExactDaysFromTicks (int ticks) 
	{
		double ticksPerDay = getTicksPerDay();
		
		double days = ticks/ticksPerDay;
		return days;
	}
	
	/**
	 * 
	 * @param ticks
	 * @return amount of completed days from ticks
	 */
	public static int getCompleteDaysFromTicks (int ticks) 
	{
		double ticksPerDay = getTicksPerDay();
		
		int days = (int)Math.floor(ticks/ticksPerDay);
		return days;
	}
	
	/**
	 * 
	 * @param ticks
	 * @return day in year from 1 to 365
	 */
	public static int getDayInYear (int ticks)
	{
		int days = getCompleteDaysFromTicks(ticks);
		// last day of year would return 0 instead of 365
		if(days%365 == 0)
			return 365;
		return days%365;
	}
	
	/**
	 * 
	 * @param ticks
	 * @return if tick is specified mating season
	 */
	public static boolean isTickInMatingSeason (int ticks)
	{
		int matingStart = Environment.getInstance().getMatingSeasonStartDay();
		int matingEnd = Environment.getInstance().getMatingSeasonEndDay();
		
		int day = getDayInYear(ticks);
		if (matingStart > matingEnd && day < matingStart && day > matingEnd) {
			return false;
		} else if(matingStart < matingEnd && (day > matingEnd || day < matingStart)) {
			return false;
		}
		return true;
	}
	
	public static int getYearsFromTicks (int ticks) {
		double years = ticks/getTicksPerYear();
		return (int)Math.floor(years);
	}
	
	public static int getTicksFromDays (int days) {
		return (int)Math.floor(days * getTicksPerDay());
	}

}
