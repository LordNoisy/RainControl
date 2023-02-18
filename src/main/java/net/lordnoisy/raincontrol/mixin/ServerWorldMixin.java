package net.lordnoisy.raincontrol.mixin;

import net.lordnoisy.raincontrol.Configuration;
import net.lordnoisy.raincontrol.RainControl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.level.ServerWorldProperties;
import org.apache.logging.log4j.core.jmx.Server;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

import java.util.Map;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

	Map<String, String> config = Configuration.checkConfigs();
	Random random = new Random();

	ServerWorld serverWorld = (ServerWorld) (Object) this;

	@Shadow
	ServerWorldProperties worldProperties;



	private int minTimeUntilRain = Integer.valueOf(config.get("min_clear_weather"));
	private int maxTimeUntilRain = Integer.valueOf(config.get("max_clear_weather"));
	private int minTimeRainTime = Integer.valueOf(config.get("min_rainy_weather"));
	private int maxTimeRainTime = Integer.valueOf(config.get("max_rainy_weather"));
	private int minThunderTime = Integer.valueOf(config.get("min_thunder_weather"));
	private int maxThunderTime = Integer.valueOf(config.get("max_thunder_weather"));
	private int thunderChance = Integer.valueOf(config.get("thunder_chance"));

	private int clearWeatherTimer = this.random.nextInt(this.minTimeUntilRain, this.maxTimeUntilRain + 1);
	private int rainWeatherTimer = this.random.nextInt(this.minTimeRainTime, this.maxTimeRainTime + 1);


	@Inject(method = "tickWeather()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ServerWorldProperties;setThunderTime(I)V", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
	private void tickWeather(CallbackInfo ci, boolean bl, int i, int j, int k, boolean bl2, boolean bl3) {
		if(worldProperties.getLevelName().equals("world")) {
			//Pick if it should thunder
			boolean thunder = false;
			if (random.nextInt(0, 101) < thunderChance) {
				thunder = true;
			}

			//Set thunder time (default MC values for now)
			int thunderTime;
			if (thunder) {
				//Set the time until it stops thundering
				thunderTime = random.nextInt(minThunderTime, maxThunderTime);
			} else {
				//Minecraft's default behavior is to set this to the same range as the clear rain period, so I'm doing that :)
				thunderTime = this.random.nextInt(this.minTimeUntilRain, this.maxTimeUntilRain + 1);
			}

			//Determine the rain time
			boolean isRaining = serverWorld.getLevelProperties().isRaining();
			if (rainWeatherTimer > 0) {
				--rainWeatherTimer;
				if (rainWeatherTimer <= 0) {
					isRaining = !isRaining;
				}
			} else if (isRaining) {
				//If it is raining we want the timer to be lower so that it doesn't rain too long
				this.setRainWeatherTimer();
			} else {
				//If it isn't raining we want the timer to higher, so that it's sunny for long time
				this.setClearWeatherTimer();
			}

			//Set all the values
			j = thunderTime;
			k = rainWeatherTimer;
			i = clearWeatherTimer;
			bl2 = thunder;
			bl3 = isRaining;
		}
	}

	private void setClearWeatherTimer() {
		this.rainWeatherTimer = this.random.nextInt(this.minTimeUntilRain, this.maxTimeUntilRain + 1);
	}

	private void setRainWeatherTimer() {
		this.clearWeatherTimer = this.random.nextInt(this.minTimeRainTime, this.maxTimeRainTime + 1);
	}

}
