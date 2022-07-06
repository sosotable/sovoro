import mysql.connector
from mysql.connector import Error
import pandas as pd


def query_select(cursor):
    sql = "select word, description from engkor;"
    cursor.execute(sql)


def query_insert(cursor, engword, korword):
    sql = (
        "insert into wordinfo(engword,korword)"
        "values(%s,%s)"
    )
    data = (engword, korword)
    cursor.execute(sql, data)

try:
    connection = mysql.connector.connect(host='13.58.48.132',
                                         database='DWORD',
                                         user='DWORD',
                                         password='ssp')
    if connection.is_connected():
        db_Info = connection.get_server_info()
        print("Connected to MySQL Server version ", db_Info)
        cursor_select = connection.cursor(buffered=True)
        cursor_insert = connection.cursor(buffered=True)
        query_select(cursor_select)
        # t=0
        for row in cursor_select.fetchall():
            # print(row[0])
            # print(nnr[len('<FONT COLOR=#0000FF>'):nnr.find('</FONT>')])
            # t += 1
            # print(t)
            nr = row[1].replace("\r\n", " ")
            nnr = nr[nr.find('<FONT COLOR=#0000FF>'):]
            query_insert(cursor_insert, row[0], nnr[len('<FONT COLOR=#0000FF>'):nnr.find('</FONT>')])
        connection.commit()

except Error as e:
    print("Error while connecting to MySQL", e)
finally:
    if connection.is_connected():
        cursor_insert.close()
        cursor_select.close()
        connection.close()
        print("MySQL connection is closed")
