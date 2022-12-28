import pymysql
import pandas as pd
import numpy as np

dataFrame = pd.read_csv('dic-refine.csv')
dataFrame.drop(['Unnamed: 0'], axis=1, inplace=True)

dataList = dataFrame.values.tolist()
print(dataList[0][1], dataList[0][2], dataList[0][3])


def query_select(cursor):
    sql = "select word, description from engkor;"
    cursor.execute(sql)


def query_insert(cursor, engword, wordid, korword):
    sql = (
        "insert into wordinfo(engword,wordid,korword)"
        "values(%s,%s,%s)"
    )
    data = (engword, wordid, korword)
    print(data)
    cursor.execute(sql, data)


connection = pymysql.connect(
    host='*',
    user='*',
    password='*',
    db='*',
    charset='*'
)

if connection is not None:
    cursor = connection.cursor()
    t = 0
    for row in dataList:
        engword = row[2]
        korword = row[3]
        t += 1
        print(t)
        query_insert(cursor, engword, t, korword)
    connection.commit()
