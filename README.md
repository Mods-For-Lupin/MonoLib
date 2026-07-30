Mods-For-Lupin/**MonoLib** is the backing library mod of mods created for [Lupin](https://www.curseforge.com/members/lupin/projects) by [Jason13](https://www.curseforge.com/members/jason13official/projects)

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
