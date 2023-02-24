package net.lordnoisy.raincontrol.mixin;

import net.lordnoisy.raincontrol.Configuration;
import net.lordnoisy.raincontrol.RainControl;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.level.ServerWorldProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

import java.util.Map;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

	Map<String, String> config = Configuration.checkConfigs();
	Random random = new Random();

	@Shadow
	ServerWorldProperties worldProperties;

	private int minTimeUntilRain = Integer.valueOf(config.get("min_clear_weather"));
	private int maxTimeUntilRain = Integer.valueOf(config.get("max_clear_weather"));
	private int minRainDuration = Integer.valueOf(config.get("min_rainy_weather"));
	private int maxRainDuration = Integer.valueOf(config.get("max_rainy_weather"));
	private int minThunderTime = Integer.valueOf(config.get("min_thunder_weather"));
	private int maxThunderTime = Integer.valueOf(config.get("max_thunder_weather"));
	private int minClearThunderTime = Integer.valueOf(config.get("min_clear_thunder"));
	private int maxClearThunderTime = Integer.valueOf(config.get("max_clear_thunder"));

	@Redirect(method = "tickWeather",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/math/MathHelper;nextBetween(Lnet/minecraft/util/math/random/Random;II)I",
					ordinal = 0
			),
			slice = @Slice(
					from = @At(value = "CONSTANT", args = "intValue=24000", shift = At.Shift.AFTER)
			)
	)
	private int redirectNextBetweenRainDuration(net.minecraft.util.math.random.Random random, int min, int max){
		if (worldProperties.getLevelName().equals("world")) {
			return this.setRainWeatherTimer();
		} else {
			return random.nextBetween(min, max);
		}
	}

	@Redirect(method = "tickWeather",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/math/MathHelper;nextBetween(Lnet/minecraft/util/math/random/Random;II)I",
					ordinal = 1
			),
			slice = @Slice(
					from = @At(value = "CONSTANT", args = "intValue=24000", shift = At.Shift.AFTER)
			)
	)
	private int redirectNextBetweenClearDuration(net.minecraft.util.math.random.Random random, int min, int max){
		if (worldProperties.getLevelName().equals("world")) {
			return this.setClearWeatherTimer();
		} else {
			return random.nextBetween(min, max);
		}
	}

	@Redirect(method = "tickWeather",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/math/MathHelper;nextBetween(Lnet/minecraft/util/math/random/Random;II)I",
					ordinal = 0
			),
			slice = @Slice(
					from = @At(value = "CONSTANT", args = "intValue=15600", shift = At.Shift.AFTER)
			)
	)
	private int redirectNextBetweenThunderDuration(net.minecraft.util.math.random.Random random, int min, int max){
		if (worldProperties.getLevelName().equals("world")) {
			return this.setThunderTimer();
		} else {
			return random.nextBetween(min, max);
		}
	}

	@Redirect(method = "tickWeather",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/math/MathHelper;nextBetween(Lnet/minecraft/util/math/random/Random;II)I",
					ordinal = 1
			),
			slice = @Slice(
					from = @At(value = "CONSTANT", args = "intValue=15600", shift = At.Shift.AFTER)
			)
	)
	private int redirectNextBetweenClearThunderDuration(net.minecraft.util.math.random.Random random, int min, int max){
		if (worldProperties.getLevelName().equals("world")) {
			return this.setClearThunderTimer();
		} else {
			return random.nextBetween(min, max);
		}
	}

	private int setClearWeatherTimer() {
		return this.random.nextInt(this.minTimeUntilRain, this.maxTimeUntilRain + 1);
	}

	private int setRainWeatherTimer() {
		return this.random.nextInt(this.minRainDuration, this.maxRainDuration + 1);
	}

	private int setClearThunderTimer() {
		return this.random.nextInt(this.minClearThunderTime, this.maxClearThunderTime + 1);
	}

	private int setThunderTimer() {
		return this.random.nextInt(this.minThunderTime, this.maxThunderTime + 1);
	}

}
