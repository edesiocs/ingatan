/*
    OpenChart2 Java Charting Library and Toolkit
    Copyright (C) 2005-2007 Approximatrix, LLC
    Copyright (C) 2001  Sebastian M�ller
    http://www.approximatrix.com

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA

    ChartDataModel.java
    Created on 28. Juni 2001, 18:58
 */

package com.approximatrix.charting.model;

import com.approximatrix.charting.event.*;

/**
 * This interface defines the methods to access chart data. It has to deal 
 * with several difficulties. The model is basically 
 * resembling a Swing table model. Furthermore, in the implementation it 
 * needs to be defined whether the datasets are organized in rows or in columns, 
 * whether the x-axis values are numeric etc. In order to layout 
 * certain components, there must be methods to return the maximum and minimum 
 * values.<p>
 * A DataSet is a series of figures belonging together, whereas a column
 * is the value printed at the x-axis. They can be rendered using specific
 * Format classes.
 * @author mueller
 * @version 1.0
 */
public interface ChartDataModel {

	/** Returns the total amount of datasets.
	 * @return the total amount of DataSets
	 */
	public int getDataSetNumber();

	/** Returns the Axis binding of a specific DataSet.
	 * @param set the DataSet index
	 * @return an integer constant defining the Axis binding
	 */
	public int getAxisBinding(int set);

	/** Sets the Axis binding of a DataSet.
	 * @param set the DataSet index
	 * @param axis the Axis binding constant
	 */
	public void setAxisBinding(int set, int axis);

	/** Returns the length of a certain dataset. Note that for proper
	 * rendering all datasets should have an equal length.
	 * @param set the DataSet index
	 * @return the int length of a DataSet
	 */
	public int getDataSetLength(int set);

	/** Returns the title of the DataSet used for rendering the Legend.
	 * @param set the DataSet index
	 * @return a String containing the Title.
	 */
	public String getDataSetName(int set);

	/** Returns the Value in a specific dataset at a certain index.
	 * @param set the DataSet index
	 * @param index the value index in the DataSet
	 * @return the value Object
	 */
	public Number getValueAt(int set, int index);

	/** Sets the value in a specific dataset at the given index.
	 * @param set the DataSet index
	 * @param index the value index in the DataSet
	 * @param value the value to be stored
	 */
	public void setValueAt(int set, int index, Object value);

	/** Returns the class of the columns.
	 * @return the class of the column values. In case of numeric
	 * columns this is always Number.class.
	 */
	public Class getColumnClass();

	/** Returns a specific column value.
	 * @param col the column index
	 * @return the column value. In case of numeric columns this is
	 * always a Number object.
	 */
	public Object getColumnValueAt(int col);

	/** Returns a specific column value.
	 * @param set the data set index
	 * @param col the column index
	 * @return the column value. In case of numeric columns this is
	 * always a Number object.
	 */
	public Object getColumnValueAt(int set, int col);

	/** Defines whether the column values are numeric,
	 * that is, they can be casted to <code>Number</code>.
	 * @return <CODE>true</CODE> if the column value can be
	 * safely casted to Number type.
	 */
	public boolean isColumnNumeric();    

	/** Adds a ChartDataModelListener
	 * @param l the ChartDataModelListener
	 */
	public void addChartDataModelListener(ChartDataModelListener l);

	/** Removes a ChartDataModelListener
	 * @param l the ChartDataModelListener
	 */
	public void removeChartDataModelListener(ChartDataModelListener l);

	/** Returns a ChartDataModelConstraints object for the given
	 * axis binding.
	 * @param axis the Axis constant
	 * @return a ChartDataModelConstraints object.
	 */
	public ChartDataModelConstraints getChartDataModelConstraints(int axis);

	/** Sets the ChartDataModelConstraints object for the given
	 * axis binding.
	 * @param axis the Axis constant
	 * @param constraints the ChartDataModelConstraints object
	 * @return a ChartDataModelConstraints object.
	 */
	public void setChartDataModelConstraints(int axis, ChartDataModelConstraints constraints);

