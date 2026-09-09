# MCLang

## What is it?

__MCLang__ is an interpreted programming language that uses __Minecraft__ commands as keywords.

## Is it possible to write a simple program in it?

Uhmm... __NO__

# CLI

```bash
# Lang version
mclang --version
mclang -v

# Run File
mclang run <file.mcl>
```

# How to install?

> I'm assuming you already have Java 21 or higher installed.

Just go to releases, download the archive, unzip it and add the path to the new folder to your PATH

# Building from source
> Why do you need this?

> [!WARNING]
> This guide is for building a .jar file, not an executable program. To run commands like "mclang -v," you'll need a script (.sh, .bat) and a path to the PATH variable.
>

Please note that the following instructions assume you are using Windows.
* Make sure you have Java 21 or higher. If not, download it here: [Adoptium](https://adoptium.net/temurin/releases?version=21&os=any&arch=any)
* Install Git (if you don't have it): [Git](https://git-scm.com/install/windows)
1. Open Git Bash:
 Press the Windows key, type "Git Bash," and press ENTER
 Enter the directory where you want to store your source code
```bash
cd $HOME/Downloads
```

2. Clone the repository
```bash
git clone https://github.com/DvHume/MCLang.git
```
3. Change to the source code directory
```bash
cd McLang
```
4. Run the build
```bash
./mvnw clean package
```
5. Locate the source file (usually the target/ folder)

LICENSE [GPL-3.0](LICENSE)

> [!NOTE]
>
> This is a study project, feel free to scold me for any reason, I will not read it. Thanks!