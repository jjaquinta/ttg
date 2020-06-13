/*
 * Created on Sep 25, 2005
 *
 */
package jo.ttg.utils;

public class ConvUtils
{
    public static final double G = 6.67e-11; // gravitational constant
    public static final double EarthMassInKG = 5.9763e24;
    public static final double EarthRadiusInKM = 6371;
    
    // conversion routines
    // MT = metric tons, SM = solar masses, Kg = Kilo Grams

    /**
     * Convert Astronomical Units to Kilometers.
     * @param AU
     * @return double
     */
    public static double convAUToKm(double AU)
    {
        return (149600000.0 * AU);
    }
    /**
     * Convert Astronomical Unots to Meters.
     * @param AU
     * @return double
     */
    public static double convAUToM(double AU)
    {
        return (convAUToKm(AU) * 1000.0);
    }
    /**
     * Convert Astronomical Units to Miles.
     * @param AU
     * @return double
     */
    public static double convAUToMiles(double AU)
    {
        return convKmToMiles(convAUToKm(AU));
    }
    /**
     * Convert days to hours.
     * @param days
     * @return double
     */
    public static double convDaysToHours(double days)
    {
        return days * 24;
    }
    /**
     * Convert days to seconds.
     * @param days
     * @return double
     */
    public static double convDaysToSeconds(double days)
    {
        return convHoursToSeconds(convDaysToHours(days));
    }
    /**
     * Convert Hours To Days.
     * @param hours
     * @return double
     */
    public static double convHoursToDays(double hours)
    {
        return hours / 24;
    }
    /**
     * Convert hours to seconds.
     * @param hours
     * @return double
     */
    public static double convHoursToSeconds(double hours)
    {
        return hours * 60 * 60;
    }
    /**
     * Convert kilograms to metric tons.
     * @param Kg
     * @return double
     */
    public static double convKgToMT(double Kg)
    {
        return (Kg / 1000.0);
    }
    /**
     * Convert kilograms to solar masses.
     * @param Kg
     * @return double
     */
    public static double convKgToSM(double Kg)
    {
        return convMTToSM(convKgToMT(Kg));
    }
    /**
     * Convert kilometers to astronomical units.
     * @param KM
     * @return double
     */
    public static double convKmToAU(double KM)
    {
        return (KM / 149600000.0);
    }
    /**
     * Convert kilometers to miles.
     * @param KM
     * @return double
     */
    public static double convKmToMiles(double KM)
    {
        return (KM / 1.6);
    }
    /**
     * Convert miles to Astronomical units.
     * @param Miles
     * @return double
     */
    public static double convMilesToAU(double Miles)
    {
        return convKmToAU(convMilesToKm(Miles));
    }
    /**
     * Convert miles to kilometers.
     * @param Miles
     * @return double
     */
    public static double convMilesToKm(double Miles)
    {
        return (Miles * 1.6);
    }
    /**
     * Convert metric tons to kilograms.
     * @param MT
     * @return double
     */
    public static double convMTToKg(double MT)
    {
        return (MT * 1000.0);
    }
    /**
     * Convert metric tons to solar masses.
     * @param MT
     * @return double
     */
    public static double convMTToSM(double MT)
    {
        return (MT / 1.939e27);
    }
    /**
     * Convert seconds to days.
     * @param sec
     * @return double
     */
    public static double convSecondsToDays(double sec)
    {
        return convHoursToDays(convSecondsToHours(sec));
    }
    /**
     * Convert seconds to hours.
     * @param sec
     * @return double
     */
    public static double convSecondsToHours(double sec)
    {
        return sec / 60 / 60;
    }
    /**
     * Convert solar masses to kilograms.
     * @param SM
     * @return double
     */
    public static double convSMToKg(double SM)
    {
        return convMTToKg(convSMToMT(SM));
    }
    /**
     * Solar masses to metric tons.
     * @param SM
     * @return double
     */
    public static double convSMToMT(double SM)
    {
        return (SM * 1.939e27);
    }
    /**
     * KGs to Jupiter Masses.
     * @param kg
     * @return double
     */
    public static double convKGToJM(double kg)
    {
        return kg/1.8986e27;
    }
    /**
     * KGs to Earth Masses.
     * @param kg
     * @return double
     */
    public static double convKGToEM(double kg)
    {
        return kg/5.972E24;
    }
    /**
     * KGs to Lunar Masses.
     * @param kg
     * @return double
     */
    public static double convKGToLM(double kg)
    {
        return kg/7.34767309E22;
    }

}
