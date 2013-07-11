#!/bin/bash
cd `dirname "$0"`
java -cp 'lib/*' com.github.meinders.remote.Main lib/presentation-remote-webapp-1.0.war &
