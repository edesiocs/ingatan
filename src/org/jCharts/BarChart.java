/***********************************************************************************************
 * File Info: $Id: BarChart.java,v 1.22 2003/03/19 01:25:06 nathaniel_auvil Exp $
 * Copyright (C) 2002
 * Author: Nathaniel G. Auvil
 * Contributor(s):
 *
 * Copyright 2002 (C) Nathaniel G. Auvil. All Rights Reserved.
 *
 * Redistribution and use of this software and associated documentation ("Software"), with or
 * without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and notices.
 * 	Redistributions must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * 	conditions and the following disclaimer in the documentation and/or other materials
 * 	provided with the distribution.
 *
 * 3. The name "jCharts" or "Nathaniel G. Auvil" must not be used to endorse or promote
 * 	products derived from this Software without prior written permission of Nathaniel G.
 * 	Auvil.  For written permission, please contact nathaniel_auvil@users.sourceforge.net
 *
 * 4. Products derived from this Software may not be called "jCharts" nor may "jCharts" appear
 * 	in their names without prior written permission of Nathaniel G. Auvil. jCharts is a
 * 	registered trademark of Nathaniel G. Auvil.
 *
 * 5. Due credit should be given to the jCharts Project (http://jcharts.sourceforge.net/).
 *
 * THIS SOFTWARE IS PROVIDED BY Nathaniel G. Auvil AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * jCharts OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
 ************************************************************************************************/


package org.jCharts;


import org.jCharts.renderers.AxisValueRenderEvent;
import org.jCharts.chartData.IAxisChartDataSet;
import org.jCharts.renderElements.RectMapArea;
import org.jCharts.properties.BarChartProperties;
import org.jCharts.properties.DataAxisProperties;

import java.awt.*;
import java.awt.geom.Rectangle2D;


abstract class BarChart
{

	/********************************************************************************************
	 * Draws the chart
	 * uses Rectangle2D......keep having rounding problems.
	 *
	 * @param axisChart
	 * @param iAxisChartDataSet
	 *********************************************************************************************/
	static void render( AxisChart axisChart, IAxisChartDataSet iAxisChartDataSet )
	{
		Graphics2D g2d = axisChart.getGraphics2D();
		BarChartProperties barChartProperties = (BarChartProperties) iAxisChartDataSet.getChartTypeProperties();

		DataAxisProperties dataAxisProperties;
		float barWidth;

		//---y axis position on screen to start drawing.
		float startingX;
		float startingY;
		float width;
		float height;


		if( axisChart.getAxisProperties().isPlotHorizontal() )
		{
			dataAxisProperties = (DataAxisProperties) axisChart.getAxisProperties().getXAxisProperties();
			barWidth = axisChart.getYAxis().getScalePixelWidth() * barChartProperties.getPercentage();

			startingX = axisChart.getXAxis().getZeroLineCoordinate();
			startingY = axisChart.getYAxis().getLastTickY() - (barWidth / 2);
			width = 0;
			height = barWidth;
			Rectangle2D.Float rectangle = new Rectangle2D.Float( startingX, startingY, width, height );

			BarChart.horizontalPlot( axisChart, iAxisChartDataSet, barChartProperties, dataAxisProperties, g2d, rectangle, startingX );
		}
		else
		{
			dataAxisProperties = (DataAxisProperties) axisChart.getAxisProperties().getYAxisProperties();
			barWidth = axisChart.getXAxis().getScalePixelWidth() * barChartProperties.getPercentage();

			startingX = axisChart.getXAxis().getTickStart() - (barWidth / 2);
			startingY = axisChart.getYAxis().getZeroLineCoordinate();
			width = barWidth;
			height = 0;
			Rectangle2D.Float rectangle = new Rectangle2D.Float( startingX, startingY, width, height );

			BarChart.verticalPlot( axisChart, iAxisChartDataSet, barChartProperties, dataAxisProperties, g2d, rectangle, startingY );
		}
	}


