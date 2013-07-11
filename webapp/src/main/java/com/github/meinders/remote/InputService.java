/*
 * Copyright 2013 Gerrit Meinders
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.meinders.remote;

import java.awt.*;
import java.awt.event.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * Simulates the requested user input on the server.
 *
 * @author Gerrit Meinders
 */
@Path( "/input" )
public class InputService
{
	@GET
	@Path( "/next" )
	public Response next()
	{
		pressKey( KeyEvent.VK_PAGE_DOWN );
		return Response.noContent().build();
	}

	@GET
	@Path( "/previous" )
	public Response previous()
	{
		pressKey( KeyEvent.VK_PAGE_UP );
		return Response.noContent().build();
	}

	private void pressKey( int key )
	{
		final Robot robot;
		try
		{
			robot = new Robot();
		}
		catch ( AWTException e )
		{
			throw new WebApplicationException( Response.serverError().entity( e.toString() ).build() );
		}
		robot.keyPress( key );
		robot.keyRelease( key );
	}
}
