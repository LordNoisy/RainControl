# Rain Control
RainControl is a simple server side Fabric mod which allows the user to configure the time between rain, the duration of rain, the duration of thunder and also the chance of a thunderstorm.

# Config
The config file is found at /config/RainControl/config.acfg with a default configuration of:
```
# The minimum and maximum amount of time you want clear weather to last, in ticks. Defaults equal 3.5-7.5 days
min_clear_weather=72000
max_clear_weather=180000
# The minimum and maximum amount of time you want rainy weather to last, in ticks
min_rainy_weather=1000
max_rainy_weather=18000
# The minimum and maximum amount of time you want thunder to go on for, in ticks
min_thunder_weather=3600
max_thunder_weather=15600
# The percentage chance it will thunder
thunder_chance=20
```

As you can see everything is commented, and should be fairly self explanatory - but if you do need any help don't hesitate to ask me in the GitHub issue tracker. Since values are in ticks it is important to remember that 20 ticks typically is 1 second in real time, and that an in-game day has 24000 ticks.

# Other

Thanks for checking out my mod, hopefully this can provide some use to you :)
