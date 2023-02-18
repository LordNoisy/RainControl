package net.lordnoisy.raincontrol;

/*
 * This class is stolen from my good friend JSJBDEV (with permission!), be sure to check out his mods!
 * https://github.com/JSJBDEV/GiveMeHats
 *
 */

import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration {

    public static Map<String,String> config = new HashMap<>();


    public static Map<String,String> loadConfigs()
    {
        File file = new File(FabricLoader.getInstance().getConfigDir().toString() + "/RainControl/config.acfg");
        try {
            List<String> lines = FileUtils.readLines(file,"utf-8");
            lines.forEach(line->
            {
                if(line.charAt(0)!='#')
                {
                    String noSpace = line.replace(" ","");
                    String[] entry = noSpace.split("=");
                    config.put(entry[0],entry[1]);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    public static void generateConfigs(List<String> input)
    {
        File file = new File(FabricLoader.getInstance().getConfigDirectory().getPath() + "/RainControl/config.acfg");

        try {
            FileUtils.writeLines(file,input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String,String> checkConfigs()
    {
        if(new File(FabricLoader.getInstance().getConfigDirectory().getPath() + "/RainControl/config.acfg").exists())
        {
            return loadConfigs();
        }
        generateConfigs(makeDefaults());
        return loadConfigs();
    }

    private static List<String> makeDefaults()
    {
        List<String> defaults = new ArrayList<>();

        defaults.add("# The minimum and maximum amount of time you want clear weather to last, in ticks. Defaults equal 3.5-7.5 days");
        defaults.add("min_clear_weather=72000");
        defaults.add("max_clear_weather=180000");

        defaults.add("# The minimum and maximum amount of time you want rainy weather to last, in ticks");
        defaults.add("min_rainy_weather=1000");
        defaults.add("max_rainy_weather=18000");

        defaults.add("# The minimum and maximum amount of time you want thunder to go on for, in ticks");
        defaults.add("min_thunder_weather=3600");
        defaults.add("max_thunder_weather=15600");

        defaults.add("# The percentage chance it will thunder");
        defaults.add("thunder_chance=20");

        return defaults;
    }

}
