# -*- coding: utf-8 -*- 
import numpy as np
import matplotlib.pyplot as plt

plt.rcParams['font.sans-serif']=['SimHei']
plt.rcParams['axes.unicode_minus']=False
plt.rcParams['legend.fontsize']=17
val1 = [0.60000000, 0.80000000, 0.80000000, 0.40000000, 0.40000000, 0.80000000, 1.00000000, 0.40000000, 0.40000000, 0.40000000, 0.20000000, 0.00000000, 1.00000000, 0.80000000, 0.20000000, 0.20000000, 0.80000000, 0.60000000, 0.20000000, 0.20000000, 0.20000000, 0.20000000, 0.60000000, 0.80000000, 0.60000000, 0.40000000, 0.20000000, 0.20000000, 0.60000000, 0.60000000]
val2 = [0.8, 0.4, 0.8, 0.4, 0.6, 0.4, 0.2, 0.4, 0.1, 0.8, 0.6, 1.0, 0.8, 0.4, 0.8, 0.3, 0.9, 0.2, 0.7, 0.2, 0.4, 0.7, 0.6, 0.4, 0.6, 0.2, 0.4, 0.4, 0.6, 1.0]
index = np.arange(30)
bar_width = 0.35         
opacity = 0.4
rects1 = plt.bar(index, val1, bar_width,alpha=opacity, color='b',label='推荐参数')    
rects2 = plt.bar(index + bar_width, val2, bar_width,alpha=opacity,color='r',label='机器学习')
plt.xlabel('用户编号', fontsize = 18)    
plt.ylabel('准确率', fontsize = 18)    
plt.xticks(index + bar_width / 2.0, (str(i+1) for i in range(30)))
plt.ylim(0,1); 
plt.legend();         
plt.tight_layout();   
plt.show();    
