/*
 * TTempdata.java   3.0 01 Jul 2001   Joseph Jaquinta
 *
 * Copyright (c) 1997,2001,2004 Joseph Jaquinta
 *
 * Permission to use, copy, modify, and distribute this software
 * and its documentation for NON-COMMERCIAL or COMMERCIAL purposes and
 * without fee is hereby granted provided that this copyright notice
 * appears in all copies.
 *
 * I MAKE NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. I SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 * */
package jo.ttg.beans.sys;
import jo.util.beans.Bean;
import jo.util.utils.MathUtils;
/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TemperatureBean extends Bean
{
	public static final double FREEZING = 273.0;
	
	private double[] mRowTemp;
	private double[] mRowTilt;
	private double mSummer;
	private double mWinter;
	private double mDay;
	private double mNight;
	private double mBodyYear;
	private double mBodyDay;

	public TemperatureBean(BodyWorldBean b)
	{
		mRowTemp = new double[12];
		mRowTilt = new double[12];
		int i, j, mult;
		double tmp, pph, pmax, mph, mmax, tilt;
		double Temp;
		mBodyYear = b.getOrbitalPeriod();
		mBodyDay = b.getStatsSiz().getDay();
		mSummer = b.getTilt() * 0.6;
		mWinter = -b.getTilt();
		if ((b.getPrimary() != null) && (b.getPrimary().getType() == BodyBean.BT_STAR))
		{
			tmp = ((BodyStarBean) b.getPrimary()).getLuminosity();
			tmp /= Math.sqrt(b.getOrbitalRadius());
		}
		else if (
			(b.getPrimary() != null) && (b.getPrimary().getPrimary() != null) && (b.getPrimary().getPrimary().getType() == BodyBean.BT_STAR))
		{
			tmp = ((BodyStarBean) b.getPrimary().getPrimary()).getLuminosity();
			tmp /= Math.sqrt(b.getPrimary().getOrbitalRadius());
		}
		else
		{
			System.err.println(
				"Body "
					+ b.toString()
					+ " has not parent or grandparent star.");
			if (b.getPrimary() != null)
			{
			    System.err.println("Primary "+b.getPrimary().getName()+" is of type "+b.getPrimary().getType());
			    if (b.getPrimary().getPrimary() != null)
				    System.err.println("Primary "+b.getPrimary().getPrimary().getName()+" is of type "+b.getPrimary().getPrimary().getType());
			}
			else
			    System.err.println("No primary!");
			System.exit(1);
			return;
		}
		Temp = b.getTemperature();
		switch (b.getPopulatedStats().getUPP().getAtmos().getValue())
		{
			case 0 :
				pph = 1.0 * tmp;
				pmax = Temp * 0.1 * tmp;
				mph = 20.0;
				mmax = Temp * 0.80;
				break;
			case 1 :
				pph = 0.9 * tmp;
				pmax = Temp * 0.3 * tmp;
				mph = 15.0;
				mmax = Temp * 0.70;
				break;
			case 2 :
			case 3 :
			case 0x0f :
				pph = 0.8 * tmp;
				pmax = Temp * 0.8 * tmp;
				mph = 8.0;
				mmax = Temp * 0.50;
				break;
			case 4 :
			case 5 :
				pph = 0.6 * tmp;
				pmax = Temp * 1.5 * tmp;
				mph = 3.0;
				mmax = Temp * 0.30;
				break;
			case 6 :
			case 7 :
				pph = 0.5 * tmp;
				pmax = Temp * 2.5 * tmp;
				mph = 1.0;
				mmax = Temp * 0.15;
				break;
			case 8 :
			case 9 :
				pph = 0.4 * tmp;
				pmax = Temp * 4.0 * tmp;
				mph = 0.5;
				mmax = Temp * 0.10;
				break;
			default :
				pph = 0.2 * tmp;
				pmax = Temp * 5.0 * tmp;
				mph = 0.2;
				mmax = Temp * 0.05;
				break;
		}
		mDay = pph * mBodyDay / 2.0;
		if (mDay > pmax)
			mDay = pmax;
		mNight = mph * mBodyDay / 2.0;
		if (mNight > mmax)
			mNight = mmax;
		mNight = -mNight;
		if (b.getPopulatedStats().getUPP().getSize().getValue() <= 10)
			mult = b.getPopulatedStats().getUPP().getSize().getValue() / 2 + 3;
		else
			mult = 8;
		tilt = b.getTilt();
		tilt += b.getPrimary().getTilt();
		if (tilt > 90.0)
			tilt = 180.0 - tilt;
		for (i = 0; i < 12; i++)
		{
			if (b.getTilt() == 0)
				j = i - 10;
			else if (b.getTilt() == 1)
				j = i - 9;
			else if ((b.getTilt() >= 2) && (b.getTilt() <= 3))
				j = i - 8;
			else if ((b.getTilt() >= 4) && (b.getTilt() <= 5))
				j = i - 7;
			else if ((b.getTilt() >= 6) && (b.getTilt() <= 8))
				j = i - 6;
			else if ((b.getTilt() >= 9) && (b.getTilt() <= 12))
				j = i - 5;
			else if ((b.getTilt() >= 13) && (b.getTilt() <= 16))
				j = i - 4;
			else if ((b.getTilt() >= 17) && (b.getTilt() <= 22))
				j = i - 3;
			else if ((b.getTilt() >= 23) && (b.getTilt() <= 28))
				j = i - 2;
			else if ((b.getTilt() >= 29) && (b.getTilt() <= 34))
				j = i - 1;
			else if ((b.getTilt() >= 35) && (b.getTilt() <= 44))
				j = i - 0;
			else if ((b.getTilt() >= 45) && (b.getTilt() <= 59))
				j = i + 1;
			else if ((b.getTilt() >= 60) && (b.getTilt() <= 84))
				j = i + 2;
			else //if (b.getTilt() >= 85)
				j = i + 3;
			if (j <= 0)
				tmp = 0.0;
			else if (j >= 4)
				tmp = 1.0;
			else
				tmp = j * 0.25;
			mRowTilt[i] = tmp;
			mRowTemp[i] = Temp + mult * (3 - i);
		}
	}
	
	private double getLattitudeTilt(double latt)
	{
		double row = MathUtils.interpolate(Math.abs(latt), 0, 90, 0, 11);
		return MathUtils.interpolate(row, mRowTilt);
	}
	
	private double getLattitudeTemp(double latt)
	{
		double row = MathUtils.interpolate(Math.abs(latt), 0, 90, 0, 11);
		return MathUtils.interpolate(row, mRowTemp);
	}
	
	public static final int TD_AVG = 0x00;
	public static final int TD_SUMMER = 0x01;
	public static final int TD_WINTER = 0x02;
	public static final int TD_DAY = 0x04;
	public static final int TD_NIGHT = 0x08;
	public static final int TD_CENTIGRADE = 0x20;
	private static final int TD_SEASON = 0x03;
	private static final int TD_DIURNAL = 0x0c;
	double tempAt(double latt)
	{
		return tempAt(latt, TD_CENTIGRADE);
	}
	double tempAt(double latt, int mode)
	{
		double temp;
		switch (mode & TD_SEASON)
		{
			case TD_SUMMER :
				temp = getLattitudeTemp(latt) + getLattitudeTilt(latt) * mSummer;
				break;
			case TD_WINTER :
				temp = getLattitudeTemp(latt) + getLattitudeTilt(latt) * mWinter;
				break;
			default :
				temp = getLattitudeTemp(latt);
				break;
		}
		switch (mode & TD_DIURNAL)
		{
			case TD_DAY :
				temp += mDay;
				break;
			case TD_NIGHT :
				temp += mNight;
				break;
			default : /* average */
				break;
		}
		if (temp < 0.0)
			temp = 0.0;
		if ((mode & TD_CENTIGRADE) != 0)
			temp -= FREEZING;
		return (temp);
	}
	private static double between(double pc, double low, double high)
	{
		return (high + low) / 2 + (high - low) / 2 * Math.cos(pc * 2 * 3.14159);
	}
	private static double between(
		double tick,
		double period,
		double low,
		double high)
	{
		return between(Math.floor(tick / period), low, high);
	}
	public double tempNowAt(double latt, long date)
	{
		return tempNowAt(latt, date, mBodyDay * .25);
	}
	public double tempNowAt(double latt, long date, double time)
	{
		return getLattitudeTemp(latt)
			+ between(
				(double) date,
				mBodyYear,
				getLattitudeTilt(latt) * mWinter,
				getLattitudeTilt(latt) * mSummer)
			+ between(time, mBodyDay, mNight, mDay);
	}
	public double tempNowAtPC(double latt, double date)
	{
		return tempNowAtPC(latt, date, .25);
	}
	public double tempNowAtPC(double latt, double date, double time)
	{
		return getLattitudeTemp(latt)
			+ between(date, getLattitudeTilt(latt) * mWinter, getLattitudeTilt(latt) * mSummer)
			+ between(time, mNight, mDay);
	}
	public double lowest(double latt)
	{
		return tempAt(latt, TD_WINTER | TD_NIGHT);
	}
	public double highest(double latt)
	{
		return tempAt(latt, TD_SUMMER | TD_DAY);
	}
	public double getFrostLine()
	{
		double lattLow = 0;
		double lattHigh = 90;
		double lattMid;
		if (lowest(lattHigh) >= FREEZING)
			return lattHigh;
		if (lowest(lattLow) <= FREEZING)
			return lattLow;
		for (int i = 0; i < 8; i++)
		{
			lattMid = (lattLow + lattHigh)/2;
			double temp = lowest(lattMid);
			if (temp < FREEZING)
				lattHigh = lattMid;
			else if (temp > FREEZING)
				lattLow = lattMid;
			else
				break;
		}
		return (lattLow + lattHigh)/2;
	}
	public double getPermafrostLine()
	{
		double lattLow = 0;
		double lattHigh = 90;
		double lattMid;
		if (highest(lattHigh) >= FREEZING)
			return lattHigh;
		if (highest(lattLow) <= FREEZING)
			return lattLow;
		for (int i = 0; i < 8; i++)
		{
			lattMid = (lattLow + lattHigh)/2;
			double temp = highest(lattMid);
			if (temp < FREEZING)
				lattHigh = lattMid;
			else if (temp > FREEZING)
				lattLow = lattMid;
			else
				break;
		}
		return (lattLow + lattHigh)/2;
	}
	public double getTempLine(double targetTemp)
	{
		double lattLow = 0;
		double lattHigh = 90;
		double lattMid;
		if (tempAt(lattHigh, TD_CENTIGRADE) >= targetTemp)
			return lattHigh;
		if (tempAt(lattLow, TD_CENTIGRADE) <= targetTemp)
			return lattLow;
		for (int i = 0; i < 8; i++)
		{
			lattMid = (lattLow + lattHigh)/2;
			double temp = tempAt(lattMid, TD_CENTIGRADE);
			if (temp < targetTemp)
				lattHigh = lattMid;
			else if (temp > targetTemp)
				lattLow = lattMid;
			else
				break;
		}
		return (lattLow + lattHigh)/2;
	}
}
