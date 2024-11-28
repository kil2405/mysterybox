#! /usr/bin/env python3

import configparser
import os
from subprocess import check_call, check_output
import fnmatch

def ReadConfig( p_fileName ) :
    cp = configparser.RawConfigParser()
    cp.optionxform=str
    cp.read( p_fileName )
    dbConfigs = cp.items( 'MariaDB' )
    for key, val in dbConfigs :
        val = val.strip()
        if val :
            os.environ[key] = val
    
    if not os.environ.get( 'MYSQL_HOST' ) :
        os.environ['MYSQL_HOST'] = '127.0.0.1'


def InitEnv( p_cfgFileName ) :
    ReadConfig( p_cfgFileName )


def RunSqlInFolder( p_folderPath ) :
    for parentDir, dirNames, fileNames in os.walk( p_folderPath ):
        parentDir = os.path.abspath( parentDir )
        for sqlFile in fnmatch.filter( sorted(fileNames), '*.sql' ):
            sqlFile = os.path.join( parentDir, sqlFile )
            print( 'Running ' + sqlFile )
            check_call( [ 'mysql','-u',os.environ['USER'] ], stdin=open(sqlFile,'rt', encoding='UTF8') )




def RunSqlInFolderWithDbName( p_folderPath , p_dbName) :
    for parentDir, dirNames, fileNames in os.walk( p_folderPath ):
        parentDir = os.path.abspath( parentDir )
        for sqlFile in fnmatch.filter( sorted(fileNames), '*.sql' ):
            sqlFile = os.path.join( parentDir, sqlFile )
            print( 'Running ' + sqlFile + " DB name : "+p_dbName)
            sql_str=open(sqlFile,'rt', encoding='UTF8').read().replace('{db_name}',p_dbName)
            check_call( [ 'mysql','-u',os.environ['USER'] , '-e', sql_str] )



def RunSqlFile( in_db, in_sqlFileName ) :
    check_call( [ 'mysql', '-u', os.environ['USER'], '-D', in_db ], stdin=open(in_sqlFileName,'rt', encoding='UTF8') )



def RunSqlString( in_db, in_sqlString ) :
    check_call( [ 'mysql', '-u', os.environ['USER'], '-D', in_db, '-e', in_sqlString ] )



def GetGitRevision() :
    try:
        return check_output( [ 'git', 'rev-parse', '--verify', 'HEAD' ] ).decode('utf8').strip()
    except:
        pass
    
    with open( 'GitRevision.txt', 'r' ) as f :
        return f.readline().strip()



def CleanDatabase( p_dbName ) :
	sql = (
        "SET FOREIGN_KEY_CHECKS=0 ;"
		"DROP DATABASE IF EXISTS {0} ;"
		"CREATE DATABASE {0} "
			"CHARACTER SET utf8 COLLATE utf8_general_ci " 
		";"
	).format( p_dbName )
	#print( sql )
	check_call( ['mysql','-u',os.environ['USER'],'-e',sql] )
	#RunSqlFile( p_dbName, 'Common.sql' )
	