	/** Enables or disables chart autoscaling.  This setting is overridden by
	 * the manual axis scaling flag.
	 * 
	 * @param b true to enable autoscaling, false otherwise
	 */
	public void setAutoScale(boolean b);

	/** Returns true if autoscaling is enabled.  Does not depend on the 
	 * manual scaling flag.
	 * 
	 * @return true if autoscaling is enabled, false otherwise
	 */
	public boolean isAutoScale();

	/** Enables the manual axis scaling. Set the desired
	 * maximum and minimum values using the setMaximum...Value
	 * functions.
	 * 
	 * @deprecated As of release 1.4, only autoscale flag will be considered
	 */
	public void setManualScale(boolean b);

	/** Returns true if the manual axis scaling is enabled. This overrides
	 * the enabled automatic axis scaling.
	 * 
	 * @deprecated As of release 1.4, only autoscale flag will be considered
	 */
	public boolean isManualScale();

	/** Sets the maximum x-axis value. 
	 *
	 * @deprecated As of release 1.3, replaced by {@link #setMinumumX()}
	 */
	public void setMaximumColumnValue(double d);

	/** Sets the minimum x-axis value. 
	 *
	 * @deprecated As of release 1.3, replaced by {@link #setMinimumX()}
	 */
	public void setMinimumColumnValue(double d);

	/** Sets the maximum y-axis value. 
	 *
	 * @deprecated As of release 1.3, replaced by {@link #setMaximumY()}
	 */
	public void setMaximumValue(Number n);

	/** Sets the minimum y-axis value. 
	 *
	 * @deprecated As of release 1.3, replaced by {@link #setMinimumY()}
	 */
	public void setMinimumValue(Number n);

	/** Sets the maximum y-axis value. 
	 *
	 * @param n the new maximum y value
	 */
	public void setMaximumValueY(Number n);

	/** Sets the minimum x-axis value. 
	 *
	 * @param n the new minimum x value
	 */
	public void setMinimumValueX(Number n);

	/** Sets the maximum x-axis value. 
	 *
	 * @param n the new maximum x value
	 */
	public void setMaximumValueX(Number n);

	/** Sets the minimum y-axis value. 
	 *
	 * @param n the new minimum y value
	 */
	public void setMinimumValueY(Number n);

	/** Returns the minimum manual x-axis scale
	 * 
	 * @return the minimum x-axis value, or null if not assigned
	 */
	public Number getManualMinimumX();

	/** Returns the maximum manual x-axis scale
	 * 
	 * @return the maximum x-axis value, or null if not assigned
	 */
	public Number getManualMaximumX();

	/** Returns the minimum manual y-axis scale
	 * 
	 * @return the minimum y-axis value, or null if not assigned
	 */
	public Number getManualMinimumY();

	/** Returns the maximum manual y-axis scale
	 * 
	 * @return the maximum y-axis value, or null if not assigned
	 */
	public Number getManualMaximumY();

	/** Returns the current maximum x-axis value used for manual scaling.
	 * 
	 * @return the maximum column value as a double for manual scaling
	 * @deprecated As of release 1.3, replaced by {@link #getManualMaximumY()}
	 */
	public double getManualMaximumColumnValue();

	/** Returns the current minimum x-axis value used for manual scaling.
	 * 
	 * @return the minimum column value as a double for manual scaling
	 * @deprecated As of release 1.3, replaced by {@link #getManualMinimumY()}
	 */
	public double getManualMinimumColumnValue();

	/** Returns the current maximum y-axis value used for manual scaling.
	 * 
	 * @return the maximum for manual scaling
	 * @deprecated As of release 1.3, replaced by {@link #getManualMaximumX()}
	 */
	public Number getManualMaximumValue();

	/** Returns the current minimum y-axis value used for manual scaling.
	 * 
	 * @return the minimum for manual scaling
	 * @deprecated As of release 1.3, replaced by {@link #getManualMinimumY()}
	 */
	public Number getManualMinimumValue();
	
	/** Promotes a new ChartDataModelEvent.
     * @param src the source object of the event.
     */
    public void fireChartDataModelChangedEvent(Object src);
}