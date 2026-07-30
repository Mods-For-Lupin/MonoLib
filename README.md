Mods-For-Lupin/**New-Mod-Template** is a customized implementation of [MultiLoader-Template](https://github.com/jaredlll08/MultiLoader-Template) for writing multi-loader (Fabric, (Neo)Forge) compatible mods, and supports exporting merged JAR files via a `mergeJarsCustom` task based on [Forgix](https://github.com/PacifistMC/Forgix)

- [1.20.1](https://github.com/Mods-For-Lupin/MonoLib/tree/1.20.1)
- [1.21.1](https://github.com/Mods-For-Lupin/MonoLib/tree/1.21.1)
- [26.1.2](https://github.com/Mods-For-Lupin/MonoLib/tree/26.1.2)

### How I'm Using MonoLib

In `.\buildSrc\src\main\groovy\multiloader-common.gradle`:

```groovy
    exclusiveContent {
      forRepository { ivy {
        url 'https://github.com/Mods-For-Lupin/MonoLib/releases/download'
        patternLayout {
          artifact 'common-[revision]/[artifact]-[revision](-[classifier]).jar'
        }
        metadataSources { artifact() }
      } }
      filter { includeGroup 'monolib' }
    }
```

Allows this in your `.\common\build.gradle`'s `dependencies` block:

```groovy
    implementation 'monolib:monolib-common:1.20.1-4.1.0'
```
