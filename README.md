# MetalMaxRe

    这是一个基于java开发的FC游戏 MetalMax ROM编辑器

## 注意

> 未经修改版的作者同意，禁止通过本程序将数据公布到任何地方

### 关于游戏版本

不同版本数据地址有所不同，错误的加载ROM版本会导致数据加载错误  
编辑器内置4种ROM版本：

* `Chinese（中文版）`
* `Janpanse（日文原版）`
* `SuperHackGeneral（SH通用版）`
* `SuperHack（SH）`

如果内置的4种版本都不能加载你的ROM，可以尝试自定义[RomVersion](core/src/main/java/me/afoolslove/metalmaxre/RomVersion.java)
并修改相关编辑器

### 使用

* 本项目使用 `java 17` 进行开发
* 下载并构建 [core](./core)模块，将其导入到你的项目里就行啦

***详细到[core](./core)模块查看**

### 编辑器

| 编辑器类名                                                                                                     | 名称       | 状态          |
|-----------------------------------------------------------------------------------------------------------|----------|-------------|
| [IShopEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/computer/shop/IShopEditor.java)         | 商店编辑器    | 未完成         |
| [IComputerEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/computer/IComputerEditor.java)      | 计算机编辑器   | 完成          |
| [IDataValueEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/data/IDataValueEditor.java)        | 数据值编辑器   | 完成          |
| [IItemEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/items/IItemEditor.java)                 | 物品编辑器    | 完成          |
| [IEventEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/map/events/IEventTilesEditor.java)     | 事件编辑器    | 完成          |
| [ITileSetEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/map/tileset/ITileSetEditor.java)     | 图块编辑器    | 完成          |
| [IWorldMapEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/map/world/IWorldMapEditor.java)     | 世界地图编辑器  | 完成          |
| [IDogSystemEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/map/IDogSystemEditor.java)         | 犬系统编辑器   | 完成          |
| [IMapEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/map/IMapEditor.java)                     | 地图编辑器    | 完成，但储存方式有问题 |
| [IMapEntranceEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/map/IMapEntranceEditor.java)     | 地图出入口编辑器 | 完成          |
| [IMapPropertiesEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/map/IMapPropertiesEditor.java) | 地图属性编辑器  | 完成          |
| [IMonsterEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/monster/IMonsterEditor.java)         | 怪物编辑器    | 进行中         |
| [IPaletteEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/palette/IPaletteEditor.java)         | 调色板编辑器   | 完成          |
| [IPlayerEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/player/IPlayerEditor.java)            | 玩家编辑器    | 完成          |
| [IPlayerExpEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/player/IPlayerExpEditor.java)      | 玩家经验值编辑器 | 完成          |
| [ISpriteEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/sprite/ISpriteEditor.java)            | 精灵编辑器    | 完成          |
| [ITankEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/tank/ITankEditor.java)                  | 坦克编辑器    | 完成          |
| [ITextEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/text/ITextEditor.java)                  | 文本编辑器    | 基础完成，不支持日文  |
| [ITreasureEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/treasure/ITreasureEditor.java)      | 宝藏编辑器    | 完成          |

## 编辑器可编辑内容

### [IShopEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/computer/shop/IShopEditor.java)（售货机商品编辑器）

实现类：[ShopEditorImplImpl](core/src/main/java/me/afoolslove/metalmaxre/editors/computer/shop/ShopEditorImpl.java)

不支持世界地图，世界地图也不支持使用售货机

* 售货机的商品、商品数量和所在地图，修改商品价格请使用 [`IItemEditor`](core/src/main/java/me/afoolslove/metalmaxre/editors/items/IItemEditor.java)

### [IComputerEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/computer/IComputerEditor.java)（计算机编辑器）

实现类：[ComputerEditorImpl](core/src/main/java/me/afoolslove/metalmaxre/editors/computer/ComputerEditorImpl.java)

