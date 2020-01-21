## whatever

一个放自己的乱七八糟代码的地儿~

主要是一些看 KotlinConf 时用来研究DSL或者字节码什么的代码，还有一些用来研究特性和性能的代码。

2020.1.9：现在还有一些Scala代码啦。在 `main\src\main\scala\cats` 目录下还有一个带不少注释、半笔记半教程的 `Cats` 库的“拆解”吧算是...？（话说 `Cats` 这个库的实现真的是相当漂亮...

~~所以肯定是没什么质量的啦~~

很多代码放在 `test` 目录下。（就是我把 JUnit 当成了多个 psvm 的意思（逃））

2019.7.27：~~这还是一个很好的 Gradle 5 multi-project 示范（雾~~

2020.1.21: 大型重构。所有benchmark放`perf`这个独立项目里边，每个benchmark同样是这个独立项目里边的独立子项目，并且通过`shadow`达成`fatJar`的打包。在该结构下新增`NewInstPerfTest`（可能会有一篇相关的分析文章..... 然而我又懒又垃圾......（（（（大哭），未来可能还会有 `RegexPerfTest`。

### 其他的一些东西

- 我的PowerShell profile，包含一些很好用的自定义指令：[GitHub Gist - Ray-Eldath / Microsoft.PowerShell_profile.ps1](https://gist.github.com/Ray-Eldath/961824f60e4f7c55f9331f866fefafa8)