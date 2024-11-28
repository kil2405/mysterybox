#! /usr/bin/env python3

from subprocess import check_call
from glob import glob
from os import path
from os import environ
import csv
import Util

Util.ReadConfig( 'Config.ini' )

Util.CleanDatabase( 'bbf_static' )

Util.RunSqlInFolder( './ResourceDB/Table' )


csvFiles = glob( './ResourceDB/CSV/*.csv' )
for csvFile in csvFiles :
    print( 'Importing ' + csvFile )
    tableName = path.splitext( path.basename(csvFile) )[0]
    with open(csvFile,"r",encoding="utf-8-sig") as f :
        row = next( csv.reader(f) )
        
        row = row[:-1]
        #print(row)
        
    fields = ", ".join( [ "`%s`"%field.strip() for field in row ] )
    print(csvFile)
    sql = ( "LOAD DATA LOCAL INFILE '%s' INTO TABLE `bbf_static`.`%s`"
            " FIELDS ESCAPED BY '\\\\' TERMINATED BY ',' ENCLOSED BY '\"'"
            " LINES TERMINATED BY '\\r\\n' IGNORE 1 LINES (%s) ;" ) % ( csvFile.replace('\\','/'), tableName , fields )
    #print( sql )
    check_call( ['mysql','-u',environ['USER'],'--local-infile','bbf_static','-e',sql] )
    

Util.RunSqlInFolder( './ResourceDB/Procedure' )
