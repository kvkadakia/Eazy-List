import numpy as np
import urllib, cStringIO

file = cStringIO.StringIO(urllib.urlopen(URL).read())
img = Image.open(file)

subscription_key = "#############################"
assert subscription_key
vision_base_url = "###############################"
ocr_url = vision_base_url + "/ocr"
import requests
headers  = {'Ocp-Apim-Subscription-Key': subscription_key }
params   = {'language': 'unk', 'detectOrientation ': 'true'}
data     = {'url': 'https://i0.wp.com/www.printablesample.com/wp-content/uploads/2017/03/Grocery-Receipt-1.jpg'}
response = requests.post(ocr_url, headers=headers, params=params, json=data)
response.raise_for_status()
analysis = response.json() 
line_infos = [region["lines"] for region in analysis["regions"]]


word_line = []
for line in line_infos:
     for word_metadata in line:
        word_infos = []
        for word_info in word_metadata["words"]:
             word_infos.append(word_info['text'])
        word_line.append(word_infos)
word = []
for i in range(0,len(word_line)):
    word.append(' '.join(word_line[i]))
word_corpus = ['WRAPPING PAPER', 'INSTANT COFFEE GOLD', 'TOMATO', 'ORANGE JUICE 1.5L', 'BUTTERMILK DESSERT', 'STRAWBERRIES', 'YOGURT' ,'APPLE JUICE' ]
import re as re
items = []
for item in word:
    for word in word_corpus:
        if (item in word):
            item = re.sub("[0-9][^ ]*?( |$)", " ", item)
            items.append(item)    
items = list(set(items))
print(items)
