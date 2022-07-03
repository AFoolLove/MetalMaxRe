# MetalMaxRe

## 创建一个MetalMaxRe实例

1. 创建用于操作字节的[RomBuffer(@NotNull RomVersion, @Nullable Path)](src/main/java/me/afoolslove/metalmaxre/RomBuffer.java)
2. 然后创建[MetalMaxRe(@NotNull RomBuffer)](src/main/java/me/afoolslove/metalmaxre/MetalMaxRe.java)，将`RomBuffer`实例传入就完成了

~~~
RomBuffer buffer = new RomBuffer((RomVersion.getChinese(), null));
MetalMaxRe metalMaxRe = new MetalMaxRe(buffer);
metalMaxRe.useDefault(); // 该方法会使用默认提供的类实现一些功能
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

~~~
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

* 使用`IEditorManager.applyEditor(@NotNull Class<IRomEditor>)`应用指定编辑器的修改
* 使用`IEditorManager.applyEditors()`应用所有编辑器的修改
* 使用`RomBuffer.save(@NotNull Path)`保存到指定路径
* 使用`RomBuffer.save(@NotNull OutputStream)`保存到输出流
* 使用`RomBuffer.toArrayByte()`将ROM转换为字节数组

~~~
RomBuffer buffer = new RomBuffer((RomVersion.getChinese(), null));
MetalMaxRe metalMaxRe = new MetalMaxRe(buffer);
metalMaxRe.useDefault(); // 使用默认提供的编辑器管理器实现类
// ---------------- 注册编辑器
EditorManagerImpl editorManager = metalMaxRe.getEditorManager();
editorManager.registerDefaultEditors();
// ---------------- 加载所有编辑器
editorManager.loadEditors();
// editorManager.loadEditors().get(); // 等待加载完毕
// ---------------- 编辑
// code... 对编辑器的所有操作
// ---------------- 应用编辑器的修改和保存
editorManager.applyEditors();
// editorManager.applyEditors().get(); // 等待应用完毕
editorManager.save(Path);

~~~

## 继承一个编辑器

如果你只是想在已有的编辑器上添加一些功能  
需要重写定义 ```@Editor.Load``` 和 ```@Editor.Apply```注解到相应的方法中

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