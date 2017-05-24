
## 项目内容

假设每个页面可存放10条指令，分配给一个作业的内存块为4。使用请求分页分配方式模拟一个作业的执行过程，该作业有320条指令，即它的地址空间为32页，目前所有页还没有调入内存。



## 设计方案

- **生成随机指令序列**
  1. 在0－319条指令之间，随机选取一个起始执行指令，如序号为m。
  2. 顺序执行下一条指令，即序号为m+1的指令。
  3. 通过随机数，跳转到前地址部分0－m-1中的某个指令处，其序号为m1。
  4. 顺序执行下一条指令，即序号为m1+1的指令。
  5. 顺序执行下一条指令，即m2+1处的指令。
  6. 重复跳转到前地址部分、顺序执行、跳转到后地址部分、顺序执行的过  程，直到执行完320条指令。


- **算法**

  当内存中已有当前的页，则不需要调入。若没有，找到内存中的空位调入。若果内存中没有空位，应进行置换。置换采用了以下两个算法：

  - FIFO

    选择进入最早的页面置换。从0号块开始进行置换，记录发生置换的块的地址，每次发生置换后往后顺延一位。

    ```java
    memory[index] = currentPage;
    index = (index + 1) % 4;
    ```

  - LRU

    选择最长时间没有被使用过的页面置换。用一个数组记录内存每块最近被使用的情况，若被使用则置为0，未被使用则每次增加1，需要置换的时候选择数值最大的块进行置换。

    ```java
    memory[index] = currentPage;
    time[index] = 0;
    for (int j = 0; j < 4; j++) {
    	if (j != index)
        	time[j] += 1;
    }
    ```



- **执行方式**

  - 单步执行

    需点击 ***Next*** 单步进行。

  - 连续执行

    点击Start之后自动每隔 0.5s 执行一条指令。

