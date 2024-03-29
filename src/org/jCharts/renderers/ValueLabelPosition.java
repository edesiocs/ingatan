/***********************************************************************************************
 * File Info: $Id: ValueLabelPosition.java,v 1.2 2003/03/05 23:36:30 nathaniel_auvil Exp $
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


package org.jCharts.renderers;


public class ValueLabelPosition {

	//---just above or right of value
	public static final ValueLabelPosition ON_TOP= new ValueLabelPosition( 0 );

	//---just below or left of value
	public static final ValueLabelPosition AT_TOP= new ValueLabelPosition( 1 );

	//---half distance from zero line to the value
	//public static final ValueLabelPosition MIDDLE= new ValueLabelPosition( 2 );

	//---just above or to the right of the zero line
	public static final ValueLabelPosition ABOVE_ZERO_LINE= new ValueLabelPosition( 3 );


	public static final ValueLabelPosition AXIS_TOP= new ValueLabelPosition( 4 );
	//public static final ValueLabelPosition AXIS_MIDDLE= new ValueLabelPosition( 5 );
	public static final ValueLabelPosition AXIS_BOTTOM= new ValueLabelPosition( 6 );


	private int position;


	/**********************************************************************************
	 *
	 * @param position
	 *********************************************************************************/
	protected ValueLabelPosition( int position ) {
		this.position= position;
	}


	/*******************************************************************************
	 *
	 * @return position.
	 ******************************************************************************/
	public int getPosition() {
		return position;
	}


	/******************************************************************************
	 *
	 * @param valueLabelPosition
	 * @return boolean
	 *****************************************************************************/
	public final boolean equals( ValueLabelPosition valueLabelPosition ) {
		return ( this.position == valueLabelPosition.position );
	}
}