	/*********************************************************************************************
	 *
	 * @param axisChart
	 * @param iAxisChartDataSet
	 * @param barChartProperties
	 * @param dataAxisProperties
	 * @param g2d
	 * @param rectangle
	 * @param startingX
	 *********************************************************************************************/
	private static void horizontalPlot( AxisChart axisChart,
													IAxisChartDataSet iAxisChartDataSet,
													BarChartProperties barChartProperties,
													DataAxisProperties dataAxisProperties,
													Graphics2D g2d,
													Rectangle2D.Float rectangle,
													float startingX )
	{
      int imageMapLabelIndex = axisChart.getYAxis().getNumberOfScaleItems() - 1;

		//---setup the total area rectangle
		Rectangle2D.Float totalItemArea = new Rectangle2D.Float();
		totalItemArea.y = axisChart.getYAxis().getOrigin() - axisChart.getYAxis().getPixelLength() + 1;
		totalItemArea.height = axisChart.getYAxis().getScalePixelWidth() - 1;
		totalItemArea.x = axisChart.getXAxis().getOrigin() + 1;
		totalItemArea.width = axisChart.getXAxis().getPixelLength() - 1;


		//---reuse the same Object for pre and post render events.
		AxisValueRenderEvent axisValueRenderEvent = new AxisValueRenderEvent( axisChart,
																									 iAxisChartDataSet,
																									 g2d,
																									 totalItemArea,
																									 axisChart.getXAxis().getZeroLineCoordinate() );

		//---there is only ever one data set for a regular bar chart
		axisValueRenderEvent.setDataSetIndex( 0 );


		//LOOP
		for( int i = 0; i < iAxisChartDataSet.getNumberOfDataItems(); i++ )
		{
			//---reset the paint as it might have changed for the outline drawing
			g2d.setPaint( iAxisChartDataSet.getPaint( 0 ) );

			//---set values for the preRender event
			axisValueRenderEvent.setValueX( axisChart.getXAxis().getZeroLineCoordinate() );
			axisValueRenderEvent.setValueY( (float) rectangle.getCenterY() );
			axisValueRenderEvent.setValueIndex( i );

			//---we want to do this regardless if we render an item
			barChartProperties.firePreRender( axisValueRenderEvent );


			//---if value == 0 do not plot anything.
			if( iAxisChartDataSet.getValue( 0, i ) != 0.0d )
			{
				if( iAxisChartDataSet.getValue( 0, i ) < 0 )
				{
					rectangle.x = axisChart.getXAxis().computeAxisCoordinate( axisChart.getXAxis().getOrigin(),
																								 iAxisChartDataSet.getValue( 0, i ),
																								 axisChart.getXAxis().getScaleCalculator().getMinValue() );
					rectangle.width = startingX - rectangle.x;

					//---set values for the postRender event
					axisValueRenderEvent.setValueX( rectangle.x );
				}
				else
				{
					rectangle.x = startingX;
					rectangle.width = BarChart.computeScaleHeightOfValue( iAxisChartDataSet.getValue( 0, i ),
																							axisChart.getXAxis().getOneUnitPixelSize() );

					//---set values for the postRender event
					axisValueRenderEvent.setValueX( rectangle.x + rectangle.width );
				}


				//---with a user defined scale, we could have non-zero data points with a height of zero.
				if( rectangle.width != 0 )
				{

/*
System.out.println( "rectangle.x= " + rectangle.x );
System.out.println( "rectangle.y= " + rectangle.y );
System.out.println( "rectangle.width= " + rectangle.width );
System.out.println( "rectangle.height= " + rectangle.height );
*/
					g2d.fill( rectangle );

					if( barChartProperties.getShowOutlinesFlag() )
					{
						barChartProperties.getBarOutlineStroke().draw( g2d, rectangle );
					}

					//---if we are generating an ImageMap, store the image coordinates
					if( axisChart.getGenerateImageMapFlag() )
					{
						String label;
						if( axisChart.getYAxis().getAxisLabelsGroup() != null )
						{
							label = axisChart.getYAxis().getAxisLabelsGroup().getTextTag( imageMapLabelIndex ).getText();
						}
						else
						{
							label = null;
						}

						axisChart.getImageMap().addImageMapArea( new RectMapArea( rectangle,
																									 iAxisChartDataSet.getValue( 0, i ),
																									 label,
																									 iAxisChartDataSet.getLegendLabel( 0 ) ) );

						imageMapLabelIndex--;
					}
				}
			}


			//---notify everyone we just rendered
			barChartProperties.firePostRender( axisValueRenderEvent );
			totalItemArea.y += axisChart.getYAxis().getScalePixelWidth();

			rectangle.y += axisChart.getYAxis().getScalePixelWidth();
		}
	}