不支持世界地图，世界地图也不支持使用计算机  
计算机包含：售货机、游戏机、计算机等

* 计算机坐标、所在地图和类型

### [IDataValueEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/data/IDataValueEditor.java)（数据值编辑器）

实现类：[DataValueEditorImpl](core/src/main/java/me/afoolslove/metalmaxre/editors/data/DataValueEditorImpl.java)

### [IItemEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/items/IItemEditor.java)（物品编辑器）

实现类：[ItemEditorImpl](core/src/main/java/me/afoolslove/metalmaxre/editors/items/ItemEditorImpl.java)

* 物品的价格
* 玩家的防具属性 [`PlayerArmor`](core/src/main/java/me/afoolslove/metalmaxre/editors/player/PlayerArmor.java)
* 玩家的武器属性 [`PlayerWeapon`](core/src/main/java/me/afoolslove/metalmaxre/editors/player/PlayerWeapon.java)
* 坦克的装备属性 [`TankEquipmentItem`](core/src/main/java/me/afoolslove/metalmaxre/editors/tank/TankEquipmentItem.java) [`TankEngine`](core/src/main/java/me/afoolslove/metalmaxre/editors/tank/TankEngine.java)
* 坦克的武器属性 [`TankWeapon`](core/src/main/java/me/afoolslove/metalmaxre/editors/tank/TankWeapon.java)

### [IEventEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/map/events/IEventTilesEditor.java)（事件编辑器）

实现类：[EventEditorImpl](core/src/main/java/me/afoolslove/metalmaxre/editors/map/events/EventTilesEditorImpl.java)

支持世界地图  
地图的比例为：1:1tile  
世界地图图块的比例为：1:4*4tile

### [ITileSetEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/map/tileset/ITileSetEditor.java)（图块编辑器）

实现类：[TileSetEditorImpl](core/src/main/java/me/afoolslove/metalmaxre/editors/map/tileset/TileSetEditorImpl.java)

* 地图的图块和图块组合数据
* 地图的图块属性

### [IWorldMapEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/map/world/IWorldMapEditor.java)（世界地图编辑器）

实现类：[WorldMapEditorImpl](core/src/main/java/me/afoolslove/metalmaxre/editors/map/world/WorldMapEditorImpl.java)

* 编辑世界地图
* 修改出航/归航的航线，出航/归航航线的路径点各最大16条

### [IDogSystemEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/map/IDogSystemEditor.java)（犬系统编辑器）

实现类：[DogSystemEditorImpl](core/src/main/java/me/afoolslove/metalmaxre/editors/map/DogSystemEditorImpl.java)

* 将某个地图设置为城镇
* ~~某个地图作为附属城镇（进入附属城镇相当于进入城镇）~~
* 使用“传真”传送时的目的地坐标，目的地只能是世界地图
* 使用机器传送时的目的地坐标，支持世界地图

### [IMapEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/map/IMapEditor.java)（地图编辑器）

实现类：[MapEditorImpl](core/src/main/java/me/afoolslove/metalmaxre/editors/map/MapEditorImpl.java)

不支持世界地图，世界地图请使用 [`WorldMapEditor`](core/src/main/java/me/afoolslove/metalmaxre/editors/map/world/WorldMapEditor.java)

* 通过 [`MapBuilder`](core/src/main/java/me/afoolslove/metalmaxre/editors/map/MapBuilder.java) 可以构建或编辑所有地图

注：暂时未解决如何在有限的空间里提搞空间利用率

### [IMapEntranceEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/map/IMapEntranceEditor.java)（地图出入口编辑器）

实现类：[MapEntranceEditorImpl](core/src/main/java/me/afoolslove/metalmaxre/editors/map/MapEntranceEditorImpl.java)

支持世界地图

* 移动到地图边界时传送的目的地：固定目的地、根据朝向不同而不同的目的地和回到上一个地图
* 地图入口和出口坐标

### [IMapPropertiesEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/map/IMapPropertiesEditor.java)（地图属性编辑器）

