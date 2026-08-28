package somdudewillson.redsparklib.common.utils;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GenericUtilsTest {

    @Mock
    private MobEffectInstance effectInstance;

    @Mock
    private MobEffect mobEffect;

    @Test
    public void testCalculateHealthFromPotionEffectInstantDamage() {
        when(effectInstance.getEffect()).thenReturn(mobEffect);
        when(mobEffect.getDescriptionId()).thenReturn("effect.minecraft.instant_damage");
        when(effectInstance.getAmplifier()).thenReturn(1); // Amplifier 1 (level II)
        when(effectInstance.getDuration()).thenReturn(1);

        // instant_damage formula: -1 * 3 * Math.pow(2, amplifier) * duration = -1 * 3 * 2 * 1 = -6.0
        double healthChange = GenericUtils.calculateHealthFromPotionEffect(effectInstance);
        assertEquals(-6.0, healthChange, 0.001);
    }

    @Test
    public void testCalculateHealthFromPotionEffectInstantHealth() {
        when(effectInstance.getEffect()).thenReturn(mobEffect);
        when(mobEffect.getDescriptionId()).thenReturn("effect.minecraft.instant_health");
        when(effectInstance.getAmplifier()).thenReturn(0); // Amplifier 0 (level I)
        when(effectInstance.getDuration()).thenReturn(1);

        // instant_health formula: 1 * 2 * Math.pow(2, amplifier) * duration = 1 * 2 * 1 * 1 = 2.0
        double healthChange = GenericUtils.calculateHealthFromPotionEffect(effectInstance);
        assertEquals(2.0, healthChange, 0.001);
    }

    @Test
    public void testCalculateHealthFromPotionEffectPoison() {
        when(effectInstance.getEffect()).thenReturn(mobEffect);
        when(mobEffect.getDescriptionId()).thenReturn("effect.minecraft.poison");
        when(effectInstance.getAmplifier()).thenReturn(0);
        when(effectInstance.getDuration()).thenReturn(100);

        // poison formula:
        // ticksPerTrigger = Math.round(25 * Math.pow(2, -0)) = 25
        // healthChange = -1 * 0.5 * (100 / 25) = -2.0
        double healthChange = GenericUtils.calculateHealthFromPotionEffect(effectInstance);
        assertEquals(-2.0, healthChange, 0.001);
    }
}
