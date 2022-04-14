# MetalMaxRe

## 创建一个编辑器

* 继承抽象类 [AbstractEditor](src/main/java/me/afoolslove/metalmaxre/editors/AbstractEditor.java)
  ，或实现接口 [IRomEditor](/src/main/java/me/afoolslove/metalmaxre/editors/IRomEditor.java)
* 在创建的编辑器中，创建一个拥有`public`修饰符的任意名称的方法，并使用`@Editor.Load`或`@Editor.Apply`来将其作为**加载数据**和**应用数据**的入口
* 使用`EditorManager.register(@NotNull Class<E>, @NotNull Function<MetalMaxRe, E>)`注册这个编辑器的类型和实例化这个编辑器方法。**_
  \*注册后会立即实例化\*_**
* 注册完毕后可以使用`EditorManager.getEditor(<Class<E>)`方法获取这个编辑器或其它编辑器的实例
* ...