实现类：[MapPropertiesEditorImpl](core/src/main/java/me/afoolslove/metalmaxre/editors/map/MapPropertiesEditorImpl.java)

支持编辑世界地图的部分属性 [`WorldMapProperties`](core/src/main/java/me/afoolslove/metalmaxre/editors/map/WorldMapProperties.java)

* 地图的宽高、玩家可移动区域、玩家可移动区域偏移量
* 精灵调色板、精灵图像
* 地图图块数据和地图图块数据的组合数据
* 隐藏图块、填充图块、门后图块、背景音乐
* 地图边界和出入口坐标
* 特殊属性：动态图块、事件图块、地下地图

### [IMonsterEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/monster/IMonsterEditor.java)（怪物编辑器）

实现类：[MonsterEditorImplImpl](core/src/main/java/me/afoolslove/metalmaxre/editors/monster/MonsterEditorImpl.java)

未完成

* 怪物的掉落物
* 怪物的部分属性
* 怪物的组合方式
* 怪物组合在地图和世界地图上的分布

**和其它未提及的小功能数据修改**

### [IPaletteEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/palette/IPaletteEditor.java)（调色板编辑器）

实现类：[PaletteEditorImpl](core/src/main/java/me/afoolslove/metalmaxre/editors/palette/PaletteEditorImpl.java)

全局调色板、精灵调色板、战斗时和非战斗时的调色板

### [IPlayerEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/player/IPlayerEditor.java)（玩家编辑器）

实现类：[PlayerEditorImpl](core/src/main/java/me/afoolslove/metalmaxre/editors/player/PlayerEditorImpl.java)

* 玩家的初始金钱
* 玩家的初始属性
* 玩家的初始装备
* 玩家的初始道具

### [IPlayerExpEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/player/IPlayerExpEditor.java)（玩家经验值编辑器）

实现类：[PlayerExpEditorImpl](core/src/main/java/me/afoolslove/metalmaxre/editors/player/PlayerExpEditorImpl.java)

* 玩家到达某一等级所需的经验（不是差值）

注：只能编辑2-99级，初始等级为1

### [ISpriteEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/sprite/ISpriteEditor.java)（精灵编辑器）

实现类：[SpriteEditorImpl](core/src/main/java/me/afoolslove/metalmaxre/editors/sprite/SpriteEditorImpl.java)

* 精灵的类型、坐标、对话和行动方式
* 特殊属性：可被推动、无视地形移动、锁定朝向、移动时不播放移动动画（平移）

### [ITankEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/tank/ITankEditor.java)（坦克编辑器）

实现类：[TankEditorImpl](core/src/main/java/me/afoolslove/metalmaxre/editors/tank/TankEditorImpl.java)

包含出租坦克

* 坦克的初始装备
* 坦克的初始属性
* 坦克的初始装甲片
* 坦克的初始位置（出租坦克只能修改初始地图，但这有什么用呢）

### [ITextEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/text/ITextEditor.java)（文本编辑器）

实现类：[TextEditorImpl](core/src/main/java/me/afoolslove/metalmaxre/editors/text/TextEditorImpl.java)

所有已知的文本

* get/set 城镇名称
* get/set 怪物名称
* get/set 物品名称

注：所有的文本都使用该类修改  
字库：[WordBank](core/src/main/java/me/afoolslove/metalmaxre/editors/text/WordBank.java)

### [ITreasureEditor](core/src/main/java/me/afoolslove/metalmaxre/editors/treasure/ITreasureEditor.java)（宝藏编辑器）

实现类：[TreasureEditorImpl](core/src/main/java/me/afoolslove/metalmaxre/editors/treasure/TreasureEditorImpl.java)

支持世界地图

* 宝藏坐标、所在地图和物品
* 地图的调查点
* 随机调查获得的宝藏和获取宝藏概率

注：宝箱的图像因地图图块组成不同而不同，世界地图为隐藏显示