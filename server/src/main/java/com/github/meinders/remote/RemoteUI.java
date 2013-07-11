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
import java.net.*;
import java.text.*;
import java.util.*;
import javax.swing.*;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.component.*;

/**
 * Shows the status and address of the web server.
 *
 * @author Gerrit Meinders
 */
public class RemoteUI
implements LifeCycle.Listener
{
	private final ResourceBundle _bundle;

	private Server _server;

	private JFrame _frame;

	private JLabel _status;

	/**
	 * Constructs a new instance.
	 */
	public RemoteUI( final Server server )
	{
		_bundle = ResourceBundle.getBundle( "RemoteUI" );
		_server = server;
		server.addLifeCycleListener( this );
	}

	public void start()
	{
		final String title = _bundle.getString( "remoteUI.title" );

		final JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout() );
		panel.setPreferredSize( new Dimension( 400, 100 ) );

		final JLabel titleLabel = new JLabel( title );
		titleLabel.setFont( titleLabel.getFont().deriveFont( 18.0f ) );
		titleLabel.setHorizontalAlignment( SwingConstants.CENTER );

		final JPanel titlePanel = createPadding( titleLabel, 10 );
		titlePanel.setBackground( new Color( 0xccddee ) );
		titlePanel.setOpaque( true );
		panel.add( titlePanel, BorderLayout.NORTH );

		final JLabel status = new JLabel();
		status.setHorizontalAlignment( SwingConstants.CENTER );
		panel.add( status );
		_status = status;

		final JFrame frame = new JFrame( title );
		frame.setContentPane( panel );
		_frame = frame;

		frame.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
		frame.addWindowListener( new WindowAdapter()
		{
			@Override
			public void windowClosing( final WindowEvent e )
			{
				stopServer();
			}

			@Override
			public void windowClosed( final WindowEvent e )
			{
				stop();
			}
		} );

		frame.pack();
		frame.setLocationRelativeTo( null );
		frame.setVisible( true );
	}

	private JPanel createPadding( final JComponent content, final int amount )
	{
		final JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout() );
		panel.add( content );
		panel.setBorder( BorderFactory.createEmptyBorder( amount, amount, amount, amount ) );
		panel.setOpaque( false );
		return panel;
	}

	private void stopServer()
	{
		final Server server = _server;
		if ( server != null )
		{
			try
			{
				server.stop();
				_server = null;
			}
			catch ( Exception e )
			{
				throw new RuntimeException( e );
			}
		}
	}

	public void stop()
	{
		final JFrame frame = _frame;
		if ( frame != null )
		{
			frame.dispose();
			_frame = null;
		}
	}

	private URI getAddress()
	{
		final Connector connector = _server.getConnectors()[ 0 ];
		return connector.getServer().getURI();
	}

	@Override
	public void lifeCycleStarting( final LifeCycle lifeCycle )
	{
		_status.setText( _bundle.getString( "server.starting" ) );
	}

	@Override
	public void lifeCycleStarted( final LifeCycle lifeCycle )
	{
		_status.setText( MessageFormat.format( _bundle.getString( "server.started" ), getAddress() ) );
	}

	@Override
	public void lifeCycleFailure( final LifeCycle lifeCycle, final Throwable throwable )
	{
		_status.setText( MessageFormat.format( _bundle.getString( "server.error" ), throwable ) );
	}

	@Override
	public void lifeCycleStopping( final LifeCycle lifeCycle )
	{
		_status.setText( _bundle.getString( "server.stopping" ) );
	}

	@Override
	public void lifeCycleStopped( final LifeCycle lifeCycle )
	{
		_status.setText( _bundle.getString( "server.stopped" ) );
	}
}
