# MetalMaxRe

## 创建一个MetalMaxRe实例

1. 创建用于操作字节的[RomBuffer(@NotNull RomVersion, @Nullable Path)](src/main/java/me/afoolslove/metalmaxre/RomBuffer.java)
    1. 使用内置ROM，通常的创建方法
   ~~~java
   RomBuffer romBuffer = new RomBuffer(RomVersion.getChinese(), null);
   ~~~
    2. 使用内置ROM，指定版本，其中的 <version> 请填写有效的版本，内置四种版本：  
       `japanese`、`chinese`、`super_hack`、`super_hack_general`
   ~~~java
   RomBuffer romBuffer = new RomBuffer("<version>", null);
   ~~~
    3. 不使用内置版本，如果你的ROM属于内置中的任意一种，请使用内置的ROM版本
   ~~~java
   // 直接传入字节
   RomBuffer romBuffer = new RomBuffer("<version>", byte[]);
   // 使用路径
   RomBuffer romBuffer = new RomBuffer("<version>", Path);
   ~~~
    4.
    如果你有自己的ROM版本，可以通过创建 [RomVersion(String, String, int, String, List<String>)](src/main/java/me/afoolslove/metalmaxre/RomVersion.java)
    实现
   ~~~java
   RomVersion romVersion = RomVersion("<version>", "/versions/custom_version", <文件大小>, "<描述>", <修改记录>);
   // 将实例添加进 RomVersion.VERSIONS
   RomVersion.VERSIONS.put("<version>", romVersion);
   // 添加完毕后就能通过前面的任意一步方法创建该实例
   RomBuffer romBuffer = new RomBuffer("<version>", null);
   // 注意：如果你没有创建自定义的版本编辑器，此时并没有任何编辑器支持这个自定义版本
   ~~~
2. 创建[MetalMaxRe(@NotNull RomBuffer)](src/main/java/me/afoolslove/metalmaxre/MetalMaxRe.java)，将`RomBuffer`实例传入就完成了
   ~~~java
   RomBuffer romBuffer = new RomBuffer((RomVersion.getChinese(), null));
   MetalMaxRe metalMaxRe = new MetalMaxRe(romBuffer);
   
   // 该方法会使用默认提供的类实现一些功能
   // 如果你用的是自定义版本，该项的编辑器部分不会起效，需要自己实现相关编辑器的功能
   metalMaxRe.useDefault();
   // code...
   ~~~

## 注册和加载编辑器

* 使用`MetalMaxRe.getEditorManager()`
  方法获取编辑器管理器，[IEditorManager](src/main/java/me/afoolslove/metalmaxre/editors/IEditorManager.java)
* 使用`IEditorManager.register(@NotNull Class<E>, @NotNull Function<MetalMaxRe, E>)`注册这个编辑器的类型和实例化这个编辑器方法。
  **_\*注册后会立即实例化，不会进行加载\*_**
* 如果使用的默认提供的编辑器管理器，可以使用`EditorManagerImpl.registerDefaultEditors()`注册默认提供的编辑器
* 使用`IEditorManager.loadEditor(@NotNull Class<IRomEditor>)`加载指定编辑器
* 使用`IEditorManager.loadEditors()`加载所有编辑器
  ~~~java
  RomBuffer buffer = new RomBuffer((RomVersion.getChinese(), null));
  MetalMaxRe metalMaxRe = new MetalMaxRe(buffer);
  metalMaxRe.useDefault(); // 使用默认提供的编辑器管理器实现类
  // ---------------- 注册编辑器
  EditorManagerImpl editorManager = metalMaxRe.getEditorManager();
  editorManager.registerDefaultEditors();
  // ---------------- 加载所有编辑器
  editorManager.loadEditors();
  // editorManager.loadEditors().get(); // 等待加载完毕
  ~~~

## 应用编辑器修改和保存修改

***索引类数据在应用才会生成有效的索引数据**

### 应用

* 使用`IEditorManager.applyEditor(@NotNull Class<IRomEditor>)`应用指定编辑器的修改
* 使用`IEditorManager.applyEditors()`应用所有编辑器的修改

### 保存

* 使用`RomBuffer.save(@NotNull Path)`保存到指定路径
* 使用`RomBuffer.save(@NotNull OutputStream)`保存到输出流
* 使用`RomBuffer.toArrayByte()`将ROM转换为字节数组
  ~~~java
  RomBuffer buffer = new RomBuffer((RomVersion.getChinese(), null));
  MetalMaxRe metalMaxRe = new MetalMaxRe(buffer);
  metalMaxRe.useDefault(); // 使用默认提供的编辑器管理器实现类
  // ---------------- 注册编辑器
  EditorManagerImpl editorManager = metalMaxRe.getEditorManager();
  editorManager.registerDefaultEditors();
  // ---------------- 加载所有编辑器
  editorManager.loadEditors(); // 加载数据，非阻塞
  // editorManager.loadEditors().get(); // 阻塞当前线程至加载完毕
  // ---------------- 编辑等操作...
  // code... 对编辑器的所有操作
  // ---------------- 应用编辑器的修改和保存
  editorManager.applyEditors(); // 应用修改，非阻塞
  // editorManager.applyEditors().get(); // 阻塞当前线程至应用完毕
  editorManager.save(Path); // 保存修改到指定文件路径
  ~~~

## 继承一个编辑器

如果你只是想在已有的编辑器上添加一些功能  
需要重写定义 ```@Editor.Load``` 和 ```@Editor.Apply```注解到相应的方法中，否则编辑器会不进行任何操作