- **类**

  - Instruction

    - 成员变量

      | 类型     | 变量名          | 描述       |
      | ------ | ------------ | -------- |
      | int [] | instructions | 存储指令执行顺序 |
      | Random | random       | 获取随机数    |

    - 成员函数

      | 返回值类型  | 函数                     | 描述       |
      | ------ | ---------------------- | -------- |
      | void   | generateInstructions() | 生成随机指令顺序 |
      | int [] | getInstructions()      | 获取指令顺序   |
      |        |                        |          |

  - FIFO

    - 成员变量

      | 类型            | 变量名                  | 描述       |
      | ------------- | -------------------- | -------- |
      | int           | currentInstruction   | 当前指令编号   |
      | int           | missPageCount        | 缺页次数     |
      | int           | executedInstructions | 已执行指令数   |
      | int           | index                | 当前内存块地址  |
      | int           | count                | 当前执行指令计数 |
      | int []        | memory               | 内存       |
      | boolean []    | bitmap               | 内存块是否为空  |
      | int []        | instructions         | 指令顺序     |
      | DecimalFormat | df                   | 输出格式     |

      </br>

      ​

    - 成员函数

      | 返回值类型   | 函数                        | 描述          |
      | ------- | ------------------------- | ----------- |
      | void    | executeInstructions()     | 执行指令        |
      | boolean | isExist(int currentPage)  | 当前页是否已在内存中  |
      | int     | getIndexOfPage(int page)  | 获取页在内存中的位置  |
      | int     | getEmptyPart()            | 获取内存中空闲块的个数 |
      | int     | getFirstEmptyIndex()      | 获取第一个空闲块的地址 |
      | int     | getPage(int instruction)  | 获取指令所在页号    |
      | int     | getCurrentInstruction()   | 获取当前指令      |
      | int     | getMissingPageCount()     | 获取缺页次数      |
      | int     | getExecutedInstructions() | 获取已执行指令数    |
      | int []  | getMemory()               | 获取内存情况      |
      | int []  | getInstructions()         | 获取指令        |
      | int     | getIndex()                | 获取当前内存块地址   |
      | double  | getRateOfMissingPage()    | 获取缺页率       |
      | void    | displayMemory()           | 展示内存存储情况    |

  - LRU

    - 成员变量

      | 类型            | 变量名                  | 描述       |
      | ------------- | -------------------- | -------- |
      | int           | currentInstruction   | 当前指令编号   |
      | int           | missPageCount        | 缺页次数     |
      | int           | executedInstructions | 已执行指令数   |
      | int           | index                | 当前内存块地址  |
      | int           | count                | 当前执行指令计数 |
      | int []        | memory               | 内存       |
      | boolean []    | bitmap               | 内存块是否为空  |
      | int []        | instructions         | 指令顺序     |
      | DecimalFormat | df                   | 输出格式     |

    - 成员函数

      | 返回值类型   | 函数                        | 描述          |
      | ------- | ------------------------- | ----------- |
      | void    | executeInstructions()     | 执行指令        |
      | boolean | isExist(int currentPage)  | 当前页是否已在内存中  |
      | int     | getIndexOfPage(int page)  | 获取页在内存中的位置  |
      | int     | getEmptyPart()            | 获取内存中空闲块的个数 |
      | int     | getFirstEmptyIndex()      | 获取第一个空闲块的地址 |
      | int     | getPage(int instruction)  | 获取指令所在页号    |
      | int     | getCurrentInstruction()   | 获取当前指令      |
      | int     | getMissingPageCount()     | 获取缺页次数      |
      | int     | getExecutedInstructions() | 获取已执行指令数    |
      | int []  | getMemory()               | 获取内存情况      |
      | int []  | getInstructions()         | 获取指令        |
      | int     | getIndex()                | 获取当前内存块地址   |
      | double  | getRateOfMissingPage()    | 获取缺页率       |
      | void    | displayMemory()           | 展示内存存储情况    |

  - Controller

    - 成员变量

      | 类型                | 变量名                 | 描述            |
      | ----------------- | ------------------- | ------------- |
      | UpdateThread      | updateThread        | 更新界面数据的线程     |
      | Thread            | thread              | 更新界面数据的线程     |
      | enum              | choices             | 枚举选择框的选项      |
      | choices           | choice              | 当前选择的选项       |
      | enum              | buttons             | 枚举按钮的文本       |
      | buttons           | button              | 当前按钮的文本       |
      | Instruction       | ins                 | Instruction对象 |
      | FIFO              | fifo                | FIFO对象        |
      | LRU               | lru                 | LRU对象         |
      | int []            | memory              | 内存块的内容        |
      | Map<Integer,Text> | map                 | 存储界面中所有文本     |
      | Button            | startButton         | "Start"按钮     |
      | ChoiceBox         | executionModeChoice | 选择框           |

    - 成员函数

      | 返回值类型 | 函数                            | 描述          |
      | ----- | ----------------------------- | ----------- |
      | void  | initialize()                  | 重写初始化       |
      | void  | init()                        | 初始化         |
      | void  | resetButtonOnMouseClicked()   | 点击reset按钮事件 |
      | void  | startButtonOnMouseClicked()   | 点击start按钮事件 |
      | void  | setVisible(boolean isVisible) | 设置文本可见性     |
      | void  | setMemory()                   | 刷新内存情况      |
      | void  | continuousExecute()           | 连续执行        |
      | void  | singleStep()                  | 单步执行        |
      | void  | update()                      | 更新界面文本      |
      | void  | initMap()                     | 初始化map      |

  - Main

    - 成员函数

    | 返回值类型 | 函数                        | 描述       |
    | ----- | ------------------------- | -------- |
    | void  | start(Stage primaryStage) | 载入FXML文件 |
    | void  | main(String[] args)       |          |

  - UpdateThread

    - 成员变量

      | 类型         | 变量名        | 描述           |
      | ---------- | ---------- | ------------ |
      | Controller | controller | Controller对象 |
      | boolean    | exit       | 控制线程是否终止     |

    - 成员函数

      | 返回值类型 | 函数                                  | 描述        |
      | ----- | ----------------------------------- | --------- |
      |       | UpdateThread(Controller controller) | 构造函数      |
      | void  | setExit(boolean flag)               | 设置exit的值  |
      | void  | run()                               | 重写run()函数 |

## 界面展示

![](http://i1.piimg.com/588926/7baa2299fed35bb3.png)
