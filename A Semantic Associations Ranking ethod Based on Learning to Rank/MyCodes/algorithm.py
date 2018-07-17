# Author: lizi
# Email: lzy960601@outlook.com

import math
from sklearn import svm
from sklearn.linear_model import SGDClassifier
from sklearn.linear_model import LogisticRegression
from sklearn import neighbors, datasets
from sklearn.gaussian_process import GaussianProcessClassifier
from sklearn import tree
from sklearn.ensemble import GradientBoostingClassifier
from sklearn.ensemble import RandomForestClassifier
from sklearn.ensemble import AdaBoostClassifier
from sklearn.naive_bayes import GaussianNB

prefix = 'D:\\E\\1My University\\Uni-Study\\NJU\\GraduatedDesign\\MyCodes\\datas\\SVM\\'

def readdata(k, met):
    pathname = prefix + 'Cluster' + str(k) + '\\' + met + '\\'
    dat = []
    res = []
    for num in range(k):
        accname = pathname + 'DataScale_' + str(num + 1) + '.txt'
        # print(accname)
        fi = open(accname, 'r')
        data = []
        ret = []
        for line in fi.readlines():
            tmp = line.split(' ')
            ret.append(float(tmp[0]))
            tmp = tmp[1:]
            dal = []
            for s in tmp:
                if len(s) < 3:
                    break
                now = s.split(':')
                dal.append(float(now[1]))
            data.append(dal)
        dat.append(data)
        res.append(ret)
        fi.close()
    return dat, res

k = int(input())
# Test
# xx = open(prefix, 'r')

train_data, train_rest = readdata(k, 'Train')
test_data, test_rest = readdata(k, 'Test')

svm_m = svm.SVC(tol = 0.001)
svm_l = []
sgd_m = SGDClassifier(loss="hinge", penalty="l2", tol = 0.001)
sgd_l = []
knn_m = neighbors.KNeighborsClassifier(n_neighbors = 5)
knn_l = []
gpc_m = GaussianProcessClassifier()
gpc_l = []
dt_m = tree.DecisionTreeClassifier()
dt_l = []
gbc_m = GradientBoostingClassifier()
gbc_l = []
rf_m = RandomForestClassifier()
rf_l = []
abc_m = AdaBoostClassifier()
abc_l = []
gnb_m = GaussianNB()
gnb_l = []
lrc_m = LogisticRegression(tol = 0.001)
lrc_l = []

def cal(nm, li):
    sum = 0
    mi = 200
    ma = 0
    for p in li:
        mi = min(mi, p)
        ma = max(ma, p)
        sum += p
    miu = sum / float(len(li))
    sig = 0
    for p in li:
        sig += (p - miu) ** 2
    sig /= float(len(li))
    sig = math.sqrt(sig)
    ma = float(ma)
    mi = float(mi)
    sig = float(sig)
    miu = float(miu)
    print(nm + ' : ',)
    print('max = {:.4f}    min = {:.4f}    miu = {:.4f}    sigma = {:.4f}'.format(ma, mi, miu, sig))


def svm_model(xtr, ytr, xte, yte):
    svm_m.fit(xtr, ytr)
    rte = svm_m.predict(xte)
    tot = len(yte)
    acc = 0
    for index in range(tot):
        if yte[index] == rte[index]:
            acc += 1
    svm_l.append( float(acc) / float(tot) )

def sgd_model(xtr, ytr, xte, yte):
    sgd_m.fit(xtr, ytr)
    rte = sgd_m.predict(xte)
    tot = len(yte)
    acc = 0
    for index in range(tot):
        if yte[index] == rte[index]:
            acc += 1
    sgd_l.append( float(acc) / float(tot) )

def knn_model(xtr, ytr, xte, yte):
    knn_m.fit(xtr, ytr)
    rte = knn_m.predict(xte)
    tot = len(yte)
    acc = 0
    for index in range(tot):
        if yte[index] == rte[index]:
            acc += 1
    knn_l.append( float(acc) / float(tot) )

