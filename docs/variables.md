# How to write a variable?

1. 
```
 type            variable name
/               /
scoreboard set hp 10 <-- value
            \
             set value
```

2. 
```python
# Same thing but `ad` changes the value
scoreboard add hp -5
```

* Moreover, '-' here means subtraction, not a signed number

## if-else

``` python
# Initialize a variable named `hp`
scoreboard set hp 100

execute if score hp matches 100 run say {"Player hp: ", $hp}
```
1. `score` - points to a variable
2. `matches` - Condition
2. `run` - execute the command if the condition is true

* Moreover, `if` always requires `run` even if the condition is not met
* If `run` is not present, the following error will be thrown:
```
Runtime error: String 4: Expected 'run'
```

## With `else` also
```python
# Initialize a variable named `hp`
scoreboard set hp 100
# Changing the value
scoreboard add hp -50

execute if score hp matches 0 run say {"Player hp: ", $hp} else run say {"This is else"}
```

* __Outputs:__ This is else