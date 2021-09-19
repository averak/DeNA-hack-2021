import requests
from bs4 import BeautifulSoup
import re

response = requests.get('https://ja.wikipedia.org/wiki/%E6%97%A5%E6%9C%AC%E3%81%AE%E8%A6%B3%E5%85%89%E5%9C%B0%E4%B8%80%E8%A6%A7')
soup = BeautifulSoup(response.text, 'html.parser')
data = soup.find_all('a', href=re.compile('/wiki/.*'))
data_arr = [i.get_text() for i in data]
result = []
for i in data_arr[6:]:
    if i == "日本の観光地":
        break
    if i == '':
        continue
    result.append(i)
unique_result = list(set(result))

insert_str = "INSERT INTO tag(name) VALUES "
for i in range(len(unique_result)):
    if len(unique_result)-1 == i:
        insert_str += "(" + unique_result[i] + ");"
    else:
        insert_str += "(" + unique_result[i] + "),"
        
print(insert_str)