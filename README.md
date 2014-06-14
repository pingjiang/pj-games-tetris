使用Java+Slick2D库实现的俄罗斯方块
===============================

在YouTube上无意间看到了别人演示的用slick开发一个俄罗斯方块的应用，为了学习slick库，我首先学习了slick官方的一个HelloWorld入门示例。
紧接着我找到了dkhalife实现的一个完整版本，所以我反编译了这里的jar包，修改了一些代码，通过理解代码实现来学习slick库。

代码是基于[https://github.com/dkhalife/tetris](https://github.com/dkhalife/tetris)修改而来，所有版权归原作者所有。
slick的设置是参考的 [http://slick.ninjacave.com/wiki/index.php?title=Main_Page](http://slick.ninjacave.com/wiki/index.php?title=Main_Page)

设置和编译
---------

从[slick](http://slick.ninjacave.com/slick.zip)官网下载了slick后解压到一个目录, 比如 `~/Apps/libs/slick`，在IDE里面加入slick的lib目录下面的jar包.

运行游戏
-------

可以编译打包一个jar包，加上jvm参数 `-Djava.library.path=~/Apps/libs/slick` 后运行Main函数 `org.pj.games.tetris.App`.

Objectives
----------

For those who don't know the game (shame on you), the objective is simple:

- Stack the pieces on top of each other in a way to create full lines
- Once a line is full it will be removed from the "pit" and will gave you a score bonus
- Try to make combos: double, tripple or quadruple (also known as Tetris) for another score boost
- When you reach the score milestone you will level up
- As you advance in the levels the pieces will drop faster (i made the speed a bit reasonable for inexperienced players in the first levels but it DOES get fast)

In my implementation of the game (not sure if this is also in the offical game too):

- There is a multiplier that is, surprise, multiplied by each bonus you get
- The multiplier is at least the level number you are at, so if you are at level 2, the multiplier is at least 2
- Each time you get a combo (doubles or more), the multiplier is increased by one
- So if you keep getting combos in a row, the multiplier will boost your score !

Hint: You can see where the current piece will end up with the dark "ghost" piece that is shown in the pit. This "ghost" will disappear if the piece is near where it is supposed to land.

Controls
--------

- Left and right arrow keys will move the current piece to the left or to the right
- The up arrow key rotates the current piece
- The down arrow key slowly moves the piece down
- Pressing space will drop the piece
- And pressing S will toggle the shadow piece

Credits
-------

- I have coded all the game by myself, i don't provide source code in the repository but if you need hints on how to build a tetris clone feel free to contact me.
- I also did all the graphics myself, except for the grey background which is a basic abstract image i found on google images.
- The sounds are also from this wonderful resources site: http://www.sounds-resource.com/ so the credits go to the original uploader 

Have fun! I hope you enjoy it!