## 创建一个编辑器

* 继承抽象类 [AbstractEditor](src/main/java/me/afoolslove/metalmaxre/editors/AbstractEditor.java)
  ，或实现接口 [IRomEditor](src/main/java/me/afoolslove/metalmaxre/editors/IRomEditor.java)
* 在创建的编辑器中，创建一个拥有`public`修饰符的任意名称的方法，并使用`@Editor.Load`或`@Editor.Apply`来将其作为**加载数据**和**应用数据**的入口
* 使用`IEditorManager.register(@NotNull Class<E>, @NotNull Function<MetalMaxRe, E>)`注册这个编辑器的类型和实例化这个编辑器方法。
  **_\*注册后会立即实例化\*_**
* 注册完毕后可以使用`EditorManager.getEditor(<Class<E>)`方法获取这个编辑器或其它编辑器的实例

## 事件

### 注册一个事件监听器

1. 实现`java.util.EventListener`接口
2. ~在实现类中添加拥有`public`修饰符的公开方法，参数为被监听的事件（以后可能会取消该限制）
3. 使用`EventHandler.register(@NotNull EventListener)`注册一个事件监听器  
   可以通过`MetalMaxRe.getEventHandler()`获取`EventHandler`实例
   ~~~java
     class TestEventListener implements EventListener {
       public void test(EditorLoadEvent.Pre event) {
         System.out.println(String.format("准备加载编辑器[%s]", event.getEditor().getClass().getSimpleName()));
       }
     
       public void test(EditorLoadEvent.Post event) {
         System.out.println(String.format("加载编辑器[%s]完毕", event.getEditor().getClass().getSimpleName()));
       }
     }
     
     class Main {
       public static void main(String[] args) {
         RomBuffer buffer = new RomBuffer(RomVersion.getChinese(), null);
         MetalMaxRe metalMaxRe = new MetalMaxRe(buffer);
         metalMaxRe.useDefault();
         // ---------------- 注册事件监听器
         TestEventListener eventListener = new TestEventListener();
         metalMaxRe.getEventHandler().register(eventListener);
     
         EditorManagerImpl editorManager = metalMaxRe.getEditorManager();
         editorManager.registerDefaultEditors();
         editorManager.loadEditors();
       }
     }
   ~~~

### 创建事件和通知一个事件的发生

* 继承[Event](src/main/java/me/afoolslove/metalmaxre/event/Event.java)
* 使用`EventHandler.callEvent(@NotNull Event)`通知一个事件的发生  
  可以通过`MetalMaxRe.getEventHandler()`获取`EventHandler`实例

## 字库

通常情况下只有在需要字节与文本互转的情况下才会用到  
**如果没有提供字库，在转换时不会得到目标结果**

### 使用字库[ICharMap](/src/main/java/me/afoolslove/metalmaxre/editors/text/mapping/ICharMap.java)

* 内置中文[CharMapCN](/src/main/java/me/afoolslove/metalmaxre/editors/text/mapping/CharMapCN.java)
  和日文[CharMapJP](/src/main/java/me/afoolslove/metalmaxre/editors/text/mapping/CharMapJP.java)两种默认实现字库
  ~~~java
  RomBuffer buffer = new RomBuffer(RomVersion.getChinese(), null);
  MetalMaxRe metalMaxRe = new MetalMaxRe(buffer);
  
  // 创建中文字库
  ICharMap cn = new CharMapCN();
  // 设置中文字库
  metalMaxRe.setCharMap(cn);
  
  // 创建日文字库
  ICharMap jp = new CharMapJP();
  // 设置日文字库
  metalMaxRe.setCharMap(jp);
  ~~~

### 自定义字库

* 继承[ICharMap](/src/main/java/me/afoolslove/metalmaxre/editors/text/mapping/ICharMap.java)并实现相关方法，可以直接复制稍作修改下面的代码使用

  ~~~java
  import java.util.ArrayList;
  import me.afoolslove.metalmaxre.utils.SingleMapEntry;

  public class CustomCharMap implements ICharMap {
      private static final Logger LOGGER = LoggerFactory.getLogger(CharMapCN.class);

      private final Map<Byte, Integer> opcodes = createDefaultOpcodes();
      private final List<SingleMapEntry<Character, Object>> values;

      public CustomCharMap() {
          // 将字符添加进 values 即可
          values = new ArrayList<>();
          // 添加 0-9 A-Z
          for (byte i = 0; i <= 9; i++) {
              values.add(SingleMapEntry.create((char) ('0' + i), i));
          }
          for (char i = 'A', j = 0x0A; i <= 'Z'; i++) {
              values.add(SingleMapEntry.create(i, (byte) (j + (i - 'A'))));
          }
      }
  
      @Override
      public Map<Byte, Integer> getOpcodes() {
          return opcodes;
      }

      @Override
      public List<SingleMapEntry<Character, Object>> getValues() {
          return values;
      }

      @Override
      @NotNull
      public Object[] getValues(char ch) {
          List<Object> values = new ArrayList<>();
          for (SingleMapEntry<Character, Object> value : getValues()) {
              if (value.getKey() == ch) {
                  values.add(value.getValue());
              }
          }
          return values.toArray();
      }

      @Override
      public @Nullable Object getValue(char ch) {
          for (SingleMapEntry<Character, Object> value : getValues()) {
              if (value.getKey() == ch) {
                  return value.getValue();
              }
          }
          return null;
      }
  }
  ~~~
  此时的`CustomCharMap`拥有除基本文本操作码和0-9A-Z转换的能力，其它字符需要自行添加
