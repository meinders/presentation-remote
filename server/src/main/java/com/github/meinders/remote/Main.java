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

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.webapp.*;

/**
 * Runs a minimal web server for the specified war file.
 *
 * @author Gerrit Meinders
 */
public class Main
{
	/**
	 * Run application.
	 *
	 * @param   args    Command-line arguments.
	 */
	public static void main( final String[] args )
	throws Exception
	{
		if ( args.length == 0 )
		{
			System.err.println( "No war file specified." );
			System.exit( 1 );
			return;
		}

		final Server server = new Server();

		final ServerConnector connector = new ServerConnector( server );
		// Set some timeout options to make debugging easier.
		connector.setIdleTimeout( 1000 * 60 * 60 );
		connector.setSoLingerTime( -1 );
		connector.setPort( 8080 );
		server.setConnectors( new Connector[] { connector } );

		final WebAppContext context = new WebAppContext();
		context.setServer( server );
		context.setContextPath( "/" );
		context.setWar( args[ 0 ] );

		server.setHandler( context );

		final RemoteUI remoteUI = new RemoteUI( server );
		remoteUI.start();

		server.start();
	}
}
