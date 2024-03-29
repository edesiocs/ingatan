/***********************************************************************************************
 * File Info: $Id: AxisProperties.java,v 1.17 2003/03/02 21:48:10 nathaniel_auvil Exp $
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


package org.jCharts.properties;


final public class AxisProperties extends Properties
{
	private AxisTypeProperties xAxisProperties;
	private AxisTypeProperties yAxisProperties;

	private boolean isPlotHorizontal= false;

   private boolean xAxisLabelsAreVertical= false;


	/***********************************************************************************************
	 *
	 * @param xAxis
	 * @param yAxis
	 ************************************************************************************************/
	public AxisProperties( LabelAxisProperties xAxis, LabelAxisProperties yAxis )
	{
		this.xAxisProperties= xAxis;
		this.yAxisProperties= yAxis;
	}


	/***********************************************************************************************
	 *
	 * @param isHorizontalPlot
	 ************************************************************************************************/
	public AxisProperties( boolean isHorizontalPlot )
	{
		super();

		this.isPlotHorizontal= isHorizontalPlot;

		if( isHorizontalPlot )
		{
			this.xAxisProperties= new DataAxisProperties();
			this.yAxisProperties= new LabelAxisProperties();
		}
		else
		{
			this.xAxisProperties= new LabelAxisProperties();
			this.yAxisProperties= new DataAxisProperties();
		}
	}


	/***********************************************************************************************
	 *
	 ************************************************************************************************/
	public AxisProperties()
	{
		super();

		this.isPlotHorizontal= false;
		this.xAxisProperties= new LabelAxisProperties();
		this.yAxisProperties= new DataAxisProperties();
	}


	public AxisTypeProperties getXAxisProperties()
	{
		return xAxisProperties;
	}


	public AxisTypeProperties getYAxisProperties()
	{
		return yAxisProperties;
	}


	public boolean isPlotHorizontal()
	{
		return isPlotHorizontal;
	}


	public boolean xAxisLabelsAreVertical()
	{
		return xAxisLabelsAreVertical;
	}


	public void setXAxisLabelsAreVertical( boolean xAxisLabelsAreVertical )
	{
		this.xAxisLabelsAreVertical = xAxisLabelsAreVertical;
	}


}