def gpc_model(xtr, ytr, xte, yte):
    gpc_m.fit(xtr, ytr)
    rte = gpc_m.predict(xte)
    tot = len(yte)
    acc = 0
    for index in range(tot):
        if yte[index] == rte[index]:
            acc += 1
    gpc_l.append( float(acc) / float(tot) )

def dt_model(xtr, ytr, xte, yte):
    dt_m.fit(xtr, ytr)
    rte = dt_m.predict(xte)
    tot = len(yte)
    acc = 0
    for index in range(tot):
        if yte[index] == rte[index]:
            acc += 1
    dt_l.append( float(acc) / float(tot) )

def gbc_model(xtr, ytr, xte, yte):
    gbc_m.fit(xtr, ytr)
    rte = gbc_m.predict(xte)
    tot = len(yte)
    acc = 0
    for index in range(tot):
        if yte[index] == rte[index]:
            acc += 1
    gbc_l.append( float(acc) / float(tot) )

def rf_model(xtr, ytr, xte, yte):
    rf_m.fit(xtr, ytr)
    rte = rf_m.predict(xte)
    tot = len(yte)
    acc = 0
    for index in range(tot):
        if yte[index] == rte[index]:
            acc += 1
    rf_l.append( float(acc) / float(tot) )

def abc_model(xtr, ytr, xte, yte):
    abc_m.fit(xtr, ytr)
    rte = abc_m.predict(xte)
    tot = len(yte)
    acc = 0
    for index in range(tot):
        if yte[index] == rte[index]:
            acc += 1
    abc_l.append( float(acc) / float(tot) )

def gnb_model(xtr, ytr, xte, yte):
    gnb_m.fit(xtr, ytr)
    rte = gnb_m.predict(xte)
    tot = len(yte)
    acc = 0
    for index in range(tot):
        if yte[index] == rte[index]:
            acc += 1
    gnb_l.append( float(acc) / float(tot) )

def lrc_model(xtr, ytr, xte, yte):
    lrc_m.fit(xtr, ytr)
    rte = lrc_m.predict(xte)
    tot = len(yte)
    acc = 0
    for index in range(tot):
        if yte[index] == rte[index]:
            acc += 1
    lrc_l.append( float(acc) / float(tot) )

for num in range(k):
    svm_model(train_data[num], 
              train_rest[num], 
              test_data[num], 
              test_rest[num])
    sgd_model(train_data[num], 
              train_rest[num], 
              test_data[num], 
              test_rest[num])
    knn_model(train_data[num], 
              train_rest[num], 
              test_data[num], 
              test_rest[num])
    gpc_model(train_data[num], 
              train_rest[num], 
              test_data[num], 
              test_rest[num])
    dt_model(train_data[num], 
              train_rest[num], 
              test_data[num], 
              test_rest[num])
    gbc_model(train_data[num], 
              train_rest[num], 
              test_data[num], 
              test_rest[num])
    rf_model(train_data[num], 
              train_rest[num], 
              test_data[num], 
              test_rest[num])
    abc_model(train_data[num], 
              train_rest[num], 
              test_data[num], 
              test_rest[num])
    gnb_model(train_data[num], 
              train_rest[num], 
              test_data[num], 
              test_rest[num])
    lrc_model(train_data[num], 
              train_rest[num], 
              test_data[num], 
              test_rest[num])

cal('Support Vector Machine', svm_l)
cal('Stochastic Gradient Descent', sgd_l)
cal('K Nearest Neighbours', knn_l)
cal('Gaussian Process Classification', gpc_l)
cal('Decision Trees', dt_l)
cal('Gradient Tree Boosting Classification', gbc_l)
cal('Random Forests', rf_l)
cal('AdaBoost Classification', abc_l)
cal('Gaussian Naive Bayes', gnb_l)
cal('Logistic Regression Classification', lrc_l)

for p in gnb_l:
    print(p,end='')
    print(', ',end='')
print()
