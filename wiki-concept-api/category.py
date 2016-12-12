from bs4 import BeautifulSoup

class Category:
    category = []
    html = open('/Users/jessicazhao/Desktop/Category_Computer science - Wikipedia.htm').read()
    soup = BeautifulSoup(html)
    # print soup
    alltag = soup.find_all('a', {'class': 'CategoryTreeLabel CategoryTreeLabelNs14 CategoryTreeLabelCategory'})

    cont = 0
    for a in alltag:
        cont += 1
        category.append(a.text)
    print cont

print Category.category

