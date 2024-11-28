#! /usr/bin/env python3

import pandas as pd
from sqlalchemy import create_engine
from os import path, environ
from glob import glob
import csv
import Util


Util.ReadConfig( 'Config.ini' )

Util.CleanDatabase( 'mysterybox_resource' )

Util.RunSqlInFolder( './ResourceDB/Table' )

# Create a connection to the MySQL database
user = environ['USER']
password = environ['MYSQL_PWD']
host = environ['MYSQL_HOST']

connection_string = f"mysql+pymysql://{user}:{password}@{host}/mysterybox_resource"
engine = create_engine(connection_string)

csvFiles = glob( './ResourceDB/CSV/*.csv' )
for csvFile in csvFiles :
    print( 'Importing ' + csvFile )
    tableName = path.splitext( path.basename(csvFile) )[0]
    
    # Read the CSV file into a DataFrame and remove the last column
    df = pd.read_csv(csvFile, encoding="utf-8-sig")
    #df.drop(df.columns[-1], axis=1, inplace=True)

    # Write DataFrame to MySQL table (replace if table exists)
    df.to_sql(tableName, con=engine, index=False, if_exists='append')
