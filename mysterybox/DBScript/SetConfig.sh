#!/usr/bin/env bash

if [ "$#" -lt 2 ] ; then
	echo 'Short of arguments'
	exit
fi

sed "s/$1=.*/$1=$2/g" -i Config.ini