	/**************************************************************************************
	 *
	 * @param axisChart
	 * @param iAxisChartDataSet
	 * @param barChartProperties
	 * @param dataAxisProperties
	 * @param g2d
	 * @param rectangle
	 * @param startingY
	 **************************************************************************************/
	private static void verticalPlot( AxisChart axisChart,
												 IAxisChartDataSet iAxisChartDataSet,
												 BarChartProperties barChartProperties,
												 DataAxisProperties dataAxisProperties,
												 Graphics2D g2d,
												 Rectangle2D.Float rectangle,
												 float startingY )
	{

		//---setup the total area rectangle
		Rectangle2D.Float totalItemArea = new Rectangle2D.Float();
		totalItemArea.x = axisChart.getXAxis().getOrigin() + 1;
		totalItemArea.y = axisChart.getYAxis().getOrigin() - axisChart.getYAxis().getPixelLength() + 1;
		totalItemArea.width = axisChart.getXAxis().getScalePixelWidth() - 1;
		totalItemArea.height = axisChart.getYAxis().getPixelLength() - 1;


		//---reuse the same Object for pre and post render events.
		AxisValueRenderEvent axisValueRenderEvent = new AxisValueRenderEvent( axisChart,
																									 iAxisChartDataSet,
																									 g2d,
																									 totalItemArea,
																									 axisChart.getYAxis().getZeroLineCoordinate() );

		//---there is only ever one data set for a regular bar chart
		axisValueRenderEvent.setDataSetIndex( 0 );



		//LOOP
		for( int i = 0; i < iAxisChartDataSet.getNumberOfDataItems(); i++ )
		{
			//---reset the paint as it might have changed for the outline drawing
			g2d.setPaint( iAxisChartDataSet.getPaint( 0 ) );

			//---set values for the preRender event
			axisValueRenderEvent.setValueX( (float) rectangle.getCenterX() );
			axisValueRenderEvent.setValueY( axisChart.getYAxis().getZeroLineCoordinate() );
			axisValueRenderEvent.setValueIndex( i );

			//---we want to do this regardless if we render an item
			barChartProperties.firePreRender( axisValueRenderEvent );


			//---if value == 0 do not plot anything.
			if( iAxisChartDataSet.getValue( 0, i ) != 0.0d )
			{
				if( iAxisChartDataSet.getValue( 0, i ) < 0 )
				{
					rectangle.y = startingY;
					rectangle.height = BarChart.computeScaleHeightOfValue( iAxisChartDataSet.getValue( 0, i ),
																							 axisChart.getYAxis().getOneUnitPixelSize() );

					axisValueRenderEvent.setValueY( rectangle.y + rectangle.height );
				}
				else
				{
					rectangle.y = axisChart.getYAxis().computeAxisCoordinate( axisChart.getYAxis().getOrigin(),
																								 iAxisChartDataSet.getValue( 0, i ),
																								 axisChart.getYAxis().getScaleCalculator().getMinValue() );
					rectangle.height = startingY - rectangle.y;

					axisValueRenderEvent.setValueY( rectangle.y );
				}

				//---with a user defined scale, we could have non-zero data points with a height of zero.
				if( rectangle.height != 0 )
				{
					g2d.fill( rectangle );

					if( barChartProperties.getShowOutlinesFlag() )
					{
						barChartProperties.getBarOutlineStroke().draw( g2d, rectangle );
						g2d.setPaint( iAxisChartDataSet.getPaint( 0 ) );
					}

					//---if we are generating an ImageMap, store the image coordinates
					if( axisChart.getGenerateImageMapFlag() )
					{
						String label;
						if( axisChart.getXAxis().getAxisLabelsGroup() != null )
						{
							label = axisChart.getXAxis().getAxisLabelsGroup().getTextTag( i ).getText();
						}
						else
						{
							label = null;
						}

						axisChart.getImageMap().addImageMapArea( new RectMapArea( rectangle,
																									 iAxisChartDataSet.getValue( 0, i ),
																									 label,
																									 iAxisChartDataSet.getLegendLabel( 0 ) ) );
					}
				}
			}

			//---notify everyone we just rendered
			barChartProperties.firePostRender( axisValueRenderEvent );
			totalItemArea.x += axisChart.getXAxis().getScalePixelWidth();

			rectangle.x += axisChart.getXAxis().getScalePixelWidth();
		}
	}


	/*******************************************************************************************************
	 * Takes a value and determines the number of pixels it should fill on the screen.
	 * If there is a user defined scale and the passed value is greater than the MAX or less than the MIN,
	 *  the height will be forced to the MAX or MIN respectively.
	 *
	 * @param value
	 * @param oneUnitPixelSize
	 * @return float the screen pixel coordinate
	 ********************************************************************************************************/
	static float computeScaleHeightOfValue( double value, double oneUnitPixelSize )
	{
		return (float) Math.abs( (value) * oneUnitPixelSize );
	}

}
